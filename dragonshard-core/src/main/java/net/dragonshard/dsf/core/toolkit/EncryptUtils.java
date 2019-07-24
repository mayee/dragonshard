/*
 *   Copyright 1999-2018 zhangchi.dev Holding Ltd.
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package net.dragonshard.dsf.core.toolkit;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.bouncycastle.jcajce.provider.asymmetric.rsa.BCRSAPrivateCrtKey;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * 加密工具类
 *
 * @author mayee
 * @version v1.0
 * @date 2019-06-26
 **/
public class EncryptUtils {

    private static SecureRandom random = new SecureRandom();

    /**
     * MD5 Base64 加密
     *
     * @param str 待加密的字符串
     * @return 加密后的字符串
     */
    public static String md5Base64(String str) {
        //确定计算方法
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            //加密后的字符串
            byte[] src = md5.digest(str.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(src);
        } catch (Exception e) {
            throw ExceptionUtils.get(e);
        }
    }

    public static byte[] aesEncrypt(byte[] input, byte[] key) {
        return aes(input, key, 1);
    }

    public static String aesEncryptHex(String input, String keyHex) {
        try {
            return new String(Hex.encodeHex(aesEncrypt(input.getBytes(StandardCharsets.UTF_8), Hex.decodeHex(keyHex))));
        } catch (Exception e) {
            throw ExceptionUtils.get(e);
        }
    }

    public static String aesEncryptBase64(String input, String keyBase64) {
        try {
            return Base64.getEncoder().encodeToString(
                    aesEncrypt(
                            input.getBytes(StandardCharsets.UTF_8),
                            Base64.getDecoder().decode(keyBase64.getBytes(StandardCharsets.UTF_8))
                    )
            );
        } catch (Exception e) {
            throw ExceptionUtils.get(e);
        }
    }

    public static String aesDecrypt(byte[] input, byte[] key) {
        byte[] decryptResult = aes(input, key, 2);
        return new String(decryptResult);
    }

    public static String aesDecryptHex(String inputHex, String keyHex) {
        try {
            return aesDecrypt(Hex.decodeHex(inputHex.toCharArray()), Hex.decodeHex(keyHex));
        } catch (DecoderException e) {
            throw ExceptionUtils.get(e);
        }
    }

    public static String aesDecryptBase64(String inputBase64, String keyBase64) {
        return aesDecrypt(Base64.getDecoder().decode(inputBase64), Base64.getDecoder().decode(keyBase64));
    }

    public static byte[] generateAesKey() {
        return aesKey(128);
    }

    public static byte[] generateAesKey(int keySize) {
        return aesKey(keySize);
    }

    public static String generateAesKeyHex() {
        return new String(Hex.encodeHex(aesKey(128)));
    }

    public static String generateAesKeyHex(int keySize) {
        return new String(Hex.encodeHex(aesKey(keySize)));
    }

    public static String generateAesKeyBase64() {
        return new String(Base64.getEncoder().encode(aesKey(128)));
    }

    public static String generateAesKeyBase64(int keySize) {
        return new String(Base64.getEncoder().encode(aesKey(keySize)));
    }

    private static byte[] aesKey(int keySize) {
        try {
            KeyGenerator e = KeyGenerator.getInstance("AES");
            e.init(keySize);
            SecretKey secretKey = e.generateKey();
            return secretKey.getEncoded();
        } catch (GeneralSecurityException e) {
            throw ExceptionUtils.get(e);
        }
    }

    public static byte[] generateIV() {
        byte[] bytes = new byte[16];
        random.nextBytes(bytes);
        return bytes;
    }

    public static byte[] generateSalt(int numBytes) {
        if (numBytes < 1) {
            numBytes = 8;
        }

        byte[] bytes = new byte[numBytes];
        random.nextBytes(bytes);
        return bytes;
    }

    private static byte[] aes(byte[] input, byte[] key, int mode) {
        try {
            SecretKeySpec e = new SecretKeySpec(key, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(mode, e);
            return cipher.doFinal(input);
        } catch (GeneralSecurityException e) {
            throw ExceptionUtils.get(e);
        }
    }

    public static byte[] sha1(byte[] input) {
        return digest(input, "SHA-1", (byte[]) null, 1);
    }

    public static byte[] sha1(byte[] input, byte[] salt) {
        return digest(input, "SHA-1", salt, 1);
    }

    public static byte[] sha1(byte[] input, byte[] salt, int iterations) {
        return digest(input, "SHA-1", salt, iterations);
    }

    public static byte[] sha256(byte[] input) {
        return digest(input, "SHA-256", (byte[]) null, 1);
    }

    public static byte[] sha256(byte[] input, byte[] salt) {
        return digest(input, "SHA-256", salt, 1);
    }

    public static byte[] sha256(byte[] input, byte[] salt, int iterations) {
        return digest(input, "SHA-256", salt, iterations);
    }

    public static byte[] sha512(byte[] input) {
        return digest(input, "SHA-512", (byte[]) null, 1);
    }

    public static byte[] sha512(byte[] input, byte[] salt) {
        return digest(input, "SHA-512", salt, 1);
    }

    public static byte[] sha512(byte[] input, byte[] salt, int iterations) {
        return digest(input, "SHA-512", salt, iterations);
    }

    private static byte[] digest(byte[] input, String algorithm, byte[] salt, int iterations) {
        try {
            MessageDigest e = MessageDigest.getInstance(algorithm);
            if (salt != null) {
                e.update(salt);
            }

            byte[] result = e.digest(input);

            for (int i = 1; i < iterations; ++i) {
                e.reset();
                result = e.digest(result);
            }

            return result;
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 生成指定供应商的密钥对
     *
     * @param provider 供应商类
     * @return 密钥对对象
     */
    public static KeyPair generateRsaKeyPairByProvider(Provider provider) {
        try {
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA", provider);
            keyPairGen.initialize(1024, new SecureRandom());
            return keyPairGen.generateKeyPair();
        } catch (Exception e) {
            throw ExceptionUtils.get(e);
        }
    }

    /**
     * 生成指定供应商的密钥对Json字符串-十六进制格式
     *
     * @param provider 供应商类
     * @return Json字符串（密钥为十六进制格式）
     */
    public static String generateRsaKeyPairByProviderHexJson(Provider provider) {
        KeyPair keyPair = generateRsaKeyPairByProvider(provider);
        BCRSAPrivateCrtKey bcrsaPrivateCrtKey = (BCRSAPrivateCrtKey)keyPair.getPrivate();
        JSONObject object = new JSONObject();
        object.put("publicExponent", bcrsaPrivateCrtKey.getPublicExponent().toString(16));
        object.put("privateExponent", bcrsaPrivateCrtKey.getPrivateExponent().toString(16));
        object.put("modulus", bcrsaPrivateCrtKey.getModulus().toString(16));
        return object.toJSONString();
    }

    /**
     * 生成密钥对
     *
     * @return 密钥对对象
     */
    public static KeyPair generateRsaKeyPair() {
        try {
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
            keyPairGen.initialize(1024, new SecureRandom());
            return keyPairGen.generateKeyPair();
        } catch (Exception e) {
            throw ExceptionUtils.get(e);
        }
    }

    public static String generateRsaKeyPairBase64Json() throws Exception {
        KeyPair keyPair = generateRsaKeyPair();
        JSONObject object = new JSONObject();
        object.put("publicKey", Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded()));
        object.put("privateKey", Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded()));
        return object.toJSONString();
    }

    public static String generateRsaKeyPairHexJson() throws Exception {
        KeyPair keyPair = generateRsaKeyPair();
        JSONObject object = new JSONObject();
        object.put("publicKey", Hex.encodeHexString(keyPair.getPublic().getEncoded()));
        object.put("privateKey", Hex.encodeHexString(keyPair.getPrivate().getEncoded()));
        return object.toJSONString();
    }

    public static byte[] encryptRsaByPublicKey(PublicKey pk, byte[] data) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(1, pk);
        int blockSize = cipher.getBlockSize();
        int outputSize = cipher.getOutputSize(data.length);
        int leavedSize = data.length % blockSize;
        int blocksSize = leavedSize != 0 ? data.length / blockSize + 1 : data.length / blockSize;
        byte[] raw = new byte[outputSize * blocksSize];

        for (int i = 0; data.length - i * blockSize > 0; ++i) {
            if (data.length - i * blockSize > blockSize) {
                cipher.doFinal(data, i * blockSize, blockSize, raw, i * outputSize);
            } else {
                cipher.doFinal(data, i * blockSize, data.length - i * blockSize, raw, i * outputSize);
            }
        }

        return raw;
    }

    public static byte[] encryptRsaByProviderPublicKey(PublicKey pk, Provider provider, byte[] data) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA", provider);
        cipher.init(1, pk);
        int blockSize = cipher.getBlockSize();
        int outputSize = cipher.getOutputSize(data.length);
        int leavedSize = data.length % blockSize;
        int blocksSize = leavedSize != 0 ? data.length / blockSize + 1 : data.length / blockSize;
        byte[] raw = new byte[outputSize * blocksSize];

        for (int i = 0; data.length - i * blockSize > 0; ++i) {
            if (data.length - i * blockSize > blockSize) {
                cipher.doFinal(data, i * blockSize, blockSize, raw, i * outputSize);
            } else {
                cipher.doFinal(data, i * blockSize, data.length - i * blockSize, raw, i * outputSize);
            }
        }

        return raw;
    }

    /**
     * 基于Provider的私钥RSA解密
     *
     * @param input    密文字节数组
     * @param priKey   私钥对象
     * @param provider 供应商类
     * @return 明文字节数组
     */
    private static byte[] rsaDecryptByProviderPirvateKey(byte[] input, PrivateKey priKey, Provider provider) {
        try {
            Cipher cipher = Cipher.getInstance("RSA", provider);
            cipher.init(2, priKey);
            return cipher.doFinal(input);
        } catch (Exception e) {
            throw ExceptionUtils.get(e);
        }
    }

    private static RSAPrivateKey generateRsaHexPrivateKeyByProvider(String modulusHex, String privateExponentHex, Provider provider) {
        try {
            KeyFactory keyFac = KeyFactory.getInstance("RSA", provider);
            RSAPrivateKeySpec priKeySpec = new RSAPrivateKeySpec(new BigInteger(modulusHex, 16), new BigInteger(privateExponentHex, 16));
            return (RSAPrivateKey)keyFac.generatePrivate(priKeySpec);
        } catch (Exception e) {
            throw ExceptionUtils.get(e);
        }
    }

    private static RSAPrivateKey generateRsaBase64PrivateKeyByProvider(String modulusBase64, String privateExponentBase64, Provider provider) {
        try {
            KeyFactory keyFac = KeyFactory.getInstance("RSA", provider);
            RSAPrivateKeySpec priKeySpec = new RSAPrivateKeySpec(
                    new BigInteger(Base64.getDecoder().decode(modulusBase64)),
                    new BigInteger(Base64.getDecoder().decode(privateExponentBase64))
            );
            return (RSAPrivateKey)keyFac.generatePrivate(priKeySpec);
        } catch (Exception e) {
            throw ExceptionUtils.get(e);
        }
    }

    /**
     * 供应商私钥解密
     *
     * @param inputHex           十六进制密文
     * @param modulusHex         十六进制指数
     * @param privateExponentHex 十六进制私钥系数
     * @param provider           供应商类
     * @return  明文字节数组
     */
    public static byte[] rsaDecryptHexByProviderPirvateKey(String inputHex, String modulusHex, String privateExponentHex, Provider provider) {
        try {
            RSAPrivateKey pk = generateRsaHexPrivateKeyByProvider(
                    modulusHex,
                    privateExponentHex,
                    provider);
            return rsaDecryptByProviderPirvateKey(Hex.decodeHex(inputHex), pk, provider);
        } catch (Exception e) {
            throw ExceptionUtils.get(e);
        }
    }

    /**
     * 十六进制格式的公钥rsa加密
     *
     * @param input     明文字符串
     * @param priKeyHex 公钥的十六进制格式字符串
     * @return 密文的十六进制格式字符串
     */
    public static String rsaEncryptHexByPrivateKey(String input, String priKeyHex) {
        try {
            return new String(Hex.encodeHex(rsaEncryptByPrivateKey(input.getBytes(StandardCharsets.UTF_8), Hex.decodeHex(priKeyHex))));
        } catch (Exception e) {
            throw ExceptionUtils.get(e);
        }
    }

    /**
     * base64格式的私钥rsa加密
     *
     * @param input        明文字符串
     * @param priKeyBase64 base64格式的私钥
     * @return 密文的base64格式字符串
     */
    public static String rsaEncryptBase64ByPrivateKey(String input, String priKeyBase64) {
        try {
            return Base64.getEncoder().encodeToString(
                    rsaEncryptByPrivateKey(input.getBytes(StandardCharsets.UTF_8), Base64.getDecoder().decode(priKeyBase64))
            );
        } catch (Exception e) {
            throw ExceptionUtils.get(e);
        }
    }

    private static byte[] rsaEncryptByPrivateKey(byte[] input, byte[] priKey) throws Exception {
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(priKey);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(1, privateKey);
        return cipher.doFinal(input);
    }


    /**
     * base64格式的公钥rsa加密
     *
     * @param input        明文字符串
     * @param pubKeyBase64 公钥的base64格式字符串
     * @return 密文的base64格式字符串
     */
    public static String rsaEncryptBase64ByPublicKey(String input, String pubKeyBase64) {
        try {
            return Base64.getEncoder().encodeToString(
                    rsaEncryptByPublicKey(input.getBytes(StandardCharsets.UTF_8), Base64.getDecoder().decode(pubKeyBase64))
            );
        } catch (Exception e) {
            throw ExceptionUtils.get(e);
        }
    }

    /**
     * 十六进制格式的公钥rsa加密
     *
     * @param input     明文字符串
     * @param pubKeyHex 公钥的十六进制格式字符串
     * @return 密文的十六进制格式字符串
     */
    public static String rsaEncryptHexByPublicKey(String input, String pubKeyHex) {
        try {
            return new String(Hex.encodeHex(rsaEncryptByPublicKey(input.getBytes(StandardCharsets.UTF_8), Hex.decodeHex(pubKeyHex))));
        } catch (Exception e) {
            throw ExceptionUtils.get(e);
        }
    }

    private static byte[] rsaEncryptByPublicKey(byte[] input, byte[] pubKey) throws Exception {
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(pubKey);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(x509KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(1, publicKey);
        return cipher.doFinal(input);
    }


    /**
     * base64格式的私钥rsa解密
     *
     * @param inputBase64  密文的base64格式字符串
     * @param priKeyBase64 私钥的base64格式字符串
     * @return 密文的字节数组
     */
    public static byte[] rsaDecryptBase64ByPirvateKey(String inputBase64, String priKeyBase64) {
        try {
            return rsaDecryptByPirvateKey(Base64.getDecoder().decode(inputBase64), Base64.getDecoder().decode(priKeyBase64));
        } catch (Exception e) {
            throw ExceptionUtils.get(e);
        }
    }

    /**
     * 十六进制格式的私钥rsa解密
     *
     * @param inputHex  十六进制格式的密文
     * @param priKeyHex 十六进制格式的私钥
     * @return 明文字节数组
     */
    public static byte[] rsaDecryptHexByPirvateKey(String inputHex, String priKeyHex) {
        try {
            return rsaDecryptByPirvateKey(Hex.decodeHex(inputHex), Hex.decodeHex(priKeyHex));
        } catch (Exception e) {
            throw ExceptionUtils.get(e);
        }
    }

    private static byte[] rsaDecryptByPirvateKey(byte[] input, byte[] priKey) throws Exception {
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(priKey);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(2, privateKey);
        return cipher.doFinal(input);
    }


    /**
     * base64格式的公钥rsa解密
     *
     * @param inputBase64  密文的base64格式字符串
     * @param pubKeyBase64 base64格式的公钥
     * @return 明文字节数组
     */
    public static byte[] rsaDecryptBase64ByPublicKey(String inputBase64, String pubKeyBase64) {
        try {
            return rsaDecryptByPublicKey(Base64.getDecoder().decode(inputBase64), Base64.getDecoder().decode(pubKeyBase64));
        } catch (Exception e) {
            throw ExceptionUtils.get(e);
        }
    }

    /**
     * 十六进制格式的公钥rsa解密
     *
     * @param inputHex  十六进制格式的密文
     * @param pubKeyHex 十六进制格式的公钥
     * @return 明文字节数组
     */
    public static byte[] rsaDecryptHexByPublicKey(String inputHex, String pubKeyHex) {
        try {
            return rsaDecryptByPublicKey(Hex.decodeHex(inputHex), Hex.decodeHex(pubKeyHex));
        } catch (Exception e) {
            throw ExceptionUtils.get(e);
        }
    }

    private static byte[] rsaDecryptByPublicKey(byte[] input, byte[] pubKey) throws Exception {
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(pubKey);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(x509KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(2, publicKey);
        return cipher.doFinal(input);
    }


}
