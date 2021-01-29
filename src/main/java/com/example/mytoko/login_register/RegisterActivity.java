package com.example.mytoko.login_register;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.mytoko.R;
import com.example.mytoko.db.SqliteHelperUser;
import com.example.mytoko.entity.User;
import com.google.android.material.snackbar.Snackbar;

public class RegisterActivity extends AppCompatActivity {
    EditText etEmail;
    EditText etNamaL;
    EditText etPassword;
    Button btRegister;
    SqliteHelperUser sqliteHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        sqliteHelper = new SqliteHelperUser(this);
        initViews();
        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()) {
                    String Email = etEmail.getText().toString();
                    String Nama = etNamaL.getText().toString();
                    String Password = etPassword.getText().toString();

                    if (!sqliteHelper.isEmailExists(Email)) {
                        sqliteHelper.addUser(new User(null, Email, Nama, Password));
                        Snackbar.make(btRegister, "Berhasil membuat akun, silahkan login", Snackbar.LENGTH_LONG).show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                            }
                        }, Snackbar.LENGTH_LONG);
                    }else {
                        Snackbar.make(btRegister, "Email sudah digunakan", Snackbar.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    private void initViews() {
        etEmail = findViewById(R.id.et_email);
        etNamaL = findViewById(R.id.et_NamaLengkap);
        etPassword = findViewById(R.id.et_password);
        btRegister = findViewById(R.id.bt_register);
    }

    public boolean validate() {
        boolean valid = false;

        String Email = etEmail.getText().toString();
        String Nama = etNamaL.getText().toString();
        String Password = etPassword.getText().toString();

        if (TextUtils.isEmpty(Email)){
            valid = false;
            etEmail.setError("Email tidak boleh kosong!");
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
            valid = false;
            etEmail.setError("Harap masukkan email yang valid!");
        } else if (TextUtils.isEmpty(Nama)){
            valid = false;
            etNamaL.setError("Nama Lengkap tidak boleh kosong!");
        } else if (TextUtils.isEmpty(Password)) {
            valid = false;
            etPassword.setError("Password tidak boleh kosong!");
        } else if (Password.length() < 6) {
            valid = false;
            etPassword.setError("Password harus 6 karakter atau lebih");
        } else {
            valid = true;
            etPassword.setError(null);
            etEmail.setError(null);
        }
        return valid;
    }

    public void open_login(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}