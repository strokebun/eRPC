package stub;

import config.RpcServerConfig;
import dto.Request;
import dto.Response;
import serialize.Serializer;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * @description: the stub of server
 * @author: Stroke
 * @date: 2021/04/21
 */
public final class ServerStub {

    private boolean running;
    private Map<String, String> registerTable;
    private Serializer serializer;

    public ServerStub() {
        running = true;
        registerTable = new HashMap<>();
        serializer = RpcServerConfig.getSerializer();
    }

    public void register(String interfaceName, String implementName) {
        registerTable.put(interfaceName, implementName);
    }

    public void run() throws IOException, ClassNotFoundException {
        ServerSocket serverSocket = new ServerSocket(RpcServerConfig.getRpcServerPort());
        while(running){
            Socket client = serverSocket.accept();
            process(client);
            client.close();
        }
        serverSocket.close();
    }

    public void process(Socket client) throws IOException, ClassNotFoundException {
        Request request = serializer.deserialize(client.getInputStream(), Request.class);
        String className = request.getClassName();
        request.setClassName(registerTable.get(className));
        Response response = getResponse(request);
        serializer.serialize(response, client.getOutputStream());
    }

    private static Response getResponse(Request request) {
        Object result = null;
        Response response = new Response();
        try {
            result = invoke(request);
        } catch (ClassNotFoundException e) {
            response.setError("class not found");
        } catch (NoSuchMethodException e) {
            response.setError("method not found");
        } catch (Exception e){
            response.setError("function inner error");
        } finally {
            response.setResult(result);
        }
        return response;
    }

    private static Object invoke(Request request) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {
        String className = request.getClassName();
        String methodName = request.getMethodName();
        Class clazz = Class.forName(className);
        Object object = clazz.newInstance();
        Method method = clazz.getMethod(methodName, request.getParametersType());
        return method.invoke(object, request.getParametersValue());
    }
}
