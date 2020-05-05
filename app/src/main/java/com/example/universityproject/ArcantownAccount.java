package com.example.universityproject;

import android.content.ContentValues;

public class ArcantownAccount {
    // @TODO: id = -1??? WHY??
    public int id = -1;
    private String login;
    private String password;
    private String name = "Null";
    private String familyName = "Null";

    public ArcantownAccount(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public ArcantownAccount(String login, String password, String name, String familyName) {
        this.login = login;
        this.password = password;
        this.name = name;
        this.familyName = familyName;
    }

    /**
     * Build full functional account
     * @author Me
     * @since 2k20-03-08
     * @return boolean (if error?)
     */
    public ArcantownAccount Build(String nameToUpdate, String fNameToUpdate) {
        this.name = nameToUpdate;
        this.familyName = fNameToUpdate;

        return this;
    }

    public String getAccountInfo() {
        StringBuffer info = new StringBuffer();

        info.append("Name: " + this.name + "\n");
        info.append("Family name: " + this.familyName + "\n");

        return info.toString();
    }

    public String getFullAccountInfo() {
        StringBuffer secure_info = new StringBuffer();

        secure_info.append("ID: " + this.id + "\n");
        secure_info.append("Login: " + this.login + "\n");
        secure_info.append("Password: " + this.password + "\n");
        secure_info.append(getAccountInfo());

        return secure_info.toString();
    }

    public ContentValues generateDatabaseRow() {
        ContentValues cv = new ContentValues();

        cv.clear();
        cv.put("login", this.login);
        cv.put("password", this.password);
        cv.put("name", this.name);
        cv.put("family_name", this.familyName);

        return cv;
    }
}
