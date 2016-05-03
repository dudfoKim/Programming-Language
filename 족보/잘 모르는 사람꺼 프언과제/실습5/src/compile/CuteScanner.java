package compile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CuteScanner {
	public enum TokenType{
		INT(1), ID(4),
		MINUS(2), PLUS(3),  
		L_PAREN(5), R_PAREN(6), 
		TRUE(8), FALSE(9), 
		TIMES(10), DIV(11), LT(12), GT(13),	EQ(14), 
		APOSTROPHE(15), QUESTION(16),//bug
		
		//not present in automata
		DEFINE(-1),	LAMBDA(-1),	COND(-1), QUOTE(-1),
		NOT(-1), CAR(-1), CDR(-1), CONS(-1),
		
		ATOM_Q(-1), NULL_Q(-1), EQ_Q(-1);

		private final int finalState;
		
		TokenType(int finalState) {
			this.finalState = finalState;
		}
		
		public static TokenType keyWordCheck(String str){
			return KeyWord.m.get(str); //if not keyword return null
		}
		
		private enum KeyWord{
			DEFINE("define", TokenType.DEFINE), 
			LAMBDA("lambda", TokenType.LAMBDA), 
			COND("cond", TokenType.COND), 
			QUOTE("quote", TokenType.QUOTE),
			NOT("not", TokenType.NOT), 
			CAR("car", TokenType.CAR), 
			CDR("cdr", TokenType.CDR),
			CONS("cons", TokenType.CONS),
			ATOM_Q("atom?", TokenType.ATOM_Q), 
			NULL_Q("null?", TokenType.NULL_Q), 
			EQ_Q("eq?", TokenType.EQ_Q);
			
			final String word;
			final TokenType type;
			
			KeyWord(String word, TokenType type){
				this.word = word;
				this.type = type;
			}
			
			private static final Map<String, TokenType> m = new HashMap<String, TokenType>();
			static{
				for(KeyWord k : KeyWord.values()){
					m.put(k.word, k.type);
				}
			}
		}
	}
	
	private int transM[][];
	private boolean accept[];
	private String source;
	private int pos;

	
	public CuteScanner(String source) {
		this.transM = new int[17][128];
		this.accept = new boolean[transM.length];
		for(TokenType t : TokenType.values()){
			if(t.finalState > 0)
				accept[t.finalState] = true;
		}
		this.source = source == null ? "" : source;
		init_TM();
	}
	
	private void init_TM() {
		for(int i=0; i<transM.length; i++)
			for(int j=0; j< transM[i].length; j++)
				transM[i][j] = -1;
		
		int i;
		for(i='0';i<'9';i++) // digit
		{
			transM[0][i] = 1;
			transM[1][i] = 1; //digit
			transM[2][i] = 1; //'-'
			transM[3][i] = 1; //'+'
			transM[4][i] = 4; //id
		}

		for(i='a';i<'z';i++) //alpha
		{
			transM[0][i] = 4;
			transM[4][i] = 4;
		}
		for(i='A';i<'Z';i++)
		{
			transM[0][i] = 4;
			transM[4][i] = 4;
		}

		transM[4]['?'] = 16; //null?, atom?...

		transM[0]['-'] = 2; // '-'
		transM[0]['+'] = 3; // '+'
		transM[0]['('] = 5; // '('
		transM[0][')'] = 6; // ')'
		transM[0]['#'] = 7; //#
		transM[7]['T'] = 8; //#T
		transM[7]['F'] = 9; //#F
		transM[0]['*'] = 10; //*
		transM[0]['/'] = 11; ///
		transM[0]['<'] = 12; //<
		transM[0]['>'] = 13; //>
		transM[0]['='] = 14; //=
		transM[0]['\''] = 15; //'
	}
	
	private Token nextToken(){		
		int StateOld = 0, StateNew;
		boolean acceptState = false;
		char c = Character.SPACE_SEPARATOR;
		StringBuffer sb = new StringBuffer();
		Token result = null;

		while( pos < source.length() ){
			if(!Character.isWhitespace( c = source.charAt(pos)))
				break;
			pos++;
		}//의미 없는 공백문자 무시
		
		if( pos >= source.length() )
			return null; //input data가 더 이상 없을 경우
		
		while (!acceptState) {
			StateNew = transM[StateOld][c];	//입력문자로 새로운 상태 판별
			
			if (StateNew == -1) {	// 입력된 문자의 상태(StateNew)가 reject일때 이전문자(StateOld)의 상태로 reject, accept판별
				if (accept[StateOld]) { 
					acceptState = true;	break; 
				} // accept
				else { acceptState = false; break; } // reject
			}

			sb.append(c);
			StateOld = StateNew;

			pos++;
			if(pos < source.length())
				c = source.charAt(pos);
			else
				c = 0; // Null Character
		}
		
		if(acceptState){
			for (TokenType t : TokenType.values()){
				if(t.finalState == StateOld){
					String lexeme = sb.toString();
					TokenType keyWord = TokenType.keyWordCheck(lexeme);
					if(keyWord != null){
						result = new Token(keyWord, lexeme);
					}else{
						result = new Token(t, lexeme);
					}
					break;
				}
			}
		}else
			System.out.println(String.format("acceptState error %s\n", sb.toString()));
		
		return result;
	}
	
	public List<Token> tokenize() {
		//Token 리스트반환
		List<Token> tokens = new ArrayList<Token>();
		Token t = null;
		
		while(true){
			t = nextToken();
			
			if(t == null)
				break;
			
			tokens.add(t);
		}
		
		return tokens;
	}
	
	
	public static class Token {
		public final TokenType type;
		public final String lexeme;

		Token(TokenType type, String lexeme) {
			this.type = type;
			this.lexeme = lexeme;
		}

		@Override
		public String toString() {
			return String.format("[%s: %s]", type.toString(), lexeme);
		}
	}	
}
