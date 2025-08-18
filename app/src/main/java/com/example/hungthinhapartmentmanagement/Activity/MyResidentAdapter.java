package com.example.hungthinhapartmentmanagement.Activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.hungthinhapartmentmanagement.R;

import java.util.ArrayList;
import java.util.List;

public class MyResidentAdapter extends ArrayAdapter<Person> {

    private Context context;
    private ArrayList<Person> people;
    int idlayout;

    public MyResidentAdapter(Context context, int idlayout, ArrayList<Person> people) {
        super(context, idlayout, people);
        this.context = context;
        this.idlayout = idlayout;
        this.people = people;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        Person person = people.get(position);

        // Kiểm tra nếu convertView null, inflate layout mới
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_resident, parent, false);
        }

        TextView tvName = convertView.findViewById(R.id.tvName);
        TextView tvRoom = convertView.findViewById(R.id.tvRoom);
        TextView tvPhone = convertView.findViewById(R.id.tvPhone);
        TextView tvEmail = convertView.findViewById(R.id.tvEmail);

        tvName.setText(person.getName());
        tvRoom.setText(person.getRoom());
        tvPhone.setText(person.getPhone());
        tvEmail.setText(person.getEmail());

        return convertView;
    }

}
