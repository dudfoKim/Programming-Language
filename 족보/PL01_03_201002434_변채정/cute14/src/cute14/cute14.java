/* 
 * 과제 번호 : 03
 * 분반 표기 : 01반
 * 소속, 학번, 성명 : 컴퓨터공학과, 201002434, 변채정
*/

package cute14;



import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class cute14 {
	
	public enum TokenType{
	//int=2, id=1, special=3~15, keyword=16~26
		INT(2), ID(1), MINUS(3), PLUS(4),
		L_PAREN(5), R_PAREN(6), TRUE(7), FALSE(8),
		TIMES(9), DIV(10), LT(11), GT(12), EQ(13),
		APOSTROPHE(14),Sharp(15),
		DEFINE(16), LAMBDA(17), COND(18), QUOTE(19),
		NOT(20), CAR(21), CDR(22), CONS(23),EQ_Q(26),
		ATOM_Q(24), NULL_Q(25);
		
		private final int finalState;
		
		TokenType(int finalState) {
			this.finalState = finalState;
		}
		
		public static TokenType keyWordCheck(String str){
			return KeyWord.m.get(str); //if not keyword return null
		}
		
		private enum KeyWord{//keyword정의
			DEFINE("define", TokenType.DEFINE),
			LAMBDA("lambda", TokenType.LAMBDA),
			COND("cond",TokenType.COND),
			QUOTE("quote",TokenType.QUOTE),
			NOT("not",TokenType.NOT),
			CDR("cdr",TokenType.CDR),
			CAR("car",TokenType.CAR),
			CONS("cons",TokenType.CONS),
			EQ_Q("eq?",TokenType.EQ_Q),
			NULL_Q("null?",TokenType.NULL_Q),
			ATOM_Q("atom?",TokenType.ATOM_Q);
			
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
	
	private int transM[][];
	private boolean accept[];
	private String source;
	private int pos;
	
	public cute14(String source) {
		this.accept = new boolean[]{false,true,true,true,true,true,true,true,true,true,true,true,true,true,true,false};// Final states
		//0과 '#'일때만 최종상태가 될 수 없고 나머지일때는 최종상태가 가능
		this.transM = new int[27][128];
		this.source = source == null ? "" : source;
		init_TM();
	}
	
	private void init_TM() {
		int i,j;
		for(i = 0 ; i <27 ; i++){//transM초기화
			for(j = 0; j<128 ; j++){
				transM[i][j] = -1;
			}
		}
		for(i='0' ; i<='9' ; i++) {//숫자 들어오면 상태변경
			transM[0][i] = 2;
			transM[1][i] = 1;//id다음 숫자는 id
			transM[2][i] = 2;
			transM[3][i] = 2;//'+'다음의 숫자가 들어올때
			transM[4][i] = 2;//'-'다음의 숫자가 들어올때
		} 


		for(i='A';i<='Z';i++){//대문자가 들어오면 1(id)로
			transM[0][i]=1;
			transM[1][i]=1;
			
		}
		for(i='a';i<='z';i++){//소문자가 들어오면 1(id)로
			transM[0][i]=1;
			transM[1][i]=1;
		}
		//special 문자 값 정의
		transM[1]['?'] = 1;
		transM[0]['+']=4;
		transM[0]['-']=3;
		transM[0]['(']=5;
		transM[0][')']=6;
		transM[0]['*']=9;
		transM[0]['/']=10;
		transM[0]['<']=11;
		transM[0]['=']=13;
		transM[0]['>']=12;
		transM[0]['\'']=14;
		transM[0]['#'] = 15;
		transM[15]['T']=7;//'#'다음에 T가 올때
		transM[15]['F']=8;//'#'다음에 F가 올때
		
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
		List<Token> a = new ArrayList<Token>();
	
		while(true){
			Token list = nextToken();
			if(list!=null){//list가 끝이 아니면
				a.add(list); //list에 token을 추가한다
		}
			else
				break;
		}
		return a;//모든 token이 추가 된 list값을 반환 
	}

	public static void main(String[] args) throws IOException{
		byte[] bytes = Files.readAllBytes(Paths.get("./as03.txt"));
		String source = new String(bytes);

		cute14 s = new cute14(source);
		List<Token> tokens = s.tokenize();//tokenize한 값을 불러옴
		
		for(int i = 0 ; i < tokens.size() ; i++){
			System.out.println(tokens.get(i));
		}
		
		
			
	}
}