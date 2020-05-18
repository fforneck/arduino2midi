package com.permanentlytemporary.arduino2midi;

import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        try {
            System.out.println( "Hello World!" );
            System.out.println(SerialPort.getCommPorts());
            for (SerialPort serialPort : SerialPort.getCommPorts()) {
                
                System.out.println(serialPort.getSystemPortName());
                System.out.println(serialPort.getDescriptivePortName());
                System.out.println(serialPort.getPortDescription());
                System.out.println(serialPort.getBaudRate());
            }
    
            SerialPort serialPort = SerialPort.getCommPort("COM3");
            serialPort.openPort();
            serialPort.setBaudRate(57600);
            serialPort.addDataListener(new SerialPortDataListener() {
                @Override
                public int getListeningEvents() { return SerialPort.LISTENING_EVENT_DATA_AVAILABLE; }
                @Override
                public void serialEvent(SerialPortEvent event)
                {
                   if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE)
                      return;
                   byte[] newData = new byte[serialPort.bytesAvailable()];
                   int numRead = serialPort.readBytes(newData, newData.length);
                   System.out.println("Read " + numRead + " bytes.");
                   System.out.println(newData[0]);
                   System.out.println(newData[1]);
                   System.out.println(newData[2]);
                }
            });
    
            Sequencer sequencer = MidiSystem.getSequencer();
            sequencer.open();
            Sequence sequence = new Sequence(Sequence.PPQ, 4);
            Track track = sequence.createTrack();
            track.add(makeEvent(144, 1, 48, 100, 48));
            track.add(makeEvent(128, 1, 48, 100, 50));
            sequencer.setSequence(sequence);
            sequencer.setTempoInBPM(220);
            sequencer.start();
  
            while (true) { 
  
                // Exit the program when sequencer has stopped playing. 
                if (!sequencer.isRunning()) { 
                    sequencer.close(); 
                    System.exit(1); 
                } 
            } 

            
        } catch (Exception e) {
            System.err.println("Error");
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }
  
    public static MidiEvent makeEvent(int command, int channel, 
                               int note, int velocity, int tick) 
    { 
  
        MidiEvent event = null; 
  
        try { 
  
            // ShortMessage stores a note as command type, channel, 
            // instrument it has to be played on and its speed. 
            ShortMessage a = new ShortMessage(); 
            a.setMessage(command, channel, note, velocity); 
  
            // A midi event is comprised of a short message(representing 
            // a note) and the tick at which that note has to be played 
            event = new MidiEvent(a, tick); 
        } 
        catch (Exception ex) { 
  
            ex.printStackTrace(); 
        } 
        return event; 
    } 
}
