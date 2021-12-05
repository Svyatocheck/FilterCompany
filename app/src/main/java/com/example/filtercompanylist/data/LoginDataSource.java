package com.example.filtercompanylist.data;

import android.util.Log;

import com.example.filtercompanylist.Controller.DatabaseAdapter;
import com.example.filtercompanylist.data.model.LoggedInUser;

import java.io.IOException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    public Result<LoggedInUser> login(String username, String password, DatabaseAdapter dbAdapter) {

        try {
            String result = "";
            result = dbAdapter.findUser(username);

            if (!result.equals("")) {
                if (dbAdapter.checkPassword(result, password) != null) {
                    LoggedInUser fakeUser = new LoggedInUser(java.util.UUID.randomUUID().toString(), " " + username, "mail");
                    return new Result.Success<>(fakeUser);

                } else {
                    return new Result.Error(new IOException("Password is incorrect!"));
                }

            } else {
                return new Result.Error(new IOException("User not found!"));
            }

        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }
}