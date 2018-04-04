package com.example.android.pets.Data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public final class PetContract {
    private PetContract() {
    }

    private static final String SCHEME = "content://";
    public static final String CONTENT_AUTHORITY = "com.example.android.pets";
    public static final String PATH_PETS = "pets";

    public static final Uri BASE_CONTENT_URI = Uri.parse(SCHEME + CONTENT_AUTHORITY);

    public static final class PetEntry implements BaseColumns {

        /**
         * Name of database table for pets
         */
        public final static String TABLE_NAME = "pets";

        /**
         * Unique ID number for the pet (only for use in the database table).
         * <p>
         * Type: INTEGER
         */
        public final static String _ID = BaseColumns._ID;

        /**
         * Name of the pet.
         * <p>
         * Type: TEXT
         */
        public final static String COLUMN_PET_NAME = "name";

        /**
         * Breed of the pet.
         * <p>
         * Type: TEXT
         */
        public final static String COLUMN_PET_BREED = "breed";

        /**
         * Gender of the pet.
         * <p>
         * The only possible values are {@link #GENDER_UNKNOWN}, {@link #GENDER_MALE},
         * or {@link #GENDER_FEMALE}.
         * <p>
         * Type: INTEGER
         */
        public final static String COLUMN_PET_GENDER = "gender";

        /**
         * Weight of the pet.
         * <p>
         * Type: INTEGER
         */
        public final static String COLUMN_PET_WEIGHT = "weight";

        /**
         * Possible values for the gender of the pet.
         */
        public static final int GENDER_UNKNOWN = 0;
        public static final int GENDER_MALE = 1;
        public static final int GENDER_FEMALE = 2;

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_PETS);

        public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/"
                + CONTENT_AUTHORITY + "/" + PATH_PETS;

        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/"
                + CONTENT_AUTHORITY + "/" + PATH_PETS;

        public static String genderToString(int g) throws Exception {
            switch (g) {
                case GENDER_UNKNOWN:
                    return "Unknown";
                case GENDER_MALE:
                    return "Male";
                case GENDER_FEMALE:
                    return "Female";
                default:
                    throw new Exception("doesn't belong to possible values for the gender of the pet");
            }
        }

        public static Boolean isValidGender(Integer g) {
            return g != null && (g == GENDER_FEMALE || g == GENDER_MALE || g == GENDER_UNKNOWN);
        }

        public static Boolean isValidWeight(Integer w) {
            return w != null && w > 0;
        }

        public static Boolean isValidName(String name) {
            return name != null && !name.isEmpty();
        }
    }
}
