package com.zx.zxboxlauncher.weather.location.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zx.zxboxlauncher.weather.location.IP2Location;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

public class IP2LocationXL implements IP2Location {

    @Override
    public String ip2Location(String ip) {
        String result = "";
        try {
            URLConnection connection = new URL("http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=js&ip=" + ip).openConnection();
            connection.getInputStream();
            connection.setConnectTimeout(5000);
            try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String s;
                while ((s = br.readLine()) != null) {
                    result += s;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        String json = result.substring(result.indexOf("=") + 1, result.length() - 1);
        Gson gson = new Gson();
        Map<String, String> map = gson.fromJson(json, new TypeToken<Map<String, String>>() {
        }.getType());
        StringBuilder location = new StringBuilder();
        if (map.get("country") != null) {
            location.append(map.get("country"));
        }
        if (map.get("province") != null) {
            location.append(map.get("province"));
        }
        if (map.get("city") != null) {
            location.append(map.get("city"));
        }
        return location.toString();
    }

    @Override
    public void addIP2Location(IP2Location ip2Location) {
        throw new UnsupportedOperationException();
    }
}
