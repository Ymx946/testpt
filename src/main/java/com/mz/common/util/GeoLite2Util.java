package com.mz.common.util;

import com.maxmind.geoip2.DatabaseReader;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;

@Component
public class GeoLite2Util {
    /**
     * @param reader GeoLite2 数据库
     * @param ip     ip地址
     * @return
     * @throws Exception
     * @description: 获得国家
     */
    public static String getCountry(DatabaseReader reader, String ip) throws Exception {
        return reader.city(InetAddress.getByName(ip)).getCountry().getNames().get("zh-CN");
    }

    /**
     * @param reader GeoLite2 数据库
     * @param ip     ip地址
     * @return
     * @throws Exception
     * @description: 获得省份
     */
    public static String getProvince(DatabaseReader reader, String ip) throws Exception {
        return reader.city(InetAddress.getByName(ip)).getMostSpecificSubdivision().getNames().get("zh-CN");
    }

    /**
     * @param reader GeoLite2 数据库
     * @param ip     ip地址
     * @return
     * @throws Exception
     * @description: 获得城市
     */
    public static String getCity(DatabaseReader reader, String ip) throws Exception {
        return reader.city(InetAddress.getByName(ip)).getCity().getNames().get("zh-CN");
    }

    /**
     * @param reader GeoLite2 数据库
     * @param ip     ip地址
     * @return
     * @throws Exception
     * @description: 获得经度
     */
    public static Double getLongitude(DatabaseReader reader, String ip) throws Exception {
        return reader.city(InetAddress.getByName(ip)).getLocation().getLongitude();
    }

    /**
     * @param reader GeoLite2 数据库
     * @param ip     ip地址
     * @return
     * @throws Exception
     * @description: 获得纬度
     */
    public static Double getLatitude(DatabaseReader reader, String ip) throws Exception {
        return reader.city(InetAddress.getByName(ip)).getLocation().getLatitude();
    }

    public DatabaseReader getDatabaseReader(String filePath) throws IOException {
        return new DatabaseReader.Builder(new File(filePath)).build();
    }

}
