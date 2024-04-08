package com.mz.common.util.map.utils;

public class Point implements Cloneable {
    private static final Point point = new Point();
    /**
     * 水平方向值/经度
     */
    public Double X;
    /**
     * 垂直方向值/纬度
     */
    public Double Y;

    public Point(Double x, Double y) {
        this.X = x;
        this.Y = y;
    }

    public Point() {
    }

    // 模拟有参构造方法
    public static Point getClone(Double x, Double y) {
        try {
            Point tempPoint = (Point) point.clone();
            tempPoint.setX(x);
            tempPoint.setY(y);
            return tempPoint;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        Point A = new Point(1d, null);
        Point B = new Point(null, 3d);
        System.out.println(A.equals(B));
    }

    public Double getX() {
        return X;
    }

    public void setX(Double x) {
        X = x;
    }

    public Double getY() {
        return Y;
    }

    public void setY(Double y) {
        Y = y;
    }

    @Override
    public boolean equals(Object obj) {


        // 如果为同一对象的不同引用,则相同
        if (this == obj) {
            return true;
        }
        // 如果传入的对象为空,则返回false
        if (obj == null) {
            return false;
        }
        if (obj instanceof Point) {
            Point point = (Point) obj;
            return point.X.equals(this.X) && point.Y.equals(this.Y);
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "Point{" +
                "X=" + X +
                ", Y=" + Y +
                '}';
    }

}
