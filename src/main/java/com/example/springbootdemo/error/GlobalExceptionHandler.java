package com.example.springbootdemo.error;

import com.example.springbootdemo.error.exceptions.*;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSendException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityNotFoundException;
import javax.xml.bind.ValidationException;
import java.time.LocalDateTime;


@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllUncaughtException(Exception exception, WebRequest request) {
        exception.printStackTrace();
        return new ResponseEntity<>(createApiErrorCurrentTime(ErrorCodes.UNHANDLED_ERROR, exception.getMessage()),
                                    HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(TooManyFailedAttemptException.class)
    public ResponseEntity<Object> tooManyFailedAttemptsException(Exception exception, WebRequest request) {
        exception.printStackTrace();
        return new ResponseEntity<>(createApiErrorCurrentTime(ErrorCodes.TOO_MANY_FAILED_ATTEMPTS, exception.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({ ValidationException.class })
    public ResponseEntity<Object> handleValidationException(Exception exception){
        exception.printStackTrace();
        return new ResponseEntity<>(createApiErrorCurrentTime(ErrorCodes.VALIDATION_FAILED, exception.getMessage()),
                                    HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ DataIntegrityViolationException.class })
    public ResponseEntity<Object> handleConstraintViolationException(Exception exception){
        exception.printStackTrace();
        return new ResponseEntity<>(createApiErrorCurrentTime(ErrorCodes.CONSTRAINT_VIOLATION, exception.getMessage()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ UserPasswordIncorrectException.class })
    protected ResponseEntity<Object> handleUsernamePasswordIncorrectException(Exception exception) {
        exception.printStackTrace();
        return new ResponseEntity<>(createApiErrorCurrentTime(ErrorCodes.USERNAME_PASSWORD_INCORRECT, exception.getMessage()),
                                    HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({UsernameNotFoundException.class})
    public ResponseEntity<Object> handleUsernameNotFoundException(Exception exception){
        exception.printStackTrace();
        return new ResponseEntity<>(createApiErrorCurrentTime(ErrorCodes.USER_NOT_FOUND, exception.getMessage()),
                                    HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({EntityNotFoundException.class})
    public ResponseEntity<Object> handleEntityNotFoundException(Exception exception){
        exception.printStackTrace();
        return new ResponseEntity<>(createApiErrorCurrentTime(ErrorCodes.ENTITY_NOT_FOUND, exception.getMessage()),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({MailSendException.class})
    public ResponseEntity<Object> handleMailSendException(Exception exception){
        exception.printStackTrace();
        return new ResponseEntity<>(createApiErrorCurrentTime(ErrorCodes.MAIL_NOT_SENT, exception.getMessage()),
                HttpStatus.REQUEST_TIMEOUT);
    }

    @ExceptionHandler({UnclosedBugsException.class})
    public ResponseEntity<Object> handleUnclosedBugsException(Exception exception){
        exception.printStackTrace();
        return new ResponseEntity<>(createApiErrorCurrentTime(ErrorCodes.UNCLOSED_BUGS, exception.getMessage()),
                HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler({TransactionSystemException.class})
    public ResponseEntity<Object> handleTransactionSystemException(Exception exception){
        exception.printStackTrace();
        return new ResponseEntity<>(createApiErrorCurrentTime(ErrorCodes.TRANSACTION_COMMIT_FAILED, exception.getMessage()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({NullPointerException.class})
    public ResponseEntity<Object> handleNullPointerException(Exception exception){
        exception.printStackTrace();
        return new ResponseEntity<>(createApiErrorCurrentTime(ErrorCodes.NULL_ENTITY, exception.getMessage()),
                HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler({AssignedUserNullException.class})
    public ResponseEntity<Object> handleAssignedUserNullException(Exception exception){
        exception.printStackTrace();
        return new ResponseEntity<>(createApiErrorCurrentTime(ErrorCodes.ASSIGNED_USER_NULL, exception.getMessage()),
                HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<?> handleMaxSizeException(MaxUploadSizeExceededException exception) {
        return new ResponseEntity<>(createApiErrorCurrentTime(ErrorCodes.MAX_FILE_SIZE_EXCEEDED, exception.getMessage()),
                HttpStatus.PAYLOAD_TOO_LARGE);
    }

    @ExceptionHandler({PermissionNotFoundException.class})
    public ResponseEntity<Object> handleClosingBugs(Exception exception){
        exception.printStackTrace();
        return new ResponseEntity<>(createApiErrorCurrentTime(ErrorCodes.PERMISSION_NOT_FOUND, exception.getMessage()),
                HttpStatus.REQUEST_TIMEOUT);

    }

    @ExceptionHandler({UserInactiveException.class})
    public ResponseEntity<Object> handleUserInactiveException(Exception exception){
        exception.printStackTrace();
        return new ResponseEntity<>(createApiErrorCurrentTime(ErrorCodes.USER_INACTIVE, exception.getMessage()),
                HttpStatus.REQUEST_TIMEOUT);

    }

    //Creates an ApiError with the current time
    private ApiError createApiErrorCurrentTime(ErrorCodes code, String msg){
        return new ApiError(LocalDateTime.now(), code, msg);
    }

}
