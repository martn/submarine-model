/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package submarinemodel;

import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

/**
 *
 * @author Administrator
 */
public class SubmarinePort extends ComPort {

    // interval of reading data from buffer
    public static final int READ_INTERVAL = 20;
    public static final byte START_SEQUENCE_BYTE = '6';
    byte[] readData = new byte[16];
    boolean synced = false;

    public SubmarinePort(Configuration config) throws PortNotFoundException {

        super(config.getProperty("portId"));



        try {

            // set default port parameters
            serialPort.setSerialPortParams(19200, SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);
        } catch (UnsupportedCommOperationException e) {
        }

        
    }
}
