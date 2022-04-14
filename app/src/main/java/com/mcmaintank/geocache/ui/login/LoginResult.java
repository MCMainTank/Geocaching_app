package com.mcmaintank.geocache.ui.login;

import androidx.annotation.Nullable;

/**
 * Authentication result : success (user details) or error message.
 */
class LoginResult {
    @Nullable
    private LoggedInUserView success=null;
    @Nullable
    private Integer error;

    private Integer loggedInUserGroup;

    LoginResult(@Nullable Integer error) {
        this.error = error;
    }

    LoginResult(@Nullable LoggedInUserView success) {
        this.success = success;
    }

    @Nullable
    LoggedInUserView getSuccess() {
        return success;
    }

    @Nullable
    Integer getError() {
        return error;
    }
}