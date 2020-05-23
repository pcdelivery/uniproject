package com.example.universityproject.data.databases;

import android.content.SharedPreferences;
import android.util.Log;

import com.example.universityproject.data.Models.ArcantownAccount;

import org.json.JSONException;
import org.json.JSONObject;

// TODO: Rename n' reorganize Account Manager
public class DatabaseManager {

    private SharedPreferences sharedPreferences;
    public ArcantownAccount acc;
    public boolean isUploadNewSuccess = false;
    public boolean isSharedComplete = false;
    public boolean isUpdateSuccess = false;
    private String email;

    /**
     * Sending query to insert new account, then gets full info about it and writing it to
     *  SharedPreferences file
     * @param email
     * @param sp
     */
    public void handleNewlyCreatedAccount(String authType, String email, String login, SharedPreferences sp) {
        sharedPreferences = sp;
        isUpdateSuccess = false;
        isSharedComplete = false;
        this.email = email;

//        sendAccountToServer(authType, email, login);

        PostNewAccountToMySQLServerNSync p = new PostNewAccountToMySQLServerNSync();
        p.execute("put_acc&auth_type=" + authType + "&email=" + email + "&login=" + login);
    }

//    public void sendAccountToServer() {
//        PutAccountFromMySQLServer p = new PutAccountFromMySQLServer();
//        p.execute("put_acc&auth_type=" + acc.getAuthType() + "&email=" + acc.getEmail() + "&login=" + acc.getLogin());
//    }

//    public void sendAccountToServer(String authType, String email, String login) {
//        PutAccountFromMySQLServer p = new PutAccountFromMySQLServer();
//        p.execute("put_acc&auth_type=" + authType + "&email=" + email + "&login=" + login);
//    }

    public void updateSharedPreferences() {
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
                .apply();
    }

    private class SyncAccountAndUpdateSharedPreferences extends ReceiveDataFromMySQLTask {

        @Override
        protected void onPostExecute(String s) {

            try {
                Log.d("GetAccountAsync", "Getting object from account json: " + s);
                JSONObject obj = new JSONObject(s).getJSONObject("account");
                acc = new ArcantownAccount(obj.toString());

                updateSharedPreferences();

                isSharedComplete = true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class PostNewAccountToMySQLServerNSync extends ReceiveDataFromMySQLTask {

        @Override
        protected void onPostExecute(String s) {
            isUploadNewSuccess = s.equals("true");

            new SyncAccountAndUpdateSharedPreferences().execute("account&email=" + email);
        }
    }
}
