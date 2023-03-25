package com.biosphere.library.util;

import com.alibaba.fastjson.JSON;
import com.biosphere.library.vo.RespBeanEnum;
import com.biosphere.library.vo.ResponseResult;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 * @author  
 * @description: 一些基本封装
 * @date 2022/1/30 20:42
 */
public class CommonUtil {
    public static String DateFormat(Date date){
        SimpleDateFormat sfm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String res = sfm.format(date);
        return res;
    }

    public static String toDay(Date date){
        SimpleDateFormat sfm = new SimpleDateFormat("yyyy-MM-dd");
        String res = sfm.format(date);
        return res;
    }

    /**
     * 功能描述: 传入字符串如果为null或者长度为0返回true
     */
    public static boolean isBlank(String obj){
        obj = obj.replaceAll("\\s*", "");
        return obj.isEmpty() || Objects.isNull(obj);
    }

    /**
     * 功能描述: 返回JSONString
     */
    public static String toChannelErrorMsg(RespBeanEnum obj){
        return JSON.toJSONString(ResponseResult.error(obj));
    }

    public static String toChannelSuccessMsg(Object obj){
        return JSON.toJSONString(ResponseResult.success(obj));
    }

}
