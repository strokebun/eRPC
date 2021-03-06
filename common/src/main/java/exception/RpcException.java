package exception;

import exception.enums.RpcErrorMessageEnum;

/**
 * @description: Exception caused by RPC invoke.
 * @author: Stroke
 * @date: 2021/04/29
 */
public class RpcException extends RuntimeException {

    public RpcException(RpcErrorMessageEnum rpcErrorMessageEnum, String detail) {
        super(rpcErrorMessageEnum.getMessage() + ":" + detail);
    }

    public RpcException(RpcErrorMessageEnum rpcErrorMessageEnum) {
        super(rpcErrorMessageEnum.getMessage());
    }

    public RpcException(String message) {
        super(message);
    }
}
