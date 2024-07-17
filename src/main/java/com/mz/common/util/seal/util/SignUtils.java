package com.mz.common.util.seal.util;

import cn.hutool.core.util.ObjectUtil;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSignatureAppearance;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.security.*;
import com.mz.common.util.seal.entity.sign.KeyWordBean;
import com.mz.common.util.seal.entity.sign.SignPDFBean;
import com.mz.common.util.seal.entity.sign.SignPDFRequestBean;
import com.mz.common.util.wxaes.HttpKit;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.*;
import java.net.URL;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
public class SignUtils {
    public static void main(String[] args) {
        FileOutputStream fileOutputStream = null;
        try {
//            String courseFile = "F:/eclipse-workspace-mangzhong/futurerural-cloud/futurerural-module-server/futurerural-server-biz/src/main/resources/";
            String courseFile = "C:/Users/Administrator/Desktop/";
//            String courseFile = "C:/Users/Administrator/Desktop/";
//            ByteArrayOutputStream result = SealUtil.genSeal("芒种数字乡村(杭州)有限公司", "33011010159361", "★", "");
//            ByteArrayOutputStream result = signSeal("talhttla", courseFile + "client1.p12", courseFile + "img/chapter.png", courseFile + "pdf/程序员小王.pdf", "电子签章原因", "电子签章位置");
//            String targetFile = "sign42.pdf";
//            targetFile = "公章1.png";

            // 2.生成私章
//            SealFont font = new SealFont();
//            font.setFontSize(120).setBold(true).setFontText("芒种数字");
//            String targetFile = "私章1.png";
//          ByteArrayOutputStream result = SealUtil.genPersonSeal(300, 16, font, "印");

            // 3.手写签名
            String targetFile = "signOut111.pdf";
//            ByteArrayOutputStream result = SignUtils.signHandImg("talhttla", courseFile + "client.p12", courseFile + "img/sign.png", courseFile + "pdf/out.pdf", "批准人", "电子签章原因", "电子签章位置");
//            ByteArrayOutputStream result = SignUtils.signSeal("talhttla", courseFile + "client.p12", courseFile + "pdf/seal.png", courseFile + "pdf/程序员小王.pdf", "（检定专用章）", "检定专用章", "检定专用章");
            ByteArrayOutputStream result = SignUtils.signSeal("talhttla", courseFile + "client.p12", courseFile + "seal.png", courseFile + "家庭医生签约协议.pdf", "乙方（盖章）：", "沙建镇中心卫生院", "福建省漳州市华安县沙建镇埔岭社区66号");

//            fileOutputStream = new FileOutputStream(new File(courseFile + "pdf/" + targetFile));
            fileOutputStream = new FileOutputStream(new File(courseFile + targetFile));
            fileOutputStream.write(result.toByteArray());
            fileOutputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("输出文件异常", e);
        } finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 单多次手写签名通用
     *
     * @param keyStorePass 证书密码
     * @param keyStorePath 证书路径
     * @param signPath     签名路径
     * @param srcFile      源文件
     * @param keywords     关键字
     * @param signReason   签章原因
     * @param signLocation 签章位置
     * @throws IOException
     */
    public static ByteArrayOutputStream signHandImg(String keyStorePass, String keyStorePath, String signPath, String srcFile, String keywords, String signReason, String signLocation) throws IOException {
        InputStream inputStream = null;
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        try {
            List<SignPDFBean> SignPDFBeans = new ArrayList();
            SignPDFBean signPDFBean = new SignPDFBean();
            signPDFBean.setKeyStorePass(keyStorePass);
            signPDFBean.setKeyStorePath(keyStorePath);
            signPDFBean.setKeyWord(keywords);
            signPDFBean.setSealPath(signPath);
            signPDFBean.setSignReason(signReason);
            signPDFBean.setSignLocation(signLocation);
            SignPDFBeans.add(signPDFBean);

            SignPDFRequestBean requestBean = new SignPDFRequestBean();
            requestBean.setSrcPDFPath(srcFile);
            requestBean.setSignPDFBeans(SignPDFBeans);

            long startTime = System.currentTimeMillis();
            // 1.解析pdf文件
            Map<Integer, List<KeyWordBean>> map = KeywordPDFUtils.getPDFText(requestBean.getSrcPDFPath());
            // 2.获取关键字坐标
            List<SignPDFBean> signPDFBeanList = requestBean.getSignPDFBeans();
            for (int i = 0; i < signPDFBeanList.size(); i++) {
                SignPDFBean pdfBean = signPDFBeanList.get(i);
                KeyWordBean bean = KeywordPDFUtils.getKeyWordXY1(map, pdfBean.getKeyWord());
                if (ObjectUtil.isEmpty(bean)) {
                    log.info("未查询到关键字。。。");
                }
                if (i == 0) {
                    inputStream = new FileInputStream(requestBean.getSrcPDFPath());
                } else {
                    inputStream = new ByteArrayInputStream(result.toByteArray());
                }
                // 3.进行盖章
                result = sign(pdfBean.getKeyStorePass(), pdfBean.getKeyStorePath(), inputStream, pdfBean.getSealPath(), bean.getX(), bean.getY(), bean.getPage(), pdfBean.getSignReason(), pdfBean.getSignLocation());
            }
            long endTime = System.currentTimeMillis();
            log.info("总时间：" + (endTime - startTime));
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("异常，原因：", e);
        } finally {
            try {
                if (null != inputStream) {
                    inputStream.close();
                }
                if (null != result) {
                    result.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 单多次电子签章通用
     *
     * @param keyStorePass 证书密码
     * @param keyStorePath 证书路径
     * @param sealPath     图章路径
     * @param srcFile      源文件
     * @param signReason   签章原因
     * @param signLocation 签章位置
     * @return
     */
    public static ByteArrayOutputStream signSeal(String keyStorePass, String keyStorePath, String sealPath, String srcFile, String sealKeyWord, String signReason, String signLocation) {
        InputStream inputStream = null;
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        try {
            int pageNum = 0, x = 0, y = 0;
            File pdfFile = new File(srcFile);
            byte[] pdfData = new byte[(int) pdfFile.length()];
            FileInputStream fileInputStream = null;
            try {
                fileInputStream = new FileInputStream(pdfFile);
                fileInputStream.read(pdfData);
            } catch (IOException e) {
                throw e;
            } finally {
                if (fileInputStream != null) {
                    try {
                        fileInputStream.close();
                    } catch (IOException e) {
                    }
                }
            }

            List<float[]> positions = PdfKeywordFinder.findKeywordPostions(pdfData, sealKeyWord);
            log.info("total:" + positions.size());
            if (positions != null && positions.size() > 0) {
                for (float[] position : positions) {
                    pageNum = (int) position[0];
                    x = (int) position[1];
                    y = (int) position[2];
                    log.info("pageNum: " + pageNum);
                    log.info("x: " + position[1] + "，y: " + position[2]);
                }
            }

            //将证书文件放入指定路径，并读取keystore ，获得私钥和证书链
            BouncyCastleProvider provider = new BouncyCastleProvider();
            Security.addProvider(provider);
            KeyStore ks = KeyStore.getInstance("PKCS12", new BouncyCastleProvider());
            char[] password = keyStorePass.toCharArray();
            ks.load(byteByUrl(keyStorePath), password);
            String alias = ks.aliases().nextElement();
            PrivateKey pk = (PrivateKey) ks.getKey(alias, password);
            // 得到证书链
            Certificate[] chain = ks.getCertificateChain(alias);

            inputStream = new FileInputStream(srcFile);
            PdfReader reader = new PdfReader(inputStream);
            //创建签章工具PdfStamper ，最后一个boolean参数是否允许被追加签名
            PdfStamper stamper = PdfStamper.createSignature(reader, result, '\0', null, true);
            // 获取数字签章属性对象
            PdfSignatureAppearance appearance = stamper.getSignatureAppearance();
            appearance.setReason(signReason);
            appearance.setLocation(signLocation);
            //设置签名的签名域名称，多次追加签名的时候，签名预名称不能一样，图片大小受表单域大小影响（过小导致压缩）
//            appearance.setVisibleSignature(new Rectangle(500, 10, 550, 50), 1, "");
            appearance.setVisibleSignature(new Rectangle(x + 150, y / 2, x - 10, y + 180), pageNum, "sig1");
            //读取图章图片
            Image image = Image.getInstance(sealPath);
            appearance.setSignatureGraphic(image);
            appearance.setCertificationLevel(PdfSignatureAppearance.CERTIFIED_NO_CHANGES_ALLOWED);
            //设置图章的显示方式，如下选择的是只显示图章（还有其他的模式，可以图章和签名描述一同显示）
            appearance.setRenderingMode(PdfSignatureAppearance.RenderingMode.GRAPHIC);
            // 摘要算法
            ExternalDigest digest = new BouncyCastleDigest();
            // 签名算法
            ExternalSignature signature = new PrivateKeySignature(pk, DigestAlgorithms.SHA512, null);
            // 调用itext签名方法完成pdf签章
            //  MakeSignature.signDetached(appearance, digest, signature, chain, null, null, null, 0, MakeSignature.CryptoStandard.CADES);
            MakeSignature.signDetached(appearance, digest, signature, chain, null, null, null, 0, null);
            //定义输入流为生成的输出流内容，以完成多次签章的过程
            inputStream = new ByteArrayInputStream(result.toByteArray());
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("异常，原因：", e);
        } finally {
            try {
                if (null != inputStream) {
                    inputStream.close();
                }
                if (null != result) {
                    result.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * @param keyStorePass 秘钥密码
     * @param keyStorePath 秘钥文件路径
     * @param signImage    *            签名图片文件
     *                     * @param x
     *                     *            x坐标
     *                     * @param y
     *                     *            y坐标
     *                     * @return
     */
    public static ByteArrayOutputStream sign(String keyStorePass, String keyStorePath, InputStream inputStream, String signImage, float x, float y, int page, String reason, String location) {
        PdfReader reader = null;
        ByteArrayOutputStream signPDFData = null;
        PdfStamper stp = null;
        try {
            BouncyCastleProvider provider = new BouncyCastleProvider();
            Security.addProvider(provider);
            KeyStore ks = KeyStore.getInstance("PKCS12", new BouncyCastleProvider());
            char[] password = keyStorePass.toCharArray();
            ks.load(byteByUrl(keyStorePath), password);
            String alias = ks.aliases().nextElement();
            PrivateKey privateKey = (PrivateKey) ks.getKey(alias, password);
            Certificate[] chain = ks.getCertificateChain(alias);
            reader = new PdfReader(inputStream);
            signPDFData = new ByteArrayOutputStream();
            stp = PdfStamper.createSignature(reader, signPDFData, '\0', null, true);
            stp.setFullCompression();
            PdfSignatureAppearance sap = stp.getSignatureAppearance();
            sap.setReason(reason);
            sap.setLocation(location);
            Image image = Image.getInstance(signImage);
            sap.setImageScale(0);
            sap.setSignatureGraphic(image);
            sap.setRenderingMode(PdfSignatureAppearance.RenderingMode.GRAPHIC);
            sap.setVisibleSignature(new Rectangle(x - 30, y - 15, x + 185, y + 68), page, UUID.randomUUID().toString().replaceAll("-", ""));
            stp.getWriter().setCompressionLevel(5);
            ExternalDigest digest = new BouncyCastleDigest();
            ExternalSignature signature = new PrivateKeySignature(privateKey, DigestAlgorithms.SHA512, provider.getName());
//            MakeSignature.signDetached(sap, digest, signature, chain, null, null, null, 0, MakeSignature.CryptoStandard.CADES);
            MakeSignature.signDetached(sap, digest, signature, chain, null, null, null, 0, null);
            reader.close();
            return signPDFData;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (signPDFData != null) {
                try {
                    signPDFData.close();
                } catch (IOException e) {

                }
            }
        }
        return null;
    }

    public static InputStream byteByUrl(String urlOrPath) throws Exception {
        InputStream in = null;
        byte[] bytes;
        if (urlOrPath.toLowerCase().startsWith("https")) {
            bytes = HttpKit.get(urlOrPath).getBytes();
        } else if (urlOrPath.toLowerCase().startsWith("http")) {
            URL url = new URL(urlOrPath);
            return url.openStream();
        } else {
            File file = new File(urlOrPath);
            if (!file.isFile() || !file.exists() || !file.canRead()) {
                return null;
            }
            return new FileInputStream(file);
        }
        return new ByteArrayInputStream(bytes);
    }

}
