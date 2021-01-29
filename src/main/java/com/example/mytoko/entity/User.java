package com.example.mytoko.entity;

public class User {
    public String id;
    public String email;
    public String nama_lengkap;
    public String password;

    public User(String id, String email, String nama_lengkap, String password) {
        this.id = id;
        this.email = email;
        this.nama_lengkap = nama_lengkap;
        this.password = password;
    }
}
