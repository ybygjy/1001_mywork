package org.ybygjy.logger;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * �ṩͳһ����־�����������ڴ���
 * @author WangYanCheng
 * @version 2012-4-16
 */
public class LoggerFactory {
    /** Root Logger */
    private Logger rootLogger;
    /**��־�ļ�·��*/
    private static String logFilePath = "./DataMigrationLog%g.log";
    /** singleton instance*/
    private static final LoggerFactory lfInst;
    static {
        lfInst = new LoggerFactory();
    }

    /**
     * ���캯����ʼ��
     */
    private LoggerFactory() {
        rootLogger = Logger.getLogger("");
        rootLogger.setLevel(Level.INFO);
        try {
            Handler fileHandler = new FileHandler(logFilePath, false);
            fileHandler.setFormatter(new SimpleFormatter());
            rootLogger.addHandler(fileHandler);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Add Logger Handler
     * @param handler {@link Handler}
     */
    public void addLoggerHandler(Handler handler) {
        rootLogger.addHandler(handler);
    }

    /**
     * Remove Logger Handler
     * @param handler {@link Handler}
     */
    public void removeLoggerHandler(Handler handler) {
        rootLogger.removeHandler(handler);
    }

    /**
     * ȡ��־��¼����
     * @param name a name for the Logger
     * @return a suitable Logger
     * @see Logger#getLogger(String)
     */
    public Logger getLogger(String name) {
        return Logger.getLogger(name);
    }

    /**
     * ȡ��־��¼����
     * @param name a name for the Logger
     * @return a suitable Logger
     * @see Logger#getLogger(String)
     */
    public Logger getLogger() {
        return rootLogger;
    }

    /**
     * ȡ����־ͳһ����ʵ��
     * @return lfInst {@link LoggerFactory}
     */
    public static LoggerFactory getInstance() {
        return lfInst;
    }

    /**
     * ȡ��־�ļ�·��
     * @return logFilePath ��־�ļ�·��
     */
    public static String getLogFilePath() {
        return logFilePath;
    }
}
