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

	// list의 한 원소만을 파싱함
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
				// 즉, 오른쪽 괄호가 아닌 모든 token에 대해
				ungetToken();
				list = parseItem(); // 한 원소를 parsing하여 head 를 만듬
				break;
			default: // 오른쪽 괄호거나 오류. List 끝. 다시 넣어두고 리턴함.
				ungetToken();
				break;
			}
		}
		
		if(list != null)
			list.setNext(parseItemList()); // recursion을 써서  tail을 만들어 head의 next에 이어 붙임
		
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
				list = new ListNode(Type.NOT_QUOTED, itemList);

		} else {
			errorLog("parseList Error lparen");// 왼쪽 괄호가 아니면 에러
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
