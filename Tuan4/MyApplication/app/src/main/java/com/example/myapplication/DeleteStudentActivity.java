package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class DeleteStudentActivity extends AppCompatActivity {

    private ListView studentListView;
    private List<Student> studentList;
    private StudentListAdapter adapter;
    private List<Boolean> selectedItems;
    private Button deleteSelectedButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_student);

        studentList = (List<Student>) getIntent().getSerializableExtra("students");
        if (studentList == null) {
            studentList = new ArrayList<>();
        }

        selectedItems = new ArrayList<>();
        for (int i = 0; i < studentList.size(); i++) {
            selectedItems.add(false);
        }

        studentListView = findViewById(R.id.deleteStudentListView);
        deleteSelectedButton = findViewById(R.id.deleteSelectedButton);

        adapter = new StudentListAdapter(this, R.layout.student_list_item, studentList);
        studentListView.setAdapter(adapter);

        deleteSelectedButton.setOnClickListener(v -> {
            for (int i = selectedItems.size() - 1; i >= 0; i--) {
                if (selectedItems.get(i)) {
                    studentList.remove(i);
                    selectedItems.remove(i);
                }
            }
            adapter.notifyDataSetChanged();

            // Truyền danh sách đã cập nhật về ClassDetailActivity
            Intent resultIntent = new Intent();
            resultIntent.putExtra("updatedStudents", (ArrayList<Student>) studentList);
            setResult(RESULT_OK, resultIntent);
            finish();
        });

    }

    class StudentListAdapter extends ArrayAdapter<Student> {
        public StudentListAdapter(DeleteStudentActivity context, int resource, List<Student> students) {
            super(context, resource, students);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                view = getLayoutInflater().inflate(R.layout.student_list_view_delete, null);
            }

            Student student = getItem(position);
            if (student != null) {
                TextView idTextView = view.findViewById(R.id.studentID);
                TextView nameTextView = view.findViewById(R.id.studentName);
                TextView yearTextView = view.findViewById(R.id.studentYear);
                CheckBox checkBox = view.findViewById(R.id.studentCheckBox);

                if (idTextView != null) idTextView.setText(String.valueOf(student.getStudentID()));
                if (nameTextView != null) nameTextView.setText(student.getStudentName());
                if (yearTextView != null) yearTextView.setText(student.getYearBirth());

                // Sử dụng checkBox
                if (checkBox != null) {
                    checkBox.setChecked(selectedItems.get(position));
                    checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> selectedItems.set(position, isChecked));
                }
            }

            return view;
        }
    }
}
