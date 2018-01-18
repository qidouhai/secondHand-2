package cn.chenny3.secondHand.service.impl;

import cn.chenny3.secondHand.service.OSSService;
import com.aliyun.oss.OSSClient;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
public class OSSServiceImpl implements OSSService {
    @Override
    public String saveImage(MultipartFile multipartFile,String filePath) throws IOException {
        // endpoint以杭州为例，其它region请按实际情况填写
        String endpoint = "http://oss-cn-beijing.aliyuncs.com";
// 云账号AccessKey有所有API访问权限，建议遵循阿里云安全最佳实践，创建并使用RAM子账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建
        String accessKeyId = "LTAItW2Pg1WV9PeO";
        String accessKeySecret = "3spRfo8CAcMcvjap8ZT0dBufIZAE0G";
// 创建OSSClient实例
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
// 上传文件流
        String fileName=generateUniqueFileName(multipartFile);
        InputStream inputStream = multipartFile.getInputStream();
        String absolutePath=filePath+fileName;
        ossClient.putObject("secondhand-oss",absolutePath , inputStream);
// 关闭client
        ossClient.shutdown();

        return absolutePath;
    }

    public String generateUniqueFileName(MultipartFile file){
        int dotPos = file.getOriginalFilename().lastIndexOf(".");
        String fileExt=file.getOriginalFilename().substring(dotPos+1);
        String fileName= UUID.randomUUID().toString().replaceAll("-","")+"."+fileExt;
        return fileName;
    }
}
