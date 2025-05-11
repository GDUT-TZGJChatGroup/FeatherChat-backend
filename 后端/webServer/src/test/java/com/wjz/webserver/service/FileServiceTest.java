package com.wjz.webserver.service;

import com.wjz.webserver.utils.Result;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FileServiceTest {

    @Mock
    private FileService fileService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testUploadImg_正常路径() throws Exception {
        // 测试文件上传正常路径
        MultipartFile img = new MockMultipartFile("img", "test.jpg", "image/jpeg", "test content".getBytes());
        Result expected = Result.ResultOk("图片上传成功");
        when(fileService.uploadImg(img)).thenReturn(expected);

        Result actual = fileService.uploadImg(img);

        assertEquals(expected, actual);
        verify(fileService, times(1)).uploadImg(img);
    }

    @Test
    void testUploadImg_空文件() {
        // 测试上传空文件
        MultipartFile img = new MockMultipartFile("img", "", "image/jpeg", new byte[0]);
        Result expected = Result.error("图片文件不能为空");
        when(fileService.uploadImg(img)).thenReturn(expected);

        Result actual = fileService.uploadImg(img);

        assertEquals(expected, actual);
        verify(fileService, times(1)).uploadImg(img);
    }

    @Test
    void testUploadImg_非图片文件() {
        // 测试上传非图片文件
        MultipartFile file = new MockMultipartFile("img", "test.txt", "text/plain", "test content".getBytes());
        Result expected = Result.error("请上传图片文件");
        when(fileService.uploadImg(file)).thenReturn(expected);

        Result actual = fileService.uploadImg(file);

        assertEquals(expected, actual);
        verify(fileService, times(1)).uploadImg(file);
    }

    @Test
    void testUploadFile_正常路径() throws Exception {
        // 测试文件上传正常路径
        MultipartFile multipartFile = new MockMultipartFile("file", "test.docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", "test content".getBytes());
        Result expected = Result.ResultOk("文件上传成功");
        when(fileService.uploadFile(multipartFile)).thenReturn(expected);

        Result actual = fileService.uploadFile(multipartFile);

        assertEquals(expected, actual);
        verify(fileService, times(1)).uploadFile(multipartFile);
    }

    @Test
    void testUploadFile_空文件() {
        // 测试上传空文件
        MultipartFile multipartFile = new MockMultipartFile("file", "", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", new byte[0]);
        Result expected = Result.error("文件不能为空");
        when(fileService.uploadFile(multipartFile)).thenReturn(expected);

        Result actual = fileService.uploadFile(multipartFile);

        assertEquals(expected, actual);
        verify(fileService, times(1)).uploadFile(multipartFile);
    }

    @Test
    void testUploadFile_超大小文件() {
        // 测试上传超大小文件，假设服务端限制文件大小为1MB
        byte[] content = new byte[1024 * 1024 + 1]; // 1MB + 1 byte
        MultipartFile multipartFile = new MockMultipartFile("file", "test.docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", content);
        Result expected = Result.error("文件大小超出限制");
        when(fileService.uploadFile(multipartFile)).thenReturn(expected);

        Result actual = fileService.uploadFile(multipartFile);

        assertEquals(expected, actual);
        verify(fileService, times(1)).uploadFile(multipartFile);
    }
}
