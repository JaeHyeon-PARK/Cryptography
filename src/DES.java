import java.util.*;
import java.math.*;

public class DES {
	static String plainText = "";
	static String beforeFP = "";
	static String key = "";
	static String leftKey = "";
	static String rightKey = "";
	static String leftPlain = "";
	static String rightPlain = "";
	static String roundLeft = "";
	static String roundRight = "";
	static byte[] plainByteArr;
	static byte[] keyByteArr;
	static byte[] afterIP;
	static byte[] cipher;
	static byte[] plain;
	static byte[] afterPD;
	static byte[] finalOutput;
	static String[] roundEncryptKey = new String[16];
	static String[] roundDecryptKey = new String[16];
	
	static final int[] IP = { // �ʱ� ġȯ ���̺�
		58, 50, 42, 34, 26, 18, 10, 2,
		60, 52, 44, 36, 28, 20, 12, 4,
		62, 54, 46, 38, 30, 22, 14, 6,
		64, 56, 48, 40, 32, 24, 16, 8,
		57, 49, 41, 33, 25, 17, 9,  1,
		59, 51, 43, 35, 27, 19, 11, 3,
		61, 53, 45, 37, 29, 21, 13, 5,
		63, 55, 47, 39, 31, 23, 15, 7
	};
	
	static final int[] FP = { // ���� ġȯ ���̺�
		40, 8, 48, 16, 56, 24, 64, 32,
		39, 7, 47, 15, 55, 23, 63, 31,
		38, 6, 46, 14, 54, 22, 62, 30,
		37, 5, 45, 13, 53, 21, 61, 29,
		36, 4, 44, 12, 52, 20, 60, 28,
		35, 3, 43, 11, 51, 19, 59, 27,
		34, 2, 42, 10, 50, 18, 58, 26,
		33, 1, 41,  9, 49, 17, 57, 25
	};
	
	static final int[] PD = { // Parity Drop ���̺� (64bit -> 56bit)
		57, 49, 41, 33, 25, 17,  9,  1,
		58, 50, 42, 34, 26, 18, 10,  2,
		59, 51, 43, 35, 27, 19, 11,  3,
		60, 52, 44, 36, 63, 55, 47, 39,
		31, 23, 15,  7, 62, 54, 46, 38,
		30, 22, 14,  6, 61, 53, 45, 37,
		29, 21, 13,  5, 28, 20, 12,  4
	};
	
	static final int[] CP = { // ���� P-Box ���̺� (56bit -> 48bit)
		14, 17, 11, 24,  1,  5,  3, 28,
		15,  6, 21, 10, 23, 19, 12,  4,
		26,  8, 16,  7, 27, 20, 13,  2,
		41, 52, 31, 37, 47, 55, 30, 40,
		51, 45, 33, 48, 44, 49, 39, 56,
		34, 53, 46, 42, 50, 36, 29, 32
	};
	
	static final int[] EP = { // Ȯ�� P-Box ���̺�(32bit -> 48bit)
		32,  1,  2,  3,  4,  5,
		 4,  5,  6,  7,  8,  9,
		 8,  9, 10, 11, 12, 13,
		12, 13, 14, 15, 16, 17,
		16, 17, 18, 19, 20, 21,
		20, 21, 22, 23, 24, 25,
		24, 25, 26, 27, 28, 29,
		28, 29, 30, 31, 32,  1
	};
	
	static final int[] FXP = { // f(x)�� straight P-Box(32bit -> 32bit)
		16,  7, 20, 21, 29, 12, 28, 17,
		 1, 15, 23, 26,  5, 18, 31, 10,
		 2,  8, 24, 14, 32, 27,  3,  9,
		19, 13, 30,  6, 22, 11,  4, 25
	};
	
	static final int[] SHIFTS = { // Key ���� �� Round �� Shift Ƚ��
		1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1
	};
	
	/* 8���� S-Box(48bit -> 32bit) */
	static final int[] S1 = {
		14,  4, 13,  1,  2, 15, 11,  8,  3, 10,  6, 12,  5,  9,  0,  7,
		 0, 15,  7,  4, 14,  2, 13,  1, 10,  6, 12, 11,  9,  5,  3,  8,
		 4,  1, 14,  8, 13,  6,  2, 11, 15, 12,  9,  7,  3, 10,  5,  0,
		15, 12,  8,  2,  4,  9,  1,  7,  5, 11,  3, 14, 10,  0,  6, 13
	};
	
	static final int[] S2 = {
		15,  1,  8, 14,  6, 11,  3,  4,  9,  7,  2, 13, 12,  0,  5, 10,
		 3, 13,  4,  7, 15,  2,  8, 14, 12,  0,  1, 10,  6,  9, 11,  5,
		 0, 14,  7, 11, 10,  4, 13,  1,  5,  8, 12,  6,  9,  3,  2, 15,
		13,  8, 10,  1,  3, 15,  4,  2, 11,  6,  7, 12,  0,  5, 14,  9
	};
	
	static final int[] S3 = {
		10,  0,  9, 14,  6,  3, 15,  5,  1, 13, 12,  7, 11,  4,  2,  8,
		13,  7,  0,  9,  3,  4,  6, 10,  2,  8,  5, 14, 12, 11, 15,  1,
		13,  6,  4,  9,  8, 15,  3,  0, 11,  1,  2, 12,  5, 10, 14,  7,
		 1, 10, 13,  0,  6,  9,  8,  7,  4, 15, 14,  3, 11,  5,  2, 12
	};
	
	static final int[] S4 = {
		 7, 13, 14,  3,  0,  6,  9, 10,  1,  2,  8,  5, 11, 12,  4, 15,
		13,  8, 11,  5,  6, 15,  0,  3,  4,  7,  2, 12,  1, 10, 14,  9,
		10,  6,  9,  0, 12, 11,  7, 13, 15,  1,  3, 14,  5,  2,  8,  4,
		 3, 15,  0,  6, 10,  1, 13,  8,  9,  4,  5, 11, 12,  7,  2, 14
	};
	
	static final int[] S5 = {
		 2, 12,  4,  1,  7, 10, 11,  6,  8,  5,  3, 15, 13,  0, 14,  9,
		14, 11,  2, 12,  4,  7, 13,  1,  5,  0, 15, 10,  3,  9,  8,  6,
		 4,  2,  1, 11, 10, 13,  7,  8, 15,  9, 12,  5,  6,  3,  0, 14,
		11,  8, 12,  7,  1, 14,  2, 13,  6, 15,  0,  9, 10,  4,  5,  3
	};
	
	static final int[] S6 = {
		12,  1, 10, 15,  9,  2,  6,  8,  0, 13,  3,  4, 14,  7,  5, 11,
		10, 15,  4,  2,  7, 12,  9,  5,  6,  1, 13, 14,  0, 11,  3,  8,
		 9, 14, 15,  5,  2,  8, 12,  3,  7,  0,  4, 10,  1, 13, 11,  6,
		 4,  3,  2, 12,  9,  5, 15, 10, 11, 14,  1,  7,  6,  0,  8, 13
	};
	
	static final int[] S7 = {
		 4, 11,  2, 14, 15,  0,  8, 13,  3, 12,  9,  7,  5, 10,  6,  1,
		13,  0, 11,  7,  4,  9,  1, 10, 14,  3,  5, 12,  2, 15,  8,  6,
		 1,  4, 11, 13, 12,  3,  7, 14, 10, 15,  6,  8,  0,  5,  9,  2,
		 6, 11, 13,  8,  1,  4, 10,  7,  9,  5,  0, 15, 14,  2,  3, 12
	};
	
	static final int[] S8 = {
		13,  2,  8,  4,  6, 15, 11,  1, 10,  9,  3, 14,  5,  0, 12,  7,
		 1, 15, 13,  8, 10,  3,  7,  4, 12,  5,  6, 11,  0, 14,  9,  2,
		 7, 11,  4,  1,  9, 12, 14,  2,  0,  6, 10, 13, 15,  3,  5,  8,
		 2,  1, 14,  7,  4, 10,  8, 13, 15, 12,  9,  0,  3,  5,  6, 11
	};
	
	static byte[] hexStringToByteArray(String string) { // 16���� ���ڿ��� byte �迭�� ��ȯ �� ��ȯ
		int length = string.length();
		int n = (int)Math.ceil((length + 1) / 2);
		byte[] result = new byte[n];		
		for (int i = length - 1; i >= 0; i -= 2) {	
			if (i == 0)
				result[i / 2] = (byte) ((Character.digit('0', 16) << 4) + Character.digit(string.charAt(i), 16));
			else
				result[i / 2] = (byte) ((Character.digit(string.charAt(i - 1), 16) << 4) + Character.digit(string.charAt(i), 16));
		}
		return result;
	}
	
	static String bytesToBinaryString(byte b) { // byte ���ڸ� ������ ���ڿ��� ��ȯ �� ��ȯ
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 8; i++)
            builder.append(((0x80 >>> i) & b) == 0 ? '0' : '1');
        return builder.toString();
    }
	
	static void rotateKey(int round) {
		byte[] temp;
		byte[] output = null;
		String tmpLeft = null, tmpRight = null;
		String rotLeftBit = null, rotRightBit = null; // ��ȯ�� 1~2bit Ű ��
		StringBuilder outSB = new StringBuilder();
		
		rotLeftBit = leftKey.substring(0, SHIFTS[round]); // Left 28bit�� ���� �۾�
		tmpLeft = leftKey.substring(SHIFTS[round], leftKey.length());
		tmpLeft += rotLeftBit;
		leftKey = tmpLeft;
		
		rotRightBit = rightKey.substring(0, SHIFTS[round]); // Right 28bit�� ���� �۾�
		tmpRight = rightKey.substring(SHIFTS[round], rightKey.length());
		tmpRight += rotRightBit;
		rightKey = tmpRight;
		String outStr = leftKey + rightKey; // 56bit�� ����
		temp = new BigInteger(outStr, 2).toByteArray();

		if(temp.length == 8)
			output = Arrays.copyOfRange(temp, 1, temp.length); // ���� �� byte�� ����
		else
			output = Arrays.copyOfRange(temp, 0, temp.length); // ���� ���� byte�� �������� ����
		
		finalOutput = permutation(output, CP, round, 6);

		for(int i = 0; i < finalOutput.length; i++) // StringBuilder ���·� ��ȯ
			outSB.append(bytesToBinaryString(finalOutput[i]));

		roundEncryptKey[round] = outSB.toString();
	}
	
	static byte[] permutation(byte[] input, int[] table, int round, int len) { // Permutation Function
		int outLen = 1 + (table.length - 1) / 8; // input �迭�� ũ�� ��ġ
		byte[] output = new byte[outLen];
		
		// append�� ���� StringBuilder ���
		StringBuilder inputSB = new StringBuilder();
		StringBuilder outputSB = new StringBuilder();
		
		for(int i = 0; i < input.length; i++) // ������ ���ڿ� ��ȯ
			inputSB.append(bytesToBinaryString(input[i]));
		String inputStr = inputSB.toString();
		
		
		for(int i = 0; i < table.length; i++) { // table�� �������� ġȯ �۾�
			outputSB.append(inputStr.charAt(table[i] - 1));
		}
		
		String outputStr = outputSB.toString();
		
		byte[] temp = new BigInteger(outputStr, 2).toByteArray(); // ��� ���ڿ��� byte �迭 ���·� ��ȯ
		
		if(temp.length == len + 1)
			temp = Arrays.copyOfRange(temp, 1, temp.length);
		else
			temp = Arrays.copyOfRange(temp, 0, temp.length);

		if(table[0] == 58) { // �ʱ� ġȯ�� ���
			if(temp.length == 9)
				output = Arrays.copyOfRange(temp, 1, temp.length); // ���� �� byte�� �������� ����
			else
				output = Arrays.copyOfRange(temp, 0, temp.length); // ���� �� byte�� ����
		}
		else if(table[0] == 40) { // ���� ġȯ�� ���
			if(temp.length == 9)
				output = Arrays.copyOfRange(temp, 1, temp.length); // ���� ���� byte�� ����
			else
				output = Arrays.copyOfRange(temp, 0, temp.length); // ���� ���� byte�� �������� ����
		}
		else if(table[0] == 57) { // �и�Ƽ ������ ���
			if(temp.length == 8)
				output = Arrays.copyOfRange(temp, 1, temp.length); // ���� �� byte�� ����
			else
				output = Arrays.copyOfRange(temp, 0, temp.length); // ���� �� byte�� �������� ����
		}
		else if(table[0] == 14) { // Key ������ ���� P-Box�� ���
			if(temp.length == 7)
				output = Arrays.copyOfRange(temp, 1, temp.length); // ���� �� byte�� ����
			else
				output = Arrays.copyOfRange(temp, 0, temp.length); // ���� �� byte�� �������� ����
		}
		else if(table[0] == 32) {// f(x)�� Ȯ�� P-Box�� ���
			if(temp.length == 7)
				output = Arrays.copyOfRange(temp, 1, temp.length); // ���� ���� byte�� ����
			else
				output = Arrays.copyOfRange(temp, 0, temp.length); // ���� �� byte�� �������� ����
		}
		else if(table[0] == 16) { // f(x)�� Straight P-Box�� ���
			if(temp.length == 5)
				output = Arrays.copyOfRange(temp, 1, temp.length); // ���� �� byte�� ����
			else
				output = Arrays.copyOfRange(temp, 0, temp.length); // ���� �� byte�� �������� ����
		}
		
		return output;
	}
	
	/* Round �Լ� */
	static void roundFunction(int round, String mode) {
		byte[] tmpLeft = new BigInteger(roundLeft, 2).toByteArray();
		byte[] afterFX = fx(roundRight, round, mode);
		StringBuilder newLeft = new StringBuilder();
		StringBuilder afterMix = new StringBuilder();
		
		if(tmpLeft.length == 5)
			tmpLeft = Arrays.copyOfRange(tmpLeft, 1, tmpLeft.length); // ���� ���� byte�� ����
		else
			tmpLeft = Arrays.copyOfRange(tmpLeft, 0, tmpLeft.length); // ���� ���� byte�� �������� ����
		
		if(round != 15) { // mixer + swapper
			/* Mixer */
			for(int i = 0; i < tmpLeft.length; i++) { // XOR ���� ����
				tmpLeft[i] = (byte) (tmpLeft[i] ^ afterFX[i]);
			}
			
			/* Swapper */
			roundLeft = roundRight; // L(i) = R(i - 1)
			for(int i = 0; i < tmpLeft.length; i++) // R(i) = L(i - 1) XOR f(x)
				afterMix.append(bytesToBinaryString(tmpLeft[i]));
			roundRight = afterMix.toString();
		}
		else { // mixer�� ����
			for(int i = 0; i < tmpLeft.length; i++) { // XOR ���� ����
				tmpLeft[i] = (byte) (tmpLeft[i] ^ afterFX[i]);
			}
			
			for(int i = 0; i < afterFX.length; i++) // R(i) = L(i - 1) XOR f(x)
				newLeft.append(bytesToBinaryString(tmpLeft[i]));
			roundLeft = newLeft.toString();
			roundRight = roundRight;
		}
	}
	
	static byte[] fx(String right, int round, String mode) {
		byte[] tmpRight = new BigInteger(right, 2).toByteArray(); // ��� ���ڿ��� byte �迭 ���·� ��ȯ
		byte[] rKey;
		
		if(mode.equals("Encrypt"))
			rKey = new BigInteger(roundEncryptKey[round], 2).toByteArray();
		else
			rKey = new BigInteger(roundDecryptKey[round], 2).toByteArray();
		
		if(tmpRight.length == 5)
			tmpRight = Arrays.copyOfRange(tmpRight, 1, tmpRight.length); // ���� �� byte�� ����
		else
			tmpRight = Arrays.copyOfRange(tmpRight, 0, tmpRight.length); // ���� �� byte�� �������� ����
		
		if(rKey.length == 7)
			rKey = Arrays.copyOfRange(rKey, 1, rKey.length); // ���� �� byte�� ����
		else
			rKey = Arrays.copyOfRange(rKey, 0, rKey.length); // ���� �� byte�� �������� ����
		
		tmpRight = permutation(tmpRight, EP, round, 6);
		
		for(int i = 0; i < tmpRight.length; i++) { // XOR ���� ����
			tmpRight[i] = (byte) (tmpRight[i] ^ rKey[i]);
		}

		tmpRight = sBox(tmpRight); // S-Box ����()
		tmpRight = permutation(tmpRight, FXP, round, 4);

		return tmpRight;
	}
	
	static byte[] sBox(byte[] input) {		
		input = split(input,6);
		byte[] output = new byte[input.length/2];
		int halfLeft = 0;		
		for (int i = 0; i < input.length; i++) {
			byte block = input[i];			
			int row = 2 * (block >> 7 & 0x0001) + (block >> 2 & 0x0001);
			int col = block >> 3 & 0x000F;
			int[] selectedSBox = getSBox(i);
			int halfRight = selectedSBox[16 * row + col];
			if (i % 2 == 0) {
				halfLeft = halfRight;
			} else {
				output[i/2] = (byte) (16 * halfLeft + halfRight);
				halfLeft = 0;
			}
		}
		return output;
	}
	
	static int[] getSBox(int i) {
		switch (i) {
			case 0: return S1;
			case 1: return S2;
			case 2: return S3;
			case 3: return S4;
			case 4: return S5;
			case 5: return S6;
			case 6: return S7;
			case 7: return S8;	
			default: return null;			
		}
	}
	
	static byte[] split(byte[] input, int length) { // 6bit * 8���� �и�
		int byteLen = (8 * input.length - 1) / length + 1;
		byte[] output = new byte[byteLen];
		for (int i = 0; i < byteLen; i++) {
			for (int j = 0; j < length; j++) {
				int value = getBitFromArray(input, length * i + j);				
				setBitInArray(output, 8 * i + j, value);
			}
		}
		return output;
	}
	
	static int getBitFromArray(byte[] array, int pos) { // byte �迭���� Ư�� ��ġ�� bit �� ����
		int value;
		int bytePos = pos / 8;
		int bitPos = pos % 8;		
		value = (array[bytePos] >> (8 - (bitPos + 1))) & 0x0001;		
		return value;
	}
	
	static void setBitInArray(byte[] input, int pos, int value) { // byte �迭�� Ư�� ��ġ�� bit �� ����
		int bytePos = pos / 8;
		int bitPos = pos % 8;		
		byte old = input[bytePos];
		old = (byte) (((0xFF7F >> bitPos) & old) & 0x00FF);
		byte newByte = (byte) ((value << (8 - (bitPos + 1))) | old);
	    input[bytePos] = newByte;
	}
	
	public static void main(String[] args) {
		System.out.println("������: �������б� ��ǻ�Ͱ��а� 21511746 ������");
		Scanner sc = new Scanner(System.in);
		
		System.out.print("64bit ���� 16���� ���·� �Է�: ");
		plainText = sc.nextLine();
		System.out.print("64bit Ű�� 16���� ���·� �Է�: ");
		key = sc.nextLine();
		
		StringBuilder plainSB = new StringBuilder();
		StringBuilder afterIPSB = new StringBuilder();
		
		/* ��ȣȭ ���� */
		plainByteArr = hexStringToByteArray(plainText);
		System.out.print("Plain Text: ");
		for(int i = 0; i < plainByteArr.length; i++) { // ������ ������ Plain Text ���
			System.out.print(bytesToBinaryString(plainByteArr[i]) + " ");
			plainSB.append(bytesToBinaryString(plainByteArr[i]));
		}
		System.out.println();
		
		keyByteArr = hexStringToByteArray(key);
		System.out.print("Key: ");
		for(int i = 0; i < keyByteArr.length; i++) // ������ ������ Key ���
			System.out.print(bytesToBinaryString(keyByteArr[i]) + " ");
		System.out.println();
		
		afterIP = permutation(plainByteArr, IP, -1, 8); // �ʱ�ġȯ
		
		afterPD = permutation(keyByteArr, PD, -1, 7); // �и�Ƽ ����
		
		/* Ű�� 28bit�� ���� */
		StringBuilder keySB = new StringBuilder();
		for(int i = 0; i < afterPD.length; i++) // ������ ���ڿ� ��ȯ
			keySB.append(bytesToBinaryString(afterPD[i]));

		leftKey = keySB.substring(0, keySB.length() / 2); // byte ��ȯ �� 0000�� ä���� -> String ���·� ����
		rightKey = keySB.substring(keySB.length() / 2, keySB.length());
		
		/* �� ������ Ű ���� */
		for(int i = 0; i < 16; i++) {
			rotateKey(i);
		}
		
		for(int i = 0; i < afterIP.length; i++) { // StringBuilder ������ �ʱ�ġȯ ���
			afterIPSB.append(bytesToBinaryString(afterIP[i]));
		}
		
		leftPlain = afterIPSB.substring(0, afterIPSB.length() / 2);
		rightPlain = afterIPSB.substring(afterIPSB.length() / 2, afterIPSB.length());
		
		roundLeft = leftPlain;
		roundRight = rightPlain;
		
		/* Round 1 ~ Round 16 ���� */
		for(int i = 0; i < 16; i++) {
			roundFunction(i, "Encrypt");
		}
		
		beforeFP = roundLeft + roundRight;
		byte[] tmpBeforeFP = new BigInteger(beforeFP, 2).toByteArray(); // ��ȣ ���ڿ��� byte �迭 ���·� ��ȯ
		if(tmpBeforeFP.length == 9)
			tmpBeforeFP = Arrays.copyOfRange(tmpBeforeFP, 1, tmpBeforeFP.length); // ���� �� byte�� ����
		else
			tmpBeforeFP = Arrays.copyOfRange(tmpBeforeFP, 0, tmpBeforeFP.length); // ���� ���� byte�� �������� ����
		
		cipher = permutation(tmpBeforeFP, FP, -1, 8); // ���� ġȯ
		System.out.print("After Encrypt: ");
		for(int i = 0; i < cipher.length; i++) // ���� ġȯ ��� ���
			System.out.print(bytesToBinaryString(cipher[i]) + " ");
		System.out.println();
		
		/* ��ȣȭ ���� */
		for(int i = 0; i < 16; i++) { // ��ȣȭ�� ���� Ű(��ȣȭ Ű ������ �Ųٷ�)
			roundDecryptKey[i] = roundEncryptKey[15 - i];
		}
		
		afterIP = permutation(cipher, IP, -1, 8); // �ʱ�ġȯ
		afterIPSB.setLength(0); // ��ȣȭ �� ����� ������ �ʱ�ȭ
		
		for(int i = 0; i < afterIP.length; i++) { // StringBuilder ������ �ʱ�ġȯ ���
			afterIPSB.append(bytesToBinaryString(afterIP[i]));
		}
		
		leftPlain = afterIPSB.substring(0, afterIPSB.length() / 2);
		rightPlain = afterIPSB.substring(afterIPSB.length() / 2, afterIPSB.length());
		
		roundLeft = leftPlain;
		roundRight = rightPlain;
		
		/* Round 1 ~ Round 16 ���� */
		for(int i = 0; i < 16; i++) {
			roundFunction(i, "Decrypt");
		}
		
		beforeFP = roundLeft + roundRight;
		tmpBeforeFP = new BigInteger(beforeFP, 2).toByteArray(); // ��ȣ ���ڿ��� byte �迭 ���·� ��ȯ
		if(tmpBeforeFP.length == 9)
			tmpBeforeFP = Arrays.copyOfRange(tmpBeforeFP, 1, tmpBeforeFP.length); // ���� �� byte�� ����
		else
			tmpBeforeFP = Arrays.copyOfRange(tmpBeforeFP, 0, tmpBeforeFP.length); // ���� ���� byte�� �������� ����
		
		plain = permutation(tmpBeforeFP, FP, -1, 8); // ���� ġȯ
		System.out.print("After Decrypt: ");
		for(int i = 0; i < plain.length; i++) // ���� ġȯ ��� ���
			System.out.print(bytesToBinaryString(plain[i]) + " ");
		System.out.println();
	}
}