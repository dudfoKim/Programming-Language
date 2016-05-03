import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CuteScanner {
	public enum TokenType{
		ID(3), INT(2);
		
		private final int finalState;
		
		private TokenType(int finalState) {
			this.finalState = finalState;
		}
	}
	
	private int transM[][];
	private boolean accept[];
	private String source;
	private int pos;
	
	public CuteScanner(String source) {
		this.accept = new boolean[]{ false, false, true, true };
		this.transM = new int[4][128];
		this.source = source == null ? "" : source;
		init_TM();
	}
	
	/*
	 * 새로 구현해야 하는 부분
	 */
	private void init_TM() {
		for(int i=0;i<4;i++)
			for(int j=0;j<128;j++)
				transM[i][j] = -1;
		
		transM[0]['-'] = 1; // '-'

		for (int i = '0'; i <= '9'; i++) // digit
		{
			transM[0][i] = 2;
			transM[1][i] = 2; // '-'
			transM[2][i] = 2; // digit
			transM[3][i] = 3; // id
		}

		for (int i = 'a'; i <= 'z'; i++) // alpha
		{
			transM[0][i] = 3;
			transM[3][i] = 3;
		}
		for (int i = 'A'; i < 'Z'; i++) {
			transM[0][i] = 3;
			transM[3][i] = 3;
		}
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
		}
		
		if( pos >= source.length() )
			return null;
		
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
					result = new Token(t, sb.toString());
					break;
				}
			}
		}else
			System.out.println(String.format("acceptState error %s\n", sb.toString()));
		
		return result;
	}
	
	/*
	 * 구현해야 하는 부분
	 */
	public List<Token> tokenize() {
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
	
	/*
	 * 새로 구현해야 하는 부분 
	 */
	public static void main(String[] args) throws IOException{
		
		byte[] bytes = Files.readAllBytes(Paths.get("./as02.txt"));
		String source = new String(bytes);

		CuteScanner s = new CuteScanner(source);
		System.out.println(s.tokenize());
	}
	
}
