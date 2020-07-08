package com.example.instagrame.Model;

public class User {
    private String name;
    private String userName;
    private String password;
    private String id;
    private String imageUrl;
    private String bio;
    private String confirmPassword;
    private String email;

    public User() {

    }

    public User(String name, String userName, String password, String id, String imageUrl, String bio, String confirmPassword, String email) {
        this.name = name;
        this.userName = userName;
        this.password = password;
        this.id = id;
        this.imageUrl = imageUrl;
        this.bio = bio;
        this.confirmPassword = confirmPassword;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
