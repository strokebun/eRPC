package serialize.factory;

import exception.ConfigurationException;
import exception.enums.ConfigurationErrorMessageEnum;
import serialize.serializer.socket.HessianSerializer;
import serialize.serializer.both.KryoSerializer;
import serialize.serializer.socket.SocketSerializer;
import serialize.serializer.socket.JdkSerializer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description: Singleton factory of io stream serializer.
 * @author: Stroke
 * @date: 2021/04/22
 */
public final class SocketSerializerFactory {
    private static Map<SerializationTypeEnum, SocketSerializer> SERIALIZER_MAP = new ConcurrentHashMap<SerializationTypeEnum, SocketSerializer>() {
        {
            put(SerializationTypeEnum.KRYO, new KryoSerializer());
            put(SerializationTypeEnum.HESSIAN, new HessianSerializer());
            put(SerializationTypeEnum.JDK, new JdkSerializer());
        }
    };

    public static SocketSerializer getInstance(SerializationTypeEnum type) {
        final SocketSerializer serializer = SERIALIZER_MAP.get(type);
        if (serializer == null) {
            throw new ConfigurationException(ConfigurationErrorMessageEnum.UNSUPPORTED_SERIALIZE_TYPE);
        }
        return serializer;
    }

    private SocketSerializerFactory() {

    }
}