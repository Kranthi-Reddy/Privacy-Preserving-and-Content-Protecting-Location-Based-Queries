package server;
public class ServerThread extends Thread
{
	Server server;
public ServerThread(Server server){
	this.server=server;
	start();
}
public void run(){
	server.start();
}
}