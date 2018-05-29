package lbq;
public class ServerThread extends Thread
{
	LocationServer server;
public ServerThread(LocationServer server){
	this.server=server;
	start();
}
public void run(){
	server.start();
}
}