package com.css.common.utils;

import com.css.common.enums.IDicTable;
import com.css.modules.remote.util.HttpRequestUtils;
import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class AreaUtils {
    @Autowired
    private HttpRequestUtils httpRequestUtils;
    @Autowired
    private SsoUrlUtils ssoUrlUtils;


    public  List getProvince() {
        String rl = ssoUrlUtils.getRl();
        String list = httpRequestUtils.sendHttpRequestForm(rl, null, HttpMethod.GET);
        List<Object> list2 = new ArrayList<>();
        String str = StringUtils.substringAfter(list, "data\":");
//        String str2 = str.substring(str.length(),str.length()-1);
        String str2 = str.substring(0, str.length() - 1);

        if (StringUtils.isNotBlank(list)) {
            List listTemp = new Gson().fromJson(str2, List.class);
            list2 = ProcessedData.getProcessedDate(listTemp, "bmAreaUuid", "administrativeAreaName");
        }
        return list2;
    }

    public List getCity(String parentId){
        String cl = ssoUrlUtils.getCl();

        String dl = cl+parentId;
        String list = httpRequestUtils.sendHttpRequestForm(dl, null, HttpMethod.GET);
        List<Object> list2 = new ArrayList<>();
        String str = StringUtils.substringAfter(list, "data\":");
//        String str2 = str.substring(str.length(),str.length()-1);
        String str2 = str.substring(0, str.length() - 1);

        if (StringUtils.isNotBlank(list)) {
            List listTemp = new Gson().fromJson(str2, List.class);
            list2 = ProcessedData.getProcessedDate(listTemp,"bmAreaUuid",  "administrativeAreaName");
        }
        return list2;
    }



    public  List getProvinceName() {
        String rl = ssoUrlUtils.getRl();
        String list = httpRequestUtils.sendHttpRequestForm(rl, null, HttpMethod.GET);
        List<Object> list2 = new ArrayList<>();
        String str = StringUtils.substringAfter(list, "data\":");
//        String str2 = str.substring(str.length(),str.length()-1);
        String str2 = str.substring(0, str.length() - 1);

        if (StringUtils.isNotBlank(list)) {
            List listTemp = new Gson().fromJson(str2, List.class);
            list2 = ProcessedData.getProcessedDate(listTemp, "administrativeAreaName");
        }
        return list2;
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
     * @param list getProvince()
     * @return
     */
    public String convertDict(String fromKey, String fromValue, String toKey,List list){
        if(StringUtils.isNotBlank(fromKey) && StringUtils.isNotBlank(fromValue)&& StringUtils.isNotBlank(toKey)){
            if(null != list && 0 < list.size()){
                int index = -1;
                boolean has = false;
                for (int i = 0; i < list.size(); i++) {
                    Map map = (Map)list.get(i);
                    int indexProvince =  fromValue.indexOf("省");
                    int indeCity = fromValue.indexOf("市");
                    if(-1 != indexProvince){
                        String val = fromValue.substring(0,indexProvince);
                        if(map.get(fromKey).toString().contains(val)){
                            index = i;
                            has = true;
                            break;
                        }
                    }
                    if(-1 != indeCity){
                        String val = fromValue.substring(0,indeCity);
                        if(map.get(fromKey).toString().contains(val)){
                            index = i;
                            has = true;
                            break;
                        }
                    }
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