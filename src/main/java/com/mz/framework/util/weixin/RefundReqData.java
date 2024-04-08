package com.mz.framework.util.weixin;

import com.mz.common.util.UUIDGenerator;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@XStreamAlias("xml")
public class RefundReqData {
    // 每个字段具体的意思请查看API文档
    private String appid = "";
    private String mch_id = "";

    /**
     * 微信支付分配的子商户号
     */
    private String sub_mch_id = "";
    // /**
    //  * 微信支付分配的机构商户号
    //  */
    // private String sp_mchid = "";

    // private String device_info = "";
    private String nonce_str = "";
    private String sign = "";
    private String transaction_id = "";
    private String out_trade_no = "";
    private String out_refund_no = "";
    private int total_fee = 0;
    private int refund_fee = 0;

    private String refund_account = "REFUND_SOURCE_RECHARGE_FUNDS";

    /**
     * 请求退款服务 (直连模式)
     *
     * @param transactionID 是微信系统为每一笔支付交易分配的订单号，通过这个订单号可以标识这笔交易，它由支付订单API支付成功时返回的数据里面获取到。
     *                      建议优先使用
     * @param outTradeNo    商户系统内部的订单号,transaction_id 、out_trade_no
     *                      二选一，如果同时存在优先级：transaction_id>out_trade_no
     * @param
     * @param outRefundNo   商户系统内部的退款单号，商户系统内部唯一，同一退款单号多次请求只退一笔
     * @param totalFee      订单总金额，单位为分
     * @param refundFee     退款总金额，单位为分,可以做部分退款
     * @param
     */
    public RefundReqData(String transactionID, String outTradeNo, String outRefundNo, int totalFee, int refundFee, String appid, String MchId, String apiKey) {
        // 微信分配的公众号ID（开通公众号之后可以获取到）
        setAppid(appid);

        // 微信支付分配的商户号ID（开通公众号的微信支付功能之后可以获取到）
        setMch_id(MchId);

        // transaction_id是微信系统为每一笔支付交易分配的订单号，通过这个订单号可以标识这笔交易，它由支付订单API支付成功时返回的数据里面获取到。
        setTransaction_id(transactionID);

        //transaction_id,out_trade_no二选一
        // 商户系统自己生成的唯一的订单号
        setOut_trade_no(outTradeNo);


        //商户系统内部的退款单号
        setOut_refund_no(outRefundNo);

        //订单总金额
        setTotal_fee(totalFee);

        //退款总金额
        setRefund_fee(refundFee);

        // 随机字符串，不长于32 位
        setNonce_str(UUIDGenerator.generate());

        // 根据API给的签名规则进行签名
        String sign = Signature.getSign(toMap(), apiKey);
        setSign(sign);// 把签名数据设置到Sign这个属性中
    }

    /**
     * 请求退款服务 (机构模式)
     *
     * @param transactionID 是微信系统为每一笔支付交易分配的订单号，通过这个订单号可以标识这笔交易，它由支付订单API支付成功时返回的数据里面获取到。
     *                      建议优先使用
     * @param outTradeNo    商户系统内部的订单号,transaction_id 、out_trade_no
     *                      二选一，如果同时存在优先级：transaction_id>out_trade_no
     * @param
     * @param outRefundNo   商户系统内部的退款单号，商户系统内部唯一，同一退款单号多次请求只退一笔
     * @param totalFee      订单总金额，单位为分
     * @param refundFee     退款总金额，单位为分,可以做部分退款
     * @param
     */
    public RefundReqData(String transactionID, String outTradeNo, String outRefundNo, int totalFee, int refundFee, String appid, String MchId, String sub_mchid, String apiKey) {

        // 微信分配的公众号ID（开通公众号之后可以获取到）
        setAppid(appid);

        // // 微信支付分配的商户号ID（开通公众号的微信支付功能之后可以获取到）
        // setSp_mchid(sp_mchid);

        // 微信支付分配的商户号ID（开通公众号的微信支付功能之后可以获取到）
        setMch_id(MchId);

        setSub_mch_id(sub_mchid);

        // transaction_id是微信系统为每一笔支付交易分配的订单号，通过这个订单号可以标识这笔交易，它由支付订单API支付成功时返回的数据里面获取到。
        setTransaction_id(transactionID);

        //transaction_id,out_trade_no二选一
        // 商户系统自己生成的唯一的订单号
        setOut_trade_no(outTradeNo);


        //商户系统内部的退款单号
        setOut_refund_no(outRefundNo);

        //订单总金额
        setTotal_fee(totalFee);

        //退款总金额
        setRefund_fee(refundFee);

        // 随机字符串，不长于32 位
        setNonce_str(UUIDGenerator.generate());

        // 根据API给的签名规则进行签名
        String sign = Signature.getSign(toMap(), apiKey);
        setSign(sign);// 把签名数据设置到Sign这个属性中
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getMch_id() {
        return mch_id;
    }

    public void setMch_id(String mch_id) {
        this.mch_id = mch_id;
    }

    public String getSub_mch_id() {
        return sub_mch_id;
    }

    public void setSub_mch_id(String sub_mch_id) {
        this.sub_mch_id = sub_mch_id;
    }

    public String getNonce_str() {
        return nonce_str;
    }

    public void setNonce_str(String nonce_str) {
        this.nonce_str = nonce_str;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getOut_refund_no() {
        return out_refund_no;
    }

    public void setOut_refund_no(String out_refund_no) {
        this.out_refund_no = out_refund_no;
    }

    public int getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(int total_fee) {
        this.total_fee = total_fee;
    }

    public int getRefund_fee() {
        return refund_fee;
    }

    public void setRefund_fee(int refund_fee) {
        this.refund_fee = refund_fee;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<String, Object>();
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field field : fields) {
            Object obj;
            try {
                obj = field.get(this);
                if (obj != null) {
                    map.put(field.getName(), obj);
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return map;
    }
}
