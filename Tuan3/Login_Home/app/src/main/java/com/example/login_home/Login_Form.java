package com.example.login_home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import java.util.ArrayList;
import java.util.List;

public class Login_Form extends AppCompatActivity {

    private Button Btn_login;
    private EditText txt_User;
    private EditText txt_Password;
    private List<user> userList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_form);

        Btn_login = findViewById(R.id.button);
        txt_User = findViewById(R.id.editUser);
        txt_Password = findViewById(R.id.editPassword);

        userList.add(new user("Huy", "123"));
        userList.add(new user("Binh", "456"));
        userList.add(new user("Tuan", "789"));
        userList.add(new user("Long", "123"));
        userList.add(new user("Loi","456"));
        userList.add(new user("Phat","789"));
        userList.add(new user("Dung","123"));


        Btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = txt_User.getText().toString();
                String password = txt_Password.getText().toString();
                if (isValidCredentials(user, password)){
                    Intent intent = new Intent(Login_Form.this, Home.class);
                    intent.putExtra("user",user);
                    intent.putExtra("pass",password);
                    startActivity(intent);
                }
            }
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private boolean isValidCredentials(String username, String password) {
        for (user User: userList) {
            if (User.getUsername().equals(username) && User.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }

}