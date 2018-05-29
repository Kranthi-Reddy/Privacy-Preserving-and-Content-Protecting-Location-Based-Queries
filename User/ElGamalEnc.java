package user;
import java.math.BigInteger;
import java.util.Random;
import java.util.StringTokenizer;

class ElGamalEnc{
	
	private BigInteger p,g,a,y, y1,y2;
	private Random r;
	public BigInteger random(int m){
		Random ran = new Random(); 
		return new BigInteger(Integer.toString(ran.nextInt(m)));
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
											  
											  
	public static String dec(String msg){
		ElGamalEnc e = new ElGamalEnc();
		return e.bigDecrypt(msg);
	}
	public static void main(String a[])throws Exception{
		System.out.println(dec("(6854448052549868309425901351886765625059924506,10895508609866516331091420310333028406623345162)(6854448052549868309425901351886765625059924506,10895508609866516331091420310333028406623345158)(9463500755848770848271745805152442029350504705,5096057135119132659414077101776959514777697989)(13682999556162324095727816122099504503967523486,14659627359273107012676324030556396924220981157)(11108644391825051953174379317303819285308838822,13553168743905180582437649336136831798246286948)(481282699855083356879465362242445176984918116,10493048520060854609395239655661930492374594013)(7666929167142125459501482515592064646763506380,7889691110069376107029511434689833159472571357)(4893003337626352152463254152616458181260144281,5260810279682188795512623296546807031696158506)"));
		System.out.println(dec("(6002284227066596822961397571091781103472106546,4945136946122483350038186112871505759662158416)(481282699855083356879465362242445176984918116,10493048520060854609395239655661930492374594008)(5261771324277491663817805740415427075458241498,7648259538138576444773057618255872296276468289)(1252536325108085790558333353367028563664169926,3187461651641220989047428897035884389991587585)(9814318725352839889923908288503801456194673492,13709912284055824893542305473133234741885242971)(7848496910603266329410997219269035719903160157,4785885998413029929592340921787464209729631664)(2248855774308786545576921621872251712342359975,13214542768358511630273642553996718088136999711)(4893003337626352152463254152616458181260144281,5260810279682188795512623296546807031696158506)(6854448052549868309425901351886765625059924506,10895508609866516331091420310333028406623345183)(3291676928777651871096356651550003750477314826,11054585789076116822838435006224095230383471416)"));
		System.out.println(dec("(1568015180832681096946332705754608570878775889,7073234432550690079248786991180079617708759278)(9814318725352839889923908288503801456194673492,13709912284055824893542305473133234741885242971)(8070576990970060978204657003853068094506615241,9136355035319899010612246877643496827550803294)(4893003337626352152463254152616458181260144281,5260810279682188795512623296546807031696158506)(5576433050916780330835133731431216407464931182,5245461236037500844182623319796625673169011184)(1568015180832681096946332705754608570878775889,7073234432550690079248786991180079617708759274)(9463500755848770848271745805152442029350504705,5096057135119132659414077101776959514777698006)(14719005453166722161963414730258839736825871685,12946703211972069584483799266247723223747255064)(9447789750729312232225045365591646262133693679,5721733792234745783891140854447492351622286978)(8070576990970060978204657003853068094506615241,9136355035319899010612246877643496827550803286)(13400309689823449757668547498620704445883751357,11970585839005634664903505084770497701750795287)"));

	}

}