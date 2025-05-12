package com.ngdat.mymusic.Activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ngdat.mymusic.R;

public class SignupActivity extends AppCompatActivity {
    EditText edtFullName, edtUsername, edtEmail, edtPassword, edtConfirmPassword;
    Button btnRegister, btnLogin;
    Spinner spinnerRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

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
        btnLogin = findViewById(R.id.btnLogin);
        spinnerRole = findViewById(R.id.spinnerRole);
    }

    private void handleEvents() {
        btnRegister.setOnClickListener(view -> registerUser());// Gọi registerUser() để dùng SharedPreferences lưu
        btnLogin.setOnClickListener(view -> finish()); // Quay lại LoginActivity
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

        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("fullName", fullName);
        editor.putString("username", username);
        editor.putString("email", email);
        editor.putString("password", password);
        editor.putString("role", selectedRole);
        editor.apply();

        Toast.makeText(this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
        finish();
    }

}
