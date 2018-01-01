package util;

import play.Logger;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

/**
 * ログ用ラッパー
 */
public final class LoggerUtil {

    public enum Type {
        START("開始："),
        END("終了："),
        ERROR("エラー："),
        ACTION("処理："),
        WARN("注意："),
        DONE("完了：");

        private String word;

        Type(String word) {
            this.word = word;
        }

        public String getWord() {
            return this.word;
        }
    }

    /**
     * クラスの名前
     */
    private String className;

    /**
     * メソッドの名前
     */
    private String methodName;


    public LoggerUtil(String className, String methodName, Type type, Object... args) {
        this.start = System.nanoTime();
        this.end = this.start;
        this.className = className;
        this.methodName = methodName;
        LoggerUtil.info(className, methodName, type, args);
    }

    static public void trace(String className, String methodName, Type type, Object... args) {
        if (Logger.isTraceEnabled()) {
            StringBuilder sb = new StringBuilder();
            for (Object obj : args) {
                sb.append(obj.toString()).append(",");
            }
            Logger.trace(type.getWord() + className + "." + methodName + "," + sb.toString());
        }
    }

    static public void warn(String className, String methodName, Type type, Object... args) {
        if (Logger.isWarnEnabled()) {
            StringBuilder sb = new StringBuilder();
            for (Object obj : args) {
                sb.append(obj.toString()).append(",");
            }
            Logger.warn(type.getWord() + className + "." + methodName + "," + sb.toString());
        }
    }

    static public void info(String className, String methodName, Type type, Object... args) {
        if (Logger.isInfoEnabled()) {
            StringBuilder sb = new StringBuilder();
            for (Object obj : args) {
                sb.append(obj.toString()).append(",");
            }
            Logger.info(type.getWord() + className + "." + methodName + "," + sb.toString());
        }
    }

    static public void error(String className, String methodName, Type type, Object... args) {
        if (Logger.isErrorEnabled()) {
            StringBuilder sb = new StringBuilder();
            for (Object obj : args) {
                sb.append(obj.toString()).append(",");
            }
            Logger.error(type.getWord() + className + "." + methodName + "," + sb.toString());
        }
    }

    static public void debug(String className, String methodName, Type type, Object... args) {
        if (Logger.isDebugEnabled()) {
            StringBuilder sb = new StringBuilder();
            for (Object obj : args) {
                sb.append(obj.toString()).append(",");
            }
            Logger.debug(type.getWord() + className + "." + methodName + "," + sb.toString());
        }
    }

    public void trace(Type type, String... values) {
        if (Logger.isTraceEnabled()) {
            long end = System.nanoTime();
            StringBuilder sb = new StringBuilder();
            sb.append(type.getWord()).append("処理時間：")
                    .append((end - this.start) / 1000000f).append("ms：")
                    .append(className).append(".").append(methodName).append(":");
            for (String value : values) {
                sb.append(value).append(",");
            }
            Logger.trace(sb.toString());
        }
    }

    public void error(Type type, String... values) {
        if (Logger.isErrorEnabled()) {
            long end = System.nanoTime();
            StringBuilder sb = new StringBuilder();
            sb.append(type.getWord()).append("処理時間：")
                    .append((end - this.start) / 1000000f).append("ms：")
                    .append(className).append(".").append(methodName).append(":");
            for (String value : values) {
                sb.append(value).append(",");
            }
            Logger.error(sb.toString());
        }
    }

    public void warn(Type type, String... values) {
        if (Logger.isWarnEnabled()) {
            long end = System.nanoTime();
            StringBuilder sb = new StringBuilder();
            sb.append(type.getWord()).append("処理時間：")
                    .append((end - this.start) / 1000000f).append("ms：")
                    .append(className).append(".").append(methodName).append(":");
            for (String value : values) {
                sb.append(value).append(",");
            }
            Logger.warn(sb.toString());
        }
    }

    public void info(Type type, String... values) {
        if (Logger.isInfoEnabled()) {
            long end = System.nanoTime();
            StringBuilder sb = new StringBuilder();
            sb.append(type.getWord()).append("処理時間：")
                    .append((end - this.start) / 1000000f).append("ms：")
                    .append(className).append(".").append(methodName).append(":");
            for (String value : values) {
                sb.append(value).append(",");
            }
            Logger.info(sb.toString());
        }
    }

    public void debug(Type type, String... values) {
        if (Logger.isDebugEnabled()) {
            long end = System.nanoTime();
            StringBuilder sb = new StringBuilder();
            sb.append(type.getWord()).append("処理時間：")
                    .append((end - this.start) / 1000000f).append("ms：")
                    .append(className).append(".").append(methodName).append(":");
            for (String value : values) {
                sb.append(value).append(",");
            }
            Logger.debug(sb.toString());
        }
    }

    /**
     * ログを初期化してからの時間を取得します。
     *
     * @return ミリ秒
     */
    public float getMillTime() {
        this.end = System.nanoTime();
        return (end - this.start) / 1000000f;
    }


    public float getIntervalMillTime() {
        long time = System.nanoTime();
        return (time - this.end) / 1000000f;
    }

    public void interval() {
        this.debug(Type.ACTION, getIntervalMillTime() + "ms");
    }

    private long start;

    private long end;

}
