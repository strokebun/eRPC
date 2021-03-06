package dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @description: RPC response from server.
 * @author: Stroke
 * @date: 2021/04/18
 */
@Getter
@Setter
public class Response implements Serializable {
    private String responseId;
    private String error;
    private Object result;
}
