package cn.linj2n.melody.web.errors;

import cn.linj2n.melody.web.dto.ResponseDTO;
import cn.linj2n.melody.web.utils.ResponseBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class RestResponseEntityExceptionHandler  {

    @Autowired
    private MessageSource message;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseDTO<?> processValidationError(MethodArgumentNotValidException ex) {
        List<FieldErrorDTO> errorDTOS =
                ex.getBindingResult().getFieldErrors()
                        .stream()
                        .map(error -> new FieldErrorDTO(error.getField(), error.getDefaultMessage()))
                        .collect(Collectors.toList());
        return ResponseBuilder.buildFailedResponse("字段错误", errorDTOS);
    }
}
