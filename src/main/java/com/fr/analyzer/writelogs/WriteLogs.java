package com.fr.analyzer.writelogs;

import com.fr.analyzer.log.LogFactory;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Date;
import org.apache.log4j.Logger;

/**
 * @author bokai
 * @version 10.0
 * Created by bokai on 2019-12-12
 */
public class WriteLogs {

    private static WriteLogs writeLogs = new WriteLogs();
    private Logger logger;

    public static WriteLogs getInstance() {
        return writeLogs;
    }

    /**
     * test
     */
    public static void main(String... args) {
        getInstance().analyzeFile("F:\\Work\\永不宕机\\treas201910", "F:\\Work\\永不宕机");
    }

    public void analyzeFile(String folderPath, String desPath) {
        try {
            File toAnaFile = new File(folderPath);
            if (toAnaFile.isDirectory()) {
                File[] files = toAnaFile.listFiles();
                for (File file : files) {
                    if (file != null) {
                        analyzeFile(file.getPath(), desPath);
                    }
                }
            }
            if (toAnaFile.getName().startsWith("focusPoint")) {
                BufferedReader reader = new BufferedReader(new FileReader(toAnaFile));
                String temp = "";
                while ((temp = reader.readLine()) != null) {
                    StringBuilder row = new StringBuilder();
                    if (temp.contains("FR-F4002")) {
                        row.append("-----");
                    } else if (temp.contains("FR-F4003")) {
                        row.append("=====");
                    } else {
                        continue;
                    }
                    String[] temps = temp.split(",");
                    Date date = new Date(Long.parseLong(temps[1]));
                    row.append(date.toString()).append(",").append(temp);
                    logger = LogFactory.getInstance().getLogger(desPath + File.separator + "result" + File.separator + toAnaFile.getName().replace("csv", "log"));
                    logger.error(row.toString());
                }
            }
        } catch (Exception e) {
            LogFactory.getSystemLogger().error(e.getMessage(), e);
        }
    }
}
