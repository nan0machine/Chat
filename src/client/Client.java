package client;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.io.*;

public class Client{
	
	protected String nickname;
	
	protected Socket socket;
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
			disconnect();
			msg = "No Connection";
		}
		System.out.println(msg);
		return msg;
	}
		
	
	
	protected void send(String m) {
		send = new Thread("send") {
			public void run() {
				try {
					output.flush();
					output.writeUTF(m);
				} catch (IOException e) {
					e.printStackTrace();
					disconnect();
				}
			}
		};
		send.start();
	}

	
	protected void disconnect() {
		synchronized(socket) {
			try {
				this.socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}}
	
	
	
	
	
}
