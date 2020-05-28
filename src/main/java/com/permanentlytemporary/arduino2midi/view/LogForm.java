package com.permanentlytemporary.arduino2midi.view;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.JTextArea;

public class LogForm {

    private JFrame frame = new JFrame("Activity Log");
    private JTextArea textArea = new JTextArea();

    public LogForm() {
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.setSize(400, 400);
        Container pane = frame.getContentPane();
        textArea.setEditable(false);
        pane.add(textArea, BorderLayout.CENTER);
    }
    
    public void show() {
    	frame.setVisible(true);
    }

    public void appendLog(String message) {
    	textArea.append(message);
    }
}