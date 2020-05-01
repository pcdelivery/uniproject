package com.example.universityproject;

import android.content.ContentValues;

public class ArcantownAccount {
    public int id = -1;
    private String login;
    private String password;
    private String name = "Null";
    private String family_name = "Null";

    public ArcantownAccount(String login, String password) {
        this.login = login;
        this.password = password;
    }

    /**
     * Build full functional account
     * @author Me
     * @since 2k20-03-08
     * @return boolean (if error?)
     */
    public ArcantownAccount Build(String nameToUpdate, String fNameToUpdate) {
        this.name = nameToUpdate;
        this.family_name = fNameToUpdate;

        return this;
    }

    public String getAccountInfo() {
        String info = "";

        info += "Name: " + this.name + "\n";
        info += "Family name: " + this.family_name + "\n";

        return info;
    }

    public String _getFullAccountInfo() {
        String secure_info = "";

        secure_info += "ID: " + this.id + "\n";
        secure_info += "Login: " + this.login + "\n";
        secure_info += "Password: " + this.password + "\n";
        secure_info += getAccountInfo();

        return secure_info;
    }

    public ContentValues generateDatabaseRow() {
        ContentValues cv = new ContentValues();

        cv.clear();
        cv.put("login", this.login);
        cv.put("password", this.password);
        cv.put("name", this.name);
        cv.put("family_name", this.family_name);

        return cv;
    }
}
