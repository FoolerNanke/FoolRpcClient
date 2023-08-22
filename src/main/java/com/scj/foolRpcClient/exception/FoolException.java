package com.scj.foolRpcClient.exception;

/**
 * @author suchangjie.NANKE
 * @Title: FoolException
 * @date 2023/8/22 22:20
 * @description 异常类
 */
public class FoolException extends RuntimeException{

    /**
     * 错误码
     */
    public String errorCode;

    /**
     * 错误信息
     */
    public String errorMessage;

    public FoolException(String errorCode, String errorMessage, Throwable throwable){
        super(throwable);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public FoolException(ExceptionEnum exceptionEnum, Throwable throwable){
        super(throwable);
        this.errorCode = exceptionEnum.getErrorCode();
        this.errorMessage = exceptionEnum.getErrorMessage();
    }

    public FoolException(ExceptionEnum exceptionEnum){
        super(exceptionEnum.getErrorMessage());
        this.errorCode = exceptionEnum.getErrorCode();
        this.errorMessage = exceptionEnum.getErrorMessage();
    }

}
