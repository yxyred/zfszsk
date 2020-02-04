package com.css.common.enums;

/**
 * 字典表接口
 */
public interface IDicTable  {



    /**
     * 当前对象是否和已知对象不等
     * @param gender
     * @return
     */
    default boolean isNotEquals(IDicTable gender) {
        return this != gender;
    }


}
