package com.css.common.utils;

import org.apache.commons.collections.map.HashedMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 数据清洗工具类
 */
public class ProcessedData {
    /**
     * 清洗数据 字典项 通用 获取指定字段
     * @param list List(Map<String,Object>)
     * @param str  指定获取的字段
     * @returnl
     */
    public static List getProcessedDate(List list, String ...str){
        List newList = new ArrayList();
        for (int i = 0; i < list.size(); i++) {
            Map map = (Map) list.get(i);
            Map newMap = new HashedMap();
            for (int j = 0; j < str.length; j++) {
                newMap.put(str[j],map.get(str[j]));
            }
            newList.add(newMap);
        }
        return newList;
    }

    /**
     * 清洗数据 考试系统题型 字典项  获取指定字段
     *
     * 传入 "remark" 字段 返回 examScore(分值) sysFlag(是否自动判分的标识)
     * remark数据为(分值_是否自动判分的标识)
     * @param list List&lt;Map&lt;String,Object&gt;&gt;
     * @param str  指定获取的字段
     * @return List&lt;Map&lt;String,Object&gt;&gt;
     */
    public static List getExamQuestionTypeProcessedDate(List list, String ...str){
        List newList = new ArrayList();
        for (int i = 0; i < list.size(); i++) {
            Map map = (Map) list.get(i);
            Map newMap = new HashedMap();
            for (int j = 0; j < str.length; j++) {
                if("remark".equals(str[j])){
                    String[] remark =map.get(str[j]).toString().split("_");
                    newMap.put("examScore",remark[0]);
                    if(1<remark.length){
                        newMap.put("sysFlag",remark[1]);
                    }else{
                        newMap.put("sysFlag","");
                    }
                }else{
                    newMap.put(str[j],map.get(str[j]));
                }

            }
            newList.add(newMap);
        }
        return newList;
    }

    /**
     * 清洗数据 考试系统题型 字典项  获取指定字段
     *
     * 传入 "remark" 字段 返回 examScore(分值)
     * remark数据为(分值_是否自动判分的标识)
     * @param list List&lt;Map&lt;String,Object&gt;&gt;
     * @param str  指定获取的字段
     * @return List&lt;Map&lt;String,Object&gt;&gt;
     */
    public static List getExamQuestionTypeWithoutSysFlagProcessedDate(List list, String ...str){
        List newList = new ArrayList();
        for (int i = 0; i < list.size(); i++) {
            Map map = (Map) list.get(i);
            Map newMap = new HashedMap();
            for (int j = 0; j < str.length; j++) {
                if("remark".equals(str[j])){
                    String[] remark =map.get(str[j]).toString().split("_");
                    newMap.put("examScore",remark[0]);
                }else{
                    newMap.put(str[j],map.get(str[j]));
                }

            }
            newList.add(newMap);
        }
        return newList;
    }






    /**
     * 清洗数据
     * @param list List(Map<String,Object>)
     * @returnl
     */
    public static List getProcessedDateWithOutMap(List list, String ...str){
        List newList = new ArrayList();
        for (int i = 0; i < list.size(); i++) {
            Map map = (Map) list.get(i);

            for (int j = 0; j < str.length; j++) {

                newList.add(map.get(str[j]));
            }

        }
        return newList;
    }

}
