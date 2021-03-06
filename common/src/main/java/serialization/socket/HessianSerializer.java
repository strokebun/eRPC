package serialization.socket;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import exception.DeserializationException;
import exception.SerializationException;

import java.io.*;

/**
 * @description: Serializer based on Hessian.
 * @author: Stroke
 * @date: 2021/04/22
 */
public class HessianSerializer implements SocketSerializer {

    @Override
    public void serialize(Object obj, OutputStream os) throws SerializationException {
        Hessian2Output output = new Hessian2Output(os);
        try {
            output.writeObject(obj);
            output.flush();
        } catch (IOException e) {
            throw new SerializationException(this.getClass().getSimpleName() + " " + e.getCause());
        }
    }

    @Override
    public <T> T deserialize(InputStream is, Class<T> clazz) throws SerializationException {
        try {
            Hessian2Input input = new Hessian2Input(is);
            return (T)input.readObject();
        } catch (IOException e) {
            throw new DeserializationException(this.getClass().getSimpleName() + " " + e.getCause());
        }
    }

}
