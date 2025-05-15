package com.ngdat.mymusic.Activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ngdat.mymusic.utils.DatabaseHelper;
import com.ngdat.mymusic.R;

public class SignupActivity extends AppCompatActivity {
    EditText edtFullName, edtUsername, edtEmail, edtPassword, edtConfirmPassword;
    Button btnRegister;
    TextView btnLogin;
    Spinner spinnerRole;

    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        dbHelper = new DatabaseHelper(this);
        initView();
        handleEvents();
    }

    private void initView() {
        edtFullName = findViewById(R.id.edtFullName);
        edtUsername = findViewById(R.id.edtUsername);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        btnLogin = findViewById(R.id.btnLogin); // TextView
        spinnerRole = findViewById(R.id.spinnerRole);
    }

    private void handleEvents() {
        btnRegister.setOnClickListener(view -> registerUser());
        btnLogin.setOnClickListener(view -> finish());
    }

    private void registerUser() {
        String fullName = edtFullName.getText().toString().trim();
        String username = edtUsername.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString();
        String confirmPassword = edtConfirmPassword.getText().toString();
        String selectedRole = spinnerRole.getSelectedItem().toString();

        if (fullName.isEmpty() || username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Mật khẩu xác nhận không khớp", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedRole.equals("Chọn quyền")) {
            Toast.makeText(this, "Vui lòng chọn quyền", Toast.LENGTH_SHORT).show();
            return;
        }

        if (dbHelper.checkUsernameExists(username)) {
            Toast.makeText(this, "Tên người dùng đã tồn tại", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean success = dbHelper.insertUser(fullName, username, email, password, selectedRole);
        if (success) {
            Toast.makeText(this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
            finish(); // Quay về LoginActivity
        } else {
            Toast.makeText(this, "Đăng ký thất bại!", Toast.LENGTH_SHORT).show();
        }
    }
}