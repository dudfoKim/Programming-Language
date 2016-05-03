/* 
 * ���� ��ȣ : 08
 * �й� ǥ�� : 01��
 * �Ҽ�, �й�, ���� : ��ǻ�Ͱ��а�, 201002434, ��ä��
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
	// list�� �� ���Ҹ��� �Ľ���
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
				// ��, ������ ��ȣ�� �ƴ� ��� token�� ����
				ungetToken();
				list = parseItem(); // �� ���Ҹ� parsing�Ͽ� head �� ����
				break;
			default: // ������ ��ȣ�ų� ����. List ��. �ٽ� �־�ΰ� ������.
				ungetToken();
				break;
			}
		}
		if (list != null)
			list.setNext(parseItemList()); // recursion�� �Ἥ tail�� ����� head�� next�� �̾� ����
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
				list = new ListNode(cuteParser2.Node.Type.NOT_QUOTED, itemList);
		} else {
			errorLog("parseList Error lparen");// ���� ��ȣ�� �ƴϸ� ����
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
			list =  parseExpr(); // ��ø����Ʈ
			list.setType(Node.Type.QUOTED);
			break;
		case APOSTROPHE:
			list =  parseExpr(); // ��ø����Ʈ
			list.setType(Node.Type.QUOTED);
			break;
		case ID:
		case INT :
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
