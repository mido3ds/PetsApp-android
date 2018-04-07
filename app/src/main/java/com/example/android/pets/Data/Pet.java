package com.example.android.pets.Data;


import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;

public class Pet {
    private int id = 0;
    private String name = "";
    private String breed = "";
    private int gender = PetContract.PetEntry.GENDER_UNKNOWN;
    private int weight = 0;

    public Pet(String name, String breed, int gender, int weight) {
        setName(name);
        setBreed(breed);
        setGender(gender);
        setWeight(weight);
    }

    public Pet(int id, String name, String breed, int gender, int weight) {
        this.id = id;
        this.name = name;
        this.breed = breed;
        this.gender = gender;
        this.weight = weight;
    }

    public Pet(Cursor cursor) {
        int nameColmnIndex = cursor.getColumnIndex(PetContract.PetEntry.COLUMN_PET_NAME);
        if (nameColmnIndex != -1) {
            setName(cursor.getString(nameColmnIndex));
        }

        int breedComnIndex = cursor.getColumnIndex(PetContract.PetEntry.COLUMN_PET_BREED);
        if (breedComnIndex != -1) {
            setBreed(cursor.getString(breedComnIndex));
        }

        int genderColmnIndex = cursor.getColumnIndex(PetContract.PetEntry.COLUMN_PET_GENDER);
        if (genderColmnIndex != -1) {
            setGender(cursor.getInt(genderColmnIndex));
        }

        int weightColmnIndex = cursor.getColumnIndex(PetContract.PetEntry.COLUMN_PET_WEIGHT);
        if (weightColmnIndex != -1) {
            setWeight(cursor.getInt(weightColmnIndex));
        }

        int idColmnIndex = cursor.getColumnIndex(PetContract.PetEntry._ID);
        if (idColmnIndex != -1) {
            setId(cursor.getInt(idColmnIndex));
        }

        if (nameColmnIndex == -1 && breedComnIndex == -1 && genderColmnIndex == -1 && weightColmnIndex == -1
                && idColmnIndex == -1) {
            throw new IllegalArgumentException("cursor is invalid");
        }
    }

    @NonNull
    public static String genderToString(int g) {
        switch (g) {
            case PetContract.PetEntry.GENDER_UNKNOWN:
                return "Unknown";
            case PetContract.PetEntry.GENDER_MALE:
                return "Male";
            case PetContract.PetEntry.GENDER_FEMALE:
                return "Female";
            default:
                throw new IllegalArgumentException("doesn't belong to possible values for the gender of the pet");
        }
    }

    public static Boolean isValidGender(Integer g) {
        return g != null && (g == PetContract.PetEntry.GENDER_FEMALE || g == PetContract.PetEntry.GENDER_MALE
                || g == PetContract.PetEntry.GENDER_UNKNOWN);
    }

    public static Boolean isValidWeight(Integer w) {
        return w != null && w > 0;
    }

    @NonNull
    public static Boolean isValidName(String name) {
        return name != null && !name.isEmpty();
    }

    public ContentValues createContentValues() {
        ContentValues values = new ContentValues();

        values.put(PetContract.PetEntry.COLUMN_PET_NAME, name);
        values.put(PetContract.PetEntry.COLUMN_PET_BREED, breed);
        values.put(PetContract.PetEntry.COLUMN_PET_WEIGHT, weight);
        values.put(PetContract.PetEntry.COLUMN_PET_GENDER, gender);

        return values;
    }

    @Override
    public String toString() {
        return "Pet{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", breed='" + breed + '\'' +
                ", gender=" + gender +
                ", weight=" + weight +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        if (id < 0) {
            throw new IllegalArgumentException("id must be greater than zero");
        }
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (!isValidName(name)) {
            throw new IllegalArgumentException("name is invalid");
        }
        this.name = name;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        if (!isValidGender(gender)) {
            throw new IllegalArgumentException("gender is invalid");
        }
        this.gender = gender;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        if (!isValidWeight(weight)) {
            throw new IllegalArgumentException("weight is invalid");
        }
        this.weight = weight;
    }
}
