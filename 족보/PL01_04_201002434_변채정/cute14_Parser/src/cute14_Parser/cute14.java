package cute14_Parser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;



public class cute14 {

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
		for(int i=0;i<16;i++){ 
			for(int j=0;j<128;j++)
				transM[i][j] = -1;
		}

		/* 초기 상태에서 Special Symbols에 따른 다음 상태값 선언 */
		transM[0]['-'] = 4;
		transM[0]['+'] = 5;
		transM[0]['\''] = 6;
		transM[0]['='] = 7;
		transM[0]['('] = 8;
		transM[0][')'] = 9;
		transM[0]['*'] = 10;
		transM[0]['/'] = 11;
		transM[0]['<'] = 12;
		transM[0]['>'] = 13;

		for(int i=0;i<16;i++){
			for(int j=0;j<128;j++){
				if(i == 0){//초기 State
					if (j == '#') //입력 값이 # 인 경우
						transM[i][j] = 1;

					else if( (j > 47) && (j < 58) )	//입력 값이 숫자(0~9)인 경우
						transM[i][j] = 2; //digit 구분

					//입력 값이 문자인 경우.(a~z && A~Z)
					else if ( ((j > 64)&&(j < 91)) || ((96 < j)&&(j < 123)) ) 
						transM[i][j] = 3; //a~z,A~Z 구분.
				}

				if(i == 1){ //#이 입력된 현재 State
					if(j == 'T')//입력 값이 T 인경우
						transM[i][j] = 14;
					else if(j == 'F') //입력 값이 F 인 경우
						transM[i][j] = 15;
				}

				if(i == 2){//현재 상태가 INT 인 경우
					if( (j > 47) && (j < 58) ) //digit
						transM[i][j] = i;
				}
				if(i == 3){//현재 상태가 ID인 경우
					if ( ((j > 64)&&(j < 91)) || ((96 < j)&&(j < 123)) || j == '?') 
						transM[i][j] = i; 
					else if( (j > 47) && (j < 58) ) //digit
						transM[i][j] = i;
				}
				if(i == 4){// 현재 상태가 '-' 인 경우
					if(  (j > 47) && (j < 58) ){  //숫자가 입력으로 들어올 경우
						transM[i][j] = 2; //다음 State는 INT가 된다.
					}
				}
				if(i == 5){// 현재 상태가 '+' 인 경우
					if((j > 47) && (j < 58)){ //숫자가 입력으로 들어올 경우
						transM[i][j] = 2; //다음 State는 INT가 된다.
					}
				}
			}
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
				String lexeme = sb.toString();
				if(t.finalState == StateOld){
					TokenType keyWord = TokenType.keyWordCheck(lexeme);
					if(keyWord != null){// Token Type이 KeyWord일 경우 
						result = new Token(keyWord, lexeme);//KeyWord 반환
					}
					else{
						result = new Token(t, lexeme);
					}
					break;
				}
			}
		}
		else
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
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		//read file… 
		byte[] bytes = Files.readAllBytes(Paths.get("./as04.txt"));
		String source = new String(bytes);

		cute14 s = new cute14(source);
		List<Token> tokens = s.tokenize();
		BasicParser p = new BasicParser(tokens); 
		Node node = p.parseList(); 

		/*for(int i = 0 ; i < tokens.size() ; i++){
			System.out.println(tokens.get(i));
		}*/
		Printer pt = new Printer(System.out); 
		pt.printList(((ListNode)node).value);

	}
}