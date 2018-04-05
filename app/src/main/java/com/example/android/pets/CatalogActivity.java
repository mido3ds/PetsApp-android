/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.pets;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.pets.Data.PetContract;
import com.example.android.pets.Data.PetDbHelper;

/**
 * Displays list of pets that were entered and stored in the app.
 */
public class CatalogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        displayDatabaseInfo();
    }

    /**
     * Temporary helper method to display information in the onscreen TextView about the state of
     * the pets database.
     */
    private void displayDatabaseInfo() {
        Cursor cursor = getContentResolver().query(PetContract.PetEntry.CONTENT_URI,
                null, null, null, null);

        try {
            // Display the number of rows in the Cursor (which reflects the number of rows in the
            // pets table in the database).
            TextView displayView = (TextView) findViewById(R.id.text_view_pet);
            displayView.setText("Number of rows in pets database table: " + cursor.getCount());
            displayView.append(("\n" + "_id" + " " + PetContract.PetEntry.COLUMN_PET_NAME + " " +
                    PetContract.PetEntry.COLUMN_PET_BREED + " " + PetContract.PetEntry.COLUMN_PET_GENDER +
                    " " + PetContract.PetEntry.COLUMN_PET_WEIGHT));

            int nameIndex = cursor.getColumnIndex(PetContract.PetEntry.COLUMN_PET_NAME);
            int breedIndex = cursor.getColumnIndex(PetContract.PetEntry.COLUMN_PET_BREED);
            int genderIndex = cursor.getColumnIndex(PetContract.PetEntry.COLUMN_PET_GENDER);
            int weightIndex = cursor.getColumnIndex(PetContract.PetEntry.COLUMN_PET_WEIGHT);
            int i = 0;

            String name, breed;
            int weight, gender;

            while (cursor.moveToNext()) {
                name = cursor.getString(nameIndex);
                breed = cursor.getString(breedIndex);
                weight = cursor.getInt(weightIndex);
                gender = cursor.getInt(genderIndex);

                displayView.append(("\n" + i + " " +
                    name + " " + breed + " " + PetContract.PetEntry.genderToString(gender) + " " + weight));

                i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            // Always close the cursor when you're done reading from it. This releases all its
            // resources and makes it invalid.
            cursor.close();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                insertDummyPet();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                deleteAllPets();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteAllPets() {
        getContentResolver().delete(PetContract.PetEntry.CONTENT_URI, null, null);
    }

    private void insertDummyPet() {
        ContentValues values = new ContentValues();
        values.put(PetContract.PetEntry.COLUMN_PET_NAME, "Toto");
        values.put(PetContract.PetEntry.COLUMN_PET_GENDER, PetContract.PetEntry.GENDER_MALE);
        values.put(PetContract.PetEntry.COLUMN_PET_BREED, "Terrier");
        values.put(PetContract.PetEntry.COLUMN_PET_WEIGHT, 7);

        getContentResolver().insert(PetContract.PetEntry.CONTENT_URI, values);
    }
}
