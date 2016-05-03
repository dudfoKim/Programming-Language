/* 
 * 과제 번호 : 04
 * 분반 표기 : 01반
 * 소속, 학번, 성명 : 컴퓨터공학과, 201002434, 변채정
 */

package cute14_Parser;

import java.io.PrintStream;
import java.util.List;

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
				item = new IdNode(cute14_Parser.Node.Type.NOT_QUOTED, t.lexme);
				break;
			case INT:
				item = new IntNode(cute14_Parser.Node.Type.NOT_QUOTED, new Integer(t.lexme));
				break;
			case DEFINE:
				item = new FunctionNode(cute14_Parser.Node.Type.NOT_QUOTED, cute14_Parser.FunctionNode.FunctionType.DEFINE);
			case LAMBDA:
				item = new FunctionNode(cute14_Parser.Node.Type.NOT_QUOTED, cute14_Parser.FunctionNode.FunctionType.LAMBDA);
			case COND:
				item = new FunctionNode(cute14_Parser.Node.Type.NOT_QUOTED, cute14_Parser.FunctionNode.FunctionType.COND);
			case NOT:
				item = new FunctionNode(cute14_Parser.Node.Type.NOT_QUOTED, cute14_Parser.FunctionNode.FunctionType.NOT);
			case CDR:
				item = new FunctionNode(cute14_Parser.Node.Type.NOT_QUOTED, cute14_Parser.FunctionNode.FunctionType.CDR);
			case CAR:
				item = new FunctionNode(cute14_Parser.Node.Type.NOT_QUOTED, cute14_Parser.FunctionNode.FunctionType.CAR);
			case CONS:
				item = new FunctionNode(cute14_Parser.Node.Type.NOT_QUOTED, cute14_Parser.FunctionNode.FunctionType.CONS);
			case EQ_Q:
				item = new FunctionNode(cute14_Parser.Node.Type.NOT_QUOTED, cute14_Parser.FunctionNode.FunctionType.EQ_Q);
			case NULL_Q:
				item = new FunctionNode(cute14_Parser.Node.Type.NOT_QUOTED, cute14_Parser.FunctionNode.FunctionType.NULL_Q);
			case ATOM_Q:		
				item = new FunctionNode(cute14_Parser.Node.Type.NOT_QUOTED, cute14_Parser.FunctionNode.FunctionType.ATOM_Q);
			case TRUE:
				item = new BooleanNode(cute14_Parser.Node.Type.NOT_QUOTED, true);
			case FALSE:
				item = new BooleanNode(cute14_Parser.Node.Type.NOT_QUOTED, false);
			case PLUS:
				item = new BinarayOpNode(cute14_Parser.Node.Type.NOT_QUOTED, cute14_Parser.BinarayOpNode.BinType.PLUS);
				break;
			case MINUS:
				item = new BinarayOpNode(cute14_Parser.Node.Type.NOT_QUOTED, cute14_Parser.BinarayOpNode.BinType.MINUS);
				break;
			case DIV:
				item = new BinarayOpNode(cute14_Parser.Node.Type.NOT_QUOTED, cute14_Parser.BinarayOpNode.BinType.DIV);
				break;
			case LT:
				item = new BinarayOpNode(cute14_Parser.Node.Type.NOT_QUOTED, cute14_Parser.BinarayOpNode.BinType.LT);
				break;
			case EQ:
				item = new BinarayOpNode(cute14_Parser.Node.Type.NOT_QUOTED, cute14_Parser.BinarayOpNode.BinType.EQ);
				break;
			case GT:
				item = new BinarayOpNode(cute14_Parser.Node.Type.NOT_QUOTED, cute14_Parser.BinarayOpNode.BinType.GT);
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
			case DEFINE:
			case LAMBDA:
			case COND:
			case QUOTE:
			case NOT:
			case CDR:
			case CAR:
			case CONS:
			case EQ_Q:
			case NULL_Q:
			case ATOM_Q:
			case TRUE:
			case FALSE:
			case PLUS:
			case MINUS:
			case DIV:
			case LT:
			case EQ:
			case GT:
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
			list.setNext(parseItemList()); // recursion을 써서 tail을 만들어 head의 next에 이어 붙임
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
				list = new ListNode(cute14_Parser.Node.Type.NOT_QUOTED, itemList);
		} else {
			errorLog("parseList Error lparen");// 왼쪽 괄호가 아니면 에러
		}
		return list;
	}
}
//3. Per-order traverse print class
