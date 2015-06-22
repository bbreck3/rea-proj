import java.io.*;
import java.net.*;
import java.math.*;
import java.util.*;

public class server{
	static PublicKey pk=new PublicKey();
	static PrivateKey sk = new PrivateKey(1024);
	static BigInteger c1, c2,pkN,pkMod;
	static Paillier pail = new Paillier();
	
	public static void main(String args[]) throws IOException{
	pail.keyGen(sk,pk);	
			

try{
			
		
		String pk_n_fromClient,pk_mod_fromClient,c1_fromClient,c2_fromClient;
		ServerSocket ss = new ServerSocket(11111);
		Socket socket =ss.accept();
		BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		PrintStream out = new PrintStream(socket.getOutputStream());
		
		//String input=in.readLine();
		out.println(pk.getN());
		out.println(pk.getMod());
		//Scanner scan = new Scanner(input);
		int count=0;
		while(true){
		
		//pk_n_fromClient = scan.nextLine();
		//pk_mod_fromClient = scan.nextLine();
		
		c1= new BigInteger(in.readLine());
		//System.out.println("C1: " + c1);
		c2=new BigInteger(in.readLine());
		//System.out.println("C2: " + c2);
		pkN=new BigInteger(in.readLine());
		//System.out.println("pkN: " + pkN);
		pkMod = new BigInteger(in.readLine());
		//System.out.println("pkMod " + pkMod);
		pk.setN(pkN);
		pk.setMod(pkMod);				
		//count++;
		BigInteger cipher = toPail(c1,c2,pk,sk);
		System.out.println("cipher to app:.." + cipher);		
		out.println(cipher);
		}
		
		
		//System.out.println("Debug...");
		//System.out.println(input);
	}catch(Exception e){
	e.printStackTrace();
	}
}	




		




/*try{
		ServerSocket ss = new ServerSocket(11111);
		Socket socket =ss.accept();
		BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		PrintStream out = new PrintStream(socket.getOutputStream());

		String input=in.readLine();
		
		
		System.out.println("Debug...");
		System.out.println(input);
	}catch(Exception e){
	e.printStackTrace();
	}*/	


public static BigInteger toPail(BigInteger c1, BigInteger c2,PublicKey pk ,PrivateKey sk){
	BigInteger result = pail.add(c1,c2,pk);
	System.out.println("result: " + result);
	//BigInteger p1 = pail.decrypt(result,sk);
	//System.out.println(p1);	
return result;
			
}


}
