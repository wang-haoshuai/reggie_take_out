package com.itheima.test;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CommonTest {

    //@Value("${reggie.path}")

    @Test
    void testUpload() throws IOException {
        String basePath = "D:\\img2\\ia";
        String name = "error.jpg";

        Path path = Paths.get(basePath, name);
        File file1 = new File(basePath , name);
        System.out.println(file1);
    }
}
