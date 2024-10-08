package com.example.danhba;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ListView contactListView;
    private CustomAdapter adapter;
    private ArrayList<Contact> contactList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contactListView = findViewById(R.id.contactListView);
        contactList = new ArrayList<>();

        // Kiểm tra quyền truy cập danh bạ
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CONTACTS)
                        != PackageManager.PERMISSION_GRANTED) {

            // Yêu cầu cả quyền READ_CONTACTS và WRITE_CONTACTS
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS}, 1);
        } else {
            // Nếu đã có đủ quyền, load danh bạ
            loadContacts();
        }

        // Xử lý sự kiện khi người dùng click vào một item trong ListView
        contactListView.setOnItemClickListener((parent, view, position, id) -> {
            Contact selectedContact = contactList.get(position);
            Intent intent = new Intent(MainActivity.this, UpdateContactActivity.class);

            // Truyền dữ liệu qua UpdateContactActivity
            intent.putExtra("contact_name", selectedContact.getName());
            intent.putExtra("contact_phone", selectedContact.getPhone());
            intent.putExtra("contact_id", selectedContact.getId());  // Thêm ID cho việc cập nhật
            startActivity(intent);
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults.length > 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            loadContacts();
        }
    }

    private void loadContacts() {
        contactList.clear();  // Xóa danh sách cũ để tránh trùng lặp

        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);

        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                String contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                Cursor phoneCursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                        new String[]{contactId}, null);

                if (phoneCursor != null && phoneCursor.moveToFirst()) {
                    String phoneNumber = phoneCursor.getString(
                            phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                    // Thêm danh bạ vào danh sách với ID
                    contactList.add(new Contact(contactId, contactName, phoneNumber));
                }
                if (phoneCursor != null) {
                    phoneCursor.close();
                }
            }
            cursor.close();
        }

        // Cập nhật lại adapter với danh sách mới
        adapter = new CustomAdapter(this, contactList);
        contactListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();  // Thông báo cho adapter rằng dữ liệu đã thay đổi
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        // Chuyển đến các Activity tương ứng
        if (id == R.id.menu_add_contact) {
            Intent intent = new Intent(MainActivity.this, AddContactActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.menu_delete_contact) {
            Intent intent = new Intent(MainActivity.this, DeleteContactActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onResume() {
        super.onResume();
        // Tải lại danh bạ khi Activity quay lại
        loadContacts();
    }
}

