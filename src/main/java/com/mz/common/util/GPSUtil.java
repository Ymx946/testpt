package com.mz.common.util;

/**
 * 参考: https://blog.csdn.net/qq_36377037/article/details/86479796 <br />
 * 不同地图坐标系(天地图、百度地图、高德地图)的经纬度转换 <br />
 * 天地图：CGCS2000，2000国家大地坐标系；我们其实很多时候直接用WGS84的坐标来代替CGCS2000坐标。因为CGCS2000的定义与WGS84实质一样。采用的参考椭球非常接近。扁率差异引起椭球面上的纬度和高度变化最大达0.1mm。当前测量精度范围内，可以忽略这点差异。可以说两者相容至cm级水平，但若一点的坐标精度达不到cm水平，则不认为CGCS2000和WGS84的坐标是相容的。<br
 * />
 * 百度地图：bd09II坐标。首先了解一下火星坐标，它是在国际标准坐标WGS-84上进行的一次加密，由于国内的电子地图都要至少使用火星坐标进行一次加密，百度直接就任性一些，直接自己又研究了一套加密算法，来了个二次加密，这就是我们所熟知的百度坐标(BD-09)。<br
 * />
 * 高德地图：高德地图：gcj02坐标，也称为火星坐标。火星坐标是国家测绘局为了国家安全在原始坐标的基础上进行偏移得到的坐标，基本国内的电子地图、导航设备都是采用的这一坐标系或在这一坐标的基础上进行二次加密得到的。<br
 * />
 *
 * @Description:
 * @author: yangh
 * @date: 2020年9月21日 下午3:25:20
 */
public class GPSUtil {
    public static double pi = 3.1415926535897932384626;
    public static double x_pi = 3.14159265358979324 * 3000.0 / 180.0;
    public static double a = 6378245.0;
    public static double ee = 0.00669342162296594323;

    public static double transformLat(double x, double y) {
        double ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y + 0.2 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(y * pi) + 40.0 * Math.sin(y / 3.0 * pi)) * 2.0 / 3.0;
        ret += (160.0 * Math.sin(y / 12.0 * pi) + 320 * Math.sin(y * pi / 30.0)) * 2.0 / 3.0;
        return ret;
    }

    public static double transformLon(double x, double y) {
        double ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(x * pi) + 40.0 * Math.sin(x / 3.0 * pi)) * 2.0 / 3.0;
        ret += (150.0 * Math.sin(x / 12.0 * pi) + 300.0 * Math.sin(x / 30.0 * pi)) * 2.0 / 3.0;
        return ret;
    }

    public static double[] transform(double lat, double lon) {
        if (outOfChina(lat, lon)) {
            return new double[]{lat, lon};
        }
        double dLat = transformLat(lon - 105.0, lat - 35.0);
        double dLon = transformLon(lon - 105.0, lat - 35.0);
        double radLat = lat / 180.0 * pi;
        double magic = Math.sin(radLat);
        magic = 1 - ee * magic * magic;
        double sqrtMagic = Math.sqrt(magic);
        dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * pi);
        dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * pi);
        double mgLat = lat + dLat;
        double mgLon = lon + dLon;
        return new double[]{mgLat, mgLon};
    }

    /**
     * 判断是否在中国
     *
     * @param lat
     * @param lon
     * @return
     */
    public static boolean outOfChina(double lat, double lon) {
        if (lon < 72.004 || lon > 137.8347) {
            return true;
        }
        return lat < 0.8293 || lat > 55.8271;
    }

    /**
     * 84 ==》 高德 <br />
     * 84 to 火星坐标系 (GCJ-02) World Geodetic System ==> Mars Geodetic System <br />
     *
     * @param lat
     * @param lon
     * @return
     */
    public static double[] gps84_To_Gcj02(double lat, double lon) {
        if (outOfChina(lat, lon)) {
            return new double[]{lat, lon};
        }
        double dLat = transformLat(lon - 105.0, lat - 35.0);
        double dLon = transformLon(lon - 105.0, lat - 35.0);
        double radLat = lat / 180.0 * pi;
        double magic = Math.sin(radLat);
        magic = 1 - ee * magic * magic;
        double sqrtMagic = Math.sqrt(magic);
        dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * pi);
        dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * pi);
        double mgLat = lat + dLat;
        double mgLon = lon + dLon;
        return new double[]{mgLat, mgLon};
    }

    /**
     * 高德 ==》 84 <br />
     * 火星坐标系 (GCJ-02) to 84 <br />
     *
     * @param lon
     * @param lat
     * @return
     */
    public static double[] gcj02_To_Gps84(double lat, double lon) {
        double[] gps = transform(lat, lon);
        double lontitude = lon * 2 - gps[1];
        double latitude = lat * 2 - gps[0];
        return new double[]{latitude, lontitude};
    }

    /**
     * 高德 ==》 84
     * 字符串 xxxx,xxxx
     *
     * @return
     */
    public static String changeCoordinate(String coordinate) {
        String coordinate84 = "";
        String[] coordinateArr = coordinate.split(",");
        if (coordinateArr != null && coordinateArr.length == 2) {
            double lon = Double.valueOf(coordinateArr[0].trim()).doubleValue();
            double lat = Double.valueOf(coordinateArr[1].trim()).doubleValue();
            double[] newCoordinateArr = GPSUtil.gcj02_To_Gps84(lat, lon);
            coordinate84 = newCoordinateArr[1] + "," + newCoordinateArr[0];
        }
        return coordinate84;
    }

    /**
     * 高德 == 》 百度 <br />
     * 火星坐标系 (GCJ-02) 与百度坐标系 (BD-09) 的转换算法 <br />
     * 将 GCJ-02 坐标转换成 BD-09 坐标 <br />
     *
     * @param lat
     * @param lon
     */
    public static double[] gcj02_To_Bd09(double lat, double lon) {
        double x = lon, y = lat;
        double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * x_pi);
        double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * x_pi);
        double tempLon = z * Math.cos(theta) + 0.0065;
        double tempLat = z * Math.sin(theta) + 0.006;
        double[] gps = {tempLat, tempLon};
        return gps;
    }

    /**
     * 百度 == 》 高德 <br />
     * 火星坐标系 (GCJ-02) 与百度坐标系 (BD-09) 的转换算法 <br />
     * 将 BD-09 坐标转换成GCJ-02 坐标 <br />
     *
     * @param lat
     * @param lon
     */
    public static double[] bd09_To_Gcj02(double lat, double lon) {
        double x = lon - 0.0065, y = lat - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi);
        double tempLon = z * Math.cos(theta);
        double tempLat = z * Math.sin(theta);
        double[] gps = {tempLat, tempLon};
        return gps;
    }

    /**
     * 84 == 》 百度 <br />
     * 将gps84转为bd09 <br />
     *
     * @param lat
     * @param lon
     * @return
     */
    public static double[] gps84_To_bd09(double lat, double lon) {
        double[] gcj02 = gps84_To_Gcj02(lat, lon);
        double[] bd09 = gcj02_To_Bd09(gcj02[0], gcj02[1]);
        return bd09;
    }

    /**
     * 百度 == 》 84 <br />
     * 将bd09转为gps84 <br />
     *
     * @param lat
     * @param lon
     * @return
     */
    public static double[] bd09_To_gps84(double lat, double lon) {
        double[] gcj02 = bd09_To_Gcj02(lat, lon);
        double[] gps84 = gcj02_To_Gps84(gcj02[0], gcj02[1]);
        // 保留小数点后六位
        gps84[0] = retain6(gps84[0]);
        gps84[1] = retain6(gps84[1]);
        return gps84;
    }

    /*
     * 保留小数点后六位
     *
     * @param num
     *
     * @return
     */
    private static double retain6(double num) {
        String result = String.format("%.6f", num);
        return Double.valueOf(result);
    }

    public static void main(String[] args) {
        double[] gcj02_To_Gps84 = gcj02_To_Gps84(30.401946, 120.183818);
        System.out.println(gcj02_To_Gps84.toString());
    }
}