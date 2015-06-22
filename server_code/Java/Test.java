

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.*;
import   java.text.SimpleDateFormat;
import java.util.Date;

public class Test {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		// TODO Auto-generated method stub
		
		String dbFile = "dbFile";
		String keyFile = "keyfile";
		Paillier paillier = new Paillier();
		PrivateKey sk = new PrivateKey(1024);
		PublicKey pk = new PublicKey();
		System.out.println(sk);
		System.out.println(pk);
		
		paillier.keyGen(sk, pk);	
		System.out.println("the writed key");
		System.out.println(sk);
		System.out.println(pk);
		
		
		FileOutputStream outFile = new FileOutputStream(dbFile);
	    ObjectOutputStream foos = new ObjectOutputStream(outFile);
		foos.writeObject(sk);
		foos.writeObject(pk);
		
		
		System.out.println("the read key");
		FileInputStream inFile = new FileInputStream(dbFile);
	    ObjectInputStream fois = new ObjectInputStream(inFile);
	    
	    PrivateKey sk_1 = new PrivateKey(1024);
		PublicKey pk_1 = new PublicKey();
	    try {
			sk_1 = (PrivateKey) fois.readObject();
			System.out.println(sk_1);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	    try {
			pk_1 = (PublicKey) fois.readObject();
			System.out.println(pk_1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	    
		
		BigInteger plaintext1 = BigInteger.valueOf(123456);
		BigInteger plaintext2 = BigInteger.valueOf(111111);
		String a = plaintext1.toString();
		System.out.println("biginteger--string");
		System.out.println(plaintext1);
		System.out.println(a);
		
		BigInteger ciphertext1 = null;
		BigInteger ciphertext2 = null;
		BigInteger result = null;
		
		ciphertext1 = paillier.encrypt(plaintext1, pk);
		ciphertext2 = paillier.encrypt(plaintext2, pk);
		
		System.out.println("Result");
		result = paillier.add(ciphertext1, ciphertext2, pk);
		System.out.println(result.toString());
				
		//System.out.println("ciphertext");

		//System.out.println(ciphertext);
		//System.out.println("bitlength");
		//System.out.println(ciphertext.bitLength());
		
		
		
		
		
		
		
		
		BigInteger plaintext_1 = null;
		BigInteger plaintext_2 = null;
		
		plaintext_1 = paillier.decrypt(result, sk);
		plaintext_2 = paillier.decrypt(result, sk_1);
		System.out.println(plaintext_1);
		System.out.println(plaintext_2);
		
		
		
		
		
		
 
	}

}
