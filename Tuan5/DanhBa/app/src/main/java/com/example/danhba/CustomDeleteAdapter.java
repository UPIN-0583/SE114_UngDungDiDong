package com.example.danhba;
import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.danhba.Contact;

import java.util.ArrayList;
import java.util.List;

public class CustomDeleteAdapter extends ArrayAdapter<Contact> {
    private Context context;
    private List<Contact> contacts;
    private SparseBooleanArray checkedItems = new SparseBooleanArray();  // Lưu trạng thái checkbox

    public CustomDeleteAdapter(Context context, List<Contact> contacts) {
        super(context, 0, contacts);
        this.context = context;
        this.contacts = contacts;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.contact_item_delete, parent, false);
        }

        // Lấy contact hiện tại
        Contact currentContact = contacts.get(position);

        // Gán dữ liệu vào các TextView trong layout
        TextView tvName = convertView.findViewById(R.id.tvName);
        TextView tvPhone = convertView.findViewById(R.id.tvPhone);
        CheckBox checkBox = convertView.findViewById(R.id.checkbox);

        // Gán tên và số điện thoại vào TextView
        tvName.setText(currentContact.getName());
        tvPhone.setText(currentContact.getPhone());

        // Thiết lập trạng thái của checkbox
        checkBox.setChecked(checkedItems.get(position, false));

        // Xử lý sự kiện khi checkbox được chọn hoặc bỏ chọn
        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                checkedItems.put(position, true);
            } else {
                checkedItems.delete(position);
            }
        });

        return convertView;
    }

    // Trả về các contact đã được chọn
    public List<Contact> getSelectedContacts() {
        List<Contact> selectedContacts = new ArrayList<>();
        for (int i = 0; i < checkedItems.size(); i++) {
            int position = checkedItems.keyAt(i);
            if (checkedItems.valueAt(i)) {
                selectedContacts.add(contacts.get(position));
            }
        }
        return selectedContacts;
    }
}
