package com.zx.zxboxlauncher.weather.location.impl;


import android.text.TextUtils;

import com.zx.zxboxlauncher.weather.location.IP2Location;

import java.util.ArrayList;
import java.util.List;

public class IP2LocationPoll  {

    private static List<IP2Location> list = new ArrayList<>();

    private static int index = 0;

    static {
        list.add(new IP2LocationBD());
        list.add(new IP2LocationTB());
        list.add(new IP2LocationXL());
    }

    public static String ip2Location(String ip) {
        String location = "";
        while(index < list.size()) {
            location = list.get(index).ip2Location(ip);
            if(!TextUtils.isEmpty(location)) {
                break;
            }
            index++;
        }

        return location;
    }

    private static void addIP2Location(IP2Location ip2Location) {
        if (ip2Location instanceof IP2LocationPoll) {
            throw new IllegalArgumentException();
        } else {
            list.add(ip2Location);
        }
    }
}
