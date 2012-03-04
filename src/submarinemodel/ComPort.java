/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package submarinemodel;

import gnu.io.*;
import java.io.*;
import java.util.*;
import org.apache.commons.lang3.ArrayUtils;

/**
 *
 * @author Martin Novak
 */
class ComPort implements Runnable, SerialPortEventListener {

    public static final int BUFFER_SIZE = 20;
    static CommPortIdentifier portId;
    //sstatic CommPortIdentifier saveportId;
    static Enumeration portList;
    InputStream inputStream;
    SerialPort serialPort;
    Thread readThread;
    OutputStream outputStream;
    protected String divertCode = "10";
    static String TimeStamp;
    boolean outputBufferEmptyFlag = false;
    byte[] inputBuffer = new byte[BUFFER_SIZE];
    int inputBufferCount = 0;

    public ComPort(String defaultPort) throws PortNotFoundException {

        boolean portFound = false;

        defaultPort = defaultPort.toUpperCase();

        portList = CommPortIdentifier.getPortIdentifiers();

        while (portList.hasMoreElements()) {
            portId = (CommPortIdentifier) portList.nextElement();
            if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {

                Util.log.write(portId.getName());

                if (portId.getName().equals(defaultPort)) {
                    Util.log.write("Found port: " + defaultPort);
                    portFound = true;
                    // init reader thread
                    // ComPort reader = new ComPort();
                    break;
                }
            }

        }
        if (!portFound) {
            Util.log.write("port " + defaultPort + " not found.");

            throw new PortNotFoundException("Port "+defaultPort+" not found.");
        } else {


            // initalize serial port
            try {
                serialPort = (SerialPort) portId.open(this.getClass().getName(), 2000);
                
            } catch (PortInUseException e) {
                Util.log.write("Error: port is in use");
            }

            try {
                inputStream = serialPort.getInputStream();
            } catch (IOException e) {
            }

            try {
                serialPort.addEventListener(this);
                serialPort.notifyOnDataAvailable(true);
            } catch (TooManyListenersException e) {

            }

            // activate the DATA_AVAILABLE notifier
            serialPort.notifyOnDataAvailable(true);

            try {
                // set default port parameters
                serialPort.setSerialPortParams(19200, SerialPort.DATABITS_8,
                        SerialPort.STOPBITS_1,
                        SerialPort.PARITY_NONE);
            } catch (UnsupportedCommOperationException e) {
            }

            try {
                // get the outputstream
                outputStream = serialPort.getOutputStream();
            } catch (IOException e) {
            }

            // start the read thread
            // readThread = new Thread(this);
            // readThread.start();
        }
     
    }

    /**
     *
     * thread to clear buffer;
     */
    public void run() {
    }

    /**
     * fills inBuffer with count number of bytes from incoming buffer
     * @param inBuffer
     * @param count
     * @return
     */
    synchronized public int readBytes(byte inBuffer[], int count) {
        // TODO throw exceptions
        if (inputBufferCount == 0 | count < 0) {
            return 0;
        }

        if (count > inputBufferCount) {
            int retval = inputBufferCount;
            System.arraycopy(inputBuffer, 0, inBuffer, 0, inputBufferCount);
            clearBuffer();
            return retval;
        } else {
            System.arraycopy(inputBuffer, 0, inBuffer, 0, count);
            inputBuffer = ArrayUtils.subarray(inputBuffer, count, inputBufferCount);
            inputBufferCount -= count;
            return count;
        }
    }

    /**
     * clears input buffer
     */
    protected void clearBuffer() {
        inputBuffer = new byte[0];
        inputBufferCount = 0;
    }

    /**
     * copies elements from one array to another
     * @param targetArray
     * @param sourceArray
     * @param count
     */
    /*    private void arrayCopy(byte targetArray[], byte sourceArray[], int count) {
    for (int i = 0; i < count; i++) {
    targetArray[i]=sourceArray[i];
    }

    }*/
    /**
     * writes string to port
     * @param string
     */
    public void write(String string) {
     //   Util.log.write("Writing \"" + string + "\" to " + serialPort.getName());

        try {
            // write string to serial port
            outputStream.write(string.getBytes());
        } catch (IOException e) {
        }
    }


    /**
     * writes byte array to port
     * @param bytes
     */
    public void write(byte[] bytes) {
        try {
            // write string to serial port
            outputStream.write(bytes);
        } catch (IOException e) {
        }
    }

    /**
     * writes single byte to port
     * @param string
     */
    public void write(byte b) {

     //   Util.log.write("Writing \"" + Util.ByteToBinary(b));

        try {
            // write string to serial port
            outputStream.write(b);
        } catch (IOException e) {
        }
    }


    /**
     * method fired if new data in read buffer, for overriding
     */
    public void dataAvailable() {
        
    }

    
    /**
     * asynchronous serial read event 
     * @param event
     */
    synchronized public void serialEvent(SerialPortEvent event) {

       // Util.log.write("Event: "+event.getEventType());
        switch (event.getEventType()) {
            case SerialPortEvent.BI:
            case SerialPortEvent.OE:
            case SerialPortEvent.FE:
            case SerialPortEvent.PE:
            case SerialPortEvent.CD:
            case SerialPortEvent.CTS:
            case SerialPortEvent.DSR:
            case SerialPortEvent.RI:
            case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
                break;
            case SerialPortEvent.DATA_AVAILABLE:
                // we get here if data has been received
                byte[] readBuffer = new byte[20];
                int readBufferCount = 0;
                try {
                    // read data
                    while (inputStream.available() > 0) {
                        readBufferCount = inputStream.read(readBuffer);
                    }

                    // buffer overflow
                    int overFlow = Math.max(readBufferCount + inputBufferCount - BUFFER_SIZE, 0);

                    // update inputBuffer, shifts data to the left if buffer full
                    inputBuffer = (byte[]) ArrayUtils.addAll(ArrayUtils.subarray(inputBuffer, overFlow, inputBufferCount),
                            ArrayUtils.subarray(readBuffer, 0, readBufferCount));
                    
                    inputBufferCount += readBufferCount - overFlow;


                    // log
                    String result = new String(ArrayUtils.subarray(inputBuffer, 0, readBufferCount + inputBufferCount));

                    dataAvailable();
                  //  Util.log.write("buffer: " + result);
                } catch (IOException e) {
                }
                break;
        }
    }
}
