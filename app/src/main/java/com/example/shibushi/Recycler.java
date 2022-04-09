package com.example.shibushi.cart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.shibushi.R;

import java.util.ArrayList;
import java.util.List;

public class Recycler extends AppCompatActivity {

    RecyclerView recyclerView;
    ContactAdapter contactAdapter;
    Model.DataSource datasource;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // hard code the datasource
        List<Model.Contact> contacts = new ArrayList<>();
        //contacts.add(new Model.Contact(image));

        datasource = new Model.DataSource(contacts);
        recyclerView = findViewById(R.id.myRecyclerView);
        render();
    }

    private void render() {
        contactAdapter = new ContactAdapter(Recycler.this, datasource);
        recyclerView.setAdapter(contactAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(Recycler.this));
    }
}