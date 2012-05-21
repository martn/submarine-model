/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package submarinemodel;

import java.io.UnsupportedEncodingException;

/**
 *
 * @author Administrator
 */
public class HexCodec {

    private static final char[] kDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a',
        'b', 'c', 'd', 'e', 'f'};
    static final byte[] HEX_CHAR_TABLE = {
        (byte) '0', (byte) '1', (byte) '2', (byte) '3',
        (byte) '4', (byte) '5', (byte) '6', (byte) '7',
        (byte) '8', (byte) '9', (byte) 'a', (byte) 'b',
        (byte) 'c', (byte) 'd', (byte) 'e', (byte) 'f'
    };

    public static byte[] hexToBytes(char[] hex) {
        int length = hex.length / 2;
        byte[] raw = new byte[length];
        for (int i = 0; i < length; i++) {
            int high = Character.digit(hex[i * 2], 16);
            int low = Character.digit(hex[i * 2 + 1], 16);
            int value = (high << 4) | low;
            if (value > 127) {
                value -= 256;
            }
            raw[i] = (byte) value;
        }
        return raw;
    }
    
    public static String byte2Hex(byte raw) throws UnsupportedEncodingException {
        byte[] hex = new byte[2];
        //int index = 0;

        int v = raw & 0xFF;
        hex[0] = HEX_CHAR_TABLE[v >>> 4];
        hex[1] = HEX_CHAR_TABLE[v & 0xF];
        
        return new String(hex, "ASCII");
    }
    

    public static String bytes2Hex(byte[] raw) throws UnsupportedEncodingException {
        byte[] hex = new byte[2 * raw.length];
        int index = 0;

        for (byte b : raw) {
            int v = b & 0xFF;
            hex[index++] = HEX_CHAR_TABLE[v >>> 4];
            hex[index++] = HEX_CHAR_TABLE[v & 0xF];
            hex[index++] = ' ';
        }
        return new String(hex, "ASCII");
    }

    public static byte[] hexToBytes(String hex) {
        return hexToBytes(hex.toCharArray());
    }

    public static byte hexToByte(String hex) {
        hex = hex.substring(0, 2); // first two characters

        return (byte) Integer.parseInt(hex, 16);
    }
}
