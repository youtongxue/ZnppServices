package com.znpp.checkservices.service;

import com.znpp.checkservices.comutils.NetUtils;
import org.springframework.stereotype.Service;

@Service
public class GetIPService {
    String ipv4 = null;//服务器本机IPv4地址

    public String GetNetIpV4() {
        ipv4 = NetUtils.getHostIp();

        return ipv4;
    }
}
