package com.itheima.reggie.web;

import com.itheima.reggie.common.R;
import com.itheima.reggie.utils.QiniuUtils;
import com.itheima.reggie.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/common")
public class CommonController {
    @Autowired
    private QiniuUtils qiniuUtils;

    @PostMapping("/upload")
    public R<String> upload(MultipartFile file) {
        if (file.isEmpty()) {
            return R.error("上传文件为空，请重试...");
        }
        String fileName = StringUtils.getRandomImageName(Objects.requireNonNull(file.getOriginalFilename()));
        String path = null;
        try {
            path = qiniuUtils.upload(file.getInputStream(), fileName);
        } catch (IOException e) {
            e.printStackTrace();
            return R.error("服务器内部错误...");
        }
        return R.success(path);
    }

    @GetMapping("/download")
    public void download(HttpServletResponse response, String name) {
        InputStream is = null;
        ServletOutputStream os = null;
        String downloadUrl = qiniuUtils.getDownloadUrl(name);
        try {
            is = qiniuUtils.download(downloadUrl);
            os = response.getOutputStream();
            response.setContentType("image/jpeg");
            IOUtils.copy(is, os);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(is, os);
        }
    }
}
