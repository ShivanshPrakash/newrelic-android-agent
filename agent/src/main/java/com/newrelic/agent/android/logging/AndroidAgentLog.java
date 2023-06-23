/*
 * Copyright (c) 2022-present New Relic Corporation. All rights reserved.
 * SPDX-License-Identifier: Apache-2.0
 */

package com.newrelic.agent.android.logging;

import android.content.Context;
import android.util.Log;

import com.newrelic.agent.android.AgentConfiguration;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class AndroidAgentLog implements AgentLog {
    private static final String TAG = "newrelic";

    // Default to INFO
    private int level = INFO;
    private static File agentLogFile;
    private static AgentConfiguration agentConfiguration;

    public AndroidAgentLog(final Context context) {
        agentLogFile = new File(context.getFilesDir(), "agentLog.txt");
        agentConfiguration = new AgentConfiguration();
    }

    @Override
    public void audit(String message) {
        if (level == AUDIT) {
            Log.d(TAG, message);
            appendLog(message);
        }
    }

    public void debug(final String message) {
        if (level >= DEBUG) {
            Log.d(TAG, message);
            appendLog(message);
        }
    }

    public void verbose(final String message) {
        if (level >= VERBOSE) {
            Log.v(TAG, message);
            appendLog(message);
        }
    }

    public void info(final String message) {
        if (level >= INFO) {
            Log.i(TAG, message);
            appendLog(message);
        }
    }

    public void warn(final String message) {
        if (level >= WARN) {
            Log.w(TAG, message);
            appendLog(message);
        }
    }

    public void error(final String message) {
        if (level >= ERROR) {
            Log.e(TAG, message);
            appendLog(message);
        }
    }

    public void error(final String message, Throwable cause) {
        if (level >= ERROR) {
            Log.e(TAG, message, cause);
            appendLog(message);
        }
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        if (level <= AUDIT && level >= ERROR) {
            this.level = level;
        } else {
            throw new IllegalArgumentException("Log level is not between ERROR and AUDIT");
        }
    }

    public static void appendLog(String log) {
        try {
            if (!agentLogFile.exists()) {
                agentLogFile.createNewFile();
            }
            AgentLogManager.setLogFileLocation(agentLogFile.getAbsolutePath());

            //BufferedWriter for performance, true to set append to file flag
            BufferedWriter buf = new BufferedWriter(new FileWriter(agentLogFile, true));
            buf.append(log);
            buf.newLine();
            buf.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
