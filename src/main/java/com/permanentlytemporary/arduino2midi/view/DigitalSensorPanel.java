package com.permanentlytemporary.arduino2midi.view;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import com.permanentlytemporary.arduino2midi.utils.Util;

public class DigitalSensorPanel extends JPanel {

    private static final long serialVersionUID = -4822182481462684968L;

    private JCheckBox enabledCheckBox = new JCheckBox();
    SpinnerNumberModel channelModel = new SpinnerNumberModel(0, 0, 15, 1);
    SpinnerNumberModel noteModel = new SpinnerNumberModel(0, 0, 255, 1);
    SpinnerNumberModel velocityModel = new SpinnerNumberModel(0, 0, 255, 1);
    SpinnerNumberModel randomizationModel = new SpinnerNumberModel(0, 0, 255, 1);
    SpinnerNumberModel durationModel = new SpinnerNumberModel(500, 0, 1000, 100);
    private JSpinner channelSpinner = new JSpinner(channelModel);
    private JSpinner noteSpinner = new JSpinner(noteModel);
    private JSpinner velocitySpinner = new JSpinner(velocityModel);
    private JSpinner randomizationSpinner = new JSpinner(randomizationModel);
    private JSpinner durationSpinner = new JSpinner(durationModel);

    public DigitalSensorPanel(String name) {
        super();
        JLabel nameLabel = new JLabel(name);
        this.add(nameLabel);
        enabledCheckBox.setText("Enabled");
        enabledCheckBox.getModel().setSelected(true);
        this.add(enabledCheckBox);
        JLabel channelLabel = new JLabel("Channel");
        this.add(channelLabel);
        this.add(channelSpinner);
        JLabel noteLabel = new JLabel("Note");
        this.add(noteLabel);
        this.add(noteSpinner);
        JLabel velocityLabel = new JLabel("Velocity");
        this.add(velocityLabel);
        this.add(velocitySpinner);
        JLabel randomizationLabel = new JLabel("Randomization");
        this.add(randomizationLabel);
        this.add(randomizationSpinner);
        JLabel durationLabel = new JLabel("Duration:");
        this.add(durationLabel);
        this.add(durationSpinner);
    }

    public boolean isEnabled() {
        return enabledCheckBox.isSelected();
    }

    public Integer getChannel() {
        return Util.getSpinnerValue(channelSpinner, "channel");
    }

    public int getNote() {
    	return Util.getSpinnerValue(noteSpinner, "note");
    }
    
    public int getVelocity() {
    	return Util.getSpinnerValue(velocitySpinner, "velocity");
    }
    
    public int getRandomization() {
    	return Util.getSpinnerValue(randomizationSpinner, "randomization");
    }

    public int getDuration() {
    	return Util.getSpinnerValue(durationSpinner, "duration");
    }

}