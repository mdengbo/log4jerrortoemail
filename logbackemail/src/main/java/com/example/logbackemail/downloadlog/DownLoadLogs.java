package com.example.logbackemail.downloadlog;

import com.example.logbackemail.Utils.CompressUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.List;
import java.util.zip.ZipOutputStream;

/**
 * @author madengbo
 * @create 2018-11-13 10:19
 * @desc 下载指定日志文件
 * @Version 1.0
 **/
@RestController
@Slf4j
public class DownLoadLogs {

    @Autowired
    CompressUtils compressUtils;

    @Value("${path.log}")
    private String logPath;

    @GetMapping("/downloadLog")
    public void downLoad(@RequestParam(value = "fileName", required = false) String fileName, HttpServletRequest request, HttpServletResponse response) throws IOException {

        log.info("fileName={}", fileName);


        log.info("日志保存路径 logPath={}", logPath);
        //logPath = "./logs/gaxq-file-webapp";
        String formPath = logPath;


        //如果未指定文件名 就打包下载 对应目录下的文件
        if (null == fileName || fileName == "") {
            //遍历这个路径下的所有文件名
            List fileNames = compressUtils.getFileName(formPath);
            //如果找不到文件  返回not found file
            if (fileNames == null || fileNames.size() == 0) {

                PrintWriter out = response.getWriter();
                //返回字符集
                response.setContentType("text/html; charset=UTF-8");
                //注意text/html，和application/html
                out.print("<html><body><script type='text/javascript'>alert('not found file');</script></body></html>");
                out.flush();
                out.close();
            } else {
                String zipName = formPath.substring(formPath.lastIndexOf("/")+1,formPath.length())+"-log";

                String zipFilename = compressUtils.getZipFilename(zipName);

                //开始批量压缩下载
                ServletOutputStream outs = response.getOutputStream();
                //输出文件位置new FileOutputStream(formPath)
                ZipOutputStream zos = new ZipOutputStream(outs);
                response.reset();
                response.setContentType("APPLICATION/OCTET-STREAM; charset=utf-8");
                response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(zipFilename, "UTF-8"));
                log.info("in BatchDownload................");
                File[] files = new File[fileNames.size()];
                for (int i = 0; i < files.length; i++) {
                    String filename = fileNames.get(i).toString();
                    files[i] = new File(formPath + "/" + filename);
                }
                // 将文件进行压缩
                try {
                    compressUtils.zipFile(files, "", zos);
                } catch (Exception e) {
                    log.info("文件压出错",e);
                }finally {

                    if(null != zos){
                        zos.flush();
                        zos.close();
                    }
                    if (null != outs) {
                        outs.flush();
                        outs.close();
                        log.info("下载完成");
                    }
                }

            }
        } else {
            //针对性下载对应文件
            File file = new File(formPath);
            // 取得文件名列表
            File[] files = file.listFiles();
            for (File f : files) {
                //跳过文件夹
                if(f.isDirectory()){
                    continue;
                }

                String ff = f.getName();

                int endPoint = ff.lastIndexOf(".");

                String subName = ff.substring(0, endPoint);

                log.info("fileName={}", ff);

                if (subName.contains(fileName)) {
                    // 取得文件的后缀名。
                    String ext = ff.substring(endPoint + 1).toUpperCase();

                    // 以流的形式下载文件。
                    InputStream fis = new BufferedInputStream(new FileInputStream(f));

                    //获取文件输出IO流
                    OutputStream outs = response.getOutputStream();
                    BufferedOutputStream bouts = new BufferedOutputStream(outs);

                    // 清空response
                    response.reset();
                    //设置response内容的类型 普通下载类型
                    response.setContentType("application/x-download");
                    //设置response内容的类型 下载安卓应用apk
                    response.setContentType("application/vnd.android.package-archive");
                    //设置文件大小
                    response.setContentLength((int) f.length());
                    //设置头部信息
                    response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(f.getName(), "UTF-8"));

                    int bytesRead = 0;
                    byte[] buffer = new byte[8192];
                    //开始向网络传输文件流
                    while ((bytesRead = fis.read(buffer, 0, 8192)) != -1) {
                        bouts.write(buffer, 0, bytesRead);
                    }
                    //调用flush()方法
                    bouts.flush();
                    fis.close();
                    outs.close();
                    bouts.close();

                    break;
                }

            }
        }

    }
}

