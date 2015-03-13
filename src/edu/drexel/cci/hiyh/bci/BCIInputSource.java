package edu.drexel.cci.hiyh.bci;

import edu.drexel.cci.hiyh.ui.AbstractBooleanInputSource;
import edu.drexel.cci.hiyh.ui.InputUI;

public class BCIInputSource extends AbstractBooleanInputSource {
    private final BCICollector collector = new BCICollector();

    @Override
    public void initAndStart(InputUI ui) {
        ui.showMessage("<html><center>Calibrating.<br>Think a neutral thought.</center></html>");
        while (true) {
            try {
                collector.getSignalDetector().getCalibratedWhenTrue();
                break;
            } catch (InterruptedException e) {}
        }

        Thread t = new Thread(this::loop);
        t.setDaemon(true);
        t.start();
    }

    private void loop() {
        while (true) {
            try {
                collector.getSignalWhenTrue();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            notifyListeners();
        }
    }
}
