package com.permanentlytemporary.arduino2midi.view;

import javax.swing.JPanel;

import com.permanentlytemporary.arduino2midi.model.AnalogSensor;
import com.permanentlytemporary.arduino2midi.model.Sensor;

public class AnalogSensorPanel extends JPanel {

    private static final long serialVersionUID = -1998293513542361006L;
    
    private byte index = 0;
    private String name = null;
    private boolean enabled = false;
    private byte channel = 0;
    private byte note = 0;
    private int duration = 0;
    private int sensitivity = 0;

    public AnalogSensorPanel(byte index, String name) {
        super();
        this.index = index;
        this.name = name;
    }

    public AnalogSensor getSensor() {
        return new AnalogSensor(index, name, enabled, channel, note, duration, sensitivity);
    }
    
}