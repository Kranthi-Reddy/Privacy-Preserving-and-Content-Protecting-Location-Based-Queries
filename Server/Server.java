package server;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.net.Socket;
import java.net.ServerSocket;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.net.InetAddress;
import com.jd.swing.custom.component.panel.SimpleGlossyPanel;
import com.jd.swing.util.PanelType;
import com.jd.swing.util.Theme;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
public class Server extends JFrame
{	
	ServerProcess thread;
	JPanel p1,p2,p3;
	JLabel l1;
	JScrollPane jsp;
	JTextArea area;
	Font f1,f2;
	ServerSocket server;
	Socket socket;
	String title1 = "PRIVACY-PRESERVING AND CONTENT-PROTECTING LOCATION";
	String title2 = "BASED QUERIES";
public void start(){
	try{
		server = new ServerSocket(5555);
		area.append("Service Provider Server Started\n\n");
		while(true){
			socket = server.accept();
			socket.setKeepAlive(true);
			InetAddress address=socket.getInetAddress();
			String ipadd=address.toString();
			area.append("Connected Computers :"+ipadd.substring(1,ipadd.length())+"\n");
			thread=new ServerProcess(socket,area);
			thread.start();
		}
	}catch(Exception e){
		e.printStackTrace();
	}
}
public Server(){
	setTitle("Service Provider Server");
	f1 = new Font("Courier New",Font.BOLD,18);
    p1 = new SimpleGlossyPanel(Theme.GLOSSY_ORANGEBLACK_THEME,PanelType.PANEL_ROUNDED_RECTANGLUR);
    l1 = new JLabel("<HTML><BODY><CENTER>"+title1+"<BR/>"+title2+"</CENTER></BODY></HTML>");
	l1.setFont(this.f1);
    l1.setForeground(Color.white);
    p1.add(l1);
    Marquee marquee = new Marquee(l1,title1+title2,32);
    marquee.start();

    f2 = new Font("Courier New", 1, 13);
    p2 = new JPanel();
    p2.setLayout(new BorderLayout());
    area = new JTextArea();
    area.setFont(f2);
    jsp = new JScrollPane(area);
    area.setEditable(false);
    p2.add(jsp);

	
	
	getContentPane().add(p1, "North");
    getContentPane().add(p2, "Center");
	addWindowListener(new WindowAdapter(){
            @Override
        public void windowClosing(WindowEvent we){
            try{
				if(socket != null){
					socket.close();
				}
             server.close();
            }catch(Exception e){
                //e.printStackTrace();
            }
        }
    });
}
public static void main(String a[])throws Exception	{
	UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	Server server = new Server();
	server.setVisible(true);
	server.setSize(800,600);
	new ServerThread(server);
}

}