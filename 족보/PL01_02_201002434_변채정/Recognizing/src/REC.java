/* 
 * 과제 번호 : 02
 * 분반 표기 : 01반
 * 소속, 학번, 성명 : 컴퓨터공학과, 201002434, 변채정
*/

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;



public class REC {

	public enum TokenType{
		ID(3), INT(2);//3이면 id, 2이면 int
		private final int finalState;
		TokenType(int finalState) {
			this.finalState = finalState;
		}
	}

	public static class Token {
		public final TokenType type;
		public final String lexme;//파일에서 읽은 값
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

	public REC (String source) {
		this.accept = new boolean[]{ false, false, true, true };// Final states: 2,3
		this.transM = new int[4][128];
		this.source = source == null ? "" : source;
		init_TM();
	}

	private void init_TM() {
		int i;
		for(i='0' ; i<='9' ; i++) {
			transM[0][i] = 2;
			transM[1][i] = 2;
			transM[2][i] = 2;
			transM[3][i] = 3;
		} //숫자가 들어오면 상태변경한다

		transM[0]['-'] = 1; // '-'이면 1로

		for(i='A';i<='Z';i++){
			transM[0][i]=3;
			transM[3][i]=3;
		}//대문자가 들어오면 3으로
		for(i='a';i<='z';i++){
			transM[0][i]=3;
			transM[3][i]=3;
		}//소문자가 들어오면 3으로 

		//공백시 끝낸다
		transM[2][' '] = -1; 
		transM[3][' '] = -1;
	}

	private Token nextToken(){
		char c = Character.SPACE_SEPARATOR;
		StringBuffer sb = new StringBuffer();
		Token result = null;
		int StateOld = 0, StateNew;
		boolean acceptState = false;
		
		while( pos < source.length() ){
			if(!Character.isWhitespace( c = source.charAt(pos)))
				break;
			pos++;
		}//의미 없는 공백문자 무시
		
		if( pos >= source.length() )
			return null; //input data가 더 이상 없을 경우
		while (!acceptState) {
			StateNew = transM[StateOld][c]; //입력문자로 새로운 상태 판별
			if (StateNew == -1) { // 입력된 문자의 상태(StateNew)가 reject일때 이전문자(StateOld)의 상태로 reject, accept판별
				if (accept[StateOld]) {
					acceptState = true; break;
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

	public static void main(String[] args){
		try{//파일 가져오기
		File inFile=new File("as02.txt");
		BufferedReader reader = new BufferedReader(new FileReader(inFile));
		String source=reader.readLine();//파일을 한줄씩 읽는다
		REC s = new REC(source);
		List<Token> tokens = s.tokenize();//tokenize한 값을 불러옴
		System.out.println(tokens.toString());//프린트해줌
	}
	catch(Exception ex){
	}
		
	}

}
