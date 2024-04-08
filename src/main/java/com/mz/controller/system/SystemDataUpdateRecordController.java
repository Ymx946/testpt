package com.mz.controller.system;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.mz.common.util.ExcelUtils;
import com.mz.model.base.BaseUser;
import com.mz.model.system.SystemDataUpdateRecord;
import com.mz.model.system.model.ExportUpdateRecordModel;
import com.mz.model.system.vo.SystemDataServiceNodeVO;
import com.mz.model.system.vo.SystemDataUpdateRecordVO;
import com.mz.service.system.SystemDataUpdateRecordService;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import com.mz.framework.annotation.NeedLogin;
import com.mz.common.util.Result;
import com.aliyuncs.utils.StringUtils;
import com.mz.common.util.ResponseCode;
import org.springframework.transaction.annotation.Transactional;
import com.mz.common.ConstantsUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * 服务节点版本更新记录(SystemDataUpdateRecord)表控制层
 *
 * @author makejava
 * @since 2023-12-29 10:01:11
 */
@Slf4j
@RestController
@RequestMapping(value = {"datacenter/systemDataUpdateRecord", "systemDataUpdateRecord"})
public class SystemDataUpdateRecordController {
    /**
     * 服务对象
     */
    @Autowired
    private SystemDataUpdateRecordService systemDataUpdateRecordService;

     /**
     * @return 对象列表
     * */
    @NeedLogin
    @Transactional
    @PostMapping("insert")
    public Result insert(SystemDataUpdateRecord pojo, @RequestHeader(value = "loginID") String loginID) {
        if (StringUtils.isEmpty(loginID)) {
            return Result.failed("loginID不能为空");
        }
        if (pojo.getNodeId()==null) {
            return Result.failed("节点ID不能为空");
        }
        if (StringUtils.isEmpty(pojo.getVersionNo())) {
            return Result.failed("版本号不能为空");
        }
        try {
            this.systemDataUpdateRecordService.insert(pojo,loginID);
            return Result.success();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Result(ResponseCode.SERVER_ERROR.getCode(), ResponseCode.SERVER_ERROR.getMsg());
        }
    }
  
    /**
     * 根据主键查询
     *
     * @return 对象列表
     */
    @NeedLogin
    @GetMapping("queryById")
    public Result queryById(Long id ) {
        if (null == id) {
            return Result.failed("ID必填");
        }
        try {
            return Result.success(this.systemDataUpdateRecordService.getById(id));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Result(ResponseCode.SERVER_ERROR.getCode(), ResponseCode.SERVER_ERROR.getMsg());
        }
    }

    /**
     * 列表page
     *
     * @return 对象列表
     */
    @NeedLogin
    @GetMapping("queryAllByLimit")
    public Result queryAllByLimit(SystemDataUpdateRecordVO vo ) {
        if (vo.getNodeId()==null) {
            return Result.failed("节点id不能为空");
        }
        try {
            return Result.success(this.systemDataUpdateRecordService.queryAllByLimit(vo));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Result(ResponseCode.SERVER_ERROR.getCode(), ResponseCode.SERVER_ERROR.getMsg());
        }
    }

    /**
     * 版本号列表查询
     *
     * @return 对象列表
     */
    @NeedLogin
    @GetMapping("queryVersionNo")
    public Result queryVersionNo(SystemDataUpdateRecordVO vo ) {
        if (vo.getNodeId()==null) {
            return Result.failed("节点id不能为空");
        }
        try {
            List<String> versionNoList = new ArrayList<>();
            List<SystemDataUpdateRecord> recordList = this.systemDataUpdateRecordService.queryAll(vo);
            if(CollectionUtil.isNotEmpty(recordList)){
                for (SystemDataUpdateRecord systemDataUpdateRecord : recordList) {
                    versionNoList.add(systemDataUpdateRecord.getVersionNo());
                }
            }
            return Result.success(versionNoList);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Result(ResponseCode.SERVER_ERROR.getCode(), ResponseCode.SERVER_ERROR.getMsg());
        }
    }
    /**
     * 删除
     * @return 对象列表
     * */
    @NeedLogin
    @Transactional
    @PostMapping("changeState")
    public Result changeState(Long id,Integer state) {
        if (id==null) {
            return Result.failed("id不能为空");
        }
        if (state==null) {
            return Result.failed("状态不能为空");
        }
        try {
            SystemDataUpdateRecord pojo = new SystemDataUpdateRecord();
            pojo.setId(id);//
            pojo.setState(state);
            this.systemDataUpdateRecordService.updateById(pojo);
            return Result.success();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Result(ResponseCode.SERVER_ERROR.getCode(), ResponseCode.SERVER_ERROR.getMsg());
        }
    }
    /**
     * 删除
     * @return 对象列表
     * */
    @NeedLogin
    @Transactional
    @PostMapping("delPojo")
    public Result delPojo(Long id) {
        if (id==null) {
            return Result.failed("id不能为空");
        }
        try {
            SystemDataUpdateRecord pojo = new SystemDataUpdateRecord();
            pojo.setId(id);
            pojo.setDelState(ConstantsUtil.IS_DEL);
            this.systemDataUpdateRecordService.updateById(pojo);
            return Result.success();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Result(ResponseCode.SERVER_ERROR.getCode(), ResponseCode.SERVER_ERROR.getMsg());
        }
    }


    /**
     * 导出
     */
    @NeedLogin
    @GetMapping("exportPojo")
    public void exportPojo(SystemDataUpdateRecordVO vo, HttpServletResponse response) throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        try {

            List<SystemDataUpdateRecord> list = systemDataUpdateRecordService.queryAll(vo);
            List<ExportUpdateRecordModel> resultList = new ArrayList<>();
            if(CollectionUtil.isNotEmpty(list)){
                for (SystemDataUpdateRecord systemDataUpdateRecord : list) {
                    ExportUpdateRecordModel model = new ExportUpdateRecordModel();
                    BeanUtil.copyProperties(systemDataUpdateRecord,model);
                    ExcelUtils.setDefaultValue(model);
                    resultList.add(model);
                }
            }
            List<Object> objList = ExcelUtils.toObject(resultList);
            List<String> fieldList = new ArrayList<String>();
            fieldList.add("日期");
            fieldList.add("版本号");
            fieldList.add("更新时间");
            fieldList.add("更新内容");
            long t1 = System.currentTimeMillis();
            ExcelUtils.writeExcelList(objList, fieldList, response);
            long t2 = System.currentTimeMillis();
            System.out.printf("write over! cost:%sms%n", (t2 - t1));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

}