package com.mz.common.util.seal.entity.sign;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Setter
@Getter
@ToString
public class SignPDFRequestBean {
    /**
     * 待签章pdf文件路径
     */
    private String srcPDFPath;
    /**
     * 签章后输出的pdf文件路径
     */
    private String outPDFPath;
    private List<SignPDFBean> SignPDFBeans;

    @Override
    public String toString() {
        return "SignPDFRequestBean [srcPDFPath=" + srcPDFPath + ", outPDFPath=" + outPDFPath + ", SignPDFBeans=" + SignPDFBeans + "]";
    }
}
