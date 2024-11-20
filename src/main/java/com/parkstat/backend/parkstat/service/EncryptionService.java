package com.parkstat.backend.parkstat.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Service
public class EncryptionService {
    @Value("${parking_key}")
    private String key;

    public String encryptKey(String accessKey) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedBytes = cipher.doFinal(accessKey.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public String decryptKey(String accessKey) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decodedBytes = Base64.getDecoder().decode(accessKey);
        byte[] originalBytes = cipher.doFinal(decodedBytes);
        return new String(originalBytes);
    }
}
