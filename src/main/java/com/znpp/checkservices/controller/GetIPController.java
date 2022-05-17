package com.znpp.checkservices.controller;

import com.znpp.checkservices.service.GetIPService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@RestController
public class GetIPController {
    String ipv4 = null;
    @Resource
    GetIPService getIPService;

    /**
     * 获取服务器的IPv4地址
     */
    @GetMapping("/netip")
    public Map<String, Object> sayHello() {
        ipv4 = getIPService.GetNetIpV4();

        //返回IPv4
        Map<String, Object> result = new HashMap<>();
        result.put("IPv4", ipv4);
        return result;
    }
}
