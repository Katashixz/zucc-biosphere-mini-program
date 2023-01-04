package com.biosphere.library.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 * @author  
 * @description: TODO
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
}
