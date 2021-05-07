package exception.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @description:
 * @author: Stroke
 * @date: 2021/04/29
 */
@Getter
@AllArgsConstructor
public enum ConfigurationErrorMessageEnum {
    MISS_CLIENT_CONFIGURATION_FILE("miss configuration file rpc-client-config.properties"),
    MISS_SERVER_CONFIGURATION_FILE("miss configuration file rpc-server-config.properties"),
    MISS_REGISTRY_SERVER_TYPE("miss registry server type"),
    MISS_REGISTRY_SERVER_ADDRESS("miss registry server address"),
    WRONG_PORT_FORMAT("wrong port format"),
    UNSUPPORTED_SERIALIZE_TYPE("unsupported serialize type");

    private final String message;
}