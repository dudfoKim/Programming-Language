package cute14Parser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 과제번호 : 04 
 * 분반 : 02반
 * 소속 : 컴퓨터공학과
 * 학번 : 201002384
 * 이름 : 김범철
 */

public class Scanner {

	public enum TokenType { // 문자,숫자 에 대한 번호 지정.
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

	public Scanner(String source) {
		this.accept = new boolean[] { false, true, true, true, true, true,
				true, true, true, true, true, true, true, true, true, true,
				true, true, true, true, true, true, true, false };// 0과 #은 다음문자가
																	// 무조건 나와야
																	// 하기 때문에
																	// false.

		this.transM = new int[27][128];
		this.source = source == null ? "" : source;
		init_TM();
	}

	private void init_TM() {
		// 전부 -1로 초기화.
		for (int i = 0; i < 27; i++)
			for (int j = 0; j < 128; j++)
				transM[i][j] = -1;

		// mDFA에 맞게 초기화 해주는 과정
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
				if (t.finalState == StateOld) {
					String lexeme = sb.toString();
					TokenType keyWord = TokenType.keyWordCheck(lexeme);

					if (keyWord != null) { // lexeme이 keyword일 경우 keyword값이
											// null이 아니기 때문에 keyword값으로 초기화.
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

		while ((temp = nextToken()) != null)	// temp에 문자열중 문자하나의 문자형과 문자내용 저장.
			list.add(temp); // temp가 null이 아니면 문자가 있는것이므로 list에 추가.

		return list;
	}

	public class BasicParser {
		boolean alreadyParse;
		int pos;
		List<Token> tokenList;

		public BasicParser(List<Token> list) {
			this.alreadyParse = false;
			this.pos = 0;
			this.tokenList = list;
		}

		private Token getNextToken() {
			if (pos < tokenList.size())
				return tokenList.get(pos++);
			else
				return null;
		}

		private void ungetToken() {
			if (pos > 0)
				pos--;
		}

		private void errorLog(String err) {
			System.out.println(err);
		}

		// list의 한 원소만을 파싱함
		private Node parseItem() {
			Node item = null;
			Token t = getNextToken();

			if (t != null) { // 각 input별로  자료형을 확인해서 넣어줌.
				switch (t.type) {
				case ID:
					item = new IdNode(Node.Type.NOT_QUOTED, t.lexme);
					break;
				case INT:
					item = new IntNode(Node.Type.NOT_QUOTED, new Integer(t.lexme));
					break;
				case MINUS:
					item = new BinarayOpNode(Node.Type.NOT_QUOTED, BinarayOpNode.BinType.MINUS);
					break;
				case PLUS:
					item = new BinarayOpNode(Node.Type.NOT_QUOTED, BinarayOpNode.BinType.PLUS);
					break;
				case TIMES:
					item = new BinarayOpNode(Node.Type.NOT_QUOTED, BinarayOpNode.BinType.TIMES);
					break;
				case DIV:
					item = new BinarayOpNode(Node.Type.NOT_QUOTED, BinarayOpNode.BinType.DIV);
					break;
				case LT:
					item = new BinarayOpNode(Node.Type.NOT_QUOTED, BinarayOpNode.BinType.LT);
					break;
				case GT:
					item = new BinarayOpNode(Node.Type.NOT_QUOTED, BinarayOpNode.BinType.GT);
					break;
				case EQ:
					item = new BinarayOpNode(Node.Type.NOT_QUOTED, BinarayOpNode.BinType.EQ);
					break;
				case DEFINE:
					item = new FunctionNode(Node.Type.NOT_QUOTED, FunctionNode.FunctionType.DEFINE);
					break;
				case LAMBDA:
					item = new FunctionNode(Node.Type.NOT_QUOTED, FunctionNode.FunctionType.LAMBDA);
					break;
				case COND:
					item = new FunctionNode(Node.Type.NOT_QUOTED, FunctionNode.FunctionType.COND);
					break;
				case NOT:
					item = new FunctionNode(Node.Type.NOT_QUOTED, FunctionNode.FunctionType.NOT);
					break;
				case CDR:
					item = new FunctionNode(Node.Type.NOT_QUOTED, FunctionNode.FunctionType.CDR);
					break;
				case CAR:
					item = new FunctionNode(Node.Type.NOT_QUOTED, FunctionNode.FunctionType.CAR);
					break;
				case CONS:
					item = new FunctionNode(Node.Type.NOT_QUOTED, FunctionNode.FunctionType.CONS);
					break;
				case EQ_Q:
					item = new FunctionNode(Node.Type.NOT_QUOTED, FunctionNode.FunctionType.EQ_Q);
					break;
				case NULL_Q:
					item = new FunctionNode(Node.Type.NOT_QUOTED, FunctionNode.FunctionType.NULL_Q);
					break;
				case ATOM_Q:
					item = new FunctionNode(Node.Type.NOT_QUOTED, FunctionNode.FunctionType.ATOM_Q);
					break;
				// 이번 과제에서는 ‘나 QUOTE 기호가 없다고 가정하기 때문에 모든 노드가 Type.NOT_QUOTED이다
				// ...
				// ...
				// ...
				case L_PAREN: // 중첩 list임
					ungetToken();
					item = parseList();
					break;
				default:
					errorLog("parseItem Error");
					break;
				}
			}
			return item;
		}

		// list 의 괄호를 뺀 내부를 parsing 함
		private Node parseItemList() {
			Node list = null;// 여기에 list를 구성해서 결과를 리턴 예정
			Token t = getNextToken();
			// keyward일 경우들 검사.
			if (t != null) {
				switch (t.type) {
				case ID:
				case INT:
				case MINUS:
				case PLUS:
				case TIMES:
				case DIV:
				case LT:
				case GT:
				case EQ:
				case DEFINE:
				case LAMBDA:
				case COND:
				case NOT:
				case CDR:
				case CAR:
				case CONS:
				case EQ_Q:
				case NULL_Q:
				case ATOM_Q:
				case L_PAREN:
					// 즉, 오른쪽 괄호가 아닌 모든 token에 대해
					ungetToken();
					list = parseItem(); // 한 원소를 parsing하여 head 를 만듬
					break;
				default: // 오른쪽 괄호거나 오류. List 끝. 다시 넣어두고 리턴함.
					ungetToken();
					break;
				}
			}

			if (list != null)
				list.setNext(parseItemList()); // recursion을 써서 tail을 만들어 head의
												// next에 이어 붙임
			return list;
		}

		public Node parseList() {
			Node list = null; // 이곳에 리스트를 구성하여 리턴 예정
			Token t = getNextToken();
			if (t != null && t.type == TokenType.L_PAREN) {
				Node itemList = parseItemList();
				t = getNextToken();

				if (t == null || t.type != TokenType.R_PAREN) // 오른쪽 괄호가 아니면 에러
					errorLog("parseList Error rparen");
				else
					list = new ListNode(Node.Type.NOT_QUOTED, itemList);
			} else {
				errorLog("parseList Error lparen");// 왼쪽 괄호가 아니면 에러
			}

			return list;
		}
	}

	public class Printer
	{
		PrintStream ps;

		public Printer(PrintStream ps)
		{
			this.ps = ps;
		}			
		
		public void NodePrint(Node node)
		{	
			while(node!=null) // 재귀를 돌기 때문에 null일경우 멈추게됨. 
			{			
				if(node.getClass()==ListNode.class) // 만약 node가 listnode일경우 다시 list가 있기  때문에 다시 들어감.  
				{
					System.out.print("("); // (의 경우 리스트의 맨앞에 오기때문에 재귀적으로 들어가기전에 프린트.
					NodePrint(((ListNode)node).value);
					System.out.print(")"); //재귀적으로 들어가 마지막일경우 인제 올라오면서 출력되야 되기 때문에 재귀문 뒤에 써줌.				
				}
				// list노드가 아닐경우  value값이 있기 때문에 출력.
				else 
					System.out.print(" ["+node+"] ");		
					
				node=node.getNext(); // listnode를 만나기 전까지 계속 리스트를 따라감.
			}			
		}
	}

	public static void main(String[] args) throws IOException
	{
		BufferedReader br = new BufferedReader(new FileReader("as04.txt"));
		String source = br.readLine();// string으로 받아옴.
		String temp = ""; // 한줄 한줄 읽기 때문에 임시로 저장할 변수 선언.
			
		do { // 처음에 받아온 문자열을 temp에 저장하고 그이후 받아오면서 계속 temp뒤에 연결.
			temp = temp + source;
			source = br.readLine();
		} while (source != null);
		
		source = temp; // 문자열을 전부 받아온후 source에 다시 넣음.
		br.close();
		
		Scanner s = new Scanner(source);
		List<Token> tokens = s.tokenize();
		
		BasicParser p = s.new BasicParser(tokens); // 파일에서 받아온 tokens을  파싱하기위해 넘겨줌.
		Node node = p.parseList(); //파싱된 노드의 root를 가져옴.
		
		Printer pt = s.new Printer(System.out);	
		
		pt.NodePrint(node); //pre-order방식으로 print.
	}
}
