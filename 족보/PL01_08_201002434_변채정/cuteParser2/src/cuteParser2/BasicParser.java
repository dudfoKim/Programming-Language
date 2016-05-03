/* 
 * 과제 번호 : 08
 * 분반 표기 : 01반
 * 소속, 학번, 성명 : 컴퓨터공학과, 201002434, 변채정
*/

package cuteParser2;

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
				ungetToken();
				item = parseExpr();
				break;
			case INT:
				ungetToken();
				item = parseExpr();
				break;
			case DEFINE:
				item = new FunctionNode(cuteParser2.Node.Type.NOT_QUOTED, cuteParser2.FunctionNode.FunctionType.DEFINE);
				break;
			case LAMBDA:
				item = new FunctionNode(cuteParser2.Node.Type.NOT_QUOTED, cuteParser2.FunctionNode.FunctionType.LAMBDA);
				break;
			case COND:
				item = new FunctionNode(cuteParser2.Node.Type.NOT_QUOTED, cuteParser2.FunctionNode.FunctionType.COND);
				break;
			case NOT:
				item = new FunctionNode(cuteParser2.Node.Type.NOT_QUOTED, cuteParser2.FunctionNode.FunctionType.NOT);
				break;
			case CDR:
				item = new FunctionNode(cuteParser2.Node.Type.NOT_QUOTED, cuteParser2.FunctionNode.FunctionType.CDR);
				break;
			case CAR:
				item = new FunctionNode(cuteParser2.Node.Type.NOT_QUOTED, cuteParser2.FunctionNode.FunctionType.CAR);
				break;
			case CONS:
				item = new FunctionNode(cuteParser2.Node.Type.NOT_QUOTED, cuteParser2.FunctionNode.FunctionType.CONS);
				break;
			case EQ_Q:
				item = new FunctionNode(cuteParser2.Node.Type.NOT_QUOTED, cuteParser2.FunctionNode.FunctionType.EQ_Q);
				break;
			case NULL_Q:
				item = new FunctionNode(cuteParser2.Node.Type.NOT_QUOTED, cuteParser2.FunctionNode.FunctionType.NULL_Q);
				break;
			case ATOM_Q:		
				item = new FunctionNode(cuteParser2.Node.Type.NOT_QUOTED, cuteParser2.FunctionNode.FunctionType.ATOM_Q);
				break;
			case TRUE:
				ungetToken();
				item = parseExpr();
				break;
			case FALSE:
				ungetToken();
				item = parseExpr();
				break;
			case PLUS:
				item = new BinarayOpNode(cuteParser2.Node.Type.NOT_QUOTED, cuteParser2.BinarayOpNode.BinType.PLUS);
				break;
			case MINUS:
				item = new BinarayOpNode(cuteParser2.Node.Type.NOT_QUOTED, cuteParser2.BinarayOpNode.BinType.MINUS);
				break;
			case TIMES:
				item = new BinarayOpNode(cuteParser2.Node.Type.NOT_QUOTED, cuteParser2.BinarayOpNode.BinType.TIMES);
				break;
			case DIV:
				item = new BinarayOpNode(cuteParser2.Node.Type.NOT_QUOTED, cuteParser2.BinarayOpNode.BinType.DIV);
				break;
			case LT:
				item = new BinarayOpNode(cuteParser2.Node.Type.NOT_QUOTED, cuteParser2.BinarayOpNode.BinType.LT);
				break;
			case EQ:
				item = new BinarayOpNode(cuteParser2.Node.Type.NOT_QUOTED, cuteParser2.BinarayOpNode.BinType.EQ);
				break;
			case GT:
				item = new BinarayOpNode(cuteParser2.Node.Type.NOT_QUOTED, cuteParser2.BinarayOpNode.BinType.GT);
				break;
			case APOSTROPHE: 
				item = parseExpr();
				item.setType(Node.Type.QUOTED);
				break;
			case QUOTE: 
				item = parseExpr();
				item.setType(Node.Type.QUOTED);
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
			case TIMES:
			case DIV:
			case LT:
			case EQ:
			case GT:
			case APOSTROPHE:
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
				list = new ListNode(cuteParser2.Node.Type.NOT_QUOTED, itemList);
		} else {
			errorLog("parseList Error lparen");// 왼쪽 괄호가 아니면 에러
		}
		return list;
	}

public Node parseProgram()
{
	if(alreadyParse){
		errorLog("Already Parse");
		return null;
	}
	Node list = null;
	Token t = getNextToken();
	if(null != t){
		switch(t.type){
		case QUOTE:
			list =  parseExpr(); // 중첩리스트
			list.setType(Node.Type.QUOTED);
			break;
		case APOSTROPHE:
			list =  parseExpr(); // 중첩리스트
			list.setType(Node.Type.QUOTED);
			break;
		case ID:
		case INT :
		case TRUE:
		case FALSE:
		case L_PAREN:
			ungetToken();
			list = parseExpr(); // 중첩리스트
			break;
		default:
			ungetToken();
			errorLog("parseSession Error");
			break;
		}
	}
	if(null != list){
		list.setNext(parseProgram());
		}
	return list;
	}
private Node parseExpr(){
	Node node = null;
	Token t = getNextToken();
	if(t != null){
		switch(t.type){
			case ID:
				node = new IdNode(cuteParser2.Node.Type.NOT_QUOTED,t.lexme);
				break;
			case INT:
				node = new IntNode(cuteParser2.Node.Type.NOT_QUOTED,new Integer(t.lexme));
				break;
			case TRUE:
				node = new BooleanNode(cuteParser2.Node.Type.NOT_QUOTED, true);
				break;
			case FALSE:
				node = new BooleanNode(cuteParser2.Node.Type.NOT_QUOTED,false);
				break;
			case L_PAREN: 
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
}
//3. Per-order traverse print class
