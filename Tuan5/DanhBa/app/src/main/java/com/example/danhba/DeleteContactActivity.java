package com.example.danhba;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class DeleteContactActivity extends AppCompatActivity {
    private ListView contactListView;
    private CustomDeleteAdapter adapter;
    private ArrayList<Contact> contactList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_contact);

        contactListView = findViewById(R.id.contactListView);
        contactList = new ArrayList<>();

        // Load danh bạ
        loadContacts();

        // Thiết lập adapter
        adapter = new CustomDeleteAdapter(this, contactList);
        contactListView.setAdapter(adapter);

        // Nút xóa danh bạ đã chọn
        Button deleteButton = findViewById(R.id.btnDelete);
        deleteButton.setOnClickListener(v -> deleteSelectedContacts());
    }

    // Hàm load danh bạ
    private void loadContacts() {
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

        adapter = new CustomDeleteAdapter(this, contactList);
        contactListView.setAdapter(adapter);
    }

    // Hàm xóa danh bạ đã chọn
    private void deleteSelectedContacts() {
        List<Contact> selectedContacts = adapter.getSelectedContacts();

        ContentResolver contentResolver = getContentResolver();

        for (Contact contact : selectedContacts) {
            // Tìm và xóa danh bạ dựa trên ID
            Uri contactUri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, contact.getId());
            Cursor cursor = contentResolver.query(contactUri, null, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                // Lấy lookupKey để xác định danh bạ cần xóa
                String lookupKey = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
                Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_LOOKUP_URI, lookupKey);
                // Xóa danh bạ
                contentResolver.delete(uri, null, null);
            }

            if (cursor != null) {
                cursor.close();
            }
        }

        // Thông báo và cập nhật danh sách
        Toast.makeText(this, "Danh bạ đã được xóa!", Toast.LENGTH_SHORT).show();
        finish();
    }
}

