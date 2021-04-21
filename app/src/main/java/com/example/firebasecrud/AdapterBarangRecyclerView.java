package com.example.firebasecrud;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firebasecrud.localDB.AllRecordActivity;
import com.example.firebasecrud.localDB.DatabaseHelper;

import java.util.ArrayList;

public class AdapterBarangRecyclerView extends RecyclerView.Adapter<ItemBarangViewHolder> {

    private final Context context;
    private final ArrayList<User> daftarUser;
    private final FirebaseDataListener listener;
    public DatabaseHelper databaseHelper;
    public User user;

    public AdapterBarangRecyclerView(Context context, ArrayList<User> daftarUser) {
        this.context = context;
        this.daftarUser = daftarUser;
        this.listener = (FirebaseDataListener) context;
    }


    @NonNull
    @Override
    public ItemBarangViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_barang, parent, false);

        databaseHelper = new DatabaseHelper(context);
        user = new User();

        ItemBarangViewHolder holder = new ItemBarangViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemBarangViewHolder holder, int position) {
// TODO: Implement this method
        holder.userId.setText("User ID   : " + daftarUser.get(position).getId());
        holder.namaBarang.setText("Full Name   : " + daftarUser.get(position).getUser_name());
        holder.merkBarang.setText("User Age     : " + daftarUser.get(position).getUser_age());
        holder.hargaBarang.setText("Ph Number   : " + daftarUser.get(position).getPhone_number());
        holder.buttonAddLocalDB.setOnClickListener(v -> {
            postDataToSQLite(position);
        });

        holder.buttonShowLocalDB.setOnClickListener(v -> {
            databaseHelper.getAllUser();
            context.startActivity(new Intent(context, AllRecordActivity.class));
            // set text to your TextView

        });

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onDataClick(daftarUser.get(position), position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return daftarUser.size();
    }

    private void postDataToSQLite(int position) {


        user.setUser_name(daftarUser.get(position).getUser_name());
        user.setUser_age(daftarUser.get(position).getUser_age());
        user.setPhone_number(daftarUser.get(position).getPhone_number());
        user.setId(daftarUser.get(position).getId());
        databaseHelper.addUser(user);
        // Snack Bar to show success message that record saved successfully
        Toast.makeText(context, "Data Saved Successfull", Toast.LENGTH_SHORT).show();
    }


    //interface data listener
    public interface FirebaseDataListener {
        void onDataClick(User user, int position);
    }
}