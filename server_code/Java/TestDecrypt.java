

import java.math.BigInteger;
import java.io.*;

public class TestDecrypt {
	private static BigInteger plain;

	public static void main(String[] args) throws IOException{
		PrivateKey sk = new PrivateKey(1024);
		PublicKey pk = new PublicKey();	
		Paillier.keyGen(sk, pk);
		
		
		BigInteger plaintext = BigInteger.valueOf(123);
		BigInteger ciphertext = Paillier.encrypt(plaintext, pk);
		
		long start = System.currentTimeMillis();
		for(int i = 0; i<20;i++){
			plain = Paillier.decrypt(ciphertext, sk);
			
		}
		long end = System.currentTimeMillis();
		
		System.out.println(end - start);
		long start1 = System.currentTimeMillis();
		BigInteger plain = BigInteger.valueOf(123);
		for(int i = 0;i<1000;i++){
			
			Paillier.encrypt(plain, pk);
			
			
		}
		long end1 = System.currentTimeMillis();
		
		System.out.println(end1 - start1);
		
		
		
	}
}
