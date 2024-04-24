package com.adanitownship.driver.utils;

import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class GzipUtils {

    public static String compress(String data) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzipOutputStream = new GZIPOutputStream(out);
        gzipOutputStream.write(data.getBytes("UTF-8"));
        gzipOutputStream.close();
        byte[] compressedBytes = out.toByteArray();
        out.close();
        // Encode the compressed data to Base64 for safe transmission
        return Base64.encodeToString(compressedBytes, Base64.DEFAULT);
    }


    public static String decompress(String encodedData) throws IOException {
        // Decode the Base64 encoded data (if encoded before transmission)
        byte[] compressedData = Base64.decode(encodedData, Base64.DEFAULT);

        // Decompress the data
        ByteArrayInputStream in = new ByteArrayInputStream(compressedData);
        GZIPInputStream gzipInputStream = new GZIPInputStream(in);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = gzipInputStream.read(buffer)) != -1) {
            out.write(buffer, 0, bytesRead);
        }
        gzipInputStream.close();
        in.close();
        out.close();

        // Return the decompressed data as a String
        return out.toString("UTF-8");
    }

   /* public static String decrypt(String encryptedData) throws Exception {
        byte[] keyValue = "4c5cfefcc958f1748eb31dcc609736FK".getBytes();
        Key key = new SecretKeySpec(keyValue, "AES");
        Cipher c = Cipher.getInstance("AES/CBC/PKCS7Padding");
        c.init(DECRYPT_MODE, key, new IvParameterSpec("K8Csuc2GiKvetPZg".getBytes()));
        byte[] decordedValue = Base64.decode(encryptedData.getBytes(), Base64.DEFAULT);
        byte[] decValue = c.doFinal(decordedValue);
        return new String(decValue);
    }*/

    public static String decrypt(String encrypted) throws Exception {
        byte[] keyValue = "4c5cfchyu4ioj1748eb31dcc609736FK".getBytes();
        SecretKeySpec skeySpec = new SecretKeySpec(keyValue, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec("K8Csuh67T8GKvPZg".getBytes());
        Cipher ecipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
        ecipher.init(Cipher.DECRYPT_MODE, skeySpec, ivSpec);
        byte[] raw = Base64.decode(encrypted, Base64.DEFAULT);
        byte[] originalBytes = ecipher.doFinal(raw);
        String original = new String(originalBytes, "UTF8");
        Log.e("decrypt" , "asd " + original);
        return original;

    }
    public static String encrypt(String message){
        try {
            byte[] keyValue = "4c5cfchyu4ioj1748eb31dcc609736FK".getBytes();

            byte[] srcBuff = message.getBytes("UTF8");

            SecretKeySpec skeySpec = new SecretKeySpec(keyValue, "AES");
            IvParameterSpec ivSpec = new IvParameterSpec("K8Csuh67T8GKvPZg".getBytes());
            Cipher ecipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            ecipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivSpec);

            byte[] dstBuff = ecipher.doFinal(srcBuff);

            return Base64.encodeToString(dstBuff, Base64.DEFAULT);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (NoSuchPaddingException e) {
            throw new RuntimeException(e);
        } catch (InvalidAlgorithmParameterException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        } catch (BadPaddingException e) {
            throw new RuntimeException(e);
        } catch (IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        }

    }



}
