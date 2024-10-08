package com.example.danhba;

import android.content.ContentProviderOperation;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class UpdateContactActivity extends AppCompatActivity {
    private EditText nameInput, phoneInput;
    private Button updateButton;
    private String contactId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_contact);

        nameInput = findViewById(R.id.nameInput);
        phoneInput = findViewById(R.id.phoneInput);
        updateButton = findViewById(R.id.updateButton);

        // Nhận dữ liệu từ MainActivity khi người dùng chọn một item
        Intent intent = getIntent();
        String currentName = intent.getStringExtra("contact_name");
        String currentPhone = intent.getStringExtra("contact_phone");
        contactId = intent.getStringExtra("contact_id");

        // Hiển thị tên và số điện thoại hiện tại trong EditText
        nameInput.setText(currentName);
        phoneInput.setText(currentPhone);

        // Cập nhật thông tin khi bấm nút "Update"
        updateButton.setOnClickListener(v -> updateContact());
    }

    private void updateContact() {
        String newName = nameInput.getText().toString();
        String newPhone = phoneInput.getText().toString();

        // Cập nhật tên và số điện thoại trong danh bạ
        ArrayList<ContentProviderOperation> ops = new ArrayList<>();

        // Cập nhật tên
        ops.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
                .withSelection(ContactsContract.Data.CONTACT_ID + "=? AND " + ContactsContract.Data.MIMETYPE + "=?",
                        new String[]{contactId, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE})
                .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, newName)
                .build());

        // Cập nhật số điện thoại
        ops.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
                .withSelection(ContactsContract.Data.CONTACT_ID + "=? AND " + ContactsContract.Data.MIMETYPE + "=?",
                        new String[]{contactId, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE})
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, newPhone)
                .build());

        try {
            getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
            Toast.makeText(this, "Thông tin đã được cập nhật", Toast.LENGTH_SHORT).show();
            finish(); // Quay lại MainActivity sau khi cập nhật
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Cập nhật không thành công", Toast.LENGTH_SHORT).show();
        }
    }
}
