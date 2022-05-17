package com.znpp.checkservices.comutils;

import java.net.*;
import java.util.Enumeration;

import static com.sun.xml.internal.ws.spi.db.BindingContextFactory.LOGGER;

public class NetUtils {

    public static void Test() {
        String hostAddress = null;
        String hostAddress1 = null;
        try {
            InetAddress address = InetAddress.getLocalHost();//获取的是本地的IP地址 //PC-20140317PXKX/192.168.0.121
            hostAddress = address.getHostAddress();//192.168.0.121
            InetAddress address1 = InetAddress.getByName("www.wodexiangce.cn");//获取的是该网站的ip地址，比如我们所有的请求都通过nginx的，所以这里获取到的其实是nginx服务器的IP地
            hostAddress1 = address1.getHostAddress();//124.237.121.122
            InetAddress[] addresses = InetAddress.getAllByName("www.baidu.com");//根据主机名返回其可能的所有InetAddress对象
            for(InetAddress addr:addresses){
                System.out.println(addr);//www.baidu.com/14.215.177.38
                //www.baidu.com/14.215.177.37
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }


        System.out.println("获取到本地IP地址为： "   +hostAddress);
        System.out.println("获取到本地IP地址为： "   +hostAddress1);
    }

    /** 获取主机地址 */
    public static String getHostIp(){

        String realIp = null;

        try {
            InetAddress address = InetAddress.getLocalHost();

            // 如果是回环网卡地址, 则获取ipv4 地址
            if (address.isLoopbackAddress()) {
                address = getInet4Address();
            }

            realIp = address.getHostAddress();

            LOGGER.info("获取主机ip地址成功, 主机ip地址: "+address);
            return address.getHostAddress();
        } catch (Exception e) {
            LOGGER.info("获取主机ip地址异常"+e);
        }

        return realIp;
    }

    /** 获取IPV4网络配置 */
    private static InetAddress getInet4Address() throws SocketException {
        // 获取所有网卡信息
        Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
        while (networkInterfaces.hasMoreElements()) {
            NetworkInterface netInterface = (NetworkInterface) networkInterfaces.nextElement();
            Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
            while (addresses.hasMoreElements()) {
                InetAddress ip = (InetAddress) addresses.nextElement();
                if (ip instanceof Inet4Address) {
                    return ip;
                }
            }
        }
        return null;
    }
}
