package com.example.firebasecrud.localDB;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.firebasecrud.Constants;
import com.example.firebasecrud.MainActivity;
import com.example.firebasecrud.R;
import com.example.firebasecrud.User;

import java.util.ArrayList;
import java.util.List;

public class AllRecordActivity extends AppCompatActivity {

    private static final String DATABASE_NAME = "UserManager.db";
    List<User> employeeList;
    SQLiteDatabase mDatabase;
    ListView listViewEmployees;
    RecordAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_record);

        listViewEmployees = (ListView) findViewById(R.id.listViewEmployees);
        employeeList = new ArrayList<>();

        //opening the database
        mDatabase = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);

        //this method will display the employees in the list
        showEmployeesFromDatabase();

    }

    private void showEmployeesFromDatabase() {
        adapter = new RecordAdapter(this, R.layout.list_layout_data, Constants.userArrayList, mDatabase);
        //adding the adapter to listview
        listViewEmployees.setAdapter(adapter);
        reloadEmployeesFromDatabase();
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
        adapter.notifyDataSetChanged();
    }


}