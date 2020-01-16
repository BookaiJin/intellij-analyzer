package com.fr.analyzer.gc.log;

import com.fr.analyzer.log.LogFactory;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.log4j.Logger;

/**
 * @author bokai
 * @version 10.0
 * Created by bokai on 2020-01-16
 */
public class GeneralGcLogs {

    private static GeneralGcLogs generalGcLogs = new GeneralGcLogs();
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
    private Logger logger = null;

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
                    try {
                        if ("GC".equalsIgnoreCase(temps[2])) {
                            row.append("[").append(temps[2]).append(" (").append(temps[5]).append(") [PSYoungGen: ")
                                    .append(Integer.valueOf(temps[7]) / 1024).append("K->").append(Integer.valueOf(temps[8]) / 1024).append("K(").append(Integer.valueOf(temps[10]) / 1024).append("K)] ")
                                    .append(Integer.valueOf(temps[19]) / 1024).append("K->").append(Integer.valueOf(temps[20]) / 1024).append("K(").append(Integer.valueOf(temps[22]) / 1024).append("K), ")
                                    .append(Integer.valueOf(temps[6]) / 1000F).append(" secs] [Times: real=").append(Integer.valueOf(temps[6]) / 1000F).append(" secs] [pid: ").append(temps[3])
                                    .append("] [node: ").append(temps[4]).append("]");
                        } else if ("Full GC".equalsIgnoreCase(temps[2])) {
                            row.append("[").append(temps[2]).append(" (").append(temps[5]).append(") [PSYoungGen: ")
                                    .append(Integer.valueOf(temps[7]) / 1024).append("K->").append(Integer.valueOf(temps[8]) / 1024).append("K(").append(Integer.valueOf(temps[9]) / 1024).append("K)] ")
                                    .append("[ParOldGen: ")
                                    .append(Integer.valueOf(temps[11]) / 1024).append("K->").append(Integer.valueOf(temps[12]) / 1024).append("K(").append(Integer.valueOf(temps[14]) / 1024).append("K)] ")
                                    .append(Integer.valueOf(temps[19]) / 1024).append("K->").append(Integer.valueOf(temps[20]) / 1024).append("K(").append(Integer.valueOf(temps[22]) / 1024).append("K), ")
                                    .append("[Metaspace: ")
                                    .append(Integer.valueOf(temps[15]) / 1024).append("K->").append(Integer.valueOf(temps[16]) / 1024).append("K(").append(Integer.valueOf(temps[18]) / 1024).append("K)], ")
                                    .append(Integer.valueOf(temps[6]) / 1000F).append(" secs] [Times: real=").append(Integer.valueOf(temps[6]) / 1000F).append(" secs] [pid: ").append(temps[3])
                                    .append("] [node: ").append(temps[4]).append("]");
                        }
                    } catch (Exception e) {
                        LogFactory.getSystemLogger().error("this line general failed" + e.getMessage(), e);
                    }
                    logger = LogFactory.getInstance().getLogger(desPath + File.separator + "__result" + File.separator + toAnaFile.getName().replace("csv", "log"));
                    logger.error(row.toString());
                }
            }
        } catch (Exception e) {
            LogFactory.getSystemLogger().error(e.getMessage(), e);
        }
    }
}
