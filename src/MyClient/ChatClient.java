package MyClient;

import java.io.*;
import java.net.*;
import java.util.*;

public class ChatClient {
	DataInputStream dInputStream = null;
	DataOutputStream dOutputStream = null;
	Socket mysocket = null;
	
	public static void main(String[] args) {
		ChatClient chatClient = new ChatClient();
		chatClient.init();
	}
	
	public void init() {
		try {
			mysocket = new Socket("100.64.158.158", 19888);
			Thread thread = new Thread(new SentToServer());
			dInputStream = new DataInputStream(mysocket.getInputStream());
			dOutputStream = new DataOutputStream(mysocket.getOutputStream());
			thread.start();
			System.out.println("客户端成功启动，可以输入消息，使用##结束对话\n");
			while(true){
				String message=dInputStream.readUTF();
				if(message.equals("##")){
					break;
				}
				System.out.println(message);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private class SentToServer implements Runnable {
		public void run() {
			try {
				Scanner input = new Scanner(System.in);
				String message = new String();
				while (true) {
					message = input.nextLine();
					dOutputStream.writeUTF(message);
					if(message.equals("##")){
						break;
					}
					System.out.println("我:"+message+"\n");
				}
			}catch (Exception e) {
				e.printStackTrace();
			}finally {
				System.out.println("你已退出聊天室");
			}
		}
	}
}

