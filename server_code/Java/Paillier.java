/*
 * Copyright (C) 2010 by Lior Malka <lior@cs.umd.edu>, Yan Huang <yhuang@virginia.edu>
 *
 * This code is made freely available under the MIT license: http://www.opensource.org/licenses/mit-license.*/


import java.math.*;
import java.util.*;
import java.io.*;
public class Paillier  {


    // k2 controls the error probability of the primality testing algorithm
    // (specifically, with probability at most 2^(-k2) a NON prime is chosen).
    private static int k2 = 40;

    private static Random rnd = new Random();
    private static BufferedWriter ctbf;
    private static BigInteger pkn,pkmod;
    
    public static void keyGen(PrivateKey sk, PublicKey pk) {
        //counts the number of times keygen is called in order to track the correct keys in which files
		
try{
		//int key_num=trackKeysRead();
            //public key
            File file = new File("pub.key");// +Integer.toString(key_num));
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bf = new BufferedWriter(fw);

            // bit_length is set as half of k1 so that when pq is computed, the
            // result has k1 bits
            int bit_length = sk.k1 / 2;

            // Chooses a random prime of length k2. The probability that p is not
            // prime is at most 2^(-k2)
            BigInteger p = new BigInteger(bit_length, k2, rnd);
            BigInteger q = new BigInteger(bit_length, k2, rnd);

            pk.k1 = sk.k1;
            //bf.write(pk.k1);
            //bf.newLine();
            pk.n = p.multiply(q); // n = pq

            //int pk_n_len = String.valueOf(pk.n).length();		
            bf.write(pk.n.toString());
            bf.newLine();
            pk.modulous = pk.n.multiply(pk.n); // modulous = n^2
            //int pk_mod_len = String.valueOf(pk.modulous).length();
            bf.write(pk.modulous.toString());
            bf.close();

		//trackKeysWrite(key_num++);
	
            sk.lambda = p.subtract(BigInteger.ONE).multiply(
                    q.subtract(BigInteger.ONE));
            sk.mu = sk.lambda.modInverse(pk.n);
            sk.n = pk.n;
            sk.modulous = pk.modulous;
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    // Compute ciphertext = (mn+1)r^n (mod n^2) in two stages: (mn+1) and (r^n).
    public static BigInteger encrypt(BigInteger plaintext, PublicKey pk) throws IOException{
        BigInteger randomness = new BigInteger(pk.k1, rnd); // creates
        // randomness of
        // length k1
        BigInteger tmp1 = plaintext.multiply(pk.n).add(BigInteger.ONE)
                .mod(pk.modulous);
        BigInteger tmp2 = randomness.modPow(pk.n, pk.modulous);
        BigInteger ciphertext = tmp1.multiply(tmp2).mod(pk.modulous);

        return ciphertext;
    }

    // Compute plaintext = L(c^(lambda) mod n^2) * mu mod n
    public static BigInteger decrypt(BigInteger ciphertext, PrivateKey sk) {
        BigInteger plaintext = L(ciphertext.modPow(sk.lambda, sk.modulous),
                sk.n).multiply(sk.mu).mod(sk.n);
        return plaintext;
    }

    // On input two encrypted values, returns an encryption of the sum of the
    // values
    public static BigInteger add(BigInteger ciphertext1,
                                 BigInteger ciphertext2, PublicKey pk) {
        BigInteger ciphertext = ciphertext1.multiply(ciphertext2).mod(
                pk.modulous);
        return ciphertext;
    }

    // On input an encrypted value x and a scalar c, returns an encryption of
    // cx.
    public static BigInteger multiply(BigInteger ciphertext1,
                                      BigInteger scalar, PublicKey pk) {
        BigInteger ciphertext = ciphertext1.modPow(scalar, pk.modulous);
        return ciphertext;
    }

    public static BigInteger multiply(BigInteger ciphertext1, int scalar,
                                      PublicKey pk) {
        return multiply(ciphertext1, BigInteger.valueOf(scalar), pk);
    }

    public static void display(BigInteger c, PrivateKey sk)  {
        BigInteger tmp = decrypt(c, sk);
        byte[] content = tmp.toByteArray();
        System.out.print("Ciphertext contains " + content.length
                + " bytes of the following plaintext:");
        for (int i = 0; i < content.length; i++)
            System.out.print((0xFF & content[i]) + " ");
        System.out.println();
    }

    // L(u)=(u-1)/n
    private static BigInteger L(BigInteger u, BigInteger n)  {
        return u.subtract(BigInteger.ONE).divide(n);
    }
	private static void trackKeysWrite(int num) throws IOException{
	File keyNum = new File("keyNum");
	FileWriter keyfw= new FileWriter(keyNum.getAbsoluteFile());
	BufferedWriter keybf= new BufferedWriter(keyfw);
		keyfw.write(num);
	keybf.close();
	}
	private static int trackKeysRead() throws IOException{
	File file =new File("keyNum");
	Scanner scan = new Scanner(file);
	return scan.nextInt();
	}

}

