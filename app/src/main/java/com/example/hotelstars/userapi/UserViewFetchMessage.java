package com.example.hotelstars.userapi;

public interface UserViewFetchMessage {
    void onUpdateSuccess(UserModel message);
    void onUpdateFailure(String message);
}
