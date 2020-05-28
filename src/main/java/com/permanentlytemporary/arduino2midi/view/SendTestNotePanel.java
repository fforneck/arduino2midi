package com.permanentlytemporary.arduino2midi.view;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import com.permanentlytemporary.arduino2midi.app.App;
import com.permanentlytemporary.arduino2midi.utils.Util;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SendTestNotePanel extends JPanel implements ActionListener {

    private static final long serialVersionUID = 3268665568944061098L;

    private App app = null;
    private JSpinner channelSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 15, 1));
    private JSpinner noteSpinner = new JSpinner(new SpinnerNumberModel(50, 0, 255, 1));
    private JSpinner velocitySpinner = new JSpinner(new SpinnerNumberModel(90, 0, 255, 1));
    private JSpinner durationSpinner = new JSpinner(new SpinnerNumberModel(500, 0, 1000, 100));

    public SendTestNotePanel(App app) {
        super();
        this.app = app;
        this.setBorder(BorderFactory.createTitledBorder("Send Test Note"));
        JLabel channelLabel = new JLabel("Channel");
        this.add(channelLabel);
        this.add(channelSpinner);
        JLabel noteLabel = new JLabel("Note");
        this.add(noteLabel);
        this.add(noteSpinner);
        JLabel velocityLabel = new JLabel("Velocity");
        this.add(velocityLabel);
        this.add(velocitySpinner);
        JLabel durationLabel = new JLabel("Duration");
        this.add(durationLabel);
        this.add(durationSpinner);
        JButton send = new JButton("Send");
        send.addActionListener(this);
        this.add(send);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        app.playNote(
        		Util.getSpinnerValue(channelSpinner, "channel"), 
        		Util.getSpinnerValue(noteSpinner, "note"), 
        		Util.getSpinnerValue(velocitySpinner, "velocity"),
        		Util.getSpinnerValue(durationSpinner, "duration"));
    }
}