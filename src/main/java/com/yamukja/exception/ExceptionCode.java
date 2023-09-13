package com.yamukja.exception;

import lombok.Getter;

@Getter
public enum ExceptionCode {
    MEMBER_NOT_FOUND(404, "Member is not found"),
    MEMBER_STATUS_SLEEP(403, "MemberStatus is sleep"),
    MEMBER_STATUS_QUIT(404, "MemberStatus is quit"),
    MEMBER_EXISTS(409, "Member already exists");


    private final int status;
    private final String message;

    ExceptionCode(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
