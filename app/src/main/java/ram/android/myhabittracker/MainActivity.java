package ram.android.myhabittracker;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import ram.android.myhabittracker.data.HabitContract;
import ram.android.myhabittracker.data.HabitTrackerDBHelper;

import static ram.android.myhabittracker.data.HabitContract.HabitEntry.COLUMN_DURATION;
import static ram.android.myhabittracker.data.HabitContract.HabitEntry.COLUMN_HABIT_NAME;

public class MainActivity extends AppCompatActivity {

    HabitTrackerDBHelper dbHelper;
    SQLiteDatabase db;
    int i=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new HabitTrackerDBHelper(this);
        Button insertbutton = (Button) findViewById(R.id.next_button);
        insertbutton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                EditText sampleMultiLineEditText = (EditText) findViewById(R.id.habit);
                String inputhabitname = sampleMultiLineEditText.getText().toString();
                EditText sampleNumberSignedEditText = (EditText) findViewById(R.id.album_description_view);
                String value = sampleNumberSignedEditText.getText().toString();
                i = Integer.parseInt(value);
                insertHabit(inputhabitname,i);


            }

        });
                Button updatebutton = (Button) findViewById(R.id.update);
                updatebutton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v){
                        EditText sampleMultiLineEditText = (EditText) findViewById(R.id.habit);
                        String inputhabitname = sampleMultiLineEditText.getText().toString();
                        readHabits(inputhabitname);
                    }

                });
        Button deletebutton = (Button) findViewById(R.id.delete);
        deletebutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                deleteEntries();





            }
        });


    }

    //Insert method for making an entry to the habit tracker database
    public void insertHabit(String name, int duration) {
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_HABIT_NAME, name);
        values.put(COLUMN_DURATION, duration);

        db.insert(HabitContract.HabitEntry.TABLE_NAME, "null", values);
        Toast.makeText(MainActivity.this, "Record Inserted Successfully!",
                Toast.LENGTH_LONG).show();
        Button updatebutton = (Button) findViewById(R.id.update);
        updatebutton.setVisibility(View.VISIBLE);
    }

    //read method that reads habit_name and duration of entries with habit name as gaming
    public void readHabits(String habitName) {
        db = dbHelper.getReadableDatabase();
        String whereClause = COLUMN_HABIT_NAME + " = ?";
        String[] selectionArgs = {habitName};
        String result = "";
        StringBuilder sb = new StringBuilder();
        String[] projection = {
                COLUMN_HABIT_NAME,
                COLUMN_DURATION};
        Cursor c = db.query(
                HabitContract.HabitEntry.TABLE_NAME,
                projection,
                whereClause,
                selectionArgs,
                null,
                null,
                null);
        c.moveToFirst();
        if (c != null) {
            do {
                for (int i = 0; i < c.getColumnCount(); i++) {
                    result = sb.append(" " + c.getString(i)).toString();
                }
            } while (c.moveToNext());
            Log.v("Result of query ", result);
            TextView textview = (TextView) findViewById(R.id.title_text_view);
            textview.setText(result);
        }
        c.close();
    }

    //deletes all the entries from the table
    public void deleteEntries() {
        db.delete(HabitContract.HabitEntry.TABLE_NAME, null, null);
        Toast.makeText(MainActivity.this, "Record deleted Successfully!",
                Toast.LENGTH_LONG).show();
        TextView textview = (TextView) findViewById(R.id.title_text_view);
        textview.setText("");
        Button updatebutton = (Button) findViewById(R.id.update);
        updatebutton.setVisibility(View.INVISIBLE);
    }


    }