package com.permanentlytemporary.arduino2midi;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Receiver;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

/**
 * Hello world!
 *
 */
public class App 
{

    private static final Integer[] POSSIBLE_BAUD_RATES = {300, 600, 1200, 2400, 4800, 9600, 14400, 19200, 28800, 38400, 57600, 115200};
    private SerialPort serialPort = null;
    private int baudRate = 57600;
    private Receiver receiver = null;
    private Map<Integer, JMenuItem> baudRateMenuMap = new HashMap<Integer, JMenuItem>();
    private Map<String, MidiDevice> midiDeviceMap = new HashMap<String, MidiDevice>();

    public static void main( String[] args ) {

        App app = new App();

    }

    private void addBaudRateSubMenu(JMenu baudRateMenu, Integer[] rates) {
        for (Integer rate : rates) {
            JRadioButtonMenuItem menuItem = new JRadioButtonMenuItem(rate.toString());
            baudRateMenu.add(menuItem);
            baudRateMenuMap.put(rate, menuItem);
        }
    }

    public App() {
        try {
            JFrame frame = new JFrame("Arduino to MIDI");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400,400);

            //Creating the MenuBar and adding components
            JMenuBar menuBar = new JMenuBar();
            JMenu portMenu = new JMenu("Port");
            JMenu baudRateMenu = new JMenu("Baud rate");
            JMenu midiDeviceMenu = new JMenu("MIDI device");
            menuBar.add(portMenu);
            menuBar.add(baudRateMenu);
            menuBar.add(midiDeviceMenu);
            addBaudRateSubMenu(baudRateMenu, POSSIBLE_BAUD_RATES);

            // Text Area at the Center
            JTextArea ta = new JTextArea();

            //Creating the panel at bottom and adding components
            JPanel panel = new JPanel(); // the panel is not visible in output
            JLabel label = new JLabel("Enter Text");
            JTextField tf = new JTextField(10); // accepts upto 10 characters
            JButton send = new JButton("Send");
            JButton reset = new JButton("Reset");
            panel.add(label); // Components Added using Flow Layout
            panel.add(tf);
            panel.add(send);
            panel.add(reset);

            //Adding Components to the frame.
            frame.getContentPane().add(BorderLayout.NORTH, menuBar);
            frame.getContentPane().add(BorderLayout.CENTER, ta);
            frame.getContentPane().add(BorderLayout.SOUTH, panel);
            frame.setVisible(true);

            for (SerialPort serialPort : SerialPort.getCommPorts()) {

                JRadioButtonMenuItem portMenuItem = new JRadioButtonMenuItem(serialPort.getSystemPortName() + " - " + serialPort.getDescriptivePortName());
                portMenu.add(portMenuItem);
                if ("COM3".equals(serialPort.getSystemPortName())) {
                    this.serialPort = serialPort;
                    portMenu.setSelected(true);
                    baudRate = serialPort.getBaudRate();
                    baudRateMenuMap.get(baudRate).setSelected(true);
                    openSerialPort("COM3", ta);
                }
            }
            
            for (MidiDevice.Info deviceInfo : MidiSystem.getMidiDeviceInfo()) {
                MidiDevice device = MidiSystem.getMidiDevice(deviceInfo);
                if (device.getMaxReceivers() != 0) {
                    JRadioButtonMenuItem midiDeviceMenuItem = new JRadioButtonMenuItem(deviceInfo.getName());
                    midiDeviceMenu.add(midiDeviceMenuItem);
                    if ("LoopBe Internal MIDI".equals(deviceInfo.getName())) {
                        midiDeviceMenuItem.setSelected(true);
                        receiver = device.getReceiver();
                    }
                    midiDeviceMenuItem.addActionListener(new ActionListener(){
                        public void actionPerformed(ActionEvent event) {
                            try {
                                receiver = device.getReceiver();
                            } catch (Exception error) {
                                System.out.println("Error: " + error.getMessage());
                                error.printStackTrace();
                                System.exit(1);
                            }
                        }
                    });
                }
            }

            
        } catch (Exception e) {
            System.err.println("Error");
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }

    private void openSerialPort(String portName, JTextArea loggingArea) {
        if (serialPort != null && serialPort.isOpen()) {
            serialPort.closePort();
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
               int sensor = ByteBuffer.wrap(subArray(newData, 0, 1)).getInt();
               int velocity = ByteBuffer.wrap(subArray(newData, 1, 2)).getInt();
               loggingArea.append("Sensor " + sensor + " Velocity " + velocity);
            }
        });

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

    private static byte[] subArray(byte[] input, int start, int length) {
        byte[] ret = new byte[length];
        for (int i = 0; i < length; i++) {
            ret[i] = input[start + i];
        }
        return ret;
    }
}
