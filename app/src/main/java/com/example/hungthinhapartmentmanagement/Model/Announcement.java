package com.example.hungthinhapartmentmanagement.Model;

import com.google.firebase.Timestamp;
import java.util.List;

public class Announcement {
    private String id;
    private String content;
    private Timestamp createAt;
    private List<String> readBy;
    private String senderEmail;
    private String targetType;
    private String targetValue;
    private String title;

    public Announcement() {
    }

    public Announcement(String id, String content, Timestamp createAt, List<String> readBy,
                        String senderEmail, String targetType, String targetValue, String title) {
        this.id = id;
        this.content = content;
        this.createAt = createAt;
        this.readBy = readBy;
        this.senderEmail = senderEmail;
        this.targetType = targetType;
        this.targetValue = targetValue;
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Timestamp createAt) {
        this.createAt = createAt;
    }

    public List<String> getReadBy() {
        return readBy;
    }

    public void setReadBy(List<String> readBy) {
        this.readBy = readBy;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public String getTargetValue() {
        return targetValue;
    }

    public void setTargetValue(String targetValue) {
        this.targetValue = targetValue;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}