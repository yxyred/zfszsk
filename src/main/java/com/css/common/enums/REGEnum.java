package com.css.common.enums;

/**
 * 本地使用枚举维护远程字典项的编码和名称，
 * 在配置字典项时，请和团队沟通下
 */
public enum REGEnum implements IDicTable {

    REG("hlwjg_dict","执法监督系统字典项表");
    REGEnum(String code, String text){
        DicCodePool.putCodeItem(this, code, text);
    }

}
