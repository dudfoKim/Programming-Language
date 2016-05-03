/*
 * ���� ��ȣ : hw04 
 * �й� : 00�й�
 * �Ҽ� : ��ǻ�Ͱ��а�
 * �й� : 201000869
 * �̸� : ���ؿ�
 */

package hw04_Reconizing_Token_u00;

import hw04_Reconizing_Token_u00.BinarayOpNode.BinType;
import hw04_Reconizing_Token_u00.FunctionNode.FunctionType;
import hw04_Reconizing_Token_u00.Node.Type;
import hw04_Reconizing_Token_u00.Scanner.Token;
import hw04_Reconizing_Token_u00.Scanner.TokenType;

import java.util.List;

public class BasicParser { 

	boolean alreadyParse; 
	int pos; 
	List<Token> tokenList; 

	
	/*
	 * Ŭ���� ���� �ʱ�ȭ
	 */
	public BasicParser(List<Token> list) { 
		this.alreadyParse = false; 
		this.pos = 0; 
		this.tokenList = list; 
	} 

	/*
	 * ���� ��ū�� ���´�.
	 */
	private Token getNextToken() { 
		if (pos < tokenList.size()) 
			return tokenList.get(pos++); 
		else 
			return null; 
	} 

	/*
	 * ��ū�� ���� position�� �ϳ� ���δ�.
	 */
	private void ungetToken() { 
		if (pos > 0) 
			pos--; 
	} 

	/*
	 * error�� ����Ѵ�.
	 */
	private void errorLog(String err) { 
		System.out.println(err); 
	} 

	// list�� �� ���Ҹ��� �Ľ���
	/*
	 * item�� case�� ������ �ش� case�� ����
	 * node�� �����Ѵ�.
	 */
	private Node parseItem() { 
		Node item = null; 
		Token t = getNextToken(); 

		if (t != null) { 
			switch (t.type) { 
			case ID: 
				item = new IdNode(Type.NOT_QUOTED, t.lexme); 
				break; 
			case INT: 
				item = new IntNode(Type.NOT_QUOTED, new 
						Integer(t.lexme)); 
				break; 
			case MINUS: 
				item = new BinarayOpNode(Type.NOT_QUOTED, 
						BinType.MINUS); 
				break;
			case PLUS: 
				item = new BinarayOpNode(Type.NOT_QUOTED, 
						BinType.PLUS); 
				break; 
			case TIMES: 
				item = new BinarayOpNode(Type.NOT_QUOTED, 
						BinType.TIMES); 
				break; 
			case DIV: 
				item = new BinarayOpNode(Type.NOT_QUOTED, 
						BinType.DIV); 
				break; 
			case LT: 
				item = new BinarayOpNode(Type.NOT_QUOTED, 
						BinType.LT); 
				break; 
			case GT: 
				item = new BinarayOpNode(Type.NOT_QUOTED, 
						BinType.GT); 
				break; 
			case EQ: 
				item = new BinarayOpNode(Type.NOT_QUOTED, 
						BinType.EQ); 
				break; 
			case TRUE: 
				item = new BooleanNode(Type.NOT_QUOTED, 
						true); 
				break; 
			case FALSE: 
				item = new BooleanNode(Type.NOT_QUOTED, 
						false); 
				break; 
			case DEFINE: 
				item = new FunctionNode(Type.NOT_QUOTED, 
						FunctionType.DEFINE);
				break; 
			case LAMBDA: 
				item = new FunctionNode(Type.NOT_QUOTED, 
						FunctionType.LAMBDA);
				break;
			case COND: 
				item = new FunctionNode(Type.NOT_QUOTED, 
						FunctionType.COND);
				break; 
			case NOT: 
				item = new FunctionNode(Type.NOT_QUOTED, 
						FunctionType.NOT);
				break; 
			case CDR: 
				item = new FunctionNode(Type.NOT_QUOTED, 
						FunctionType.CDR);
				break; 
			case CAR: 
				item = new FunctionNode(Type.NOT_QUOTED, 
						FunctionType.CAR);
				break; 
			case CONS: 
				item = new FunctionNode(Type.NOT_QUOTED, 
						FunctionType.CONS); 
				break; 
			case EQ_Q: 
				item = new FunctionNode(Type.NOT_QUOTED, 
						FunctionType.EQ_Q); 
				break;
			case NULL_Q: 
				item = new FunctionNode(Type.NOT_QUOTED, 
						FunctionType.NULL_Q); 
				break; 
			case ATOM_Q: 
				item = new FunctionNode(Type.NOT_QUOTED, 
						FunctionType.ATOM_Q); 
				break; 
				//�̹� ���������� ���� QUOTE ��ȣ�� ���ٰ� �����ϱ� ������ ��� 
				//��尡 Type.NOT_QUOTED�̴� 
				//... 
				//... 
				//... 

			/*
			 * ��ø ����Ʈ�� ������ ��ū�� ��ȯ�ϰ� parseList�� ����
			 * listNode�� �����Ͽ� �ڽ� List�� �����.
			 */
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
			case MINUS:
			case PLUS: 
			case TIMES: 
			case DIV: 
			case LT: 
			case GT: 
			case EQ: 
			case TRUE: 
			case FALSE: 
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

	/*
	 * ��ū�鿡 ���� List�� �����Ѵ�.
	 * List�� '('���� �����ؾ� �ϸ�, itemNode�� ���յȴ�.
	 * '('�� ������ �� token�� item���� Parsing�Ѵ�.
	 */
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
} 

