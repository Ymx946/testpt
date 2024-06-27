package com.mz.common.util.seal.entity.cert;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 证书参数
 */
@Setter
@Getter
@ToString
public class CertificateParams {
    /**
     * 事项字符串
     */
    private String issuerStr;
    /**
     * 主题字符串
     */
    private String subjectStr;
    /**
     * CRL分发点
     */
    private String certificateCrl;
    /**
     * 证书密码
     */
    private String password;
    /**
     * 额外的信息
     */
    private String extensionsJson;
}
