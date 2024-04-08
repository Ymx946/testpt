package com.mz;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.maxmind.geoip2.DatabaseReader;
import com.mz.common.ConstantsUtil;
import com.mz.common.context.PageInfo;
import com.mz.common.util.GeoLite2Util;
import com.mz.model.device.TabPublicDevice;
import com.mz.service.device.TabPublicDeviceService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {FutureRuralApplication.class, TabPublicDeviceService.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DeviceTest {

    @Autowired
    private GeoLite2Util geoLite2Util;

    @Autowired
    private TabPublicDeviceService tabPublicDeviceService;

    public static void main(String[] args) {
        Map<?, ?> retMap = new HashMap<>();
//        retMap = DeviceUtil.getDeviceLastData("15112501");
////        int statusCode = Integer.parseInt(retMap.get("statusCode")+"");
////        if(200 == statusCode){
////            Object retObj = retMap.get("entity");
////        }

//        String device_token = DeviceUtil.getDeviceToken("test", "123456");
//        retMap = DeviceUtil.getDeviceUserInfo(device_token, "test", "");
//        log.info(retMap.toString());
//        retMap = DeviceUtil.getDeviceInfo(device_token, 56289911);
//        JSONArray deviceElementInfo = DeviceUtil.getDeviceElementInfo(device_token);

//        JSONArray deviceRelayInfo = DeviceUtil.getDeviceRelayInfo(device_token);
//        retMap= DeviceUtil.postDeviceRelayController(device_token, 15112502, 0, 1);
//        retMap= DeviceUtil.postDeviceIMEIInfo(device_token, "864650052449862");
//        retMap = DeviceUtil.postDeviceDataExtendData(device_token, "864650052449862");
//        retMap = DeviceUtil.postDeviceImeiImage(device_token, "864650058053254");
//        retMap = DeviceUtil.postDeviceIMEIImageList(device_token, "864650058053254", 1, 8, String startTime, String endTime, String sort);
//        log.info(retMap.toString());


        Map<String, Object> map = new HashMap<>();
        map.put("name", "test");
        map.put("id", 123);
        System.out.println(map.hashCode());

    }

    @Test
    public void testGeoLite2Util() throws Exception {
        DatabaseReader reader = geoLite2Util.getDatabaseReader("E:/tool/geoLite/GeoLite2-City.mmdb");
        // 访问IP
        String ip = "74.125.153.104";
        String site = "国家：" + GeoLite2Util.getCountry(reader, ip) + "\n省份：" + GeoLite2Util.getProvince(reader, ip) + "\n城市：" + GeoLite2Util.getCity(reader, ip) + "\n经度：" + GeoLite2Util.getLongitude(reader, ip) + "\n纬度：" + GeoLite2Util.getLatitude(reader, ip);
        System.out.println(site);
    }

    @Test
    public void testDevice() {
        PageHelper.startPage(1, 10);

        List<TabPublicDevice> devices = tabPublicDeviceService.lambdaQuery().eq(TabPublicDevice::getDelState, ConstantsUtil.IS_DONT_DEL).orderByDesc(TabPublicDevice::getCreateTime).list();
        devices.forEach(System.out::println);
        PageInfo<TabPublicDevice> tabPublicDevicePageInfo = new PageInfo<>(devices);
        System.out.println(tabPublicDevicePageInfo);

        Page<TabPublicDevice> page = new Page<>(1, 10);
        Page<TabPublicDevice> iPage = tabPublicDeviceService.page(page);

        System.out.println("总页数：" + iPage.getPages());
        System.out.println("总数：" + iPage.getTotal());
        System.out.println("页数：" + iPage.getSize());
        System.out.println("是否存在上一页：" + iPage.hasPrevious());
        System.out.println("是否存在下一页：" + iPage.hasNext());
        System.out.println("页码：" + iPage.getCurrent());


    }

}