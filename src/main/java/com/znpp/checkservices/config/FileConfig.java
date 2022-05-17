package com.znpp.checkservices.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.FileSystemResource;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.annotation.Resource;
import java.io.IOException;

@Configuration
public class FileConfig {

    @Resource
    private Environment env;

    //文件存储根目录
    public String getRootDir() {
        return env.getProperty("upload.root");
    }
    //临时文件存放的目录
    public String getTempFileDir(){
        return env.getProperty("upload.temp.filepath");
    }
    //文件最终存放的目录
    public String getFileDir(){
        return env.getProperty("upload.filepath");
    }

    /**
     * @Description CommonsMultipartResolver文件解析器，默认是StandardServletMultipartResolver
     **/
    @Bean(name = "multipartResolver")
    public MultipartResolver getFileResolver(){
        CommonsMultipartResolver resolver = new CommonsMultipartResolver();
        resolver.setDefaultEncoding(env.getProperty("upload.encode"));
        long maxSize = env.getProperty("upload.maxfilesize",Long.class);
        resolver.setMaxUploadSize(maxSize);
        try {
            String path = env.getProperty("upload.root")+env.getProperty("upload.temp");
            FileSystemResource resource = new FileSystemResource(path);
            resolver.setUploadTempDir(resource);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return resolver;
    }

}