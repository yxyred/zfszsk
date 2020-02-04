package com.css.modules.exam.controller;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.css.common.utils.FileUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.css.modules.exam.entity.LawExamNoticeTEntity;
import com.css.modules.exam.service.LawExamNoticeTService;
import com.css.common.utils.PageUtils;
import com.css.common.utils.R;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 考试须知
 *
 * @author 
 * @email 
 * @date 2019-11-26 11:12:19
 */
@RestController
@RequestMapping("exam/lawexamnoticet")
@Api(value = "exam/lawexamnoticet", description = "考试须知", tags = "考试须知")
public class LawExamNoticeTController {
    @Autowired
    private LawExamNoticeTService lawExamNoticeTService;
    @Autowired
    FileUtils fileUtils;
    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = lawExamNoticeTService.queryPage(params);
        return R.ok().put("page", page);
    }

    @GetMapping("/examContent")
    @ApiOperation(value = "考试须知内容", notes = "考试须知内容",
            produces = "application/json")
    public R getContent(){
        List<LawExamNoticeTEntity> lawExamNoticeTEntities = lawExamNoticeTService.list();
        String content = "";
        if(null != lawExamNoticeTEntities && lawExamNoticeTEntities.size() > 0){
            content = lawExamNoticeTEntities.get(0).getContent();
        }
        return R.ok().put("content", content);
    }

    /**
     * 已读
     * @return
     */
    @ApiOperation(value = "是否已读", notes = "是否已读",
            produces = "application/json")
    @GetMapping("/readNotice")
    public R readNotice(){
        lawExamNoticeTService.readNotice();
        return R.ok();
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    public R info(@PathVariable("id") String id){
		LawExamNoticeTEntity lawExamNoticeT = lawExamNoticeTService.getById(id);
        return R.ok().put("lawExamNoticeT", lawExamNoticeT);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public R save(@RequestBody LawExamNoticeTEntity lawExamNoticeT){
		lawExamNoticeTService.save(lawExamNoticeT);
        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    public R update(@RequestBody LawExamNoticeTEntity lawExamNoticeT){
		lawExamNoticeTService.updateById(lawExamNoticeT);
        return R.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    public R delete(@RequestBody String[] ids){
		lawExamNoticeTService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }

    @GetMapping(value = "/downloadPic")
    @ApiOperation(value = "下载图片考试须知", produces = "application/octet-stream")
    public void downloadDemo(HttpServletResponse response, HttpServletRequest request) {
        String fileDir = "pic";
        //获取文件路径
        String rootPath = fileUtils.getRootPath();
        String path = rootPath + fileDir + File.separator;

        fileUtils.download(path, "考试须知.html", response, request);
    }

}
