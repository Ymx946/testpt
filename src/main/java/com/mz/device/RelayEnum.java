package com.mz.device;

/**
 * 继电器列表
 */
public enum RelayEnum {
    BLANK(1, 0, "-"),
    FAN(2, 1, "风机"),
    WATER_PUMP(3, 2, "水泵"),
    AERATOR(4, 3, "增氧机"),
    WET_CURTAIN(5, 4, "湿帘"),
    SHADE(6, 5, "遮阳"),
    WINDOW(7, 6, "开窗"),
    KEEP_WARM(8, 7, "保温"),
    FEEDING_MACHINE(9, 8, "投食机");

    private Integer id;
    private Integer index;
    private String name;

    RelayEnum(int id, int index, String name) {
        this.id = id;
        this.index = index;
        this.name = name;
    }

    RelayEnum(int index, String name) {
        this.index = index;
        this.name = name;
    }

    @Override
    public String toString() {
        return "RelayEnum {" + "id=" + id + ", index='" + index + ", name='" + name + '\'' + '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
