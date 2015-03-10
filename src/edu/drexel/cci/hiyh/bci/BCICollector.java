package edu.drexel.cci.hiyh.bci;

import java.util.ArrayList;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;


public class BCICollector extends Thread {
	private boolean alive = true;
	private Boolean monitor = true;
	private Boolean signal = false;
	private long storedTime = 0;
	private SignalDetector sd = new SignalDetector();
	
	public BCICollector() {

	}
	
	public SignalDetector getSignalDetector() {
		return sd;
	}

	public boolean getSignal() {
		synchronized(monitor) {
			boolean sig = signal;
			signal = false;
			return sig;
		}
	}

	public boolean getSignalWhenTrue() throws InterruptedException {
		synchronized(monitor){	
			while(!signal) {
				monitor.wait();
			}
			signal = false;
			return true;
		}
	}

	public boolean getSignalWhenTrue(long timeout) throws InterruptedException {
		synchronized(monitor){
			while(!signal) {
				monitor.wait(timeout);
			}
			signal = false;
			return true;
		}
	}

	private void setSignal(boolean sig) {
		long now = System.currentTimeMillis();
		if (now - storedTime > 3000) {
			synchronized(monitor){
				signal = sig;
				if (signal) {
					monitor.notifyAll();
				}
			}
			storedTime = now;
		}
	}

	public void stopSampling() {
		alive=false;
	}

	@Override
	public void run() {
		Pointer eEvent = Edk.INSTANCE.EE_EmoEngineEventCreate();
		Pointer eState = Edk.INSTANCE.EE_EmoStateCreate();
		IntByReference userID = null;
		IntByReference nSamplesTaken = null;
		IntByReference gx = null;
		IntByReference gy = null;
		int state = 0;
		float secs = 1;
		boolean readytocollect = false;

		userID = new IntByReference(0);
		nSamplesTaken = new IntByReference(0);

		if (Edk.INSTANCE.EE_EngineConnect("Emotiv Systems-5") != EdkErrorCode.EDK_OK
				.ToInt()) {
			System.out.println("Emotiv Engine start up failed.");
			return;
		}

		Pointer hData = Edk.INSTANCE.EE_DataCreate();
		Edk.INSTANCE.EE_DataSetBufferSizeInSec(secs);

		while (alive) {
			state = Edk.INSTANCE.EE_EngineGetNextEvent(eEvent);

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
				System.out.println("Internal error in Emotiv Engine!");
				break;
			}

			if (readytocollect) {
				Edk.INSTANCE.EE_DataUpdateHandle(0, hData);

				Edk.INSTANCE.EE_DataGetNumberOfSample(hData, nSamplesTaken);
				Edk.INSTANCE.EE_HeadsetGetGyroDelta(0, gx, gy);
				if (nSamplesTaken != null) {
					if (nSamplesTaken.getValue() != 0) {
						double[][] combinedData = new double[14][nSamplesTaken.getValue()];
						double[] col = new double[nSamplesTaken.getValue()];
						for (int sampleIdx = 0; sampleIdx < nSamplesTaken
								.getValue(); ++sampleIdx) {
							for (int i = 0; i < 14; i++) {

								Edk.INSTANCE.EE_DataGet(hData, i, col,
										nSamplesTaken.getValue());
								for(int j = 0; j< col.length;j++) {
									combinedData[i][j]=col[j];
								}

							}
						}
						setSignal(sd.process(combinedData));				
					}
				}
			}
		}

		Edk.INSTANCE.EE_EngineDisconnect();
		Edk.INSTANCE.EE_EmoStateFree(eState);
		Edk.INSTANCE.EE_EmoEngineEventFree(eEvent);
	}
}
