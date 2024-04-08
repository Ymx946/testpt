package com.mz.common.util.seal.entity.seal;

import java.awt.*;

/**
 * @Description: 印章配置类
 */
public class SealConfiguration {
    /**
     * 主文字
     */
    private SealFont mainFont;
    /**
     * 副文字
     */
    private SealFont viceFont;
    /**
     * 抬头文字
     */
    private SealFont titleFont;
    /**
     * 中心文字
     */
    private SealFont centerFont;
    /**
     * 边线圆
     */
    private SealCircle borderCircle;
    /**
     * 内边线圆
     */
    private SealCircle borderInnerCircle;
    /**
     * 内线圆
     */
    private SealCircle innerCircle;
    /**
     * 背景色，默认红色
     */
    private Color backgroudColor = Color.RED;
    /**
     * 图片输出尺寸，默认300
     */
    private Integer imageSize = 300;

    public SealFont getMainFont() {
        return mainFont;
    }

    public SealConfiguration setMainFont(SealFont mainFont) {
        this.mainFont = mainFont;
        return this;
    }

    public SealFont getViceFont() {
        return viceFont;
    }

    public SealConfiguration setViceFont(SealFont viceFont) {
        this.viceFont = viceFont;
        return this;
    }

    public SealFont getTitleFont() {
        return titleFont;
    }

    public SealConfiguration setTitleFont(SealFont titleFont) {
        this.titleFont = titleFont;
        return this;
    }

    public SealFont getCenterFont() {
        return centerFont;
    }

    public SealConfiguration setCenterFont(SealFont centerFont) {
        this.centerFont = centerFont;
        return this;
    }

    public SealCircle getBorderCircle() {
        return borderCircle;
    }

    public SealConfiguration setBorderCircle(SealCircle borderCircle) {
        this.borderCircle = borderCircle;
        return this;
    }

    public SealCircle getBorderInnerCircle() {
        return borderInnerCircle;
    }

    public SealConfiguration setBorderInnerCircle(SealCircle borderInnerCircle) {
        this.borderInnerCircle = borderInnerCircle;
        return this;
    }

    public SealCircle getInnerCircle() {
        return innerCircle;
    }

    public SealConfiguration setInnerCircle(SealCircle innerCircle) {
        this.innerCircle = innerCircle;
        return this;
    }

    public Color getBackgroudColor() {
        return backgroudColor;
    }

    public SealConfiguration setBackgroudColor(Color backgroudColor) {
        this.backgroudColor = backgroudColor;
        return this;
    }

    public Integer getImageSize() {
        return imageSize;
    }

    public SealConfiguration setImageSize(Integer imageSize) {
        this.imageSize = imageSize;
        return this;
    }
}
