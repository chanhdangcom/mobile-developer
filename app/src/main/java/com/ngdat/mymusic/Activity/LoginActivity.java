package com.ngdat.mymusic.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ngdat.mymusic.R;
import com.ngdat.mymusic.utils.DatabaseHelper;

public class LoginActivity extends AppCompatActivity {
    EditText edtUsername, edtPassword;
    Button btnLogin;
    TextView btnSignup;

    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnSignup = findViewById(R.id.btnSignup);

        dbHelper = new DatabaseHelper(this);

        btnLogin.setOnClickListener(v -> loginUser());

        btnSignup.setOnClickListener(v -> {
            startActivity(new Intent(this, SignupActivity.class));
        });
    }

    private void loginUser() {
        String username = edtUsername.getText().toString().trim();
        String password = edtPassword.getText().toString();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        Cursor cursor = dbHelper.getUser(username, password);
        if (cursor != null && cursor.moveToFirst()) {
            int userId = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            String role = cursor.getString(cursor.getColumnIndexOrThrow("role"));
            Toast.makeText(this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();

            // Lưu thông tin người dùng
            SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("userId", userId);
            editor.apply();

            // Chuyển đến giao diện phù hợp với vai trò
            if (role.equalsIgnoreCase("admin")) {
                startActivity(new Intent(this, AdminActivity.class));
            } else {
                startActivity(new Intent(this, MainActivity.class));
            }

            finish();
        } else {
            Toast.makeText(this, "Sai tên đăng nhập hoặc mật khẩu", Toast.LENGTH_SHORT).show();
        }

        if (cursor != null) cursor.close();
    }
}