package com.example.logbackemail.Utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author madengbo
 * @create 2018-11-13 14:36
 * @desc 解压  压缩工具、
 * @Version 1.0
 **/
@Component
@Slf4j
public class CompressUtils {

    public void zipFile(File[] subs, String baseName, ZipOutputStream zos) throws Exception {

        for (int i = 0; i < subs.length; i++) {

            File f = subs[i];
            zos.putNextEntry(new ZipEntry(baseName + f.getName()));
            FileInputStream fis = new FileInputStream(f);
            byte[] buffer = new byte[1024];
            int r = 0;
            while ((r = fis.read(buffer)) != -1) {
                zos.write(buffer, 0, r);
                zos.flush();
            }
            zos.closeEntry();
            fis.close();
        }
    }

    public String getZipFilename(String zipName) {
        //默认时间命名
        Date date = new Date();
        String s = date.getTime() + ".zip";

        if (null != zipName || zipName != "") {
            s = zipName + s;
        }
        return s;
    }

    public List getFileName(String filePath) {
        File file = new File(filePath);
        List fileNameList = new ArrayList();
        File[] fileList = file.listFiles();
        if (fileList == null || fileList.length == 0) {
            return null;
        }
        for (int i = 0; i < fileList.length; i++) {
            if ("txt".equals(fileList[i].getName().substring(fileList[i].getName().lastIndexOf(".") + 1))) {
                continue;
            }
            if (fileList[i].isFile()) {
                String fileName = fileList[i].getName();
                fileNameList.add(fileName);
            }
        }
        return fileNameList;
    }

    public HttpServletResponse downLoadFiles(List<File> files, HttpServletResponse response) throws Exception {
        try {
            //List<File> 作为参数传进来，就是把多个文件的路径放到一个list里面

            //创建一个临时压缩文件

            //临时文件可以放在CDEF盘中，但不建议这么做，因为需要先设置磁盘的访问权限，最好是放在服务器上，方法最后有删除临时文件的步骤
            String zipFilename = "D:/tempFile.zip";
            File file = new File(zipFilename);
            if (!file.exists()) {
                file.createNewFile();
            }
            response.reset();
            //response.getWriter()
            //创建文件输出流
            FileOutputStream fous = new FileOutputStream(file);
            ZipOutputStream zipOut = new ZipOutputStream(fous);
            zipFile(files, zipOut);
            zipOut.close();
            fous.close();
            return downloadZip(file, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    /**
     * 把接受的全部文件打成压缩包
     */
    public static void zipFile(List files, ZipOutputStream outputStream) {
        int size = files.size();
        for (int i = 0; i < size; i++) {
            File file = (File) files.get(i);
            zipFile(file, outputStream);
        }
    }

    /**
     * 根据输入的文件与输出流对文件进行打包
     */
    public static void zipFile(File inputFile, ZipOutputStream ouputStream) {
        try {
            if (inputFile.exists()) {
                if (inputFile.isFile()) {
                    FileInputStream IN = new FileInputStream(inputFile);
                    BufferedInputStream bins = new BufferedInputStream(IN, 512);
                    ZipEntry entry = new ZipEntry(inputFile.getName());
                    ouputStream.putNextEntry(entry);
                    // 向压缩文件中输出数据
                    int nNumber;
                    byte[] buffer = new byte[512];
                    while ((nNumber = bins.read(buffer)) != -1) {
                        ouputStream.write(buffer, 0, nNumber);
                    }
                    // 关闭创建的流对象
                    bins.close();
                    IN.close();
                } else {
                    try {
                        File[] files = inputFile.listFiles();
                        for (int i = 0; i < files.length; i++) {
                            zipFile(files[i], ouputStream);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static HttpServletResponse downloadZip(File file, HttpServletResponse response) {
        if (file.exists() == false) {
            System.out.println("待压缩的文件目录：" + file + "不存在.");
        } else {
            try {
                // 以流的形式下载文件。
                InputStream fis = new BufferedInputStream(new FileInputStream(file.getPath()));
                byte[] buffer = new byte[fis.available()];
                fis.read(buffer);
                fis.close();
                // 清空response
                response.reset();

                OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
                response.setContentType("application/octet-stream");

                //如果输出的是中文名的文件，在此处就要用URLEncoder.encode方法进行处理
                response.setHeader("Content-Disposition", "attachment;filename="
                        + new String(file.getName().getBytes("GB2312"), "ISO8859-1"));
                toClient.write(buffer);
                toClient.flush();
                toClient.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            } finally {
                try {
                    File f = new File(file.getPath());
                    f.delete();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return response;
    }

}
