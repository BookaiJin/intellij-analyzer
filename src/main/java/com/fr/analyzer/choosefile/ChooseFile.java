package com.fr.analyzer.choosefile;

import com.fr.analyzer.log.LogFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * @author bokai
 * @version 10.0
 * Created by bokai on 2019-12-12
 */
public class ChooseFile {

    private static ChooseFile chooseFile = new ChooseFile();

    public static ChooseFile getInstance() {
        return chooseFile;
    }

    /**
     * test
     */
//    public static void main(String... args) {
//        getInstance().getFilesPathToAnalyze("");
//    }
    public void getFilesPathToAnalyze(String filePath, String desPath) {
        String extractFilePath = "";
        try {
            ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(filePath));
            ZipEntry zipEntry = zipInputStream.getNextEntry();
            byte[] buffer = new byte[1024];
            while (zipEntry != null) {
                String zipFileName = zipEntry.getName();
                if (isToUnzip(zipFileName)) {
                    zipFileName = zipFileName.endsWith(".zip") ? "/zip/" + zipFileName : "/csv/" + zipFileName;
                    File extractedFile = new File(desPath + File.separator + zipFileName);
                    File resultFolder = new File(extractedFile.getParent());
                    if ("".equals(extractFilePath)) {
                        extractFilePath = resultFolder.getPath();
                    }
                    final boolean b = resultFolder.mkdirs();
                    FileOutputStream fos;
                    try {
                        fos = new FileOutputStream(extractedFile);
                    } catch (Exception e) {
                        LogFactory.getSystemLogger().error(e.getMessage(), e);
                        zipEntry = zipInputStream.getNextEntry();
                        continue;
                    }
                    int len;
                    while ((len = zipInputStream.read(buffer)) > 0) {
                        fos.write(buffer, 0, len);
                    }
                    if (extractedFile.getName().endsWith(".zip")) {
                        getFilesPathToAnalyze(extractedFile.getPath(), desPath);
                    }
                    fos.close();
                }
                try {
                    zipEntry = zipInputStream.getNextEntry();
                } catch (Exception e) {
                    LogFactory.getSystemLogger().error(e.getMessage(), e);
                }
            }
            zipInputStream.closeEntry();
            zipInputStream.close();
        } catch (Exception e) {
            LogFactory.getSystemLogger().error(e.getMessage(), e);
            LogFactory.getSystemLogger().error("Unzip failed");
        }
    }

    private boolean isToUnzip(String fileName) {
        return fileName.endsWith("zip") || (fileName.endsWith(".csv") && (fileName.startsWith("gcRecord") || fileName.startsWith("focusPoint")));
    }
}
