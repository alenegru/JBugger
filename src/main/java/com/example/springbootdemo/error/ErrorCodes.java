package com.example.springbootdemo.error;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ErrorCodes {
    UNHANDLED_ERROR(0, "Unexpected exception."),
    USERNAME_PASSWORD_INCORRECT(1, "Username or password are incorrect."),
    VALIDATION_FAILED(2, "Input field could not be validated."),
    USER_NOT_FOUND(3, "User was not found in the database."),
    ENTITY_NOT_FOUND(4, "Entity was not found in the database."),
    MAIL_NOT_SENT(5, "Mail could not be sent."),
    CONSTRAINT_VIOLATION(6, "Database constraint was violated."),
    INVALID_STATE_TRANSITION(7, "Bug state transition was invalid."),
    MAX_FILE_SIZE_EXCEEDED(8, "File was larger than the max size."),
    NULL_ENTITY(9, "The given entity was null."),
    PERMISSION_NOT_FOUND(10, "You don't have permission to do that."),
    UNCLOSED_BUGS(11, "User has unclosed bugs and can not be deactivated."),
    TRANSACTION_COMMIT_FAILED(12, "Transaction could not be committed. Check for constraint failures."),
    ASSIGNED_USER_NULL(13, "Assigned user can not be empty."),
    USER_INACTIVE(14, "User is inactive."),
    TOO_MANY_FAILED_ATTEMPTS(15, "Too many failed attempts.")

    ;

    private final int code;
    private final String description;

    ErrorCodes(int code, String description){
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "Error" + code + ": " + description;
    }


}
