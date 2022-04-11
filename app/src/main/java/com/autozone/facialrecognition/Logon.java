package com.autozone.facialrecognition;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

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
}
