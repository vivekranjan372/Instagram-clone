package com.example.instagrame.Model;

public class Post {
    private String imageUrl;
    private String postId;
    private String description;
    private String publisher;

    public Post() {
    }

    public Post(String imageUrl, String postId, String description, String publisher) {
        this.imageUrl = imageUrl;
        this.postId = postId;
        this.description = description;
        this.publisher = publisher;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
}
