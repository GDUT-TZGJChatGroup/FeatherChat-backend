package com.wjz.webserver.service;

import com.wjz.webserver.utils.Result;
import org.springframework.web.multipart.MultipartFile;


public interface FileService {

    Result uploadImg(MultipartFile img);

    Result uploadFile(MultipartFile multipartFile);
}
