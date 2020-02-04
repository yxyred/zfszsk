package com.css.common.utils;

import org.json.JSONObject;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 百分比排序工具类
 */
public class CollectionsSortUtils {

    /**
     * 将jsonObjectList按 百分比小数点后position位的值 从大到小排序
     * @param jsonObjectList
     * @param position 小数点后的位数
     */
    public static void sortByProportion(List<JSONObject> jsonObjectList,int position,String key){
        Collections.sort(jsonObjectList, new Comparator<JSONObject>() {
//            private static final String key = "proportion";
            @Override
            public int compare(JSONObject o1, JSONObject o2) {
                try {
                    double valo1 = BigDecimalUtils.round((double)o1.get(key),position);
                    double valo2 =  BigDecimalUtils.round((double)o2.get(key),position);
                    return (getValue(String.valueOf(valo2), position))-(getValue(String.valueOf(valo1), position));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                return 0;
            }
        });
    }
    /**
     * 将doubleList按 百分比小数点后position位的值 从大到小排序
     * @param doubleList
     * @param position 小数点后的位数
     */
    public static void sortByProportion(List<Double> doubleList,int position){
        Collections.sort(doubleList, new Comparator<Double>() {
            @Override
            public int compare(Double o1, Double o2) {
                return (getValue(String.valueOf(o2), position))-(getValue(String.valueOf(o1), position));
            }
        });
    }

    /**
     * 获得指定位置上的数字
     * @param s
     * @param position
     * @return
     */
    private static int getValue(String s, int position) {
        try {
            return Integer.parseInt(s.substring(s.indexOf(".") + position, s.indexOf(".") + position + 1));
        }catch (StringIndexOutOfBoundsException e){
            return -1;
        }
    }



}
