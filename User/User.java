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
public class User extends JFrame
{
	JPanel p1,p2;
	JLabel l1;
	JButton b1,b2;
	Font f1,f2;
	
public User(){
	setTitle("User");
	p1 = new JPanel();
    l1 = new JLabel("<html><body><center><font size=4 color=#f5ea01>PRIVACY-PRESERVING AND CONTENT-PROTECTING LOCATION<BR/>BASED QUERIES</font></center></body></html>");
	l1.setForeground(Color.white);
    p1.add(l1);
    p1.setBackground(Color.black);

    f2 = new Font("Courier New", 1, 13);
    p2 = new JPanel();
	b1 = new JButton("Login");
	b1.setFont(f2);
    p2.add(b1);
	b1.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent ae){
			setVisible(false);
			Login l = new Login(User.this);
			l.setSize(350,230);
			l.setVisible(true);
		}
	});

	b2 = new JButton("Register");
	b2.setFont(f2);
    p2.add(b2);
	b2.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent ae){
			setVisible(false);
			Register r = new Register(User.this);
			r.setSize(370,270);
			r.setResizable(false);
			r.setVisible(true);
		}
	});
	
    getContentPane().add(p1, "North");
    getContentPane().add(p2, "Center");
	
}
public static void main(String a[]){
	User user = new User();
	user.setVisible(true);
	user.setSize(500,200);
}
}