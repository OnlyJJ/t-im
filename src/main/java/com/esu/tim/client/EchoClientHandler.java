package com.esu.tim.client;

import java.io.UnsupportedEncodingException;
import java.util.Random;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class EchoClientHandler extends ChannelInboundHandlerAdapter  {
	private static int count;
	
	public EchoClientHandler() {}
	
	@Override
	public void channelActive(ChannelHandlerContext cxt) {
		for(int i=0; i<10; i++) {
			String reqMsg = "我是客户端" + new Random().nextInt(1000000);
			System.err.println(reqMsg);
			try {
				byte[] data = reqMsg.getBytes();
				ByteBuf reqByteBuf = Unpooled.buffer();
				reqByteBuf.writeBytes(data, 0, data.length);
				// 将消息发送给服务器
				cxt.writeAndFlush(reqByteBuf);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws UnsupportedEncodingException {
		System.err.println("this is " + ++count + " times receives msg [" + msg + "]");
	}
	
}
