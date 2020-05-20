package com.permanentlytemporary.arduino2midi.model;

public class DigitalSensor extends Sensor {

    private byte velocity = 0;
    private byte velocityRandomization = 0;

    public DigitalSensor(byte index, String name, boolean enabled, byte channel, byte note, int duration, byte velocity,
            byte velocityRandomization) {
        super(index, name, enabled, channel, note, duration);
        this.velocity = velocity;
        this.velocityRandomization = velocityRandomization;
    }

    public byte getVelocity() {
        return velocity;
    }

    public void setVelocity(byte velocity) {
        this.velocity = velocity;
    }

    public byte getVelocityRandomization() {
        return velocityRandomization;
    }

    public void setVelocityRandomization(byte velocityRandomization) {
        this.velocityRandomization = velocityRandomization;
    }

    @Override
    public String toString() {
        return "DigitalSensor [index=" + index + 
                ", name=" + name + 
                ", enabled=" + enabled + 
                ", channel=" + channel + 
                ", note=" + note + 
                ", duration=" + duration + 
                ", velocity=" + velocity + 
                ", velocityRandomization=" + velocityRandomization + "]";
    }

}