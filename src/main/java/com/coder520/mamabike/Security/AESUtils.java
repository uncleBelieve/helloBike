package com.coder520.mamabike.Security;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;

/**
* @Description: 测试加密类
* @Author: HJY
* @Date: 2018/7/14 
*/ 
public class AESUtils {


    public static final String KEY_ALGORITHM = "AES";
    public static final String KEY_ALGORITHM_MODE = "AES/CBC/PKCS5Padding";


    /**
     * AES对称加密
     * @param data
     * @param key key需要16位
     * @return
     */
    public static String encrypt(String data , String key) {
        try {
            SecretKeySpec spec = new SecretKeySpec(key.getBytes("UTF-8"),KEY_ALGORITHM);
            Cipher cipher = Cipher.getInstance(KEY_ALGORITHM_MODE);    //生产出KEY_ALGORITHM_MODE这个模式实例
            cipher.init(Cipher.ENCRYPT_MODE , spec,new IvParameterSpec(new byte[cipher.getBlockSize()])); //实例化KEY_ALGORITHM_MODE
            byte[] bs = cipher.doFinal(data.getBytes("UTF-8"));
            return Base64Util.encode(bs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  null;
    }


    /**
     * AES对称解密 key需要16位
     * @param data
     * @param key
     * @return
     */
    public static String decrypt(String data, String key) {
        try {
            SecretKeySpec spec = new SecretKeySpec(key.getBytes("UTF-8"), KEY_ALGORITHM);
            Cipher cipher = Cipher.getInstance(KEY_ALGORITHM_MODE);
            cipher.init(Cipher.DECRYPT_MODE , spec , new IvParameterSpec(new byte[cipher.getBlockSize()]));
            byte[] originBytes = Base64Util.decode(data);
            byte[] result = cipher.doFinal(originBytes);
            return new String(result,"UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  null;
    }

    public static void main(String[] args) throws Exception {

        String key = "123456789abcdfgt";
        String dataToEn= "{'mobile':'18980840843','code':'6666','platform':'android'}";//用户信息采用AES密钥加密，AES密钥再采用RSA公钥加密，解密的话RSA私钥解密
        String enResult = encrypt(dataToEn,key);
        System.out.println(enResult);//AES加密数据



        byte [] enKey = RSAUtil.encryptByPublicKey(key.getBytes("UTF-8"),"MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCcnhr8mVEZMhIaVrIDHGPd P5KX7F1+C+GvERUGiAo8JDADHzbfP0/qNz9bl0+RJ+knkpm9lWInz15AKxud eNHnJPo1MF7IjCDFjzGhGF8g4DFrrQnyC5e6+4eQmklYZj5mgeQiVltfWCGM XysL/hdeh/6KFgOoO4sdOMFQVqT3xwIDAQAB");
        System.out.println(new String(enKey,"UTF-8"));
        String baseKey= Base64Util.encode(enKey);
        System.out.println(baseKey);//RSA加密AES的秘钥




       byte [] de = Base64Util.decode(baseKey);
       byte [] deKeyResult = RSAUtil.decryptByPrivateKey(de);
        System.out.println(new String(deKeyResult));//服务端RSA解密AES的key


     }
}
