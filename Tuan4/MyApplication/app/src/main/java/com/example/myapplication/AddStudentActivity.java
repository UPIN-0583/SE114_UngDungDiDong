package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class AddStudentActivity extends AppCompatActivity {
    private EditText studentIdEditText, studentNameEditText, studentYearEditText;
    private Button addStudentButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        studentIdEditText = findViewById(R.id.studentIdEditText);
        studentNameEditText = findViewById(R.id.studentNameEditText);
        studentYearEditText = findViewById(R.id.studentYearEditText);
        addStudentButton = findViewById(R.id.addStudentButton);

        addStudentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int studentId = Integer.parseInt(studentIdEditText.getText().toString());
                String studentName = studentNameEditText.getText().toString();
                String studentYear = studentYearEditText.getText().toString();

                // Trả kết quả về Activity gọi nó
                Intent resultIntent = new Intent();
                resultIntent.putExtra("studentId", studentId);
                resultIntent.putExtra("studentName", studentName);
                resultIntent.putExtra("studentYear", studentYear);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }
}
