package com.znpp.checkservices.client;

import com.dtflys.forest.annotation.JSONBody;
import com.dtflys.forest.annotation.Post;
import com.dtflys.forest.http.ForestResponse;
import com.znpp.checkservices.entity.FileInfo;

public interface CheckClient {
    /**
     * 检测图片，调用Python FastApi构建的本地Http接口
     */
    @Post(url = "http://127.0.0.1:8092/checkfile/")
    ForestResponse checkImage(@JSONBody FileInfo fileInfo);

}
