package com.fr.analyzer.writelogs;

import com.fr.analyzer.log.LogFactory;
import java.io.File;
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
        getInstance().analyzeFile("/Users/bokai/Downloads/永不宕机/四川德恩精工/treasures", "/Users/bokai/Downloads/永不宕机/四川德恩精工/logs");
    }

    public void analyzeFile(String folderPath, String desPath) {
        StringBuilder row = new StringBuilder();
        try {
            File toAnaFile = new File(folderPath);
            if (toAnaFile.isDirectory()) {
                File[] files = toAnaFile.listFiles();
                for (File file : files) {
                    analyzeFile(file.getPath(), desPath + File.separator + toAnaFile.getName());
                }
            }
            if(toAnaFile.getName().startsWith("FocusPoint")){

            }
            logger = LogFactory.getInstance().getLogger("filePath");
            logger.fatal(row);
        } catch (Exception e) {

        }
    }
}
