package com.diga.generic.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 日志工具, 主要了为了后面迎合Logback + Lombok
 */
public class log {
    private static LogEnum LEVEL = LogEnum.DEBUG;
    private static ExecutorService executorService = Executors.newSingleThreadExecutor();
    private static final Log logger = new Log(10);

    public enum LogEnum {
        DEBUG, INFO, WARN, ERROR
    }

    public static void setLEVEL(LogEnum level) {
        LEVEL = level;
    }

    private static void log(LogEnum logLevel, String format, Object... args) {
        String message = String.format(format, args);
        switch (logLevel) {
            case DEBUG:
                logger.set(LogEnum.DEBUG, message);
                break;
            case INFO:
                logger.set(LogEnum.INFO, message);
                break;
            case WARN:
                logger.set(LogEnum.WARN, message);
                break;
            case ERROR:
                logger.set(LogEnum.ERROR, message);
                break;
        }

        executorService.submit(() -> {
            try {
                System.out.println(logger.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    public static void warn(String format, Object... args) {
        log(LogEnum.WARN, format, args);
    }

    public static void error(String format, Object... args) {
        log(LogEnum.ERROR, format, args);
    }

    public static void info(String format, Object... args) {
        log(LogEnum.INFO, format, args);
    }

    public static void debug(String format, Object... args) {
        log(LogEnum.DEBUG, format, args);
    }

    /**
     * 日志块信息
     */
    @Data
    @AllArgsConstructor
    private static class LogBlock {
        private Long currentTime;
        private LogEnum level;
        private String message;
    }

    private static class Log {
        /**
         * 通过logQueue实现日志的同步存放, 我们还需要一个异步线程来
         * 操作这个logQueue
         */
        private ArrayBlockingQueue<LogBlock> logQueue;

        /**
         * @param size 最大存放的日志条数, 放满后线程进入阻塞状态
         */
        public Log(int size) {
            if (size < 1) {
                throw new IllegalArgumentException("存放日志条数最少为1条");
            }
            this.logQueue = new ArrayBlockingQueue<>(size);
        }

        public void set(LogEnum level, String message) {
            this.logQueue.add(new LogBlock(System.currentTimeMillis(), level, message));
        }

        /**
         * 获取需要打印的日志
         * @return
         */
        public String get() throws InterruptedException {
            LogBlock block = this.logQueue.take();
            Date date = new Date(block.currentTime);
            String time = DateTimeUtils.dateToString(date, "yyyy-MM-dd HH:mm:ss");
            StringBuilder sb = new StringBuilder();
            String name = Thread.currentThread().getName();

            StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
            StackTraceElement e = stacktrace[1];
            String methodName = e.getMethodName();

            return sb.append(time).append(" [").append(block.level.name()).append("] [").append(name).append("] ").append(methodName).append(" - ").append(block.message).toString();
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            log.info("log");
            System.out.println("println");
        }
    }

}
