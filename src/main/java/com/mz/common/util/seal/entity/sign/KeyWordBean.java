package com.mz.common.util.seal.entity.sign;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class KeyWordBean implements Comparable<KeyWordBean> {
    private float x;
    private float y;
    private int page;
    private String text;

    public KeyWordBean() {
        super();
    }

    public KeyWordBean(float x, float y, int page, String text) {
        super();
        this.x = x;
        this.y = y;
        this.page = page;
        this.text = text;
    }

    @Override
    public String toString() {
        return "KeyWordBean [x=" + x + ", y=" + y + ", page=" + page + ", text=" + text + "]";
    }

    @Override
    public int compareTo(KeyWordBean o) {
        int i = (int) (o.getY() - this.getY());//先按照Y轴排序
        if (i == 0) {
            return (int) (this.x - o.getX());//如果Y轴相等了再按X轴进行排序
        }
        return i;
    }

}
