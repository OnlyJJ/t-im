package com.esu.tim.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

@Sharable
public class ChildChannelHandler extends ChannelInitializer<SocketChannel>{

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		// TODO Auto-generated method stub
		ch.pipeline().addLast(new LineBasedFrameDecoder(1024));
		ch.pipeline().addLast(new StringDecoder());
		ch.pipeline().addLast(new TimerServerHandler());
	}

}
