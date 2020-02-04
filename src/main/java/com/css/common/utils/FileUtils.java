package com.css.common.utils;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.fileupload.util.Streams;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slw.sso.client.BaseUCConstant;
import org.slw.sso.user.SsoUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.URLEncoder;
import java.util.*;
@Component
public class FileUtils {
    private static final Logger logger = LoggerFactory.getLogger(FileUtils.class);
    @Autowired
    private SsoUrlUtils ssoUrlUtils;

    //在文件操作中，不用/或者\，推荐使用File.separator
    /**
     * 根路径
     */
    private String rootPath;
    /**
     *     文件存储路径
     */
    private String fileRootPath;
    /**
     *     模板文件存储路径
     */
    private String docRootPath;
    /**
     * 根路径
     */
    public  String getRootPath() {
        String osname = System.getProperty("os.name").toLowerCase();
        if (osname.indexOf("windows") >= 0) {
            this.rootPath = this.getClass().getResource("/").getFile().substring(1);
        } else {
            this.rootPath = this.getClass().getResource("/").getPath();
        }
        return rootPath;

    }
    /**
     *     文件存储路径
     */
    public  String getFileRootPath() {
        String fileDir="files";
        String osname = System.getProperty("os.name").toLowerCase();
        if (osname.indexOf("windows") >= 0) {
            this.fileRootPath = this.getClass().getResource("/").getFile().substring(1) + File.separator + fileDir + File.separator;
        } else {
            this.fileRootPath = this.getClass().getResource("/").getPath() + File.separator + fileDir + File.separator;
        }
        return fileRootPath;
    }
    /**
     *     文件存储路径
     */
    public  String getDocRootPath() {
        String fileDir="doc";
        String osname = System.getProperty("os.name").toLowerCase();
        if (osname.indexOf("windows") >= 0) {
            this.fileRootPath = this.getClass().getResource("/").getFile().substring(1) + File.separator + fileDir + File.separator;
        } else {
            this.fileRootPath = this.getClass().getResource("/").getPath() + File.separator + fileDir + File.separator;
        }
        return fileRootPath;
    }

    /**
     * 上传文件到默认文件路径 工程名\files\  <br/>
     *
     * 以文件的原名进行存储
     * @param multipartFiles
     * @return
     */
    public String uploadFile(MultipartFile[] multipartFiles){
        File fileDir = new File(this.getFileRootPath());
        if (!fileDir.exists() && !fileDir.isDirectory()) {
            fileDir.mkdirs();
        }
        try {
            if (multipartFiles != null && multipartFiles.length > 0) {
                for(int i = 0;i<multipartFiles.length;i++){
                    try {
                        //以原来的名称命名,覆盖掉旧的
                        String storagePath = this.getFileRootPath()+multipartFiles[i].getOriginalFilename();
                        logger.info("上传的文件：" + multipartFiles[i].getName() + "," + multipartFiles[i].getContentType() + "," + multipartFiles[i].getOriginalFilename()
                                +"，保存的路径为：" + storagePath);
                        Streams.copy(multipartFiles[i].getInputStream(), new FileOutputStream(storagePath), true);
                        //或者下面的
                        // Path path = Paths.get(storagePath);
                        //Files.write(path,multipartFiles[i].getBytes());
                    } catch (IOException e) {
                        logger.error(ExceptionUtils.getFullStackTrace(e));
                    }
                }
            }

        } catch (Exception e) {
            return e.getMessage();
        }
        return "上传成功!";
    }

    /**
     * 上传文件到默认文件路径 工程名\files\ <br/>
     *
     * 以fileName进行存储，fileName为""，以文件的原名存储
     * @param multipartFiles
     * @param fileName 文件名 带后缀
     * @return
     */
    public  String uploadFile(MultipartFile multipartFiles,String fileName){
        File fileDir = new File(this.getFileRootPath());
        if (!fileDir.exists() && !fileDir.isDirectory()) {
            fileDir.mkdirs();
        }
        try {
            if (multipartFiles != null ) {
                try {
                    //以原来的名称命名,覆盖掉旧的
                    String storagePath;
                    if(StringUtils.isNotEmpty(fileName)){
                        storagePath = this.getFileRootPath()+fileName;
                    }else{
                        storagePath = this.getFileRootPath()+multipartFiles.getOriginalFilename();
                    }
                    logger.info("上传的文件：" + multipartFiles.getName() + "," + multipartFiles.getContentType() + "," + multipartFiles.getOriginalFilename()
                            +"，保存的路径为：" + storagePath);
                    Streams.copy(multipartFiles.getInputStream(), new FileOutputStream(storagePath), true);
                    //或者下面的
                    // Path path = Paths.get(storagePath);
                    //Files.write(path,multipartFiles.getBytes());

//                    multipartFiles.transferTo(new File(storagePath));
//
//                    String result = httpClientUploadFile(fileName);
//
//                    JSONObject jsonObject = JSONObject.parseObject(result);
//                    Map<String,String> fileMap = Maps.newHashMap();
//                    if (null != jsonObject && "上传成功".equals(jsonObject.get("message"))) {
//                        fileMap.put("file_url", jsonObject.get("url").toString());
//                        fileMap.put("fileName", fileName);
//                        fileMap.put("fileType", multipartFiles.getContentType());
//                        logger.info(fileMap.toString());
//                        deleteFile(fileName);
//                    }else{
//                        deleteFile(fileName);
//                    }
                } catch (IOException e) {
                    logger.error(ExceptionUtils.getFullStackTrace(e));
                }
            }

        } catch (Exception e) {
            logger.error(ExceptionUtils.getFullStackTrace(e));
            return e.getMessage();
        }
        return "上传成功!";
    }

    /**
     *  默认文件路径下载文件
     * @param fileName 文件名 带后缀
     * @param response
     * @return
     */
    public  void download(String fileName, final HttpServletResponse response){
        OutputStream os = null;
        InputStream is= null;
        try {
            // 取得输出流
            os = response.getOutputStream();
            // 清空输出流
            response.reset();
            response.setCharacterEncoding("UTF-8");
            response.setHeader("content-Type", "application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            //读取流
            is = new FileInputStream(this.getFileRootPath() +fileName);
            if (is == null) {
                logger.error("下载附件失败，请检查文件“" + fileName + "”是否存在");
            }
            //复制
            IOUtils.copy(is, response.getOutputStream());
            response.getOutputStream().flush();
        } catch (IOException e) {
            logger.error("下载附件失败,error:"+e.getMessage());
        }
        //文件的关闭放在finally中
        finally
        {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                logger.error(ExceptionUtils.getFullStackTrace(e));
            }
            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                logger.error(ExceptionUtils.getFullStackTrace(e));
            }
        }
    }

    /**
     *
     * 将文件上传到文件服务器
     * @param fileName 文件名 带后缀
     * @throws Exception
     */
    public  String httpClientUploadFile(String fileName) throws Exception {
        // 上传文件 POST 同步方法

        Map<String, String> uploadParams = new LinkedHashMap<String, String>();

        uploadParams.put("userId", "none");
        uploadParams.put("moduleName", "ZFJDXT");
        uploadParams.put("fileSrcName", URLEncoder.encode(fileName, "UTF-8"));

        String token = URLEncoder.encode(
                "AQIC5wM2LY4Sfcx0FSNeUzw0Jnmyh9-Y5XP_l4_U0KGGPns.*AAJTSQACMDEAAlNLABMyMDI5NjExOTUxODM2MjU2MTY0*",
                "UTF-8");
        uploadParams.put("token", token);

        String path = new File(this.getFileRootPath() + fileName).getAbsolutePath();
        String url = ssoUrlUtils.getUploadDfsBackEndUrl();

        String result = HttpClientUtil.getInstance().uploadFileImpl(url,
                path , "file", uploadParams);



        return result;
    }


    /**
     * 从文件服务器下载文件
     *
     * @param md5Path 文件的md5
     * @param fileName 文件的名字
     * @param response
     * @throws Exception
     */
    public void httpClientDownloadFile(String md5Path,String fileName, HttpServletResponse response) throws Exception {
        if(StringUtils.isNotBlank(md5Path)){
            String url = ssoUrlUtils.getDownloadUrl() + md5Path;

            HttpClientUtil.getInstance().httpDownloadFile(url,fileName,response,null);
        }

    }


    /**
     * 下载文件
     * @param fileName 文件名 带后缀
     * @param response
     * @param request
     * @return
     */
    public  void download(String path,String fileName, final HttpServletResponse response, final HttpServletRequest request){
        OutputStream os = null;
        InputStream is= null;
        try {
            // 取得输出流
            os = response.getOutputStream();
            // 清空输出流
            response.reset();
            response.setCharacterEncoding("UTF-8");
            response.setHeader("content-Type", "application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));

            //读取流
            File f;
            if (StringUtils.isNotBlank(path) && StringUtils.isNotBlank(fileName)) {
                f = new File(path +fileName);
            }else{
                logger.error("文件名不能为空！");
            }

            is = new FileInputStream(path +fileName);
            if (is == null) {
                logger.error("下载附件失败，请检查文件“" + fileName + "”是否存在");
            }
            //复制
            IOUtils.copy(is, response.getOutputStream());
            response.getOutputStream().flush();
        } catch (IOException e) {
            logger.error("下载附件失败,error:"+e.getMessage());
        }
        //文件的关闭放在finally中
        finally
        {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                logger.error(ExceptionUtils.getFullStackTrace(e));
            }
            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                logger.error(ExceptionUtils.getFullStackTrace(e));
            }
        }
    }

    /**
     * 删除文件
     * @param fileName 文件名 带后缀
     * @return
     */
    public boolean deleteFile(String fileName){
        boolean result = false;

        File file = new File(this.getFileRootPath() + fileName);
        if(file.exists()){
            file.delete();
            result = true;
            logger.info("删除文件" + this.getFileRootPath() + fileName);
        }
        return result;
    }



//    public static void download(String path, HttpServletResponse response) {
//        try {
//            if(StringUtils.isNotBlank(path)){
//                File file = new File(path);
//                // 取得文件名。
//                String fileName = file.getName();
//                // 以流的形式下载文件。
//                InputStream fis = new BufferedInputStream(new FileInputStream(path));
//                byte[] buffer = new byte[fis.available()];
//                fis.read(buffer);
//                fis.close();
//                // 清空response
//                response.reset();
//                String uncod= URLDecoder.decode(fileName,"UTF-8");
//                fileName = new String(uncod.getBytes("UTF-8"), "iso-8859-1");
//                response.setHeader("Content-Disposition", "attachment;filename=".concat(String.valueOf(fileName)));
//                // 设置response的Header
//                response.addHeader("Content-Length", "" + file.length());
//                OutputStream toClient = new BufferedOutputStream(
//                                    response.getOutputStream());
//                toClient.write(buffer);
//                toClient.flush();
//                toClient.close();
//            }
//              // path是指欲下载的文件的路径。
//        } catch (IOException ex) {
//          ex.printStackTrace();
//        }
//  }




}
