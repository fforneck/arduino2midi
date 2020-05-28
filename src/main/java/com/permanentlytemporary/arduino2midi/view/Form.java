package com.permanentlytemporary.arduino2midi.view;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.permanentlytemporary.arduino2midi.app.App;
import com.permanentlytemporary.arduino2midi.utils.Util;

public class Form implements ItemListener, ActionListener {

    private App app = null;
    private LogForm logForm = null;
    private PlotterForm plotterForm = null;
    private JFrame frame = new JFrame("Arduino to MIDI");
    private JComboBox<String> serialPortComboBox = null;
    private JComboBox<Integer> baudRateComboBox = new JComboBox<Integer>(Util.POSSIBLE_BAUD_RATES);
    private JComboBox<String> midiDeviceComboBox = null;
    private AnalogSensorPanel[] analogSensorPanels = new AnalogSensorPanel[16];
    private DigitalSensorPanel[] digitalSensorPanels = new DigitalSensorPanel[54];
    private JMenuItem consoleMenuItem = new JMenuItem("Show Console");
    private JMenuItem plotterMenuItem = new JMenuItem("Show Plotter"); 

    public Form(App app, LogForm logForm, PlotterForm plotterForm, String[] serialPorts, String[] mididevices) {
        this.app = app;
        this.logForm = logForm;
        this.plotterForm = plotterForm;
        this.serialPortComboBox = new JComboBox<String>(serialPorts);
        this.midiDeviceComboBox = new JComboBox<String>(mididevices);
    }

    public void show() {
        try {
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1600, 1000);

            // Creating Menu
            JMenuBar menuBar = new JMenuBar();
            JMenu extrasMenu = new JMenu("Extras");
            menuBar.add(extrasMenu);
            extrasMenu.add(consoleMenuItem);
            consoleMenuItem.addActionListener(this);
            extrasMenu.add(plotterMenuItem);
            plotterMenuItem.addActionListener(this);

            // Creating the communication panel
            JPanel commPanel = new JPanel();
            commPanel.setBorder(BorderFactory.createTitledBorder("Devices"));
            JLabel serialPortLabel = new JLabel("Serial Port");
            commPanel.add(serialPortLabel);
            serialPortComboBox.addItemListener(this);
            app.changeSerialPort((String) serialPortComboBox.getSelectedItem());
            commPanel.add(serialPortComboBox);
            JLabel baudRateLabel = new JLabel("Baud Rate");
            commPanel.add(baudRateLabel);
            baudRateComboBox.setSelectedItem(9600);
            baudRateComboBox.addItemListener(this);
            commPanel.add(baudRateComboBox);
            JLabel midiDeviceLabel = new JLabel("Midi Device");
            commPanel.add(midiDeviceLabel);
            midiDeviceComboBox.addItemListener(this);
            app.changeMidiDevice((String) midiDeviceComboBox.getSelectedItem());
            commPanel.add(midiDeviceComboBox);

            // Creating the analog sensor panel
            JPanel analogSensorsPanel = new JPanel();
            analogSensorsPanel.setBorder(BorderFactory.createTitledBorder("Analog Sensors"));
            analogSensorsPanel.setLayout(new BoxLayout(analogSensorsPanel, BoxLayout.Y_AXIS));
            for (int i = 0; i < analogSensorPanels.length; i++) {
                AnalogSensorPanel analogSensorPanel = new AnalogSensorPanel("A" + i);
                analogSensorPanels[i] = analogSensorPanel;
                analogSensorsPanel.add(analogSensorPanel);
            }

            // Creating the digital sensor panel
            JPanel leftDigitalSensorsPanel = new JPanel();
            leftDigitalSensorsPanel.setBorder(BorderFactory.createTitledBorder("Digital Sensors"));
            leftDigitalSensorsPanel.setLayout(new BoxLayout(leftDigitalSensorsPanel, BoxLayout.Y_AXIS));
            for (int i = 0; i < digitalSensorPanels.length / 2; i++) {
                DigitalSensorPanel digitalSensorPanel = new DigitalSensorPanel("D" + i);
                digitalSensorPanels[i] = digitalSensorPanel;
                leftDigitalSensorsPanel.add(digitalSensorPanel);
            }
            JPanel rightDigitalSensorsPanel = new JPanel();
            rightDigitalSensorsPanel.setBorder(BorderFactory.createTitledBorder("Digital Sensors"));
            rightDigitalSensorsPanel.setLayout(new BoxLayout(rightDigitalSensorsPanel, BoxLayout.Y_AXIS));
            for (int i = digitalSensorPanels.length / 2; i < digitalSensorPanels.length; i++) {
                DigitalSensorPanel digitalSensorPanel = new DigitalSensorPanel("D" + i);
                digitalSensorPanels[i] = digitalSensorPanel;
                rightDigitalSensorsPanel.add(digitalSensorPanel);
            }

            // Creating the panel at bottom and adding components
            SendTestNotePanel testPanel = new SendTestNotePanel(app);

            // Adding Components to the frame.
            Container pane = frame.getContentPane();
            JPanel leftPanel = new JPanel();
            leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
            leftPanel.add(commPanel);
            leftPanel.add(analogSensorsPanel);
            leftPanel.add(testPanel);
            pane.add(menuBar, BorderLayout.PAGE_START);
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

    public boolean isEnabled(boolean analog, int sensor) {
        if (analog) {
            return analogSensorPanels[sensor].isEnabled();
        } else {
            return digitalSensorPanels[sensor].isEnabled();
        }
    }

    public Integer getChannel(boolean analog, int sensor) {
        if (analog) {
            return analogSensorPanels[sensor].getChannel();
        } else {
            return digitalSensorPanels[sensor].getChannel();
        }
    }

    public int getNote(boolean analog, int sensor) {
        if (analog) {
            return analogSensorPanels[sensor].getNote();
        } else {
            return digitalSensorPanels[sensor].getNote();
        }
    }

    public int getSensitivity(int sensor) {
        return analogSensorPanels[sensor].getSensitivity();
    }

    public int getVelocity(int sensor) {
        return digitalSensorPanels[sensor].getVelocity();
    }

    public int getRandomization(int sensor) {
        return digitalSensorPanels[sensor].getRandomization();
    }

    public int getDuration(boolean analog, int sensor) {
        if (analog) {
            return analogSensorPanels[sensor].getDuration();
        } else {
            return digitalSensorPanels[sensor].getDuration();
        }
    }

    public void itemStateChanged(ItemEvent e) {
        if (e.getSource() == serialPortComboBox) {
            app.changeSerialPort((String) serialPortComboBox.getSelectedItem());
        } else if (e.getSource() == baudRateComboBox) {
            app.changeBaudRate((Integer) baudRateComboBox.getSelectedItem());
        } else if (e.getSource() == midiDeviceComboBox) {
            app.changeMidiDevice((String) midiDeviceComboBox.getSelectedItem());
        } else {
            System.out.println(" ERROR: event not recognized");
        }

    }

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == consoleMenuItem) {
			logForm.show();
		} else if (e.getSource() == plotterMenuItem) {
			plotterForm.show();
		}
		
	}

}