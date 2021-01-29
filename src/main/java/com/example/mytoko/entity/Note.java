package com.example.mytoko.entity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

public class Note implements Parcelable {
    private int id;
    private String judul_buku;
    private String harga;
    private String deskripsi;
    private String date;

    public Note(int id, String judul_buku, String harga, String deskripsi, String date){
        this.id = id;
        this.judul_buku = judul_buku;
        this.harga = harga;
        this.deskripsi = deskripsi;
        this.date = date;
    }

    public Note(){}

    protected Note(Parcel in) {
        id = in.readInt();
        judul_buku = in.readString();
        harga = in.readString();
        deskripsi = in.readString();
        date = in.readString();
    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getJudulBuku() {
        return judul_buku;
    }

    public void setJudulBuku(String judul_buku) {
        this.judul_buku = judul_buku;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(judul_buku);
        parcel.writeString(harga);
        parcel.writeString(deskripsi);
        parcel.writeString(date);
    }
}