package auth;

import javacard.framework.*;
import javacardx.crypto.Cipher;
import javacard.security.*;
import auth.PinInterface;
import javacardx.apdu.ExtendedLength;

public class cinema extends Applet implements CINEMA_CONSTANTS, ExtendedLength, CinemaInterface 
{
	// S tin hin ti 0-11;
	// Tng tin 12-23
	// s �im 24-31;
	// Cn li m� m code
	private static byte[] data = new byte[256];
	private Cipher aesCipher;
	private static byte[] dataDecrypted;
	private static byte[] bufferEn;
	
    private AESKey aesKey;
    
    private final byte[] pinAID 
		= new byte[]{(byte)0x11, (byte)0x22, (byte)0x33, 
		(byte)0x44, (byte)0x55, (byte)0x88, (byte)0x00};
	public static void install(byte[] bArray, short bOffset, byte bLength) 
	{
		new cinema(bArray, (short) (bOffset + 1), bArray[bOffset]);
	}
	private cinema(byte[] bArray, short bOffset, byte bLength){
		bufferEn = JCSystem.makeTransientByteArray((short) 256, JCSystem.CLEAR_ON_DESELECT);
		dataDecrypted = JCSystem.makeTransientByteArray((short) 256, JCSystem.CLEAR_ON_DESELECT);
		
		// Khoi tao AES
		aesCipher = Cipher.getInstance(Cipher.ALG_AES_BLOCK_128_CBC_NOPAD, false);
        aesKey = (AESKey) KeyBuilder.buildKey(KeyBuilder.TYPE_AES, KeyBuilder.LENGTH_AES_128, false);
        
        register();
		setAESKey();
        encrypt(data,(short)0);
        Util.arrayCopy(bufferEn,  (short)0,data,  (short)0, (short)256);
        
	}
	
	private void setAESKey(){
		
		// get AES Key from PIN applet
		
 		AID masterAID = JCSystem.lookupAID(
						pinAID, 
						(short)0, 
						(byte)pinAID.length);
		PinInterface sio = (PinInterface)
			(JCSystem.getAppletShareableInterfaceObject(masterAID, (byte)0x12));
			
		byte[] hasPin= sio.getHashPin();

        try {
            aesKey.setKey(hasPin, (short) 0);
        } catch(Exception e) {
          
        }
    
	}

	public void process(APDU apdu)
	{
		if (selectingApplet())
		{
			return;
		}
		
		setAESKey();

		byte[] buf = apdu.getBuffer();
		switch (buf[ISO7816.OFFSET_INS])
		{
		case INS_SHOW: 
			short le = apdu.setOutgoing();

			
			short index = (short)0;
			
			decrypt(data, (short)0);
			
			
			// x�a s tin hin ti tr�c khi gi
			
			
			apdu.setOutgoingLength((short)256);
			short toSend = (short)256;
			short pointer = 0;
			short sendLen = 0;
			while(toSend > 0)
			{
			sendLen = (toSend > le)?le:toSend;

			apdu.sendBytesLong(dataDecrypted, pointer,sendLen);
			toSend -= sendLen;
			pointer += sendLen;
			}
			
			break;
		case INS_NAPTIEN:
			// nap 12 byte
			naptien(apdu);
			break;
		case INS_THANHTOAN:
			// 12 Byte dau so tien, 8 byte sau la so diem cong, 
			//8 byte sau la so diem tru,  byte tiep theo  l� index ma giam gia de su dung,
			//8 * n byte sau la ma giam gia,
			thanhtoan(apdu);
			break;
		default:
			ISOException.throwIt(ISO7816.SW_INS_NOT_SUPPORTED);
		}
	}
	private void naptien(APDU apdu){
		byte byteRead = (byte)(apdu.setIncomingAndReceive());
		short dataOffset = apdu.getOffsetCdata();
		// short dataLen = apdu.getIncomingLength();
		short i=0;
		// Gii m ; s tin l� 54.321 s l�u l� 000000054321
		// Khi np s tin l� 362 s chuyn th�nh 000000000362 khi gi
		if (byteRead < (short) 12)ISOException.throwIt(ISO7816.SW_UNKNOWN);
		decrypt(data, (short)0);
		//t�nh tng
		byte[] buffer = apdu.getBuffer();
		JCSystem.beginTransaction();
		byte nho = 0;//bin nh
		byte tongDonvi = 0;
		for (i=(short) 11; i>=0; i--) {
			tongDonvi = (byte)(dataDecrypted[i] + buffer[dataOffset + i] + nho);
			if (tongDonvi >= (byte) 10){
				dataDecrypted[i] =(byte)(tongDonvi - (byte)10);
				nho = (byte)1;
			} else {
				nho = (byte)0;
				dataDecrypted[i] = tongDonvi;
			}
		}

		encrypt(dataDecrypted, (short)0);
	 Util.arrayCopy(bufferEn,  (short)0,data, (short)0, (short)256);
	 JCSystem.commitTransaction();
	}
	
	private void thanhtoan(APDU apdu){
		short byteRead = (short)(apdu.setIncomingAndReceive());
		short dataOffset = apdu.getOffsetCdata();
		// short dataLen = apdu.getIncomingLength();
		short i=0;
		// Gii m ; s tin l� 54.321 s l�u l� 000000054321
		// Khi np s tin l� 362 s chuyn th�nh 000000000362 khi gi
		
		decrypt(data, (short)0);
		//t�nh tng
		byte[] buffer = apdu.getBuffer();
		JCSystem.beginTransaction();
		byte nho = 0;//bin nh
		byte tongDonvi = 0;
		
		// Tr tin hin ti
		for (i=(short) 11; i>=0; i--) {
			
			tongDonvi = (byte)(dataDecrypted[i] - buffer[dataOffset + i] - nho);
			if (tongDonvi < (byte) 0){
				dataDecrypted[i] =(byte)((byte)10 + tongDonvi);
				nho = (byte)1;
			} else {
				nho = (byte)0;
				dataDecrypted[i] = tongDonvi;	
			}
		}
		if (nho == 1) {
			JCSystem.abortTransaction();
			ISOException.throwIt(CINEMA_CONSTANTS.SW_LACK_MONEY);
		}
		// T�nh tong thanh to�n
		nho = (byte)0;
		for (i=(short) 23; i>=12; i--) {
			tongDonvi = (byte)(dataDecrypted[i] + buffer[dataOffset + i -(short)12] + nho);
			if (tongDonvi >= (byte) 10){
				dataDecrypted[i] =(byte)(tongDonvi - (byte)10);
				nho = (byte)1;
			} else {
				nho = (byte)0;
				dataDecrypted[i] = tongDonvi;
			}
		}
		// Cong �iem
		nho = (byte)0;
		for (i=(short) 31; i>=(short)24; i--) {
			
			tongDonvi = (byte)(dataDecrypted[i] + buffer[dataOffset + i - (short)12] + nho);
			if (tongDonvi >= (byte) 10){
				dataDecrypted[i] =(byte)(tongDonvi - (byte)10);
				nho = (byte)1;
			} else {
				nho = (byte)0;
				dataDecrypted[i] = tongDonvi;
			}
		}
		// Tru �iem
		nho = (byte)0;
		for (i=(short) 31; i>=24; i--) {
			
			tongDonvi = (byte)(dataDecrypted[i] - buffer[dataOffset + i - (short)4] - nho);
			if (tongDonvi < (byte) 0){
				dataDecrypted[i] =(byte)((byte)10 + tongDonvi);
				nho = (byte)1;
			} else {
				nho = (byte)0;
				dataDecrypted[i] = tongDonvi;	
			}
		}
		// Th�m ma giam gi�
		short j = 0;
		short offSet= (short)32;

		while (byteRead >= (short)29) {
			
			byteRead = (short) (byteRead - (short)8);
			for (i=(short) offSet ; i<=(short)255; i += (short)8) {
				if (dataDecrypted[i] == 0) {
					Util.arrayCopy(buffer,  (short)(dataOffset + (short)29 + (short)8 * j),dataDecrypted, (short)i, (short)8);
					
					offSet = i;
					j++;
					break;
				}
				
			}
		}
		
		// X�a ma giam gia
		byte index = buffer[ (short)(dataOffset + (short)28)];
		// index = 100 th th�i
		
		if ( (short)index < (short)28 ) {
			for (i = (short)(32 + (short)(8 * index)); i < (short)(32 + (short)(8 * index) + (short)8) ; i ++) {
				dataDecrypted[i] = 0;
			}
			
		}
	encrypt(dataDecrypted, (short)0);
	Util.arrayCopy(bufferEn,  (short)0,data, (short)0, (short)256);
		
	
	 JCSystem.commitTransaction();
	}
	private void encrypt(byte[] temp, short offset) {
        aesCipher.init(aesKey, Cipher.MODE_ENCRYPT);

        aesCipher.doFinal(temp, offset , (short)256, bufferEn, (short) 0x00);
        
       
    }

    
    private void decrypt(byte[] decryptData,short offSet) {
        aesCipher.init(aesKey, Cipher.MODE_DECRYPT);
        
       
        aesCipher.doFinal(decryptData, offSet, (short) 256, dataDecrypted, (short) 0x00);
    
    }
    public Shareable getShareableInterfaceObject(AID clientAID, byte parameter) 
	{
		if(parameter != CINEMA_CONSTANTS.PARAMETER_INTERFACE) return null;
		return this;
	}
	

	public void changeInfoMarketEncode(byte[] keyAESNew ) {
	   
		
			
			try {
				
			decrypt(data, (short)0);
			Util.arrayCopyNonAtomic(dataDecrypted, (short) 0,data, (short) 0, (short)256);
			
			aesKey.setKey(keyAESNew, (short) 0);
			
			encrypt(data,(short)0);
			Util.arrayCopyNonAtomic(bufferEn,  (short)0,data, (short)0, (short)256);
			
			

		} catch(Exception e) {
			
		}
   	}
}
