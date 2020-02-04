package com.css.common.enums;

/**
 * 本地使用枚举维护远程字典项的编码和名称
 */
public enum LawEnforcementOfficersEnum implements IDicTable {


    LAWENFORCEMENTOFFICERS("hlwjg_law_enforcement_officers","执法人员相关字典项"),
    REGDICT("hlwjg_dict","执法监督系统字典项表");

    LawEnforcementOfficersEnum(String code, String text){
            DicCodePool.putCodeItem(this, code, text);
        }

}
