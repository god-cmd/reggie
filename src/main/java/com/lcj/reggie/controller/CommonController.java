package com.lcj.reggie.controller;/*
    @author lcj
    @create -
*/

import com.lcj.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/common")
public class CommonController {
    @Value("${upload.path}")
    private String fileUploadPath;

    @GetMapping("/download")
    public ResponseEntity<byte[]> download(@RequestParam("name") String name) throws IOException {
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(fileUploadPath+name));
        byte[] bytes = in.readAllBytes();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Disposition","attachment;filename="+name);
        ResponseEntity<byte[]> responseEntity = new ResponseEntity<byte[]>(bytes,httpHeaders, HttpStatus.OK);
        return responseEntity;
    }

    @PostMapping("/upload")
    public R<String> upload(@RequestPart("file") MultipartFile file) throws IOException {
        String filename = file.getOriginalFilename();
        log.info("上传的文件名是：{}",filename);
        String suffix = filename.substring(filename.lastIndexOf("."));
        String finalFilename = UUID.randomUUID().toString() + suffix;
        String filePath = fileUploadPath+finalFilename;

        File dir = new File(fileUploadPath);
        if(!dir.exists()){
            dir.mkdir();
        }

        file.transferTo(new File(filePath));
        return R.success(finalFilename);
    }
}
