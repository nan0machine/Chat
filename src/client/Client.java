package client;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.io.*;

public class Client{
	
	private String nickname;
	
	private Socket socket;
	private String address;
	private int port;
	
	private Thread send;
	private DataInputStream input;
	private DataOutputStream output;
	
	public Client(String nickname, String address, int port) {
		this.nickname = nickname;
		this.address = address;
		this.port = port;	
	}
	
	
	public boolean openConnection() {
		try{
			socket = new Socket();
			socket.bind(new InetSocketAddress("localhost",0));
			socket.connect(new InetSocketAddress(this.address, this.port));
			
			output = new DataOutputStream(socket.getOutputStream());
			input = new DataInputStream(socket.getInputStream());
		}catch(Exception e ) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	

	public String toString() { 
		return this.nickname + " " + this.address.toString() + " " + this.port;
	}

	
	protected String receive() {
		String msg = "";
		try{
			msg = input.readUTF();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(msg);
		return msg;
	}
		
	
	
	protected void send(String m) {
		send = new Thread("send") {
			public void run() {
				try {
					output.writeUTF(m);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		send.start();
	}

	
	
	
	
	
	
	
	
}
