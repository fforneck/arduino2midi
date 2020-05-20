package com.permanentlytemporary.arduino2midi.model;

public class AnalogSensor extends Sensor {

    private int sensitivity = 0;

    public AnalogSensor(byte index, String name, boolean enabled, byte channel, byte note, int duration, int sensitivity) {
        super(index, name, enabled, channel, note, duration);
        this.sensitivity = sensitivity;
    }

    public int getSensitivity() {
        return sensitivity;
    }

    public void setSensitivity(int sensitivity) {
        this.sensitivity = sensitivity;
    }

    @Override
    public String toString() {
        return "AnalogSensor [index=" + index + 
        ", name=" + name + 
        ", enabled=" + enabled + 
        ", channel=" + channel + 
        ", note=" + note +
        ", duration=" + duration + 
        ", sensitivity=" + sensitivity + "]";
    }
    
}