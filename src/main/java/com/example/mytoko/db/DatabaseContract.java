package com.example.mytoko.db;

import android.provider.BaseColumns;

public class DatabaseContract {
    static String TABLE_NAME = "buku";

    public static final class NoteColumns implements BaseColumns {
        public static String JUDUL_BUKU = "judul_buku";
        public static String HARGA = "harga";
        public static String DESKRIPSI = "deskripsi";
        public static String DATE = "date";
    }
}
