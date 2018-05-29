package user;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import java.io.File;
import java.io.FileInputStream;
import java.net.Socket;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import javax.swing.JOptionPane;
import java.util.Random;
public class UserScreen extends JFrame
{
	JPanel p1,p2;
	JLabel l1;
	JButton b1,b2;
	Font f1,f2;
	String user;
	JFileChooser chooser;
public UserScreen(String usr){
	user=usr;
	setTitle("Cloud User");
	p1 = new JPanel();
    l1 = new JLabel("<html><body><center><font size=4 color=#f5ea01>PRIVACY-PRESERVING AND CONTENT-PROTECTING LOCATION<BR/>BASED QUERIES</font></center></body></html>");
	l1.setForeground(Color.white);
    p1.add(l1);
    p1.setBackground(Color.black);

    f2 = new Font("Courier New", 1, 13);
    p2 = new JPanel();
	b1 = new JButton("Payment");
	b1.setFont(f2);
    p2.add(b1);
	b1.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent ae){
			String input = JOptionPane.showInputDialog(UserScreen.this,"Enter payment amount\n50 to get only nearest neighbor\n100 to get all possible nearest places");
			if(input != null){
				int value = 0;
				try{
					value = Integer.parseInt(input.trim());
				}catch(NumberFormatException nfe){
					JOptionPane.showMessageDialog(UserScreen.this,"Payment value must be numeric");
					return;
				}
				if(value == 50 || value == 100){
					payment(Integer.toString(value));
				}else{
					JOptionPane.showMessageDialog(UserScreen.this,"Payment value either 50 or 100");
				}
			}
		}
	});

	b2 = new JButton("Search");
	b2.setFont(f2);
    p2.add(b2);
	b2.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent ae){
			String lat = JOptionPane.showInputDialog(UserScreen.this,"Enter latitude of your location");
			String lon = JOptionPane.showInputDialog(UserScreen.this,"Enter longitude of your location");
			if(lat != null && lon != null){
				double la= 0;
				double lo = 0;
				try{
					la = Double.parseDouble(lat.trim());
					lo = Double.parseDouble(lon.trim());
				}catch(NumberFormatException nfe){
					JOptionPane.showMessageDialog(UserScreen.this,"Latitude or Longitude value must be numeric");
					return;
				}
				sendRequest(lat,lon);
			}
		}
	});

		
    getContentPane().add(p1, "North");
    getContentPane().add(p2, "Center");
	chooser = new JFileChooser();
}
public void payment(String amount){
	try{
		Socket socket = new Socket("localhost",5555);
		ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
		Object req[] = {"payment",user,amount};
		out.writeObject(req);
		ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
		Object res[] = (Object[])in.readObject();
		String re = (String)res[0];
		JOptionPane.showMessageDialog(this,re);
		
	}catch(Exception e){
		e.printStackTrace();
	}
}
public int createRandom(){
	int min = 2;
	int max = 6;
	Random r = new Random();
	return r.nextInt(max - min + 1) + min;
}
public int randomPosition(int users){
	Random r = new Random();
	return r.nextInt(users);
}
public void sendRequest(String lat,String lon){
	try{
		int random_users = createRandom();
		int position = randomPosition(random_users);
		double users_lat[] = new double[random_users];
		double users_lon[] = new double[random_users];
		String users[] = new String[random_users];
		for(int i=0;i<random_users;i++){
			if(i != position){
				double fake_lat = Double.parseDouble(lat)+(i+1);
				users_lat[i] = fake_lat;
				users[i] = "temp";
			}
		}
		for(int i=0;i<random_users;i++){
			if(i != position){
				double fake_lon = Double.parseDouble(lon)+(i+1);
				users_lon[i] = fake_lon;
			}
		}
		users_lat[position] = Double.parseDouble(lat);
		users_lon[position] = Double.parseDouble(lon);
		users[position] = user;
		Socket socket = new Socket("localhost",5555);
		ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
		Object req[] = {"search",users_lat,users_lon,users};
		out.writeObject(req);
		ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
		Object res[] = (Object[])in.readObject();
		String re = (String)res[0];
		String arr[] = re.split("#");
		System.out.println(arr[0]+" "+lat+" "+arr[1]+" "+lon);
		if(arr[0].equals(lat) && arr[1].equals(lon)){
			String result_arr[] = arr[2].split("\n");
			StringBuilder sb = new StringBuilder();
			for(String str : result_arr){
				sb.append(ElGamalEnc.dec(str)+"\n");
			}
			JOptionPane.showMessageDialog(this,sb.toString());
		}else{
			JOptionPane.showMessageDialog(this,"unauthorised User");
		}
	}catch(Exception e){
		e.printStackTrace();
	}
}		

}