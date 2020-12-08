import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class CipherBlockChaining {

	public static void main(String[] args) {
		String plainText = uploadFile("PlainText_Example");				//text
		String key_ah = uploadFile("Key_Example");						//a-h key
		String initVector = uploadFile("IV_Example");					//initial vector

		String encrypted_text = encrypt(plainText, initVector, key_ah);
		String decrypted_text = decrypt(encrypted_text, initVector, key_ah);
		System.out.println("\n"+"decrypted text: \n\n" + decrypted_text);
	}

	public static String encrypt(String plain_text, String initVector, String key) {
		String[] blocks = str2Blocks(plain_text);
		String[] blocksXORed = new String[blocks.length];
		char[][] keyValueArry = getStringKeyValue(key);	//[0-key, 1-value][letters]
		String[] cipher_text = new String[blocks.length];

		for (int i = 0; i < blocks.length; i++) {
			blocksXORed[i] = XOR(blocks[i],initVector);
			cipher_text[i] = encryptWithKey(blocksXORed[i], keyValueArry);
		}
		return blocks2Str(cipher_text);
	}

	public static String decrypt(String encrypted_text, String initVector, String key) {
		String[] encryptedBlocks = str2Blocks(encrypted_text);
		char[][] keyValueArry = getStringKeyValue(key);	//[0-key, 1-value][letters]

		String[] decrypt_text = new String[encryptedBlocks.length];
		String[] results = new String[encryptedBlocks.length];

		for (int i = 0; i < encryptedBlocks.length; i++) {
			decrypt_text[i] = unCryptWithKey(encryptedBlocks[i], keyValueArry);
			results[i] = XOR(decrypt_text[i],initVector);
		}
		return blocks2Str(results);
	}

	public static String[] str2Blocks(String text) {
		int rows = text.length() / 10 + ((text.length() % 10 > 0) ? 1 : 0);
		String[] stringArray = new String[rows];

		for(int i = 0, j = 0, l = text.length(); i < l; i += 10, j++)
			stringArray[j] = text.substring(i, Math.min(l, i + 10));

		for (int i = 0; i < stringArray.length; i++)
			while (stringArray[i].length() < 10)
				stringArray[i] += "\0";

		return stringArray;
	}

	public static String blocks2Str(String[] blocks) {
		String text ="";
		for (int i=0 ; i<blocks.length; i++) {
			text += blocks[i];
		}
		return text;
	}

	public static String XOR(String block, String initVector) {
		String strArr = "";

		for (int j = 0; j < block.length(); j++) {
			strArr += ((char)(block.charAt(j)^initVector.charAt(j) ))+"";
		}
		return strArr;
	}



	public static char[][] getStringKeyValue(String text) {
		String newText = new String(text);

		for (int i = 0; i < newText.length(); i++) {
			//if(!((newText.charAt(i) >=65 && newText.charAt(i) <=90) || (newText.charAt(i) >=97 && newText.charAt(i) <=122))) {
			if( ! ( ((newText.charAt(i)+"").matches("[A-Z?]")) || ((newText.charAt(i)+"").matches("[a-z?]")))) {
				newText = newText.substring(0,i) + newText.substring(i+1);
				i-=1;
			}
		}

		char[][] keyValueArry = new char[2][newText.length()/2];
		int i=0;
		while (newText.length()>0) {
			keyValueArry[0][i] = newText.charAt(0);
			keyValueArry[1][i++] = newText.charAt(1);
			if(newText.length()%2==0)
				newText = newText.substring(2);
		}
		return keyValueArry;
	}

	private static String encryptWithKey(String text, char[][] keyValueArry) {
		for (int j = 0; j < text.length(); j++) {
			for (int m = 0; m < keyValueArry[0].length; m++) {
				if (text.charAt(j) == (keyValueArry[0][m])) {
					text = text.substring(0,j) + keyValueArry[1][m] + text.substring(j+1);
					break;
				}
			}
		}
		return text;
	}

	private static String unCryptWithKey(String encryptedString, char[][] keyValueArry) {
		String resualts = encryptedString+"";
		for (int j = 0; j < resualts.length(); j++) {
			for (int m = 0; m < keyValueArry[1].length; m++) {
				if (resualts.charAt(j) == keyValueArry[1][m]) {
					resualts = resualts.substring(0,j) + keyValueArry[0][m] + resualts.substring(j+1);
					break;
				}
			}
		}
		return resualts;
	}

	public static String uploadFile(String path) {
		String str = "";
		File file = new File(path+ ".txt"); 

		Scanner scanner;
		try {
			scanner = new Scanner(file);
			str = scanner.useDelimiter("\\A").next();
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}	
		return str;
	}

}
