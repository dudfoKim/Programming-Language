package cute14Scanner;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 과제번호 : 03
 * 분반 : 02반
 * 소속 : 컴퓨터공학과
 * 학번 : 201002384
 * 이름 : 김범철
 */

public class Scanner {
	public enum TokenType { //문자,숫자 에 대한 번호 지정.
		INT(1), ID(2), MINUS(3), PLUS(4), L_PAREN(5), R_PAREN(6), TRUE(7), FALSE(
				8), TIMES(9), DIV(10), LT(11), GT(12), EQ(13), APOSTROPHE(14), DEFINE(
				15), LAMBDA(16), COND(17), QUOTE(18), NOT(19), CAR(20), CDR(21), CONS(
				22), ATOM_Q(23), NULL_Q(24), EQ_Q(25);

		private final int finalState;

		TokenType(int finalState) {
			this.finalState = finalState;
		}

		public static TokenType keyWordCheck(String str) {
			return KeyWord.m.get(str); // if not keyword return null
		}

		private enum KeyWord { // Keyword들 정의.
			DEFINE("define", TokenType.DEFINE), LAMBDA("lambda",
					TokenType.LAMBDA), COND("cond", TokenType.COND), QUOTE(
					"quote", TokenType.QUOTE), NOT("not", TokenType.NOT), CDR(
					"cdr", TokenType.CDR), CAR("car", TokenType.CAR), CONS(
					"cons", TokenType.CONS), EQ("eq?", TokenType.EQ_Q), NULL(
					"null?", TokenType.NULL_Q), ATOM("atom?", TokenType.ATOM_Q);

			final String word;
			final TokenType type;

			KeyWord(String word, TokenType type) {
				this.word = word;
				this.type = type;
			}

			private static final Map<String, TokenType> m = new HashMap<String, TokenType>();
			static {

				for (KeyWord k : KeyWord.values()) {
					m.put(k.word, k.type);
				}
			}
		}
	}

	public static class Token
	{
		public final TokenType type;
		public final String lexme;

		Token(TokenType type, String lexme) {
			this.type = type;
			this.lexme = lexme;
		}

		@Override
		public String toString() {
			return String.format("[%s: %s]", type.toString(), lexme);
		}
	}

	private int transM[][];
	private boolean accept[];
	private String source;
	private int pos;

	public Scanner(String source) 
	{
		this.accept = new boolean[] { false, true, true, true, true, true,
				true, true, true, true, true, true, true, true, true, true,
				true, true, true, true, true, true, true, true, true, false };// 0과 #은 다음문자가 무조건 나와야 하기 때문에 false. 
		
		this.transM = new int[27][128];
		this.source = source == null ? "" : source;
		init_TM();
	}

	private void init_TM() {
		// 전부 -1로 초기화.
		for (int i = 0; i < 27; i++)
			for (int j = 0; j < 128; j++)
				transM[i][j] = -1;
		
		//mDFA에 맞게 초기화 해주는 과정
		for (int j = '0'; j <= '9'; j++) { 
			transM[0][j] = 1;
			transM[1][j] = 1;
			transM[2][j] = 2;
			transM[3][j] = 1;
			transM[4][j] = 1;
		}

		for (int k = 'a'; k <= 'z'; k++) {
			transM[0][k] = 2;
			transM[2][k] = 2;
		}

		for (int k = 'A'; k <= 'Z'; k++) {
			transM[0][k] = 2;
			transM[2][k] = 2;
		}
		
		transM[0]['-'] = 3;
		transM[0]['+'] = 4;
		transM[0]['('] = 5;
		transM[0][')'] = 6;
		transM[0]['*'] = 9;
		transM[0]['/'] = 10;
		transM[0]['>'] = 12;
		transM[0]['<'] = 11;
		transM[0]['='] = 13;
		transM[0]['\''] = 14;
		transM[0]['#'] = 26;

		transM[2]['?'] = 2;

		transM[26]['T'] = 7;
		transM[26]['F'] = 8;

	}

	private Token nextToken() {
		char c = Character.SPACE_SEPARATOR;
		StringBuffer sb = new StringBuffer();
		Token result = null;
		int StateOld = 0, StateNew;
		boolean acceptState = false;

		while (pos < source.length()) {
			if (!Character.isWhitespace(c = source.charAt(pos)))
				break;
			pos++;
		}// 의미 없는 공백문자 무시

		if (pos >= source.length())
			return null; // input data가 더 이상 없을 경우
		while (!acceptState) {
			StateNew = transM[StateOld][c]; // 입력문자로 새로운 상태 판별
			if (StateNew == -1) // 입력된 문자의 상태(StateNew)가 reject일때
								// 이전문자(StateOld)의 상태로 reject, accept판별
			{

				if (accept[StateOld]) {
					acceptState = true;
					break;
				} // accept
				else {
					acceptState = false;
					break;
				} // reject
			}

			sb.append(c);
			StateOld = StateNew;
			pos++;
			if (pos < source.length())
				c = source.charAt(pos);
			else
				c = 0; // Null Character
		}
		if (acceptState) {
			for (TokenType t : TokenType.values()) {
				if (t.finalState == StateOld){ 
					String lexeme = sb.toString();
					TokenType keyWord = TokenType.keyWordCheck(lexeme);

					if (keyWord != null) { // lexeme이 keyword일 경우 keyword값이 null이 아니기 때문에 keyword값으로  초기화.
						result = new Token(keyWord, lexeme);
					}

					else { // keyword가 아닐경우 원래상태 그대로 초기화.
						result = new Token(t, lexeme);
					}

					break;
				}
			}
		}

		else
			System.out.println(String.format("acceptState error %s\n",
					sb.toString()));

		return result;
	}

	public List<Token> tokenize() {
		List<Token> list = new ArrayList<Token>(); // token형 array객체 생성.
		Token temp = null;

		while ((temp = nextToken()) != null)
			// temp에 문자열중 문자하나의 문자형과 문자내용 저장.
			list.add(temp); // temp가 null이 아니면 문자가 있는것이므로 list에 추가.

		return list;
	}

	public static void main(String[] args) throws IOException {

		BufferedReader br = new BufferedReader(new FileReader("as03.txt")); 

		String source = br.readLine(); // string으로 받아옴.
		String temp = ""; // 한줄 한줄 읽기 때문에 임시로 저장할 변수 선언.
		
		do { //처음에 받아온 문자열을 temp에 저장하고 그이후 받아오면서 계속 temp뒤에 연결.
			temp = temp + source;
			source = br.readLine();
		} while (source != null);
		
		source = temp; // 문자열을 전부 받아온후 source에 다시 넣음.
		
		Scanner s = new Scanner(source);
		List<Token> tokens = s.tokenize();
		br.close();
		
		for(int i=0 ; i<tokens.size() ; i++) // 출력
			System.out.println(tokens.get(i));
	}
}
