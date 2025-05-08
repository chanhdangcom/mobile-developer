package com.ngdat.mymusic.Activity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ngdat.mymusic.Adapter.UserAdapter;
import com.ngdat.mymusic.Model.User;
import com.ngdat.mymusic.R;
import com.ngdat.mymusic.utils.UserDatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class AdminActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<User> userList;
    private UserDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin); // layout chứa RecyclerView

        recyclerView = findViewById(R.id.myRecycleTaiKhoan);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dbHelper = new UserDatabaseHelper(this);
        userList = dbHelper.getAllUsers();

        userAdapter = new UserAdapter(this, userList);
        recyclerView.setAdapter(userAdapter);
    }

}
