package com.example.barry215.greendao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table "COMMENT_CACHE".
 */
public class CommentCache {

    private Long id;
    private Long helpId;
    private Long parentId;
    private String commentClass;
    private String commentBody;
    private String commentUser;
    private String commentedUser;
    private java.util.Date commentTime;

    public CommentCache() {
    }

    public CommentCache(Long id) {
        this.id = id;
    }

    public CommentCache(Long id, Long helpId, Long parentId, String commentClass, String commentBody, String commentUser, String commentedUser, java.util.Date commentTime) {
        this.id = id;
        this.helpId = helpId;
        this.parentId = parentId;
        this.commentClass = commentClass;
        this.commentBody = commentBody;
        this.commentUser = commentUser;
        this.commentedUser = commentedUser;
        this.commentTime = commentTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getHelpId() {
        return helpId;
    }

    public void setHelpId(Long helpId) {
        this.helpId = helpId;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getCommentClass() {
        return commentClass;
    }

    public void setCommentClass(String commentClass) {
        this.commentClass = commentClass;
    }

    public String getCommentBody() {
        return commentBody;
    }

    public void setCommentBody(String commentBody) {
        this.commentBody = commentBody;
    }

    public String getCommentUser() {
        return commentUser;
    }

    public void setCommentUser(String commentUser) {
        this.commentUser = commentUser;
    }

    public String getCommentedUser() {
        return commentedUser;
    }

    public void setCommentedUser(String commentedUser) {
        this.commentedUser = commentedUser;
    }

    public java.util.Date getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(java.util.Date commentTime) {
        this.commentTime = commentTime;
    }

}
