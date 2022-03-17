package com.example.hotelstars.encryption;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class SymmtCrypto {   //secret chars
    private static final String SECRET = "Thisisthesecretkey";
    //algorithm
    private static final String ALGORITHM = "AES";
    private Key key;
    public Key getKey() {
        return key;}
    private static Key genkey(){
        return new SecretKeySpec( Arrays.copyOf(SECRET.getBytes(), 16), ALGORITHM );
    }
    //cipher supports encryption and decryption
    private Cipher cipher;
    public SymmtCrypto() {
        try {
            cipher = Cipher.getInstance( ALGORITHM );
            key = genkey();
        } catch (NoSuchAlgorithmException | NoSuchPaddingException ex) {
            ex.printStackTrace();}
    }


    //encrypt: Input: Original data; Output: cipherText
    @RequiresApi(api = Build.VERSION_CODES.O)
    public String encrypt(String data ) throws Exception{
        cipher.init(Cipher.ENCRYPT_MODE, key);
        //cipher introduces fundamental methods: update(), doFinal()
        byte[] cipherText = cipher.doFinal( data.getBytes() );
        return Base64.getEncoder().encodeToString(cipherText);}


    //decrypt: Input: cipherText; Output: Original data
    @RequiresApi(api = Build.VERSION_CODES.O)
    public String decrypt(String cipherText )  throws Exception{
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] dataBytes = cipher.doFinal( Base64.getDecoder().decode(cipherText) );
        return new String( dataBytes );
    }
}

