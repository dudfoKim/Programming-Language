/*
 * ������ȣ : PL08-2
 * �й� : 02�й�
 * �� ��ȣ : 15
 * ���� �Ҽ� : �泲���б� �������� ��ǻ�Ͱ��а�
 * �й� �� ���� : 201002384(���ö) 201002436(������)
 */

package cute14ProjectItem2;


import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * ������ȣ : 09
 * �й� : 02�� 
 * �Ҽ� : ��ǻ�Ͱ��а� 
 * �й� : 201002384 
 * �̸� : ���ö
 */

public class ScannerClass {

	public enum TokenType { // ����,���� �� ���� ��ȣ ����.
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

		private enum KeyWord { // Keyword�� ����.
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

	public ScannerClass(String source) {
		this.accept = new boolean[] { false, true, true, true, true, true,
				true, true, true, true, true, true, true, true, true, true,
				true, true, true, true, true, true, true, true, true, false };// 0��
																				// #��
																				// �������ڰ�
																				// ������
																				// ���;�
																				// �ϱ�
																				// ������
																				// false.

		this.transM = new int[27][128];
		this.source = source == null ? "" : source;
		init_TM();
	}

	private void init_TM() {
		// ���� -1�� �ʱ�ȭ.
		for (int i = 0; i < 27; i++)
			for (int j = 0; j < 128; j++)
				transM[i][j] = -1;

		// mDFA�� �°� �ʱ�ȭ ���ִ� ����
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
		}// �ǹ� ���� ���鹮�� ����

		if (pos >= source.length())
			return null; // input data�� �� �̻� ���� ���
		while (!acceptState) {
			StateNew = transM[StateOld][c]; // �Է¹��ڷ� ���ο� ���� �Ǻ�
			if (StateNew == -1) // �Էµ� ������ ����(StateNew)�� reject�϶�
								// ��������(StateOld)�� ���·� reject, accept�Ǻ�
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

					if (keyWord != null) { // lexeme�� keyword�� ��� keyword����
											// null�� �ƴϱ� ������ keyword������ �ʱ�ȭ.
						result = new Token(keyWord, lexeme);
					}

					else { // keyword�� �ƴҰ�� �������� �״�� �ʱ�ȭ.
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
		List<Token> list = new ArrayList<Token>(); // token�� array��ü ����.
		Token temp = null;

		while ((temp = nextToken()) != null)
			// temp�� ���ڿ��� �����ϳ��� �������� ���ڳ��� ����.
			list.add(temp); // temp�� null�� �ƴϸ� ���ڰ� �ִ°��̹Ƿ� list�� �߰�.

		return list;
	}

	public class Parser {

		boolean alreadyParse;
		int pos;
		List<Token> tokenList;

		public Parser(List<Token> list) {
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

		private Node parseExpr() // �������� expr�� id,int boolean,list�� ������ ����.
		{
			Node node = null;
			Token t = getNextToken();

			if (t != null) { // id,int,boolean�� �˻��ؼ� ��带 ����.
				switch (t.type) {
				case ID:
					node = new IdNode(Node.Type.NOT_QUOTED, t.lexme);
					break;
				case INT:
					node = new IntNode(Node.Type.NOT_QUOTED, new Integer(
							t.lexme));
					break;
				case TRUE:
					node = new BooleanNode(Node.Type.NOT_QUOTED, true);
					break;
				case FALSE:
					node = new BooleanNode(Node.Type.NOT_QUOTED, false);
					break;
				case L_PAREN: // ��ø list��
					ungetToken();
					node = parseList();
					break;
				default:
					errorLog("parseExpr switch Error");
					break;
				}
			}
			return node;
		}

		// list�� �� ���Ҹ��� �Ľ���
		private Node parseItem() {
			Node item = null;
			Token t = getNextToken();

			if (t != null) { // �� input���� �ڷ����� Ȯ���ؼ� �־���.
				switch (t.type) {
				case ID: // id,int,boolean�� ��� expr�� �ٽ� ��.
					ungetToken();
					item = parseExpr();
					break;
				case INT:
					ungetToken();
					item = parseExpr();
					break;
				case MINUS:
					item = new BinarayOpNode(Node.Type.NOT_QUOTED,
							BinarayOpNode.BinType.MINUS);
					break;
				case PLUS:
					item = new BinarayOpNode(Node.Type.NOT_QUOTED,
							BinarayOpNode.BinType.PLUS);
					break;
				case TIMES:
					item = new BinarayOpNode(Node.Type.NOT_QUOTED,
							BinarayOpNode.BinType.TIMES);
					break;
				case DIV:
					item = new BinarayOpNode(Node.Type.NOT_QUOTED,
							BinarayOpNode.BinType.DIV);
					break;
				case LT:
					item = new BinarayOpNode(Node.Type.NOT_QUOTED,
							BinarayOpNode.BinType.LT);
					break;
				case GT:
					item = new BinarayOpNode(Node.Type.NOT_QUOTED,
							BinarayOpNode.BinType.GT);
					break;
				case EQ:
					item = new BinarayOpNode(Node.Type.NOT_QUOTED,
							BinarayOpNode.BinType.EQ);
					break;
				case DEFINE:
					item = new FunctionNode(Node.Type.NOT_QUOTED,
							FunctionNode.FunctionType.DEFINE);
					break;
				case LAMBDA:
					item = new FunctionNode(Node.Type.NOT_QUOTED,
							FunctionNode.FunctionType.LAMBDA);
					break;
				case COND:
					item = new FunctionNode(Node.Type.NOT_QUOTED,
							FunctionNode.FunctionType.COND);
					break;
				case NOT:
					item = new FunctionNode(Node.Type.NOT_QUOTED,
							FunctionNode.FunctionType.NOT);
					break;
				case CDR:
					item = new FunctionNode(Node.Type.NOT_QUOTED,
							FunctionNode.FunctionType.CDR);
					break;
				case CAR:
					item = new FunctionNode(Node.Type.NOT_QUOTED,
							FunctionNode.FunctionType.CAR);
					break;
				case CONS:
					item = new FunctionNode(Node.Type.NOT_QUOTED,
							FunctionNode.FunctionType.CONS);
					break;
				case EQ_Q:
					item = new FunctionNode(Node.Type.NOT_QUOTED,
							FunctionNode.FunctionType.EQ_Q);
					break;
				case NULL_Q:
					item = new FunctionNode(Node.Type.NOT_QUOTED,
							FunctionNode.FunctionType.NULL_Q);
					break;
				case ATOM_Q:
					item = new FunctionNode(Node.Type.NOT_QUOTED,
							FunctionNode.FunctionType.ATOM_Q);
					break;
				case TRUE:
					ungetToken();
					item = parseExpr();
					break;
				case FALSE:
					ungetToken();
					item = parseExpr();
					break;
				case QUOTE: // quote�� '������� expr�� ���� list�� type�� quoted�� ����
					item = parseExpr();
					item.setType(Node.Type.QUOTED);
					break;
				case APOSTROPHE:
					item = parseExpr();
					item.setType(Node.Type.QUOTED);
					break;
				case L_PAREN: // ��ø list��
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

		// list �� ��ȣ�� �� ���θ� parsing ��
		private Node parseItemList() {
			Node list = null;// ���⿡ list�� �����ؼ� ����� ���� ����
			Token t = getNextToken();
			// keyward�� ���� �˻�.
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
				case TRUE:
				case FALSE:
				case QUOTE:
				case APOSTROPHE:
				case L_PAREN:// ��, ������ ��ȣ�� �ƴ� ��� token�� ����
					ungetToken();
					list = parseItem(); // �� ���Ҹ� parsing�Ͽ� head �� ����
					break;
				default: // ������ ��ȣ�ų� ����. List ��. �ٽ� �־�ΰ� ������.
					ungetToken();
					break;
				}
			}

			if (list != null)
				list.setNext(parseItemList()); // recursion�� �Ἥ tail�� ����� head��
												// next�� �̾� ����
			return list;
		}

		public Node parseList() {
			Node list = null; // �̰��� ����Ʈ�� �����Ͽ� ���� ����
			Token t = getNextToken();
			if (t != null && t.type == TokenType.L_PAREN) {
				Node itemList = parseItemList();
				t = getNextToken();
				if (t == null || t.type != TokenType.R_PAREN) // ������ ��ȣ�� �ƴϸ� ����
					errorLog("parseList Error rparen");
				else
					list = new ListNode(Node.Type.NOT_QUOTED, itemList);
			} else {
				errorLog("parseList Error lparen");// ���� ��ȣ�� �ƴϸ� ����
			}

			return list;
		}

		// ���α׷��� �Ľ�
		public Node parseProgram() {
			if (alreadyParse) {
				errorLog("Already Parse");
				return null;
			}

			Node list = null;
			Token t = getNextToken();
			if (null != t) {
				switch (t.type) { // '�� quote�� quoted���̱� ������ expr�� list�� �����ϸ�
									// Ÿ���� ��������.
				case APOSTROPHE:
					list = parseExpr(); // ��ø����Ʈ
					list.setType(Node.Type.QUOTED);
					break;
				case ID:
				case INT:
				case TRUE:
				case FALSE:
				case L_PAREN:
					ungetToken();
					list = parseExpr(); // ��ø����Ʈ
					break;
				default:
					ungetToken();
					errorLog("parseSession Error");
					break;
				}
			}

			if (null != list) {
				list.setNext(parseProgram());
			}

			return list;

		}
	}

	public class Printer {
		PrintStream ps;

		public Printer(PrintStream ps) {
			this.ps = ps;
		}

		public void NodePrint(Node node) {
			while (node != null) // ��͸� ���� ������ null�ϰ�� ���߰Ե�.
			{
				if (node.getClass() == ListNode.class) // ���� node�� listnode�ϰ��
														// �ٽ� list�� �ֱ� ������ �ٽ�
														// ��.
				{
					if (node.getType() == node.type.QUOTED && !(node instanceof BooleanNode)) // quote�� ��� '�� ��µǱ�
															// ������ ���.
						System.out.print("'");

					System.out.print("("); // (�� ��� ����Ʈ�� �Ǿտ� ���⶧���� ��������� ��������
											// ����Ʈ.
					NodePrint(((ListNode) node).value);
					System.out.print(")"); // ��������� �� �������ϰ�� ���� �ö���鼭 ��µǾ� �Ǳ�
											// ������ ��͹� �ڿ� ����.
				}
				// list��尡 �ƴҰ�� value���� �ֱ� ������ ���.
				else {
					if (node.getType() == node.type.QUOTED && !(node instanceof BooleanNode)) //ó������ '�� ������ ��찡
															// ������ ���.
						System.out.print("'");

					if (node instanceof BooleanNode) {
						if (((BooleanNode) node).value == true)
							System.out.print(" #T ");
						else
							System.out.print(" #F ");

					} else
						System.out.print(" " + node + " ");
				}

				node = node.getNext(); // listnode�� ������ ������ ��� ����Ʈ�� ����.
			}
		}
	}
	
	@SuppressWarnings("resource")
	public static void main(String[] args) throws IOException
	{
			
		Scanner sc = new Scanner(System.in);
		
		System.out.println("[���� : -1]");
		
		while(true)
		{	
			System.out.print("> ");
			String source=sc.nextLine(); //Cute Expression�� �Է�.
			
			if(source.equals("-1"))
				break;
			
			ScannerClass s = new ScannerClass(source);
			List<Token> tokens = s.tokenize();

			Parser p = s.new Parser(tokens); // ���Ͽ��� �޾ƿ� tokens�� �Ľ��ϱ����� �Ѱ���.
			Node node = p.parseProgram(); // �Ľ̵� ����� root�� ������.

			Printer pt = s.new Printer(System.out);

			CuteInterpreter i = new CuteInterpreter();

			for (Node n = node; n != null; n = n.getNext()) 
			{
				Node ir = i.runExpr(n);
				System.out.print("��");
				pt.NodePrint(ir);
				System.out.println();
			}
		}
	}
}
