package com.example.firebasecrud;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


public class ItemBarangViewHolder extends RecyclerView.ViewHolder {
    public TextView namaBarang;
    public TextView merkBarang;
    public TextView hargaBarang;
    public TextView userId;
    public View view;
    public Button buttonAddLocalDB;
    public Button buttonShowLocalDB;

    public ItemBarangViewHolder(View view) {
        super(view);

        namaBarang = (TextView) view.findViewById(R.id.nama_barang);
        merkBarang = (TextView) view.findViewById(R.id.merk_barang);
        hargaBarang = (TextView) view.findViewById(R.id.harga_barang);
        userId = (TextView) view.findViewById(R.id.user_id);
        buttonAddLocalDB = view.findViewById(R.id.btnAddDatatoLocalDB);
        buttonShowLocalDB = view.findViewById(R.id.btnShowLocalDBData);
        this.view = view;
    }
}