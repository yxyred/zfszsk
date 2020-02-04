package com.css.modules.message.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.css.common.utils.PageUtils;
import com.css.common.utils.R;
import com.css.modules.message.entity.MessageCenterTEntity;
import com.css.modules.message.service.MessageCenterTService;
import com.css.modules.sys.controller.AbstractController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * 消息中心表
 *
 * @author liukai
 * @date 2019-12-13 15:14:50
 */
@RestController
@RequestMapping("/messagecentert")
@Api(tags = "messageCenter" ,description = "消息中心")
public class MessageCenterTController extends AbstractController {
    @Autowired
    private MessageCenterTService messageCenterTService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = messageCenterTService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
     * 获取当前登录人的消息列表
     * @return
     */
    @ApiOperation("获取当前登录人的消息列表")
    @GetMapping("/list")
    public R list(){
        //List<MessageCenterTEntity> list = messageCenterTService.list(new QueryWrapper<MessageCenterTEntity>().eq("RECEIVER_ID", getUserId()));
        List<MessageCenterTEntity> list = messageCenterTService.list(new QueryWrapper<MessageCenterTEntity>().eq("RECEIVER_ID", "123"));
        return R.ok().put("list", list);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") String id){
		MessageCenterTEntity messageCenterT = messageCenterTService.getById(id);

        return R.ok().put("messageCenterT", messageCenterT);
    }


    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody MessageCenterTEntity messageCenterT){
		messageCenterTService.save(messageCenterT);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody MessageCenterTEntity messageCenterT){
		messageCenterTService.updateById(messageCenterT);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody String[] ids){
		messageCenterTService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
