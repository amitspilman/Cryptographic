
public class CipherTextOnlyAttack {

	public static void main(String[] args) {
		String cipherText = CipherBlockChaining.uploadFile("CipherText_Example");			//encrypted text
		String initVector = CipherBlockChaining.uploadFile("IV_Example");					//initial vector
		String newKey = keyAttack(cipherText, initVector);
		System.out.println("new key is:"+"\n" + newKey);
	}

	public static String keyAttack(String encrypted_text, String initVector) {
		String[] dictionary = {"the", "of", "are", "and", "you", "can", "to", "have", "not", "you", "with",
				"this","but","his","from", "he", "her", "that", "in", "that", "was", "is", "has", "it",
				"him", "bright", "cold", "day", "april", "time", "example",	"by","for", "have","my",
				"not","yes","of","or","so","get","which"};

		String KEY = "abcdefgh";
		String[] keys = {""};

		StringPermutn (KEY,"",keys);		
		String[] keys_array = keys[0].split(" ");		// 8! = 40320	

		//Arrange key in the right way
		for (int i = 0; i < keys_array.length; i++) {
			String key = "";
			for (int j = 0; j < keys_array[i].length(); j++) {
				key += KEY.charAt(j)+" "+ keys_array[i].charAt(j) +"\n";
			}
			keys_array[i] = key+"";
		}

		String decrypted_text = "";
		int count= 0;

		for (int key_number = 1; key_number < keys_array.length; key_number++) {				// keys loop
			decrypted_text = CipherBlockChaining.decrypt(encrypted_text, initVector, keys_array[key_number]);		// decrypt

			for (int j = 0; j < dictionary.length; j++) {
				String dict_upper = dictionary[j].substring(0,1).toUpperCase() + dictionary[j].substring(1).toLowerCase()+"";
				if (decrypted_text.contains(" "+dictionary[j]) || decrypted_text.contains(dictionary[j]) || decrypted_text.contains(" "+dict_upper) || decrypted_text.contains(dict_upper)) {
					count++;
				}
			}
			System.out.println(count);
			if (count > 40) {
				System.out.println(decrypted_text);
				return keys_array[key_number];
			}
			else
				count=0;
		}
		System.out.println("couldent find it");
		return keys_array[0];
	}

	static void StringPermutn(String str, String ans, String[] s) { 	  
		if (str.length() == 0) { 
			s[0] += (ans + " "); 
			return; 
		} 
		for (int i = 0; i < str.length(); i++) { 
			char ch = str.charAt(i); 
			String ros = str.substring(0, i) + str.substring(i + 1); 
			StringPermutn(ros, ans + ch, s); 
		} 
	} 

}
