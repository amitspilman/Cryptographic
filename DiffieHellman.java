
import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;


public class DiffieHellman {

	public static void main(String[] args) {
		String plain_message = CipherBlockChaining.uploadFile("PlainMessage_Example");			//short message
		String sender_info = "group_4";
		String CA_info = "CyberSecurity2020";

		int length = (plain_message.getBytes().length +4)/2;

		HashMap<BigInteger, BigInteger> p_q_primes = RSA.generate_p_q_different_primes (plain_message + sender_info + CA_info);
		BigInteger p = new BigInteger(p_q_primes.keySet().toArray()[0].toString());
		BigInteger q = p_q_primes.get(p);
		BigInteger n = p.multiply(q);
		BigInteger fi_n = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));

		BigInteger e = RSA.getPublicKey(fi_n);
		BigInteger d = e.modInverse(fi_n);

		BigInteger private_Xa = BigInteger.probablePrime(length , new Random());	//	private key A
		BigInteger private_Xb = BigInteger.probablePrime(length , new Random());	//	private key B

		while(private_Xa.compareTo(q) >= 0 || private_Xb.compareTo(q) >= 0) {
			private_Xa = BigInteger.probablePrime(length , new Random());
			private_Xb = BigInteger.probablePrime(length , new Random());
		}

		q = BigInteger.valueOf(7);
		private_Xa = BigInteger.valueOf(5);
		private_Xb = BigInteger.valueOf(3);

		BigInteger[] DiffieHellman_Array = diffie_hellman(q, private_Xa, private_Xb);
		byte[] hash = get_digest(sender_info, DiffieHellman_Array[4], CA_info);

		byte[] digest = sign_certificate(hash, e, n);
		byte[] digest_out = get_hash_from_certificate(digest, d, n);

		System.out.println(Arrays.equals(hash, digest_out));
	}


	public static BigInteger[] diffie_hellman(BigInteger q, BigInteger private_Xa, BigInteger private_Xb) {
		BigInteger alpha = generatePrimitiveRoot(q);
		System.out.println("alpha: "+alpha);

		BigInteger Ya = alpha.modPow(private_Xa, q);	//	public key A
		BigInteger Yb = alpha.modPow(private_Xb, q);	//	public key B

		BigInteger Ka = Yb.modPow(private_Xa, q);	//symmetric key	A
		BigInteger Kb = Ya.modPow(private_Xb, q);	//symmetric key B

		BigInteger[] resualts = {Kb,Ka,Yb,private_Xb,Ya,private_Xa,alpha};
		return resualts;
	}

	public static byte[] get_digest(String sender_info, BigInteger sender_public_key, String CA_info) {
		byte[] si = sender_info.getBytes();
		byte[] pk = sender_public_key.toByteArray();
		byte[] ca = CA_info.getBytes();
		byte[] combined = new byte[pk.length + si.length + ca.length];

		System.arraycopy(si,0,combined,0,si.length);
		System.arraycopy(pk,0,combined,si.length,pk.length);
		System.arraycopy(ca,0,combined,si.length+pk.length,ca.length);
		return combined;
	}

	public static byte[] sign_certificate(byte[] hash, BigInteger public_key, BigInteger n) {
		BigInteger bi = new BigInteger(hash);
		String text = new String(bi.toByteArray());
		return RSA.encrypt(text, public_key, n);
	}

	public static byte[] get_hash_from_certificate(byte[] signed_certificate,BigInteger private_key,BigInteger n) {
		return RSA.decrypt(signed_certificate, private_key, n).getBytes();	
	}


	private static BigInteger generatePrimitiveRoot(BigInteger q) {
		BigInteger a = generateUniform(q);
		return q.subtract(a.multiply(a).mod(q)); 
	}

	private static BigInteger generateUniform(BigInteger q){
		q = q.subtract(new BigInteger(4+""));

		BigInteger a;
		do {
			a = new BigInteger(q.bitLength(), new Random());
		}while (a.compareTo(q) >= 0);

		return a.add(new BigInteger(2+""));
	}

}
