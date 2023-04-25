package com.biosphere.library.util;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.region.Region;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Random;

/**
 * @Author: zh、hyh
 * @Date: 2021/12/10 16:02
 */

public class TencentCosUtil {

    // 创建COS 凭证
    private static COSCredentials credentials = new BasicCOSCredentials(MyInfo.SecretId,MyInfo.SecretKey);
    // 配置 COS 区域 就购买时选择的区域 我这里是 南京（nanjing）
    private static ClientConfig clientConfig = new ClientConfig(new Region("ap-nanjing"));

    public static String uploadfile(MultipartFile file, String openID){
        // 创建 COS 客户端连接
        COSClient cosClient = new COSClient(credentials,clientConfig);
        String fileName = file.getOriginalFilename();
        try {
            String substring = fileName.substring(fileName.lastIndexOf("."));
            File localFile = File.createTempFile(String.valueOf(System.currentTimeMillis()),substring);
            file.transferTo(localFile);
            Random random = new Random();
            fileName =MyInfo.prefix + openID + "_" + random.nextInt(10000) + System.currentTimeMillis() + substring;
            // 将 文件上传至 COS
            PutObjectRequest objectRequest = new PutObjectRequest(MyInfo.bucketName,fileName,localFile);
            cosClient.putObject(objectRequest);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }finally {
            cosClient.shutdown();
        }
        return MyInfo.URL+fileName;
    }

    public static String uploadAvatar(MultipartFile file, String openID){
        // 创建 COS 客户端连接
        COSClient cosClient = new COSClient(credentials,clientConfig);
        String fileName = file.getOriginalFilename();
        try {
            String substring = fileName.substring(fileName.lastIndexOf("."));
            File localFile = File.createTempFile(String.valueOf(System.currentTimeMillis()),substring);
            file.transferTo(localFile);
            fileName =MyInfo.prefix + openID + "_" + "avatar" + ".jpeg";
            // 将 文件上传至 COS
            PutObjectRequest objectRequest = new PutObjectRequest(MyInfo.bucketName,fileName,localFile);
            cosClient.putObject(objectRequest);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }finally {
            cosClient.shutdown();
        }
        return MyInfo.URL+fileName;
    }

    public static String uploadfile(File file,String fileName){
        // 创建 COS 客户端连接
        COSClient cosClient = new COSClient(credentials,clientConfig);
        try {
            // 将 文件上传至 COS
            Random random = new Random();
            fileName=random.nextInt(10000)+fileName;
            PutObjectRequest objectRequest = new PutObjectRequest(MyInfo.bucketName,MyInfo.prefix+fileName,file);
            cosClient.putObject(objectRequest);
            System.out.println("insert into cos success!");
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            cosClient.shutdown();
        }

        return MyInfo.URL+MyInfo.prefix+fileName;
    }

    public static void delFile(String filename){
        new Thread(new Runnable() {
            public void run() {
                // 创建 COS 客户端连接
                COSClient cosClient = new COSClient(credentials,clientConfig);
                try{
                    String key=MyInfo.prefix+filename;
                    cosClient.deleteObject(MyInfo.bucketName, key);
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    cosClient.shutdown();
                }
            }
        }).start();
    }

}