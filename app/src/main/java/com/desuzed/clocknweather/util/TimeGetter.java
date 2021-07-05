package com.desuzed.clocknweather.util;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class TimeGetter {
    public Integer getHour (){
        return new GregorianCalendar().get(Calendar.HOUR);
    }
    public Integer getMinute (){
        return new GregorianCalendar().get(Calendar.MINUTE);
    }
    public Integer getSec(){
        return new GregorianCalendar().get(Calendar.SECOND);
    }
    public Integer getMSec (){
        return new GregorianCalendar().get(Calendar.MILLISECOND);
    }
}
