package com.permanentlytemporary.arduino2midi.app;

import java.util.HashMap;
import java.util.Map;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortEvent;
import com.fazecast.jSerialComm.SerialPortPacketListener;
import com.permanentlytemporary.arduino2midi.view.Form;
import com.permanentlytemporary.arduino2midi.view.LogForm;
import com.permanentlytemporary.arduino2midi.view.PlotterForm;

public class App implements SerialPortPacketListener {
	
	private static final int NUMBER_OF_BYTES = 27;

    private Form mainForm = null;
    private LogForm logForm = new LogForm();
    private PlotterForm plotterForm = new PlotterForm();
    private Map<String, SerialPort> serialPortMap = new HashMap<String, SerialPort>();
    private SerialPort currentSerialPort = null;
    private Integer currentBaudRate = 9600;
    private Map<String, MidiDevice> midiDeviceMap = new HashMap<String, MidiDevice>();
    private MidiDevice currentMidiDevice = null;
    private Receiver currentReceiver = null;
    private int[] analogReads = new int[16];
    private boolean[] digitalReads = new boolean[54];

    public static void main(String[] args) {

        new App();

    }

    public App() {

        getMidiDevices();
        getSerialPorts();
        mainForm = new Form(this, logForm, plotterForm, serialPortMap.keySet().toArray(new String[serialPortMap.size()]), midiDeviceMap.keySet().toArray(new String[midiDeviceMap.size()]));
        mainForm.show();

    }

    private void getMidiDevices() {
        for (MidiDevice.Info deviceInfo : MidiSystem.getMidiDeviceInfo()) {
            try {
                MidiDevice device = MidiSystem.getMidiDevice(deviceInfo);
                if (device.getMaxReceivers() != 0) {
                    midiDeviceMap.put(deviceInfo.getName(), device);
                }
            } catch (MidiUnavailableException e) {
                System.out.println("ERROR: Midi device is not available: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void getSerialPorts() {
        for (SerialPort serialPort : SerialPort.getCommPorts()) {
            serialPortMap.put(serialPort.getDescriptivePortName(), serialPort);
        }
    }

    public void changeSerialPort(String portName) {
    	if (currentSerialPort != null && currentSerialPort.isOpen()) {
    		currentSerialPort.closePort();
    	}
    	currentSerialPort = serialPortMap.get(portName);
    	currentSerialPort.setComPortParameters(currentBaudRate, 8, 1, 0);
    	currentSerialPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING, 0, 0);
    	if (currentSerialPort.openPort()) {
    		System.out.println("Serial Port is open");
    		currentSerialPort.addDataListener(this);
    	}
    }

    public void playNote(int channel, int note, int velocity, int duration) {
        try {
            currentReceiver.send(new ShortMessage(ShortMessage.NOTE_ON, channel, note, velocity), -1);
            Thread.sleep(duration);
            currentReceiver.send(new ShortMessage(ShortMessage.NOTE_OFF, channel, note, 0), -1);
        } catch (Exception e) {
            System.out.println(" ERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }
  
    public void changeBaudRate(Integer rate) {
        currentSerialPort.setBaudRate(rate);
    }

    public void changeMidiDevice(String midiDeviceName) {
        try {
            if (currentReceiver != null) {
                currentReceiver.close();
            }
            if (currentMidiDevice != null && currentMidiDevice.isOpen()) {
                currentMidiDevice.close();
            }
            currentMidiDevice = midiDeviceMap.get(midiDeviceName);
            currentMidiDevice.open();
            currentReceiver = currentMidiDevice.getReceiver();
        } catch (MidiUnavailableException e) {
            System.err.println(" ERROR: midi device is not available - " + e.getMessage());
            e.printStackTrace();
        }
    }

	public int getListeningEvents() {
        return SerialPort.LISTENING_EVENT_DATA_RECEIVED;
	}

	public void serialEvent(SerialPortEvent event) {
		//System.out.println("New serial port event: " + event.getEventType());
		byte[] newData = event.getReceivedData();
		System.out.println(newData.length + " bytes: " + arr2String(newData));
		for (int i = 0; i < analogReads.length; i++) {
			analogReads[i] = 
					(512 * getBit(newData[Math.floorDiv((i * 10) + 9, 8)], ((i * 10) + 9) % 8)) +
					(256 * getBit(newData[Math.floorDiv((i * 10) + 8, 8)], ((i * 10) + 8) % 8)) +
					(128 * getBit(newData[Math.floorDiv((i * 10) + 7, 8)], ((i * 10) + 7) % 8)) +
					( 64 * getBit(newData[Math.floorDiv((i * 10) + 6, 8)], ((i * 10) + 6) % 8)) +
					( 32 * getBit(newData[Math.floorDiv((i * 10) + 5, 8)], ((i * 10) + 5) % 8)) +
					( 16 * getBit(newData[Math.floorDiv((i * 10) + 4, 8)], ((i * 10) + 4) % 8)) +
					(  8 * getBit(newData[Math.floorDiv((i * 10) + 3, 8)], ((i * 10) + 3) % 8)) +
					(  4 * getBit(newData[Math.floorDiv((i * 10) + 2, 8)], ((i * 10) + 2) % 8)) +
					(  2 * getBit(newData[Math.floorDiv((i * 10) + 1, 8)], ((i * 10) + 1) % 8)) +
					(  1 * getBit(newData[Math.floorDiv((i * 10) + 0, 8)], ((i * 10) + 0) % 8));
		}
		//System.out.println("analogReads: " + arr2String(analogReads));
		for (int i = 0; i < digitalReads.length; i++) {
			digitalReads[i] = getBit(newData[20 + Math.floorDiv(i, 8)], i % 8) == 1;
		}
		System.out.println("digitalReads: " + arr2String(digitalReads));
		// TODO: convert numbers, detect a change and send note
		//playNote(mainForm.getChannel(true, sensor), mainForm.getNote(true, sensor), velocity, mainForm.getDuration(true, sensor));
	}
	
	private int getBit(byte number, int position) {
	   return (number >> position) & 1;
	}
	
	private String arr2String(byte[] arr) {
		StringBuilder str = new StringBuilder();
		for (int i = 0; i < arr.length; i++) {
			str.append(arr[i]);
			str.append(", ");
		}
		return str.toString();
	}
	
	private String arr2String(int[] arr) {
		StringBuilder str = new StringBuilder();
		for (int i = 0; i < arr.length; i++) {
			str.append(arr[i]);
			str.append(", ");
		}
		return str.toString();
	}
	
	private String arr2String(boolean[] arr) {
		StringBuilder str = new StringBuilder();
		for (int i = 0; i < arr.length; i++) {
			str.append(arr[i] ? "HIGH" : " LOW");
			str.append(", ");
		}
		return str.toString();
	}

	@Override
	public int getPacketSize() {
		return NUMBER_OF_BYTES;
	}
}
