package com.css.common.enums;

import java.util.List;

public class CommonEnums {
    /**
     * 执法监督系统字典项表,固定的字典编码
     */
    public enum LawDict implements IDicTable {
        GENDER("hlwjg_gender","性别"),
        UNITTYPENAME("hlwjg_unit_type_name","工作单位类别"),
        EXPERTISE("hlwjg_expertise","技能专长"),
        LAWTYPENAME("hlwjg_law_type_name","执法类别"),
        POLITICALSTATUS("hlwjg_political_status","政治面貌"),
        SUPERVISONSTAFF("hlwjg_supervison_staff","是否监督人员"),
        QUALIFICATIONLEGALPRO("hlwjg_qualification_legal_pro","是否具有法律职业资格"),
        LAWSTAFFTYPE("hlwjg_law_staff_type","执法人员性质"),
        LAWMAIN("hlwjg_law_main","执法主体"),
        DEPTNAME("hlwjg_dept_name","发布单位"),
        EXAMQUESTIONTYPE("law_exam_question_type","题型"),
        QUALIFIEDSCORE("law_qualified_score","及格分数");
        LawDict(String code, String text){
            DicCodePool.putCodeItem(this, code, text);
        }



    }


    public enum LawRoot implements IDicTable {
        LAWROOT("hlwjg_dict","互联网+监管字典项");
        LawRoot(String code, String text){
            DicCodePool.putCodeItem(this, code, text);
        }

    }

    /**
     * 执法监督系统字典项表,固定的字典编码
     */
    public enum OtherDict implements IDicTable {
        NATIONALITY("d_nationality","民族"),
        EDULEAVE("s_eduLeave","最高学历");
        OtherDict(String code, String text){
            DicCodePool.putCodeItem(this, code, text);
        }
    }

    public enum OtherRoot implements IDicTable {
        DROOT("d_root","应用支撑平台");
        OtherRoot(String code, String text){
            DicCodePool.putCodeItem(this, code, text);
        }
    }



}
