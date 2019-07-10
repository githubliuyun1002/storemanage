package com.xiabuxiabu.storemanage.publicutils;

import org.springframework.stereotype.Component;
import java.util.Date;

/**
 * 比较两个Date之间相差得天数
 * notes:当两个天数，相差不足24小时，day为0
 */
@Component
public class DateTool {

    public static int differentDaysByMillisecond(Date date1, Date date2){
        int days = (int) ((date2.getTime() - date1.getTime()) / (1000*3600*24));
        return days;
    }
}
