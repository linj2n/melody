package cn.linj2n.melody.web.errors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    private MessageSource message;

    public RestResponseEntityExceptionHandler() {
        super();
    }

    // 409
//    @ExceptionHandler({ UserAlreadyExistException.class})
//    public ResponseEntity<Object> handleUserAlreadyExist(final RuntimeException ex, final WebRequest request) {
//        logger.error("409 Status Code", ex);
//        final ErrorResponse bodyOfResponse =
//                new ErrorResponse(HttpStatus.CONFLICT.value(), "UserAlreadyExist", message.getMessage("message.emailOrLogin.alreadyExist",null,request.getLocale()));
//        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.CONFLICT, request);
//    }
}
