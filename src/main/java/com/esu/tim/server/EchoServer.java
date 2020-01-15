package com.esu.tim.server;

import java.security.cert.PKIXRevocationChecker.Option;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class EchoServer {
	public void bind(int port) throws Exception {
		EventLoopGroup boss = new NioEventLoopGroup();
		EventLoopGroup work = new NioEventLoopGroup();
		
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(boss, work).channel(NioServerSocketChannel.class)
//				.option(ChannelOption.SO_BACKLOG, 100)
//				.handler(new LoggingHandler(LogLevel.INFO))
				.childHandler(new ChannelInitializer<SocketChannel>() {

					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						// 分隔符处理粘包/拆包问题
//						ByteBuf delimiter = Unpooled.copiedBuffer("$_".getBytes());
//						ch.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, delimiter));
						
						// 固定长度处理粘包/拆包
//						ch.pipeline().addLast(new FixedLengthFrameDecoder(20));
						// 自定义长度
						ch.pipeline().addLast(new LengthFieldPrepender(4, false)); // 编码
						ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4)); // 解码
						// 转换字符
						ch.pipeline().addLast(new StringDecoder());
						
						ch.pipeline().addLast(new EchoServerHandler());
					}
					
				});
			ChannelFuture future = b.bind(port).sync();
			future.channel().closeFuture().sync();
		} finally {
			boss.shutdownGracefully();
			work.shutdownGracefully();
		}
	}
	
	public static void main(String[] args) {
		try {
			new EchoServer().bind(8080);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
