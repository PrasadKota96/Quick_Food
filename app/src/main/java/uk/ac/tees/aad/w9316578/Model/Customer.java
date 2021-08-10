package uk.ac.tees.aad.w9316578.Model;

import java.io.Serializable;

public class Customer implements Serializable {

    private String address,phone,userId,userType,username,profileImage;


    public Customer() {
    }

    public Customer(String address, String phone, String userId, String userType, String username, String profileImage) {
        this.address = address;
        this.phone = phone;
        this.userId = userId;
        this.userType = userType;
        this.username = username;
        this.profileImage = profileImage;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
