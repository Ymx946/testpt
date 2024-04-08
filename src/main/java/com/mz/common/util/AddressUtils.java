package com.mz.common.util;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.City;
import com.maxmind.geoip2.record.Country;
import com.maxmind.geoip2.record.Postal;
import com.maxmind.geoip2.record.Subdivision;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.text.ParseException;

public class AddressUtils {

    public static AddressVO getAddressGeoIp(String ip, String databaseUrl) throws IOException {
        AddressVO addressVO = new AddressVO();
        if (ip.startsWith("100")) {
            addressVO.setNation("未知");
            addressVO.setProvince("未知");
            addressVO.setCity("未知");
            addressVO.setAddress("商级NAT IP");
        } else if (ip.equals("127.0.0.1")) {
            addressVO.setNation("未知");
            addressVO.setProvince("未知");
            addressVO.setCity("未知");
            addressVO.setAddress("本地IP");
        } else {
            // 创建 GeoLite2 数据库
//        File database = new File("E:/Download/geoLite2-city/GeoLite2-City.mmdb");
            File database = new File(databaseUrl);
            // 读取数据库内容
            DatabaseReader reader = new DatabaseReader.Builder(database).build();
            InetAddress ipAddress = InetAddress.getByName(ip);
            // 获取查询结果
            CityResponse response = null;
            try {
                response = reader.city(ipAddress);
//            System.out.println("-----------------AddressGeoIp.response---------"+response);
                String address = "";
                // 获取国家信息
                Country country = response.getCountry();
//            System.out.println(country.getIsoCode());               // 'CN'
//            System.out.println(country.getName());                  // 'China'
//            System.out.println(country.getNames().get("zh-CN"));    // '中国'
                addressVO.setNation(country.getNames().get("zh-CN"));
                address += country.getNames().get("zh-CN");
                // 获取省份
                Subdivision subdivision = response.getMostSpecificSubdivision();
//            System.out.println(subdivision.getName());   // 'Guangxi Zhuangzu Zizhiqu'
//            System.out.println(subdivision.getIsoCode()); // '45'
//            System.out.println(subdivision.getNames().get("zh-CN")); // '广西壮族自治区'
                address += subdivision.getNames().get("zh-CN");
                addressVO.setProvince(subdivision.getNames().get("zh-CN"));
                // 获取城市
                City city = response.getCity();
                Postal postal = response.getPostal();
//            System.out.println(city.getName()); // 'Nanning'
//            System.out.println(postal.getCode()); // 'null'
//            System.out.println(city.getNames().get("zh-CN")); // '南宁'
                address += city.getNames().get("zh-CN");
                addressVO.setCity(city.getNames().get("zh-CN"));
//            Location location = response.getLocation();
//            System.out.println(location.getLatitude());  // 22.8167
//            System.out.println(location.getLongitude()); // 108.3167
                addressVO.setAddress(address);
            } catch (GeoIp2Exception | IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        return addressVO;
    }

    public static void main(String[] args) throws ParseException, IOException {
//        113.215.189.137
//        100.97.74.139
        String ip = "74.125.153.104";
//        String databaseUrl = "E:/Download/geoLite2-city/GeoLite2-City.mmdb";
        String databaseUrl = "E:/Download/geoLite2-city/GeoLite2-City.mmdb";
        if (ip.startsWith("100")) {
            System.out.println("商级NAT IP");
        } else {
            System.out.println(getAddressGeoIp(ip, databaseUrl));
        }

    }
}