package com.css.common.utils;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.*;

/**
 * 统计相关工具类
 */
public class StatisticsUtils {

    /**
     * 将分组后的数据 格式化 为 百分比格式 并返回 jsonObjectList
     * json格式 {"proportion":"23.08%","name":"XXX","count":3}
     * @param sum 数据的总数
     * @param collect  按某字段分组的数据
     * @return 结果不带分组的标准
     */
    public static List<JSONObject> getResultWithProportion(long sum,  Map<Object, Long> collect){

        //将分组后的数据放入jsonObjectList中，如{"proportion":"23.08%","name":"XXX","count":3}
        List<JSONObject> jsonObjectList = new ArrayList<JSONObject>();
        //存放分组数据后的百分比
        List<Double> proportionList = new ArrayList<Double>();
        collect.forEach((s,l)->{
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", s);
            jsonObject.put("count", l);
            double proportion = (double)l/(double)sum;
            try {
                //此处proportion已经为百分比的数值
                double round = BigDecimalUtils.round(proportion*100,2);
                proportion = round;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            jsonObject.put("proportion",proportion);
            proportionList.add(proportion);
            jsonObjectList.add(jsonObject);
        });

        //将jsonObjectList按 百分比小数点后position位的值 从大到小排序
        CollectionsSortUtils.sortByProportion(jsonObjectList,2,"proportion");
        //将proportionList放入处理百分比之和不为100%的算法
        Map proportionFormatMap = ProportionUtils.format(proportionList);
        List<Double> proportionResult  = (List<Double>) proportionFormatMap.get("result");
        //是否调换顺序的标识
        boolean reverseFlag = (boolean) proportionFormatMap.get("reverse");
        if(reverseFlag){
            Collections.reverse(proportionResult);
        }
        //将处理后的百分比数据放入jsonObjectList中，并格式化为xx.xx%
        for (int i = 0; i < jsonObjectList.size(); i++) {
            double proportion = proportionResult.get(i);
            String strProportion  = proportionFormat(proportion,2);
            jsonObjectList.get(i).put("proportion",strProportion);
        }
        return jsonObjectList;
    }

    /**
     * 暂时无用
     *
     * 将分组后的数据 格式化 为 百分比格式 并返回 jsonObjectList
     * json格式 {"proportion":"23.08%","name":"XXX","count":3}
     * 对jsonObject过滤
     * @param sum 数据的总数
     * @param collect  按某字段分组的数据
     * @return
     */
    public static List<JSONObject> getResult(long sum,  Map<Object, Long> collect,List standadList){
        List<JSONObject> jsonObjectList = getResultWithProportion(sum,collect);
        List<JSONObject> newJsonObjectList = new ArrayList<JSONObject>();
        for (int i = 0; i < standadList.size(); i++) {
            String standad = standadList.get(i).toString();
            boolean has = false ;
            int index = 0;
            for (int j = 0; j < jsonObjectList.size(); j++) {
                String name = jsonObjectList.get(j).get("name").toString();
                if(name.equals(standad)){
                    has = true;
                    index = j;
                    break;
                }
            }
            if(has){
                newJsonObjectList.add(jsonObjectList.get(index));
            }else {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name", standad);
                jsonObject.put("count", 0);
                jsonObject.put("proportion","0%");
                newJsonObjectList.add(jsonObject);
            }

        }
        return newJsonObjectList;
    }

    /**
     * 按分组的标准 对分组后的数据 格式化 为 百分比格式 并返回 jsonObjectList
     *
     * 对百分比过程中进行过滤
     * json格式 {"proportion":"23.08%","name":"XXX","count":3}
     * @param sum 数据的总数
     * @param collect  按某字段分组的数据
     * @param standadList  分组的标准
     * @return 结果带固定的分组标准
     */
    public static List<JSONObject> getResultWithProportion(long sum,  Map<Object, Long> collect,List standadList){
        //将分组后的数据放入jsonObjectList中，如{"proportion":"23.08%","name":"XXX","count":3}
        List<JSONObject> jsonObjectList = new ArrayList<JSONObject>();
        //存放分组数据后的百分比
        List<Double> proportionList = new ArrayList<Double>();
        for (int i = 0; i < standadList.size(); i++) {
            String standad = standadList.get(i).toString();
            boolean has = collect.keySet().contains(standad);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", standad);
            if(has){
                double proportion = (double)collect.get(standad)/(double)sum;
                jsonObject.put("count", collect.get(standad));
                try {
                    //此处proportion已经为百分比的数值
                    double round = BigDecimalUtils.round(proportion*100,2);
                    proportion = round;
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                jsonObject.put("proportion",proportion);
                proportionList.add(proportion);
            }else{
                jsonObject.put("count",0);
                jsonObject.put("proportion",0d);
                proportionList.add(0d);
            }
            jsonObjectList.add(jsonObject);

        }
        //将jsonObjectList按 百分比小数点后position位的值 从大到小排序
        CollectionsSortUtils.sortByProportion(jsonObjectList,2,"proportion");
        //将proportionList放入处理百分比之和不为100%的算法
        Map proportionFormatMap = ProportionUtils.format(proportionList);
        List<Double> proportionResult  = (List<Double>) proportionFormatMap.get("result");
        //是否调换顺序的标识
        boolean reverseFlag = (boolean) proportionFormatMap.get("reverse");
        if(reverseFlag){
            Collections.reverse(proportionResult);
        }
        //将处理后的百分比数据放入jsonObjectList中，并格式化为xx.xx%
        for (int i = 0; i < jsonObjectList.size(); i++) {
            double proportion = proportionResult.get(i);
            String strProportion  = proportionFormat(proportion,2);
            jsonObjectList.get(i).put("proportion",strProportion);
        }
        return jsonObjectList;
    }

    /**
     * 将分组后的数据 格式化 为 数量分组 并返回 jsonObjectList
     * json格式 {"name":"XXX","count":3}
     * @param sum 数据的总数
     * @param collect  按某字段分组的数据
     * @return
     */
    public static List<JSONObject> getResultWithoutProportion(long sum,  Map<Object, Long> collect){

        //将分组后的数据放入jsonObjectList中，如{"name":"XXX","count":3}
        List<JSONObject> jsonObjectList = new ArrayList<JSONObject>();

        collect.forEach((s,l)->{
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", s);
            jsonObject.put("count", l);
            jsonObjectList.add(jsonObject);
        });
        return jsonObjectList;
    }

    /**
     * 将分组后的数据 按五年的年份分组 并返回 jsonObjectList
     * json格式 {"name":"2019","count":3}
     * @param sum 数据的总数
     * @param collect  按某字段分组的数据
     * @param yearList  五年的年份list,如[2015,2016,2017,2018,2019]
     * @return 结果不带固定的分组标准
     */
    public static List<JSONObject> getResultWithoutProportion(long sum,  Map<Object, Long> collect,List yearList){

        //将分组后的数据放入jsonObjectList中，如{"name":"XXX","count":3}
        List<JSONObject> jsonObjectList = new ArrayList<JSONObject>();

        for (int i = 0; i < yearList.size() ; i++) {
           String year = yearList.get(i).toString();
            boolean has = collect.keySet().contains(year);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", year);
            if(has){
                jsonObject.put("count", collect.get(year));
            }else{
                jsonObject.put("count",0);
            }
            jsonObjectList.add(jsonObject);
        }
        return jsonObjectList;
    }

    /**
     * 将分组后的数据 按map中的key统计五年的年度数据 并返回 jsonObjectList
     * json格式 {"name":"XXX","count":"[3,4,3,3,2]"}
     * @param sum 数据的总数
     * @param collect  按某字段分组的数据
     * @param yearList  五年的年份list,如[2015,2016,2017,2018,2019]
     * @return 结果不带固定的分组标准
     */
    public static List<JSONObject> getResultWithYears(long sum,  Map<Object, Map<Object,Long>> collect,List yearList){

        //将分组后的数据放入jsonObjectList中，如{"name":"XXX","count":3}
        List<JSONObject> jsonObjectList = new ArrayList<JSONObject>();

        for(Object key:collect.keySet()){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", key);
            List yearCountList = new ArrayList();
            for (int i = 0; i < yearList.size() ; i++) {
                String year = yearList.get(i).toString();
                boolean has = collect.get(key).keySet().contains(year);
                if(has){
                    yearCountList.add(collect.get(key).get(year));
                }else{
                    yearCountList.add(0);
                }
            }
            jsonObject.put("count", yearCountList);
            jsonObject.put("years", yearList);
            jsonObjectList.add(jsonObject);
        }
        return jsonObjectList;
    }
    /**
     * 考试结果统计
     *
     * @param collectExamTimeSum 按年份分组的考试结果总数
     * @param collect 按年份分组和合格人数分组的考试结果数量
     * @param qualifiedList 合格和不合格的标识
     * @return
     */
    public static List<JSONObject> getExamTimetWithQualified(Map<String, Long> collectExamTimeSum,  Map<Object, Map<Object,Long>> collect,List qualifiedList){

        List<JSONObject> jsonObjectList = new ArrayList<JSONObject>();

        for(Object key:collect.keySet()){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", key);
            int sum = Integer.valueOf(collectExamTimeSum.get(key).toString());

            List countList = new ArrayList();
            List qualifiedNameList = new ArrayList();
            qualifiedNameList.add("合格人数（人）");
            qualifiedNameList.add("不合格人数（人）");
            qualifiedNameList.add("合格率（%）");
            qualifiedNameList.add("总人数（人）");
            //合格人数（人）和 不合格人数（人）
            for (int i = 0; i < qualifiedList.size() ; i++) {
                String qualifiedCode = qualifiedList.get(i).toString();
                boolean has = collect.get(key).keySet().contains(qualifiedCode);
                if(has){
                    countList.add(collect.get(key).get(qualifiedCode));
                }else{
                    countList.add(0);
                }
            }
            //合格率（%）
            if(collect.get(key).containsKey("1")){
                double num = Double.valueOf( collect.get(key).get("1").toString());
                try {
                    double proportion = BigDecimalUtils.div(num,(double)sum,2);
                    double round = BigDecimalUtils.round(proportion*100,2);
                    String s = proportionFormat(round, 2);
                    countList.add(s);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }else{
                countList.add(0);
            }
            //总人数
            countList.add(sum);
            jsonObject.put("count", countList);
            jsonObject.put("qualified", qualifiedNameList);
            jsonObjectList.add(jsonObject);
        }
        return jsonObjectList;
    }

    /**
     * 考试结果统计
     *
     * @param collectExamTimeSum 按年份分组的考试结果总数
     * @param collect 按年份分组和合格人数分组的考试结果数量
     * @param qualifiedList 合格和不合格的标识
     * @return
     */
    public static List<JSONObject> getExamTimetWithQualifiedAndYears(Map<String, Long> collectExamTimeSum,  Map<Object, Map<Object,Long>> collect,List qualifiedList,List yearList){

        List<JSONObject> jsonObjectList = new ArrayList<JSONObject>();
        for (int i = 0; i < yearList.size() ; i++) {

            JSONObject jsonObject = new JSONObject();
            List qualifiedNameList = new ArrayList();
            qualifiedNameList.add("合格人数（人）");
            qualifiedNameList.add("不合格人数（人）");
            qualifiedNameList.add("合格率（%）");
            qualifiedNameList.add("总人数（人）");
            List countList = new ArrayList();

            String year = yearList.get(i).toString();
            boolean haskey = collect.keySet().contains(year);
            //有当年的数据
            if(haskey){
                jsonObject.put("name", year);
                int sum = Integer.valueOf(collectExamTimeSum.get(year).toString());

                //合格人数（人）和 不合格人数（人）
                for (int j = 0; j < qualifiedList.size() ; j++) {
                    String qualifiedCode = qualifiedList.get(j).toString();
                    boolean has = collect.get(year).keySet().contains(qualifiedCode);
                    if(has){
                        countList.add(collect.get(year).get(qualifiedCode));
                    }else{
                        countList.add(0);
                    }
                }
                //合格率（%）
                if(collect.get(year).containsKey("1")){
                    double num = Double.valueOf( collect.get(year).get("1").toString());
                    try {
                        double proportion = BigDecimalUtils.div(num,(double)sum,2);
                        double round = BigDecimalUtils.round(proportion*100,2);
                        String s = proportionFormat(round, 2);
                        countList.add(s);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }else{
                    countList.add(0);
                }
                //总人数
                countList.add(sum);
                jsonObject.put("count", countList);

            }else{
                //没有当年的数据
                jsonObject.put("name", year);
                for (int k = 0; k < qualifiedNameList.size(); k++) {
                    countList.add(0);
                }
                jsonObject.put("count", countList);
            }
            jsonObject.put("qualified", qualifiedNameList);
            jsonObjectList.add(jsonObject);

        }

        return jsonObjectList;
    }


    /**
     * 将分组后的数据 按map中的key统计五年的年度数据 并返回 jsonObjectList
     * json格式 {"name":"XXX","count":"[3,4,3,3,2]"}
     * @param sum 数据的总数
     * @param collect  按某字段分组的数据
     * @param yearList  五年的年份list,如[2015,2016,2017,2018,2019]
     * @return 结果带固定的分组标准
     */
    public static List<JSONObject> getResultWithYears(long sum,  Map<Object, Map<Object,Long>> collect,List yearList,List standadList){

        //将分组后的数据放入jsonObjectList中，如{"name":"XXX","count":3}
        List<JSONObject> jsonObjectList = new ArrayList<JSONObject>();

        for (int i = 0; i < standadList.size(); i++) {
            String standad = standadList.get(i).toString();
            boolean hasStandad = collect.keySet().contains(standad);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", standad);
            List yearCountList = new ArrayList();
            for (int j = 0; j < yearList.size() ; j++) {
                if(hasStandad){
                    String year = yearList.get(j).toString();
                    boolean has = collect.get(standad).keySet().contains(year);
                    if(has){
                        yearCountList.add(collect.get(standad).get(year));
                    }else{
                        yearCountList.add(0);
                    }
                }else{
                    yearCountList.add(0);
                }
            }
            jsonObject.put("count", yearCountList);
            jsonObject.put("years", yearList);
            jsonObjectList.add(jsonObject);
        }
        return jsonObjectList;
    }

    /**
     * 格式化为百分比格式 小数点后位数不够自动补0
     * @param val 需要格式化的数据
     * @param position 精确小数后的位数
     * @return xx.xx%
     */
    public static String proportionFormat(double val,int position ){
//        NumberFormat numberFormat = NumberFormat.getInstance();
//        numberFormat.setMaximumFractionDigits(position);
////        double times = Math.pow(10,position);
//        String strProportion = numberFormat.format(val);
        String str = "%."+position+"f";
        String strProportion = String.format(str,val);
        return strProportion;
    }


}
