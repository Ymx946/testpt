package com.mz.common.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NumberUtil {
    private static final Random random = new Random();
    // 默认除法运算精度
    private static final int DEF_DIV_SCALE = 10;

    // 这个类不能实例化
    private NumberUtil() {
    }

    /**
     * 随意中奖数量
     *
     * @param num
     * @param rate
     * @return
     */
    public static int getRandomWinning(int num, int rate) {
        int resultcount = 0;
        for (int i = 0; i < num; i++) {
            double numRodom = Math.random() * 100;
            if (numRodom < rate) {
                resultcount++;
            }
        }
        return resultcount;
    }

    /**
     * 根据总数分割个数及限定区间进行数据随机处理 数列浮动阀值为0.95 获取红包
     *
     * @param total    - 被分割的总数
     * @param splitNum - 分割的个数
     * @param min      - 单个数字下限
     * @param max      - 单个数字上线
     * @return - 返回符合要求的数字列表
     */
    public static List<Integer> getHongbao(int total, int splitNum, int min, int max) {
        return genRandList(total, splitNum, min, max, 0.95f);
    }

    /**
     * 根据总数分割个数及限定区间进行数据随机处理
     *
     * @param total    - 被分割的总数
     * @param splitNum - 分割的个数
     * @param min      - 单个数字下限
     * @param max      - 单个数字上线
     * @param thresh   - 数列浮动阀值[0.0, 1.0]
     * @return
     */
    public static List<Integer> genRandList(int total, int splitNum, int min, int max, float thresh) {
        assert total >= splitNum * min && total <= splitNum * max;
        assert thresh >= 0.0f && thresh <= 1.0f;
        // 平均分配
        int average = total / splitNum;
        List<Integer> list = new ArrayList<>(splitNum);
        int rest = total - average * splitNum;
        for (int i = 0; i < splitNum; i++) {
            if (i < rest) {
                list.add(average + 1);
            } else {
                list.add(average);
            }
        }
        // 如果浮动阀值为0则不进行数据随机处理
        if (thresh == 0) {
            return list;
        }
        // 根据阀值进行数据随机处理
        for (int i = 0; i < splitNum - 1; i++) {
            int nextIndex = i + 1;
            int rangeThis = Math.min(list.get(i) - min, max - list.get(i));
            int rangeNext = Math.min(list.get(nextIndex) - min, max - list.get(nextIndex));
            int rangeFinal = (int) Math.ceil(thresh * (Math.min(rangeThis, rangeNext) + 1));
            int randOfRange = random.nextInt(rangeFinal);
            int randRom = list.get(i) > list.get(nextIndex) ? -1 : 1;

            list.set(i, list.get(i) + randRom * randOfRange);
            list.set(nextIndex, list.get(nextIndex) + randRom * randOfRange * -1);
            System.out.println(list.get(i));
        }
        return list;
    }

    /**
     * 红包随机
     *
     * @param total
     * @param num
     * @param min
     */
    public static void hongbao(double total, int num, double min) {
        for (int i = 1; i < num; i++) {
            double safe_total = (total - (num - i) * min) / (num - i);
            double money = Math.random() * (safe_total - min) + min;
            BigDecimal money_bd = new BigDecimal(money);
            money = money_bd.setScale(0, RoundingMode.HALF_UP).doubleValue();
            total = total - money;
            BigDecimal total_bd = new BigDecimal(total);
            total = total_bd.setScale(0, RoundingMode.HALF_UP).doubleValue();
            System.out.println("第" + i + "个红包：" + money + ",余额为:" + total + "元");
        }
        System.out.println("第" + num + "个红包：" + total + ",余额为:0元");

    }

    /**
     * 获取两个数之间的数
     *
     * @param min
     * @param max
     * @return
     */
    public static int getRandomByNums(int min, int max) {
        int result = (int) (Math.random() * (max - min) + min);
        return result;
    }

    /**
     * 提供精确的加法运算。
     *
     * @param v1 被加数
     * @param v2 加数
     * @return 两个参数的和
     */
    public static double add(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2).doubleValue();
    }

    /**
     * 提供精确的加法运算。
     *
     * @param v1 被加数
     * @param v2 加数
     * @return 两个参数的和
     */
    public static float add(float v1, float v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2).floatValue();
    }

    /**
     * 获取bigdecimal对象
     *
     * @author LOONGYA
     * @paramer String v1
     */
    public static BigDecimal getBD(String v1) {
        Assert.notNull(v1, "null无法转型");
        return new BigDecimal(v1).setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * 获取bigdecimal对象
     *
     * @author LOONGYA
     * @paramer String v1
     */
    public static String setBDScale(BigDecimal v1) {
        Assert.notNull(v1, "null无法转型");
        return v1.setScale(2, RoundingMode.HALF_UP).toString();
    }

    /**
     * 获取bigdecimal对象
     *
     * @author LOONGYA
     * @paramer Double v1
     */
    public static BigDecimal getBD(Double v1) {
        Assert.notNull(v1, "null无法转型");
        return new BigDecimal(v1).setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * 提供精确的减法运算。
     *
     * @param v1 被减数
     * @param v2 减数
     * @return 两个参数的差
     */
    public static double sub(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2).doubleValue();
    }

    /**
     * 提供精确的乘法运算。
     *
     * @param v1 被乘数
     * @param v2 乘数
     * @return 两个参数的积
     */
    public static double mul(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
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
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2, scale, RoundingMode.HALF_UP).doubleValue();
    }

    /**
     * 提供精确的小数位四舍五入处理。
     *
     * @param v     需要四舍五入的数字
     * @param scale 小数点后保留几位
     * @return 四舍五入后的结果
     */
    public static double round(double v, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b = new BigDecimal(Double.toString(v));
        BigDecimal one = new BigDecimal("1");
        return b.divide(one, scale, RoundingMode.HALF_UP).doubleValue();
    }

    public static String randomNumString(int length) {
        if (length <= 0) {
            return "";
        }
        Random random = new Random();
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < length; i++) {
            buffer = buffer.append(random.nextInt(10));
        }
        return buffer.toString();
    }

    /**
     * 取0-bound范围内的随机整数
     *
     * @param bound
     * @return
     */
    public static int randomInt(int bound) {
        Random random = new Random();
        return random.nextInt(bound);
    }

    /**
     * @param str1 str2
     * @return
     * @Description:字符串数字加法
     * @author Gjun
     * @Date 2018-07-27
     */
    public static String addStringSetScale2(String str1, String str2) {
        if (StringUtils.isBlank(str1)) {
            str1 = "0";
        }
        if (StringUtils.isBlank(str2)) {
            str2 = "0";
        }
        BigDecimal sum = new BigDecimal(str1).add(new BigDecimal(str2));
        return sum.setScale(2).toString();
    }

    // 价格转换，元转分，四舍五入取整
    public static Long priceTransformY2F(String amount) {
        BigDecimal b = new BigDecimal(new BigDecimal(amount).multiply(new BigDecimal(100)).toString());
        BigDecimal one = new BigDecimal("1");
        return b.divide(one, 0, RoundingMode.HALF_UP).longValue();
    }

    // 价格转换，元转分，四舍五入取整
    public static Long priceTransformY2F(Double amount) {
        BigDecimal b = new BigDecimal(new BigDecimal(Double.toString(amount)).multiply(new BigDecimal(100)).toString());
        BigDecimal one = new BigDecimal("1");
        return b.divide(one, 0, RoundingMode.HALF_UP).longValue();
    }

    public static String priceTransformFen2Yuan(Long amount) {
        return new BigDecimal(Long.toString(amount)).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP).toString();
    }

    public static String priceTransformhao2Fen(Long amount) {
        return new BigDecimal(Long.toString(amount)).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP).toString();
    }

    public static String priceTransformt3(Long amount) {
        return new BigDecimal(Long.toString(amount)).divide(new BigDecimal(1000), 2, RoundingMode.HALF_UP).toString();
    }

    public static String priceTransformFen2Yuan(String amount) {
        return new BigDecimal(amount).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP).toString();
    }

    public static String priceTransformFen2Yuan(Integer amount) {
        return new BigDecimal(Integer.toString(amount)).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP).toString();
    }

    public static String priceTransformHao2Yuan(Long amount) {
        return new BigDecimal(Long.toString(amount)).divide(new BigDecimal(10000), 2, RoundingMode.HALF_UP).toString();
    }

    public static String priceTransformHao2Yuan(String amount) {
        return new BigDecimal(amount).divide(new BigDecimal(10000), 2, RoundingMode.HALF_UP).toString();
    }

    public static String priceTransformHao2Yuan(Integer amount) {
        return new BigDecimal(Integer.toString(amount)).divide(new BigDecimal(10000), 2, RoundingMode.HALF_UP).toString();
    }

    public static String priceTransformHao2Fen(Long amount) {
        return new BigDecimal(Long.toString(amount)).divide(new BigDecimal(100), 0, RoundingMode.HALF_UP).toString();
    }

    public static String priceTransformHao2Fen(String amount) {
        return new BigDecimal(amount).divide(new BigDecimal(100), 0, RoundingMode.HALF_UP).toString();
    }

    public static String priceTransformHao2Fen(Integer amount) {
        return new BigDecimal(Integer.toString(amount)).divide(new BigDecimal(100), 0, RoundingMode.HALF_UP).toString();
    }

    // 计算String类型平均值保留一位小数
    public static String calAverageString(String str1, String str2) {
        if (StringUtils.isBlank(str1)) {
            str1 = "0";
        }
        if (StringUtils.isBlank(str2)) {
            str2 = "0";
        }
        BigDecimal sum = new BigDecimal(str1).add(new BigDecimal(str2));
        BigDecimal two = new BigDecimal("2");
        return sum.divide(two, 1, RoundingMode.HALF_UP).toString();
    }

    // 按照运费档次计算运费
    public static long calFreightInterval(String count, String cost, String addCount, String addCost, String amount) {
        long result = 0;
        if (StringUtils.isNoneBlank(count, cost, addCount, addCost, amount)) {
            BigDecimal countb = new BigDecimal(count);
            BigDecimal costb = new BigDecimal(cost);
            BigDecimal addCountb = new BigDecimal(addCount);
            BigDecimal addCostb = new BigDecimal(addCost);
            BigDecimal amountb = new BigDecimal(amount);
            if (amountb.compareTo(new BigDecimal("0")) == 0) {
                return result;
            }
            if (amountb.compareTo(countb) > 0 && addCountb.compareTo(new BigDecimal("0")) != 0) {
                BigDecimal[] results = (amountb.subtract(countb)).divideAndRemainder(addCountb);
                BigDecimal shang = results[0];
                BigDecimal yushu = results[1];
                if (yushu.compareTo(new BigDecimal("0")) != 0) {
                    shang = shang.add(new BigDecimal("1"));
                }
                result = shang.multiply(addCostb).add(costb).setScale(2).multiply(new BigDecimal("100")).longValue();
            } else {
                result = costb.setScale(2).multiply(new BigDecimal("100")).longValue();
            }
        }
        return result;
    }

    public static String rate(int a, int b, int c) {
        NumberFormat numberFormat = NumberFormat.getInstance();
        // 设置精确到小数点后2位
        numberFormat.setMaximumFractionDigits(c);
        String result = numberFormat.format((double) a / (double) b);
        return result;
    }

    // 价格转换，元转毫，四舍五入取整 元，角，分，厘，毫
    public static Long priceTransformY2H(String amount) {
        BigDecimal b = new BigDecimal(new BigDecimal(amount).multiply(new BigDecimal(10000)).toString());
        BigDecimal one = new BigDecimal("1");
        return b.divide(one, 0, RoundingMode.HALF_UP).longValue();
    }

    // 价格转换，元转毫，四舍五入取整，角，分，厘，毫
    public static Long priceTransformY2H(Double amount) {
        BigDecimal b = new BigDecimal(new BigDecimal(Double.toString(amount)).multiply(new BigDecimal(10000)).toString());
        BigDecimal one = new BigDecimal("1");
        return b.divide(one, 0, RoundingMode.HALF_UP).longValue();
    }

    public static void main(String[] args) {
        List<Integer> t = NumberUtil.getHongbao(500, 10, 10, 100);
        System.out.println(t);
    }

}
