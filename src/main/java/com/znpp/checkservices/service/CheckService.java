package com.znpp.checkservices.service;

import com.dtflys.forest.http.ForestResponse;
import com.znpp.checkservices.client.CheckClient;
import com.znpp.checkservices.entity.FileInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class CheckService {
    @Resource
    private CheckClient checkClient;
    FileInfo fileInfo = new FileInfo();

    public ForestResponse checkImage(String path, String imgName) {
        fileInfo.setPath(path);
        fileInfo.setImgName(imgName);
        ForestResponse res = checkClient.checkImage(fileInfo);
        return res;
    }

}
