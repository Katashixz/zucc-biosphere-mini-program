package com.biosphere.library.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author  
 * @description: TODO
 * @date 2022/1/30 20:42
 */
public class FormatUtil {
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
}
