package edu.drexel.cci.hiyh.ui;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MouseInputSource extends AbstractBooleanInputSource {
    private MouseListener ml = new MouseAdapter() {
                public void mouseClick(MouseEvent e) {
                    notifyListeners();
                }
            };

    public void addListener(BooleanInputSource.Listener l) {
        if (l instanceof Component) {
            super.addListener(l);
            ((Component)l).addMouseListener(ml);
        } else {
            throw new IllegalArgumentException("Listener must be a Component. I'm so sorry.");
        }
    }
    
    public void removeListener(BooleanInputSource.Listener l) {
        if (l instanceof Component) {
            super.removeListener(l);
            ((Component)l).removeMouseListener(ml);
        } else {
            throw new IllegalArgumentException("Listener must be a Component. I'm so sorry.");
        }
    }
}
