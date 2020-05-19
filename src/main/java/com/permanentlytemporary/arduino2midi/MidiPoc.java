package com.permanentlytemporary;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.MidiDevice.Info;

public class MidiPoc {
    public static void main(String[] args) {
        try {
            Info info = null;
            for (MidiDevice.Info deviceInfo : MidiSystem.getMidiDeviceInfo()) {
                MidiDevice device = MidiSystem.getMidiDevice(deviceInfo);
                System.out.println(deviceInfo.getName() + " | " + deviceInfo.getDescription() + " | " + deviceInfo.getVendor() + " | " + deviceInfo.getVersion() + " | receivers " + device.getMaxReceivers() + " | " + device.getMaxTransmitters() + " | " + device.getMicrosecondPosition());
                if ("LoopBe Internal MIDI".equals(deviceInfo.getName()) && "External MIDI Port".equals(deviceInfo.getDescription())) {
                    info = deviceInfo;
                }
            }
            
            if (info != null) {
                
                MidiDevice device = MidiSystem.getMidiDevice(info);
                device.open();
                Receiver rcvr = device.getReceiver();
                System.out.println("Playing note on " + rcvr + "...");
                // Start playing on channel 0
                // the note Middle C (60), 
                // moderately loud (velocity = 93).
                rcvr.send(new ShortMessage(ShortMessage.NOTE_ON, 0, 60, 93), -1);
                Thread.sleep(500);
                rcvr.send(new ShortMessage(ShortMessage.NOTE_OFF, 0, 60, 0), -1);
                rcvr.close();
            }

        } catch (Exception e) {
            System.out.println("Error:" + e.getMessage());
            e.printStackTrace();
        }
    }
}
