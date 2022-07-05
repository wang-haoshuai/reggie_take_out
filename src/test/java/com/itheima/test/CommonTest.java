package com.itheima.test;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CommonTest {

    //@Value("${reggie.path}")

    @Test
    void testUpload() throws IOException {
        String basePath = "D:\\img1\\";
        String name = "error.jpg";
        //String prefix = name.substring(name.lastIndexOf("."));
        //String fileName = UUID.randomUUID() + prefix;
        //System.out.println(fileName);

        Path path = Paths.get(basePath, name);
        System.out.println(path);

        FileUtils.forceMkdirParent(path.toFile());
    }
}
