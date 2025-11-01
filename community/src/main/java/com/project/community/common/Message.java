package com.project.community.common;

public enum Message {

    SIGNUP_SUCCESS("signup success"),
    SIGNIN_SUCCESS("sign in success"),
    USER_INFO_FETCHED("user info fetched"),
    PROFILE_UPDATE_SUCCESS("profile updated"),
    PASSWORD_UPDATE_SUCCESS("password updated"),
    WITHDRAWAL_SUCCESS("withdrawal success"),
    POST_PUBLISHED("post published"),
    POST_FETCHED("post fetched"),
    POST_LIST_FETCHED("post list fetched"),
    POST_UPDATE_SUCCESS("post updated"),
    POST_DELETE_SUCCESS("post deleted"),
    COMMENT_POST_SUCCESS("comment posted"),
    POST_COMMENT_FETCHED("post's comments fetched"),
    COMMENT_UPDATED("comment updated"),
    COMMENT_DELETED("comment deleted"),
    POST_LIKE_REGISTERED("like registered."),
    POST_LIKE_IS_LIKED_FETCHED("like info fetched"),
    POST_LIKE_CANCELED("like canceled."),
    POST_VIEW_COUNT_UPDATED("Post view count updated."),
    PROFILE_IMAGE_POST_SUCCESS("Profile image is uploaded."),
    PROFILE_IMAGE_RETURNED("Profile image is fetched."),
    PROFILE_IMAGE_UPDATED("Profile image is updated."),
    PROFILE_IMAGE_NOT_SET("Profile image is not set."),
    POST_IMAGE_UPLOADED("Post image is uploaded."),
    POST_IMAGE_NOT_SET("Post image is not set."),
    POST_IMAGE_RETURNED("Post image is returned."),
    POST_IMAGE_UPDATED("Post image is updated."),
    SIGNOUT_SUCCESS("Signed out.");


    private final String message;

    Message(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
