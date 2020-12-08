import java.math.BigInteger;
import java.util.HashMap;

public class DigitalEnvelope {

	public static void main(String[] args) {
		String plain_message = CipherBlockChaining.uploadFile("PlainMessage_Example");			//short message
		String initVector = CipherBlockChaining.uploadFile("IV_Example");					//initial vector
		String key_ah = CipherBlockChaining.uploadFile("Key_Example");						//a-h key

		HashMap<BigInteger, BigInteger> p_q_primes = RSA.generate_p_q_different_primes(plain_message);

		BigInteger p = new BigInteger(p_q_primes.keySet().toArray()[0].toString());
		BigInteger q = p_q_primes.get(p);
		BigInteger n = p.multiply(q);
		BigInteger fi_n = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));

		BigInteger e = RSA.getPublicKey(fi_n);
		BigInteger d = e.modInverse(fi_n);

		HashMap<byte[],String> DigitalEnvelope=create_envelope(initVector, plain_message, key_ah, e, n);
		String envelope_text = open_envelope(DigitalEnvelope,initVector,d,n);
		System.out.println("dycripted_message: " + envelope_text);
	}

	public static HashMap<byte[],String> create_envelope(String init_vector, String plain_text, String symmetric_key,BigInteger public_key, BigInteger n){ ///Q1
		String CipherText= CipherBlockChaining.encrypt(plain_text, init_vector, symmetric_key);
		byte [] keyEncrepted= RSA.encrypt(symmetric_key, public_key, n);	

		HashMap<byte[],String> envelope = new HashMap<>();
		envelope.put(keyEncrepted,CipherText);			
		return envelope;

	}

	public static String open_envelope(HashMap<byte[],String> envelope, String init_vector, BigInteger private_key,BigInteger n) {
		byte[] byteArray =  (byte[]) envelope.keySet().toArray()[0];
		String decKey= RSA.decrypt(byteArray,private_key,n);	
		String decText= CipherBlockChaining.decrypt(envelope.get(byteArray), init_vector, decKey);
		return decText;
	}

}
