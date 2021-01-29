package com.example.mytoko.ui.home.home;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mytoko.R;
import com.example.mytoko.adapter.NoteAdapter;
import com.example.mytoko.db.NoteHelper;
import com.example.mytoko.entity.Note;
import com.example.mytoko.helper.MappingHelper;
import com.example.mytoko.login_register.LoginActivity;
import com.example.mytoko.ui.home.transaksi.TransaksiActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, LoadNotesCallback{
    private ProgressBar progressBar;
    private RecyclerView rvNotes;
    private NoteAdapter adapter;
    private NoteHelper noteHelper;
    private static final String EXTRA_STATE = "EXTRA_STATE";
    private final int ALERT_DIALOG_CLOSE = 10;
    CoordinatorLayout home, transaksi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fabAdd = findViewById(R.id.fab_add);
        fabAdd.setOnClickListener(view -> {
            Intent intent = new Intent(this, ProsesScrudActivity.class);
            startActivityForResult(intent, ProsesScrudActivity.REQUEST_ADD);
        });

        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle("Daftar Baju");

        progressBar = findViewById(R.id.progressbar);
        rvNotes = findViewById(R.id.rv_notes);
        rvNotes.setLayoutManager(new LinearLayoutManager(this));
        rvNotes.setHasFixedSize(true);
        adapter = new NoteAdapter(this);
        rvNotes.setAdapter(adapter);


        noteHelper = NoteHelper.getInstance(getApplicationContext());
        noteHelper.open();

        new HomeActivity.LoadNotesAsync(noteHelper, this).execute();
        if (savedInstanceState == null) {
            new HomeActivity.LoadNotesAsync(noteHelper, this).execute();
        } else {
            ArrayList<Note> list = savedInstanceState.getParcelableArrayList(EXTRA_STATE);
            if (list != null) {
                adapter.setListNotes(list);
            }
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.getMenu().getItem(0).setCheckable(true);

        home = findViewById(R.id.home_activity);
        transaksi = findViewById(R.id.transaksi_activity);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (data != null) {
            if (requestCode == ProsesScrudActivity.REQUEST_ADD) {
                if (resultCode == ProsesScrudActivity.RESULT_ADD) {
                    Note note = data.getParcelableExtra(ProsesScrudActivity.EXTRA_NOTE);
                    adapter.addItem(note);
                    rvNotes.smoothScrollToPosition(adapter.getItemCount() - 1);
                    showSnackbarMessage("Satu baju berhasil ditambahkan");
                }
            }
            else if (requestCode == ProsesScrudActivity.REQUEST_UPDATE) {
                if (resultCode == ProsesScrudActivity.RESULT_UPDATE) {
                    Note note = data.getParcelableExtra(ProsesScrudActivity.EXTRA_NOTE);
                    int position = data.getIntExtra(ProsesScrudActivity.EXTRA_POSITION, 0);
                    adapter.updateItem(position, note);
                    rvNotes.smoothScrollToPosition(position);
                    showSnackbarMessage("Satu baju berhasil diubah");
                } else if (resultCode == ProsesScrudActivity.RESULT_DELETE) {
                    int position = data.getIntExtra(ProsesScrudActivity.EXTRA_POSITION, 0);
                    adapter.removeItem(position);
                    showSnackbarMessage("Satu baju berhasil dihapus");
                }
            }
        }
    }

    private void showSnackbarMessage(String message) {
        Snackbar.make(rvNotes, message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        noteHelper.close();
    }

    @Override
    public void preExecute() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void postExecute(ArrayList<Note> notes) {
        progressBar.setVisibility(View.INVISIBLE);
        if (notes.size() > 0) {
            adapter.setListNotes(notes);
        } else {
            adapter.setListNotes(new ArrayList<Note>());
            showSnackbarMessage("Tidak ada data saat ini");
        }
    }

    private static class LoadNotesAsync extends AsyncTask<Void, Void, ArrayList<Note>> {
        private final WeakReference<NoteHelper> weakNoteHelper;
        private final WeakReference<LoadNotesCallback> weakCallback;
        private LoadNotesAsync(NoteHelper noteHelper, LoadNotesCallback callback) {
            weakNoteHelper = new WeakReference<>(noteHelper);
            weakCallback = new WeakReference<>(callback);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            weakCallback.get().preExecute();
        }
        @Override
        protected ArrayList<Note> doInBackground(Void... voids) {
            Cursor dataCursor = weakNoteHelper.get().queryAll();
            return MappingHelper.mapCursorToArrayList(dataCursor);
        }

        @Override
        protected void onPostExecute(ArrayList<Note> notes) {
            super.onPostExecute(notes);
            weakCallback.get().postExecute(notes);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(EXTRA_STATE, adapter.getListNotes());
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id_ = item.getItemId();

        if (id_ == R.id.nav_transaksi){
            getSupportActionBar().setTitle("Transaksi");
            Intent i = new Intent(this, TransaksiActivity.class);
            startActivity(i);

        } else if (id_ == R.id.action_logout){
            showAlertDialog(ALERT_DIALOG_CLOSE);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void showAlertDialog(int type) {
        final boolean isDialogClose = type == ALERT_DIALOG_CLOSE;
        String dialogTitle = null, dialogMessage = null;
        if (isDialogClose) {
            dialogTitle = "Keluar Akun";
            dialogMessage = "Apakah anda ingin keluar dari akun?";
        }
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(dialogTitle);
        alertDialogBuilder
                .setMessage(dialogMessage)
                .setCancelable(false)
                .setPositiveButton("Ya", (dialog, id) -> {
                    if (isDialogClose) {
                        Intent intent = new Intent(this, LoginActivity.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Tidak", (dialog, id) -> dialog.cancel());
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void onBackPressed(){
        showAlertDialog(ALERT_DIALOG_CLOSE);
    }
}

interface LoadNotesCallback {
    void preExecute();
    void postExecute(ArrayList<Note> notes);
}
