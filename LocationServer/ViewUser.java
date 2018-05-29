package lbq;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JButton;
import java.util.ArrayList;
import java.awt.Dimension;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JFrame;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;
public class ViewUser extends JFrame
{
	
	JTable table;
	DefaultTableModel dtm;
	JScrollPane jsp;
	Font f1;
	JPanel p1,p2;
	String columns[]={"Username","Password","Confirm Password"};
	JButton b1,b2;
public ViewUser(){
	super("View Registered Users");
	setLayout(new BorderLayout());
	f1 = new Font("Courier New",Font.BOLD,13);
	p2 = new JPanel();
	p2.setBackground(Color.white);
	p2.setLayout(new BorderLayout());
	dtm = new DefaultTableModel(){
		public boolean isCellEditable(int r,int c){
			return false;
		}
	};
	table = new JTable(dtm);
	table.getTableHeader().setFont(new Font("Courier New",Font.BOLD,15));
	jsp = new JScrollPane(table);
	table.setFont(f1);
	table.setRowHeight(30);
	dtm.addColumn(columns[0]);
	dtm.addColumn(columns[1]);
	dtm.addColumn(columns[2]);
	
	p2.add(jsp,BorderLayout.CENTER);
	
	add(p2,BorderLayout.CENTER);
	load();
}
public void clear(){
	for(int i=dtm.getRowCount()-1;i>=0;i--){
		dtm.removeRow(i);
	}
}
public void load(){
	try{
		clear();
		ArrayList<String> list = DBCon.getUserList();
		for(int i=0;i<list.size();i++){
			String s[] = list.get(i).split(",");
			Object row[]={s[0],s[1],s[2]};
			dtm.addRow(row);
		}
	}catch(Exception e){
		e.printStackTrace();
	}
}
				
			
}