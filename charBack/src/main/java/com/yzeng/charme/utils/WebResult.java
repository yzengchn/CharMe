package com.yzeng.charme.utils;

import java.io.Serializable;


/**
 * @ClassName: WebResult
 * @version 1.0
 * @Desc: WEB返回JSON结果
 * @author xiaojun.zhou
 * @date 2015年5月26日上午10:23:01
 * @history v1.0
 */
public class WebResult implements Serializable
{
    private static final long serialVersionUID = -4776437900752507269L;

    /**
     * 后台请求成功
     */
    public static final String BACK_SUCCESS = "1";

    /**
     * 后台请求失败
     */
    public static final String BACK_FAIL = "0";
    
    /**
     * 后台请求成功
     */
    public static final int SUCCESS = 1;

    /**
     * 后台请求失败
     */
    public static final int FAIL = 0;
    /**
     * 返回消息
     */
    private String msg;

    /**
     * 返回码
     */
    private String code;

    /**
     * 返回数据
     */
    private Object data;

    public WebResult()
    {
    }

    public WebResult(String msg, String code)
    {
        super();
        this.msg = msg;
        this.code = code;
    }

    public WebResult(String msg, String code, Object data)
    {
        super();
        this.msg = msg;
        this.code = code;
        this.data = data;
    }

    public String getMsg()
    {
        return msg;
    }

    public void setMsg(String msg)
    {
        this.msg = msg;
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public Object getData()
    {
        return data;
    }

    public void setData(Object data)
    {
        this.data = data;
    }

    @Override
    public String toString()
    {
        return "WebResult [msg=" + msg + ", code=" + code + ", data=" + data + "]";
    }
    
    /**
     * 初始失败方法
     *
     * @author cc HSSD0473
     * @see [类、类#方法、类#成员]
     */
    public void invokeFail(){
    	this.data = null;
    	this.code = BACK_FAIL;
    	this.msg = "操作失败";
    }
    
    public void invokeFail(String msg){
    	this.data = null;
    	this.code = BACK_FAIL;
    	if(msg != null && !msg.equals(""))
    	{
    		this.msg = msg;
    	}
    }
    
    public void invokeSuccess()
    {
    	this.code = BACK_SUCCESS;
    	this.msg = "操作成功";
    }
    
    public void invokeSuccess(String msg)
    {
    	if(msg != null && !msg.equals(""))
    	{
    		this.msg = msg;
    	}
    	this.code = BACK_SUCCESS;
    }
    
    /**
     * 组装成功方法
     *
     * @author cc HSSD0473
     * @param msg
     * @param data
     * @see [类、类#方法、类#成员]
     */
    public void invokeSuccess(String msg, Object data){
    	if(msg != null && !msg.equals(""))
    	{
    		this.msg = msg;
    	}
    	if(data != null)
    	{
    		this.data = data;
    	}
    	this.code = BACK_SUCCESS;
    }

}
