package lbq;
import java.math.BigInteger;
import java.util.Random;
import java.util.StringTokenizer;

class ElGamalEnc{
	
	private BigInteger p,g,a,y, y1,y2;
	private Random r;
	public BigInteger random(int m){
		Random ran = new Random(); 
		return new BigInteger(Integer.toString(100));//ran.nextInt(m)));
	}
	public ElGamalEnc(){
		r = new Random();
		p = new BigInteger("14893003337626352152463254152616458181260144281");
		g = new BigInteger("4893003337626352152463254152616458181260144281");
		a = new BigInteger("843900337326351225463254152616458181260144281");
		y = g.modPow(a,p);
		y1 = new BigInteger("0");
		y2 = new BigInteger("0");
	}
	//encrypts with built in public key
	public void encrypt(BigInteger m){
		//System.out.println("m: " + m.toString());
		//constructs a random bit integer k with any number of bits from 3 to 1 less than p
		BigInteger k = random(p.bitCount()-(int)(3 + Math.random()*p.bitCount()));
		y1 = g.modPow(k,p);
		y2 = m.xor(y.modPow(k,p));
 	   //System.out.println("y1: " + y1);
		//System.out.println("y2: " + y2);
	}
	public BigInteger decrypt(BigInteger[] b){
		return b[1].xor(b[0].modPow(a,p));
	}

	//version of encrypt which takes a new public key
	/*public String bigEncrypt(String message,BigInteger g, BigInteger y, BigInteger p){
		this.g = g;
		this.y = y;
		this.p = p;
		return bigEncrypt(message);
	}*/

	public String bigEncrypt(String message,String key){
		StringTokenizer st = new StringTokenizer(key,"(),");
		p = new BigInteger(st.nextToken());
		g = new BigInteger(st.nextToken());
		y = new BigInteger(st.nextToken());
		return bigEncrypt(message);
	}


	//version of encrypt which uses the default public key   
	public String bigEncrypt(String message){
		byte[]b = message.getBytes();
		BigInteger[][] cipher = new BigInteger[b.length][2];
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<b.length;i++){
			encrypt(new BigInteger(""+b[i]+"")); 
			cipher[i][0]= y1;
			cipher[i][1]= y2;
		}
		for(int i=0;i<b.length;i++){
			sb.append("(");
			sb.append(cipher[i][0]);
			sb.append(",");
			sb.append(cipher[i][1]);
			sb.append(")");
		}
		return (new String(sb));
		
	}
	public String bigDecrypt(String c){
		StringTokenizer st = new StringTokenizer(c,"(),");
		BigInteger[] temp = new BigInteger[2];
		StringBuffer plain= new StringBuffer();
		while (st.hasMoreTokens()){
			temp[0]=new BigInteger(st.nextToken());
			temp[1]=new BigInteger(st.nextToken());
			plain.append((char)(decrypt(temp)).intValue());
		}
		return new String(plain);   
	}
											  
											  
	public static String enc(String msg){
		System.out.println(msg+"kk");
		ElGamalEnc e = new ElGamalEnc();
		String k = "14893003337626352152463254152616458181260144281,4893003337626352152463254152616458181260144281,5260810279682188795512623296546807031696158558";
		String temp = e.bigEncrypt(msg,k);
		return temp;
	}
	public static String dec(String msg){
		ElGamalEnc e = new ElGamalEnc();
		return e.bigDecrypt(msg);
	}
	public static void main(String args[]){
		String s1 = enc("maitrivanam");
		String s2 = dec(s1);
		System.out.println(s1+" "+s2);
	}
}