package com.mz.device;

/**
 * 害虫列表
 */
public enum PestEnum {
    YEE("YEE", "夜蛾"),
    SHIDAOMING("SHIDAOMING", "水稻螟"),
    YUMIMING("YUMIMING", "玉米螟"),
    DOUJIAYEMING("DOUJIAYEMING", "豆荚野螟"),
    YEMING("YEMING", "野螟"),
    TIANE("TIANE", "天蛾"),
    LOUGU("LOUGU", "蝼蛄"),
    CIE("CIE", "刺蛾"),
    HUACHUN("HUACHUN", "花蝽"),
    GANSHUTIANE("GANSHUTIANE", "甘薯天蛾"),
    DENGE("DENGE", "灯蛾"),
    XIECHUN("XIECHUN", "蝎蝽"),
    HUANGCHONG("HUANGCHONG", "蝗虫"),
    JIAODUNCHUN("JIAODUNCHUN", "角盾蝽"),
    TIANNIU("TIANNIU", "天牛"),
    DAOFEISHI("DAOFEISHI", "稻飞虱"),
    ZHANGLANG("ZHANGLANG", "蟑螂"),
    PIAOCHONG("TANGLANG", "瓢虫"),
    LONGSHI("LONGSHI", "龙虱"),
    BANYILACHAN("BANYILACHAN", "斑衣蜡蝉"),
    YILING("YILING", "蚁蛉"),
    YINCHICHONG("YINCHICHONG", "隐翅虫"),
    BUJIACHONG("BUJIACHONG", "步甲虫"),
    YAYING("YAYING", "蚜蝇"),
    JINGUIZI("JINGUIZI", "金龟子");
    private String code;
    private String msg;

    PestEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "PestEnum{" + "code=" + code + ", msg='" + msg + '\'' + '}';
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
