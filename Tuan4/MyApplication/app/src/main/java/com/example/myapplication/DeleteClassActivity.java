package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DeleteClassActivity extends AppCompatActivity {

    private ListView deleteClassListView;
    private List<Class> classList;  // Danh sách lớp học
    private ClassListAdapter adapter; // Adapter để hiển thị danh sách lớp
    private Button deleteSelectedButton; // Nút để xóa lớp đã chọn
    private List<Boolean> selectedItems; // Danh sách trạng thái được chọn của các lớp học

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_class);

        // Nhận danh sách lớp được truyền từ MainActivity
        classList = (ArrayList<Class>) getIntent().getSerializableExtra("classList");
        if (classList == null) {
            classList = new ArrayList<>();
        }

        deleteClassListView = findViewById(R.id.deleteClassListView);
        deleteSelectedButton = findViewById(R.id.deleteSelectedButton);

        // Khởi tạo danh sách selectedItems với giá trị false (không chọn)
        selectedItems = new ArrayList<>();
        for (int i = 0; i < classList.size(); i++) {
            selectedItems.add(false);
        }

        // Khởi tạo adapter và gán vào ListView
        adapter = new ClassListAdapter(this, R.layout.class_list_view_delete, classList);
        deleteClassListView.setAdapter(adapter);

        // Xử lý sự kiện khi bấm nút "Xóa lớp đã chọn"
        deleteSelectedButton.setOnClickListener(v -> {
            for (int i = selectedItems.size() - 1; i >= 0; i--) {
                if (selectedItems.get(i)) {
                    classList.remove(i);
                    selectedItems.remove(i);
                }
            }
            adapter.notifyDataSetChanged();
            // Trả lại dữ liệu cập nhật cho MainActivity
            Intent resultIntent = new Intent();
            resultIntent.putExtra("updatedClassList", (Serializable) classList);
            setResult(RESULT_OK, resultIntent);
            finish(); // Đóng DeleteClassActivity
        });

    }

    // Adapter cho danh sách lớp học
    class ClassListAdapter extends ArrayAdapter<Class> {
        private List<Class> classes;
        private int resource;

        public ClassListAdapter(DeleteClassActivity context, int resource, List<Class> classes) {
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

            Class currentClass = getItem(position);

            if (currentClass != null) {
                TextView idTextView = v.findViewById(R.id.ID);
                TextView nameTextView = v.findViewById(R.id.classname);
                CheckBox checkBox = v.findViewById(R.id.classCheckBox);

                if (idTextView != null) idTextView.setText(String.valueOf(currentClass.getID()));
                if (nameTextView != null) nameTextView.setText(currentClass.getClassName());

                // Xử lý CheckBox cho mỗi hàng
                if (checkBox != null) {
                    checkBox.setOnCheckedChangeListener(null); // Để tránh tái sử dụng View
                    checkBox.setChecked(selectedItems.get(position));

                    // Thiết lập sự kiện khi chọn/deselect CheckBox
                    checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> selectedItems.set(position, isChecked));
                }
            }
            return v;
        }
    }
}
