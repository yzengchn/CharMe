package com.yzeng.charme.netty;

import java.util.Date;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;

public class NettyClient {
	public static void main(String[] args) {
		Bootstrap bootstrap = new Bootstrap();
		NioEventLoopGroup group = new NioEventLoopGroup();
		bootstrap.group(group)
				 .channel(NioSocketChannel.class)
				 .handler(new ChannelInitializer<Channel>() {

					@Override
					protected void initChannel(Channel ch) throws Exception {
						ch.pipeline().addLast(new StringEncoder());
					}
				});
		
		Channel channel = bootstrap.connect("127.0.0.1", 8888).channel();
		
		while(true) {
			channel.writeAndFlush(new Date()+" :msg");
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
