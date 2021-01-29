package com.example.mytoko.ui.home.transaksi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mytoko.R;
import com.example.mytoko.db.NoteHelper;
import com.example.mytoko.entity.Note;

public class ProsesTransaksiActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText etJudulBuku, etHarga, etDeskripsi, etBayar;
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

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proses_transaksi);

        etJudulBuku = findViewById(R.id.et_judulbuku);
        etDeskripsi = findViewById(R.id.et_deskripsi);
        etHarga = findViewById(R.id.et_harga);
        etBayar = findViewById(R.id.et_bayar);
        btSubmit = findViewById(R.id.bt_submit);

        noteHelper = NoteHelper.getInstance(getApplicationContext());
        noteHelper.open();

        note = getIntent().getParcelableExtra(EXTRA_NOTE);
        if (note != null){
            position = getIntent().getIntExtra(EXTRA_POSITION, 0);
            isEdit = true;
        } else {
            note = new Note();
        }
        String actionBarTitle = null;
        String btTitle = null;
        if (isEdit){
            actionBarTitle = "Transaksi";
            btTitle = "Konfirmasi";
            if (note != null){
                etJudulBuku.setText(note.getJudulBuku());
                etHarga.setText(note.getHarga());
                etDeskripsi.setText(note.getDeskripsi());
            }
        }
        if (getSupportActionBar() != null){
            getSupportActionBar().setTitle(actionBarTitle);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        btSubmit.setText(btTitle);
        btSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        String etbayar = etBayar.getText().toString().trim();
        if (TextUtils.isEmpty(etbayar)){
            etBayar.setError("Bayar tidak boleh kosong!");
            return;
        }
        int harga = Integer.parseInt(etHarga.getText().toString());
        int bayar = Integer.parseInt(etBayar.getText().toString());
        int jumlah;
        if (bayar < harga){
            jumlah = harga-bayar;
            Toast.makeText(this, "Uang anda kurang Rp."+jumlah+", bayar ulang!", Toast.LENGTH_LONG).show();
        } else if (bayar > harga){
            jumlah = bayar-harga;
            Toast.makeText(this, "Kembalian anda Rp."+jumlah+", Transaksi sukses!", Toast.LENGTH_LONG).show();
            long result = noteHelper.deleteById(String.valueOf(note.getId()));
            if (result > 0) {
                Intent intent = new Intent();
                intent.putExtra(EXTRA_POSITION, position);
                setResult(RESULT_DELETE, intent);
                finish();
            } else {
                Toast.makeText(ProsesTransaksiActivity.this, "Transaksi gagal", Toast.LENGTH_SHORT).show();
            }
        } else{
            Toast.makeText(this, "Transaksi sukses", Toast.LENGTH_LONG).show();
            long result = noteHelper.deleteById(String.valueOf(note.getId()));
            if (result > 0) {
                Intent intent = new Intent();
                intent.putExtra(EXTRA_POSITION, position);
                setResult(RESULT_DELETE, intent);
                finish();
            } else {
                Toast.makeText(ProsesTransaksiActivity.this, "Transaksi gagal", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
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
        String dialogTitle = null, dialogMessage = null;
        if (isDialogClose) {
            dialogTitle = "Batal";
            dialogMessage = "Apakah anda ingin membatalkan perubahan pada form?";
        }
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(dialogTitle);
        alertDialogBuilder
                .setMessage(dialogMessage)
                .setCancelable(false)
                .setPositiveButton("Ya", (dialog, id) -> {
                    if (isDialogClose) {
                        finish();
                    }
                })
                .setNegativeButton("Tidak", (dialog, id) -> dialog.cancel());
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}