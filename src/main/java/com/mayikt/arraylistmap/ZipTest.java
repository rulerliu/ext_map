package com.mayikt.arraylistmap;

import java.io.*;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @description:
 * @author: liuwq
 * @date: 2019/8/19 0019 上午 9:35
 * @version: V1.0
 */
public class ZipTest {

    public static void main(String[] args) {
        String source = "E:/ziptest/1.png";
        String target = "E:/ziptest/test.zip";
//        zipFileNoBuffer(source, target); // 59676
//        zipFileBuffer(source, target);
        zipFileChannel(source, target);
    }

    public static void zipFileNoBuffer(String source, String target) {
        // 开始时间
        long beginTime = System.currentTimeMillis();
        File zipFile = new File(target);
        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile))) {

            for (int i = 0; i < 10; i++) {
                try (InputStream input = new FileInputStream(source)) {
                    zos.putNextEntry(new ZipEntry(i + ".png"));
                    int temp;
                    while ((temp = input.read()) != -1) {
                        zos.write(temp);
                    }
                }
            }
            System.out.println(System.currentTimeMillis() - beginTime);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void zipFileBuffer(String source, String target) {
        //开始时间
        long beginTime = System.currentTimeMillis();
        File zipFile = new File(target);
        try (ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFile));
             BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(zipOut)) {
            for (int i = 0; i < 10; i++) {
                try (BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(source))) {
                    zipOut.putNextEntry(new ZipEntry(i + ".png"));
                    int temp;
                    while ((temp = bufferedInputStream.read()) != -1) {
                        bufferedOutputStream.write(temp);
                    }
                }
            }
            System.out.println(System.currentTimeMillis() - beginTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void zipFileChannel(String source, String target) {
        //开始时间
        long beginTime = System.currentTimeMillis();
        File zipFile = new File(target);
        try (ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFile));
             WritableByteChannel writableByteChannel = Channels.newChannel(zipOut)) {
            for (int i = 0; i < 10; i++) {
                try (FileChannel fileChannel = new FileInputStream(source).getChannel()) {
                    zipOut.putNextEntry(new ZipEntry(i + ".png"));
                    fileChannel.transferTo(0, fileChannel.size(), writableByteChannel);
                }
            }
            System.out.println(System.currentTimeMillis() - beginTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
