package com.example.hungthinhapartmentmanagement.Activity;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hungthinhapartmentmanagement.Adapter.ParkingAdapter;
import com.example.hungthinhapartmentmanagement.Model.ParkingModel;
import com.example.hungthinhapartmentmanagement.R;

import java.util.ArrayList;
import java.util.List;

public class Parking extends AppCompatActivity {

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_information);
        //setSupportActionBar(findViewById(R.id.toolbar));

        RecyclerView recyclerView = findViewById(R.id.rvParking);
        List<ParkingModel> list = new ArrayList<>();
        list.add(new ParkingModel(R.drawable.ic_car, "Tên nhà xe: Bãi xe B2", "Tối đa: 50 xe", "Số chỗ còn: 20"));
        list.add(new ParkingModel(R.drawable.ic_car, "Tên nhà xe: Bãi xe B3", "Tối đa: 50 xe", "Số chỗ còn: 40"));
        list.add(new ParkingModel(R.drawable.ic_bike, "Tên nhà xe: Bãi xe C", "Tối đa: 200 xe", "Số chỗ còn: 50"));
        list.add(new ParkingModel(R.drawable.ic_bike, "Tên nhà xe: Bãi xe B1", "Tối đa: 200 xe", "Số chỗ còn: 20"));

        ParkingAdapter adapter = new ParkingAdapter(list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
}
