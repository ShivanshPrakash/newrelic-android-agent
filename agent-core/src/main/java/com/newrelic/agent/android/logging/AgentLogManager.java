/*
 * Copyright (c) 2022-present New Relic Corporation. All rights reserved.
 * SPDX-License-Identifier: Apache-2.0
 */

package com.newrelic.agent.android.logging;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.newrelic.agent.android.AgentConfiguration;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class AgentLogManager {
    private static DefaultAgentLog instance = new DefaultAgentLog();
    private static String logFileLocation = "";
    private static final String LOG_API_URL = "https://log-api.newrelic.com/log/v1";
    private static final String LOG_API_KEY = "";

    public static AgentLog getAgentLog() {
        return instance;
    }

    public static void setAgentLog(AgentLog instance) {
        AgentLogManager.instance.setImpl(instance);
    }

    public static String getLogFileLocation() {
        return logFileLocation;
    }

    public static void setLogFileLocation(String logFileLocation) {
        AgentLogManager.logFileLocation = logFileLocation;
    }

    public static void clearLog() {
        try {
            File logFile = new File(logFileLocation);
            if (logFile.exists()) {
                logFile.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void uploadLog() {
        AgentConfiguration agentConfiguration = new AgentConfiguration();
        File logFile = new File(logFileLocation);
        try {
            JsonArray jsonArr = new JsonArray();
            if (logFile.exists()) {
                try {
                    BufferedReader br = new BufferedReader(new FileReader(logFile));
                    String line;

                    while ((line = br.readLine()) != null) {
                        JsonObject jsonObj = new JsonObject();
                        jsonObj.addProperty("message", line);
                        jsonObj.addProperty("entity.guid", "MjUxOTY2NnxNT0JJTEV8QVBQTElDQVRJT058NTM0NTQ0NTUz");
                        jsonObj.addProperty("sessionId", agentConfiguration.getSessionID());
                        jsonArr.add(jsonObj);
                    }
                    br.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            URL url = new URL(LOG_API_URL);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Api-Key", LOG_API_KEY);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);
            conn.setDoInput(true);

            DataOutputStream localdos = new DataOutputStream(conn.getOutputStream());
            localdos.writeBytes(jsonArr.toString());
            localdos.flush();
            localdos.close();

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            StringBuilder sb = new StringBuilder();
            while (reader.readLine() != null) {
                sb.append(reader.readLine());
            }
            getAgentLog().info(sb.toString());
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            clearLog();
        }
    }
}
