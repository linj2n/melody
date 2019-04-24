package cn.linj2n.melody.web.utils;

import cn.linj2n.melody.web.dto.ResponseDTO;
import cn.linj2n.melody.web.dto.support.ResponseCode;
import org.springframework.http.HttpStatus;

import javax.xml.ws.Response;

public class ResponseBuilder {
    private static final String DEFAULT_SUCCESS_STATUS = "success";
    private static final String DEFAULT_FAILED_STATUS = "fail";

    public static ResponseDTO buildSuccessResponse() {
        return new ResponseDTO()
                .setCode(ResponseCode.SUCCESS)
                .setMessage(DEFAULT_SUCCESS_STATUS);
    }

    public static ResponseDTO buildSuccessResponse(String message) {
        return new ResponseDTO()
                .setCode(ResponseCode.SUCCESS)
                .setMessage(message);
    }

    public static <T> ResponseDTO<T> buildSuccessResponse(String message, T data) {
        return new ResponseDTO()
                .setCode(ResponseCode.SUCCESS)
                .setMessage(message)
                .setData(data);
    }

    public static <T> ResponseDTO<T> buildSuccessResponse(T data) {
        return new ResponseDTO()
                .setCode(ResponseCode.SUCCESS)
                .setMessage(DEFAULT_SUCCESS_STATUS)
                .setData(data);
    }

    public static ResponseDTO buildFailedResponse() {
        return new ResponseDTO()
                .setCode(ResponseCode.FAIL)
                .setMessage(DEFAULT_FAILED_STATUS);
    }

    public static ResponseDTO buildFailedResponse(ResponseCode responseCode) {
        return new ResponseDTO()
                .setCode(responseCode)
                .setMessage(DEFAULT_FAILED_STATUS);
    }


    public static ResponseDTO buildFailedResponse(String message) {
        return new ResponseDTO()
                .setCode(ResponseCode.FAIL)
                .setMessage(message);
    }

    public static <T> ResponseDTO<T> buildFailedResponse(String message, T data) {
        return new ResponseDTO()
                .setCode(ResponseCode.FAIL)
                .setMessage(message)
                .setData(data);
    }

    public static <T> ResponseDTO<T> buildFailedResponse(T data) {
        return new ResponseDTO()
                .setCode(ResponseCode.FAIL)
                .setData(data);
    }
    
}
