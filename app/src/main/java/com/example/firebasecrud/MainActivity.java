package com.example.firebasecrud;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firebasecrud.weather.MyWeather;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterBarangRecyclerView.FirebaseDataListener {
    SearchView mSearchView;
    //variable fields
    private Toolbar mToolbar;
    private FloatingActionButton mFloatingActionButton;
    private EditText mEditNama;
    private EditText mEditMerk;
    private EditText mEditHarga;
    private EditText mEditUserID;
    private RecyclerView mRecyclerView;
    private AdapterBarangRecyclerView mAdapter;
    private ArrayList<User> daftarUser;
    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;
    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mFirebaseInstance;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);


        dl = findViewById(R.id.drawer_layout);
        t = new ActionBarDrawerToggle(this, dl, R.string.open, R.string.close);

        dl.addDrawerListener(t);
        t.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nv = findViewById(R.id.nav_view);

        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.nav_item_one:
                        startActivity(new Intent(getApplicationContext(), MyWeather.class));
                        break;
                    default:
                        return true;
                }


                return true;

            }
        });





        //android toolbar

        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        mFirebaseInstance = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseInstance.getReference("user");
        mDatabaseReference.child("item_data").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                daftarUser = new ArrayList<>();
                for (DataSnapshot mDataSnapshot : dataSnapshot.getChildren()) {
                    User user = mDataSnapshot.getValue(User.class);
                    user.setKey(mDataSnapshot.getKey());
                    daftarUser.add(user);
                }
                //set adapter RecyclerView
                mAdapter = new AdapterBarangRecyclerView(MainActivity.this, daftarUser);
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // TODO: Implement this method
                Toast.makeText(MainActivity.this, databaseError.getDetails() + " " + databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }

        });


        mFloatingActionButton = findViewById(R.id.tambah_barang);
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialogTambahBarang();
            }
        });
    }




    @Override
    public void onDataClick(final User user, int position) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Action");

        builder.setPositiveButton("Update Record", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialogUpdateBarang(user);
            }
        });
        builder.setNegativeButton("Delete Record", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                hapusDataBarang(user);
            }
        });


        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        Dialog dialog = builder.create();
        dialog.show();
    }



    private void dialogTambahBarang() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add User Data");
        View view = getLayoutInflater().inflate(R.layout.layout_tambah_barang, null);

        mEditNama = view.findViewById(R.id.nama_barang);
        mEditMerk = view.findViewById(R.id.merk_barang);
        mEditHarga = view.findViewById(R.id.harga_barang);
        mEditUserID = view.findViewById(R.id.user_id);

        builder.setView(view);


        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {

                String namaBarang = mEditNama.getText().toString();
                String merkBarang = mEditMerk.getText().toString();
                String hargaBarang = mEditHarga.getText().toString();
                String userId = mEditUserID.getText().toString();

                if (!namaBarang.isEmpty() && !merkBarang.isEmpty() && !hargaBarang.isEmpty() && !userId.isEmpty()) {
                    submitDataBarang(new User(namaBarang, merkBarang, hargaBarang, userId));
                } else {
                    Toast.makeText(MainActivity.this, "Data must be filled!", Toast.LENGTH_LONG).show();
                }
            }
        });


        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        Dialog dialog = builder.create();
        dialog.show();
    }



    private void dialogUpdateBarang(final User user) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Data must be filled!");
        View view = getLayoutInflater().inflate(R.layout.layout_edit_barang, null);

        mEditNama = view.findViewById(R.id.nama_barang);
        mEditMerk = view.findViewById(R.id.merk_barang);
        mEditHarga = view.findViewById(R.id.harga_barang);
        mEditUserID = view.findViewById(R.id.user_id);

        mEditNama.setText(user.getUser_name());
        mEditMerk.setText(user.getUser_age());
        mEditHarga.setText(user.getPhone_number());
        mEditUserID.setText(user.getId());
        builder.setView(view);

        //final User mBarang = (User)getIntent().getSerializableExtra("
        if (user != null) {
            builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    user.setUser_name(mEditNama.getText().toString());
                    user.setUser_age(mEditMerk.getText().toString());
                    user.setPhone_number(mEditHarga.getText().toString());
                    user.setId(mEditUserID.getText().toString());
                    updateDataBarang(user);
                }
            });
        }
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        Dialog dialog = builder.create();
        dialog.show();

    }


    private void submitDataBarang(User user) {
        mDatabaseReference.child("item_data").push().setValue(user).addOnSuccessListener(this, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void mVoid) {
                Toast.makeText(MainActivity.this, "Data saved successfully!", Toast.LENGTH_LONG).show();
            }
        });
    }


    private void updateDataBarang(User user) {
        mDatabaseReference.child("item_data").child(user.getKey()).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void mVoid) {
                Toast.makeText(MainActivity.this, "Data updated successfully !", Toast.LENGTH_LONG).show();
            }
        });
    }


   void hapusDataBarang(User user) {
        if (mDatabaseReference != null) {
            mDatabaseReference.child("item_data").child(user.getKey()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void mVoid) {
                    Toast.makeText(MainActivity.this, "Data deleted successfully!", Toast.LENGTH_LONG).show();
                }
            });
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (t.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }



}