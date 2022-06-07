package com.j23.server.services.encryptDecrypt;

import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Service
public class EncryptDecryptService {

  public static String secretKey = "o9szYIOq1rRMiouNhNvaq96lqUvCekxR";

  public static String decrypt(String ciphertext) throws Exception {
    SecretKey sk = getSecretKey(secretKey);
    Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
    cipher.init(Cipher.DECRYPT_MODE, sk);
    return new String(cipher.doFinal(Base64.getDecoder().decode(ciphertext)));
    //return new String(cipher.doFinal(base64Decode("ASDASDADS")));
  }

  public static SecretKey getSecretKey(String secretKey) throws Exception {
    byte[] decodeSecretKey = Base64.getDecoder().decode(secretKey);
    //byte[] decodeSecretKey = base64Decode(secretKey);
    return new SecretKeySpec(decodeSecretKey, 0, decodeSecretKey.length, "AES");
  }

}
