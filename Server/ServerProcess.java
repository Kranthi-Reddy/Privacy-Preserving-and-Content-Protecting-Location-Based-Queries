package server;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.StringTokenizer;
import java.io.FileWriter;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.File;
import javax.swing.JTextArea;
import java.io.FileOutputStream;
import java.util.ArrayList;
public class ServerProcess extends Thread{
    Socket socket;
    ObjectOutputStream out;
    ObjectInputStream in;
	JTextArea area;
public ServerProcess(Socket soc,JTextArea area){
    socket=soc;
	this.area=area;
	try{
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
	}catch(Exception e){
        e.printStackTrace();
    }
}
@Override
public void run(){
	try{
		process();		
    }catch(Exception e){
        e.printStackTrace();
    }
}
public void process()throws Exception{
	Object input[]=(Object[])in.readObject();
	String type=(String)input[0];
	if(type.equals("Register")){
		Socket soc = new Socket("localhost",5000);
		ObjectOutputStream ou = new ObjectOutputStream(soc.getOutputStream());
		ou.writeObject(input);
		ou.flush();
		area.append("Request sent to Location server for user registration\n"); 
		ObjectInputStream ois = new ObjectInputStream(soc.getInputStream());
		Object res[] = (Object[])ois.readObject();
		out.writeObject(res);
		out.flush();
		area.append("Response sent to client\n");
	}
	if(type.equals("Login")){
		Socket soc = new Socket("localhost",5000);
		ObjectOutputStream ou = new ObjectOutputStream(soc.getOutputStream());
		ou.writeObject(input);
		ou.flush();
		area.append("Request sent to Location server for user login\n"); 
		ObjectInputStream ois = new ObjectInputStream(soc.getInputStream());
		Object res[] = (Object[])ois.readObject();
		out.writeObject(res);
		out.flush();
		area.append("Response sent to client\n");
	}
	if(type.equals("payment")){
		Socket soc = new Socket("localhost",5000);
		ObjectOutputStream ou = new ObjectOutputStream(soc.getOutputStream());
		ou.writeObject(input);
		ou.flush();
		area.append("Request sent to Location server for Payment\n"); 
		ObjectInputStream ois = new ObjectInputStream(soc.getInputStream());
		Object res[] = (Object[])ois.readObject();
		out.writeObject(res);
		out.flush();
		area.append("Response sent to client\n");
	}
	if(type.equals("search")){
		Socket soc = new Socket("localhost",5000);
		ObjectOutputStream ou = new ObjectOutputStream(soc.getOutputStream());
		ou.writeObject(input);
		ou.flush();
		area.append("Request sent to Location server for nearest places\n"); 
		ObjectInputStream ois = new ObjectInputStream(soc.getInputStream());
		Object res[] = (Object[])ois.readObject();
		out.writeObject(res);
		out.flush();
		area.append("Response sent to client\n");
	}
}
}
