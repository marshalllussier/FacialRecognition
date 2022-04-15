package com.autozone.facialrecognition.fragments;

import android.annotation.SuppressLint;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
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
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.autozone.facialrecognition.Logon;
import com.autozone.facialrecognition.MainActivity;
import com.autozone.facialrecognition.R;

import java.util.Calendar;
import java.util.HashMap;

public class AlternateloginFragment extends Fragment {

    private EditText Name;
    private EditText Password;
    private TextView Info;
    private Button Login;
    private View view;
    private final HashMap<String, String> accounts = new HashMap<>();
    private int counter = 5;

    @RequiresApi(api = Build.VERSION_CODES.N)
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
        accounts.put("kamal", "password");
        accounts.put("nick", "password");
        accounts.put("marshall", "password");
        accounts.put("dianne", "password");
        accounts.put("hong", "password");
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n")
    private void validate(String userName, String userPassword){
        if(accounts.containsKey(userName) && accounts.get(userName).equals(userPassword)){ // temporary
//            Toast.makeText(requireActivity(), "Unlock device", Toast.LENGTH_LONG).show();
            MainActivity activity = (MainActivity) getActivity();
            activity.set_user_logged_in(true);
            Calendar c = Calendar.getInstance();
            @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd HH:mm");
            String formattedDate = dateFormat.format(c.getTime());
            Logon logon = new Logon(userName, formattedDate, getContext());
            String lastLogonToast = logon.getLastLogon();
            if (!lastLogonToast.equals("")) {
                Toast.makeText(requireActivity(), ("You last logged on at: " + lastLogonToast), Toast.LENGTH_LONG).show();
            }
            logon.logToFile();
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new LoginhistoryFragment()).commit(); // This will redirect to a different fragment upon successful login
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
