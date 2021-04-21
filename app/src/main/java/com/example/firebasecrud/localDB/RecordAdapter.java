package com.example.firebasecrud.localDB;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.firebasecrud.Constants;
import com.example.firebasecrud.R;
import com.example.firebasecrud.User;

import java.util.List;

public class RecordAdapter extends ArrayAdapter<User> {

    Context mCtx;
    int listLayoutRes;
    final List<User> employeeList;
    SQLiteDatabase mDatabase;
    DatabaseHelper mDatabaseHelper;
    RecordAdapter mRecordAdapter;
    User user;

    public RecordAdapter(Context mCtx, int listLayoutRes, List<User> employeeList, SQLiteDatabase mDatabase) {
        super(mCtx, listLayoutRes, employeeList);

        this.mCtx = mCtx;
        this.listLayoutRes = listLayoutRes;
        this.employeeList = employeeList;
        this.mDatabase = mDatabase;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(listLayoutRes, null);

        //getting employee of user_age = "OmarAlDayel"the specified position
        user = employeeList.get(position);
        mDatabaseHelper = new DatabaseHelper(mCtx);

        //getting views
        TextView textViewName = view.findViewById(R.id.textViewName);
        TextView textViewId = view.findViewById(R.id.textViewId);
        TextView textViewAge = view.findViewById(R.id.textViewAge);
        TextView textViewPhone = view.findViewById(R.id.textViewPhNumber);

        //adding data to views
        textViewName.setText(user.getUser_name());
        textViewId.setText(user.getId());
        textViewAge.setText(user.getUser_age());
        textViewPhone.setText(user.getPhone_number());

        //we will use these buttons later for update and delete operation
        Button buttonDelete = view.findViewById(R.id.buttonDeleteEmployee);
        buttonDelete.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(mCtx);
            builder.setTitle("Are you sure?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if(user!=null){
                        mDatabaseHelper.deleteUser(user);

                    }else{
                        Toast.makeText(getContext(),"User Does'nt Exsits",Toast.LENGTH_SHORT).show();
                    }


                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        });


        Button buttonEdit = view.findViewById(R.id.buttonEditEmployee);
        buttonEdit.setOnClickListener(v -> {
            updateRecord(user);
        });




        return view;
    }

    private void updateRecord(final User user) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(mCtx);

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.dialog_update_employee, null);
        builder.setView(view);


        final EditText editTextName = view.findViewById(R.id.editTextName);
        final EditText editTextId = view.findViewById(R.id.editTextId);
        final EditText editTextAge = view.findViewById(R.id.editTextAge);
        final EditText editTextPhone = view.findViewById(R.id.editTextPhoneNumber);



        final AlertDialog dialog = builder.create();
        dialog.show();

        view.findViewById(R.id.buttonUpdateEmployee).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                editTextName.setText(user.getUser_name());
                editTextPhone.setText(user.getPhone_number());
                editTextAge.setText(user.getUser_age());
                editTextId.setText(String.valueOf(user.getId()));

                String name = editTextName.getText().toString().trim();
                String id = editTextId.getText().toString().trim();
                String age = editTextAge.getText().toString().trim();
                String number = editTextPhone.getText().toString().trim();

                if (name.isEmpty()) {
                    editTextName.setError("Name can't be blank");
                    editTextName.requestFocus();
                    return;
                }

                if (age.isEmpty()) {
                    editTextId.setError("Age can't be blank");
                    editTextId.requestFocus();
                    return;
                }
                if (id.isEmpty()) {
                    editTextName.setError("Id can't be blank");
                    editTextName.requestFocus();
                    return;
                }

                if (number.isEmpty()) {
                    editTextId.setError("Number can't be blank");
                    editTextId.requestFocus();
                    return;
                }

                String sql = "UPDATE employees \n" +
                        "SET name = ?, \n" +
                        "department = ?, \n" +
                        "salary = ? \n" +
                        "WHERE id = ?;\n";

                mDatabase.execSQL(sql, new String[]{name, id, age, number, String.valueOf(user.getId())});
                Toast.makeText(mCtx, "Employee Updated", Toast.LENGTH_SHORT).show();
                reloadEmployeesFromDatabase();

                dialog.dismiss();
            }
        });



    }


    private void reloadEmployeesFromDatabase() {
        Cursor cursorEmployees = mDatabase.rawQuery("SELECT * FROM user", null);
        if (cursorEmployees.moveToFirst()) {
            Constants.userArrayList.clear();
            do {
                Constants.userArrayList.add(new User(
                        cursorEmployees.getString(0),
                        cursorEmployees.getString(1),
                        cursorEmployees.getString(2),
                        cursorEmployees.getString(3)
                ));
            } while (cursorEmployees.moveToNext());
        }
        cursorEmployees.close();
        RecordAdapter.this.notifyDataSetChanged();
    }

}