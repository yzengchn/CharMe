/******************************************************************************
 * Copyright (C) 2015 ShenZhen HeShiDai Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为合时代控股有限公司开发研制。未经本公司正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.yzeng.charme.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @ClassName: WebUtils
 * @version 1.0
 * @Desc: WebUtils
 * @author xiaojun.zhou
 * @date 2015年6月25日下午1:49:28
 * @history v1.0
 */
public class WebUtils
{
    /**
     * 描述：获取request对象
     * @author xiaojun.zhou
     * @date 2015年6月25日下午1:56:05
     * @return
     */
    public static HttpServletRequest getRequest()
    {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    /**
     * 描述：获取responce对象
     * @author xiaojun.zhou
     * @date 2015年6月25日下午1:56:15
     * @return
     */
    public static HttpServletResponse getResponse()
    {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
    
    }

    /**
     * 描述：获取session
     * @author xiaojun.zhou
     * @date 2015年6月25日下午1:57:17
     * @return
     */
    public static HttpSession getSession()
    {
        return getRequest().getSession();
    }

    /**
     * 描述：设置session值
     * @author xiaojun.zhou
     * @date 2015年6月25日下午2:01:22
     * @param key
     * @param val
     */
    public static <T> void setSessionValue(String key, T val)
    {
        getSession().setAttribute(key, val);
    }

    /**
     * 描述：获取session值
     * @author xiaojun.zhou
     * @date 2015年6月25日下午2:01:38
     * @param key
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T getSessionValue(String key)
    {
        return (T) getSession().getAttribute(key);
    }

    /**
     * 描述：移除session
     * @date 2015年7月30日上午9:08:19
     * @param key
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T removeSessionValue(String key)
    {
        Object obj = getSession().getAttribute(key);
        getSession().removeAttribute(key);
        return (T) obj;
    }

    /**
     * 描述：获取客户端ip
     * @date 2015年6月25日下午5:12:55
     * @param request
     * @return
     */
    public static String getRemoteIP(HttpServletRequest request)
    {
        String ip = request.getHeader("x-forwarded-for");
        
        
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
        {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
        {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
        {
            ip = request.getRemoteAddr();
        }
        return ip.equals("0:0:0:0:0:0:0:1") ? "127.0.0.1" : ip;
    }

    /**
     * 描述：获取客户端ip
     * @author xiaojun.zhou
     * @date 2015年7月28日下午3:27:13
     * @return
     */
    public static String getRemoteIP()
    {
        HttpServletRequest request = getRequest();
        return getRemoteIP(request);
    }
    
    /**
     * 获取请求的参数列MAP
     *
     * @author cc HSSD0473
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static Map<String, Object> getRequestPar(){
    	Map<String, Object> map = new HashMap<>();
    	
		Enumeration<?> enu = getRequest().getParameterNames();
		while (enu.hasMoreElements())
		{
			String paraName = (String) enu.nextElement();
			map.put(paraName, getRequest().getParameter(paraName));
		}
    	
    	return map;
    }
    
    /**
     * 获取服务器IP地址
     */
    public final static String LOCAL_IP;
    static
    {
            String sIP = "";
            InetAddress ip = null;
            try
            {
                    boolean bFindIP = false;
                    Enumeration<NetworkInterface> netInterfaces = (Enumeration<NetworkInterface>) NetworkInterface
                                    .getNetworkInterfaces();
                    while (netInterfaces.hasMoreElements())
                    {
                            if (bFindIP)
                            {
                                    break;
                            }
                            NetworkInterface ni = (NetworkInterface) netInterfaces.nextElement();
                            Enumeration<InetAddress> ips =ni.getInetAddresses();
                            while (ips.hasMoreElements())
                            {
                                    ip = (InetAddress) ips.nextElement();
                                    if (!ip.isLoopbackAddress() && ip.getHostAddress().matches("(\\d{1,3}\\.){3}\\d{1,3}"))
                                    {
                                            bFindIP = true;
                                            break;
                                    }
                            }
                    }
            }
            catch (Exception e)
            {
                    
            }
            if (null != ip)
            {
                    sIP = ip.getHostAddress();
            }
            LOCAL_IP = sIP;
            
    }
}
