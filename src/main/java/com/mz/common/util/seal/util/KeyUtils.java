package com.mz.common.util.seal.util;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import java.io.File;
import java.io.IOException;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class KeyUtils {
    /**
     * 算法名称
     */
    private static final String ALGORITHM = "RSA";

    /**
     * 密钥长度
     */
    private static final int KEY_SIZE = 2048;
    private static final String PUBLIC_KEY = "publicKey";
    private static final String PRIVATE_KEY = "privateKey";
    private static final Map<String, Object> keyMap = new ConcurrentHashMap<String, Object>();

    /**
     * 随机生成密钥对（包含公钥和私钥）
     */
    public static KeyPair generateKeyPair(String keyType) throws NoSuchAlgorithmException {
        String algorithm = "";
        if (keyType.equalsIgnoreCase("rsa")) {
            algorithm = "RSA";
        }
        // 获取指定算法的密钥对生成器
        KeyPairGenerator gen = KeyPairGenerator.getInstance(algorithm);
        // 初始化密钥对生成器（指定密钥长度, 使用默认的安全随机数源）
        gen.initialize(KEY_SIZE);
        // 随机生成一对密钥（包含公钥和私钥）
        return gen.generateKeyPair();
    }

    /**
     * 将 公钥/私钥 编码后以 Base64 的格式保存到指定文件
     */
    public static void saveKeyForEncodedBase64(Key key, File keyFile) throws IOException {
        // 获取密钥编码后的格式
        byte[] encBytes = key.getEncoded();

        // 转换为 Base64 文本
        String encBase64 = new BASE64Encoder().encode(encBytes);

        // 保存到文件
        IOUtils.writeFile(encBase64, keyFile);
    }

    /**
     * 根据公钥的 Base64 文本创建公钥对象
     */
    public static PublicKey getPublicKey(String pubKeyBase64) throws Exception {
        // 把 公钥的Base64文本 转换为已编码的 公钥bytes
        byte[] encPubKey = new BASE64Decoder().decodeBuffer(pubKeyBase64);

        // 创建 已编码的公钥规格
        X509EncodedKeySpec encPubKeySpec = new X509EncodedKeySpec(encPubKey);

        // 获取指定算法的密钥工厂, 根据 已编码的公钥规格, 生成公钥对象
        return KeyFactory.getInstance(ALGORITHM).generatePublic(encPubKeySpec);
    }

    /**
     * 根据私钥的 Base64 文本创建私钥对象
     */
    public static PrivateKey getPrivateKey(String priKeyBase64) throws Exception {
        // 把 私钥的Base64文本 转换为已编码的 私钥bytes
        byte[] encPriKey = new BASE64Decoder().decodeBuffer(priKeyBase64);

        // 创建 已编码的私钥规格
        PKCS8EncodedKeySpec encPriKeySpec = new PKCS8EncodedKeySpec(encPriKey);

        // 获取指定算法的密钥工厂, 根据 已编码的私钥规格, 生成私钥对象
        return KeyFactory.getInstance(ALGORITHM).generatePrivate(encPriKeySpec);
    }

    /**
     * 公钥加密数据
     */
    public static byte[] encrypt(byte[] plainData, PublicKey pubKey) throws Exception {
        // 获取指定算法的密码器
        Cipher cipher = Cipher.getInstance(ALGORITHM);

        // 初始化密码器（公钥加密模型）
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);

        // 加密数据, 返回加密后的密文
        return cipher.doFinal(plainData);
    }

    /**
     * 私钥解密数据
     */
    public static byte[] decrypt(byte[] cipherData, PrivateKey priKey) throws Exception {
        // 获取指定算法的密码器
        Cipher cipher = Cipher.getInstance(ALGORITHM);

        // 初始化密码器（私钥解密模型）
        cipher.init(Cipher.DECRYPT_MODE, priKey);

        // 解密数据, 返回解密后的明文
        return cipher.doFinal(cipherData);
    }

    // 一般要求是2048位 或更多
    public static Map genKey() throws NoSuchAlgorithmException {
        //获得对象 KeyPairGenerator 参数 RSA 1024个字节
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(ALGORITHM);
        keyPairGen.initialize(2048);
        //通过对象 KeyPairGenerator 获取对象KeyPair
        KeyPair keyPair = keyPairGen.generateKeyPair();
        //通过对象 KeyPair 获取RSA公私钥对象RSAPublicKey RSAPrivateKey
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        //公私钥对象存入map中
        keyMap.put(PUBLIC_KEY, publicKey);
        keyMap.put(PRIVATE_KEY, privateKey);
        return keyMap;
    }

    public static void main(String[] args) throws NoSuchAlgorithmException {
        Map map = genKey();
        System.out.print("-----");
    }
}
