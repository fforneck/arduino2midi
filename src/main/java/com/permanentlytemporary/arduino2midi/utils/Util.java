package com.permanentlytemporary.arduino2midi.utils;

import javax.swing.JSpinner;

public class Util {
	
	public static final Integer[] POSSIBLE_BAUD_RATES = {300, 600, 1200, 2400, 4800, 9600, 14400, 19200, 28800, 38400, 57600, 115200};
    
    public static Integer getSpinnerValue(JSpinner spinner, String name) {
    	try {
    		spinner.commitEdit();
    	} catch ( java.text.ParseException e ) {
    		System.err.println(" ERROR: error parsing " + name + " spinner");
    		e.printStackTrace();
    	}
        return (Integer) spinner.getValue();
    }

}
