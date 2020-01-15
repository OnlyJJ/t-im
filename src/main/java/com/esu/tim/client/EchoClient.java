package com.esu.tim.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;

public class EchoClient {
	public void connect(String host, int port) throws Exception {
		EventLoopGroup group = new NioEventLoopGroup();
		Bootstrap b = new Bootstrap();
		try {
			b.group(group).channel(NioSocketChannel.class)
//				.option(ChannelOption.TCP_NODELAY, true)
				.handler(new ChannelInitializer<SocketChannel>() {
	
					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
//						ByteBuf delimiter = Unpooled.copiedBuffer("$_".getBytes());
//						ch.pipeline().addLast(new DelimiterBasedFrameDecoder(10024, delimiter));
						// 自定义长度
						// 编码，长度为4，则通过writeInt写入，即最大长度为Integer.MAX_VALUE
						ch.pipeline().addLast(new LengthFieldPrepender(4, false)); 
						// 解码，可通过调整各参数，实现复杂的协议，此处未考虑压缩情况
						ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4)); 
						ch.pipeline().addLast(new StringDecoder());
						ch.pipeline().addLast(new EchoClientHandler());
					}
				});
			ChannelFuture f = b.connect(host, port).sync();
			
			f.channel().closeFuture().sync();
		} finally {
			group.shutdownGracefully();
		}
	}
	
	public static void main(String[] args) {
		try {
			new EchoClient().connect("127.0.0.1", 8080);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
