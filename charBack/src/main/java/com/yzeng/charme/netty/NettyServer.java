package com.yzeng.charme.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;

public class NettyServer {
	public static void main(String[] args) {
		//bossGroup表示监听端口，accept 新连接的线程组
		NioEventLoopGroup bossGroup = new NioEventLoopGroup();
		//workerGroup表示处理每一条连接的数据读写的线程组
		NioEventLoopGroup workGroup = new NioEventLoopGroup();
		//创建了一个引导类 ServerBootstrap，这个类将引导我们进行服务端的启动工作	
		ServerBootstrap bootstrap = new ServerBootstrap();
		
		bootstrap.group(bossGroup, workGroup)
				 .channel(NioServerSocketChannel.class)
				 .childHandler(new ChannelInitializer<NioSocketChannel>() {

					@Override
					protected void initChannel(NioSocketChannel ch) throws Exception {
						// TODO Auto-generated method stub
						ch.pipeline().addLast(new StringDecoder());
                        ch.pipeline().addLast(new SimpleChannelInboundHandler<String>() {
                            @Override
                            protected void channelRead0(ChannelHandlerContext ctx, String msg) {
                                System.out.println(msg);
                            }
                        });
					}
				 })
				 .bind(8888);
		
	}
}
