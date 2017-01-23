package com.wjs.schedule.net.server.handle;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

/**
 * 服务器端业务逻辑，封装TimeServerHander，需要针对客户端的session进行缓存操作，并记录数据库
 */
public class TimeServerHandler extends IoHandlerAdapter {

    
    /**
     * 消息接收事件
     * 接收到消息时调用的方法，也就是用于接收消息的方法，一般情况下，message 是一个IoBuffer 类，
     * 如果你使用了协议编解码器，那么可以强制转换为你需要的类型。
     * 通常我们都是会使用协议编解码器的， 就像上面的例子， 因为协议编解码器是TextLineCodecFactory，所以我们可以强制转message 为String 类型。
     */
    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        String strMsg = message.toString();
        if (strMsg.trim().equalsIgnoreCase("quit")) {
            session.close(true);
            return;
        }
        // 返回消息字符串
        session.write("Hi Client!");
        // 打印客户端传来的消息内容
        System.out.println("Message written : " + strMsg);
    }

    /**
     * 当发送消息成功时调用这个方法，注意这里的措辞，发送成功之后，也就是说发送消息是不能用这个方法的。
	     发送消息的时机： 发送消息应该在sessionOpened()、messageReceived()方法中调用IoSession.write()方法完成。
	     因为在sessionOpened()方法中，TCP 连接已经真正打开，同样的在messageReceived()方法TCP 连接也是打开状态，只不过两者的时机不同。
	  sessionOpened()方法是在TCP 连接建立之后，接收到数据之前发送；
	  messageReceived()方法是在接收到数据之后发送，你可以完成依据收到的内容是什么样子，决定发送什么样的数据。因为这个接口中的方法太多，因此通常使用适配器模式IoHandlerAdapter，覆盖你所感兴趣的方法即可。
     */
	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
		// TODO Auto-generated method stub
		super.messageSent(session, message);
	}
    
    
}