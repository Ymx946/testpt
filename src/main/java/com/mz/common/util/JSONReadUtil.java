package com.mz.common.util;


import com.mz.common.util.file.OssUtil;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class JSONReadUtil {

    public static String readJson(MultipartFile file, HttpServletRequest request) throws Exception {
        String jsonString = null;
        try {
            String fileName = file.getOriginalFilename();
            String prefix = fileName.substring(fileName.lastIndexOf("."));
            Path tempfile = null;
            tempfile = Files.createTempFile(OssUtil.getUUID(), prefix);
            file.transferTo(tempfile);
            File tempYstp = null;
            tempYstp = tempfile.toFile();
            FileReader fileReader = new FileReader(tempYstp);
            Reader reader = new InputStreamReader(new FileInputStream(tempYstp), StandardCharsets.UTF_8);
            int ch = 0;
            StringBuffer stringBuffer = new StringBuffer();
            while ((ch = reader.read()) != -1) {
                stringBuffer.append((char) ch);
            }
            jsonString = stringBuffer.toString();
            fileReader.close();
            reader.close();
        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonString;
    }
}
