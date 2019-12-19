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

    private static DailyRollingFileAppender sysAppender = new DailyRollingFileAppender();

    private static Logger fileLogger = Logger.getLogger("file");

    private static FileAppender fileAppender = new FileAppender();

    static {
        systemLogger = Logger.getLogger("system");
        sysAppender.setName("system");
        sysAppender.setDatePattern("'.'yyyy-MM-dd");
        sysAppender.setFile(System.getProperty("user.dir") + "/analyzer-system.log");
        sysAppender.setLayout(new PatternLayout("%d %t %p [%c] %m %n"));
        sysAppender.setAppend(true);
        sysAppender.activateOptions();
        systemLogger.setAdditivity(false);
        systemLogger.addAppender(sysAppender);
    }

    public static void refreshSystemLogger(String path){
        sysAppender.setFile(path + "/syslog/analyzer-system.log");
        sysAppender.activateOptions();
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
        fileAppender.setName("file");
        fileAppender.setFile(fileName);
        fileAppender.setLayout(new PatternLayout());
        fileAppender.activateOptions();
        fileLogger.setAdditivity(false);
        fileLogger.addAppender(fileAppender);
        return fileLogger;
    }
}
