package com.example.danhba;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class CustomAdapter extends ArrayAdapter<Contact> {
    private Context context;
    private List<Contact> contacts;

    public CustomAdapter(Context context, List<Contact> contacts) {
        super(context, 0, contacts);
        this.context = context;
        this.contacts = contacts;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Kiểm tra nếu view chưa được tái sử dụng, tạo view mới
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.contact_item, parent, false);
        }

        // Lấy contact hiện tại
        Contact currentContact = contacts.get(position);

        // Gán dữ liệu vào các TextView trong layout
        TextView tvIndex = convertView.findViewById(R.id.tvIndex);
        TextView tvName = convertView.findViewById(R.id.tvName);
        TextView tvPhone = convertView.findViewById(R.id.tvPhone);

        // Gán thông tin số thứ tự, tên và số điện thoại
        tvIndex.setText(String.valueOf(position + 1)); // Số thứ tự
        tvName.setText(currentContact.getName());      // Tên
        tvPhone.setText(currentContact.getPhone());    // Số điện thoại

        return convertView;
    }
}

