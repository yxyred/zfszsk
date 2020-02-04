package com.css.common.enums;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DicCodePool {
    /**
     * 用于存储代码项
     */
    private static final Map<IDicTable, DicCodeBean> codeItemMap = new ConcurrentHashMap<>();

    /**
     * 往 map 中添加代码项
     *
     * @param iDicTable 枚举类
     * @param code
     * @param text
     */
    public static final void putCodeItem(IDicTable iDicTable, String code, String text) {
        codeItemMap.put(iDicTable, new DicCodeBean(code, text));
    }

    public static final DicCodeBean getCodeItem(IDicTable iDicTable) {
        return codeItemMap.get(iDicTable);
    }
}
