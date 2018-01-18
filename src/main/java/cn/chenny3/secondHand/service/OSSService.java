package cn.chenny3.secondHand.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


public interface OSSService {
    String saveImage(MultipartFile multipartFile,String filePath) throws IOException;
}
