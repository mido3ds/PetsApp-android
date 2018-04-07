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

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.pets.Data.Pet;
import com.example.android.pets.Data.PetContract;
import com.example.android.pets.R;

/**
 * Allows user to create a new pet or edit an existing one.
 */
public class EditorActivity extends AppCompatActivity {

    // extras keys [& values]
    public static final String ID = "ID";
    public static final String PURPOSE = "PURPOSE";
    public static final int INSERT = 0;
    public static final int UPDATE = 1;
    private static int GENDER_UNKNOWN_INDEX;
    private static int GENDER_MALE_INDEX;
    private static int GENDER_FEMALE_INDEX;
    private int purpose;
    private Uri givenUri;
    /**
     * EditText field to enter the pet's name
     */
    private EditText mNameEditText;

    /**
     * EditText field to enter the pet's breed
     */
    private EditText mBreedEditText;

    /**
     * EditText field to enter the pet's weight
     */
    private EditText mWeightEditText;

    /**
     * EditText field to enter the pet's gender
     */
    private Spinner mGenderSpinner;

    /**
     * Gender of the pet. The possible values are:
     * 0 for unknown gender, 1 for male, 2 for female.
     */
    private int mGender = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        setupActivity(getIntent().getExtras());

        // Find all relevant views that we will need to read user input from
        mNameEditText = (EditText) findViewById(R.id.edit_pet_name);
        mBreedEditText = (EditText) findViewById(R.id.edit_pet_breed);
        mWeightEditText = (EditText) findViewById(R.id.edit_pet_weight);
        mGenderSpinner = (Spinner) findViewById(R.id.spinner_gender);

        setupSpinner();
    }

    private void setupActivity(Bundle extras) {
        purpose = extras.getInt(PURPOSE, INSERT);
        int id = extras.getInt(ID, -1);

        if (purpose == UPDATE) {
            assert id >= 0;

            givenUri = Uri.withAppendedPath(PetContract.PetEntry.CONTENT_URI, String.valueOf(id));
            setTitle(R.string.editor_activity_title_edit_pet);
            fillForm();
        } else {
            givenUri = PetContract.PetEntry.CONTENT_URI;
            setTitle(R.string.editor_activity_title_new_pet);
        }
    }

    private void fillForm() {
        Cursor cursor = getContentResolver().query(givenUri, null, null, null, null);
        Pet pet = new Pet(cursor);

        mNameEditText.setText(pet.getName());
        mBreedEditText.setText(pet.getBreed());
        mWeightEditText.setText(String.valueOf(pet.getWeight()));

        int position = getPositionInSpinner(pet.getGender());
        mGenderSpinner.setSelection(position);
    }

    private int getPositionInSpinner(int gender) {
        switch (gender) {
            case PetContract.PetEntry.GENDER_UNKNOWN:
                return GENDER_UNKNOWN_INDEX;
            case PetContract.PetEntry.GENDER_MALE:
                return GENDER_MALE_INDEX;
            case PetContract.PetEntry.GENDER_FEMALE:
                return GENDER_FEMALE_INDEX;
        }
        return gender;
    }

    /**
     * Setup the dropdown spinner that allows the user to select the gender of the pet.
     */
    private void setupSpinner() {
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter genderSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_gender_options, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        mGenderSpinner.setAdapter(genderSpinnerAdapter);

        // Set the integer mSelected to the constant values
        mGenderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.gender_male))) {
                        mGender = PetContract.PetEntry.GENDER_MALE;
                    } else if (selection.equals(getString(R.string.gender_female))) {
                        mGender = PetContract.PetEntry.GENDER_FEMALE;
                    } else {
                        mGender = PetContract.PetEntry.GENDER_UNKNOWN;
                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mGender = PetContract.PetEntry.GENDER_UNKNOWN;
            }
        });

        GENDER_UNKNOWN_INDEX = genderSpinnerAdapter.getPosition(getResources().getString(R.string.gender_unknown));
        GENDER_MALE_INDEX = genderSpinnerAdapter.getPosition(getResources().getString(R.string.gender_male));
        GENDER_FEMALE_INDEX = genderSpinnerAdapter.getPosition(getResources().getString(R.string.gender_female));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                proceedWithData();
                finish();
                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.action_delete:
                deletePet();
                finish();
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // Navigate back to parent activity (CatalogActivity)
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void proceedWithData() {
        try {
            if (purpose == INSERT) {
                getContentResolver().insert(givenUri, createPet().createContentValues());
            } else {
                getContentResolver().update(givenUri, createPet().createContentValues(), null, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void deletePet() {
        try {
            getContentResolver().delete(givenUri, null, null);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private Pet createPet() throws Exception {
        return (new Pet(getName(), getBreed(), mGender, getWeight()));
    }

    @NonNull
    private String getBreed() {
        return mBreedEditText.getText().toString();
    }

    private int getWeight() throws Exception {
        String weight = mWeightEditText.getText().toString();

        if (weight.equals("")) {
            throw new Exception("didn't add weight");
        }

        return Integer.parseInt(weight);
    }

    @NonNull
    private String getName() {
        return mNameEditText.getText().toString();
    }
}