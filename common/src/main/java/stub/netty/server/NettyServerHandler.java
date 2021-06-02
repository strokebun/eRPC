package stub.netty.server;

import constants.RpcMessageBodyConstants;
import constants.enums.RpcMessageTypeEnum;
import dto.Request;
import dto.Response;
import dto.RpcMessage;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;

/**
 * @description:
 * @author: Stroke
 * @date: 2021/05/25
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    private NettyRpcServerStub serverStub;

    public NettyServerHandler(NettyRpcServerStub serverStub) {
        this.serverStub = serverStub;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try {
            RpcMessage message = (RpcMessage) msg;
            byte messageType = message.getMessageType();

            RpcMessage rpcMessage = RpcMessage.builder()
                    .serializationCode(serverStub.getSerializationType().getCode())
                    .compressionCode(serverStub.getCompressionType().getCode())
                    .build();
            if (RpcMessageTypeEnum.HEART_BEAT_REQUEST.getCode() == messageType) {
                rpcMessage.setMessageType(RpcMessageTypeEnum.HEART_BEAT_RESPONSE.getCode());
                rpcMessage.setData(RpcMessageBodyConstants.PONG);
            } else {
                // invoke the method
                Request request = (Request) message.getData();
                String className = request.getClassName();
                request.setClassName(serverStub.getRegisterTable().get(className));
                Response response = serverStub.getResponse(request);
                rpcMessage.setMessageType(RpcMessageTypeEnum.RESPONSE.getCode());
                rpcMessage.setData(response);
            }
            ctx.writeAndFlush(rpcMessage).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleState state = ((IdleStateEvent) evt).state();
            if (state == IdleState.READER_IDLE) {
                System.out.println("idle check happen, so close the connection");
                ctx.close();
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

}
