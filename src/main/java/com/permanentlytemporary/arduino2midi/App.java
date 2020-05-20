package com.permanentlytemporary.arduino2midi;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import com.permanentlytemporary.arduino2midi.model.AnalogSensor;
import com.permanentlytemporary.arduino2midi.view.Form;

public class App {

    private Form form = new Form(this);
    private Map<String, SerialPort> serialPortMap = new HashMap<String, SerialPort>();
    private SerialPort currentSerialPort = null;
    private Integer currentBaudRate = 57600;
    private Map<String, MidiDevice> midiDeviceMap = new HashMap<String, MidiDevice>();
    private MidiDevice currentMidiDevice = null;
    private Receiver currentReceiver = null;

    public static void main(String[] args) {

        new App();

    }

    public App() {

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

        for (SerialPort serialPort : SerialPort.getCommPorts()) {
            serialPortMap.put(serialPort.getDescriptivePortName(), serialPort);
        }

        form.show(serialPortMap.keySet().toArray(new String[serialPortMap.size()]), midiDeviceMap.keySet().toArray(new String[midiDeviceMap.size()]));

    }

    public void changeSerialPort(String portName) {
        SerialPort serialPort = serialPortMap.get(portName);
        if (currentSerialPort != null && currentSerialPort.isOpen()) {
            currentSerialPort.closePort();
        }
        currentSerialPort = serialPort;
        currentSerialPort.openPort();
        currentSerialPort.setBaudRate(currentBaudRate);
        currentSerialPort.addDataListener(new SerialPortDataListener() {

            @Override
            public int getListeningEvents() { 
                return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
            }

            @Override
            public void serialEvent(SerialPortEvent event) {
               if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE)
                  return;
               byte[] newData = new byte[serialPort.bytesAvailable()];
               int numRead = serialPort.readBytes(newData, newData.length);
               
               System.out.println("Read " + numRead + " bytes.");
               System.out.println(newData[0]);
               System.out.println(newData[1]);
               System.out.println(newData[2]);
               byte[] sensorData = {newData[0]};
               byte[] velocityData = {newData[1], newData[2]};
               int sensor = ByteBuffer.wrap(sensorData).getInt();
               int velocity = ByteBuffer.wrap(velocityData).getInt();
               form.appendLog("Sensor " + sensor + " Velocity " + velocity);
               AnalogSensor analogSensor = form.getAnalogSensor(sensor);
               try {
                   currentReceiver.send(new ShortMessage(ShortMessage.NOTE_ON, analogSensor.getChannel(), analogSensor.getNote(), velocity), -1);
                   Thread.sleep(analogSensor.getDuration());
                   currentReceiver.send(new ShortMessage(ShortMessage.NOTE_OFF, analogSensor.getChannel(), analogSensor.getNote(), 0), -1);
               } catch (Exception e) {
                   System.out.println(" ERROR: " + e.getMessage());
                   e.printStackTrace();
               }
            }
        });
    }
  
    public void changeBaudRate(Integer rate) {
        currentSerialPort.setBaudRate(rate);
    }

    public void changeMidiDevice(String midiDeviceName) throws MidiUnavailableException {
        if (currentReceiver != null) {
            currentReceiver.close();
        }
        if (currentMidiDevice != null && currentMidiDevice.isOpen()) {
            currentMidiDevice.close();
        }
        currentMidiDevice = midiDeviceMap.get(midiDeviceName);
        currentMidiDevice.open();
        currentReceiver = currentMidiDevice.getReceiver();
    }
}
