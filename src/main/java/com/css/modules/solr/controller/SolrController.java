package com.css.modules.solr.controller;


import com.css.modules.solr.service.SolrService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

/**
 * @author zhoudong
 * @version 0.1
 * @date 2018/12/01 15:35
 */
@RestController
@Api(tags = "solr" ,description = "solr")
@RequestMapping("solr")
public class SolrController {

    private final String db_core = "test_core";
    private final String file_core = "file_core";
    private final String fileDataImport = file_core + "/dataimport?command=full-import&clean=false&commit=true&entity=file";

    @Value("${spring.data.solr.host}")
    private String solrHost;
    @Autowired
    private SolrClient client;
    @Autowired
    private SolrService solrService;

    /**
     * 新增/修改 索引
     * 当 id 存在的时候, 此方法是修改(当然, 我这里用的 uuid, 不会存在的), 如果 id 不存在, 则是新增
     * @return
     */
    @ApiOperation("添加")
    @PostMapping("/add")
    public String add(@RequestParam String key, @RequestParam String content) {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        try {
            SolrInputDocument doc = new SolrInputDocument();
            doc.setField("id", uuid);
            doc.setField(key, content);
            /* 如果spring.data.solr.host 里面配置到 core了, 那么这里就不需要传 collection1 这个参数
             * 下面都是一样的
             */
            client.add(db_core, doc);
            //client.commit();
            client.commit(db_core);
            return uuid;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "error";
    }

    /**
     * 根据id删除索引
     * @param id
     * @return
     */
    @ApiOperation(value = "通过id删除", produces = "application/json")
    @PostMapping("/delete")
    public String delete(String id)  {
        try {
            client.deleteById(db_core,id);
            client.commit(db_core);

            return id;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "error";
    }

    /**
     * 删除所有的索引
     * @return
     */
    @ApiOperation(value = "全部删除", produces = "application/json")
    @GetMapping("/deleteAll")
    public String deleteAll(){
        try {
            solrService.deleteAll(db_core);
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "error";
    }


    /**
     * 根据id查询索引
     * @return
     * @throws Exception
     */
    @PostMapping("/getById/{id}")
    @ApiOperation(value = "查询getById", produces = "application/json")
    public String getById(@PathVariable String id) throws Exception {
        SolrDocument document = solrService.getById(db_core,id);
        System.out.println(document);
        return document.toString();
    }

    /**
     * 综合查询: 在综合查询中, 有按条件查询, 条件过滤, 排序, 分页, 高亮显示, 获取部分域信息
     * @return
     */
    @PostMapping("/search")
    @ApiOperation(value = "综合查询", produces = "application/json")
    public SolrDocumentList search(@RequestBody Map<String, Object> mapIds){
        SolrDocumentList results = null;
        try {
            results = solrService.querySolr(db_core,mapIds.get("q").toString(),"keyword");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SolrServerException e) {
            e.printStackTrace();
        }
        return results;
    }

    /**
     * 文件创建索引
     */
    @PostMapping("/doc/dataimport")
    @ApiOperation(value = "文件创建索引", produces = "application/json")
    public String dataimport(){
        // 1、清空索引，生产环境不建议这样使用，可以保存文件新建及更新时间，增量创建索引
        try {
            solrService.deleteAll(file_core);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 2、创建索引
        /*Mono<String> result = WebClient.create(solrHost)
                .get()
                .uri(fileDataImport)
                .retrieve()
                .bodyToMono(String.class);*/
        //System.out.println(result.block());
        return "success";
    }

    /**
     * 综合查询: 在综合查询中, 有按条件查询, 条件过滤, 排序, 分页, 高亮显示, 获取部分域信息
     * @return
     */
    @PostMapping("/doc/search")
    @ApiOperation(value = "综合查询doc", produces = "application/json")
    public SolrDocumentList docSearch(@RequestBody Map<String, Object> mapIds){
        SolrDocumentList results = null;
        try {
            results = solrService.querySolr(file_core,mapIds.get("q").toString(),"keyword");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SolrServerException e) {
            e.printStackTrace();
        }
        return results;
    }
}
