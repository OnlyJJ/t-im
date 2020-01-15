package com.esu.tim.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class TimerServer {
	public void bind(int port) {
		// boss线程组用于接收客户端连接
		EventLoopGroup boss = new NioEventLoopGroup();
		// work用于处理网络事件
		EventLoopGroup work = new NioEventLoopGroup();
		try {
			// NIO服务端辅助启动类
			ServerBootstrap b = new ServerBootstrap();
			b.group(boss, work)
				.channel(NioServerSocketChannel.class)
				// 设置TCP参数
				.option(ChannelOption.SO_BACKLOG, 1024)
				// 绑定I/O事件处理类
				.childHandler(new ChildChannelHandler()); 
			// 绑定监听端口，同步阻塞等待绑定完成，返回异步future，等待回调结果
			ChannelFuture future = b.bind(port).sync();
			System.err.println("server 启动完成，开始监听连接。。。");
			// 同步等待服务端链路关闭后，主函数才退出
			future.channel().closeFuture().sync();
		} catch(Exception e){
		} finally {
			// 优雅的释放线程资源
			boss.shutdownGracefully();
			work.shutdownGracefully();
		}
		System.err.println("server关闭！");
	}
	
	public static void main(String[] args) {
		int port = 8080;
		new TimerServer().bind(port);
	}
}
