package com.bonree.common.util;

import com.bonree.common.code.CodeMessage;
import com.bonree.model.ClientResponseParamVO;
import com.bonree.model.consts.ServerConsts;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import org.apache.http.protocol.HTTP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 响应类
 */

public class ResponseUtil {

    private static Logger log = LoggerFactory.getLogger(ResponseUtil.class);

    public static void responseJson(ChannelHandlerContext ctx, HttpResponseStatus status, String jsonStr, String md5) {
        log.info("{}:  {} response: {}", md5,LogUtils.getLine(), jsonStr);

        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status, Unpooled.wrappedBuffer(jsonStr.getBytes()));
        response.headers().set(HTTP.CONTENT_TYPE, "text/json;charset=utf-8");
        response.headers().set(HTTP.CONTENT_LEN, response.content().readableBytes());
        response.headers().set(HTTP.CONN_DIRECTIVE, HTTP.CONN_KEEP_ALIVE);
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);//短连接
    }

    public static boolean isLack(Object result, ClientResponseParamVO clientResponseParamVO, CodeMessage codeMessage, String type) {
        if (result == null || "".equals(result)) {
            clientResponseParamVO.setCode(codeMessage.getCode());
            clientResponseParamVO.setReason(codeMessage.getMessage(type));
            return true;
        }
        return false;
    }
}
