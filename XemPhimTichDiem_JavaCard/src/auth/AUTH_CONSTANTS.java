package auth;

public class AUTH_CONSTANTS {
	final static byte AUTH =(byte)0x63;
	
	final static byte[] DEAULT_PIN_CODE = new byte[]{1,2,3,4,5,6};
	
	// maximum number of incorrect tries before the
	// PIN is blocked
	final static byte PIN_TRY_LIMIT =(byte)0x05;

	//size PIN
	final static byte PIN_SIZE =(byte)0x06;

	// signal that the PIN verification failed
	final static short SW_VERIFICATION_FAILED = 0x6611;

	// signal the PIN validation is required
	// for a credit or a debit transaction
	//Sai ma pin
	final static short SW_PIN_VERIFICATION_REQUIRED = 0x6312;
	//Do dai ma pin khong du
	final static short SW_WRONG_PIN_LEN = 0x6613;
	//Qua so lan nhap sai
	final static short SW_OVER_TRY_TIMES = 0x6614;
	
	final static byte PARAMETER_INTERFACE = 0x12;
}
