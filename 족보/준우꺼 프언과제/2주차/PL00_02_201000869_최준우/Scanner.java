package hw02_Reconizing_Token_u00;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Scanner {

	private int transM[][]; 
	private boolean accept[];
	private String source; 
	private int pos; 

	public Scanner(String source) { 
		this.accept = new boolean[]{ false, false, true, 
				true };// Final states: 2,3 
		this.transM = new int[4][128]; 
		this.source = source == null ? "" : source; 
		init_TM(); 
	} 

	/**
	 * Transition Matrix를 초기화 한다.
	 * 
	 * state 0 : 초기 state
	 * state 1 : 음수 입력 대기 상태
	 * state 2 : integer 대기 상태
	 * state 3 : id 대기 상태
	 * 
	 * 초기 state 0 에서 시작하여
	 * 숫자가 들어 올 때 2번 state로 
	 * 알파벳가 들어 올 때 3번 state로
	 * -가 들어 오면 1번 state로 state 전환한다.
	 * 
	 * state 1에서 숫자가 들어오면 2번 state로
	 * 그 외 글자가 들어오면 -1 state로 전환한다.
	 * 
	 * state 2에서 숫자가 들어오면 다시 2번 state로
	 * 그 외 글자가 들어오면 -1 state로 전환한다.
	 * 
	 * state 3에서 숫자나 알파벳이 들어오면 다시 3번 state로
	 * 그 외 글자가 들어오면 -1 state로 전환한다.
	 */
	private void init_TM() { 

		for(int i=0; i<4; i++)
		{
			if(i==0)
			{
				for(int j=0; j<128; j++)
				{
					if('0'<=j && j<='9')
						transM[i][j]=2;
					else if(('A'<=j && j<='Z')||('a'<=j && j<='z'))
						transM[i][j]=3;
					else if(j=='-')
						transM[i][j]=1;
					else
						transM[i][j]=-1;
				}
			}

			else if(i==1)
			{
				for(int j=0; j<128; j++)
				{
					if('0'<=j && j<='9')
						transM[i][j]=2;
					else
						transM[i][j]=-1;
				}
			}
			else if(i==2)
			{
				for(int j=0; j<128; j++)
				{
					if('0'<=j && j<='9')
						transM[i][j]=2;
					else
						transM[i][j]=-1;
				}
			}
			else if(i==3)
			{
				for(int j=0; j<128; j++)
				{
					if('0'<=j && j<='9')
						transM[i][j]=3;
					else if(('A'<=j && j<='Z')||('a'<=j && j<='z'))
						transM[i][j]=3;
					else
						transM[i][j]=-1;
				}
			}
		}

	} 
	/**
	 * as02.txt의 string 나열을 tokenize한다.
	 * string간의 불필요한 whitespace를 지우고 space를 구분자로 하여
	 * 글자 마다의 state를 transition한다.
	 * space를 만나면 여태까지 transition한 상태의 token을 반환하며
	 * as02.txt의 모든 string을 읽고 나면 null을 리턴한다.
	 * 만약 정의 하지 않은 글자가 들어오면 -1 state로 들어가 
	 * acceptState error를 발생하고 tokenize를 종료한다.
	 * @return result - tokenize하여 얻은 token을 반환한다.
	 */
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
					acceptState = true; 
					break; 
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
		{
			sb.append(c);
			System.out.println(String.format("acceptState error by \"%s\", position : %d\n", sb.toString(), this.pos));//  4
		}
		return result; 
	} 

	/**
	 * Token형 객체를 저장하는 ArrayList 선언하여
	 * nextToken()이 null일 때 까지 list에 Token을 저장한다.
	 * @return list - as02.txt의 Token List
	 */
	public List<Token> tokenize() { 
		//Token 리스트반환, nextToken()이용.. 

		List<Token> list = new ArrayList<Token>();

		Token temp = this.nextToken();

		while(temp != null)
		{
			list.add(temp);
			temp = this.nextToken();
		}
		return list;
	} 

	/**
	 * as02.txt 파일을 filereader와 bufferedReader를 통하여
	 * as02.txt의 text를 source에 저장하여 Scanner 객체를 통해
	 * source를 tokenize한다. 그 뒤 list에 쌓인 token들을 출력한다.
	 * @param args
	 */
	@SuppressWarnings("resource")
	public static void main(String[] args){ 
		//txt file to String
		try
		{
			File file = new File("./as02.txt");
			BufferedReader buf = new BufferedReader(new FileReader(file));

			String source = buf.readLine();
			Scanner s = new Scanner(source); 
			List<Token> tokens = s.tokenize(); 
			//print 
			System.out.println(tokens.toString());
		}
		catch(FileNotFoundException e)
		{
			System.out.println("No File, AbNormal Terminate");
			e.printStackTrace();
		}
		catch(IOException e)
		{
			System.out.println("IOException, AbNormal Terminate");
			e.printStackTrace();
		}



	} 

	/**
	 * Token은 해당 token의 type과 string 값인 lexme을 갖고 있으며
	 * source에서 의미를 갖는 가장 작은 단위로 쪼개진다.
	 * @author choijunwoo
	 */

	public static class Token { 
		public final TokenType type; 
		public final String lexme; 

		Token(TokenType type, String lexme) 
		{ 
			this.type = type; 
			this.lexme = lexme; 
		} 

		@Override 
		public String toString() 
		{ 
			return String.format("[%s: %s]", type.toString(), lexme); 
		} 
	} 

	/**
	 * token이 갖는 finalstate는 ID와 INT로 정의한다.
	 * @author choijunwoo
	 *
	 */
	public enum TokenType{ 
		ID(3), INT(2); 

		private final int finalState; 

		TokenType(int finalState) 
		{ 
			this.finalState = finalState; 
		} 
	}

}
