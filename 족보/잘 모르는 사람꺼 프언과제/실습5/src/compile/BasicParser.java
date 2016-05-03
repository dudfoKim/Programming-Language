package compile;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import ast.BinarayOpNode;
import ast.BinarayOpNode.BinType;
import ast.BooleanNode;
import ast.FunctionNode;
import ast.FunctionNode.FunctionType;
import ast.IdNode;
import ast.IntNode;
import ast.ListNode;
import ast.Node;
import ast.Node.Type;

import compile.CuteScanner.Token;
import compile.CuteScanner.TokenType;


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

	// list�� �� ���Ҹ��� �Ľ���
	private Node parseItem() {
		Node item = null;
		Token t = getNextToken();

		if (t != null) {
			switch (t.type) {
			case ID:
				item = new IdNode(Type.NOT_QUOTED, t.lexeme);
				break;
			case INT:
				item = new IntNode(Type.NOT_QUOTED, new Integer(t.lexeme));
				break;
			case MINUS:
				item = new BinarayOpNode(Type.NOT_QUOTED, BinType.MINUS);
				break;
			case ATOM_Q:
				item = new FunctionNode(Type.NOT_QUOTED, FunctionType.ATOM_Q);
				break;
				
			case CAR:
				item = new FunctionNode(Type.NOT_QUOTED, FunctionType.CAR);
				break;
				
			case CDR:
				item = new FunctionNode(Type.NOT_QUOTED, FunctionType.CDR);
				break;
				
			case COND:
				item = new FunctionNode(Type.NOT_QUOTED, FunctionType.COND);
				break;
			case CONS:
				item = new FunctionNode(Type.NOT_QUOTED, FunctionType.CONS);
				break;
				
			case DEFINE:
				item = new FunctionNode(Type.NOT_QUOTED, FunctionType.DEFINE);
				break;
				
			case DIV:
				item = new BinarayOpNode(Type.NOT_QUOTED, BinType.DIV);
				break;
				
			case EQ:
				item = new BinarayOpNode(Type.NOT_QUOTED, BinType.EQ);
				break;
				
			case EQ_Q:
				item = new FunctionNode(Type.NOT_QUOTED, FunctionType.EQ_Q);
				break;
			case FALSE:
				item = new BooleanNode(Type.NOT_QUOTED, false);
				break;
				
			case GT:
				item = new BinarayOpNode(Type.NOT_QUOTED, BinType.GT);
				break;
				
			case LAMBDA:
				item = new FunctionNode(Type.NOT_QUOTED, FunctionType.LAMBDA);
				break;
			case LT:
				item = new BinarayOpNode(Type.NOT_QUOTED, BinType.LT);
				break;
				
			case NOT:
				item = new FunctionNode(Type.NOT_QUOTED, FunctionType.NOT);
				break;
				
			case NULL_Q:
				item = new FunctionNode(Type.NOT_QUOTED, FunctionType.NULL_Q);
				break;
			case PLUS:
				item = new BinarayOpNode(Type.NOT_QUOTED, BinType.PLUS);
				break;
				
			case TIMES:
				item = new BinarayOpNode(Type.NOT_QUOTED, BinType.TIMES);
				break;
				
			case TRUE:
				item = new BooleanNode(Type.NOT_QUOTED, true);
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

		if (t != null) {
			switch (t.type) {
			case ID:
			case INT:
			case ATOM_Q:
			case CAR:
			case CDR:
			case COND:
			case CONS:
			case DEFINE:
			case DIV:
			case EQ:
			case EQ_Q:
			case FALSE:
			case GT:
			case LAMBDA:
			case LT:
			case MINUS:
			case NOT:
			case NULL_Q:
			case PLUS:
			case TIMES:
			case TRUE:				
			case L_PAREN:
				// ��, ������ ��ȣ�� �ƴ� ��� token�� ����
				ungetToken();
				list = parseItem(); // �� ���Ҹ� parsing�Ͽ� head �� ����
				break;
			default: // ������ ��ȣ�ų� ����. List ��. �ٽ� �־�ΰ� ������.
				ungetToken();
				break;
			}
		}
		
		if(list != null)
			list.setNext(parseItemList()); // recursion�� �Ἥ  tail�� ����� head�� next�� �̾� ����
		
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
				list = new ListNode(Type.NOT_QUOTED, itemList);

		} else {
			errorLog("parseList Error lparen");// ���� ��ȣ�� �ƴϸ� ����
		}

		return list;
	}
	
	public static void main(String[] args) throws Exception {
		byte[] bytes = Files.readAllBytes(Paths.get("./as04.txt"));
		String source = new String(bytes);
		
		CuteScanner s = new CuteScanner(source);
		List<Token> result = s.tokenize();
		BasicParser p = new BasicParser(result);
		Node node = p.parseList();

		Printer pt = new Printer(System.out);
		pt.printNode(node);
	}
}
