package com.example.mytoko.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mytoko.CustomOnItemClickListener;
import com.example.mytoko.R;
import com.example.mytoko.entity.Note;
import com.example.mytoko.ui.home.transaksi.ProsesTransaksiActivity;

import java.util.ArrayList;

public class TransaksiAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {
    private ArrayList<Note> listNotes = new ArrayList<>();
    private Activity activity;

    @NonNull
    @Override
    public NoteAdapter.NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false);
        return new NoteAdapter.NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteAdapter.NoteViewHolder holder, int position) {
        holder.tvJudulBuku.setText(listNotes.get(position).getJudulBuku());
        holder.tvHarga.setText(listNotes.get(position).getHarga());
        holder.tvDeskripsi.setText(listNotes.get(position).getDeskripsi());
        holder.tvDate.setText(listNotes.get(position).getDate());
        holder.cvNote.setOnClickListener(new CustomOnItemClickListener(position, (view, position1) -> {
            Intent intent = new Intent(activity, ProsesTransaksiActivity.class);
            intent.putExtra(ProsesTransaksiActivity.EXTRA_POSITION, position1);
            intent.putExtra(ProsesTransaksiActivity.EXTRA_NOTE, listNotes.get(position1));
            activity.startActivityForResult(intent, ProsesTransaksiActivity.REQUEST_UPDATE);
        }));
    }

    @Override
    public int getItemCount() {
        return listNotes.size();
    }

    static class NoteViewHolder extends RecyclerView.ViewHolder {
        final TextView tvJudulBuku, tvHarga, tvDeskripsi, tvDate;
        final CardView cvNote;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            tvJudulBuku = itemView.findViewById(R.id.tv_item_judul_buku);
            tvHarga = itemView.findViewById(R.id.tv_item_harga);
            tvDeskripsi = itemView.findViewById(R.id.tv_item_deskripsi);
            tvDate = itemView.findViewById(R.id.tv_item_date);
            cvNote = itemView.findViewById(R.id.cv_item_note);
        }
    }

    public TransaksiAdapter(Activity activity) {
        this.activity = activity;
    }

    public ArrayList<Note> getListNotes() {
        return listNotes;
    }

    public void setListNotes(ArrayList<Note> listNotes) {
        if (listNotes.size() > 0) {
            this.listNotes.clear();
        }
        this.listNotes.addAll(listNotes);
        notifyDataSetChanged();
    }

    public void addItem(Note note) {
        this.listNotes.add(note);
        notifyItemInserted(listNotes.size() - 1);
    }

    public void updateItem(int position, Note note) {
        this.listNotes.set(position, note);
        notifyItemChanged(position, note);
    }

    public void removeItem(int position) {
        this.listNotes.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, listNotes.size());
    }
}
