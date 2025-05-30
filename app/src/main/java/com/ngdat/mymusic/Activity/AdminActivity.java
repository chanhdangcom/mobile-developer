package com.ngdat.mymusic.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ngdat.mymusic.Adapter.UserAdapter;
import com.ngdat.mymusic.Model.User;
import com.ngdat.mymusic.R;
import com.ngdat.mymusic.utils.DatabaseHelper;

import java.util.List;

public class AdminActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<User> userList;
    private DatabaseHelper dbHelper;
    private Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        recyclerView = findViewById(R.id.myRecycleTaiKhoan);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dbHelper = new DatabaseHelper(this);
        userList = dbHelper.getAllUsers();

        userAdapter = new UserAdapter(this, userList);
        recyclerView.setAdapter(userAdapter);

        btnLogout = findViewById(R.id.btnLogout);  // Đảm bảo gọi đúng findViewById
        btnLogout.setOnClickListener(view -> {
            logout();
        });
    }
    @Override
    protected void onPause() {
        super.onPause();
        logout();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        logout();
    }
    private void logout() {

        Intent intent = new Intent(AdminActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();  // Đóng AdminActivity
    }
}
