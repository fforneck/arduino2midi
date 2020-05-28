package com.permanentlytemporary.arduino2midi.view;

import java.awt.Container;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class PlotterForm {

    private JFrame frame = new JFrame("Plotter");
    private JLabel[] analogSensorLabels = new JLabel[16];
    private JLabel[] digitalSensorLabels = new JLabel[54];

    public PlotterForm() {
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.setSize(400, 400);
        Container pane = frame.getContentPane();
        pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
        
        // The analog pane
        JPanel analogPanel = new JPanel(new GridLayout(2, 8));
        analogPanel.setBorder(BorderFactory.createTitledBorder("Analog Sensors"));
        for (int i = 0; i < analogSensorLabels.length; i++) {
        	analogSensorLabels[i] = new JLabel("A" + i + ":");
        	analogPanel.add(analogSensorLabels[i]);
		}
        pane.add(analogPanel);
        
        // The digital pane
        JPanel digitalPanel = new JPanel(new GridLayout(9, 6));
        digitalPanel.setBorder(BorderFactory.createTitledBorder("Analog Sensors"));
        for (int i = 0; i < digitalSensorLabels.length; i++) {
        	digitalSensorLabels[i] = new JLabel("D" + i + ":");
        	digitalPanel.add(digitalSensorLabels[i]);
		}
        pane.add(digitalPanel);
    }
    
    public void show() {
    	frame.setVisible(true);
    }

    public void updateValues(Integer[] analogSensors, Integer[] digitalSensors) {
    	for (int i = 0; i < analogSensorLabels.length; i++) {
    		analogSensorLabels[i].setText("A" + i + ": " + analogSensors[i]);
		}
    	for (int i = 0; i < digitalSensorLabels.length; i++) {
    		digitalSensorLabels[i].setText("A" + i + ": " + digitalSensors[i]);
    	}
    }
}