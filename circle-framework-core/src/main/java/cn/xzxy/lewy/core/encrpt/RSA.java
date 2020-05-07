package cn.xzxy.lewy.core.encrpt;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class RSA {

    public static final String PRIVATE_KEY = "privateKey";
    public static final String PUBLIC_KEY = "publicKey";
    // key大小
    private static final int KEY_SIZE = 1024;

    public static RSA getInstance() {
        return new RSA();
    }

    public RSA() {}

    /**
     * 获取RSA秘钥键值对
     */
    public Map<String, Key> initRSAKeyPair() throws Exception {
        log.info("initRSAKeyPair ...");
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance(CipherConstPool.CIPHER_RSA);
        keyGen.initialize(KEY_SIZE);
        KeyPair keyPair = keyGen.generateKeyPair();

        PrivateKey sk = keyPair.getPrivate();
        PublicKey pk = keyPair.getPublic();

        Map<String, Key> keyMaps = new HashMap<String, Key>();
        keyMaps.put(PUBLIC_KEY, pk);
        keyMaps.put(PRIVATE_KEY, sk);
        return keyMaps;
    }

    /**
     * 获取RSA秘钥键值对
     */
    public Map<String, String> initStringRSAKeyPair() throws Exception {
        log.info("initStringRSAKeyPair ... ");
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance(CipherConstPool.CIPHER_RSA);
        keyGen.initialize(KEY_SIZE);
        KeyPair keyPair = keyGen.generateKeyPair();

        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();

        Map<String, String> keyMaps = new HashMap<String, String>();
        keyMaps.put(PUBLIC_KEY, Base64Utils.encodeToString(publicKey.getEncoded()));
        keyMaps.put(PRIVATE_KEY, Base64Utils.encodeToString(privateKey.getEncoded()));
        return keyMaps;
    }

    /**
     * byte code转RSA秘钥键值对
     */
    public Map<String, Key> initRSAKeyPair(byte[] pk, byte[] sk) throws Exception {
        log.info("initRSAKeyPair ... ");
        PublicKey publicKey = this.getPublic(pk);
        PrivateKey privateKey = this.getPrivate(sk);

        Map<String, Key> keyMaps = new HashMap<String, Key>();
        keyMaps.put(PUBLIC_KEY, publicKey);
        keyMaps.put(PRIVATE_KEY, privateKey);
        return keyMaps;

    }

    /**
     * byte code转私钥
     */
    public PrivateKey getPrivate(byte[] sk) throws Exception {
        log.info("getPrivateKey ... ");
        KeyFactory kf = KeyFactory.getInstance(CipherConstPool.CIPHER_RSA);
        PKCS8EncodedKeySpec skSpec = new PKCS8EncodedKeySpec(sk);
        PrivateKey privateKey = kf.generatePrivate(skSpec);
        return privateKey;
    }

    /**
     * byte code转公钥
     */
    public PublicKey getPublic(byte[] pk) throws Exception {
        log.info("getPublicKey ... ");
        KeyFactory kf = KeyFactory.getInstance(CipherConstPool.CIPHER_RSA);
        X509EncodedKeySpec pkSec = new X509EncodedKeySpec(pk);
        PublicKey publicKey = kf.generatePublic(pkSec);
        return publicKey;
    }

    /**
     * 字符串转对象 PublicKey
     */
    public PublicKey getPublicKeyFromString(String publicKeyStr) throws Exception {
        byte[] pk = Base64Utils.decode(publicKeyStr);
        return this.getPublic(pk);
    }

    /**
     * 字符串转对象 PublicKey
     */
    public PrivateKey getPrivateKeyFromString(String privateKeyStr) throws Exception {
        byte[] sk = Base64Utils.decode(privateKeyStr);
        return this.getPrivate(sk);
    }


    /**
     * 获取私钥code
     */
    public byte[] getPrivate(Key privateKey) {
        return privateKey.getEncoded();
    }

    /**
     * 获取公钥code
     */
    public byte[] getPublic(Key publicKey) {
        return publicKey.getEncoded();
    }

    /**
     * 公钥加密
     */
    protected byte[] encrypt(byte[] message, PublicKey key) throws Exception {
        Cipher cipher = Cipher.getInstance(CipherConstPool.CIPHER_RSA);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(message);
    }

    /**
     * 公钥加密
     */
    public String encryptOnPublicKey(String metaText, String publicKeyStr) throws Exception {
        log.info("encryptOnPublicKey ... ");
        PublicKey publicKey = getPublicKeyFromString(publicKeyStr);
        byte[] message = metaText.getBytes(CharsetConstPool.CHAR_SET_UTF_8);
        byte[] secretText = this.encrypt(message, publicKey);
        return Base64Utils.encodeToString(secretText);
    }

    /**
     * 私钥加密
     */
    protected byte[] encrypt(byte[] message, PrivateKey key) throws Exception {
        Cipher cipher = Cipher.getInstance(CipherConstPool.CIPHER_RSA);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(message);
    }

    /**
     * 私钥加密
     */
    public String encryptOnPrivateKey(String metaText, String privateKeyStr) throws Exception {
        log.info("encryptOnPrivateKey ... ");
        PrivateKey privateKey = getPrivateKeyFromString(privateKeyStr);
        byte[] message = metaText.getBytes(CharsetConstPool.CHAR_SET_UTF_8);
        byte[] secretText = this.encrypt(message, privateKey);
        return Base64Utils.encodeToString(secretText);
    }


    /**
     * 公钥解密
     */
    protected byte[] decrypt(byte[] message, PublicKey key) throws Exception {
        Cipher cipher = Cipher.getInstance(CipherConstPool.CIPHER_RSA);
        cipher.init(Cipher.DECRYPT_MODE, key);
        return cipher.doFinal(message);
    }

    /**
     * 公钥解密
     */
    public String decryptOnPublicKey(String metaText, String publicKeyStr) throws Exception {
        log.info("decryptOnPublicKey ... ");
        PublicKey publicKey = getPublicKeyFromString(publicKeyStr);
        byte[] message = Base64Utils.decode(metaText);
        byte[] secretText = this.decrypt(message, publicKey);
        return new String(secretText, CharsetConstPool.CHAR_SET_UTF_8);
    }

    /**
     * 私钥解密
     */
    protected byte[] decrypt(byte[] input, PrivateKey key) throws Exception {
        Cipher cipher = Cipher.getInstance(CipherConstPool.CIPHER_RSA);
        cipher.init(Cipher.DECRYPT_MODE, key);
        return cipher.doFinal(input);
    }

    /**
     * 私钥解密
     */
    public String decryptOnPrivateKey(String metaText, String privateKeyStr) throws Exception {
        log.info("decryptOnPrivateKey ... ");
        PrivateKey privateKey = getPrivateKeyFromString(privateKeyStr);
        byte[] message = Base64Utils.decode(metaText);
        byte[] secretText = this.decrypt(message, privateKey);
        return new String(secretText, CharsetConstPool.CHAR_SET_UTF_8);
    }
}
