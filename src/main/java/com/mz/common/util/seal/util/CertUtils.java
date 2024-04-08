package com.mz.common.util.seal.util;

import com.alibaba.fastjson.JSONObject;
import com.itextpdf.text.pdf.codec.Base64;
import com.mz.common.util.StringUtils;
import com.mz.common.util.UUIDGenerator;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x509.*;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.pkcs.PKCS10CertificationRequestBuilder;
import org.bouncycastle.pkcs.jcajce.JcaPKCS10CertificationRequestBuilder;
import org.bouncycastle.x509.X509V3CertificateGenerator;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Date;

@Slf4j
public class CertUtils {
    public static PKCS10CertificationRequest generateCSR(X500Name subject, PublicKey pubKey, PrivateKey priKey) throws OperatorCreationException {
        PKCS10CertificationRequestBuilder csrBuilder = new JcaPKCS10CertificationRequestBuilder(subject, pubKey);
        ContentSigner signerBuilder = new JcaContentSignerBuilder("SM3withSM2").setProvider(BouncyCastleProvider.PROVIDER_NAME).build(priKey);
        return csrBuilder.build(signerBuilder);
    }


    @SneakyThrows
    private void createPfxCert() throws NoSuchAlgorithmException, IOException, InvalidKeySpecException, KeyStoreException, CertificateException, OperatorCreationException {
        String keyType = "RSA"; // 密钥算法
        String certPassword = "111111";// 证书密码
        String commonName = "某某测试设备证书"; // 公司名称
        String ucsCode = "ait1234567890"; // 公司社会信用码
        String duration = "1"; // 证书有效期 单位为:月

        KeyPair keyPair = KeyUtils.generateKeyPair(keyType.toLowerCase());
        String dn = "CN=" + commonName + ",OU=" + ucsCode + ",C=CN";
        PKCS10CertificationRequest pkcs10CertificationRequest = CertUtils.generateCSR(new X500Name(dn), keyPair.getPublic(), keyPair.getPrivate());
        String p10 = Base64.encodeBytes(pkcs10CertificationRequest.getEncoded());

        JSONObject certResult = applySemiCert(p10, commonName, ucsCode, duration, keyPair);//申请证书

        log.info("certResult===" + certResult.toJSONString());
        String errorCode = certResult.getString("errorCode");
        if (StringUtils.equals("0", errorCode)) {
            JSONObject certInfo = certResult.getJSONObject("certInfo");
            String cert = certInfo.getString("cert");
            //String serialNumber = certInfo.getString("serialNumber");
            X509Certificate pubKey = new JcaX509CertificateConverter().setProvider("BC").getCertificate(new X509CertificateHolder(Base64.decode(cert)));


            String privateKeyStr = Base64.encodeBytes(keyPair.getPrivate().getEncoded());
            byte[] privateKeyBytes = Base64.decode(privateKeyStr);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            PrivateKey privateKey = kf.generatePrivate(new PKCS8EncodedKeySpec(privateKeyBytes));

            // 创建KeyStore,存储证书ca
            Security.insertProviderAt(new BouncyCastleProvider(), 1);
            //KeyStore store = KeyStore.getInstance("BKS", "BC");
            KeyStore store = KeyStore.getInstance("PKCS12");
            store.load(null, null);
            store.setKeyEntry("pc", privateKey, certPassword.toCharArray(), new X509Certificate[]{pubKey});
            String outPath = "D:\\" + commonName + ".pfx";
            OutputStream outputStream = new FileOutputStream(outPath);
            store.store(outputStream, certPassword.toCharArray());
        } else {
            throw new RuntimeException("error");
        }
    }

    private JSONObject applySemiCert(String p10, String commonName, String ucsCode, String duration, KeyPair keyPair) throws SignatureException, NoSuchProviderException, InvalidKeyException {
        JSONObject certResult = new JSONObject();
        certResult.put("errorCode", "0");

        X509V3CertificateGenerator certGen = new X509V3CertificateGenerator();
        certGen.setSerialNumber(BigInteger.valueOf(System.currentTimeMillis()));
        certGen.setNotBefore(new Date(System.currentTimeMillis() - 50000));
        certGen.setNotAfter(new Date(System.currentTimeMillis() + 50000));
        certGen.setSignatureAlgorithm("SM3withSM2");

        certGen.addExtension(X509Extensions.BasicConstraints, true, new BasicConstraints(false));
        certGen.addExtension(X509Extensions.KeyUsage, true,new KeyUsage(KeyUsage.digitalSignature | KeyUsage.keyAgreement| KeyUsage.dataEncipherment | KeyUsage.keyEncipherment));
        certGen.addExtension(X509Extensions.ExtendedKeyUsage, true,  new ExtendedKeyUsage(KeyPurposeId.id_kp_serverAuth));
        certGen.addExtension(X509Extensions.SubjectAlternativeName, false, new GeneralNames(new GeneralName(GeneralName.rfc822Name, "test@test.test")));

        X500NameBuilder issuerBuilder = new X500NameBuilder(BCStyle.INSTANCE);
        issuerBuilder.addRDN(BCStyle.C, "CN");
        issuerBuilder.addRDN(BCStyle.OU, ucsCode);
        issuerBuilder.addRDN(BCStyle.CN, commonName);
        issuerBuilder.addRDN(BCStyle.DATE_OF_BIRTH, duration);
        X500Name x500Name = issuerBuilder.build();

        X509Certificate cert = certGen.generateX509Certificate(keyPair.getPrivate(), "BC");

        JSONObject certInfo = new JSONObject();
        certInfo.put("cert", cert);
        certInfo.put("serialNumber", UUIDGenerator.generate());
        certResult.put("certInfo", certInfo);

        return certResult;
    }

}
