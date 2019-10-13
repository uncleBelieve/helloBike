package com.coder520.mamabike.Security;

import com.sun.org.apache.xml.internal.security.utils.Base64;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;


import java.io.InputStream;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * 加密解密工具类
 * @author HJY
 * @create 2017-07-31 15:28
 **/

public class RSAUtil {

    /**
     * 私钥字符串
     */
    private static String PRIVATE_KEY ="";
    /**
     * 公钥字符串
     */
    private static String PUBLIC_KEY ="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCcnhr8mVEZMhIaVrIDHGPd P5KX7F1+C+GvERUGiAo8JDADHzbfP0/qNz9bl0+RJ+knkpm9lWInz15AKxud eNHnJPo1MF7IjCDFjzGhGF8g4DFrrQnyC5e6+4eQmklYZj5mgeQiVltfWCGM XysL/hdeh/6KFgOoO4sdOMFQVqT3xwIDAQAB";


    public static final String KEY_ALGORITHM = "RSA";



    /**
     * 读取私钥字符串
     * @throws Exception
     */

    public static void convert() throws Exception {
        byte[] data = null;

        try {
            InputStream is = RSAUtil.class.getResourceAsStream("/enc_pri");
            int length = is.available();
            data = new byte[length];
            is.read(data);
        } catch (Exception e) {
        }

        String dataStr = new String(data);
        try {
            PRIVATE_KEY = dataStr;
        } catch (Exception e) {
        }

        if (PRIVATE_KEY == null) {
            throw new Exception("Fail to retrieve key");
        }
    }



    /**
     * 私钥解密
     *
     * @param data
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPrivateKey(byte[] data) throws Exception {
        convert();
        byte[] keyBytes = Base64Util.decode(PRIVATE_KEY);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        return cipher.doFinal(data);
    }

    /**
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPublicKey(byte[] data, String key) throws Exception {
        byte[] keyBytes = Base64Util.decode(key);
        X509EncodedKeySpec pkcs8KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicKey = keyFactory.generatePublic(pkcs8KeySpec);

        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
  //      Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(data);
    }


    public static void main(String[] args) throws Exception {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        keyPairGen.initialize(1024);
        KeyPair keyPair = keyPairGen.generateKeyPair();    //返回一对keyPair，可以拿到公钥私钥
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();
        System.out.println(Base64Util.encode(privateKey.getEncoded()));   //需要的是字符串，返回对象利用Base64转码
        System.out.println(Base64Util.encode(publicKey.getEncoded()));

        String data = "老王来了、、、";
        byte[] enResult =  encryptByPublicKey(data.getBytes("UTF-8"),PUBLIC_KEY);
        System.out.println(enResult.toString());
        //解密
        byte [] deResult =   decryptByPrivateKey(enResult);
        System.out.println(new String(deResult,"UTF-8"));

   }

}
