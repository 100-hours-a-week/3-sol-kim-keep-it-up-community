package com.project.community.common;

public enum ErrorMessage {

    EMAIL_CONFLICT("Email has already been taken"),
    NICKNAME_CONFLICT("Nickname has already been taken"),
    USER_NOT_FOUND("User not found"),
    WRONG_PASSWORD("Wrong Password"),
    WRONG_EMAIL("Wrong Email"),
    USER_GONE("User has withdrawn"),
    USER_ALREADY_GONE("User has already withdrawn"),
    POST_NOT_FOUND("Post not found"),
    POST_GONE("Post has been deleted."),
    COMMENT_NOT_FOUND("Comment not found."),
    COMMENT_ALREADY_GONE("Already deleted comment"),
    ALREADY_LIKED("Already liked."),
    LIKE_NOT_FOUND("No Like to be canceled."),
    SIGNIN_NEEDED("Need to sign in."),
    SIGNIN_AGAIN("Session has expired. Sign in again.");

    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
