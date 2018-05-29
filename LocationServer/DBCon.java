package lbq;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
public class DBCon{
    private static Connection con;
public synchronized static Connection getCon()throws Exception {
    Class.forName("com.mysql.jdbc.Driver");
    con = DriverManager.getConnection("jdbc:mysql://localhost/lbq","root","root");
    return con;
}
public static String register(String[] input)throws Exception{
    String msg="fail";
    boolean flag=false;
    boolean flag1=false;
	con = getCon();
    Statement stmt=con.createStatement();
    ResultSet rs=stmt.executeQuery("select username from newuser where username='"+input[0]+"'");
    if(rs.next()){
        flag=true;
        msg = "uexist";
    }
    stmt=con.createStatement();
    rs=stmt.executeQuery("select password from newuser where password='"+input[1]+"'");
    if(rs.next() && !flag){
        flag1=true;
        msg = "pexist";
    }
	if(!flag && !flag1){
		PreparedStatement stat=con.prepareStatement("insert into newuser values(?,?,?)");
		stat.setString(1,input[0]);
		stat.setString(2,input[1]);
		stat.setString(3,input[2]);
		int i=stat.executeUpdate();
		stat.close();
		if(i > 0){
			msg = "Registration process completed";
		}
    }
	rs.close();stmt.close();con.close();
    return msg;
}
public static String payment(String input[])throws Exception{
	String msg="Error in payment";
	java.util.Date d1 = new java.util.Date();
	java.sql.Date d2 = new java.sql.Date(d1.getTime());
	con = getCon();
	PreparedStatement stat=con.prepareStatement("insert into payment values(?,?,?,?)");
	stat.setString(1,input[0]);
	stat.setString(2,input[1]);
	stat.setDate(3,d2);
	if(input[1].equals("50"))
		stat.setString(4,"One Nearest Neighbor");
	if(input[1].equals("100"))
		stat.setString(4,"Many Nearest Neighbor");
	int i=stat.executeUpdate();
	stat.close();
	if(i > 0){
		msg = "Payment Done";
	}
    stat.close();con.close();
    return msg;
}
public static String login(String input[])throws Exception{
    String msg="fail";
    con = getCon();
    Statement stmt=con.createStatement();
    ResultSet rs=stmt.executeQuery("select username,password from newuser where username='"+input[0]+"' and password='"+input[1]+"'");
    if(rs.next()){
        msg = "valid login";
    }
	rs.close();stmt.close();con.close();
    return msg;
}
public static String isAuthorisedUser(String user)throws Exception{
	String msg="unauthorised";
    con = getCon();
    Statement stmt=con.createStatement();
    ResultSet rs=stmt.executeQuery("select search_type from payment where username='"+user+"'");
    if(rs.next()){
        msg = rs.getString(1);
    }
	rs.close();stmt.close();con.close();
    return msg;
}
public static ArrayList<String> getUserList()throws Exception{
	ArrayList<String> list = new ArrayList<String>();
	con = getCon();
    Statement stmt=con.createStatement();
    ResultSet rs=stmt.executeQuery("select * from newuser");
    while(rs.next()){
        list.add(rs.getString(1)+","+rs.getString(2)+","+rs.getString(3));
    }
	rs.close();stmt.close();con.close();
	return list;
}
}
