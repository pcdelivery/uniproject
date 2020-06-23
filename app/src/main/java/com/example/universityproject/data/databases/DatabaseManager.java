package com.example.universityproject.data.databases;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.example.universityproject.data.Models.ArcantownAccount;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;

// TODO: Rename n' reorganize Account Manager
// TODO: Data app-server streaming only through Manager object
public class DatabaseManager {
    static private String TAG = "DatabaseAccountManager";

    private Context mContext;
    private ArcantownAccount acc;
    private SharedPreferences sharedPreferences;

//    public boolean isAccountInfoFetched;
    public boolean isCreateNewSuccess;
    public boolean isUpdateSuccess;
    public boolean isQuizUpdateSuccess;


    public void setShared(ArcantownAccount acc) {
        this.acc = acc;
        updateSharedPreferences();
    }

    public DatabaseManager(Context context) {
        mContext = context;
        sharedPreferences = mContext.getSharedPreferences("account1", Context.MODE_PRIVATE);
        acc = getAccountFromShared();
    }

//    public DatabaseManager(Context context, String alternativeAccountName) {
//        mContext = context;
//        sharedPreferences = mContext.getSharedPreferences(alternativeAccountName, Context.MODE_PRIVATE);
//        acc = getAccountFromShared();
//    }

//    public DatabaseManager clear() {
//        sharedPreferences.edit().clear().apply();
//        return this;
//    }

    /**
     * Sending query to insert new account, then gets full info about it and writing it to
     *  SharedPreferences file
     * @param authType: def/google/facebook/etc.
     * @param email: User Email
     * @param login: User Login
     */
    public void handleNewlyCreatedAccount(String authType, String email, String login) {
        new PostNewAccountToMySQLServerNSync()
                .execute("put_acc&auth_type=" + authType + "&email=" + email + "&login=" + login);
    }

    public void updateAccount(String field, String newValue) {
        new UpdateAccountInMySQLServerNSync()
                .execute("change&email=" + acc.getEmail() + "&field=" + field + "&value=" + newValue);
    }

    public void updateQuizInAccount(String placeID, String pointsToAdd) {
        new UpdateAccountInMySQLServerNSync()
                .execute("update_quiz&email=" + acc.getEmail() + "&placeid=" + placeID + "&points=" + pointsToAdd);
    }

    private void updateSharedPreferences() {
        sharedPreferences.edit()
                .putString("id", String.valueOf(acc.getId()))
                .putString("auth_type", acc.getAuthType())
                .putString("email", acc.getEmail())
                .putString("login", acc.getLogin())
                .putString("name", acc.getPersonalName())
                .putString("points", String.valueOf(acc.getPoints()))
                .putString("cur_country", acc.getCurrentCountry())
                .putString("cur_town", acc.getCurrentTown())
                .putString("completed", acc.getCompletedPlaces())
                .putString("bonus", String.valueOf(acc.getBonus()))
                .apply();
    }

    // @todo: Restructure set/get pairs. Only valuable and safe must be left
    public String getUserIDFromShared() {
        return sharedPreferences.getString("id", null);
    }

    public String getAuthTypeFromShared() {
        return sharedPreferences.getString("auth_type", null);
    }

    public String getEmailFromShared() {
        return sharedPreferences.getString("email", null);
    }

    public String getLoginFromShared() {
        return sharedPreferences.getString("login", null);
    }

    public String getNameFromShared() {
        return sharedPreferences.getString("name", null);
    }

    public String getPointsFromShared() {
        return sharedPreferences.getString("points", null);
    }

    public String getCountryFromShared() {
        return sharedPreferences.getString("cur_country", null);
    }

    public String getTownFromShared() {
        return sharedPreferences.getString("cur_town", null);
    }

    public String getCompletedFromShared() {
        return sharedPreferences.getString("completed", null);
    }

    public String getBonusFromShared() {
        return sharedPreferences.getString("bonus", null);
    }

    public ArcantownAccount getAccountFromShared() {
        ArcantownAccount res = null;

        try {
           res = new ArcantownAccount(sharedPreferences);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return res;
    }

//    public String getAccountInfo() {
//        if (acc == null)
//            return "EMPTY";
//        else
//            return acc.generateJSONObject();
//    }

//    public void addCompleted(String placeid) {
//        String res = getCompleted() + "," + placeid;
//
//        sharedPreferences.edit()
//                .putString("completed", res)
//                .apply();
//    }

    // Creating new account result handling
    // url-tail example: "put_acc&auth_type=def&email=email@email.email&login=userlogin"
    @SuppressLint("StaticFieldLeak")
    private class PostNewAccountToMySQLServerNSync extends ReceiveDataFromMySQLTask {

        @Override
        protected void onPostExecute(String s) {
            isCreateNewSuccess = false;

            if (s == null) {
                Toast.makeText(mContext, "Connection error. Server does not response", Toast.LENGTH_LONG).show();
                Log.d(TAG, "Connection error. Server does not response");
            }
            else if (s.equals("false")) {
                // todo: account with email already exist... probably
                Toast.makeText(mContext, "Connection error. Failed while creating account", Toast.LENGTH_LONG).show();
                Log.d(TAG, "Connection error. Failed while creating account");
            }
            else {
                try {
                    // todo delete
                    Log.d(TAG, "Account before: " + (acc == null ? "null" : acc.generateJSONObject()));

                    JSONObject obj = new JSONObject(s).getJSONObject("account");

                    acc = new ArcantownAccount(obj.toString());
                    updateSharedPreferences();
                    Toast.makeText(mContext, "Account created successfully", Toast.LENGTH_LONG).show();
                    isCreateNewSuccess = true;

                    // todo delete
                    Log.d(TAG, "Account after: " + acc.generateJSONObject());
                } catch (JSONException e) {
                    Toast.makeText(mContext, "Data received error. Failed while creating account", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        }
    }

    // Update account result handling
    // url-tail example: "change&email=email@email.email&field=points&value=42"
    private class UpdateAccountInMySQLServerNSync extends ReceiveDataFromMySQLTask {

        @Override
        protected void onPostExecute(String s) {
            isUpdateSuccess = false;

            if (s == null)
                Toast.makeText(mContext, "Connection error. Server does not response", Toast.LENGTH_LONG).show();
            else if (s.equals("false"))
                Toast.makeText(mContext, "Connection error. Failed while updating account", Toast.LENGTH_LONG).show();
            else {
                try {
                    // todo delete
                    Log.d(TAG, "Account before: " + acc.generateJSONObject());

                    JSONObject obj = new JSONObject(s).getJSONObject("account");

                    acc = new ArcantownAccount(obj.toString());
                    updateSharedPreferences();
                    Toast.makeText(mContext, "Synchronised successfully", Toast.LENGTH_LONG).show();
                    isUpdateSuccess = true;

                    // todo delete
                    Log.d(TAG, "Account after: " + acc.generateJSONObject());
                } catch (JSONException e) {
                    Toast.makeText(mContext, "Data received error. Failed while updating account", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        }
    }

    // Update account quiz-info (points, completed) result handling
    // url-tail example: "update_quiz&email=email@email.email&placeid=18&points=42"
    private class UpdateCompletedQuizInMySQLServerNSync extends ReceiveDataFromMySQLTask {

        @Override
        protected void onPostExecute(String s) {
            isQuizUpdateSuccess = false;

            if (s == null)
                Toast.makeText(mContext, "Connection error. Server does not response", Toast.LENGTH_LONG).show();
            else if (s.equals("false"))
                Toast.makeText(mContext, "Connection error. Failed while updating quiz", Toast.LENGTH_LONG).show();
            else {
                try {
                    // todo delete
                    Log.d(TAG, "Account before: " + acc.generateJSONObject());

                    JSONObject obj = new JSONObject(s).getJSONObject("account");

                    acc = new ArcantownAccount(obj.toString());
                    updateSharedPreferences();
                    Toast.makeText(mContext, "Synchronised successfully", Toast.LENGTH_LONG).show();
                    isQuizUpdateSuccess = true;

                    // todo delete
                    Log.d(TAG, "Account after: " + acc.generateJSONObject());
                } catch (JSONException e) {
                    Toast.makeText(mContext, "Data received error. Failed while updating quiz", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        }
    }

    // TODO: get + post + put + delete
    // https://bizzapps.ru/b/rest-api/

//    @SuppressLint("StaticFieldLeak")
//    private class Update extends ReceiveDataFromMySQLTask {
//
//        @Override
//        protected void onPostExecute(String s) {
//            isUploadNewSuccess = s.equals("true");
//
//            new SyncAccountAndUpdateSharedPreferences().execute("account&email=" + email);
//        }
//    }
}
