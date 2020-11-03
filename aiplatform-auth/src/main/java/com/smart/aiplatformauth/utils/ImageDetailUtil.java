package com.smart.aiplatformauth.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;

/**
 * 各种图片处理工具类
 * @Auther chengjz
 */
@Component
public class ImageDetailUtil {

  @Value("${Instructions.imagepathurl}")
  private String imagepathurl;

  @Value("${Instructions.imagepath}")
  private String imagepath;

  /**
   * base64字符串转化成图片
   * @param imgStr
   * @return
   */
  public String generateImage(String imgStr) {

    //对字节数组字符串进行Base64解码并生成图片
    if (imgStr == null) {
      //图像数据为空
      return "error";
    }
    //Base64解码
    BASE64Decoder decoder = new BASE64Decoder();
    try
    {
      String[] imgStrs = imgStr.split(";");
      String imageend = "";
      for(int i=0;i<imgStrs.length;i++) {
        byte[] b = decoder.decodeBuffer(imgStrs[i]);
        for(int j=0;j<b.length;++j)
        {
          if(b[j]<0)
          {
            //调整异常数据
            b[j]+=256;
          }
        }
        //生成jpeg图片
        String imageUID = System.currentTimeMillis() + String.valueOf(i);
        String imgFilePath = imagepath + imageUID + ".jpg";//新生成的图片
        OutputStream out = new FileOutputStream(imgFilePath);
        out.write(b);
        out.flush();
        out.close();
        if(imageend.equals("")) {
          imageend = imageUID + ".jpg";
        } else {
          imageend = imageend + "," + imageUID + ".jpg";
        }
      }
      return imageend;
    }
    catch (Exception e)
    {
      return "error";
    }
  }

  /**
   * 图片相对路径地址转换
   * @param imageName
   * @return
   */
  public String imagePathDetail(String imageName) {
    if(imageName != null && !imageName.equals("")) {
      String imageNewName = "";
      String[] imageNames = imageName.split(",");
      for(String imageNameStr : imageNames) {
        if(imageNewName.equals("")) {
          imageNewName = imagepathurl + imageNameStr;
        } else {
          imageNewName = imageNewName + "," + imagepathurl + imageNameStr;
        }
      }
      return imageNewName;
    }
    return imageName;
  }

  /**
   * 由文件上传改的图片上传方法
   * @param file
   * @return
   */
  public String upload(MultipartFile[] file) {
    String result = "success";
    String fileEndName = "";
    for(int i=0; i<file.length; i++) {
      String path =imagepath;
      String suffix = file[i].getOriginalFilename().substring(file[i].getOriginalFilename().lastIndexOf("."));
      suffix = suffix.toLowerCase();
      String fileName = "";
      if(suffix.equals(".jpg") || suffix.equals(".jpeg") || suffix.equals(".png") || suffix.equals(".gif") || suffix.equals(".bmp")){
        fileName = UUID.randomUUID().toString() + suffix;
        File targetFile = new File(path, fileName);
        if(!targetFile.getParentFile().exists()){
          targetFile.getParentFile().mkdirs();
        }
        //保存
        try {
          file[i].transferTo(targetFile);
          if(fileEndName.equals("")) {
            fileEndName = fileName;
          } else {
            fileEndName = fileEndName + "," + fileName;
          }
        } catch (Exception e) {
          result = "error";
        }
      }else{
        result = "error";
      }
    }
    if(!result.equals("error")) {
      result = fileEndName;
    }
    return result;
  }

  /**
   * 重要文件上传方法,限制PDF格式
   * @param file
   * @return
   */
  public String uploadFile(MultipartFile[] file) {
    String result = "success";
    String fileEndName = "";
    for(int i=0; i<file.length; i++) {
      String path =imagepath;
      String suffix = file[i].getOriginalFilename().substring(file[i].getOriginalFilename().lastIndexOf("."));
      suffix = suffix.toLowerCase();
      String fileName = "";
      if(suffix.equals(".pdf")){
        fileName = UUID.randomUUID().toString() + suffix;
        File targetFile = new File(path, fileName);
        if(!targetFile.getParentFile().exists()){
          targetFile.getParentFile().mkdirs();
        }
        //保存
        try {
          file[i].transferTo(targetFile);
          if(fileEndName.equals("")) {
            fileEndName = fileName;
          } else {
            fileEndName = fileEndName + "," + fileName;
          }
        } catch (Exception e) {
          result = "error";
        }
      }else{
        result = "error";
      }
    }
    if(!result.equals("error")) {
      result = fileEndName;
    }
    return result;
  }

  /**
   * 重要文件上传方法,不限格式
   * @param file
   * @return
   */
  public String uploadFile1(MultipartFile[] file) {
    String result = "success";
    String fileEndName = "";
    for(int i=0; i<file.length; i++) {
      String path =imagepath;
      String suffix = file[i].getOriginalFilename().substring(file[i].getOriginalFilename().lastIndexOf("."));
      suffix = suffix.toLowerCase();
      String fileName = "";
      fileName = UUID.randomUUID().toString() + suffix;
      File targetFile = new File(path, fileName);
      if(!targetFile.getParentFile().exists()){
        targetFile.getParentFile().mkdirs();
      }
      //保存
      try {
        file[i].transferTo(targetFile);
        if(fileEndName.equals("")) {
          fileEndName = fileName;
        } else {
          fileEndName = fileEndName + "," + fileName;
        }
      } catch (Exception e) {
        result = "error";
      }

    }
    if(!result.equals("error")) {
      result = fileEndName;
    }
    return result;
  }
}
