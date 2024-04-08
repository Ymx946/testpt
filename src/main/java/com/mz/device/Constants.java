package com.mz.device;

public class Constants {


    public final static String ACCOUNT_TEST = "test";
//    public final static String ACCOUNT_PROD = "H810040";
    public final static String ACCOUNT_PROD = "H810131";

    public final static String PWD_TEST = "123456";
    public final static String PWD_PROD = "88888888";
    public final static String BASE_URL = "http://101.34.116.221:8005";

    // 获取设备最新数据
    public final static String DEVICE_LASTDATA_URL = BASE_URL + "/intfa/queryData/DEVICEID";
    public final static String DEVICE_PEST_LASTDATA_URL = BASE_URL + "/pest/intfa/queryData/DEVICEID";

    // 根据用户名密码获取token
    public final static String DEVICE_GET_TOKEN = BASE_URL + "/login";

    // 获取用户信息
    public final static String DEVICE_GET_USERINFO = BASE_URL + "/user/USERNAME";

    // 获取要素列表
    public final static String DEVICE_GET_ELEMENT = BASE_URL + "/element";

    // 获取继电器列表、继电器控制
    public final static String DEVICE_GET_RELAY = BASE_URL + "/relay";

    // 获取设备信息
    public final static String DEVICE_GET_DEVICEINFO = BASE_URL + "/device/DEVICEID";

    // 获取设备数据及继电器状态
    public final static String DEVICE_GET_DEVICESTATE = BASE_URL + "/data/DEVICEID";

    // 获取虫情设备信息
    public final static String DEVICE_GET_IMEIINFO = BASE_URL + "/pest/config/IMEI";
    public final static String DEVICE_GET_IMEIINFO_ONLINE = BASE_URL + "/pest/status/IMEI";

    // 获取虫情设备监测数据
    public final static String DEVICE_GET_DATAEXTEND_DATA = BASE_URL + "/pest/dataextend/IMEI";

    // 获取最新虫情图片、获取虫情图片列表
    public final static String DEVICE_GET_IMEI_IMAGE = BASE_URL + "/pest/image/IMEI";

    // 虫情设备调试
    public final static String DEVICE_GET_IMEI_DEGUG = BASE_URL + "/pest/debug/IMEI";

}
