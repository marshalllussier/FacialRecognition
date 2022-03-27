package com.autozone.facialrecognition.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.autozone.facialrecognition.R;

public class AlternateloginFragment extends Fragment {

    private EditText Name;
    private EditText Password;
    private TextView Info;
    private Button Login;
    private View view;
    private int counter = 5;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_alternate_login, container, false);
        Name = view.findViewById(R.id.etName);
        Password = view.findViewById(R.id.etPassword);
        Login = view.findViewById(R.id.btnLogin);
        Info = view.findViewById(R.id.tvInfo);
        Info.setText("Number of attempts remaining: 5");
        Login.setOnClickListener(view1 -> validate(Name.getText().toString(), Password.getText().toString()));
        return view;
    }

    @SuppressLint("SetTextI18n")
    private void validate(String userName, String userPassword){
        if(userName.equals("admin") && userPassword.equals("admin")){
            Toast.makeText(requireActivity(), "Unlock device", Toast.LENGTH_LONG).show();

//            menu.findItem(R.id.nav_login_history).setVisible(true);

//            Intent intent = new Intent(MainActivity.this, SecondActivity.class);
//            startActivity(intent);
        }
        else{
            counter--;
            Info.setText("Number of attempts remaining: " + counter);
            if(counter == 0){
                Toast.makeText(requireActivity(), "Device disabled", Toast.LENGTH_LONG).show();
                Login.setEnabled(false);
            }
        }
    }
}
