package com.css.modules.sys.controller;

import com.css.common.enums.CommonEnums;
import com.css.common.enums.DicCodePool;
import com.css.common.utils.R;
import com.css.common.utils.REnumUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/sys/enum")
@Api(value = "/sys/enum",description = "字典项",tags = "字典项")
public class EnumController {
    @Autowired
    private REnumUtil rEnumUtil;

    @GetMapping("/all")
    @ApiOperation(value = "获取所有字典项", produces = "application/json")
    public R lawEnforcementOfficers(){
        JSONObject jsonObject = new JSONObject();

        List lawDictKeysList = new ArrayList();
        lawDictKeysList.add("hlwjg_gender");
        lawDictKeysList.add("hlwjg_unit_type_name");
        lawDictKeysList.add("hlwjg_expertise");
        lawDictKeysList.add("hlwjg_law_type_name");
        lawDictKeysList.add("hlwjg_political_status");
        lawDictKeysList.add("hlwjg_supervison_staff");
        lawDictKeysList.add("hlwjg_qualification_legal_pro");
        lawDictKeysList.add("hlwjg_law_staff_type");
        lawDictKeysList.add("hlwjg_law_main");
        lawDictKeysList.add("hlwjg_dept_name");
        List examQuestionDictKeysList = new ArrayList();
        examQuestionDictKeysList.add("law_exam_question_type");

        jsonObject = rEnumUtil.getCodeAndNameList(jsonObject,lawDictKeysList,
                DicCodePool.getCodeItem(CommonEnums.LawRoot.LAWROOT).getCode());
        jsonObject = rEnumUtil.getExamQuestionTypeWithoutSysFlagList(jsonObject,examQuestionDictKeysList,
                DicCodePool.getCodeItem(CommonEnums.LawRoot.LAWROOT).getCode());

        List otherKeysList = new ArrayList();
        otherKeysList.add("d_nationality");
        otherKeysList.add("s_eduLeave");

        jsonObject = rEnumUtil.getCodeAndNameList(jsonObject,otherKeysList,
                DicCodePool.getCodeItem(CommonEnums.OtherRoot.DROOT).getCode());

        Map resultJson = new Gson().fromJson(jsonObject.toString(), new TypeToken<Map<String,List<Map<Object,Object>>>>(){}.getType());
        return R.ok().put("data",resultJson);
    }




}
