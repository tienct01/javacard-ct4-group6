package auth;

import javacard.security.*;
import javacardx.crypto.Cipher;
import javacard.framework.Shareable;

public interface CinemaInterface extends Shareable{
	 public void changeInfoMarketEncode(byte[] keyAESNew );
}
