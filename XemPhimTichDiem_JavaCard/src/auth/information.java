package auth;

import javacard.framework.*;
import javacardx.crypto.Cipher;
import javacard.security.*;
import auth.PinInterface;
import javacardx.apdu.ExtendedLength;
import auth.InfoInterface;
public class information extends Applet implements INFO_CONSTANTS, ExtendedLength, InfoInterface
{
	// variable	
	boolean khoitao = false;
	
	
	// khai bao bien
	private Cipher aesCipher;
    private AESKey aesKey;
    
    // variable	 encrypt
	
	private static byte[] infoEn = new byte[256];
	
	private static byte[] avatarEn = new byte[(short)32000];
	private static byte[] buffer;
	
	private static byte[] bufferImg;
	private static byte[] bufferEn;
	private static byte[] dataDecrypted;
	private static short countImage, countInfo;	
	private final byte[] pinAID 
		= new byte[]{(byte)0x11, (byte)0x22, (byte)0x33, 
		(byte)0x44, (byte)0x55, (byte)0x88, (byte)0x00};

	public static void install(byte[] bArray, short bOffset, byte bLength) 
	{
		new information(bArray,bOffset,bLength);
	}
	
	private information(byte[] bArray, short bOffset, byte bLength){
		
		// Khoi tao AES
		// Tạo đối tượng cipher sử dụng thuật toán AES mode CBC 
		aesCipher = Cipher.getInstance(Cipher.ALG_AES_BLOCK_128_CBC_NOPAD, false);
		// Tạo khóa aes
        aesKey = (AESKey) KeyBuilder.buildKey(KeyBuilder.TYPE_AES, KeyBuilder.LENGTH_AES_128, false);
        // Tạo buffer tạm thời, buffer này sẽ bị xóa khi thẻ thông minh k còn được chọn
        buffer = JCSystem.makeTransientByteArray ((short) 256,JCSystem.CLEAR_ON_DESELECT);
        bufferEn = JCSystem.makeTransientByteArray((short) 256, JCSystem.CLEAR_ON_DESELECT);
        dataDecrypted = JCSystem.makeTransientByteArray((short) 256, JCSystem.CLEAR_ON_DESELECT);
        
        register();
	}
	
	public Shareable getShareableInterfaceObject(AID clientAID, byte parameter) 
	{
		if(parameter != INFO_CONSTANTS.PARAMETER_INTERFACE) return null;
		return this;
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
		short recvLen = apdu.setIncomingAndReceive();
		short dataLen = apdu.getIncomingLength();
		short countImg;
		byte flag;
		short dataOffset = apdu.getOffsetCdata();
		switch (buf[ISO7816.OFFSET_INS])
		{
		case INS_INSERT:
			flag = (byte)0;
			// //  copy length array 
			
			JCSystem.beginTransaction();
			countImage= (short)0;
			while (recvLen > 0) {
				if (flag < COUNT_AVATAR) {
					short i;	
					LoopInfo:
					for(i = (short)(dataOffset);i<(short)(dataOffset +recvLen);i++ ){
						if (buf[i] == (byte) 0x01) {
							flag++;
							if(flag == COUNT_AVATAR) break;
						}
					}
					countInfo = (short)(i - dataOffset);
					Util.arrayCopy(buf, (short)(dataOffset), buffer , (short)0, countInfo);
				
					encrypt(countInfo);
					
					Util.arrayCopy(bufferEn, (byte) 0,infoEn, (byte) 0, (short) 256);
					i++;
					short lenImg = (short)(dataLen - i + dataOffset);
					
					bufferImg = new byte[lenImg];
					for(short j = (short)i;j < (short)(dataOffset + recvLen);j++ ){
						bufferImg[j - i] = buf[j];
						countImage++;	
					}
				} else {

						Util.arrayCopyNonAtomic(buf, dataOffset,bufferImg, countImage, recvLen);
						countImage += recvLen;
				}
				recvLen = apdu.receiveBytes(dataOffset);
				

			}
			countImg = countImage;
			while (countImg > (short) 256) {
				Util.arrayCopyNonAtomic(bufferImg,  (short)(countImage - countImg), buffer , (short)0, (short)256);
				encrypt((short)256);
				Util.arrayCopyNonAtomic(bufferEn, (byte) 0, avatarEn, (short)(countImage - countImg),(short) 256);
				countImg -= (short)256;
			}
				Util.arrayCopyNonAtomic(bufferImg,  (short)(countImage - countImg), buffer , (short)0,  countImg);
				encrypt(countImg);
				Util.arrayCopyNonAtomic(bufferEn, (byte) 0, avatarEn, (short)(countImage - countImg), (short) 256);
			
			// avatarEn = 	bufferImg;	
			if (!khoitao) {
				//sinh chu ky so
			AID masterAID = JCSystem.lookupAID(
					pinAID, 
					(short)0, 
					(byte)pinAID.length);
			PinInterface sio = (PinInterface)
			(JCSystem.getAppletShareableInterfaceObject(masterAID, (byte)0x12));
			
				sio.initChuky();
				khoitao = true;
				apdu.setOutgoing();
				apdu.setOutgoingLength((short)1);
				apdu.sendBytesLong(new byte[]{(byte)1}, (short)0,(short)1);
			}
			JCSystem.commitTransaction();
			/////
			
			break;	
	
		case INS_THONGTIN:
			// decrypt
			short le = apdu.setOutgoing();

			
			decrypt(infoEn);
			
			short length =(short)(countImage + countInfo + 1);
			
			apdu.setOutgoingLength(length);
			apdu.sendBytesLong(dataDecrypted,(short) 0,countInfo);
			apdu.sendBytesLong(new byte[]{0x01}, (short)0,(short)1);

			countImg = countImage;
			short pointer = 0;
			short sendLen = 0;
			
			while (countImg > (short) 256) {
				Util.arrayCopyNonAtomic(avatarEn,  (short)(countImage - countImg), buffer , (short)0, (short)256);
				decrypt(buffer);
				apdu.sendBytesLong(dataDecrypted,(short) 0,(short)128);
				apdu.sendBytesLong(dataDecrypted,(short) 128,(short)128);
				countImg -= (short)256;
			}
				Util.arrayCopyNonAtomic(avatarEn,  (short)(countImage - countImg), buffer , (short)0, (short) 256);
				decrypt(buffer);
				apdu.sendBytesLong(dataDecrypted,(short) 0,countImg);
			
			
			break;
		case INS_CHANGE_INFO:

			countImg= (short)0;

			flag = (byte)0;
			// //  copy length array 
			
			JCSystem.beginTransaction();
				
			short i;	
			LoopInfo:
			for(i = (short)(dataOffset);i<(short)(dataOffset +recvLen);i++ ){
				if (buf[i] == (byte) 0x01) {
					flag++;
					if(flag == COUNT_AVATAR) break;
				}
			}
			countInfo = (short)(i - dataOffset);
			Util.arrayCopy(buf, (short)(dataOffset), buffer , (short)0, countInfo);
		
			encrypt(countInfo);
			
			Util.arrayCopy(bufferEn, (byte) 0,infoEn, (byte) 0, (short) 256);
			JCSystem.commitTransaction();
			break;
		case INS_CHANGE_IMAGE:
			
			

			flag = (byte)0;
			// //  copy length array 
			
			JCSystem.beginTransaction();
			countImage = dataLen;
			countImg= (short)0;
			bufferImg = new byte[dataLen];
			while (recvLen > 0) {
				Util.arrayCopy(buf, dataOffset,bufferImg, countImg, recvLen);
				countImg += recvLen;
				
				recvLen = apdu.receiveBytes(dataOffset);
				

			}
			while (countImg > (short) 256) {
				Util.arrayCopyNonAtomic(bufferImg,  (short)(countImage - countImg), buffer , (short)0, (short)256);
				encrypt((short)256);
				Util.arrayCopyNonAtomic(bufferEn, (byte) 0, avatarEn, (short)(countImage - countImg),(short) 256);
				countImg -= (short)256;
			}
				Util.arrayCopyNonAtomic(bufferImg,  (short)(countImage - countImg), buffer , (short)0,  countImg);
				encrypt(countImg);
				Util.arrayCopyNonAtomic(bufferEn, (byte) 0, avatarEn, (short)(countImage - countImg), (short) 256);
			JCSystem.commitTransaction();
			break;
		default:
			apdu.setOutgoing();
			apdu.setOutgoingLength((short)10);
			apdu.sendBytesLong(new byte[]{(byte)10,(byte)10,(byte)10,(byte)10,(byte)10,(byte)10,(byte)10,(byte)10,(byte)10,(byte)10},(short)0,(short) 10);
			break;
		}
	}
	
	/*AES*/
	private void encrypt(short count) {
        aesCipher.init(aesKey, Cipher.MODE_ENCRYPT);
       
	    byte[] temp = new byte[256];
    	
		Util.arrayCopy(buffer, (byte) 0,temp, (byte) 0, count);
        
        aesCipher.doFinal(temp, (short) 0 , (short)256, bufferEn, (short) 0x00);
        
        
    }

    
    private void decrypt(byte[] decryptData) {
        aesCipher.init(aesKey, Cipher.MODE_DECRYPT);
        
       
        aesCipher.doFinal(decryptData, (short)0, (short) 256, dataDecrypted, (short) 0x00);
        // short i=0;
        // while (dataDecrypted[i] != (byte)0x00) i++;
		// byte[] result = new byte[i];
        // Util.arrayCopy(dataDecrypted, (byte) 0,result, (byte) 0, i);
        // return result;
    }
   
   	public void changeInfoEncode(byte[] keyAESOld, byte[] keyAESNew ) {
			decrypt(infoEn);
			aesKey.setKey(keyAESNew, (short) 0);
			Util.arrayCopyNonAtomic(dataDecrypted, (byte) 0,buffer, (byte) 0, (short)256);
			encrypt((short)256);
			aesKey.setKey(keyAESOld, (short) 0);
			Util.arrayCopyNonAtomic(bufferEn, (byte) 0,infoEn, (byte) 0, (short) 256);
			short countImg = countImage;
			while (countImg > (short) 256) {
				Util.arrayCopyNonAtomic(avatarEn,  (short)(countImage - countImg), buffer , (short)0, (short)256);
				decrypt(buffer);
				aesKey.setKey(keyAESNew, (short) 0);
				Util.arrayCopyNonAtomic(dataDecrypted, (byte) 0,buffer, (byte) 0, (short)256);
				encrypt((short)256);
				aesKey.setKey(keyAESOld, (short) 0);
				Util.arrayCopyNonAtomic(bufferEn, (byte) 0,avatarEn, (short)(countImage - countImg), (short) 256);
				countImg -= (short)256;
			}
			
			Util.arrayCopyNonAtomic(avatarEn,  (short)(countImage - countImg), buffer , (short)0, (short)256);
				decrypt(buffer);
				aesKey.setKey(keyAESNew, (short) 0);
				Util.arrayCopyNonAtomic(dataDecrypted, (byte) 0,buffer, (byte) 0, (short)256);
				encrypt((short)256);
				
				Util.arrayCopyNonAtomic(bufferEn, (byte) 0,avatarEn, (short)(countImage - countImg), (short) 256);
			
			
			// decrypt(infoEn);
			// aesKey.setKey(keyAESNew, (short) 0);
			// Util.arrayCopy(dataDecrypted, (byte) 0,buffer, (byte) 0, 256);
			// encrypt((short)256);
			// Util.arrayCopyNonAtomic(bufferEn, (byte) 0,infoEn, (byte) 0, (short) 256);
		
   	}
}
