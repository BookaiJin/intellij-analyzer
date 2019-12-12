package com.fr.analyzer.log;

import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

/**
 * 根据名字自动生成日志输出器
 *
 * @author bokai
 * @version 10.0
 * Created by bokai on 2019-12-12
 */
public class LogFactory {

    private static LogFactory logFactory = new LogFactory();

    /**
     * 解析过程中的异常日志
     * 输出至运行目录
     */
    private static Logger systemLogger;

    private static Logger fileLogger;

    private static FileAppender fileAppender;

    static {
        systemLogger = Logger.getLogger("system");
        DailyRollingFileAppender fileAppender = new DailyRollingFileAppender();
        fileAppender.setName("system");
        fileAppender.setDatePattern("'.'yyyy-MM-dd");
        fileAppender.setFile(System.getProperty("user.dir") + "/analyzer-system.log");
        fileAppender.setLayout(new PatternLayout("%d %t %p [%c] %m %n"));
        fileAppender.setAppend(true);
        fileAppender.activateOptions();
        systemLogger.setAdditivity(false);
        systemLogger.addAppender(fileAppender);
    }

    public static Logger getSystemLogger() {
        return systemLogger;
    }

    public static LogFactory getInstance() {
        return logFactory;
    }

    public static void main(String... args) {

    }

    /**
     * 根据文件名构建输出器
     */
    public Logger getLogger(String fileName) {

        return fileLogger;
    }
}
