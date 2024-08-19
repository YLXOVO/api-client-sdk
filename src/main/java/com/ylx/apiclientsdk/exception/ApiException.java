package com.ylx.apiclientsdk.exception;

/**
 * 自定义API异常类，用于处理API调用中出现的异常情况。
 * 继承自Exception，提供了额外的错误码和错误消息功能，
 * 以便API客户端能够根据错误码快速定位问题。
 */
public class ApiException extends Exception{
    // 序列化UID，用于异常对象的序列化和反序列化。
    private static final long serialVersionUID = 1L;
    // 错误码，用于快速定位异常原因。
    private int code ;

    /**
     * 构造函数，根据错误码和错误消息初始化ApiException。
     *
     * @param code 错误码，用于快速定位异常原因。
     * @param message 异常的详细描述。
     */
    public ApiException(int code,String message){
        super(message);
        this.code = code;
    }

    /**
     * 构造函数，根据另一个异常和错误消息初始化ApiException。
     *
     * @param message 异常的详细描述。
     * @param cause 引发当前异常的原始异常。
     */
    public ApiException(String message ,Throwable cause){
        super(message,cause);
    }

    /**
     * 构造函数，根据错误码对象初始化ApiException，错误消息从ErrorCode对象中获取。
     *
     * @param errorCode 包含错误码和错误消息的枚举对象。
     */
    public ApiException(ErrorCode errorCode){
        super(errorCode.getMessage());
        this.code=errorCode.getCode();
    }

    /**
     * 构造函数，根据错误码对象和自定义错误消息初始化ApiException。
     *
     * @param errorCode 包含错误码和默认错误消息的枚举对象。
     * @param message 自定义的错误消息，用于详细描述异常情况。
     */
    public ApiException(ErrorCode errorCode,String message){
        super(message);
        this.code= errorCode.getCode();
    }

    /**
     * 获取错误码。
     *
     * @return 异常对应的错误码。
     */
    public int getCode(){
        return code ;
    }
}
