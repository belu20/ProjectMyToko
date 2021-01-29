package com.example.mytoko.helper;

import android.database.Cursor;

import com.example.mytoko.db.DatabaseContract;
import com.example.mytoko.entity.Note;

import java.util.ArrayList;

public class MappingHelper {
    public static ArrayList<Note> mapCursorToArrayList(Cursor notesCursor){
        ArrayList<Note> notesList = new ArrayList<>();
        while (notesCursor.moveToNext()){
            int id = notesCursor.getInt(notesCursor.getColumnIndexOrThrow(DatabaseContract.NoteColumns._ID));
            String judul_buku = notesCursor.getString(notesCursor.getColumnIndexOrThrow(DatabaseContract.NoteColumns.JUDUL_BUKU));
            String harga = notesCursor.getString(notesCursor.getColumnIndexOrThrow(DatabaseContract.NoteColumns.HARGA));
            String deskripsi = notesCursor.getString(notesCursor.getColumnIndexOrThrow(DatabaseContract.NoteColumns.DESKRIPSI));
            String date = notesCursor.getString(notesCursor.getColumnIndexOrThrow(DatabaseContract.NoteColumns.DATE));
            notesList.add(new Note(id, judul_buku, harga, deskripsi, date));
        }
        return notesList;
    }

}
