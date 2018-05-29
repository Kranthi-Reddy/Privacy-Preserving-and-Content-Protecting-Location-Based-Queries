package lbq;
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
import java.text.DecimalFormat;
public class ProcessThread extends Thread{
    Socket socket;
    ObjectOutputStream out;
    ObjectInputStream in;
	JTextArea area;
	ArrayList<String> dataset;
	DecimalFormat formatter = new DecimalFormat("#.####");
	ElGamalEnc e;
	String k;
public ProcessThread(Socket soc,JTextArea area,ArrayList<String> dataset,ElGamalEnc e,String k){
    socket=soc;
	this.area=area;
	this.dataset=dataset;
	this.e = e;
	this.k = k;
	try{
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
	}catch(Exception e1){
        e1.printStackTrace();
    }
}
@Override
public void run(){
	try{
		process();		
    }catch(Exception e1){
        e1.printStackTrace();
    }
}
public void process()throws Exception{
	Object input[]=(Object[])in.readObject();
	String type=(String)input[0];
	if(type.equals("Register")){
		String user = (String)input[1];
		String pass = (String)input[2];
		String cpass = (String)input[3];
		String in[] = {user,pass,cpass};
		String msg=DBCon.register(in);
		Object res[]={msg};
        out.writeObject(res);
        out.flush();
		area.append("Send response to server "+msg+"\n");
	}
	if(type.equals("Login")){
		String user = (String)input[1];
		String pass = (String)input[2];
		String in[] = {user,pass};
		String msg=DBCon.login(in);
		Object res[]={msg};
        out.writeObject(res);
        out.flush();
		area.append("Send response to server "+msg+"\n");
	}
	if(type.equals("payment")){
		String user = (String)input[1];
		String amount = (String)input[2];
		String in[] = {user,amount};
		String msg=DBCon.payment(in);
		Object res[]={msg};
        out.writeObject(res);
        out.flush();
		area.append("Send response to server "+msg+"\n");
	}
	if(type.equals("search")){
		double lat[] = (double[])input[1];
		double lon[] = (double[])input[2];
		String users[] = (String[])input[3];
		StringBuilder result = new StringBuilder();
		for(int i=0;i<users.length;i++){
			String check_user = DBCon.isAuthorisedUser(users[i]);
			if(!check_user.equals("unauthorised")){
				if(check_user.equals("One Nearest Neighbor")){
					result.append(lat[i]+"#"+lon[i]+"#");
					String s1 = getNearestPoint(lat[i],lon[i]);
					result.append(s1);
				}
				if(check_user.equals("Many Nearest Neighbor")){
					result.append(lat[i]+"#"+lon[i]+"#");
					String s1 = getPossiblePoints(lat[i],lon[i]);
					result.append(s1);
				}
			}
		}
		Object res[]={result.toString()};
        out.writeObject(res);
        out.flush();
		area.append("Send response to server "+result.toString()+"\n");
	}
}
public String getNearestPoint(double la,double lo){
	String result = "none";
	double nearest_dis = 100;
	for(int i=0;i<dataset.size();i++){
		String loc_arr[] = dataset.get(i).split("\\s+");
		if(loc_arr.length ==3){
			if(!Double.toString(la).equals(loc_arr[1]) && !Double.toString(lo).equals(loc_arr[2])){
				double dis = distance(la,lo,Double.parseDouble(loc_arr[1].trim()),Double.parseDouble(loc_arr[2].trim()),'M');
				if(dis <= nearest_dis){
					nearest_dis = dis;
					result = e.bigEncrypt(loc_arr[0].trim(),k);
				}
			}
		}
	}
	return result;
}
public String getPossiblePoints(double la,double lo){
	StringBuilder result = new StringBuilder();
	double nearest_dis = 100;
	for(int i=0;i<dataset.size();i++){
		String loc_arr[] = dataset.get(i).split("\\s+");
		if(loc_arr.length ==3){
			if(!Double.toString(la).equals(loc_arr[1]) && !Double.toString(lo).equals(loc_arr[2])){
				double dis = distance(la,lo,Double.parseDouble(loc_arr[1].trim()),Double.parseDouble(loc_arr[2].trim()),'M');
				if(dis <= nearest_dis){
					result.append(e.bigEncrypt(loc_arr[0].trim(),k)+"\n");
				}
			}
		}
	}
	if(result.length() > 0)
		result.deleteCharAt(result.length()-1);
	return result.toString();
}
private static double distance(double lat1, double lon1, double lat2, double lon2, char unit) {
	double theta = lon1 - lon2;
	double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
	dist = Math.acos(dist);
	dist = rad2deg(dist);
	dist = dist * 60 * 1.1515;
	if (unit == 'K') {
		dist = dist * 1.609344;
	} else if (unit == 'N') {
		dist = dist * 0.8684;
	}
	return (dist);
}
private static double deg2rad(double deg) {
	return (deg * Math.PI / 180.0);
}
private static double rad2deg(double rad) {
	return (rad * 180.0 / Math.PI);
}		
}
