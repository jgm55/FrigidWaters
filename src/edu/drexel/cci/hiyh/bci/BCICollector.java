package edu.drexel.cci.hiyh.bci;

import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;

/**
 * Interfaces with the EDK to collect data upon request.
 */
public class BCICollector extends Thread implements AutoCloseable {
    private final Pointer hData;

    public BCICollector() {
        if (Edk.INSTANCE.EE_EngineConnect("Emotiv Systems-5") != EdkErrorCode.EDK_OK
                .ToInt()) {
            System.err.println("Emotiv Engine start up failed.");
            // TODO exception?
            hData = null;
            return;
        }

        // We seem to need to enable data acquisition per userid.
        // To get a userid, we wait for a "user added" event and grab it from
        // there.
        // TODO Can we just pass 0 as a userid, like we can for
        // DataUpdateHandle?
        IntByReference userID = new IntByReference(0);
        Pointer eEvent = Edk.INSTANCE.EE_EmoEngineEventCreate();
        boolean readytocollect = false;
        do {
            int state = Edk.INSTANCE.EE_EngineGetNextEvent(eEvent);

            // New event needs to be handled
            if (state == EdkErrorCode.EDK_OK.ToInt()) {
                int eventType = Edk.INSTANCE.EE_EmoEngineEventGetType(eEvent);
                Edk.INSTANCE.EE_EmoEngineEventGetUserId(eEvent, userID);

                // Log the EmoState if it has been updated
                if (eventType == Edk.EE_Event_t.EE_UserAdded.ToInt())
                    if (userID != null) {
                        Edk.INSTANCE.EE_DataAcquisitionEnable(
                                userID.getValue(), true);
                        readytocollect = true;
                    }
            } else if (state != EdkErrorCode.EDK_NO_EVENT.ToInt()) {
                System.err.println("Internal error in Emotiv Engine!");
                break;
            }
        } while (!readytocollect);
        Edk.INSTANCE.EE_EmoEngineEventFree(eEvent);

        // Create our data buffer handle
        hData = Edk.INSTANCE.EE_DataCreate();
        Edk.INSTANCE.EE_DataSetBufferSizeInSec(1);
    }

    /**
     * Collect available data from the device.
     *
     * @return 2d array of sensor data. First index is over the samples (i.e.
     *         time steps), second is over the available sensors.
     */
    public double[][] getData() {
        //IntByReference gx = null;
        //IntByReference gy = null;
        //Edk.INSTANCE.EE_HeadsetGetGyroDelta(0, gx, gy);

        IntByReference nSamplesTaken = new IntByReference(0);
        Edk.INSTANCE.EE_DataUpdateHandle(0, hData);
        Edk.INSTANCE.EE_DataGetNumberOfSample(hData, nSamplesTaken);

        double[][] combinedData = new double[nSamplesTaken.getValue()][14];

        // We have to query for all of the data per sensor and transpose the
        // resulting data so that the first index is to the sample and the
        // second to the sensor.
        if (nSamplesTaken.getValue() > 0) {
            double[] col = new double[nSamplesTaken.getValue()];
            for (int i = 0; i < 14; i++) {
                Edk.INSTANCE.EE_DataGet(hData, i, col, nSamplesTaken.getValue());
                for(int j = 0; j < col.length;j++)
                    combinedData[j][i]=col[j];
            }
        }

        return combinedData;
    }

    @Override
    public void close() {
        Edk.INSTANCE.EE_EngineDisconnect();
        Edk.INSTANCE.EE_DataFree(hData);
    }
}
