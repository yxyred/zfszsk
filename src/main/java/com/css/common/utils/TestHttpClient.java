package com.css.common.utils;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Base64;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.util.concurrent.Executors.*;

public class TestHttpClient {

	String[] urisToGet = { "http://blog.csdn.net/gaolu/article/details/48466059",
			"http://blog.csdn.net/gaolu/article/details/48243103",
			"http://blog.csdn.net/gaolu/article/details/47656987",
			"http://blog.csdn.net/gaolu/article/details/47055029",

			"http://blog.csdn.net/gaolu/article/details/46400883",
			"http://blog.csdn.net/gaolu/article/details/46359127",
			"http://blog.csdn.net/gaolu/article/details/46224821",
			"http://blog.csdn.net/gaolu/article/details/45305769",

			"http://blog.csdn.net/gaolu/article/details/43701763",
			"http://blog.csdn.net/gaolu/article/details/43195449",
			"http://blog.csdn.net/gaolu/article/details/42915521",
			"http://blog.csdn.net/gaolu/article/details/41802319",

			"http://blog.csdn.net/gaolu/article/details/41045233",
			"http://blog.csdn.net/gaolu/article/details/40395425",
			"http://blog.csdn.net/gaolu/article/details/40047065",
			"http://blog.csdn.net/gaolu/article/details/39891877",

			"http://blog.csdn.net/gaolu/article/details/39499073",
			"http://blog.csdn.net/gaolu/article/details/39314327",
			"http://blog.csdn.net/gaolu/article/details/38820809",
			"http://blog.csdn.net/gaolu/article/details/38439375", };

	static class GetRunnable implements Runnable {
		private CountDownLatch countDownLatch;
		private final CloseableHttpClient httpClient;
		private final HttpGet httpget;

		public GetRunnable(CloseableHttpClient httpClient, HttpGet httpget, CountDownLatch countDownLatch) {
			this.httpClient = httpClient;
			this.httpget = httpget;
			this.countDownLatch = countDownLatch;
		}

		@Override
		public void run() {
			CloseableHttpResponse response = null;
			try {
				response = httpClient.execute(httpget, HttpClientContext.create());
				HttpEntity entity = response.getEntity();
				System.out.println(EntityUtils.toString(entity, "utf-8"));
				EntityUtils.consume(entity);
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				countDownLatch.countDown();
				try {
					if (response != null){
						response.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	// 多线程get请求测试
	@Test
	public void testHttpClientGet() throws Exception {

		long start = System.currentTimeMillis();
		try {
			int pagecount = urisToGet.length;
			ExecutorService executors = newFixedThreadPool(pagecount);
			CountDownLatch countDownLatch = new CountDownLatch(pagecount);
			for (int i = 0; i < pagecount; i++) {
				HttpGet httpget = new HttpGet(urisToGet[i]);
				HttpClientUtil.config(httpget);
				// 启动线程抓取
				executors.execute(new GetRunnable(HttpClientUtil.getInstance().getHttpClient(), httpget,
						countDownLatch));
			}
			countDownLatch.await();
			executors.shutdown();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			System.out.println(
					"线程" + Thread.currentThread().getName() + "," + System.currentTimeMillis() + ", 所有线程已完成，开始进入下一步！");
		}
		long end = System.currentTimeMillis();
		System.out.println("consume -> " + (end - start));
	}

	// get请求触发套红接口
	@Test
	public void testHttpClientGetMergeRedFile() throws Exception {

		long start = System.currentTimeMillis();
		String result = HttpClientUtil.getInstance().httpGet(
				"https://csfile.szoa.sz.gov.cn/file/mergeRedPdf?md5Path=f533dbd9d67794ea79108593d609a9f9@58458&mergeRedPdfPath=fdcaff227e583e6d1e033e3a872a5b00@7092",
				null, null);
		System.out.println(result);
		long end = System.currentTimeMillis();
		System.out.println("consume -> " + (end - start));
	}

	// post请求测试
	@Test
	public void testHttpClientHttpPost() throws Exception {

		long start = System.currentTimeMillis();
		// POST 同步方法
		Map<String, String> params = new HashMap<String, String>();
		params.put("username", "admin");
		params.put("password", "admin");
		String result = HttpClientUtil.getInstance().httpPost("http://www.sohu.com", params, null);
		System.out.println(result);
		
		// HttpClientUtil.getInstance().httpDownloadFile("https://10.248.71.211/file/download?md5Path=8d4719fdb38de8506b9fc5438614582e@192179&staticToken=f1634ed30b374f159a684ef353184f16&caToken="+caToken+"&isWpsSecure=1",
		// "C:\\Users\\12555\\Desktop\\securitydoc1.wpss", null);
		// String result
		// =HttpClientUtil.getInstance().httpGet("https://csfile.szoa.sz.gov.cn/file/previewMd5?md5Path=3170b1fcb90c6373d045a9bd56ee1930@29816",null);

		long end = System.currentTimeMillis();
		System.out.println("consume -> " + (end - start));
	}

	// 下载接口测试
	@Test
	public static void testHttpClientDownloadFile() throws Exception {

		long start = System.currentTimeMillis();
		// 文件下载示例
		HttpClientUtil.getInstance().httpDownloadFile(
				"http://file.zr.miit.gov.cn/file/download?md5Path=e655c72f4c9154cf07bc43106092a7ce@503333", "D:\\demo.log",
				null);
		// 下载进度输出
//		HttpClientUtil.getInstance().httpDownloadFile(
//				"http://file.zr.miit.gov.cn/file/download?md5Path=e655c72f4c9154cf07bc43106092a7ce@503333", "D:\\demo.pdf",
//				new HttpClientUtil.HttpClientDownLoadProgress() {
//					@Override
//					public void onProgress(int progress) {
//						System.out.println("download progress = " + progress);
//					}
//				}, null);
		long end = System.currentTimeMillis();
		System.out.println("consume -> " + (end - start));
	}

	// 上传接口测试
	//@Test
	public static void testHttpClientUploadFile() throws Exception {

		long start = System.currentTimeMillis();
		// 上传文件 POST 同步方法

		Map<String, String> uploadParams = new LinkedHashMap<String, String>();

		uploadParams.put("userId", "none");
		uploadParams.put("moduleName", "注意这里要填你系统的英文简称方便文件追朔！！！");
		uploadParams.put("fileSrcName", URLEncoder.encode("深圳市人大常委会办公厅文件.pdf", "UTF-8"));

		String token = URLEncoder.encode(
				"AQIC5wM2LY4Sfcx0FSNeUzw0Jnmyh9-Y5XP_l4_U0KGGPns.*AAJTSQACMDEAAlNLABMyMDI5NjExOTUxODM2MjU2MTY0*",
				"UTF-8");
		uploadParams.put("token", token);

		String result = HttpClientUtil.getInstance().uploadFileImpl("http://file.zr.miit.gov.cn/file/uploadDfsBackEnd",
				new File("C:\\usr\\local\\cssoa\\logs\\css_oa.log").getAbsolutePath(), "file", uploadParams);

		System.out.println(result);

		long end = System.currentTimeMillis();
		System.out.println("consume -> " + (end - start));
	}

	public static void main(String[] args) throws Exception {
		
		testHttpClientUploadFile();
		testHttpClientDownloadFile();
		
	}

}