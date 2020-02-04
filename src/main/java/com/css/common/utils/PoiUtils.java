package com.css.common.utils;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class PoiUtils {
    public static ResponseEntity<byte[]> exportExcel(Workbook workbook, String fileName) throws IOException {

        String txt = fileName + ".xls";

        // 创建一个http请求头
        HttpHeaders headers = new HttpHeaders();
        // 设置，参数：1.控制方式-内嵌，2.文件名，在浏览器需转换格式
        headers.setContentDispositionFormData("attachment",
                txt);
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        // 创建一个字节数组输出流
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        workbook.write(baos);
        // 使用给定的主体、头和状态代码创建一个新的http实体
        ResponseEntity<byte[]> responseEntity = new ResponseEntity<>(baos.toByteArray(), headers, HttpStatus.CREATED);
        return responseEntity;
    }
}
