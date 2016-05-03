/* 
 * ���� ��ȣ : 04
 * �й� ǥ�� : 01��
 * �Ҽ�, �й�, ���� : ��ǻ�Ͱ��а�, 201002434, ��ä��
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
	// list�� �� ���Ҹ��� �Ľ���
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
			case DIV:
			case LT:
			case EQ:
			case GT:
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
				list = new ListNode(cute14_Parser.Node.Type.NOT_QUOTED, itemList);
		} else {
			errorLog("parseList Error lparen");// ���� ��ȣ�� �ƴϸ� ����
		}
		return list;
	}
}
//3. Per-order traverse print class
