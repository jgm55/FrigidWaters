package edu.drexel.cci.hiyh.ui;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.SwingUtilities;

public class MouseInputSource extends AbstractBooleanInputSource {
    private final MouseListener ml = new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    notifyListeners();
                }
            };

    public void initAndStart(InputUI ui) {
        Component glassPane = ui.getGlassPane();
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                glassPane.addMouseListener(ml);
                glassPane.setVisible(true);
            }
        });
    }
}
