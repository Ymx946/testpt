package com.mz.common.util.map;

import com.mz.common.util.map.utils.Point;

import java.math.BigDecimal;

public class MapMathUtil {

    private static final int DEF_DIV_SCALE = 10;

    public static double[] millierConvertion(double lon, double lat) {
        double L = 6381372 * Math.PI * 2;// 地球周长
        double W = L;// 平面展开后，x轴等于周长
        double H = L / 2;// y轴约等于周长一半
        double mill = 2.3;// 米勒投影中的一个常数，范围大约在正负2.3之间
        double x = lon * Math.PI / 180;// 将经度从度数转换为弧度
        double y = lat * Math.PI / 180;// 将纬度从度数转换为弧度
        y = 1.25 * Math.log(Math.tan(0.25 * Math.PI + 0.4 * y));// 米勒投影的转换
        // 弧度转为实际距离
        x = (W / 2) + (W / (2 * Math.PI)) * x;
        y = (H / 2) - (H / (2 * mill)) * y;
        double[] result = new double[2];
        result[0] = x;
        result[1] = y;
        return result;
    }

    // 面积算法
    // 点到线段的距离 ： 点（x0,y0） 到由两点组成的线段（x1,y1） ,( x2,y2 )
    public static double pointToLine(Point point0, Point point1, Point point2) {
        double x0 = point0.getX();
        double y0 = point0.getY();
        double x1 = point1.getX();
        double y1 = point1.getY();
        double x2 = point2.getX();
        double y2 = point2.getY();
        double space = 0;
        double a, b, c;
        a = lineSpace(x1, y1, x2, y2);// 线段的长度
        b = lineSpace(x1, y1, x0, y0);// (x1,y1)到点的距离
        c = lineSpace(x2, y2, x0, y0);// (x2,y2)到点的距离

        if (c <= 0.000001 || b <= 0.000001) {
            space = 0;
            return space;
        }
        if (a <= 0.000001) {
            space = b;
            return space;
        }
        if (c * c >= a * a + b * b) {
            space = b;
            return space;
        }
        if (b * b >= a * a + c * c) {
            space = c;
            return space;
        }
        double p = (a + b + c) / 2;// 半周长
        // double s = Math.sqrt(p * (p - a) * (p - b) * (p - c));
        double s = Math.sqrt(p * sub(p, a) * sub(p, b) * sub(p, c));// 海伦公式求面积
        space = 2 * s / a;// 返回点到线的距离（利用三角形面积公式求高）
        return space;
    }

    // TODO 待优化，添加double精确计算

    // 矢量算法
    public static double PointToSegDist(Point point0, Point point1, Point point2) {
        double x0 = point0.getX();
        double y0 = point0.getY();
        double x1 = point1.getX();
        double y1 = point1.getY();
        double x2 = point2.getX();
        double y2 = point2.getY();
        double cross = (x2 - x1) * (x0 - x1) + (y2 - y1) * (y0 - y1);
        if (cross <= 0)
            return Math.sqrt((x0 - x1) * (x0 - x1) + (y0 - y1) * (y0 - y1));

        double d2 = (x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1);
        // if (cross >= d2) return Math.sqrt((x0 - x2) * (x0 - x2) + (y0 - y2) * (y0 -
        // y2));
        double l = sub(cross, d2);
        if (l >= 0)
            return Math.sqrt((x0 - x2) * (x0 - x2) + (y0 - y2) * (y0 - y2));

        double r = cross / d2;
        double px = x1 + (x2 - x1) * r;
        double py = y1 + (y2 - y1) * r;
        return Math.sqrt((x0 - px) * (x0 - px) + (py - y0) * (py - y0));
    }

    /**
     * 计算两点的距离
     *
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return
     */
    public static double lineSpace(double x1, double y1, double x2, double y2) {
        // 根号a平方+b平方
        return Math.sqrt(add((sub(x1, x2)) * (sub(x1, x2)), (sub(y1, y2)) * (sub(y1, y2))));
    }

    public static double getDistance(Point start, Point end) {

        double lon1 = (Math.PI / 180) * start.getX();
        double lon2 = (Math.PI / 180) * end.getX();
        double lat1 = (Math.PI / 180) * start.getY();
        double lat2 = (Math.PI / 180) * end.getY();

        // 地球半径
        double R = 6371;

        // 两点间距离 km，如果想要米的话，结果*1000就可以了
        double d = Math.acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon2 - lon1)) * R;

        return d * 1000;
    }

    /**
     * 提供精确的加法运算。
     *
     * @param v1 被加数
     * @param v2 加数
     * @return 两个参数的和 比格戴斯某
     */
    public static double add(double v1, double v2) {
        /*
         * BigDecimal b1 = BigDecimal.valueOf(v1); BigDecimal b2 =
         * BigDecimal.valueOf(v2); return b1.add(b2).doubleValue();
         */
        return v1 + v2;
    }

    /**
     * 提供精确的减法运算。
     *
     * @param v1 被减数
     * @param v2 减数
     * @return 两个参数的差
     */
    public static double sub(double v1, double v2) {
        /*
         * BigDecimal b1 = BigDecimal.valueOf(v1); BigDecimal b2 =
         * BigDecimal.valueOf(v2); return b1.subtract(b2).doubleValue();
         */
        return v1 - v2;
    }

    /**
     * 提供精确的乘法运算。
     *
     * @param v1 被乘数
     * @param v2 乘数
     * @return 两个参数的积
     */
    public static double mul(double v1, double v2) {
        BigDecimal b1 = BigDecimal.valueOf(v1);
        BigDecimal b2 = BigDecimal.valueOf(v2);
        return b1.multiply(b2).doubleValue();
    }

    /**
     * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到 小数点以后10位，以后的数字四舍五入。
     *
     * @param v1 被除数
     * @param v2 除数
     * @return 两个参数的商
     */
    public static double div(double v1, double v2) {
        return div(v1, v2, DEF_DIV_SCALE);
    }

    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指 定精度，以后的数字四舍五入。
     *
     * @param v1    被除数
     * @param v2    除数
     * @param scale 表示表示需要精确到小数点以后几位。
     * @return 两个参数的商
     */
    public static double div(double v1, double v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b1 = BigDecimal.valueOf(v1);
        BigDecimal b2 = BigDecimal.valueOf(v2);
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

//	@SuppressWarnings("unused")
//	public static void main(String[] args) {
//		String longitude = "120.05788344501846";
//		String latitude = "28.88572781423553";
//
//		longitude = StringUtils.substring(longitude, 0, 18);
//		latitude = StringUtils.substring(latitude, 0, 18);
//
//		String data = "359972069883608,120.05774429844296,28.88572781423553";
//
//		Point start = new Point(Double.parseDouble(longitude), Double.parseDouble(latitude));
//
//		String[] split = data.split(",");
//		Point end = new Point(Double.parseDouble(split[1]), Double.parseDouble(split[2]));
//		double distance = MapMathUtil.getDistance(start, end);
//		System.out.println(distance);

//		Double distance = MapMathUtil.getDistance(new Point(120.0615685675568, 28.885362177970546), new Point(120.06321044952482, 28.885545225710796));
//		System.out.println(distance);
//	}
}
