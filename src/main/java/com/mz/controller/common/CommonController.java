package com.mz.controller.common;

import com.mz.common.util.*;
import com.mz.common.util.file.FdfsFileUtil;
import com.mz.common.util.file.OssUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;

/**
 * 公用控制层
 *
 * @author makejava
 * @since 2021-03-17 11:00:22
 */
@Slf4j
@RestController
@RequestMapping(value = {"server/commonController", "commonController"})
public class CommonController {

    @Value("${spring.profiles.active}")
    private String profile;
    @Autowired
    private OssUtil ossUtil;

    /**
     * 上传文件(获取文件地址)OSS
     */
    @SneakyThrows
    @PostMapping("getFileUrl")
    public Result getFileUrl(@RequestParam(value = "clientFile", required = false) MultipartFile clientFile, String tenantId,
                             @RequestParam(defaultValue = "false") Boolean needWH,
                             HttpServletRequest request) {
        if (ObjectUtils.isEmpty(clientFile)) {
            return Result.failed("文件不能为空");
        }

        AttaVo attaVo = new AttaVo();
        if ("agri".equalsIgnoreCase(profile)) {
            attaVo = FdfsFileUtil.uploadFile(clientFile, request);
        } else {
            attaVo = ossUtil.uploadFile(clientFile, tenantId, needWH, request);
        }

        if (ObjectUtils.isEmpty(attaVo)) {
            return Result.failed("文件格式错误");
        }

        return Result.success(attaVo.getUrl());
    }
}