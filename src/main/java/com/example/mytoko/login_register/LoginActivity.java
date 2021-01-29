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
import com.example.mytoko.ui.home.home.HomeActivity;
import com.google.android.material.snackbar.Snackbar;

public class LoginActivity extends AppCompatActivity {
    EditText etEmail;
    EditText etPassword;
    Button btLogin;
    SqliteHelperUser sqliteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sqliteHelper = new SqliteHelperUser(this);
        initViews();

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()) {
                    String email = etEmail.getText().toString();
                    String password = etPassword.getText().toString();
                    User currentUser = sqliteHelper.Authenticate(new User(null, email, null, password));
                    if (currentUser != null) {
                        Snackbar.make(btLogin, "Login berhasil!", Snackbar.LENGTH_LONG).show();

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run(){
                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        },200);
                    } else {
                        Snackbar.make(btLogin, "Login gagal, coba lagi", Snackbar.LENGTH_LONG).show();

                    }
                }
            }
        });
    }

    private void initViews() {
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        btLogin = findViewById(R.id.bt_login);
    }

    public boolean validate() {
        boolean valid = false;

        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)){
            valid = false;
            etEmail.setError("Email tidak boleh kosong!");
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            valid = false;
            etEmail.setError("Harap masukkan email yang valid!");
        } else if (TextUtils.isEmpty(password)) {
            valid = false;
            etPassword.setError("Password tidak boleh kosong!");
        } else if (password.length() < 6) {
            valid = false;
            etPassword.setError("Password harus 6 karakter atau lebih");
        } else {
            valid = true;
            etPassword.setError(null);
            etEmail.setError(null);
        }
        return valid;
    }

    @Override
    public void onBackPressed(){
        finish();
    }

    public void open_register(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
}
