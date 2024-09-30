package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AddClassActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_class);

        EditText classIdEditText = findViewById(R.id.classIdEditText);
        EditText classNameEditText = findViewById(R.id.classNameEditText);
        Button addClassButton = findViewById(R.id.addClassButton);

        addClassButton.setOnClickListener(v -> {
            int classId = Integer.parseInt(classIdEditText.getText().toString());
            String className = classNameEditText.getText().toString();

            // Truyền dữ liệu về MainActivity qua Intent
            Intent resultIntent = new Intent();
            resultIntent.putExtra("classId", classId);
            resultIntent.putExtra("className", className);
            setResult(RESULT_OK, resultIntent);
            finish(); // Kết thúc Activity này và quay lại MainActivity
        });
    }
}
