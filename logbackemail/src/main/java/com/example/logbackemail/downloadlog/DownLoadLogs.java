package com.example.logbackemail.downloadlog;

import com.example.logbackemail.Utils.CompressUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
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

    @RequestMapping("/download")
    public void downLoad(@RequestParam(value = "fileName", required = false) String fileName, HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.info("filePath{}", fileName);
        PrintWriter out = response.getWriter();
        //返回字符集
        response.setContentType("text/html; charset=UTF-8");
        String path1 = this.getClass().getClassLoader().getResource(".").getPath();
        String str = path1.substring(0, path1.indexOf("target"));

        String formPath = logPath;

        //如果未指定文件名 就打包下载 对应目录下的文件
        if (null == fileName || fileName == "") {
            //遍历这个路径下的所有文件名
            List fileNames = compressUtils.getFileName(formPath);
            //如果找不到文件  返回not found file
            if (fileNames == null || fileNames.size() == 0) {

                //注意text/html，和application/html
                out.print("<html><body><script type='text/javascript'>alert('not found file');</script></body></html>");
                out.flush();
                out.close();
            } else {
                //开始批量压缩下载
                ZipOutputStream zos = new ZipOutputStream(response.getOutputStream());
                response.setContentType("APPLICATION/OCTET-STREAM");
                response.setHeader("Content-Disposition", "attachment; filename=" + compressUtils.getZipFilename());
                log.info("in BatchDownload................");
                File[] files = new File[fileNames.size()];
                for (int i = 0; i < files.length; i++) {
                    String filename = fileNames.get(i).toString();
                    files[i] = new File(formPath + "/" + filename);
                }
                // 将文件进行压缩
                compressUtils.zipFile(files, "", zos);
                zos.flush();
                zos.close();
            }
        } else {
            //针对性下载对应文件 支持关键字模糊匹配文件名
            File file = new File(formPath);
            // 取得文件名列表
            File[] files = file.listFiles();
            for (File f : files) {

                String filename = f.getName();
                log.info("fileName:", filename);

                if (filename.toLowerCase().contains(fileName)) {
                    // 取得文件的后缀名。
                    String ext = filename.substring(filename.lastIndexOf(".") + 1).toUpperCase();

                    // 以流的形式下载文件。
                    InputStream fis = new BufferedInputStream(new FileInputStream(f));
                    byte[] buffer = new byte[fis.available()];
                    fis.read(buffer);
                    fis.close();
                    // 清空response
                    response.reset();
                    // 设置response的Header
                    response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes()));
                    response.addHeader("Content-Length", "" + file.length());
                    OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
                    response.setContentType("application/octet-stream");
                    toClient.write(buffer);
                    toClient.flush();
                    toClient.close();
                }

            }
        }

    }
}

