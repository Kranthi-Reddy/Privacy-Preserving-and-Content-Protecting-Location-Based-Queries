package lbq;
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
import org.jfree.ui.RefineryUtilities;
import java.io.DataInputStream;
import java.io.DataOutputStream;
public class LocationServer extends JFrame
{	
	ProcessThread thread;
	JPanel p1,p2,p3;
	JLabel l1;
	JScrollPane jsp;
	JTextArea area;
	Font f1,f2;
	ServerSocket server;
	Socket socket;
	JButton b1;
	String title1 = "PRIVACY-PRESERVING AND CONTENT-PROTECTING LOCATION";
	String title2 = "BASED QUERIES";
	static ArrayList<String> dataset = new ArrayList<String>();
	ElGamalEnc e = new ElGamalEnc();
	String k = "14893003337626352152463254152616458181260144281,4893003337626352152463254152616458181260144281,5260810279682188795512623296546807031696158558";
public static void loaddataset()throws Exception{
	dataset.clear();
	BufferedReader br = new BufferedReader(new FileReader("dataset.txt"));
	String line = null;
	while((line = br.readLine())!=null){
		dataset.add(line);
	}
	br.close();
}
public void start(){
	try{
		loaddataset();
		server = new ServerSocket(5000);
		area.append("Server Started\n\n");
		while(true){
			socket = server.accept();
			socket.setKeepAlive(true);
			InetAddress address=socket.getInetAddress();
			String ipadd=address.toString();
			area.append("Connected Computers :"+ipadd.substring(1,ipadd.length())+"\n");
			thread=new ProcessThread(socket,area,dataset,e,k);
			thread.start();
		}
	}catch(Exception e1){
		e1.printStackTrace();
	}
}

public LocationServer(){
	setTitle("LBQ Server");
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

	p3 = new SimpleGlossyPanel(Theme.GLOSSY_ORANGEBLACK_THEME,PanelType.PANEL_ROUNDED_RECTANGLUR);
	b1 = new JButton("View Users");
	b1.setFont(f2);
	p3.add(b1);
	b1.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent ae){
			ViewUser vr = new ViewUser();
			vr.setVisible(true);
			vr.setSize(800,400);
		}
	});
	
	getContentPane().add(p1, "North");
    getContentPane().add(p2, "Center");
	getContentPane().add(p3, "South");
    addWindowListener(new WindowAdapter(){
            @Override
        public void windowClosing(WindowEvent we){
            try{
				if(socket != null){
					socket.close();
				}
             server.close();
            }catch(Exception e1){
                //e.printStackTrace();
            }
        }
    });
}
public static void main(String a[])throws Exception	{
	UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	LocationServer ls=new LocationServer();
	ls.setVisible(true);
	ls.setSize(800,600);
	new ServerThread(ls);
}

}