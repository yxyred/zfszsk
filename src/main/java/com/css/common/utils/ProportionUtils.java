package com.css.common.utils;

import org.apache.commons.collections.map.HashedMap;

import java.util.*;

/**
 * 百分比工具类
 */
public class ProportionUtils {


    /**
     * 处理百分比之和不为100%
     * @param result List<Double>
     * @return
     */
    public static Map format(List<Double> result) {
        Map map = new HashedMap();
//       List<Double> result = new ArrayList<Double>();
        // 13.626332% 47.989636% 9.596008% 28.788024
//        result.add(13.62);
//        result.add(47.98);
//        result.add(9.59);
//        result.add(28.78);

        CollectionsSortUtils.sortByProportion(result,2);

        double sum = 0;
        for (int i = 0; i < result.size(); i++) {
            sum = BigDecimalUtils.add(sum, result.get(i));
        }
        //与100的差值
        double d= BigDecimalUtils.sub(sum,100);
        //将小数最后一位替换为1
        double b=turnLastToOne(d);
        //获取小数最后一位
        int w=getTimes(d);
        boolean doOrNot=true;
        //和100比较大
        if(d>0){
            //调换顺序
            Collections.reverse(result);
            map.put("reverse",true);
            //百分比不做处理
//            if(getEquelsNum(result)>w){
//                doOrNot=false;
//            }
//            if(doOrNot) {
//                for (int i = 0; i < w; i++) {
//                    result.set(i, BigDecimalUtils.sub(result.get(i), b));
//                }
//            }
            map.put("result",result);
        //和比100小，要在指定几个上加上
        }else if(d<0){
            map.put("reverse",false);
            //百分比不做处理
//            if(getEquelsNum(result)>w){
//                doOrNot=false;
//            }
//            if(doOrNot) {
//                for (int i = 0; i < w; i++) {
//                    result.set(i, BigDecimalUtils.sub(result.get(i), b));
//                }
//            }
            map.put("result",result);
        }else{
            map.put("reverse",false);
            map.put("result",result);
        }
        return map;
    }

    private static int getEquelsNum(List<Double> result) {
        int k=1;
        double dd=-1d;
        for(double d:result){
            if(dd!=-1){
                if(dd==d){
                    k++;
                }
            }else{
                dd=d;
            }
        }
        return k;
    }
    private static int getTimes(double d) {
        String s=String.valueOf(d);
        return Integer.parseInt(s.substring(
                s.length()-1,s.length()));
    }
    private static double turnLastToOne(double d) {//获得指定位置上的数字
        String s=String.valueOf(d);
        s=s.substring(0,s.length()-1);
        return Double.parseDouble(s+"1");
    }

}
