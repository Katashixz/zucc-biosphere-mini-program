package com.biosphere.library.util;

import java.util.Date;

/**
 * @author  
 * @description: TODO
 * @date 2022/2/16 22:53
 */
public class DateUtil {
    public static Long BetweenDays(Date earlyDate,Date laterDate){

        Long betweenDays = (laterDate.getTime() - earlyDate.getTime()) / (1000L*3600L*24L);
        System.out.println(betweenDays);
        return betweenDays;
    }

}
