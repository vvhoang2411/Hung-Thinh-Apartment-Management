package com.example.hungthinhapartmentmanagement.Activity;

import android.os.Bundle;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.hungthinhapartmentmanagement.R;

import java.util.ArrayList;

public class ResidentManagement extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_resident_management);

        ArrayList<Person> residentList = new ArrayList<>();
        residentList.add(new Person("Nguyễn Minh Tuấn", "P201", "0345751602", "minhtuand5@gmail.com", "minhtuanc123@"));
        residentList.add(new Person("Hoàng Đức Huy", "P204", "0375819810", "huyhhoangg@gmail.com", "huyhhoang123@"));
        residentList.add(new Person("Ninh Quang Khải", "P202", "0399588222", "nqk152004@gmail.com", "quangkhai123@"));

        ListView listViewResults = findViewById(R.id.listViewResults);
        MyResidentAdapter myResidentAdapter = new MyResidentAdapter(this, android.R.layout.simple_list_item_1, residentList);
        listViewResults.setAdapter(myResidentAdapter);


    }
}