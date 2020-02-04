package com.css.common.utils;

import com.css.common.enums.CommonEnums;
import com.css.common.enums.DicCodePool;
import com.css.common.enums.IDicTable;
import com.css.modules.remote.util.HttpRequestUtils;
import com.google.gson.Gson;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class REnumUtil {
    @Autowired
    private HttpRequestUtils httpRequestUtils;
    @Autowired
    private SsoUrlUtils ssoUrlUtils;

    /**
     * 根据字典表名获取字典列表
     * @param tableName
     * @return
     */
    public  String queryDictListByTableUrl(String tableName){
        String url = ssoUrlUtils.getDictListByTableUrl() + tableName;
        return httpRequestUtils.sendHttpRequestForm(url, null, HttpMethod.GET);
    }

    /**
     * 根据字典编码，父节点编码，查询子集字典项
     * @param parentCode
     * @param tableName
     * @return
     */
    public String getDictsByDictCode(String parentCode, String tableName){
        String url = ssoUrlUtils.getDictsByDictCodeUrl() + parentCode + "&tableName=" + tableName;
        return httpRequestUtils.sendHttpRequestForm(url, null, HttpMethod.GET);
    }





    /**
     * 获取字典项的name和code
     *
     * @param parentCode 字典编码
     * @param tableName 父节点
     * @return
     */
    public List getCodeAndNameList( IDicTable parentCode, IDicTable tableName){
        List list = new ArrayList();
        String dict = getDictsByDictCode(
                DicCodePool.getCodeItem(parentCode).getCode(),
                DicCodePool.getCodeItem(tableName).getCode());
        if (StringUtils.isNotBlank(dict)) {
            List listTemp = new Gson().fromJson(dict, List.class);
            list = ProcessedData.getProcessedDate(listTemp,"name","code");
        }
        return list;
    }

    /**
     * 获取字典项的name和code
     *
     * @param parentCode 字典编码
     * @param tableName 父节点
     * @return
     */
    public List getCodeAndNameList( String parentCode, String tableName){
        List list = new ArrayList();
        String dict = getDictsByDictCode(
                parentCode,
                tableName);
        if (StringUtils.isNotBlank(dict)) {
            List listTemp = new Gson().fromJson(dict, List.class);
            list = ProcessedData.getProcessedDate(listTemp,"name","code");
        }
        return list;
    }

    /**
     * 获取考试系统 字典项 的
     * name(题型名称) code(题型code) examScore(分值) sysFlag(是否自动判分的标识)
     *
     * @param parentCode 字典编码
     * @param tableName 父节点
     * @return List&lt;Map&lt;String,Object&gt;&gt;
     */
    public List getExamQuestionTypeList( IDicTable parentCode, IDicTable tableName){
        List list = new ArrayList();
        String dict = getDictsByDictCode(
                DicCodePool.getCodeItem(parentCode).getCode(),
                DicCodePool.getCodeItem(tableName).getCode());
        if (StringUtils.isNotBlank(dict)) {
            List listTemp = new Gson().fromJson(dict, List.class);
            list = ProcessedData.getExamQuestionTypeProcessedDate(listTemp,"name","code","remark");
        }
        return list;
    }

    /**
     * 获取考试系统 字典项 的
     * name(题型名称) code(题型code) examScore(分值)
     *
     * @param parentCode 字典编码
     * @param tableName 父节点
     * @return List&lt;Map&lt;String,Object&gt;&gt;
     */
    public List getExamQuestionTypeWithoutSysFlagList( IDicTable parentCode, IDicTable tableName){
        List list = new ArrayList();
        String dict = getDictsByDictCode(
                DicCodePool.getCodeItem(parentCode).getCode(),
                DicCodePool.getCodeItem(tableName).getCode());
        if (StringUtils.isNotBlank(dict)) {
            List listTemp = new Gson().fromJson(dict, List.class);
            list = ProcessedData.getExamQuestionTypeWithoutSysFlagProcessedDate(listTemp,"name","code","remark");
        }
        return list;
    }

    /**
     * 获取考试系统 字典项 的
     * name(题型名称) code(题型code) examScore(分值)
     *
     * @param parentCode 字典编码
     * @param tableName 父节点
     * @return List&lt;Map&lt;String,Object&gt;&gt;
     */
    public List getExamQuestionTypeWithoutSysFlagList( String parentCode, String tableName){
        List list = new ArrayList();
        String dict = getDictsByDictCode(
                parentCode,
                tableName);
        if (StringUtils.isNotBlank(dict)) {
            List listTemp = new Gson().fromJson(dict, List.class);
            list = ProcessedData.getExamQuestionTypeWithoutSysFlagProcessedDate(listTemp,"name","code","remark");
        }
        return list;
    }

    /**
     * 获取字典项的name
     *
     * @param parentCode 字典编码
     * @param tableName 父节点
     * @return
     */
    public List getNameList( IDicTable parentCode, IDicTable tableName){
        List list = new ArrayList();
        String dict = getDictsByDictCode(
                DicCodePool.getCodeItem(parentCode).getCode(),
                DicCodePool.getCodeItem(tableName).getCode());
        if (StringUtils.isNotBlank(dict)) {
            List listTemp = new Gson().fromJson(dict, List.class);
            list = ProcessedData.getProcessedDateWithOutMap(listTemp,"name");
        }
        return list;
    }

    /**
     * 一次性获取多个字典项的name和code
     *
     * @param jsonObject 用于存储返回值的jsonObject
     * @param allparentCodeList 想要获取字典项的字典编码list
     * @param tableName 对应的父节点
     * @return
     */
    public JSONObject getExamQuestionTypeWithoutSysFlagList( JSONObject jsonObject,List allparentCodeList, String tableName){
        for (int i = 0; i < allparentCodeList.size(); i++) {
            String key = allparentCodeList.get(i).toString();
            List list = getExamQuestionTypeWithoutSysFlagList(key,tableName);
            String praceKey = praceKey(key);
            jsonObject.put(praceKey, list);
        }
        return jsonObject;
    }

    /**
     * 一次性获取多个字典项的name和code
     *
     * @param jsonObject 用于存储返回值的jsonObject
     * @param allparentCodeList 想要获取字典项的字典编码list
     * @param tableName 对应的父节点
     * @return
     */
    public JSONObject getCodeAndNameList( JSONObject jsonObject,List allparentCodeList, String tableName){
        for (int i = 0; i < allparentCodeList.size(); i++) {
            String key = allparentCodeList.get(i).toString();
            List list = getCodeAndNameList(key,tableName);
            String praceKey = praceKey(key);
            jsonObject.put(praceKey, list);
        }
        return jsonObject;
    }


    /**
     * 转换 key
     * @param str
     * @return
     */
    private String praceKey(String str){
        String[] list = str.split("_");
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(list[1]);
        if(2 <= list.length){
            for (int i = 2; i < list.length; i++) {
                stringBuffer.append(list[i].substring(0,1).toUpperCase() + list[i].substring(1));
            }
        }
        return stringBuffer.toString();
    }

    /**
     * 字典项 code 和 name 的 value 相互转换
     *
     * 例：传入fromKey=code，fromValue=1，toKey=name，
     * 返回 是
     *
     * @param fromKey 需要转换的key
     * @param fromValue 需要转换的key的value
     * @param toKey   转换为key
     * @param list getCodeAndNameList(parentCode,tableName)
     * @return
     */
    public String convertDict(String fromKey,String fromValue,String toKey,List list){

        if(StringUtils.isNotBlank(fromKey) && StringUtils.isNotBlank(fromValue)&& StringUtils.isNotBlank(toKey)){
            if(null != list && 0 < list.size()){
                int index = -1;
                boolean has = false;
                for (int i = 0; i < list.size(); i++) {
                    Map map = (Map)list.get(i);
                    if(fromValue.equals(map.get(fromKey))){
                        index = i;
                        has = true;
                        break;
                    }
                }
                if(index != -1 && has == true){
                    Map map = (Map)list.get(index);
                    return map.get(toKey).toString();
                }else{
                    return "";
                }
            }else{
                return "";
            }
        }else{
            return "";
        }

    }

}
