package com.autozone.facialrecognition.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.autozone.facialrecognition.R;


import java.io.File;
import java.io.FileInputStream;

public class LoginhistoryFragment extends Fragment {

    private View view;
    private TextView text;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        String content = readFromFile("log_file.txt");
        view = inflater.inflate(R.layout.fragment_login_history, container, false);
        text = view.findViewById(R.id.text_login_history);
        text.setText(content);
        return view;
    }

    private String readFromFile(String fileName) {
        File path = getContext().getFilesDir();
        File readFrom = new File(path, fileName);
        byte[] content = new byte[(int) readFrom.length()];
        try {
            FileInputStream stream = new FileInputStream(readFrom);
            stream.read(content);
            stream.close();
            return new String(content);
        } catch (Exception exc) {
            exc.printStackTrace();
            return exc.toString();
        }
    }
}
