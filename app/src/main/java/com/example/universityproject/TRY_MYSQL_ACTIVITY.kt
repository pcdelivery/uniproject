package com.example.universityproject

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import com.example.universityproject.data.Models.ArcantownAccount
import com.example.universityproject.data.databases.DatabaseManager

/**
 * DatabaseManager test field
 */
class TRY_MYSQL_ACTIVITY : AppCompatActivity() {
    private val TAG = "TRY_MYSQL_ACTIVITY"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout._activity_try_mysql)

//        val manager = DatabaseManager(this, "account_try4")


        // Account creation
//        manager.handleNewlyCreatedAccount("def", "default@mail.com", "DefaultLogin")
//        manager.handleNewlyCreatedAccount("google", "google6@mail.com", "GoogleLogin")
//        manager.handleNewlyCreatedAccount("facebook", "facebook@mail.com", "FacebookLogin")
        // /Account creation


        // ArcantownAccount get from shared memory
//        manager.accountFromShared
        // /ArcantownAccount get from shared memory


//        <string name="cur_country">null</string>
//        <string name="cur_town">null</string>
//        <string name="bonus">0</string>
//        <string name="name">null</string>
//        <string name="completed">0</string>
//        <string name="login">GoogleLogin</string>
//        <string name="points">0</string>


        // Account update (using one-by-one is not recommended. Google email = identificator for account updating)
//        manager.updateAccount("email", "google7@mail.com")

//        manager.updateAccount("id", "24")
//        manager.updateAccount("auth_type", "google1")
//        manager.updateAccount("login", "Google1Login")
//        manager.updateAccount("name", "My New Name")
//        manager.updateAccount("points", "15")
//        manager.updateAccount("cur_country", "China")
//        manager.updateAccount("cur_town", "Beijing")
//        manager.updateAccount("completed", "0,1")
//        manager.updateAccount("bonus", "1")
        // /Account update


        // Account quiz updating
//        manager.updateQuizInAccount(manager.emailFromShared, "102", "30")
        // /Account quiz updating
    }
}
