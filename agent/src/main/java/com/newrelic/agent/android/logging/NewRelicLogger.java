package com.newrelic.agent.android.logging;


import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

public class NewRelicLogger {

    protected static final String TAG = NewRelicLogger.class.getName();
    protected boolean remoteLoggingEnabled = true;

    protected int logLevel = Log.INFO;

    protected Context context;


    private static File agentLogFile;

    public void v(String message) {
        Log.v(TAG, message);
    }

    public void v(String message, Throwable throwable) {
        Log.v(TAG, message, throwable);
    }

    public void v(String message, Throwable throwable, Map<String, String> attributes) {
        Log.v(TAG, message, throwable);
    }

    public void d(String message) {
        Log.d(TAG, message);
    }

    public void d(String message, Throwable throwable) {
        Log.d(TAG, message, throwable);
    }

    public void d(String message, Throwable throwable, Map<String, String> attributes) {
        Log.d(TAG, message, throwable);
    }

    public void i(String message) {
        Log.i(TAG, message);
    }

    public void i(String message, Throwable throwable) {
        Log.i(TAG, message, throwable);
    }

    public void i(String message, Throwable throwable, Map<String, String> attributes) {
        Log.i(TAG, message, throwable);
    }

    public void wtf(String message) {
        Log.wtf(TAG, message);
    }

    public void wtf(String message, Throwable throwable) {
        Log.wtf(TAG, message, throwable);
    }

    public void wtf(String message, Throwable throwable, Map<String, String> attributes) {
        Log.wtf(TAG, message, throwable);
    }

    public void e(String message) {
        Log.e(TAG, message);
    }

    public void e(String message, Throwable throwable) {
        Log.e(TAG, message, throwable);
    }

    public void e(String message, Throwable throwable, Map<String, String> attributes) {
        Log.e(TAG, message, throwable);
    }

    public void w(String message) {
        Log.w(TAG, message);
    }

    public void w(String message, Throwable throwable) {
        Log.w(TAG, message, throwable);
    }

    public void w(String message, Throwable throwable, Map<String, String> attributes) {
        Log.w(TAG, message, throwable);
    }

    public void log(int priority, String message) {
        Log.println(priority, TAG, message);
    }

    private NewRelicLogger(Builder builder) {
        this.remoteLoggingEnabled = builder.remoteLoggingEnabled;
        this.logLevel = builder.logLevel;
        this.context = builder.context;
        agentLogFile = new File(context.getFilesDir(), "agentLog.txt");
    }

    public static void appendLog(String logLevel,String message,Throwable throwable,Map<String, String> attributes) {
        try {
            if (!agentLogFile.exists()) {
                agentLogFile.createNewFile();
            }
            //BufferedWriter for performance, true to set append to file flag
            BufferedWriter buf = new BufferedWriter(new FileWriter(agentLogFile, true));

            Map<String,String> log = new HashMap<>();
            log.put("timeStamp",String.valueOf(System.currentTimeMillis()));
            log.put("message",message);
            log.put("logLevel",logLevel);
            if(throwable != null) {
                log.put("throwableMessage", throwable.getLocalizedMessage());
            }
            if(attributes != null) {
                log.putAll(attributes);
            }

            JSONObject jsonObject = new JSONObject(log);
            String logJsonData = jsonObject.toString();
            buf.append(logJsonData);
            buf.append(",");
            buf.newLine();
            buf.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }



    public static class Builder {
        private boolean remoteLoggingEnabled;

        private int logLevel;

        private Context context;


        // other fields

        public Builder remoteLoggingEnabled(boolean remoteLoggingEnabled) {
            this.remoteLoggingEnabled = remoteLoggingEnabled;
            return this;
        }

        public Builder logLevel(int logLevel) {
            this.logLevel = logLevel;
            return this;
        }

        public Builder context(Context context) {
            this.context = context;
            return this;
        }

        // other setter methods
        public NewRelicLogger build() {
            return new NewRelicLogger(this);
        }
    }

}
