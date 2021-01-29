package com.example.mytoko.ui.home.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mytoko.R;
import com.example.mytoko.db.DatabaseContract;
import com.example.mytoko.db.NoteHelper;
import com.example.mytoko.entity.Note;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.provider.CallLog.Calls.DATE;

public class ProsesScrudActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText etJudulBuku, etHarga, etDeksripsi;
    private Button btSubmit;
    private boolean isEdit = false;
    private Note note;
    private int position;
    private NoteHelper noteHelper;

    public static final String EXTRA_NOTE = "extra_note";
    public static final String EXTRA_POSITION = "extra_position";
    public static final int REQUEST_ADD = 100;
    public static final int RESULT_ADD = 101;
    public static final int REQUEST_UPDATE = 200;
    public static final int RESULT_UPDATE = 201;
    public static final int RESULT_DELETE = 301;
    private final int ALERT_DIALOG_CLOSE = 10;
    private final int ALERT_DIALOG_DELETE = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proses_scrud);
        etJudulBuku = findViewById(R.id.et_judulbuku);
        etHarga = findViewById(R.id.et_harga);
        etDeksripsi = findViewById(R.id.et_deskripsi);
        btSubmit = findViewById(R.id.bt_submit);
        noteHelper = NoteHelper.getInstance(getApplicationContext());
        noteHelper.open();
        note = getIntent().getParcelableExtra(EXTRA_NOTE);
        if (note != null) {
            position = getIntent().getIntExtra(EXTRA_POSITION, 0);
            isEdit = true;
        } else {
            note = new Note();
        }
        String actionBarTitle;
        String btTitle;
        if (isEdit) {
            actionBarTitle = "Ubah Data";
            btTitle = "Update";
            if (note != null) {
                etJudulBuku.setText(note.getJudulBuku());
                etHarga.setText(note.getHarga());
                etDeksripsi.setText(note.getDeskripsi());
            }
        } else {
            actionBarTitle = "Input Data";
            btTitle = "Simpan";
        }
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(actionBarTitle);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        btSubmit.setText(btTitle);
        btSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.bt_submit) {
            String judul_buku = etJudulBuku.getText().toString().trim();
            String harga = etHarga.getText().toString().trim();
            String deskripsi = etDeksripsi.getText().toString().trim();
            if (TextUtils.isEmpty(judul_buku)) {
                etJudulBuku.setError("Judul Buku tidak boleh kosong");
                return;
            } else if (TextUtils.isEmpty(harga)) {
                etHarga.setError("Harga tidak boleh kosong");
                return;
            } else if (TextUtils.isEmpty(deskripsi)) {
                etDeksripsi.setError("Deskripsi tidak boleh kosong");
                return;
            }
            note.setJudulBuku(judul_buku);
            note.setHarga(harga);
            note.setDeskripsi(deskripsi);
            Intent intent = new Intent();
            intent.putExtra(EXTRA_NOTE, note);
            intent.putExtra(EXTRA_POSITION, position);

            ContentValues values = new ContentValues();
            values.put(DatabaseContract.NoteColumns.JUDUL_BUKU, judul_buku);
            values.put(DatabaseContract.NoteColumns.HARGA, harga);
            values.put(DatabaseContract.NoteColumns.DESKRIPSI, deskripsi);

            if (isEdit) {
                long result = noteHelper.update(String.valueOf(note.getId()), values);
                if (result > 0) {
                    setResult(RESULT_UPDATE, intent);
                    finish();
                } else {
                    Toast.makeText(ProsesScrudActivity.this, "Gagal mengupdate data", Toast.LENGTH_SHORT).show();
                }
            } else {
                note.setDate(getCurrentDate());
                values.put(DATE, getCurrentDate());
                long result = noteHelper.insert(values);
                if (result > 0) {
                    note.setId((int) result);
                    setResult(RESULT_ADD, intent);
                    finish();
                } else {
                    Toast.makeText(ProsesScrudActivity.this, "Gagal menambah data", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private String getCurrentDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (isEdit) {
            getMenuInflater().inflate(R.menu.delete_menu, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_delete) {
            showAlertDialog(ALERT_DIALOG_DELETE);
        } else if (id == android.R.id.home) {
            showAlertDialog(ALERT_DIALOG_CLOSE);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        showAlertDialog(ALERT_DIALOG_CLOSE);
    }

    private void showAlertDialog(int type) {
        final boolean isDialogClose = type == ALERT_DIALOG_CLOSE;
        String dialogTitle, dialogMessage;
        if (isDialogClose) {
            dialogTitle = "Batal";
            dialogMessage = "Apakah anda ingin membatalkan perubahan pada form?";
        } else {
            dialogMessage = "Apakah anda yakin ingin menghapus item ini?";
            dialogTitle = "Hapus Note";
        }
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(dialogTitle);
        alertDialogBuilder
                .setMessage(dialogMessage)
                .setCancelable(false)
                .setPositiveButton("Ya", (dialog, id) -> {
                    if (isDialogClose) {
                        finish();
                    } else {
                        long result = noteHelper.deleteById(String.valueOf(note.getId()));
                        if (result > 0) {
                            Intent intent = new Intent();
                            intent.putExtra(EXTRA_POSITION, position);
                            setResult(RESULT_DELETE, intent);
                            finish();
                        } else {
                            Toast.makeText(ProsesScrudActivity.this, "Gagal menghapus data", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Tidak", (dialog, id) -> dialog.cancel());
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}