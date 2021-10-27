package MyServer;

import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer{
	private  List<Client> clients = new ArrayList<Client>();// ��������û�
	private static int clientNumber=1;
	
	public static void main(String[] args) {
		ChatServer chatServer = new ChatServer();
		chatServer.init();
	}
	@SuppressWarnings("resource")
	public void init() {
		ServerSocket server = null;
		Socket socket = null;
		try {
			server = new ServerSocket(60105);
			System.out.println("*************�����***************");
	        System.out.println("�������Ѿ��������˿�19888�ȴ�������\n");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		while (true) {
			try {
				socket = server.accept();
				System.out.println("SYSTEM_MESSAGE:Client"+clientNumber+" enters the ChatRoom\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (socket != null) {
				Client client = new Client(socket);
				clientNumber++;
				clients.add(client);
				Thread thread = new Thread(client);
				thread.start();
			}
		}
	}

	private class Client implements Runnable {
		
		private Socket socket = null;
		private DataInputStream dInputStream = null;
		private DataOutputStream dOutputStream = null;
		private String name = null;

		public Client(Socket socket) {
			super();
			this.name = "client"+clientNumber;
			this.socket = socket;
			try {
				dInputStream = new DataInputStream(this.socket.getInputStream());
				dOutputStream = new DataOutputStream(this.socket.getOutputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public void run() {
			this.sendToClient("����ǳƣ�"+this.name+"\n");
			for (int i = 0; i < clients.size(); i++) {         // ����Ϣ����ÿ���û���������
				Client client = clients.get(i);
				if(!client.equals(this))
					client.sendToClient(this.name+"������������\n");
			}
			try {
				while (true) {
					String message=dInputStream.readUTF();
					if(message.equals("##")){
						dOutputStream.writeUTF("##");
						throw new IOException();
					}
					System.out.println(this.name+" says: \n"+message+"\n");
					for (int i = 0; i < clients.size(); i++) {              // ����Ϣ����ÿ���û��������Լ�
						Client client = clients.get(i);
						if(!client.equals(this)) {
							client.sendToClient(this.name+"˵��\n"+message+"\n");
						}
					}
				}
			} catch (IOException e) {
				System.out.println(this.name+" has exited\n");
				for (int i = 0; i < clients.size(); i++) {             // ����Ϣ����ÿ���û���������
					Client client = clients.get(i);
					if(!client.equals(this))
						client.sendToClient(this.name+"�뿪��������\n");
				}
				clients.remove(this);
				//clientNumber--;
			}
		}
		
		public void sendToClient(String str) {
			try{
				dOutputStream.writeUTF(str);
			}catch(IOException e){
				e.printStackTrace();
			}
		}
	}
}

