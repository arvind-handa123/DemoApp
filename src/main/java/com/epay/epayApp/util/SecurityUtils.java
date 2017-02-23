/**
 * 
 */
package com.epay.epayApp.util;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Aeon
 *
 */
public class SecurityUtils {
	
	private static final Logger LOGGER  = LoggerFactory.getLogger(SecurityUtils.class);
	private static final String ALGORITHM = "AES";
	public static String API_SECRET_KEY="1441177929934000";
	
	/**
	 *     
	 * @param accessToken
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws InvalidKeyException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 */
		    public static String encript(String accessToken) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException{
		    	Cipher cipher = Cipher.getInstance(ALGORITHM);
		    	SecretKeySpec spec = new SecretKeySpec(API_SECRET_KEY.getBytes(), ALGORITHM);
		    	cipher.init(Cipher.ENCRYPT_MODE, spec);
		    	byte[]output = cipher.doFinal(accessToken.getBytes());
		    	byte[] encodedBytes = Base64.encodeBase64(output);
		    	return new String(encodedBytes);
		    	
		    
		    }
		    
		 /**
		  * @param encodedAccessToken
		  * @param key
		  * @return
		  * @throws NoSuchAlgorithmException
		  * @throws NoSuchPaddingException
		  * @throws InvalidKeyException
		  * @throws IllegalBlockSizeException
		  * @throws BadPaddingException
		  * @throws DecoderException
		  */
		    public static String decript(String encodedAccessToken,String key) {
		    	Cipher cipher;
				try {
					cipher = Cipher.getInstance(ALGORITHM);
			    	SecretKeySpec spec = new SecretKeySpec(key.getBytes(), ALGORITHM);
			    	cipher.init(Cipher.DECRYPT_MODE, spec);
			    	byte[] decodedBytes = Base64.decodeBase64(encodedAccessToken);
			    	byte[] output = cipher.doFinal(decodedBytes);
		    	return new String(output);
				} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
					LOGGER.debug("exception {} raised while decoding token",e);
				}
				// if unable to decript token return key
				return key;
		    }
		    

}
