package server;

public class MainServer {

	private int port;
	private Server server; 
	
	public MainServer(int port){
		this.port = port;
		this.server = new Server(port);
	}
	
	
	
	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("Invalid command! \n" + "Use: jar MainServer.jar [port]");
			return;
		}
		int port = Integer.parseInt(args[0]);
		new MainServer(port);
	}

}
