package com.fr.analyzer.log;

import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 * @author bokai
 * @version 10.0
 * Created by bokai on 2020-01-17
 */
public class LoggerWrapper {

    private static Map<String, Logger> loggerMap = new HashMap<>();

    public static Logger getLogger(String node) {
        return loggerMap.get(node);
    }

    public static void setLogger(String node, Logger logger) {
        loggerMap.put(node, logger);
    }

}
