package edu.drexel.cci.hiyh.ui;

import java.awt.Container;
import java.awt.Dimension;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;


public class CalibrationUI{

    private BooleanInputSource inputsrc;
    private final JFrame frame = new JFrame("HIYH");
    private final JPanel calibrationPanel = new JPanel();

    public CalibrationUI() {
        this.inputsrc = inputsrc;
        try {
            SwingUtilities.invokeAndWait(this::buildUI);
        } catch (InterruptedException | InvocationTargetException e) {
            // FIXME
            e.printStackTrace();
        }
    }

    private void buildUI() {
//        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setPreferredSize(new Dimension(400,200));
    	calibrationPanel.add(new JLabel("Think neutral thought"));
        calibrationPanel.setVisible(true);

        switchToCalibrationPanel();

        frame.setVisible(true);
    }

    
    public void dismissUI(){
    	frame.dispose();
    }

    private void switchTo(Container pane) {
        frame.setContentPane(pane);
        frame.pack();
    }

    private void switchToCalibrationPanel() {
        switchTo(calibrationPanel);
    }

    // Consumer-thing so we can chain it in select and get
    private <T> void switchToCalibrationPanel(T unused) {
    	switchToCalibrationPanel();
    }
}
