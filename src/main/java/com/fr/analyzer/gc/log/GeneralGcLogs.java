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
    private LoggerWrapper loggerWrapper = null;

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
                    if (LoggerWrapper.getLogger(temps[4] + toAnaFile.getName()) == null) {
                        LoggerWrapper.setLogger(temps[4] + toAnaFile.getName(), LogFactory.getInstance().getLogger(desPath + File.separator + "__result" + File.separator + typeFolder + File.separator + toAnaFile.getName().replace("csv",
                                temps[4] + "." + "log")));
                    }
                    try {
                        if ("GC".equalsIgnoreCase(temps[2])) {
                            row.append("[").append(temps[2]).append(" (").append(temps[5]).append(") [PSYoungGen: ")
                                    .append((temps[7])).append("K->").append((temps[8])).append("K(").append((temps[10])).append("K)] ")
                                    .append((temps[19])).append("K->").append((temps[20])).append("K(").append((temps[22])).append("K), ")
                                    .append(Integer.valueOf(temps[6]) / 1000F).append(" secs] [Times: real=").append(Integer.valueOf(temps[6]) / 1000F).append(" secs] [pid: ").append(temps[3])
                                    .append("] [node: ").append(temps[4]).append("]");
                        } else if ("Full GC".equalsIgnoreCase(temps[2])) {
                            row.append("[").append(temps[2]).append(" (").append(temps[5]).append(") [PSYoungGen: ")
                                    .append((temps[7])).append("K->").append((temps[8])).append("K(").append((temps[9])).append("K)] ")
                                    .append("[ParOldGen: ")
                                    .append((temps[11])).append("K->").append((temps[12])).append("K(").append((temps[14])).append("K)] ")
                                    .append((temps[19])).append("K->").append((temps[20])).append("K(").append((temps[22])).append("K), ")
                                    .append("[Metaspace: ")
                                    .append((temps[15])).append("K->").append((temps[16])).append("K(").append((temps[18])).append("K)], ")
                                    .append(Integer.valueOf(temps[6]) / 1000F).append(" secs] [Times: real=").append(Integer.valueOf(temps[6]) / 1000F).append(" secs] [pid: ").append(temps[3])
                                    .append("] [node: ").append(temps[4]).append("]");
                        }
                        row.append(" [balancePromoterScore: ").append(temps[23]).append(", releasePromoterScore: ").append(temps[24]).append(", loadScore: ").append(temps[25]).append("]");
                    } catch (Exception e) {
                        LogFactory.getSystemLogger().error("this line general failed" + e.getMessage(), e);
                    }
                    LoggerWrapper.getLogger(temps[4]).error(row.toString());
                }
            }
        } catch (Exception e) {
            LogFactory.getSystemLogger().error(e.getMessage(), e);
        }
    }
}
