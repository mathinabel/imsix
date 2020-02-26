package com.quyuanjin.imsix.utils;

import com.quyuanjin.imsix.Constant;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimerHelper {

    public static int TimeType()  {

        //例如比较当前时间和早上6：00
        String nowTime = new SimpleDateFormat("HH:MM").format(new Date());


        int i = 0;
        try {
            i = DateCompare(nowTime,"18:00","HH:MM");
        } catch (Exception e) {
            e.printStackTrace();
        }
        switch (i) {
            case 0:
                return Constant.time_equal;
            case 1:
                return Constant.time_late;
            case -1:
                return Constant.time_early;

        }
        return Constant.time_error;
    }

    /**
     * 根据时间类型比较时间大小
     *
     * @param source
     * @param traget
     * @param type "YYYY-MM-DD" "yyyyMMdd HH:mm:ss"  类型可自定义
     * @return
     *  0 ：source和traget时间相同
     *  1 ：source比traget时间大
     *  -1：source比traget时间小
     * @throws Exception
     */
    public static int DateCompare(String source, String traget, String type) throws Exception {
        int ret = 2;
        SimpleDateFormat format = new SimpleDateFormat(type);
        Date sourcedate = format.parse(source);
        Date tragetdate = format.parse(traget);
        ret = sourcedate.compareTo(tragetdate);
        return ret;
    }
}
