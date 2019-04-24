package cn.linj2n.melody.web.dto.support;

public enum ResponseCode {
    /**
     * 成功
     */
    SUCCESS(20000),
    /**
     * 失败
     */
    FAIL(40000),
    /**
     * 没有权限
     */
    UNAUTHORIZED(40001),
    /**
     * 接口不存在
     */
    NOT_FOUND(40004),
    /**
     * 服务器错误
     */
    INTERNAL_SERVER_ERROR(50000);

    private final int code;

    ResponseCode(int code) {
        this.code = code;
    }

    public int code() {
        return code;
    }
}