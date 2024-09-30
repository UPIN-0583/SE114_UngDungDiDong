package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ClassDetailActivity extends AppCompatActivity {

    private TextView classNameTextView, classIdTextView, memberCountTextView;
    private ListView studentListView;
    private List<Student> students;
    private StudentListAdapter adapter;
    private static final int REQUEST_CODE_ADD_STUDENT = 1;
    private static final int REQUEST_CODE_DELETE_STUDENT = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_detail);

        // Initialize the views
        classNameTextView = findViewById(R.id.className);
        classIdTextView = findViewById(R.id.classId);
        memberCountTextView = findViewById(R.id.memberCount);
        studentListView = findViewById(R.id.studentListView);

        // Lấy dữ liệu từ Intent
        int classId = getIntent().getIntExtra("classId", 0);
        String className = getIntent().getStringExtra("className");
        int memberCount = getIntent().getIntExtra("memberCount", 0);
        students = (List<Student>) getIntent().getSerializableExtra("students");

        if (students == null) {
            students = new ArrayList<>();
        }

        // Set text for TextViews
        classIdTextView.setText("Class ID: " + classId);
        classNameTextView.setText("Class Name: " + className);
        memberCountTextView.setText("Members: " + memberCount);

        // Initialize and set the adapter
        adapter = new StudentListAdapter(this, R.layout.student_list_item, students);
        studentListView.setAdapter(adapter);
    }


    // Adapter cho danh sách học sinh
    class StudentListAdapter extends ArrayAdapter<Student> {
        private List<Student> students;
        private int resource;

        public StudentListAdapter(ClassDetailActivity context, int resource, List<Student> students) {
            super(context, resource, students);
            this.students = students;
            this.resource = resource;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater vi;
                vi = LayoutInflater.from(this.getContext());
                v = vi.inflate(this.resource, null);
            }
            Student s = getItem(position);

            if (s != null) {
                TextView idTextView = v.findViewById(R.id.studentID);
                TextView nameTextView = v.findViewById(R.id.studentName);
                TextView yearTextView = v.findViewById(R.id.studentYear);

                if (idTextView != null) idTextView.setText(String.valueOf(s.getStudentID()));
                if (nameTextView != null) nameTextView.setText(s.getStudentName());
                if (yearTextView != null) yearTextView.setText(s.getYearBirth());
            }
            return v;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.student_menu, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_ADD_STUDENT && resultCode == RESULT_OK && data != null) {
            int studentId = data.getIntExtra("studentId", 0);
            String studentName = data.getStringExtra("studentName");
            String studentYear = data.getStringExtra("studentYear");

            // Thêm học sinh mới vào danh sách
            Student newStudent = new Student(studentId, studentName, studentYear);
            students.add(newStudent);

            // Cập nhật lại ListView
            adapter.notifyDataSetChanged();
            memberCountTextView.setText("Members: " + students.size());

        } else if (requestCode == REQUEST_CODE_DELETE_STUDENT && resultCode == RESULT_OK && data != null) {
            // Nhận danh sách cập nhật từ DeleteStudentActivity
            List<Student> updatedStudents = (List<Student>) data.getSerializableExtra("updatedStudents");
            if (updatedStudents != null) {
                students.clear();  // Xóa danh sách hiện tại
                students.addAll(updatedStudents);  // Thêm danh sách cập nhật

                // Cập nhật lại ListView
                adapter.notifyDataSetChanged();
                memberCountTextView.setText("Members: " + students.size());
            }
        }
    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.add_student) {
            // Chuyển sang Activity thêm học sinh
            Intent addStudentIntent = new Intent(ClassDetailActivity.this, AddStudentActivity.class);
            startActivityForResult(addStudentIntent, REQUEST_CODE_ADD_STUDENT);
            return true;
        } else if (id == R.id.delete_student) {
            // Chuyển sang Activity xóa học sinh
            Intent deleteStudentIntent = new Intent(ClassDetailActivity.this, DeleteStudentActivity.class);
            deleteStudentIntent.putExtra("students", (Serializable) students); // Truyền danh sách học sinh
            startActivityForResult(deleteStudentIntent, REQUEST_CODE_DELETE_STUDENT);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }


}
