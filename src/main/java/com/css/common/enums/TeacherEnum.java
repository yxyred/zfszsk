package com.css.common.enums;

public class TeacherEnum {

    /**
     * 性别
     */
    enum Gender implements IDicTable {
        MAN("1", "男"), WOMAN("2", "女");
        Gender(String code, String text) {
            DicCodePool.putCodeItem(this, code, text);
        }

    }

    /**
     * 状态
     */
    enum State implements IDicTable {
        WORK("10", "在职"), RESIGNED("20", "离职"), EXPELLED("30", "开除");
        State(String code, String text) {
            DicCodePool.putCodeItem(this, code, text);
        }

    }


    public static void main(String[] args) {
        DicCodeBean codeItem = DicCodePool.getCodeItem(Gender.MAN);
        System.out.println(codeItem);
    }


}
