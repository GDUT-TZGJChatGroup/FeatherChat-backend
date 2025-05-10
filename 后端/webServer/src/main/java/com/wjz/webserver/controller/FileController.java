package com.wjz.webserver.controller;

import com.wjz.webserver.service.FileService;
import com.wjz.webserver.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RestController
public class FileController {

    @Autowired
    private FileService fileService;

    @PostMapping("/upload")
    public Result uploadImg(@RequestParam("img") MultipartFile multipartFile) {
        System.out.println("上传图片");
        return fileService.uploadImg(multipartFile);

    }
    @PostMapping("/upload/file")
    public Result uploadFile(@RequestParam("file") MultipartFile multipartFile) {
        System.out.println("上传文件");
        return fileService.uploadFile(multipartFile);
    }
}
