package com.gp.web.model;

import com.gp.dao.info.PostInfo;
import com.gp.info.CombineInfo;
import com.gp.svc.info.PostExt;
import com.gp.svc.info.UserLiteInfo;

import java.util.List;

/**
 * Created by garydiao on 2015-12-12
 */
public class PostItem {

    private long postId;

    private long workgroupId;

    private String owner;

    private String ownerAvatar;

    private String subject;

    private String content;

    private String excerpt;

    private String type;

    private String postTime;

    private String state;

    private String ownerName;

    private String workgroupName;

    private boolean commentOn;

    private String classification;

    private String priority;

    private int upvoteCount;

    public int getUpvoteCount() {
        return upvoteCount;
    }

    public void setUpvoteCount(int upvoteCount) {
        this.upvoteCount = upvoteCount;
    }

    private List<UserLiteInfo> attendees;

    private List<Comment> comments;

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public long getPostId() {
        return postId;
    }

    public void setPostId(long postId) {
        this.postId = postId;
    }

    public long getWorkgroupId() {
        return workgroupId;
    }

    public void setWorkgroupId(long workgroupId) {
        this.workgroupId = workgroupId;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getExcerpt() {
        return excerpt;
    }

    public void setExcerpt(String excerpt) {
        this.excerpt = excerpt;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPostTime() {
        return postTime;
    }

    public void setPostTime(String postTime) {
        this.postTime = postTime;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getWorkgroupName() {
        return workgroupName;
    }

    public void setWorkgroupName(String workgroupName) {
        this.workgroupName = workgroupName;
    }

    public boolean getCommentOn() {
        return commentOn;
    }

    public void setCommentOn(boolean commentOn) {
        this.commentOn = commentOn;
    }

    public List<UserLiteInfo> getAttendees() {
        return attendees;
    }

    public void setAttendees(List<UserLiteInfo> attendees) {
        this.attendees = attendees;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public String getOwnerAvatar() {
        return ownerAvatar;
    }

    public void setOwnerAvatar(String ownerAvatar) {
        this.ownerAvatar = ownerAvatar;
    }

}
