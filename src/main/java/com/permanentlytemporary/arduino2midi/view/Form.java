package com.permanentlytemporary.arduino2midi.view;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.permanentlytemporary.arduino2midi.App;
import com.permanentlytemporary.arduino2midi.model.AnalogSensor;
import com.permanentlytemporary.arduino2midi.model.DigitalSensor;

public class Form {

    private App app = null;
    private JFrame frame = new JFrame("Arduino to MIDI");
    private AnalogSensor[] analogSensors = new AnalogSensor[16];
    private DigitalSensor[] digitalSensors = new DigitalSensor[54];
    private static final Integer[] POSSIBLE_BAUD_RATES = { 300, 600, 1200, 2400, 4800, 9600, 14400, 19200, 28800, 38400,
            57600, 115200 };
    private static final Byte[] POSSIBLE_MIDI_CHANNELS = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15 };
    private static final Integer[] POSSIBLE_NOTES = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18,
            19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45,
            46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72,
            73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95, 96, 97, 98, 99,
            100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120,
            121, 122, 123, 124, 125, 126, 127, 128, 129, 130, 131, 132, 133, 134, 135, 136, 137, 138, 139, 140, 141,
            142, 143, 144, 145, 146, 147, 148, 149, 150, 151, 152, 153, 154, 155, 156, 157, 158, 159, 160, 161, 162,
            163, 164, 165, 166, 167, 168, 169, 170, 171, 172, 173, 174, 175, 176, 177, 178, 179, 180, 181, 182, 183,
            184, 185, 186, 187, 188, 189, 190, 191, 192, 193, 194, 195, 196, 197, 198, 199, 200, 201, 202, 203, 204,
            205, 206, 207, 208, 209, 210, 211, 212, 213, 214, 215, 216, 217, 218, 219, 220, 221, 222, 223, 224, 225,
            226, 227, 228, 229, 230, 231, 232, 233, 234, 235, 236, 237, 238, 239, 240, 241, 242, 243, 244, 245, 246,
            247, 248, 249, 250, 251, 252, 253, 254, 255 };
    private JTextArea logTextArea = new JTextArea();

    public Form(App app) {
        this.app = app;
    }

    public AnalogSensor getAnalogSensor(int index) {
        return analogSensors[index];
    }

    public DigitalSensor getDigitalSensor(int index) {
        return digitalSensors[index];
    }

    public void show(String[] serialPorts, String[] mididevices) {
        try {
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1600, 1000);

            // Creating the communication panel
            JPanel commPanel = new JPanel();
            commPanel.setBorder(BorderFactory.createTitledBorder("Devices"));
            JLabel serialPortLabel = new JLabel("Serial Port");
            commPanel.add(serialPortLabel);
            JComboBox<String> serialPortComboBox = new JComboBox<String>(serialPorts);
            commPanel.add(serialPortComboBox);
            JLabel baudRateLabel = new JLabel("Baud Rate");
            commPanel.add(baudRateLabel);
            JComboBox<Integer> baudRateComboBox = new JComboBox<Integer>(POSSIBLE_BAUD_RATES);
            commPanel.add(baudRateComboBox);
            JLabel midiDeviceLabel = new JLabel("Midi Device");
            commPanel.add(midiDeviceLabel);
            JComboBox<String> midiDeviceComboBox = new JComboBox<String>(mididevices);
            commPanel.add(midiDeviceComboBox);
            
            // Creating the analog sensor panel
            JPanel analogSensorsPanel = new JPanel();
            analogSensorsPanel.setBorder(BorderFactory.createTitledBorder("Analog Sensors"));
            analogSensorsPanel.setLayout(new BoxLayout(analogSensorsPanel, BoxLayout.Y_AXIS));
            for (int i = 0; i < analogSensors.length; i++) {
                JPanel analogSensorPanel = new JPanel();
                JLabel analogSensorLabel = new JLabel("A" + i + ":");
                analogSensorPanel.add(analogSensorLabel);
                JLabel enabledLabel = new JLabel("Enabled: ");
                analogSensorPanel.add(enabledLabel);
                JCheckBox enabledCheckBox = new JCheckBox();
                analogSensorPanel.add(enabledCheckBox);
                JLabel channelLabel = new JLabel("Channel: ");
                analogSensorPanel.add(channelLabel);
                JComboBox<Byte> channelComboBox = new JComboBox<Byte>(POSSIBLE_MIDI_CHANNELS);
                analogSensorPanel.add(channelComboBox);
                JLabel noteLabel = new JLabel("Note: ");
                analogSensorPanel.add(noteLabel);
                JComboBox<Integer> noteComboBox = new JComboBox<Integer>(POSSIBLE_NOTES);
                analogSensorPanel.add(noteComboBox);
                JLabel sensitivityLabel = new JLabel("Sensitivity");
                analogSensorPanel.add(sensitivityLabel);
                JComboBox<Integer> sensitivityComboBox = new JComboBox<Integer>(POSSIBLE_NOTES);
                analogSensorPanel.add(sensitivityComboBox);
                JLabel durationLabel = new JLabel("Duration");
                analogSensorPanel.add(durationLabel);
                JTextField durationField = new JTextField();
                durationField.setColumns(4);
                analogSensorPanel.add(durationField);
                analogSensorsPanel.add(analogSensorPanel);
            }
            
            // Creating the digital sensor panel
            JPanel leftDigitalSensorsPanel = new JPanel();
            leftDigitalSensorsPanel.setBorder(BorderFactory.createTitledBorder("Digital Sensors"));
            leftDigitalSensorsPanel.setLayout(new BoxLayout(leftDigitalSensorsPanel, BoxLayout.Y_AXIS));
            for (int i = 0; i < digitalSensors.length / 2; i++) {
                JPanel digitalSensorPanel = new JPanel();
                JLabel digitalSensorLabel = new JLabel("D" + i + ":");
                digitalSensorPanel.add(digitalSensorLabel);
                JLabel enabledLabel = new JLabel("Enabled: ");
                digitalSensorPanel.add(enabledLabel);
                JCheckBox enabledCheckBox = new JCheckBox();
                digitalSensorPanel.add(enabledCheckBox);
                JLabel channelLabel = new JLabel("Channel: ");
                digitalSensorPanel.add(channelLabel);
                JComboBox<Byte> channelComboBox = new JComboBox<Byte>(POSSIBLE_MIDI_CHANNELS);
                digitalSensorPanel.add(channelComboBox);
                JLabel noteLabel = new JLabel("Note: ");
                digitalSensorPanel.add(noteLabel);
                JComboBox<Integer> noteComboBox = new JComboBox<Integer>(POSSIBLE_NOTES);
                digitalSensorPanel.add(noteComboBox);
                JLabel sensitivityLabel = new JLabel("Sensitivity");
                digitalSensorPanel.add(sensitivityLabel);
                JComboBox<Integer> sensitivityComboBox = new JComboBox<Integer>(POSSIBLE_NOTES);
                digitalSensorPanel.add(sensitivityComboBox);
                JLabel durationLabel = new JLabel("Duration");
                digitalSensorPanel.add(durationLabel);
                JTextField durationField = new JTextField();
                durationField.setColumns(4);
                digitalSensorPanel.add(durationField);
                leftDigitalSensorsPanel.add(digitalSensorPanel);
            }
            JPanel rightDigitalSensorsPanel = new JPanel();
            rightDigitalSensorsPanel.setBorder(BorderFactory.createTitledBorder("Digital Sensors"));
            rightDigitalSensorsPanel.setLayout(new BoxLayout(rightDigitalSensorsPanel, BoxLayout.Y_AXIS));
            for (int i = digitalSensors.length / 2; i < digitalSensors.length; i++) {
                JPanel digitalSensorPanel = new JPanel();
                JLabel digitalSensorLabel = new JLabel("D" + i + ":");
                digitalSensorPanel.add(digitalSensorLabel);
                JLabel enabledLabel = new JLabel("Enabled: ");
                digitalSensorPanel.add(enabledLabel);
                JCheckBox enabledCheckBox = new JCheckBox();
                digitalSensorPanel.add(enabledCheckBox);
                JLabel channelLabel = new JLabel("Channel: ");
                digitalSensorPanel.add(channelLabel);
                JComboBox<Byte> channelComboBox = new JComboBox<Byte>(POSSIBLE_MIDI_CHANNELS);
                digitalSensorPanel.add(channelComboBox);
                JLabel noteLabel = new JLabel("Note: ");
                digitalSensorPanel.add(noteLabel);
                JComboBox<Integer> noteComboBox = new JComboBox<Integer>(POSSIBLE_NOTES);
                digitalSensorPanel.add(noteComboBox);
                JLabel sensitivityLabel = new JLabel("Sensitivity");
                digitalSensorPanel.add(sensitivityLabel);
                JComboBox<Integer> sensitivityComboBox = new JComboBox<Integer>(POSSIBLE_NOTES);
                digitalSensorPanel.add(sensitivityComboBox);
                JLabel durationLabel = new JLabel("Duration");
                digitalSensorPanel.add(durationLabel);
                JTextField durationField = new JTextField();
                durationField.setColumns(4);
                digitalSensorPanel.add(durationField);
                rightDigitalSensorsPanel.add(digitalSensorPanel);
            }
            
            // Text Area at the Center
            JPanel logPanel = new JPanel();
            logPanel.setBorder(BorderFactory.createTitledBorder("Activity Log"));
            
            logPanel.add(logTextArea);
            
            //Creating the panel at bottom and adding components
            JPanel testPanel = new JPanel();
            testPanel.setBorder(BorderFactory.createTitledBorder("Send Test Note"));
            JLabel channelLabel = new JLabel("Channel");
            testPanel.add(channelLabel);
            JComboBox<Byte> testChannelComboBox = new JComboBox<Byte>(POSSIBLE_MIDI_CHANNELS);
            testPanel.add(testChannelComboBox);
            JLabel noteLabel = new JLabel("Note");
            testPanel.add(noteLabel);
            JComboBox<Integer> testNoteComboBox = new JComboBox<Integer>(POSSIBLE_NOTES);
            testPanel.add(testNoteComboBox);
            JLabel velocityLabel = new JLabel("Velocity");
            testPanel.add(velocityLabel);
            JComboBox<Integer> testVelocityComboBox = new JComboBox<Integer>(POSSIBLE_NOTES);
            testPanel.add(testVelocityComboBox);
            JLabel durationLabel = new JLabel("Duration");
            testPanel.add(durationLabel);
            JTextField testDurationField = new JTextField();
            testDurationField.setColumns(4);
            testPanel.add(testDurationField);
            JButton send = new JButton("Send");
            testPanel.add(send);
            
            //Adding Components to the frame.
            Container pane = frame.getContentPane();
            JPanel leftPanel = new JPanel();
            leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
            leftPanel.add(commPanel);
            leftPanel.add(analogSensorsPanel);
            leftPanel.add(logPanel);
            leftPanel.add(testPanel);
            pane.add(leftPanel, BorderLayout.LINE_START);
            pane.add(leftDigitalSensorsPanel, BorderLayout.CENTER);
            pane.add(rightDigitalSensorsPanel, BorderLayout.LINE_END);
            frame.setVisible(true);
            
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Error: " + e.getMessage());
            System.err.println(" ERROR: " + e.getMessage());
            e.printStackTrace();
        }
        
    }

    public void appendLog(String message) {
        logTextArea.append(message);
    }

}