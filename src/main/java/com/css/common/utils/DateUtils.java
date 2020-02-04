package com.css.common.utils;

import com.baomidou.mybatisplus.annotation.TableField;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 日期处理
 */
public class DateUtils {
	/** 时间格式(yyyy-MM-dd) */
	public final static String DATE_PATTERN = "yyyy-MM-dd";
	/** 时间格式(yyyy-MM-dd HH:mm:ss) */
	public final static String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    /**
     * 日期格式化 日期格式为：yyyy-MM-dd
     * @param date  日期
     * @return  返回yyyy-MM-dd格式日期
     */
	public static String format(Date date) {
        return format(date, DATE_PATTERN);
    }

    /**
     * 日期格式化 日期格式为：yyyy-MM-dd
     * @param date  日期
     * @param pattern  格式，如：DateUtils.DATE_TIME_PATTERN
     * @return  返回yyyy-MM-dd格式日期
     */
    public static String format(Date date, String pattern) {
        if(date != null){
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            return df.format(date);
        }
        return null;
    }

    /**
     * 字符串转换成日期
     * @param strDate 日期字符串
     * @param pattern 日期的格式，如：DateUtils.DATE_TIME_PATTERN
     */
    public static Date stringToDate(String strDate, String pattern) {
        if (StringUtils.isBlank(strDate)){
            return null;
        }

        DateTimeFormatter fmt = DateTimeFormat.forPattern(pattern);
        return fmt.parseLocalDateTime(strDate).toDate();
    }

    /**
     * 根据周数，获取开始日期、结束日期
     * @param week  周期  0本周，-1上周，-2上上周，1下周，2下下周
     * @return  返回date[0]开始日期、date[1]结束日期
     */
    public static Date[] getWeekStartAndEnd(int week) {
        DateTime dateTime = new DateTime();
        LocalDate date = new LocalDate(dateTime.plusWeeks(week));

        date = date.dayOfWeek().withMinimumValue();
        Date beginDate = date.toDate();
        Date endDate = date.plusDays(6).toDate();
        return new Date[]{beginDate, endDate};
    }

    /**
     * 对日期的【秒】进行加/减
     *
     * @param date 日期
     * @param seconds 秒数，负数为减
     * @return 加/减几秒后的日期
     */
    public static Date addDateSeconds(Date date, int seconds) {
        DateTime dateTime = new DateTime(date);
        return dateTime.plusSeconds(seconds).toDate();
    }

    /**
     * 对日期的【分钟】进行加/减
     *
     * @param date 日期
     * @param minutes 分钟数，负数为减
     * @return 加/减几分钟后的日期
     */
    public static Date addDateMinutes(Date date, int minutes) {
        DateTime dateTime = new DateTime(date);
        return dateTime.plusMinutes(minutes).toDate();
    }

    /**
     * 对日期的【小时】进行加/减
     *
     * @param date 日期
     * @param hours 小时数，负数为减
     * @return 加/减几小时后的日期
     */
    public static Date addDateHours(Date date, int hours) {
        DateTime dateTime = new DateTime(date);
        return dateTime.plusHours(hours).toDate();
    }

    /**
     * 对日期的【天】进行加/减
     *
     * @param date 日期
     * @param days 天数，负数为减
     * @return 加/减几天后的日期
     */
    public static Date addDateDays(Date date, int days) {
        DateTime dateTime = new DateTime(date);
        return dateTime.plusDays(days).toDate();
    }

    /**
     * 对日期的【周】进行加/减
     *
     * @param date 日期
     * @param weeks 周数，负数为减
     * @return 加/减几周后的日期
     */
    public static Date addDateWeeks(Date date, int weeks) {
        DateTime dateTime = new DateTime(date);
        return dateTime.plusWeeks(weeks).toDate();
    }

    /**
     * 对日期的【月】进行加/减
     *
     * @param date 日期
     * @param months 月数，负数为减
     * @return 加/减几月后的日期
     */
    public static Date addDateMonths(Date date, int months) {
        DateTime dateTime = new DateTime(date);
        return dateTime.plusMonths(months).toDate();
    }

    /**
     * 对日期的【年】进行加/减
     *
     * @param date 日期
     * @param years 年数，负数为减
     * @return 加/减几年后的日期
     */
    public static Date addDateYears(Date date, int years) {
        DateTime dateTime = new DateTime(date);
        return dateTime.plusYears(years).toDate();
    }

    /**
     * 根据当年 获取近N年的年份
     *
     * @param num 年的数量，负数为减
     * @return 加/减几年后的年份,包含当年
     */
    public  static List<Integer> getYears(int num){
        Date date = new Date();
        int currentYear = Integer.valueOf(format(date,"yyyy"));
        return getYearList(num,currentYear);
    }

    private static List<Integer> getYearList(int num ,int year){
        List yearList = new ArrayList();
        if(0 <= num){
            for (int i = 0; i < num; i++) {
                yearList.add(year + i);
            }
        }else{
            for (int i = num+1; i <= 0; i++) {
                yearList.add(year + i);
            }
        }
        return yearList;
    }


    /**
     * 获取指定的起始年份
     *
     * 结束年份可以比开始年份小
     *
     * @param yearStart 开始年份
     * @param yearEnd 结束年份
     * @return [yearStart,...,yearEnd]
     */
    public static List<Integer> getYears(int yearStart, int yearEnd) {
        int num = (yearEnd - yearStart) > 0 ? (yearEnd - yearStart) + 1 : (yearEnd - yearStart) - 1;
        return getYearList(num, yearStart);
    }

    /**
     * 计算相差年份
     *
     * @param year 年份
     * @return 和当年年份的差值
     */
    public static int calculateTimeGapYear(int year){
        Date date = new Date();
        int currentYear = Integer.valueOf(format(date,"yyyy"));
        int gapYear = currentYear - year;
        return gapYear;
    }


    /**
     * 根据生日计算年龄
     *
     * @param birthDay 生日
     * @return 年龄
     */
    public static int getAgeByBirthDay(String birthDay){
        if (birthDay == null || birthDay.length()<4) {
            return 0;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //得到当前的年份
        String cYear = sdf.format(new Date()).substring(0,4);
        String cMouth = sdf.format(new Date()).substring(5,7);
        String cDay = sdf.format(new Date()).substring(8,10);
        //得到生日年份
        String birth_Year = birthDay .substring(0,4);
        String birth_Mouth = birthDay .substring(5,7);
        String birth_Day = birthDay .substring(8,10);
        //年月日比较后得到年龄
        int age = Integer.parseInt(cYear) - Integer.parseInt(birth_Year);
        if ((Integer.parseInt(cMouth) - Integer.parseInt(birth_Mouth))<0) {
            age=age-1;
        }else if ((Integer.parseInt(cMouth) - Integer.parseInt(birth_Mouth))==0) {
            if ( (Integer.parseInt(cDay) - Integer.parseInt(birth_Day))>0) {
                age=age-1;
            }else {
                age = Integer.parseInt(cYear) - Integer.parseInt(birth_Year);
            }
        }else if ((Integer.parseInt(cMouth) - Integer.parseInt(birth_Mouth))>0) {
            age = Integer.parseInt(cYear) - Integer.parseInt(birth_Year);
        }
        return age;
    }

    /** 比较两个时间相差天数 */
    public static float calculateTimeGapDay(String time1, String time2) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");

        float day = 0;
        Date date1 = null;
        Date date2 = null;

        try {
            date1 = simpleDateFormat.parse(time1);
            date2 = simpleDateFormat.parse(time2);
            long millisecond = date2.getTime() - date1.getTime();
            day = millisecond / (24 * 60 * 60 * 1000);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return (day);
    }

    /** 比较两个时间相差天数 */
    public static float calculateTimeGapDay(Date time1, Date time2) {
        float day = 0;
        try {
            Date date1, date2;
            date1 = time1;
            date2 = time2;
            long millisecond = date2.getTime() - date1.getTime();
            day = millisecond / (24 * 60 * 60 * 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (day);
    }

    /**
     * 比较两个时间相差秒数
     * */
    public static long calculateTimeGapSecond(String time1, String time2) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");

        long second = 0;
        Date date1 = null;
        Date date2 = null;

        try {
            date1 = simpleDateFormat.parse(time1);
            date2 = simpleDateFormat.parse(time2);
            long millisecond = date2.getTime() - date1.getTime();
            second = millisecond / 1000;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return (second);
    }

    /**
     *  比较两个时间相差秒数
     * */
    public static float calculateTimeGapSecond(Date time1, Date time2) {
        long second = 0;
        try {
            Date date1, date2;
            date1 = time1;
            date2 = time2;
            long millisecond = date2.getTime() - date1.getTime();
            second = millisecond / 1000;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (second);
    }



}
