/*
 * 과제 번호 : hw09 
 * 분반 : 00분반
 * 소속 : 컴퓨터공학과
 * 학번 : 201000869
 * 이름 : 최준우
 */

package hw10_Reconizing_Token_u00;

import java.util.*;

public class Scanner {

	private int transM[][]; 
	private boolean accept[];
	private String source; 
	private int pos; 

	public Scanner(String source) { 
		this.accept = new boolean[]{ false, true, 
				true, true, true, true, true, true, true, true, true, true, false, true, true, 
				true, true, true, true, true, true, true, true, true, true, true, true, true,};
		// Final states: 1,2,3,4,5,6,7,8,9,10,11,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27 
		this.transM = new int[28][128]; 
		this.source = source == null ? "" : source; 
		init_TM(); 
	} 

	/**
	 * Transition Matrix를 초기화 한다.
	 * 
	 * state 0 : 초기 state
	 * state 1 : integer 대기 상태
	 * state 2 : plus state
	 * state 3 : minus state
	 * state 4 : "(" state
	 * state 5 : ")" state 
	 * state 6 : "*" state 
	 * state 7 : "/" state 
	 * state 8 : "<" state 
	 * state 9 : ">" state 
	 * state 10 : "=" state 
	 * state 11 : "\'" state
	 * state 12 : "#" state
	 * 
	 * KeyWord States
	 * state 13 : TRUE state 
	 * state 14 : FALSE state
	 * state 15 : ID state
	 * state 17 : LAMBDA state
	 * state 18 : QUOTE state
	 * state 19 : DEFINE state
	 * state 20 : NOT state
	 * state 21 : COND state
	 * state 22 : CAR state
	 * state 23 : CDR state
	 * state 24 : CONS state 
	 * state 25 : ATOM_Q state 
	 * state 26 : NULL_Q state
	 * state 27 : EQ_Q state
	 * 
	 */
	private void init_TM() { 

		for(int i=0; i<28; i++)
		{
			if(i==0)
			{
				for(int j=0; j<128; j++)
				{
					if('0'<=j && j<='9')
						transM[i][j]=1;
					else if(('A'<=j && j<='Z')||('a'<=j && j<='z'))
						transM[i][j]=15;
					else if(j=='+')
						transM[i][j]=2;
					else if(j=='-')
						transM[i][j]=3;
					else if(j=='(')
						transM[i][j]=4;
					else if(j==')')
						transM[i][j]=5;
					else if(j=='*')
						transM[i][j]=6;
					else if(j=='/')
						transM[i][j]=7;
					else if(j=='<')
						transM[i][j]=8;
					else if(j=='>')
						transM[i][j]=9;
					else if(j=='=')
						transM[i][j]=10;
					else if(j=='\'')
						transM[i][j]=11;
					else if(j=='#')
						transM[i][j]=12;
					else
						transM[i][j]=-1;
				}
			}

			else if(i==1)
			{
				for(int j=0; j<128; j++)
				{
					if('0'<=j && j<='9')
						transM[i][j]=1;
					else
						transM[i][j]=-1;
				}
			}
			else if(i==2)
			{
				for(int j=0; j<128; j++)
				{
					if('0'<=j && j<='9')
						transM[i][j]=1;
					else
						transM[i][j]=-1;
				}
			}
			else if(i==3)
			{
				for(int j=0; j<128; j++)
				{
					if('0'<=j && j<='9')
						transM[i][j]=1;
					else
						transM[i][j]=-1;
				}
			}
			else if(i==4)
			{
				for(int j=0; j<128; j++)
				{
					transM[i][j]=-1;
				}
			}
			else if(i==5)
			{
				for(int j=0; j<128; j++)
				{
					transM[i][j]=-1;
				}
			}
			else if(i==6)
			{
				for(int j=0; j<128; j++)
				{
					transM[i][j]=-1;
				}
			}
			else if(i==7)
			{
				for(int j=0; j<128; j++)
				{
					transM[i][j]=-1;
				}
			}
			else if(i==8)
			{
				for(int j=0; j<128; j++)
				{
					transM[i][j]=-1;
				}
			}
			else if(i==9)
			{
				for(int j=0; j<128; j++)
				{
					transM[i][j]=-1;
				}
			}
			else if(i==10)
			{
				for(int j=0; j<128; j++)
				{
					transM[i][j]=-1;
				}
			}
			else if(i==11)
			{
				for(int j=0; j<128; j++)
				{
					transM[i][j]=-1;
				}
			}
			else if(i==12)
			{
				for(int j=0; j<128; j++)
				{
					if(j=='T')
						transM[i][j]=13;
					else if(j=='F')
						transM[i][j]=14;
					else
						transM[i][j]=-1;
				}
			}
			else if(i==13)
			{
				for(int j=0; j<128; j++)
				{
					transM[i][j]=-1;
				}
			}
			else if(i==14)
			{
				for(int j=0; j<128; j++)
				{
					transM[i][j]=-1;
				}
			}
			else if(i==15)
			{
				for(int j=0; j<128; j++)
				{
					if('0'<=j && j<='9')
						transM[i][j]=15;
					else if(('A'<=j && j<='Z')||('a'<=j && j<='z'))
						transM[i][j]=15;
					else if(j=='?')
						transM[i][j]=15;
					else
						transM[i][j]=-1;
				}
			}
			else if(i==17)
			{
				for(int j=0; j<128; j++)
				{
					if('0'<=j && j<='9')
						transM[i][j]=15;
					else if(('A'<=j && j<='Z')||('a'<=j && j<='z'))
						transM[i][j]=15;
					else
						transM[i][j]=-1;
				}
			}
			else if(i==18)
			{
				for(int j=0; j<128; j++)
				{
					if('0'<=j && j<='9')
						transM[i][j]=15;
					else if(('A'<=j && j<='Z')||('a'<=j && j<='z'))
						transM[i][j]=15;
					else
						transM[i][j]=-1;
				}
			}
			else if(i==19)
			{
				for(int j=0; j<128; j++)
				{
					if('0'<=j && j<='9')
						transM[i][j]=15;
					else if(('A'<=j && j<='Z')||('a'<=j && j<='z'))
						transM[i][j]=15;
					else
						transM[i][j]=-1;
				}
			}
			else if(i==20)
			{
				for(int j=0; j<128; j++)
				{
					if('0'<=j && j<='9')
						transM[i][j]=15;
					else if(('A'<=j && j<='Z')||('a'<=j && j<='z'))
						transM[i][j]=15;
					else
						transM[i][j]=-1;
				}
			}
			else if(i==21)
			{
				for(int j=0; j<128; j++)
				{
					if('0'<=j && j<='9')
						transM[i][j]=15;
					else if(('A'<=j && j<='Z')||('a'<=j && j<='z'))
						transM[i][j]=15;
					else
						transM[i][j]=-1;
				}
			}
			else if(i==22)
			{
				for(int j=0; j<128; j++)
				{
					if('0'<=j && j<='9')
						transM[i][j]=15;
					else if(('A'<=j && j<='Z')||('a'<=j && j<='z'))
						transM[i][j]=15;
					else
						transM[i][j]=-1;
				}
			}
			else if(i==23)
			{
				for(int j=0; j<128; j++)
				{
					if('0'<=j && j<='9')
						transM[i][j]=15;
					else if(('A'<=j && j<='Z')||('a'<=j && j<='z'))
						transM[i][j]=15;
					else
						transM[i][j]=-1;
				}
			}
			else if(i==24)
			{
				for(int j=0; j<128; j++)
				{
					if('0'<=j && j<='9')
						transM[i][j]=15;
					else if(('A'<=j && j<='Z')||('a'<=j && j<='z'))
						transM[i][j]=15;
					else
						transM[i][j]=-1;
				}
			}
			else if(i==25)
			{
				for(int j=0; j<128; j++)
				{
					if('0'<=j && j<='9')
						transM[i][j]=15;
					else if(('A'<=j && j<='Z')||('a'<=j && j<='z'))
						transM[i][j]=15;
					else
						transM[i][j]=-1;
				}
			}
			else if(i==26)
			{
				for(int j=0; j<128; j++)
				{
					if('0'<=j && j<='9')
						transM[i][j]=15;
					else if(('A'<=j && j<='Z')||('a'<=j && j<='z'))
						transM[i][j]=15;
					else
						transM[i][j]=-1;
				}
			}
			else if(i==27)
			{
				for(int j=0; j<128; j++)
				{
					if('0'<=j && j<='9')
						transM[i][j]=15;
					else if(('A'<=j && j<='Z')||('a'<=j && j<='z'))
						transM[i][j]=15;
					else
						transM[i][j]=-1;
				}
			}
		}

	} 
	/**
	 * console의 string 나열을 tokenize한다.
	 * string간의 불필요한 whitespace를 지우고 space를 구분자로 하여
	 * 글자 마다의 state를 transition한다.
	 * space를 만나면 여태까지 transition한 상태의 token을 반환하며
	 * 모든 string을 읽고 나면 null을 리턴한다.
	 * keyWordCheck(lexeme)를 하여 읽어 들인 
	 * 문자열이 keyword인지 확인한다.
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
					String lexeme = sb.toString();
					TokenType keyWord = TokenType.keyWordCheck(lexeme);
					if(keyWord != null)
						result = new Token(keyWord, lexeme);
					else
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
	 * @return list - as05.txt의 Token List
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
	 * as03.txt 파일을 filereader와 bufferedReader를 통하여
	 * as03.txt의 text를 source에 저장하여 Scanner 객체를 통해
	 * source를 tokenize한다. 그 뒤 list에 쌓인 token들을 출력한다.
	 * @param args
	 */

	

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
	 * token이 갖을 수 있는 final state의 type을 정의한다.
	 * inner enum class로 keyword를 선언하여 
	 * Hash Map에 word와 type을 대응하여 put한 뒤, 
	 * keyWordCheck(String str)을 통해 tokenize하여 얻은 lexeme를 key로 type을 get하여
	 * 해당 Keyword가 맞는지 확인한다. keyword가 아닐 경우는 null을 반환한다.
	 * @author choijunwoo
	 *
	 */
	public enum TokenType{ 
		ID(15), INT(1), MINUS(3), PLUS(2), L_PAREN(4), R_PAREN(5), TRUE(13), FALSE(14),
		TIMES(6), DIV(7), LT(8), GT(9), EQ(10), APOSTROPHE(11), DEFINE(19),
		LAMBDA(17), COND(21), QUOTE(18), NOT(20),CAR(22), CDR(23), CONS(24), ATOM_Q(25), NULL_Q(26),EQ_Q(27); 

		private final int finalState; 

		TokenType(int finalState) 
		{ 
			this.finalState = finalState; 
		} 

		public static TokenType keyWordCheck(String str)
		{
			return KeyWord.m.get(str);
		}

		private enum KeyWord
		{
			DEFINE("define", TokenType.DEFINE),
			LAMBDA("lambda", TokenType.LAMBDA),
			QUOTE("quote", TokenType.QUOTE),
			NOT("not", TokenType.NOT),
			COND("cond", TokenType.COND),
			CAR("car", TokenType.CAR),
			CDR("cdr", TokenType.CDR),
			CONS("cons", TokenType.CONS),
			ATOM_Q("atom?", TokenType.ATOM_Q),
			NULL_Q("null?", TokenType.NULL_Q),
			EQ_Q("eq?", TokenType.EQ_Q)
			;

			final String word;
			final TokenType type;

			KeyWord(String word, TokenType type)
			{
				this.word = word;
				this.type = type;
			}

			private static final Map<String, TokenType> m = new HashMap<String, TokenType>();
			static
			{
				for(KeyWord k : KeyWord.values())
				{
					m.put(k.word, k.type);
				}
			}
		}
	}

}
