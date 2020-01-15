package com.esu.tim.server;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelHandler.Sharable;

public class TimerServerHandler extends ChannelInboundHandlerAdapter  {
	private static int count;
	
	/**
	 * 接收客户端消息
	 */
	@Override
	public void channelRead(ChannelHandlerContext cx, Object msg) throws UnsupportedEncodingException {
		System.err.println("开始读取消息。。。。");
		// 将消息内容转换为netty的ByteBuf对象，它提供更强大和灵活的功能
//		ByteBuf buf = (ByteBuf) msg;
		// 从缓冲区获取可读取的字节数，并创建对应大小的字节数组
//		byte[] req = new byte[buf.readableBytes()];
		// 将缓冲区的字节复制到新建的数组中
//		buf.readBytes(req);
		// 转换为字符串
//		String body = new String(req, "UTF-8");
		String body = (String) msg;
		System.out.println("this is server time:" + body + count++);
		String currentTime = "quer-time".equalsIgnoreCase(body) ? new Date(System.currentTimeMillis()).toString() : "bad time";
		
		ByteBuf resp = Unpooled.copiedBuffer(currentTime.getBytes());
		// 异步发送应答消息给客户端
		// write方法不会直接将消息写入SocketChannel，只是把待发送的消息写入缓冲数组中
//		cx.write(resp);
		cx.writeAndFlush(resp);
	}
	
	/**
	 *  将缓冲数组中的待发送消息推送到SocketChannel中
	 */
	@Override
	public void channelReadComplete(ChannelHandlerContext cx) {
		cx.flush();
	}
	
	/**
	 * 异常处理
	 * 当发生异常时，关闭 ChannelHandlerContext，释放和它相关联的句柄等资源
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ctx.close();
	}
	
}
