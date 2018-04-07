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
package com.example.android.pets.UI;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.android.pets.Data.Pet;
import com.example.android.pets.Data.PetContract;
import com.example.android.pets.R;

/**
 * Displays list of pets that were entered and stored in the app.
 */
public class CatalogActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final Pet dummyPet = new Pet("Toto", "Terrier", PetContract.PetEntry.GENDER_MALE, 7);
    private final PetCursorAdapter petCursorAdapter = new PetCursorAdapter(this, null);
    private final LoaderManager loaderManager = getLoaderManager();
    private final int LOADER_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        setupFloatingActionButton();
        setupListView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        runLoader();
    }

    private void setupFloatingActionButton() {
        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class)
                        .putExtra(EditorActivity.PURPOSE, EditorActivity.INSERT);
                startActivity(intent);
            }
        });
    }

    private void setupListView() {
        ListView listView = findViewById(R.id.catalog_list);
        listView.setAdapter(petCursorAdapter);
        listView.setEmptyView(findViewById(R.id.empty_view));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long i) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class)
                        .putExtra(EditorActivity.PURPOSE, EditorActivity.UPDATE)
                        .putExtra(EditorActivity.ID, position+1);
                startActivity(intent);
            }
        });
    }

    private void runLoader() {
        Loader loader = loaderManager.getLoader(LOADER_ID);
        if (loader != null && loader.isReset()) {
            loaderManager.restartLoader(LOADER_ID, null, this);
        } else {
            loaderManager.initLoader(LOADER_ID, null, this);
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
        getContentResolver().insert(PetContract.PetEntry.CONTENT_URI, dummyPet.createContentValues());
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this, PetContract.PetEntry.CONTENT_URI,
                null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        // go and use this cursor
        petCursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // close the last cursor
        petCursorAdapter.swapCursor(null);
    }
}
