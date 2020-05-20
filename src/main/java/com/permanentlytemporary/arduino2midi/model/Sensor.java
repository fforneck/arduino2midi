package com.permanentlytemporary.arduino2midi.model;

public abstract class Sensor {

    protected byte index = 0;
    protected String name = null;
    protected boolean enabled = false;
    protected byte channel = 0;
    protected byte note = 0;
    protected int duration = 0;

    public Sensor() {
    }

    public Sensor(byte index, String name, boolean enabled, byte channel, byte note, int duration) {
        this.index = index;
        this.name = name;
        this.enabled = enabled;
        this.channel = channel;
        this.note = note;
        this.duration = duration;
    }

    public byte getIndex() {
        return index;
    }
    
    public void setIndex(byte index) {
        this.index = index;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public byte getChannel() {
        return channel;
    }

    public void setChannel(byte channel) {
        this.channel = channel;
    }

    public byte getNote() {
        return note;
    }

    public void setNote(byte note) {
        this.note = note;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "Sensor [index=" + index + 
                ", name=" + name + 
                ", enabled=" + enabled + 
                ", channel=" + channel + 
                ", note=" + note + 
                ", duration=" + duration + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + index;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Sensor other = (Sensor) obj;
        if (index != other.index)
            return false;
        return true;
    }
    
}