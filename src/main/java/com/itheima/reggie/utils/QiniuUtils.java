package com.itheima.reggie.utils;

import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

@Component
public class QiniuUtils {

    @Autowired
    private UploadManager uploadManager;

    @Autowired
    private Auth auth;

    @Value("${qiniu.bucketName}")
    private String bucketName;

    @Value("${qiniu.path}")
    private String url;

    public String upload(InputStream file, String fileName) throws QiniuException {
        String token = auth.uploadToken(bucketName);
        Response res = uploadManager.put(file, fileName, token, null, null);
        if (!res.isOK()) {
            throw new RuntimeException("上传七牛云出错：" + res);
        }
        return url + "/" + fileName;
    }

    public String getDownloadUrl(String targetUrl) {
        return auth.privateDownloadUrl(targetUrl);
    }

    public InputStream download(String downloadUrl) throws IOException {
        ClientHttpRequest request = new OkHttp3ClientHttpRequestFactory().createRequest(URI.create("http://" + downloadUrl), HttpMethod.GET);
        ClientHttpResponse execute = request.execute();
        return execute.getBody();
    }
}
