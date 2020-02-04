package com.css.modules.expert.controller;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.css.common.utils.FileUtils;
import com.css.common.utils.ExportExcelUtil;
import com.css.common.utils.PageUtils;
import com.css.common.utils.R;
import com.css.common.validator.ValidatorUtils;
import com.css.common.validator.group.AddGroup;
import com.css.common.validator.group.UpdateGroup;
import com.css.modules.expert.entity.BumExpertAndExperienceTEntity;
import com.css.modules.expert.entity.BumExpertTDeleteEntity;
import com.css.modules.expert.entity.BumExpertTExcelEntity;
import com.css.modules.expert.entity.BumExpertTEntity;
import com.css.modules.expert.service.BumExpertTService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.swagger.annotations.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 专家库
 *
 * @author liukai
 * @date 2019-11-12 17:17:24
 */
@RestController
@RequestMapping("/expert")
@Api(value = "/expert", description = "专家", tags = "专家")
public class BumExpertTController {
    @Autowired
    private BumExpertTService bumExpertTService;
    @Autowired
    FileUtils fileUtils;
    /**
     * 列表
     */
    @ApiOperation(value = "专家列表", produces = "application/json")
    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = bumExpertTService.queryPage(params);
        return R.ok().put("page", page);
    }

    @ApiOperation(value = "随机抽取专家", produces = "application/json")
    @GetMapping("/randomExtraction")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "nums", value = "抽取人数", dataType = "String", paramType = "query", required = true),
            /*@ApiImplicitParam(name = "expertise", value = "专家技术专长", dataType = "list", paramType = "query"),
            @ApiImplicitParam(name = "experienceType", value = "辅法类别", dataType = "list", paramType = "query"),*/
            @ApiImplicitParam(name = "province", value = "省", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "city", value = "市", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "page", value = "页码", dataType = "int", paramType = "query", required = true),
            @ApiImplicitParam(name = "limit", value = "每页条数", dataType = "int", paramType = "query", required = true),
            @ApiImplicitParam(name = "sidx", value = "排序字段", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "order", value = "排序方式，如：asc、desc", dataType = "string", paramType = "query")
    })
    public R randomExtraction(@ApiParam(name="params" ,hidden = true) @RequestParam Map<String, Object> params){
        PageUtils page = bumExpertTService.randomExtraction(params);
        return R.ok().put("page", page);
    }

    /**
     * 信息
     */
    @ApiOperation(value = "专家信息", produces = "application/json")
    @GetMapping("/info/{id}")
    public R info(@PathVariable("id") String id){
        BumExpertAndExperienceTEntity bumExpertAndExperienceTEntity = bumExpertTService.selectById(id);
        return R.ok().put("bumExpertAndExperienceTEntity", bumExpertAndExperienceTEntity);
    }

    /**
     * 保存
     */
    @ApiOperation(value = "保存专家信息", produces = "application/json")
    @PostMapping("/save")
    public R save(@RequestBody BumExpertAndExperienceTEntity bumExpertAndExperienceTEntity){
        ValidatorUtils.validateEntity(bumExpertAndExperienceTEntity, AddGroup.class);
		bumExpertTService.saveExpert(bumExpertAndExperienceTEntity);
        return R.ok();
    }

    /**
     * 修改
     */
    @ApiOperation(value = "通过id修改专家信息", produces = "application/json")
    @PostMapping("/update")
    public R update(@RequestBody BumExpertAndExperienceTEntity bumExpertAndExperienceTEntity){
        ValidatorUtils.validateEntity(bumExpertAndExperienceTEntity, UpdateGroup.class);
		bumExpertTService.updateById(bumExpertAndExperienceTEntity);
        return R.ok();
    }

    /**
     * 删除
     */
    @ApiOperation(value = "通过id删除专家信息", produces = "application/json")
    @PostMapping("/delete")
    public R delete(@RequestBody BumExpertTDeleteEntity ids){
		bumExpertTService.removeByIds(ids.getIds());
        return R.ok();
    }

    /**
     * 下载模板
     */
    @GetMapping(value = "/downloadDemo")
    @ApiOperation(value = "下载模板", produces = "application/octet-stream")
    public void downloadDemo(HttpServletResponse response, HttpServletRequest request) {
        String path = fileUtils.getDocRootPath();
        fileUtils.download(path, "专家名录库导入模板.xls", response, request);
    }

    /**
     * 导入excel
     */
    @PostMapping("/import")
    @ApiOperation(value = "导入excel", produces = "application/json")
    public R importExcel(@ApiParam(value = "文件",required = true) @RequestParam("file") MultipartFile file) {
        bumExpertTService.importBumExpertT(file);
        return new R().put("message", "导入成功");
    }

    /**
     * 批量导出
     */
    @PostMapping("/downloadBatch")
    @ApiOperation(value = "批量导出", produces = "application/octet-stream")
    @ApiImplicitParam(name = "mapIds",value = "ids",paramType = "body",dataType = "body")
    public void downloadBatch(HttpServletResponse response, @RequestBody Map<String, Object> mapIds) throws Exception {
        List<BumExpertTExcelEntity> bumExpertTExcelEntities = bumExpertTService.exportByIds(mapIds);
        ExportExcelUtil.exportExcel(bumExpertTExcelEntities, BumExpertTExcelEntity.class, "专家名录库.xls", response);
    }
    /**
     * 统计信息
     */
    @GetMapping("/statistics")
    @ApiOperation(value = "统计信息",
            produces = "application/json")

    public R statistics(){
        //数据库所有数据
        List<BumExpertTEntity> list = bumExpertTService.list(new QueryWrapper<BumExpertTEntity>()
                .ne("DEL_FLAG", "1"));

        List<BumExpertTEntity> processedList = bumExpertTService.getProcessedData(list);
        //按执法类别分组的数据
        List<JSONObject> jsonObjectExpertiseList = bumExpertTService.getExpertiseStatistics(processedList);
        List<JSONObject> jsonObjectUnitTypeNameList = bumExpertTService.getUnitTypeNameStatistics(processedList);
        List<JSONObject> jsonObjectProvinceList = bumExpertTService.getProvinceStatistics(processedList);

        JSONObject result = new JSONObject();
        result.put("expertise",jsonObjectExpertiseList);
        result.put("unitTypeName",jsonObjectUnitTypeNameList);
        result.put("province",jsonObjectProvinceList);

        //将数据格式化json
        Map resultJson = new Gson().fromJson(result.toString(), new TypeToken<Map<String,List<Map<String,String>>>>(){}.getType());
        return R.ok().put("data",resultJson);

    }





}
