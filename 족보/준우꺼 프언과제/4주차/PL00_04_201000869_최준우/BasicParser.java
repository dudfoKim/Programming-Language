/*
 * 과제 번호 : hw04 
 * 분반 : 00분반
 * 소속 : 컴퓨터공학과
 * 학번 : 201000869
 * 이름 : 최준우
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
	 * 클래스 변수 초기화
	 */
	public BasicParser(List<Token> list) { 
		this.alreadyParse = false; 
		this.pos = 0; 
		this.tokenList = list; 
	} 

	/*
	 * 다음 토큰을 얻어온다.
	 */
	private Token getNextToken() { 
		if (pos < tokenList.size()) 
			return tokenList.get(pos++); 
		else 
			return null; 
	} 

	/*
	 * 토큰의 현재 position을 하나 줄인다.
	 */
	private void ungetToken() { 
		if (pos > 0) 
			pos--; 
	} 

	/*
	 * error를 출력한다.
	 */
	private void errorLog(String err) { 
		System.out.println(err); 
	} 

	// list의 한 원소만을 파싱함
	/*
	 * item의 case를 나누어 해당 case에 대한
	 * node를 생성한다.
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
				//이번 과제에서는 ‘나 QUOTE 기호가 없다고 가정하기 때문에 모든 
				//노드가 Type.NOT_QUOTED이다 
				//... 
				//... 
				//... 

			/*
			 * 중첩 리스트를 만나면 토큰을 반환하고 parseList를 통해
			 * listNode를 생성하여 자식 List를 만든다.
			 */
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

	/*
	 * 토큰들에 대해 List를 생성한다.
	 * List는 '('에서 시작해야 하며, itemNode로 조합된다.
	 * '('을 만나면 각 token을 item으로 Parsing한다.
	 */
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
} 

