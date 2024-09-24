package com.example.login_home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Home extends AppCompatActivity {

    private Button btn_logout;
    private TextView txt_view1;
    private TextView txt_view2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        btn_logout = findViewById(R.id.button2);
        txt_view1 = findViewById(R.id.textView5);
        txt_view2 = findViewById(R.id.textView7);

        Intent intent = getIntent();
        String receivedUser = intent.getStringExtra("user");
        String receivedPass = intent.getStringExtra("pass");
        txt_view1.setText("Username: " + receivedUser);
        txt_view2.setText("Password: " + receivedPass);

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}