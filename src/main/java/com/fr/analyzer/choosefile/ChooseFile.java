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
    public static void main(String... args) {
        getInstance().getFilesPathToAnalyze("F:\\Work\\永不宕机\\treas201910.zip");
    }

    public String getFilesPathToAnalyze(String filePath) {
        String extractFilePath = "";
        try {
            ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(filePath));
            ZipEntry zipEntry = zipInputStream.getNextEntry();
            byte[] buffer = new byte[1024];
            while (zipEntry != null) {
                String zipFileName = zipEntry.getName();
                File extractedFile = new File(filePath.substring(0, filePath.lastIndexOf(File.separator)) + File.separator + zipFileName);
                File resultFolder = new File(extractedFile.getParent());
                if ("".equals(extractFilePath)) {
                    extractFilePath = resultFolder.getPath();
                }
                final boolean b = resultFolder.mkdirs();
                FileOutputStream fos = new FileOutputStream(extractedFile);
                int len;
                while ((len = zipInputStream.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                if (extractedFile.getName().endsWith(".zip")) {
                    getFilesPathToAnalyze(extractedFile.getPath());
                }
                fos.close();
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
        return extractFilePath;
    }
}
