package com.mz;

import cn.hutool.core.date.DateUtil;
import com.gexin.fastjson.JSON;
import com.mz.common.util.IdWorker;
import com.mz.mapper.localhost.TabPublicSiteClickRecordMapper;
import com.mz.model.market.TabMarketMember;
import com.mz.model.market.vo.ImportMarketMember;
import com.mz.model.publicinfo.TabPublicSiteClickRecord;
import com.mz.service.market.TabMarketMemberService;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = FutureRuralApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ZjzwTest {

    @Resource
    TabPublicSiteClickRecordMapper tabPublicSiteClickRecordMapper;
    @Resource
    com.mz.mapper.localhost.TabMarketMemberMapper tabMarketMemberMapper;
    @Autowired
    private TabMarketMemberService tabMarketMemberService;

    public static void main(String[] args) {

    }

    /**
     * 从文件夹路径中读取文件内容
     */
    public static String readFileByPath(String filePath) {
        StringBuilder result = new StringBuilder();
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                return "";
            }
            FileInputStream fileInputStream = new FileInputStream(file);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                result.append(line);
                result.append("\n");
            }
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.toString();
    }


    private List<ImportMarketMember> readExcel(String path) {
        try {
            FileInputStream fileInputStream = new FileInputStream(path);
            Workbook workbook = new HSSFWorkbook(fileInputStream);
            Sheet sheet = workbook.getSheetAt(0);
            int lastRowNum = sheet.getLastRowNum();
            List<ImportMarketMember> list = new ArrayList<>();
            for (int i = 0; i <= lastRowNum; i++) {
                Row row = sheet.getRow(i);
                ImportMarketMember importMarketMember = new ImportMarketMember();
                importMarketMember.setNo(row.getCell(0).getNumericCellValue());
//                importMarketMember.setNo(Double.parseDouble(row.getCell(0).getStringCellValue()));
//                importMarketMember.setNo(row.getCell(0).getStringCellValue());
                importMarketMember.setUserName(row.getCell(1).getStringCellValue());

                Cell idNoCell = row.getCell(2);
                CellType idNoCellType = idNoCell.getCellType();
                if (idNoCellType.equals(CellType.NUMERIC)) {
                    double numericCellValue = idNoCell.getNumericCellValue();
                    CellStyle style = idNoCell.getCellStyle();
                    DecimalFormat format = new DecimalFormat();
                    String temp = style.getDataFormatString();
                    // 单元格设置成常规
                    if (temp.equals("General")) {
                        format.applyPattern("#");
                    }
                    importMarketMember.setIdNo(format.format(numericCellValue).replace(",", "").replace("'", ""));
                } else if (idNoCellType.equals(CellType.STRING)) {
                    importMarketMember.setIdNo(idNoCell.getStringCellValue().replace("'", ""));
                }

                Cell phoneCell = row.getCell(3);
                CellType phoneCellType = phoneCell.getCellType();
                if (phoneCellType.equals(CellType.NUMERIC)) {
                    double numericCellValue = phoneCell.getNumericCellValue();
                    CellStyle style = phoneCell.getCellStyle();
                    DecimalFormat format = new DecimalFormat();
                    String temp = style.getDataFormatString();
                    // 单元格设置成常规
                    if (temp.equals("General")) {
                        format.applyPattern("#");
                    }
                    importMarketMember.setPhone(format.format(numericCellValue).replace(",", ""));
                } else if (phoneCellType.equals(CellType.STRING)) {
                    importMarketMember.setPhone(phoneCell.getStringCellValue());
                }
                list.add(importMarketMember);
            }
            System.out.println(JSON.toJSONString(list));
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @Test
    public void testZjzwTxt() {
        try {
            String siteId = "1514873032714223616";
            String tenantId = "1478277449404907520";
            String areaCode = "330111110254";
            String areaName = "东梓关";

            String line = readFileByPath("C:\\Users\\yangh\\Desktop\\东梓关.txt");
            String[] split = line.split("\n");
            for (String l : split) {
                System.out.println(l);
                String[] tmp = l.split(",");
                String userName = tmp[2];
                String idnum = tmp[3];
                String mobile = tmp[1];

                TabMarketMember tabMarketMember = new TabMarketMember();
                if (StringUtils.isNoneBlank(mobile)) {
                    tabMarketMember = this.tabMarketMemberService.queryByPhone(siteId, tenantId, mobile);
                    if (null == tabMarketMember && StringUtils.isNoneBlank(idnum)) {
                        tabMarketMember = this.tabMarketMemberService.queryByIdNo(idnum, tenantId, siteId);
                    }
                } else {
                    if (StringUtils.isNoneBlank(idnum)) {
                        tabMarketMember = this.tabMarketMemberService.queryByIdNo(idnum, tenantId, siteId);
                    }
                }

                if (null == tabMarketMember || StringUtils.isBlank(tabMarketMember.getId())) {
                    TabMarketMember inertMarketMember = new TabMarketMember();
                    inertMarketMember.setTenantId(tenantId);
                    inertMarketMember.setSiteId(siteId);
                    inertMarketMember.setMemberPhone(mobile);
                    inertMarketMember.setIdNo(idnum);
                    inertMarketMember.setOwnerAreaCode("33");
                    inertMarketMember.setAreaCode(areaCode);
                    inertMarketMember.setAreaName(areaName);
                    inertMarketMember.setRealName(userName);
                    inertMarketMember.setMemberName(userName);
                    if (StringUtils.isNoneBlank(idnum)) {
                        inertMarketMember.setIdentityType(TabMarketMember.IDENTITY_TYPE_VILLAGER);
                    } else {
                        inertMarketMember.setIdentityType(TabMarketMember.IDENTITY_TYPE_TOURIST);
                    }
                    tabMarketMemberService.insert(inertMarketMember);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testZjzw() {
        try {
            String siteId = "1514873032714223616";
            String tenantId = "1478277449404907520";
            String areaCode = "330109104266";
            String areaName = "欢潭村";

            String path = "C:\\Users\\yangh\\Desktop\\欢潭村-330109104266.xls";

            List<ImportMarketMember> personList = readExcel(path);
            for (ImportMarketMember importMarketMember : personList) {
                String idnum = importMarketMember.getIdNo();
                String mobile = importMarketMember.getPhone();
                String userName = importMarketMember.getUserName();

                TabMarketMember tabMarketMember = new TabMarketMember();
                if (StringUtils.isNoneBlank(mobile)) {
                    tabMarketMember = this.tabMarketMemberService.queryByPhone(siteId, tenantId, mobile);
                    if (null == tabMarketMember && StringUtils.isNoneBlank(idnum)) {
                        tabMarketMember = this.tabMarketMemberService.queryByIdNo(idnum, tenantId, siteId);
                    }
                } else {
                    if (StringUtils.isNoneBlank(idnum)) {
                        tabMarketMember = this.tabMarketMemberService.queryByIdNo(idnum, tenantId, siteId);
                    }
                }

                if (null == tabMarketMember || StringUtils.isBlank(tabMarketMember.getId())) {
                    TabMarketMember inertMarketMember = new TabMarketMember();
                    inertMarketMember.setTenantId(tenantId);
                    inertMarketMember.setSiteId(siteId);
                    inertMarketMember.setMemberPhone(mobile);
                    inertMarketMember.setIdNo(idnum);
                    inertMarketMember.setOwnerAreaCode("33");
                    inertMarketMember.setAreaCode(areaCode);
                    inertMarketMember.setAreaName(areaName);
                    //                inertMarketMember.setZlbUserid(null);
                    //                inertMarketMember.setZlbAliuserid(zlbAliuserid);
                    inertMarketMember.setRealName(userName);
                    inertMarketMember.setMemberName(userName);
                    if (StringUtils.isNoneBlank(idnum)) {
                        inertMarketMember.setIdentityType(TabMarketMember.IDENTITY_TYPE_VILLAGER);
                    } else {
                        inertMarketMember.setIdentityType(TabMarketMember.IDENTITY_TYPE_TOURIST);
                    }
                    tabMarketMemberService.insert(inertMarketMember);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testData() {
        long tenantId = 1478277449404907520L;
        long siteId = 1514873032714223616L;
        String siteName = "浙里·未来乡村";
        String areaCode = "330521115220";
        String areaName = "五四村";
        long nodeId = 1535525244620505088l;
        String nodeName = "三务公开";

        TabMarketMember tabMarketMember = new TabMarketMember();
        tabMarketMember.setAreaCode(areaCode);
        List<TabMarketMember> tabMarketMembers = tabMarketMemberMapper.queryAll(tabMarketMember);

        for (TabMarketMember marketMember : tabMarketMembers) {
            TabPublicSiteClickRecord tabPublicSiteClickRecord = new TabPublicSiteClickRecord();
            tabPublicSiteClickRecord.setId(new IdWorker(0L, 0L).nextId());
            tabPublicSiteClickRecord.setTenantId(tenantId);
            tabPublicSiteClickRecord.setSiteId(siteId);
            tabPublicSiteClickRecord.setSiteName(siteName);
            tabPublicSiteClickRecord.setAreaCode(areaCode);
            tabPublicSiteClickRecord.setAreaName(areaName);
            tabPublicSiteClickRecord.setNodeId(nodeId);
            tabPublicSiteClickRecord.setNodeName(nodeName);

            tabPublicSiteClickRecord.setMemberId(Long.parseLong(marketMember.getId()));
            tabPublicSiteClickRecord.setMemberName(marketMember.getMemberName());

            String curTime = DateUtil.formatDateTime(new Date());
            tabPublicSiteClickRecord.setCreatTime(curTime);
            tabPublicSiteClickRecord.setCreatUser("沈国朝");
            tabPublicSiteClickRecord.setModifyTime(curTime);
            tabPublicSiteClickRecord.setModifyUser("沈国朝");

            tabPublicSiteClickRecordMapper.insert(tabPublicSiteClickRecord);
        }
    }

    private List<ImportMarketMember> readTxt(String path) {
        try {


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
