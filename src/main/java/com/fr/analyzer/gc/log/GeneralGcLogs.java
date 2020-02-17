package com.fr.analyzer.gc.log;

import com.fr.analyzer.log.LogFactory;
import com.fr.analyzer.log.LoggerWrapper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author bokai
 * @version 10.0
 * Created by bokai on 2020-01-16
 */
public class GeneralGcLogs {

    private static final String typeFolder = "gcLogs";
    private static GeneralGcLogs generalGcLogs = new GeneralGcLogs();
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

    private GeneralGcLogs() {
    }

    public static void main(String... args) {

    }

    public static GeneralGcLogs getInstance() {

        return generalGcLogs;
    }

    public void generalGcLogs(String folderPath, String desPath) {


        try {
            File toAnaFile = new File(folderPath);
            if (toAnaFile.getName().startsWith("__result")) {
                return;
            }
            if (toAnaFile.isDirectory()) {
                File[] files = toAnaFile.listFiles();
                assert files != null;
                for (File file : files) {
                    if (file.getName().endsWith("csv")) {
                        generalGcLogs(file.getPath(), desPath);
                    }
                }
            }
            if (toAnaFile.getName().startsWith("gcRecord")) {
                BufferedReader reader = new BufferedReader(new FileReader(toAnaFile));
                String temp;
                while ((temp = reader.readLine()) != null) {
                    StringBuilder row = new StringBuilder();
                    String[] temps = temp.split(",");
                    try {
                        Date date = new Date(Long.parseLong(temps[1]));
                        row.append(dateFormat.format(date)).append(": ");
                    } catch (Exception e) {
                        //表头无法解析，不管
                        continue;
                    }
                    String loggerKey = temps[4] + toAnaFile.getName();
                    String loggerKeyByMon = temps[4];
                    if (LoggerWrapper.getLogger(loggerKey) == null) {
                        LoggerWrapper.setLogger(
                                loggerKey,
                                LogFactory.getInstance().getLogger(
                                        desPath + File.separator + "__result" + File.separator + typeFolder + File.separator + temps[4] + File.separator + toAnaFile.getName().replace("csv", "log")
                                )
                        );
                    }
                    if (LoggerWrapper.getLogger(loggerKeyByMon) == null) {
                        LoggerWrapper.setLogger(
                                loggerKeyByMon,
                                LogFactory.getInstance().getLogger(
                                        desPath + File.separator + "__result" + File.separator + typeFolder + File.separator + temps[4] + ".log")
                        );
                    }
                    try {
                        //解析一条记录,接在StringBuilder后边
                        GcMessageGenerator.record(row, temps);
                    } catch (Exception e) {
                        LogFactory.getSystemLogger().error("this line general failed" + e.getMessage(), e);
                    }
                    LoggerWrapper.getLogger(loggerKey).error(row.toString());
                    LoggerWrapper.getLogger(loggerKeyByMon).error(row.toString());
                }
            }
        } catch (Exception e) {
            LogFactory.getSystemLogger().error(e.getMessage(), e);
        }
    }
}
