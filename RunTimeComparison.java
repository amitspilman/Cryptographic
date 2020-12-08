import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Scanner;

public class RunTimeComparison {

	public static void main(String[] args) {
		String MB = CipherBlockChaining.uploadFile("1MB");
		//String MB = CipherBlockChaining.uploadFile("10MB");
		//String MB = CipherBlockChaining.uploadFile("20MB");
		//String MB = CipherBlockChaining.uploadFile("50MB");
		//String MB = CipherBlockChaining.uploadFile("100MB");
		
//		String IVLong = CipherBlockChaining.uploadFile("IVLong_Example");
//		String KeyLong= CipherBlockChaining.uploadFile("KeyLong_Example");
		System.out.println("read text file: "+ System.currentTimeMillis()/1000 + " sec");
 
//		Long startTime= System.currentTimeMillis();
//		CipherBlockChaining.encrypt(MB, IVLong, KeyLong);
//		//CipherBlockChaining.decrypt(MB, IVLong, KeyLong);
//		
//		Long endTime= System.currentTimeMillis();
//		Long RunTime= (endTime - startTime)/1000; //in seconds
//		System.out.println("Run_Time: "+RunTime);
		
		HashMap<BigInteger, BigInteger> p_q_primes = RSA.generate_p_q_different_primes(MB);

		BigInteger p = new BigInteger(p_q_primes.keySet().toArray()[0].toString());
		BigInteger q = p_q_primes.get(p);
		BigInteger n = p.multiply(q);
		BigInteger fi_n = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));

		BigInteger e = RSA.getPublicKey(fi_n);
		BigInteger d = e.modInverse(fi_n); 
		
		System.out.println("startTime: "+ System.currentTimeMillis()/1000 + " sec");
		byte[] res =RSA.encrypt(MB, e, n);
		System.out.println("middleTime: "+ System.currentTimeMillis()/1000 + " sec");
		RSA.decrypt(res, d, n);
		System.out.println("endTime: "+System.currentTimeMillis()/1000 + " sec");

	}
	
}
