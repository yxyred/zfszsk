package com.css.modules.exam.controller;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.css.common.utils.ExportExcelUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.swagger.annotations.*;
import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.css.modules.exam.entity.LawExamCorrectTEntity;
import com.css.modules.exam.service.LawExamCorrectTService;
import com.css.common.utils.PageUtils;
import com.css.common.utils.R;

import javax.servlet.http.HttpServletResponse;


/**
 * 考试批改表
 *
 * @author 
 * @email 
 * @date 2019-11-26 11:12:19
 */
@RestController
@RequestMapping("exam/lawexamcorrectt")
@Api(value = "exam/lawexamcorrectt", description = "考试批改表", tags = "考试批改表")
public class LawExamCorrectTController {
    @Autowired
    private LawExamCorrectTService lawExamCorrectTService;

    /**
     * 列表
     */
    @GetMapping("/list")
    @ApiOperation(value = "试卷信息列表", notes = "返回试卷信息列表",
            produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", dataType = "int", paramType = "query", required = true),
            @ApiImplicitParam(name = "limit", value = "每页条数", dataType = "int", paramType = "query", required = true),
            @ApiImplicitParam(name = "sidx", value = "排序字段", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "order", value = "排序方式，如：asc、desc", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "testName", value = "试卷名称", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "examTime", value = "考试日期", dataType = "date", paramType = "query"),
    })
    public R list(@ApiParam(name="params" ,hidden = true) @RequestParam Map<String, Object> params){
        PageUtils page = lawExamCorrectTService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
     * 案例解析题部分
     * 传入试卷批改ID
     */
    @GetMapping("/info/{id}")
    @ApiOperation(value = "案例解析题部分", produces = "application/json")
    public R info(@PathVariable("id") String id){
        Map result = lawExamCorrectTService.getSynInfo(id);
        return R.ok().put("data", result);
    }

    /**
     * 人工批改提交
     */
    @GetMapping("/synSave")
    @ApiOperation(value = "人工批改提交", notes = "返回试卷信息列表",
            produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "考试批改ID", dataType = "int", paramType = "query", required = true),
            @ApiImplicitParam(name = "score", value = "分数", dataType = "int", paramType = "query", required = true),
    })
    public R synSave(@ApiParam(name="params" ,hidden = true) @RequestParam Map<String, Object> params){
        String message = lawExamCorrectTService.synSave(params);
        return R.ok().put("msg",message);
    }

    /**
     * 批量同步信息
     */
    @PostMapping("/sysncInfoBatch")
    @ApiOperation(value = "批量同步信息", produces = "application/json")
    @ApiImplicitParam(name = "mapIds",value = "考试批改ID",paramType = "body",dataType = "body")
    public R sysncInfoBatch(@RequestBody Map<String, Object> mapIds) {
        String meaasge = lawExamCorrectTService.sysncInfoBatch(mapIds);
        return R.ok().put("msg",meaasge);
    }

    /**
     * 全部导出
     */
    @GetMapping(value = "/downloadAll")
    @ApiOperation(value = "全部导出", produces = "application/octet-stream")
    public void downloadAll(HttpServletResponse response) throws Exception {
        List<LawExamCorrectTEntity> list = lawExamCorrectTService.list(new QueryWrapper<LawExamCorrectTEntity>()
                .ne("DEL_FLAG", "1"));
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setId(Integer.toString(i + 1));
        }
        ExportExcelUtil.exportExcel(list, LawExamCorrectTEntity.class, "考试批改.xls", response);
    }

    /**
     * 批量导出
     */
    @PostMapping("/downloadBatch")
    @ApiOperation(value = "批量导出", produces = "application/octet-stream")
    @ApiImplicitParam(name = "mapIds",value = "ids",paramType = "body",dataType = "body")
    public void downloadBatch(HttpServletResponse response, @RequestBody Map<String, Object> mapIds) throws Exception {
//      直接获取map中存的数组map={"ids":[1,2,3]}
        List<Object> vlist = (List<Object>) mapIds.get("ids");
//      list 转化为数组
        Object[] ids = vlist.toArray(new Object[vlist.size()]);
//      Object... values代表Object[]
        List<LawExamCorrectTEntity> list = lawExamCorrectTService.list(new QueryWrapper<LawExamCorrectTEntity>()
                .in(!mapIds.isEmpty(), "id", ids)
                .ne("DEL_FLAG", "1"));
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setId(Integer.toString(i + 1));
        }
        ExportExcelUtil.exportExcel(list, LawExamCorrectTEntity.class, "考试批改.xls", response);
    }

    /**
     * 统计信息
     */
    @GetMapping("/statistics")
    @ApiOperation(value = "统计信息",
            produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "testId", value = "试卷ID", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "yearStart", value = "开始年份", dataType = "int", paramType = "query",required = true),
            @ApiImplicitParam(name = "yearEnd", value = "结束年份", dataType = "int", paramType = "query",required = true)
    })
    public R statistics(String testId ,int yearStart,int yearEnd){
        JSONObject result = new JSONObject();
        //数据库所有数据
        if(StringUtils.isNotBlank(testId)){
            List<LawExamCorrectTEntity> testNamelist = lawExamCorrectTService.list(new QueryWrapper<LawExamCorrectTEntity>()
                    .eq("TEST_ID",testId)
                    .ne("DEL_FLAG", "1"));
            //按试卷名称 考试结果统计
            List<JSONObject> jsonObjectTestNameList = lawExamCorrectTService.getTestNameStrStatistics(testNamelist,yearStart,yearEnd);
            result.put("testName",jsonObjectTestNameList);
        }

        List<LawExamCorrectTEntity> examTimelist = lawExamCorrectTService.list(new QueryWrapper<LawExamCorrectTEntity>()
                .ne("DEL_FLAG", "1"));

        //将考试时间 格式化到年 放入 examTimeStr
        List<LawExamCorrectTEntity> examTimeProcessedList = lawExamCorrectTService.getProcessedData(examTimelist);

        //历年考试结果统计
        List<JSONObject> jsonObjectExamTimeList = lawExamCorrectTService.getExamTimeStrStatistics(examTimeProcessedList,yearStart,yearEnd);
        result.put("examTime",jsonObjectExamTimeList);

        //将数据格式化json
        Map resultJson = new Gson().fromJson(result.toString(), new TypeToken<Map<Object,List<Map<Object,Object>>>>(){}.getType());

        return R.ok().put("data",resultJson);
    }
    
}
