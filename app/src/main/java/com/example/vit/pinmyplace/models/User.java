package com.example.vit.pinmyplace.models;


import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("name")
    public String name;
    @SerializedName("email")
    public String email;
    @SerializedName("id")
    public String facebookId;
    @SerializedName("gender")
    public String gender;

    @Override
    public String toString() {
        return "User - " + name + ", email - " + email +
                ".\nfacebookId - " + facebookId + ", gender - " + gender;
    }
}
