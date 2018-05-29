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
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JOptionPane;
import java.net.Socket;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
public class Login extends JFrame
{
	JPanel p1,p2;
	JLabel l1,l2,l3;
	JButton b1,b2;
	Font f1,f2;
	JTextField tf1,tf2;
	User cu;
public Login(User cu){
	this.cu=cu;
	setTitle("User Login Screen");
	p1 = new JPanel();
    l1 = new JLabel("<html><body><center><font size=4 color=#f5ea01>User Login Screen</font></center></body></html>");
	l1.setForeground(Color.white);
    p1.add(l1);
    p1.setBackground(Color.black);

    f2 = new Font("Courier New", 1, 13);
    p2 = new JPanel();
	p2.setLayout(null);
	l1 = new JLabel("Username");
	l1.setFont(f2);
	l1.setBounds(10,10,100,30);
	p2.add(l1);
	tf1 = new JTextField();
	tf1.setBounds(130,10,120,30);
	p2.add(tf1);
	tf1.setFont(f2);

	
	l2 = new JLabel("Password");
	l2.setFont(f2);
	l2.setBounds(10,60,100,30);
	p2.add(l2);
	tf2 = new JPasswordField();
	tf2.setBounds(130,60,120,30);
	p2.add(tf2);
	tf2.setFont(f2);

	
	b1 = new JButton("Login");
	b1.setFont(f2);
    p2.add(b1);
	b1.setBounds(20,110,120,30);
	b1.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent ae){
			login();
		}
	});

	b2 = new JButton("Reset");
	b2.setFont(f2);
    p2.add(b2);
	b2.setBounds(160,110,120,30);
	b2.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent ae){
			clear();
		}
	});
	
    getContentPane().add(p1, "North");
    getContentPane().add(p2, "Center");
	
}
public void login(){
	String user = tf1.getText().trim();
	String pass = tf2.getText().trim();
	if(user.length() == 0){
		JOptionPane.showMessageDialog(this,"Username must be enter");
		tf1.requestFocus();
		return;
	}
	if(pass.length() == 0){
		JOptionPane.showMessageDialog(this,"Password must be enter");
		tf2.requestFocus();
		return;
	}
	try{
		Socket socket = new Socket("localhost",5555);
		ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
		Object req[] = {"Login",user,pass};
		out.writeObject(req);
		ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
		Object res[] = (Object[])in.readObject();
		String re = (String)res[0];
		if(!re.equals("fail")){
			setVisible(false);
			UserScreen us = new UserScreen(user);
			us.setVisible(true);
			us.setSize(500,300);
		}else{
			JOptionPane.showMessageDialog(this,"Invalid Login");
		}
	}catch(Exception e){
		e.printStackTrace();
	}
}
		

public void clear(){
	tf1.setText("");
	tf2.setText("");
	
}
}