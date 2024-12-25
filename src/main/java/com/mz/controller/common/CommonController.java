package com.mz.controller.common;

import com.mz.common.util.AttaVo;
import com.mz.common.util.LocalUploadUtil;
import com.mz.common.util.Result;
import com.mz.common.util.file.FdfsFileUtil;
import com.mz.common.util.file.OssUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

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
    public Result getFileUrl(@RequestParam(value = "clientFile", required = false) MultipartFile clientFile,
                             @RequestParam(defaultValue = "false") Boolean needWH,
                             HttpServletRequest request) {
        if (ObjectUtils.isEmpty(clientFile)) {
            return Result.failed("文件不能为空");
        }

        AttaVo attaVo = new AttaVo();
        if ("agri".equalsIgnoreCase(profile)) {
            attaVo = FdfsFileUtil.uploadFile(clientFile, request);
        } else {
//            attaVo = ossUtil.uploadFile(clientFile, tenantId, needWH, request);
            attaVo = ossUtil.uploadToLocalFile(clientFile, needWH);
        }

        if (ObjectUtils.isEmpty(attaVo)) {
            return Result.failed("文件格式错误");
        }
        return Result.success(attaVo);
    }

    @PostMapping("image")
    public void downloadImage(HttpServletResponse response)
            throws ServletException, IOException {
        // 图片的本地路径
        String imagePath = "D:\\localPic\\test_1735110672195.jpg";
        File imageFile = new File(imagePath);
        // 检查文件是否存在
        if (!imageFile.exists()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        // 设置响应头，告知浏览器要下载文件
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=test_1735110672195.jpg");
        // 读取文件并输出到响应流
        try (FileInputStream fis = new FileInputStream(imageFile);
             OutputStream os = response.getOutputStream()) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
        }
    }
}
