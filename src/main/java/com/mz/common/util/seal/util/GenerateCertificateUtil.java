package com.mz.common.util.seal.util;

import com.github.pagehelper.util.StringUtil;
import com.mz.common.ConstantsUtil;
import com.mz.common.util.seal.entity.cert.Extension;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.*;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 证书生成工具类
 *
 * @author 一朝风月
 * @date 2021/8/6 14:20
 */
public class GenerateCertificateUtil {

    public static Map<String, byte[]> generateCert(String keyStorePass, String companyName, String ucsCode, String province, String city, String certificateCrl, List<Extension> extensions) {
        String issuerStr = "CN=芒种数字乡村(杭州)有限公司,OU=91330110MA2KD71N36,O=芒种数字,C=CN,L=杭州,ST=浙江";
        String subjectStr = "CN=" + companyName + ",OU=" + ucsCode + ",C=CN,L=" + city + ",ST=" + province;
        keyStorePass = StringUtil.isEmpty(keyStorePass) ? ConstantsUtil.PASSWORD_DEFAULT : keyStorePass;
        if (StringUtil.isEmpty(subjectStr)) {
            subjectStr = issuerStr;
        } else {
            issuerStr = subjectStr;
        }
        certificateCrl = StringUtil.isEmpty(certificateCrl) ? "https://www.mzszxc.com/" : certificateCrl;
        return createCert(keyStorePass, issuerStr, subjectStr, certificateCrl, extensions);
    }

    private static KeyPair getKey() throws NoSuchAlgorithmException {
        // 密钥对 生成器，RSA算法 生成的  提供者是 BouncyCastle
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA", new BouncyCastleProvider());
        // 密钥长度 1024
        generator.initialize(1024);
        // 证书中的密钥 公钥和私钥
        return generator.generateKeyPair();
    }

    /**
     * 创建证书
     *
     * @param keyStorePass   证书密码
     * @param issuerStr      颁发机构信息
     * @param subjectStr     使用者信息
     * @param certificateCrl 颁发地址
     * @return 证书
     */
    public static Map<String, byte[]> createCert(String keyStorePass, String issuerStr, String subjectStr,
                                                 String certificateCrl, List<Extension> extensions) {

        Map<String, byte[]> result = new HashMap<>();
        ByteArrayOutputStream out = null;
        try {
            // 生成证书
            // KeyStore keyStore = KeyStore.getInstance("JKS");
            KeyStore keyStore = KeyStore.getInstance("PKCS12", new BouncyCastleProvider());
            keyStore.load(null, null);
            KeyPair keyPair = getKey();
            //  issuer与 subject相同的证书就是CA证书
            Certificate cert = generateCertificate(issuerStr, subjectStr, keyPair, result, certificateCrl, extensions);
            // cretkey随便写，标识别名
            keyStore.setKeyEntry("cretkey", keyPair.getPrivate(), keyStorePass.toCharArray(), new Certificate[]{cert});
            out = new ByteArrayOutputStream();
            cert.verify(keyPair.getPublic());
            keyStore.store(out, keyStorePass.toCharArray());
            byte[] keyStoreData = out.toByteArray();
            result.put("keyStoreData", keyStoreData);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 生成证书
     *
     * @param issuerStr      事项字符串
     * @param subjectStr     主题字符串
     * @param keyPair        密钥对（公钥，私钥）
     * @param result         结果
     * @param certificateCrl CRL分发点
     * @param extensions     扩展字段
     * @return 生成证书
     */
    private static Certificate generateCertificate(String issuerStr, String subjectStr, KeyPair keyPair,
                                                   Map<String, byte[]> result,
                                                   String certificateCrl, List<Extension> extensions) {
        ByteArrayInputStream bout = null;
        X509Certificate cert = null;
        try {
            PublicKey publicKey = keyPair.getPublic();
            PrivateKey privateKey = keyPair.getPrivate();
            Date notBefore = new Date();
            Calendar rightNow = Calendar.getInstance();
            rightNow.setTime(notBefore);
            // 日期加1年
            rightNow.add(Calendar.YEAR, 1);
            Date notAfter = rightNow.getTime();
            // 证书序列号
            BigInteger serial = BigInteger.probablePrime(256, new Random());
            X509v3CertificateBuilder builder = new JcaX509v3CertificateBuilder(
                    new X500Name(issuerStr), serial, notBefore, notAfter, new X500Name(subjectStr), publicKey);
//            JcaContentSignerBuilder jBuilder = new JcaContentSignerBuilder("SHA1withRSA");
            JcaContentSignerBuilder jBuilder = new JcaContentSignerBuilder("SHA256WithRSA");
            SecureRandom secureRandom = new SecureRandom();
            jBuilder.setSecureRandom(secureRandom);
            ContentSigner singer = jBuilder.setProvider(new BouncyCastleProvider()).build(privateKey);
            // 分发点
            ASN1ObjectIdentifier identifier = new ASN1ObjectIdentifier("2.5.29.31");
            GeneralName generalName = new GeneralName(GeneralName.uniformResourceIdentifier, certificateCrl);
            GeneralNames seneralNames = new GeneralNames(generalName);
            DistributionPointName distributionPoint = new DistributionPointName(seneralNames);
            DistributionPoint[] points = new DistributionPoint[1];
            points[0] = new DistributionPoint(distributionPoint, null, null);
            CRLDistPoint crlDistPoint = new CRLDistPoint(points);
            builder.addExtension(identifier, true, crlDistPoint);
            // 用途
            ASN1ObjectIdentifier keyUsage = new ASN1ObjectIdentifier("2.5.29.15");
            // | KeyUsage.nonRepudiation | KeyUsage.keyCertSign
            builder.addExtension(keyUsage, true, new KeyUsage(KeyUsage.digitalSignature | KeyUsage.keyEncipherment));
            // 基本限制 X509Extension.java
            ASN1ObjectIdentifier basicConstraints = new ASN1ObjectIdentifier("2.5.29.19");
            builder.addExtension(basicConstraints, true, new BasicConstraints(true));
            // privateKey:使用自己的私钥进行签名，CA证书
            if (extensions != null) {
                for (Extension ext : extensions) {
                    builder.addExtension(
                            new ASN1ObjectIdentifier(ext.getOid()),
                            ext.isCritical(),
                            ASN1Primitive.fromByteArray(ext.getValue()));
                }
            }
            X509CertificateHolder holder = builder.build(singer);
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            bout = new ByteArrayInputStream(holder.toASN1Structure().getEncoded());
            cert = (X509Certificate) cf.generateCertificate(bout);
            byte[] certBuf = holder.getEncoded();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            // 证书数据
            result.put("certificateData", certBuf);
            //公钥
            result.put("publicKey", publicKey.getEncoded());
            //私钥
            result.put("privateKey", privateKey.getEncoded());
            //证书有效开始时间
            result.put("notBefore", format.format(notBefore).getBytes(StandardCharsets.UTF_8));
            //证书有效结束时间
            result.put("notAfter", format.format(notAfter).getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bout != null) {
                try {
                    bout.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return cert;
    }
}

