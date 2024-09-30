package com.example.myapplication;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.example.myapplication.R;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView classListView;
    private List<Class> classList;
    private ClassListAdapter adapter;
    private static final int REQUEST_CODE_ADD_CLASS = 1;
    private static final int REQUEST_CODE_DELETE_CLASS = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        classListView = findViewById(R.id.classListView);

        // Tạo danh sách sv của lớp
        List<Student> studentsClass1 = new ArrayList<>();
        studentsClass1.add(new Student(1, "Student A","2004"));
        studentsClass1.add(new Student(2, "Student b","2006"));

        List<Student> studentsClass2 = new ArrayList<>();
        studentsClass2.add(new Student(3, "Student c","2003"));
        studentsClass2.add(new Student(4, "Student d","2005"));

        List<Student> studentsClass3 = new ArrayList<>();
        studentsClass3.add(new Student(5, "Student e","2004"));
        studentsClass3.add(new Student(6, "Student f","2005"));

        // Tạo ds lớp

        classList = new ArrayList<>();
        classList.add(new Class(1, "IT003", studentsClass1));
        classList.add(new Class(2, "IT005", studentsClass2));
        classList.add(new Class(3, "IT007", studentsClass3));

        adapter = new ClassListAdapter(this, R.layout.class_list_item, classList);
        classListView.setAdapter(adapter);

        classListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Lấy đối tượng Class được click
                Class selectedClass = (Class) parent.getItemAtPosition(position);

                if (selectedClass != null) {
                    // Chuyển sang trang chi tiết và truyền dữ liệu sang
                    Intent intent = new Intent(MainActivity.this, ClassDetailActivity.class);
                    intent.putExtra("classId", selectedClass.getID());
                    intent.putExtra("className", selectedClass.getClassName());
                    intent.putExtra("memberCount", selectedClass.getStudents().size());

                    // Truyền danh sách sinh viên dưới dạng List
                    List<Student> students = selectedClass.getStudents();
                    intent.putExtra("students", (Serializable) students);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.class_menu, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_ADD_CLASS && resultCode == RESULT_OK && data != null) {
            // Nhận dữ liệu từ AddClassActivity
            int classId = data.getIntExtra("classId", 0);
            String className = data.getStringExtra("className");

            // Thêm lớp mới vào danh sách
            List<Student> students = new ArrayList<>(); // Danh sách sinh viên rỗng cho lớp mới
            Class newClass = new Class(classId, className, students);
            classList.add(newClass);

            // Cập nhật lại ListView
            adapter.notifyDataSetChanged();
        }
        else if (requestCode == REQUEST_CODE_DELETE_CLASS && resultCode == RESULT_OK && data != null) {
            classList = (ArrayList<Class>) data.getSerializableExtra("updatedClassList");

            // Cập nhật lại Adapter với danh sách lớp mới
            adapter = new ClassListAdapter(this, R.layout.class_list_item, classList);
            classListView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.add_class) {
            // Chuyển sang Activity thêm lớp
            Intent addClassIntent = new Intent(MainActivity.this, AddClassActivity.class);
            startActivityForResult(addClassIntent, REQUEST_CODE_ADD_CLASS);
            return true;
        } else if (id == R.id.delete_class) {
            // Chuyển sang Activity xóa lớp
            Intent deleteClassIntent = new Intent(MainActivity.this, DeleteClassActivity.class);
            deleteClassIntent.putExtra("classList", (Serializable) classList); // Truyền dữ liệu dưới dạng Serializable
            startActivityForResult(deleteClassIntent, REQUEST_CODE_DELETE_CLASS);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}
    class ClassListAdapter extends ArrayAdapter<Class> {
    int resource;
    private List<Class> classes;

    public ClassListAdapter(Context context, int resource, List<Class> classes) {
        super(context, resource, classes);
        this.classes = classes;
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
        Class s = getItem(position);

        if (s != null) {
            TextView idTextView = v.findViewById(R.id.ID);
            TextView nameTextView = v.findViewById(R.id.classname);

            if (idTextView != null) idTextView.setText(String.valueOf(s.getID()));
            if (nameTextView != null) nameTextView.setText(s.getClassName());
        }
        return v;
    }
}


