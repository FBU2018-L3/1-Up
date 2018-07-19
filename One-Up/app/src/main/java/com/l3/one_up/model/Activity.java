package com.l3.one_up.model;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by luzcamacho on 7/16/18.
 */

@ParseClassName("Activity")
public class Activity extends ParseObject {
    private static final String KEY_NAME = "name";
    private static final String KEY_TAG = "category";
    private static final String KEY_BASE = "baselineXP";
    private static final String KEY_INPUT_TYPE = "inputType";

    public String getName() { return getString(KEY_NAME); }

    public String getCategory() { return getString(KEY_TAG); }

    public Integer getBaseXP() { return getInt(KEY_BASE); }

    public JSONObject getInputType() { return getJSONObject(KEY_INPUT_TYPE); }

    public static class Query extends ParseQuery<Activity> {

        public Query() {
            super(Activity.class);
        }
        /* Returns the specified information in the ArrayList (they should all be) for all for all activities  */
        public Query returnInputTypes(ArrayList<String> myInput) {
            selectKeys(myInput);
            setLimit(25); //delete this line if you want to return ALL results under the given parameters
            return this;
        }

        /* returns all activities by the specified category */
        public Query getCategoryAct(String Category) {
            include(KEY_TAG);
            whereEqualTo(KEY_TAG, Category);
            return this;
        }
        /* order by baseline XP value */
        public Query getByPoints() {
            orderByAscending(KEY_BASE);
            return this;
        }
        /* returns all documented activities. Please be careful :) */
        public Query getAll() {
            return this;
        }

        public Query fromLocalDatastore() {
            super.fromLocalDatastore();
            return this;
        }
    }
}
