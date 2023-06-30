package auth;

import javacard.security.*;
import javacardx.crypto.Cipher;
import javacard.framework.*;
import auth.AUTH_CONSTANTS;

public class pin extends Applet implements PinInterface
{
    private byte[] keyAES;
    private MessageDigest mDig ;
	private static OwnerPIN pin;
	private RSAPrivateKey rsaPrivKey;
	private RSAPublicKey rsaPubKey;
	private Signature rsaSig;
	private short sigLen;
	private byte[] sig_buffer;
	private final byte[] infoAID 
		= new byte[]{(byte)0x11, (byte)0x22, (byte)0x33, 
		(byte)0x44, (byte)0x55, (byte)0x88, (byte)0x01};
		
	private final byte[] marketAID 
		= new byte[]{(byte)0x11, (byte)0x22, (byte)0x33, 
		(byte)0x44, (byte)0x55, (byte)0x88, (byte)0x02};
	
	public static void install(byte[] bArray, short bOffset, byte bLength) 
	{
		new pin(bArray, bOffset, bLength);
	}
	
	private pin(byte[] bArray, short bOffset, byte bLength) {
		
		// Tạo instance
		mDig = MessageDigest.getInstance(MessageDigest.ALG_MD5, true);
		// Lấy length applet id
		byte aIDLen = bArray[bOffset];
		
		if(aIDLen == 0){
			register();
		}else{
			register(bArray, (short) (bOffset + 1), aIDLen);
		}
		
		// Khoi tao ma pin (123456)
		// Do dai ma pin toi da la 8
		pin = new OwnerPIN(AUTH_CONSTANTS.PIN_TRY_LIMIT, AUTH_CONSTANTS.PIN_SIZE);
		pin.update(AUTH_CONSTANTS.DEAULT_PIN_CODE, (short) 0, (byte) AUTH_CONSTANTS.DEAULT_PIN_CODE.length);
		
		// hash pin by md5
		
		keyAES = hashMD5Message(AUTH_CONSTANTS.DEAULT_PIN_CODE);
    }
    
    public void initChuky() {
	    
	    sigLen = (short)(KeyBuilder.LENGTH_RSA_1024/8);
		sig_buffer = new byte[sigLen];
		
		rsaSig =
		Signature.getInstance(Signature.ALG_RSA_SHA_PKCS1,false);
		
		rsaPrivKey =
		(RSAPrivateKey)KeyBuilder.buildKey(KeyBuilder.TYPE_RSA_PRIVATE,
		(short)(8*sigLen),false);
		
		rsaPubKey =
		(RSAPublicKey)KeyBuilder.buildKey(KeyBuilder.TYPE_RSA_PUBLIC,
		(short)(8*sigLen), false);

		KeyPair keyPair = new KeyPair(KeyPair.ALG_RSA,
		(short)(8*sigLen));
		keyPair.genKeyPair();
		rsaPrivKey = (RSAPrivateKey)keyPair.getPrivate();
		rsaPubKey = (RSAPublicKey)keyPair.getPublic();
		
    }
    
    private void exportPublicModulus(APDU apdu) {
		byte buffer[] = apdu.getBuffer();
		short expLenmo = rsaPubKey.getModulus(buffer, (short) 0);
		apdu.setOutgoingAndSend((short) 0, (short) (expLenmo));
	}
	
	private void exportPublicExponent(APDU apdu) {
		byte buffer[] = apdu.getBuffer();
		short expLenex = rsaPubKey.getExponent(buffer, (short) 0);
		apdu.setOutgoingAndSend((short) 0, (short) expLenex);
	}
    // private void rsaSign(APDU apdu)//tao chu ky so
	// {
	// rsaSig.init(rsaPrivKey, Signature.MODE_SIGN);
	// rsaSig.sign(s3, (short)0, (short)(s3.length),
	// sig_buffer, (short)0);
	// apdu.setOutgoing();
	// apdu.setOutgoingLength(sigLen);

	// apdu.sendBytesLong(sig_buffer, (short)0, sigLen);
	// }
	
	private void signData(APDU apdu){
		byte buffer[] = apdu.getBuffer();
		
		rsaSig.init(rsaPrivKey, Signature.MODE_SIGN);
		rsaSig.sign(buffer, ISO7816.OFFSET_CDATA, (short)20, sig_buffer, (short)0);
	
		apdu.setOutgoing();
		apdu.setOutgoingLength((short)(sigLen));
		apdu.sendBytesLong(sig_buffer, (short)0, sigLen);
	}
    public boolean select() {
		return true;
	}
   
	public void deselect() {
		pin.reset();
	}

	public Shareable getShareableInterfaceObject(AID clientAID, byte parameter) 
	{
		if(parameter != AUTH_CONSTANTS.PARAMETER_INTERFACE) return null;
		return this;
	}
    
    public byte[] getHashPin(){
		return keyAES;
	}
	
	public void process(APDU apdu)
	{
		if (selectingApplet()){return;}
		
		byte[] buf = apdu.getBuffer();
		
		 // check SELECT APDU command
		if((buf[ISO7816.OFFSET_CLA] == 0x00
			 && buf[ISO7816.OFFSET_INS] == (byte)(0xA4)))
			return;
		
		if (buf[ISO7816.OFFSET_CLA] != APPLET_CONSTANTS.CLA)
			ISOException.throwIt(ISO7816.SW_CLA_NOT_SUPPORTED);
		
		switch (buf[ISO7816.OFFSET_INS])
		{
		case APPLET_CONSTANTS.UPDATE:
			updatePIN(apdu);
			return;
		case APPLET_CONSTANTS.VERIFY:
			verify(apdu);
			return;
		case APPLET_CONSTANTS.UNBLOCK: 
			unblock();
			return;
		case APPLET_CONSTANTS.EXPORT_PUBK_MODU:
				exportPublicModulus(apdu);
			break;
		case APPLET_CONSTANTS.EXPORT_PUBK_EXPO:
			exportPublicExponent(apdu);
		break;
		
		case APPLET_CONSTANTS.SIGN_DATA:
			signData(apdu);
		break;
		
		default:
			ISOException.throwIt(ISO7816.SW_INS_NOT_SUPPORTED);
		}
	}
	
	
	
	private void updatePIN(APDU apdu){
		byte byteRead = (byte)(apdu.setIncomingAndReceive());
		// short dataLen = apdu.getIncomingLength();
		short i=0;
		
		if(byteRead != (byte)(2*AUTH_CONSTANTS.PIN_SIZE))
			ISOException.throwIt(AUTH_CONSTANTS.SW_WRONG_PIN_LEN);
		
		
		byte[] buffer = apdu.getBuffer();
		
		// check code pin old 	
		if (!pin.check(buffer, ISO7816.OFFSET_CDATA, AUTH_CONSTANTS.PIN_SIZE)) {
			ISOException.throwIt(AUTH_CONSTANTS.SW_VERIFICATION_FAILED);
		}
			
		byte[] tempPIN = new byte[ AUTH_CONSTANTS.PIN_SIZE];
		Util.arrayCopy(
					buffer, 
					(short) (ISO7816.OFFSET_CDATA +  AUTH_CONSTANTS.PIN_SIZE),
					tempPIN, 
					(short)0, AUTH_CONSTANTS.PIN_SIZE
		);
		JCSystem.beginTransaction();
		byte[] keyAESNew = hashMD5Message(tempPIN);
		AID masterAID = JCSystem.lookupAID(
						infoAID, 
						(short)0, 
						(byte)infoAID.length);
		
		InfoInterface sioInfo = (InfoInterface) (JCSystem.getAppletShareableInterfaceObject(masterAID, (byte)0x11));
		sioInfo.changeInfoEncode(keyAES, keyAESNew);
		
		masterAID = JCSystem.lookupAID(
						marketAID, 
						(short)0, 
						(byte)marketAID.length);
		
		CinemaInterface sioInfoMarket = (CinemaInterface) (JCSystem.getAppletShareableInterfaceObject(masterAID, (byte)0x11));
		sioInfoMarket.changeInfoMarketEncode(keyAESNew);
		keyAES = keyAESNew;
		pin.update(tempPIN, (short) 0, (byte)tempPIN.length);
	
		JCSystem.commitTransaction();
	}
	
	private void verify(APDU apdu) {
		byte[] buffer = apdu.getBuffer();
		
		byte byteRead = (byte)(apdu.setIncomingAndReceive());
		// Check pin length
		if(byteRead != AUTH_CONSTANTS.PIN_SIZE){
			ISOException.throwIt(AUTH_CONSTANTS.SW_WRONG_PIN_LEN);
		}
		// Compare pin
		if ( pin.check(buffer, ISO7816.OFFSET_CDATA,byteRead) == false ){
			if(pin.getTriesRemaining() == 0){
				ISOException.throwIt(AUTH_CONSTANTS.SW_OVER_TRY_TIMES);
			}
			ISOException.throwIt(AUTH_CONSTANTS.SW_VERIFICATION_FAILED);
		}
	} 
	
	private void checkValidatePIN(){
		if (!pin.isValidated()){
			if(pin.getTriesRemaining() == 0){
				ISOException.throwIt(AUTH_CONSTANTS.SW_OVER_TRY_TIMES);
			}
			ISOException.throwIt(AUTH_CONSTANTS.SW_PIN_VERIFICATION_REQUIRED);
		}
	}
	
	private void unblock(){
		pin.resetAndUnblock();
	}
	
	private byte[] hashMD5Message(byte[] input) {
		byte[] output = new byte[APPLET_CONSTANTS.KEY_SIZE];
		
		mDig.reset();
		mDig.doFinal(input, (short)0, (short)input.length, output, (short) 0);
		
		return output;
	}
	

}
