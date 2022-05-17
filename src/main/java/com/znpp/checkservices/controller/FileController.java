package com.znpp.checkservices.controller;

import com.dtflys.forest.http.ForestResponse;
import com.google.gson.Gson;
import com.znpp.checkservices.config.FileConfig;
import com.znpp.checkservices.service.CheckService;
import org.apache.commons.io.FileUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
public class FileController {
    private String imgPath = null;

    @Resource
    private FileConfig fileConfig;
    @Resource
    private CheckService checkService;

    /**
     * 测试接口
     */
    @GetMapping("/ykttest")
    public Map<String, Object> sayHello() {
        Map<String, Object> result = new HashMap<>();
        System.out.println("收到请求 》》》");
        result.put("message", "显示这句话，说明YktLoginController正常运行");
        return result;
    }

    /**
     * @Description 多文件上传
     **/
    @PostMapping("/files")
    public String uploadFiles(@RequestParam("file") MultipartFile[] files) throws IOException {
        //
        String filePath = null;
        //Result rs = null;
        // 使用日期来分类管理上传的文件
        String format = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        //先存临时文件
        File folder = new File(fileConfig.getRootDir() + fileConfig.getTempFileDir() + format + "/");
        if (!folder.exists()) {
            folder.mkdirs();
        }

        StringBuilder paths = new StringBuilder();
        String oldName = null;
        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                paths.append(",");
                continue;
            }
            oldName = file.getOriginalFilename();
            //String newName = UUID.randomUUID().toString() + oldName.substring(oldName.lastIndexOf("."));
            File newFile = new File(folder, oldName);
            //保存文件
            file.transferTo(newFile);
            paths.append(fileConfig.getFileDir() + format + "/" + oldName);

            filePath = folder + "/" + oldName;
        }

        //调用检测函数
        ForestResponse response = checkService.checkImage(filePath, oldName);

        //rs = Result.ok();
//        Map<String,Object> data = new HashMap(4);
//        data.put("paths",filePath);
//        data.put("result",response);
//        //rs.setData(data);
//        Gson gson = new Gson();
//        String result = gson.toJson(data);

        //打印检测结果
        System.out.println("打印结果 >>>    " + response.getContent());

        return String.valueOf(response.getContent());
    }


    /**
     * @Description 单文件上，这里业务场景是 上传失物招领的图片信息，每个事件需要绑定唯一ID值，考虑使用时间戳
     * http 请求里的 header 中 Content-Type 需要设置为：  multipart/form-data
     * 将上传的文件放在Body中，以 form-data 提交 其中这个接口定义了 参数名为 "file"
     **/
    @PostMapping("/file")
    public Object uploadFile(@RequestParam MultipartFile file) throws Exception{
        //获取文件类型
        System.out.println("MultipartFile 识别出文件类型为： >>>>>> "+file.getContentType());
        String oldName = file.getOriginalFilename();//得到上传的源文件名，得到后缀名
        String length = oldName.substring(0,oldName.indexOf("."));//这里截取 . 后面的
        String fileType = oldName.substring(length.length(),oldName.length());
        System.out.println("上传的文件后缀名为 >>>>>> : "+fileType);

        // 使用日期来分类管理上传的文件
        String format = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        //先存临时文件
        File folder = new File( fileConfig.getRootDir()+fileConfig.getFileDir() + format+"/");
        if (!folder.exists()) {
            folder.mkdirs();
        }
        if(file == null||file.isEmpty()){
            return ("文件不可以是空");
        }
        if (!fileType.equals(".jpg") & !fileType.equals(".png")){
            return "文件类型非图片";
        }


        //利用UUID类生成唯一文件名
        //String newName = UUID.randomUUID().toString();
        long fileName = new Date().getTime();
        File newFile = new File(folder, (fileName+fileType));//构建文件存储路径以及文件名称
        //保存文件
        file.transferTo(newFile);

        //返回结果
        Map<String,Object> data = new HashMap<>();
        imgPath = fileConfig.getRootDir()+fileConfig.getFileDir() + format+"/"+fileName+fileType;
        data.put("msg", "successful");
        data.put("path", imgPath);
        Gson gson = new Gson();
        String result = gson.toJson(data);

        System.out.println(result);
        System.out.println("file name > "+ String.valueOf(fileName)+fileType);

        //调用检测函数
        ForestResponse response = checkService.checkImage(imgPath, String.valueOf(fileName)+fileType);
        System.out.println(" check result : > > >   " + response.getContent());



        return response.getContent();
    }

    /**
     * @Description 文件下载
     **/
    @GetMapping("/downloadfile")
    public ResponseEntity downloadFile(@RequestParam String fileName, String downloadFilePath) throws IOException {
        //下载文件,简单new个文件
        File downloadFile = new File(fileConfig.getRootDir()+downloadFilePath);
        String downloadFileName = "";
        if(fileName!=null){
            downloadFileName = fileName+downloadFilePath.substring(downloadFilePath.lastIndexOf("."));
        }
        else{
            downloadFileName = downloadFile.getName();
        }

        HttpHeaders headers = new HttpHeaders();
        //下载显示的文件名，并解决中文名称乱码问题
        downloadFileName = new String(downloadFileName.getBytes("UTF-8"),"iso-8859-1");
        //通知浏览器以attachment（下载方式）打开
        headers.setContentDispositionFormData("attachment", downloadFileName);
        //applicatin/octet-stream: 二进制流数据（最常见的文件下载）
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        // 使用下org.apache.commons.io.FileUtils工具类
        byte[] bytes = FileUtils.readFileToByteArray(downloadFile);
        return new ResponseEntity<byte[]>(bytes, headers, HttpStatus.CREATED);
    }

    /**
     * 返回图片
     */
    @GetMapping("/getcheckimage.jpg")
    //这里用Header存放参数是因为前端使用Glide请求在线图片URL目前只能添加header
    public void getCheckCode(HttpServletResponse response, @RequestHeader String path) throws Exception {
        System.out.println(path);

        File image = new File(path);
        InputStream myStream = new FileInputStream(image);
        response.setContentType("image/jpeg; charset=UTF-8");
        OutputStream outputStream = response.getOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = myStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, len);
        }
        myStream.close();

    }
}