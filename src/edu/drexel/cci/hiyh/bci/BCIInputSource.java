package edu.drexel.cci.hiyh.bci;

import edu.drexel.cci.hiyh.ui.AbstractBooleanInputSource;

public class BCIInputSource extends AbstractBooleanInputSource {
    private final BCICollector collector = new BCICollector();

    public BCIInputSource() {
        collector.start();
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
