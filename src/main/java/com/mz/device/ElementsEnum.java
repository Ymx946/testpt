package com.mz.device;

/**
 * 要素列表
 */
public enum ElementsEnum {
    UNDEFINED(4, 100, "未定义", "-", 0, 10, 1D),
    ATMOSPHERIC_TEMPERATURE(5, 101, "大气温度", "℃", -50, 100, 0.1D),
    ATMOSPHERIC_HUMIDITY(6, 102, "大气湿度", "%RH", 0, 100, 0.1D),
    SIMULATED_AIR_PRESSURE(7, 103, "模拟气压", "HPA", 500, 1500, 0.1D),
    RAINFALL(8, 104, "雨量", "MM", 0, 100, 0.1D),
    SIMPLE_TOTAL_RADIATION(9, 105, "简易总辐射", "W/M2", 0, 1500, 1D),
    SOIL_TEMPERATURE(10, 106, "土壤温度", "℃", -50, 100, 0.1D),
    SOIL_MOISTURE(11, 107, "土壤湿度", "%", 0, 100, 0.1D),
    WIND_SPEED(12, 108, "风速", "M/S", 0, 70, 0.1D),
    WIND_DIRECTION(13, 109, "风向", "°", 0, 360, 1D),
    EVAPORATION(14, 110, "蒸发", "MM", 0, 1000, 0.1D),
    SNOW_AMOUNT(15, 111, "雪量", "MM", 0, 1000, 0.1D),
    ILLUMINATION(16, 112, "照度", "LUX", 0, 200000, 10D),
    SUNSHINE_HOURS(17, 113, "日照时数", "H", 0, 24, 0.1D),
    PHOTOSYNTHESIS(18, 114, "光合", "W/M2", 0, 2000, 1D),
    RAINFALL_ACCUMULATION(19, 115, "雨量累计", "MM", 0, 6000, 0.1D),
    RADIATION_ACCUMULATION(20, 116, "辐射累计", "MJ/M2", 0, 1000, 0.01D),
    WHETHER_THERE_IS_RAIN_OR_SNOW(21, 117, "有无雨雪", "", 0, 1, 1D),
    NOISE(22, 118, "噪声", "DB", 0, 100, 0.1D),
    WATER_LEVEL(23, 119, "水位", "CM", 0, 10000, 0.1D),
    CARBON_CARBON_DIOXIDEDIOXIDE(24, 120, "二氧化碳", "PPM", 0, 2000, 1D),
    EXPOSURE(25, 121, "曝辐量", "CAL/CM2", 0, 3, 1D),
    LIQUID_LEVEL(26, 122, "液位", "MM", 0, 1000, 0.1D),
    PHOTOSYNTHETICALLY_ACTIVE_RADIATION(27, 123, "光合有效辐射", "W/M2", 0, 2000, 1D),
    VOLTAGE(28, 124, "电压", "V", 0, 15, 0.001D),
    UV(29, 125, "紫外线", "MW/M2", 0, 2000, 1D),
    DUST(30, 126, "粉尘", "UG/M3", 0, 4, 0.1D),
    DIGITAL_AIR_PRESSURE(31, 127, "数字气压", "HPA", 10, 1100, 0.1D),
    CURRENT(32, 142, "电流", "MA", 0, 5000, 0.1D),
    MAXIMUM_WIND_SPEED(33, 129, "最大风速", "M/S", 0, 70, 0.1D),
    AVERAGE_WIND_SPEED(36, 130, "平均风速", "M/S", 0, 70, 0.1D),
    LONGITUDE(37, 131, "经度", "°", -180, 180, 0.1D),
    LATITUDE(38, 132, "纬度", "°", -90, 90, 0.1D),
    ALTITUDE(39, 133, "海拔高度", "M", -2000, 9000, 0.1D),
    TOTAL_RADIATION(40, 134, "TBQ 总辐射", "W/M2", 0, 2000, 1D),
    DIRECT_RADIATION(41, 135, "直接辐射", "W/M2", 0, 2000, 1D),
    SCATTERED_RADIATION(42, 136, "散射辐射", "W/M2", 0, 2000, 1D),
    UV_RADIATION(43, 138, "紫外辐射", "W/M2", 0, 2000, 1D),
    SMD_TEMPERATURE(44, 139, "贴片温度", "℃", -50, 100, 0.1D),
    DEW_POINT_TEMPERATURE(45, 140, "露点温度", "℃", -50, 40, 0.1D),
    CARBON_MONOXIDE(46, 141, "一氧化碳", "PPM", 0, 1000, 1D),
    PH(47, 128, "PH 值", "", 0, 14, 0.01D),
    ULTRASONIC_WIND_SPEED(48, 143, "超声波风速", "M/S", 0, 60, 0.01D),
    WATER_TEMPERATURE(49, 144, "水温", "℃", -50, 100, 0.1D),
    PM_UG(50, 145, "PM2.5", "UG/M3", 0, 10, 0.1D),
    PM10(51, 146, "PM10", "UG/M3", 0, 10, 0.1D),
    VISIBILITY(52, 152, "能见度", "M", 0, 50000, 0.1D),
    PM(57, 151, "PM10", "ug/m3", 0, 2000, 1d),
    Negative_oxygen_ions(58, 153, "负氧离子", "个/cm3", 0, 5000, 10d),
    salt(59, 154, "盐分", "mg/L", 0, 15000, 1d),
    Conductivity(60, 155, "电导率", "mS/cm", 0, 20, 0.01d),
    SO2(61, 156, "SO2", "ug/m3", 0, 20000, 1d),
    CO(62, 157, "CO", "mg/m3", 0, 2000, 0.01d),
    NO2(63, 158, "NO2", "ug/m3", 0, 2000, 1d),
    Pipe_flow(65, 160, "管道流量", "m3/s", 0, 1000, 0.01d),
    flow_rate(66, 161, "流速", "L/min", 0, 10000, 1d),
    pipeline_pressure(67, 162, "管道压力", "KPa", 0, 10000, 1d),
    temperature_difference(68, 163, "温差", "℃", -150, 150, 0.1d),
    dissolved_oxygen(69, 164, "溶解氧", "mg/L", 0, 20, 0.01d),
    Dissolved_oxygen_difference(70, 165, "溶解氧差", "mg/L", -20, 20, 0.01d),
    Ammonia_nitrogen(71, 166, "氨氮", "mg/L", 0, 2000, 0.1d),
    NEGATIVE_OXYGEN_IONS(72, 167, "负氧离子", "个/cm3", 0, 50000, 1d),
    TSP(73, 168, "TSP", "ug/m3", 0, 2000, 1d),
    water_level(74, 169, "水位", "m", 0, 300, 0.01d),
    TURBIDIT(75, 170, "浊度", "NTU", 0, 20, 0.1D),
    SO2_PPB(86, 181, "SO2", "PPB", 0, 20000, 1D),
    CO_PPB(87, 182, "CO", "PPB", 0, 20000, 1D),
    NO2_PPB(88, 183, "NO2", "PPB", 0, 20000, 1D),
    O3D(89, 184, "O3", "PPB", 0, 20000, 1D),
    NH3(90, 185, "NH3", "PPM", 0, 5000, 0.01D),
    H2S(91, 186, "H2S", "PPM", 0, 5000, 0.01D),
    OXYGEN(92, 187, "氧气", "%VOL", 0, 1000, 0.1D),
    SONIC_LEVEL(93, 188, "声波液位", "MM", 0, 30000, 0.4D),
    MIN_PM25(94, 189, "5MIN-PM25", "UG/M3", 0, 2000, 1D),
    WATER_LEVEL_M(95, 191, "水位", "M", 0, 10000, 0.1D),
    MIN_PM10_UG(96, 190, "5MIN-PM10", "UG/M3", 0, 2000, 1D),
    RESET_TO_ZERO(97, 192, "归零", "0", 0, 0, 0D),
    CO_MG(98, 193, "CO", "MG/M3", 0, 2000, 0.1D),
    CH4(99, 194, "CH4", "%LEL", 0, 100, 0.1D),
    CH4S(100, 195, "CH4S", "MG/M3", 0, 2000, 0.001D),
    VISIBILITY_M(101, 196, "能见度", "M", 0, 60000, 1D),
    SOIL_SALINITY(103, 198, "土壤盐分", "", 0, 10000, 1D),
    CUMULATIVE_FLOW(104, 199, "累积流量", "M3", 0, 60000, 0.01D),
    TOTAL_NUMBER_OF_COLONIES(105, 200, "菌落总数", "CFU", 0, 800, 1D),
    SOIL_HEAT_FLUX(106, 201, "土壤热通量", "W/M2", -500, 500, 0.1D),
    LIQUID_LEVEL_MM(107, 202, "液位", "MM", 0, 5000, 1D),
    LIQUID_LEVEL_M(108, 203, "液位", "MM", 0, 20000, 1D),
    AMMONIN_NITROGEN(109, 204, "氨氮", "PPM", 0, 300, 0.1D),
    MMHG(112, 207, "毫米汞柱", "mmHg", 0, 2000, 0.1d),
    ILLUMINATION_LUX(113, 208, "照度", "LUX", 0, 200000, 1D),
    VOLTAGE_V(114, 209, "电压", "V", 0, 100000, 0.1D),
    CURRENT_A(115, 210, "电流", "A", 0, 100000, 0.1D),
    ORP(116, 211, "ORP", "MV", -10000, 10000, 0.01D),
    ELECTRICITY(119, 214, "电量", "KW·H", 0, 10000, 0.01D),
    FLOW_M3(120, 215, "流量", "M3/H", 0, 10000, 0.01D),
    CUMULATIVE_FLOW_M3(121, 216, "累计流量", "M3", 0, 30000, 1D),
    CUMULATIVE_FLOW_L(122, 217, "累计流量", "L", 0, 30000, 1D),
    CODING(123, 218, "编码", "", 0, 20000, 1D),
    CONDUCTIVITY_US(125, 220, "电导率", "US/CM", 0, 30000, 1D),
    ROAD_CONDITION(126, 221, "路面状况", "", 0, 100, 1D),
    DIAMETER(127, 222, "直径", "MM", 0, 30000, 0.1D),
    FLOW(130, 225, "流量", "M3/H", 0, 30000, 0.1D),
    PPM(131, 226, "PPM", "PPM", 0, 30000, 0.001D),
    HUMIDITY(133, 228, "湿度", "%RH", 0, 100, 0.001D),
    RADIATION(134, 229, "辐射", "W/M2", 0, 30000, 1D),
    WIND__DIRECTION(141, 236, "0.1 风向", "°", 0, 360, 0.1D),
    ATMOSPHERIC__TEMPERATURE(142, 237, "0.01 大气温度", "℃", 0, 100, 0.01D),
    ATMOSPHERIC__HUMIDITY(143, 238, "0.01 大气湿度", "%RH", 0, 100, 0.01D),
    NITROGEN_ION(144, 239, "氮离子", "MG/KG", 0, 500, 1D),
    POTASSIUM_ION(145, 240, "钾离子", "MG/KG", 0, 500, 1D),
    PHOSPHORUS_ION(146, 241, "磷离子", "MG/KG", 0, 500, 1D),
    FLOW_ML(149, 244, "流量", "ML", 0, 30000, 1D),
    FLOW_RATE_ML(150, 245, "流速", "ML/S", 0, 30000, 1D),
    WEIGHT(151, 246, "重量", "G", 0, 30000, 1D),
    PERCENTAGE(152, 247, "百分比", "%", 0, 30000, 0.01D),
    DECIMETER(153, 248, "分米", "DM", 0, 30000, 0.01D),
    MODEL(154, 249, "模式", "", 0, 30000, 1D),
    MOTOR(155, 250, "电机", "", 0, 30000, 1D),
    DIRECTION(156, 251, "方向", "", 0, 30000, 1D),
    SWITCH_STATUS(157, 252, "开关状态", "", 0, 30000, 1D),
    PAR(158, 253, "PAR", "W/M2", 0, 30000, 0.01D),
    PPFD(159, 254, "PPFD", "UMOL/M2/S", 0, 30000, 0.1D),
    YPFD(160, 255, "YPFD", "UMOL/M2/S", 0, 30000, 0.1D),
    RADIATION_W(161, 256, "辐射", "W/M2", 0, 30000, 0.01D),
    COLOR_TEMPERATURE(162, 257, "色温", "K", 0, 30000, 1D),
    RADIOACTIVITY(163, 258, "辐照度", "MW/M2", 0, 30000, 0.01D);

    private Integer id;
    private Integer index;
    private String name;
    private String unit;
    private Integer min;
    private Integer max;
    private Double prec;
    ElementsEnum(Integer id, Integer index, String name, String unit, Integer min, Integer String, Double prec) {
        this.id = id;
        this.index = index;
        this.name = name;
        this.unit = unit;
        this.min = min;
        this.max = max;
        this.prec = prec;
    }

    @Override
    public String toString() {
        return "ElementsEnum {" + "id=" + id + ", index='" + index + ", name='" + name + '\'' + '}';
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

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Integer getMin() {
        return min;
    }

    public void setMin(Integer min) {
        this.min = min;
    }

    public Integer getMax() {
        return max;
    }

    public void setMax(Integer max) {
        this.max = max;
    }

    public Double getPrec() {
        return prec;
    }

    public void setPrec(Double prec) {
        this.prec = prec;
    }
}
