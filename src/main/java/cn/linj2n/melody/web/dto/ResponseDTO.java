package cn.linj2n.melody.web.dto;

public class ResponseDTO <T>{
    private String status;
    private String message;
    private T data;


    public String getStatus() {
        return status;
    }

    public ResponseDTO setStatus(String status) {
        this.status = status;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public ResponseDTO setMessage(String message) {
        this.message = message;
        return this;
    }

    public T getData() {
        return data;
    }

    public ResponseDTO setData(T data) {
        this.data = data;
        return this;
    }
}
