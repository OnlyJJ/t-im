package com.esu.tim.server;

import java.util.Random;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelHandler.Sharable;

@Sharable
public class EchoServerHandler extends ChannelInboundHandlerAdapter  {
	private static int count;
	
	@Override
	public void channelRead(ChannelHandlerContext cxt, Object msg) {
		String body = (String) msg;
		System.err.println("this is " + ++count + " times receives client: [" + body + "]");
		body += new Random().nextInt(1000);
		byte[] resp = body.getBytes();
		ByteBuf buf = Unpooled.buffer();
		buf.writeBytes(resp, 0, resp.length);
		cxt.writeAndFlush(buf);
	}
	
}
