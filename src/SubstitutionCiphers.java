public class SubstitutionCiphers {
	public static void main(String[] args) {
		StringBuilder ciphers = new StringBuilder( // 암호화문
				"JCTY JYEVDSOQ TYEFMRSJTJ BSJOYL SR OFY NDYHSCVJ JYEOSCR EMR ZY STNBYTYROYL VJSRA EDQNOCADMNFQ. "
				+ "EDQNOCADMNFQ, M WCDL WSOF ADYYX CDSASR, TYMRJ \"JYEDYO WDSOSRA\". "
				+ "FCWYHYD, WY VJY OFY OYDT OC DYPYD OC OFY JESYREY MRL MDO CP ODMRJPCDTSRA TYJJMAYJ OC TMXY OFYT JYEVDY MRL STTVRY OC MOOMEXJ. "
				+ "MBOFCVAF SR OFY NMJO EDQNOCADMNFQ DYPPYDYL CRBQ OC OFY YREDQOSCR MRL LYEDQNOSCR CP TYJJMAYJ VJSRA JYEYDO XYQJ, "
				+ "OCLMQ SO SJ LYPSRYL MJ SRHCBHSRA OFDYY LSJOSREO TYEFMRSJTJ: JQTTYODSE XYQ YREDSNFYDTYRO, MJQTTYODSE XYQ YRESNFYDTYRO, MRL FMJFSRA. "
				+ "WY WSBB ZDSYPBQ LSJEVJJ OFYJY OFDYY TYEFMRSJTJ FYDY. SR JQTTYODSE YRESNFYDTYRO, MR YROSOQ, JMQ MBSEY, EMR JYRL M TYJJMAY OC COFYD YROSOQ, "
				+ "JMQ ZCZ, CHYD MR SRJYEVDY EFMRRYB WSOF OFY MJJVTNOSCR OFMO MR MLHYDJMDQ, JMQ YHY, "
				+ "EMRRCO VRLYDJOMRL OFY ECROYROJ CP OFY TYJJMAY ZQ JSTNBQ YMHYJLDCNNSRA CHYD OFY EFMRRYB. "
				+ "MBSEY YREDQNOJ OFY TYJJMAY VJSRA MR YREDQNOSCR MBACDSOFT. ZCZ LYEDQNOJ OFY TYJJMAY VJSRA M LYEDQNOSCR MBACDSOFT. "
				+ "JQTTYODSE XYQ YRESNFYDTYRO VJYJ M JSRABY JYEDYO XYQ PCD ZCOF YREDQNOSCR MRL LYEDQNOSCR. "
				+ "YREDQNOSCR/LYEDQNOSCR EMR ZY OFCVAFO CP MJ YBYEODCRSE BCEXSRA JQJOYT. "
				+ "SR JQTTYODSE XYQ YRESNFYDSRA, MBSEY NVOJ OFY TYJJMAY SR M ZCG MRL BCEXJ OFY ZCG VJSRA OFY JFMDYL JYEDYO XYQ; "
				+ "ZCZ VRBCEXJ OFY ZCG WSOF OFY JMTY XYQ MRL OMXYJ CVO OFY TYJJMAYJ. "
				+ "SR MJQTTYODSE YRESNFYDTYRO, WY FMHY OFY JMTY JSOVMOSCR MMJ OFY JQTTYODSE XYQ YRESNFYDTYRO, WSOF M PYW YGEYNOSCRJ. "
				+ "PSDJO, OFYDY MDY OWC XYQJ SRJOYML CP CRY; CRY NVZBSE XYQ MRL CRY NDSHMOY XYQ. "
				+ "OC JYRL M JYEVDY TYJJMAY OC ZCZ, MBSEY PSDJOJ YREDQNOJ OFY TYJJMAY VJSRA ZCZ\"J NVZBSE XYQ. "
				+ "OC LYEDQNOJ OFY TYJJMAY, ZCZ VJYJ FSJ CWR NDSHMOY XYQ");
		StringBuilder p = new StringBuilder("");
		int[] alphaCount = new int[26]; // idx = 0: 'A'의 갯수, idx = 25: 'Z'의 갯수 
		
		// 암호문 -> 평문을 위한 표
		char[] transfer = {'G', 'L', 'O', 'R', 'C', 'H', 'J', 'V', 'Q', 'S', 'X', 'D', 'A', // 'A' to 'M'
							'P', 'T', 'F', 'Y', 'N', 'I', 'M', 'Z', 'U', 'W', 'K', 'E', 'B'}; // 'N' to 'Z'
		
		for(int i = 0; i < ciphers.length(); i++) { // 문자 변환
			char temp = ciphers.charAt(i);
			
			if(65 <= temp && temp <= 90) {
				alphaCount[(int)temp - 65]++; // 알파벳 빈도 수 증가
				char changeChar = transfer[(int)temp - 65]; // transfer 배열에 맞게 복호화 수행
				p.append(changeChar);
			}
			else p.append(temp); // 알파벳이 아닌 경우 그대로 출력
		}
		
		System.out.println("--- 알파벳의 빈도 수 ---");
		for(int i = 0; i < 26; i++) {
			char alphabet = (char)(65 + i);
			System.out.println(alphabet + ": " + alphaCount[i]);
		}
		
		System.out.println("\n--- 해독 결과 ---");
		System.out.println(p);
	}
}