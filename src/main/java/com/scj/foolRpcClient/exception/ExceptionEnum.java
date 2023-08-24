package com.scj.foolRpcClient.exception;

/**
 * @author suchangjie.NANKE
 * @Title: ExceptionEnum
 * @date 2023/8/22 22:25
 * @description 异常枚举常量
 */
public enum ExceptionEnum {

    GENERATE_CLIENT_FAILED("GENERATE_CLIENT_FAILED", "建立远程链接客户端失败"),

    CALL_BACK_METHOD_ERROR("CALL_BACK_METHOD_ERROR", "回调函数填写异常"),

    ILLEGAL_ACCESS("ILLEGAL_ACCESS", "无法访问该Bean"),

    PROTOCOL_NOT_MATCH("PROTOCOL_NOT_MATCH", "消息协议不匹配"),

    METHOD_NOT_EXIST("METHOD_NOT_EXIST", "请求方法不存在"),

    REGISTER_ADDRESS_ERROR("REGISTER_ADDRESS_ERROR", "注册中心地址错误"),

    GET_REMOTE_SERVER_ERROR("GET_REMOTE_SERVER_ERROR", "获取服务提供地址错误"),

    ;

    final String errorCode;

    final String errorMessage;

    ExceptionEnum(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
