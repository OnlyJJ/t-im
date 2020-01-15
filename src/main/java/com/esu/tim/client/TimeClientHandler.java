package com.esu.tim.client;

import java.io.UnsupportedEncodingException;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class TimeClientHandler extends ChannelInboundHandlerAdapter {
	private static int count;
	
	/**
	 * 发送消息
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		for(int i=0; i<10; i++) {
			String reqMsg = "我是客户端 " + count++;
			System.err.println(reqMsg);
			try {
				byte[] reqMsgByte = reqMsg.getBytes("UTF-8");
				ByteBuf reqByteBuf = Unpooled.buffer(reqMsgByte.length);
				// 将指定的源数组的数据传输到缓冲区数组中
				reqByteBuf.writeBytes(reqMsgByte);
				// 将消息发送给服务器
				ctx.writeAndFlush(reqByteBuf);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 接收消息
	 */
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws UnsupportedEncodingException {
		// 将消息对象转换为netty的ByteBuf对象
//		ByteBuf buf = (ByteBuf) msg;
		// 获取缓冲区可读取字节数，并创建对应大小的数组
//		byte[] req = new byte[buf.readableBytes()];
		// 将缓冲区字节读到数组中
//		buf.readBytes(req);
		// 转换为字符串
//		String body = new String(req, "UTF-8");
		String body = (String) msg;
		System.out.println("now is :" + body);
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
