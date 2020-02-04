package com.css.common.utils;


import org.apache.commons.io.IOUtils;
import org.apache.http.*;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.*;

/**
 * http请求工具类
 *
 * @author zhuzhe
 * @date 2018/5/3 11:46
 */
public class HttpClientUtil{

    public interface HttpClientDownLoadProgress {
        public void onProgress(int progress);
    }
    /**
     * 最大重试次数
     */
    public static int maxRetryTimes = 5;

    //public static Logger log = LoggerFactory.getLogger(HttpClientUtil.class);

    public CloseableHttpClient getHttpClient() {
        return httpClient;
    }

    /**
     * 单例对象
     */
    private CloseableHttpClient httpClient;

    public static HttpClientUtil getInstance() {
        return HttpClientUtil;
    }

    /**
     * 单例
     */
    private static HttpClientUtil HttpClientUtil = new HttpClientUtil();

    private  HttpClientUtil(){
        ConnectionSocketFactory plainsf = PlainConnectionSocketFactory.getSocketFactory();
        SSLConnectionSocketFactory sslConnectionSocketFactory = null;
        try {
            final SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, new TrustStrategy() {
                @Override
                public boolean isTrusted(X509Certificate[] x509Certificates, String authType) throws CertificateException {
                    return true;
                }
            }).build();

            sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext, new String[]{"TLSv1", "TLSv1.1", "TLSv1.2"}, null, NoopHostnameVerifier.INSTANCE);
            final Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("http", plainsf)
                    .register("https", sslConnectionSocketFactory)
                    .build();
            RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(5000)
                    .setSocketTimeout(900000)
                    .setConnectTimeout(5000)
                    .build();
            final PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(registry);
            // 将最大连接数增加到200
            cm.setMaxTotal(3000);
            // 将每个路由基础的连接增加到20
            cm.setDefaultMaxPerRoute(1500);

            // 将目标主机的最大连接数增加到50
            HttpHost localhost = new HttpHost("http://csfile.szoa.sz.gov.cn", 443);
            cm.setMaxPerRoute(new HttpRoute(localhost), 2000);

            //请求重试处理
            HttpRequestRetryHandler httpRequestRetryHandler = new HttpRequestRetryHandler() {
                @Override
                public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
                    if (executionCount >= maxRetryTimes) {// 如果已经重试了5次，就放弃
                        return false;
                    }
                    if (exception instanceof NoHttpResponseException) {// 如果服务器丢掉了连接，那么就重试
                        return true;
                    }
                    if (exception instanceof SSLHandshakeException) {// 不要重试SSL握手异常
                        return false;
                    }
                    if (exception instanceof InterruptedIOException) {// 超时
                        return false;
                    }
                    if (exception instanceof UnknownHostException) {// 目标服务器不可达
                        return false;
                    }
                    if (exception instanceof ConnectTimeoutException) {// 连接被拒绝
                        return false;
                    }
                    if (exception instanceof SSLException) {// ssl握手异常
                        return false;
                    }

                    HttpClientContext clientContext = HttpClientContext.adapt(context);
                    HttpRequest request = clientContext.getRequest();
                    // 如果请求是幂等的，就再次尝试
                    if (!(request instanceof HttpEntityEnclosingRequest)) {
                        return true;
                    }
                    return false;
                }
            };
            httpClient = HttpClients.custom()
                    .setConnectionManager(cm)
                    .setRetryHandler(httpRequestRetryHandler).setDefaultRequestConfig(requestConfig)
                    .build();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
    }

    public static void config(HttpRequestBase httpRequestBase) {
        httpRequestBase.setHeader("User-Agent", "Mozilla/5.0");
        httpRequestBase.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        httpRequestBase.setHeader("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");//"en-US,en;q=0.5");
        httpRequestBase.setHeader("Accept-Charset", "ISO-8859-1,utf-8,gbk,gb2312;q=0.7,*;q=0.7");
        // 配置请求的超时设置
        RequestConfig requestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD_STRICT)
                .setExpectContinueEnabled(Boolean.TRUE).setTargetPreferredAuthSchemes(Arrays.asList(AuthSchemes.NTLM, AuthSchemes.DIGEST))
                .setProxyPreferredAuthSchemes(Arrays.asList(AuthSchemes.BASIC)).setConnectionRequestTimeout(5000)
                .setSocketTimeout(900000)
                .setConnectTimeout(5000)
                .build();
        httpRequestBase.setConfig(requestConfig);
    }

    public void httpDownloadFile(String url, String filePath,
                                        Map<String, String> headMap) {
        try {
            HttpGet request = new HttpGet(url);
            config(request);
            if(headMap!=null) {
                for (Map.Entry<String, String> e : headMap.entrySet()) {
                    request.addHeader(e.getKey(), e.getValue());
                }
            }
            CloseableHttpResponse response1 = httpClient.execute(request);
            try {
                System.out.println(response1.getStatusLine().toString());
                HttpEntity httpEntity = response1.getEntity();
                long contentLength = httpEntity.getContentLength();
                InputStream in = httpEntity.getContent();
                // 根据InputStream 下载文件
                FileOutputStream fos = new FileOutputStream(filePath);
                // 通过ioutil 对接输入输出流，实现文件下载
                IOUtils.copy(new BufferedInputStream(in), fos);
                fos.close();
                EntityUtils.consume(httpEntity);
            } finally {
                response1.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void httpDownloadFile(String url, String fileName, HttpServletResponse response,
                                 Map<String, String> headMap) {
        OutputStream os = null;
        InputStream in= null;
        try {
            HttpGet request = new HttpGet(url);
            config(request);
            if(headMap!=null) {
                for (Map.Entry<String, String> e : headMap.entrySet()) {
                    request.addHeader(e.getKey(), e.getValue());
                }
            }
            CloseableHttpResponse response1 = httpClient.execute(request);
            try {
                // 取得输出流
                os = response.getOutputStream();

                System.out.println(response1.getStatusLine().toString());
                HttpEntity httpEntity = response1.getEntity();
                long contentLength = httpEntity.getContentLength();
                System.out.println(contentLength+"contentLength");
                in = httpEntity.getContent();
                System.out.println(in.available()+"in.available()");

                response.setContentLength((int)contentLength);
                response.addHeader("Content-Length", contentLength+"");

                response.reset();
                response.setCharacterEncoding("UTF-8");
                response.setHeader("content-Type", "application/vnd.ms-excel");
                response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));

                IOUtils.copy(new BufferedInputStream(in), response.getOutputStream());
                EntityUtils.consume(httpEntity);
            } finally {
                response1.close();
                if (in != null) {
                    in.close();
                }
                if (os != null) {
                    os.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void httpDownloadFile(String url, String filePath,
                                        HttpClientDownLoadProgress progress, Map<String, String> headMap) {

        try {
            HttpGet request = new HttpGet(url);
            if(headMap!=null) {
                for (Map.Entry<String, String> e : headMap.entrySet()) {
                    request.addHeader(e.getKey(), e.getValue());
                }
            }
            CloseableHttpResponse response1 = httpClient.execute(request);
            try {
                System.out.println(response1.getStatusLine());
                HttpEntity httpEntity = response1.getEntity();

                long contentLength = httpEntity.getContentLength();
                InputStream is = httpEntity.getContent();
                // 根据InputStream 下载文件
                ByteArrayOutputStream output = new ByteArrayOutputStream();
                byte[] buffer = new byte[4096];
                int r = 0;
                long totalRead = 0;
                while ((r = is.read(buffer)) > 0) {
                    output.write(buffer, 0, r);
                    totalRead += r;
                    if (progress != null) {// 回调进度
                        progress.onProgress((int) (totalRead * 100 / contentLength));
                    }
                }
                FileOutputStream fos = new FileOutputStream(filePath);
                output.writeTo(fos);
                output.flush();
                output.close();
                fos.close();
                EntityUtils.consume(httpEntity);
            } finally {
                response1.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * http get请求
     *
     * @param url
     * @return
     */
    public String httpGet(String url, Map<String, String> headMap,Map<String, String> parameterMap) {
        long startTime=System.currentTimeMillis();
        String responseContent = null;
        try {
            if(!url.contains("?")){
                url+="?";
            }
            else{
                url+="&";
            }
            if(parameterMap!=null){
                for (Map.Entry<String, String> e : parameterMap.entrySet()) {
                    url+= e.getKey() + "=" + e.getValue() +"&";
                }
            }
            if(url.endsWith("&")){
                url=url.substring(0,url.length()-1);
            }
            HttpGet request = new HttpGet(url);
            if(headMap!=null) {
                for (Map.Entry<String, String> e : headMap.entrySet()) {
                    request.addHeader(e.getKey(), e.getValue());
                }
            }
            CloseableHttpResponse response1 = httpClient.execute(request);
            try {
                  System.out.println(response1.getStatusLine());
                HttpEntity entity = response1.getEntity();
                responseContent = EntityUtils.toString(entity, "UTF-8");
                System.out.println("debug:" + responseContent);
                EntityUtils.consume(entity);
            } finally {
                response1.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(System.currentTimeMillis()-startTime+"httpGet spendTime");
        return responseContent;
    }

    /**
     * 将返回结果转化为String
     *
     * @param entity
     * @return
     * @throws Exception
     */
    private String getRespString(HttpEntity entity) throws Exception {
        if (entity == null) {
            return null;
        }
        InputStream is = entity.getContent();
        StringBuffer strBuf = new StringBuffer();
        byte[] buffer = new byte[4096];
        int r = 0;
        while ((r = is.read(buffer)) > 0) {
            strBuf.append(new String(buffer, 0, r, "UTF-8"));
        }
        return strBuf.toString();
    }


    /**
     * http的post请求
     *
     * @param url
     * @param paramsMap
     * @return
     */
    public String httpPost(String url, Map<String, String> paramsMap,
                           Map<String, String> headMap) {
        String responseContent = null;
        Long startTime=System.currentTimeMillis();
        try {
            HttpPost httpPost = new HttpPost(url);
            if(headMap!=null) {
                for (Map.Entry<String, String> e : headMap.entrySet()) {
                    httpPost.addHeader(e.getKey(), e.getValue());
                }
            }
            if (paramsMap != null) {
                List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();

                for (String key : paramsMap.keySet()) {
                    nameValuePairList.add(new BasicNameValuePair(key, paramsMap.get(key)));
                }
                UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(nameValuePairList, "utf-8");
                formEntity.setContentType("application/x-www-form-urlencoded; charset=UTF-8");
                httpPost.setEntity(formEntity);
               // request.setEntity(new ByteArrayEntity(body));
               // request.setEntity(new StringEntity(body, "utf-8"));
            }

            CloseableHttpResponse response = httpClient.execute(httpPost);
            try {
                //System.out.println(response.getStatusLine()+"statusLine");
                HttpEntity entity = response.getEntity();
                responseContent = getRespString(entity);
                EntityUtils.consume(entity);
            } finally {
                response.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }       
        System.out.println(System.currentTimeMillis()-startTime+"httpPost spendTime");
        // System.out.println("responseContent = " + responseContent);
        return responseContent;
    }

    /**
     * 上传文件
     *
     * @param serverUrl       服务器地址
     * @param localFilePath   本地文件路径
     * @param serverFieldName
     * @param params
     * @return
     * @throws Exception
     */
    public String uploadFileImpl(String serverUrl, String localFilePath,
                                 String serverFieldName, Map<String, String> params)
            throws Exception {
        String respStr = null;
        CloseableHttpResponse response=null;
        try {
            HttpPost httppost = new HttpPost(serverUrl);
            FileBody binFileBody = new FileBody(new File(localFilePath),ContentType.APPLICATION_OCTET_STREAM);

            MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder
                    .create().setMode(HttpMultipartMode.RFC6532);
            multipartEntityBuilder.setCharset(Consts.UTF_8);
            // add the file params
            multipartEntityBuilder.addPart(serverFieldName, binFileBody);
            // 设置上传的其他参数
            setUploadParams(multipartEntityBuilder, params);

            HttpEntity reqEntity = multipartEntityBuilder.build();
            httppost.setEntity(reqEntity);

            response = httpClient.execute(httppost);

           // System.out.println(response.getStatusLine() + "statusLine");
            System.out.println(Arrays.asList(response.getAllHeaders()) + "allHeader");
            HttpEntity resEntity = response.getEntity();
            respStr = EntityUtils.toString(resEntity, "UTF-8");
            EntityUtils.consume(resEntity);
        }finally {
            if(response!=null){
                response.close();
            }
        }

        return respStr;
    }

    /**
     * 设置上传文件时所附带的其他参数
     *
     * @param multipartEntityBuilder
     * @param params
     */
    private void setUploadParams(MultipartEntityBuilder multipartEntityBuilder,
                                 Map<String, String> params) {
        if (params != null && params.size() > 0) {
            Set<String> keys = params.keySet();
            for (String key : keys) {
                multipartEntityBuilder
                        .addPart(key, new StringBody(params.get(key),
                                ContentType.create("text/plain", Consts.UTF_8)));
            }
        }
    }

   // protected static ObjectMapper mapper = new ObjectMapper();

    /**
     * 将结果转换成JSONObject
     *
     * @param httpResponse
     * @return
     * @throws IOException
     */
//    public static Map<String, Object> getJson(HttpResponse httpResponse) throws IOException {
//        HttpEntity entity = httpResponse.getEntity();
//        String resp = EntityUtils.toString(entity, "UTF-8");
//        EntityUtils.consume(entity);
//        return mapper.readValue(resp, Map.class);
//    }


}