package com.wjs.schedule.net.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import javax.annotation.PostConstruct;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.springframework.stereotype.Component;

import com.wjs.schedule.constant.CuckooNetConstant;
import com.wjs.schedule.net.server.filter.RegistFilter;
import com.wjs.schedule.net.server.handle.CuckooServerHandler;

@Component
public class MinaTimeServer {
	
    // 定义监听端口
    private Integer port = 8888;
    
    
    public Integer getPort() {
    	
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	
	@PostConstruct
	public void init() throws IOException{
		// 创建服务端监控线程
        IoAcceptor acceptor = new NioSocketAcceptor();
        acceptor.getSessionConfig().setReadBufferSize(2048);
        acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);
        // 设置日志记录器
        acceptor.getFilterChain().addLast("logger", new LoggingFilter());
        acceptor.getFilterChain().addLast("regist", new RegistFilter());
        // 设置编码过滤器
        acceptor.getFilterChain().addLast(
            "codec",
            new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName(CuckooNetConstant.ENCODING))));
        // 指定业务逻辑处理器
        acceptor.setHandler(new CuckooServerHandler());
        // 设置端口号
        acceptor.bind(new InetSocketAddress(port));
        // 启动监听线程
        acceptor.bind();
	}
	
}