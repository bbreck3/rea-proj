import java.math.*;
import java.io.*;

public class PalTest{
	private static BufferedWriter ct1f,ct2f;
	public static void main(String args[]) throws IOException{
		//CipherText 1		
		File ct1 = new File("ciphertext1");
		FileWriter ct1w = new FileWriter(ct1.getAbsoluteFile());
		ct1f = new BufferedWriter(ct1w);
		
		//CipherText 2	
		File ct2 = new File("ciphertext2");	
		FileWriter ct2w = new FileWriter(ct2.getAbsoluteFile());
		ct2f = new BufferedWriter(ct2w);
		
		
		String keyFile = "keyfile";
		Paillier pail = new Paillier();
		PrivateKey sk = new PrivateKey(1024);
		PublicKey pk = new PublicKey();
		
		System.out.println("Gerating Keys....");
		pail.keyGen(sk,pk);
		
		System.out.println("Generating plaintext...");
		BigInteger p1 = BigInteger.valueOf(1111111);
		BigInteger p2 = BigInteger.valueOf(2222222);
		System.out.println("P1 val: " + p1.toString());
		System.out.println("P2 val: " + p2.toString());
		System.out.println("Generating ciphertext....");
		BigInteger c1 =pail.encrypt(p1,pk);
		ct1f.write(c1.toString());
		ct1f.close();
		System.out.println("c1 Val...");
		System.out.println(c1.toString());
		BigInteger c2 = pail.encrypt(p2,pk);
		ct2f.write(c2.toString());
		ct2f.close();
		System.out.println("Homomorphic Operation Add...");
		BigInteger result=pail.add(c1,c2,pk);
		//System.out.println(result);
		System.out.println("Decrypting...");
		BigInteger p_1 = pail.decrypt(result,sk);
		//BigInteger p_2 = pail.decrypt(result,sk_1);
		System.out.println("Result");
		System.out.println(p_1);
		//System.out.println(p_2);
		
		 	
}
}
