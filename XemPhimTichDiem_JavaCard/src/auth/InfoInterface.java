package auth;

import javacard.security.*;
import javacardx.crypto.Cipher;
import javacard.framework.Shareable;

public interface InfoInterface extends Shareable{
	 public void changeInfoEncode(byte[] keyAESOld , byte[] keyAESNew );
}
