package stub;

import config.RpcClientConfig;
import dto.Request;
import dto.Response;
import serialize.Serializer;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.Socket;


/**
 * @description: the stub of client
 * @author: Stroke
 * @date: 2021/04/21
 */
public class ClientStub {

    public static Object getStub(Class clazz, Serializer serializer) {
        InvocationHandler h = new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                String serverIp = RpcClientConfig.getRpcServerIp();
                int serverPort = RpcClientConfig.getRpcServerPort();
                Socket socket = new Socket(serverIp, serverPort);

                // generate request and send to rpc server
                Request request = new Request();
                //request.setRequestId("1");
                request.setClassName(clazz.getName());
                request.setMethodName(method.getName());
                request.setParametersType(method.getParameterTypes());
                request.setParametersValue(args);
                serializer.serialize(request, socket.getOutputStream());

                // get response from rpc server
                Response response = serializer.deserialize(socket.getInputStream(), Response.class);
                socket.close();
                if (response.getError() == null) {
                    return response.getResult();
                } else {
                    return null;
                }
            }
        };

        Object object = Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, h);
        return object;
    }

    public static Object getStub(Class clazz){
        Serializer serializer = RpcClientConfig.getSerializer();
        return getStub(clazz, serializer);
    }
}
