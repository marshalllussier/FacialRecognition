package com.autozone.facialrecognition;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Logon {
    private String username;
    private Context context;
    private String timestamp;

    public Logon(String username, String timestamp, Context context) {
        this.username = username;
        this.timestamp = timestamp;
        this.context = context;
    }

    public void logToFile() {
        try {
            File path = context.getFilesDir();
            FileOutputStream writer = new FileOutputStream(new File(path, "log_file.txt"), true);
            username = username.substring(0, 1).toUpperCase() + username.substring(1);
            String to_log = (username + " logged in: " + timestamp + "\n");
            writer.write(to_log.getBytes());
            writer.close();
        }
        catch (IOException exc) {
            Log.e("Exception", "File write failed: " + exc);
        }
    }

    public String getLastLogon() {
        File path = context.getFilesDir();
        File readFrom = new File(path, "log_file.txt");
        try {
            InputStreamReader streamReader = new InputStreamReader(new FileInputStream(readFrom));
            BufferedReader br = new BufferedReader(streamReader);
            String line = new String();
            username = username.substring(0, 1).toUpperCase() + username.substring(1);
            while (br.ready()) {
                String tempLine = br.readLine();
                if (tempLine.contains(username)) {
                    line = tempLine;
                }
            }
            return line.split(":", 2)[1];
        } catch (Exception exc) {
            Log.e("Exception", "Unable to fetch last logon. Error: " + exc);
            return exc.toString();
        }
    }
}
