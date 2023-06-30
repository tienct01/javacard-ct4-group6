/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Constants;

/**
 *
 * @author Admin
 */
public interface  APPLET_CONSTANTS {
    public static final byte[] AID_AUTH_APPLET = {
        (byte)0x11, (byte)0x22, (byte)0x33, (byte)0x44, (byte)0x55, 
        (byte)0x88, (byte)0x00
    };
    
    public static final byte[] AID_MARKET_APPLET = {
        (byte)0x11, (byte)0x22, (byte)0x33, (byte)0x44, (byte)0x55, 
        (byte)0x88, (byte)0x02
    };
    
    public static short SELECT_NULL = 0;
    public static short SELECT_AUTH = 1;
    public static short SELECT_INFO = 2;
     public static short SELECT_MARKET = 3;
//    public static int SELECT_AUTH =0;
    public static final byte[] AID_INFO_APPLET = {
        (byte)0x11, (byte)0x22, (byte)0x33, (byte)0x44, (byte)0x55, 
        (byte)0x88, (byte)0x01
    };
    
    public final static byte CLA =(byte)0xB0;

    public final static byte CREATE = (byte) 0x10;
    public final static byte UPDATE = (byte) 0x11;
    public final static byte READ = (byte) 0x13;
    public final static byte DELETE = (byte) 0x14;
    public final static byte UNBLOCK = (byte) 0x15;
    public final static byte VERIFY = (byte) 0x16;
    public final static byte CLEAR = (byte) 0x17;
    
    public final static byte GET_RSA = (byte) 0x20;
    public final static byte GET_MOL = (byte) 0x18;
    public final static byte GET_EXPO = (byte) 0x19;
}
