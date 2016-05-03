package compile;

import java.util.List;
import java.util.Scanner;

import compile.CuteScanner.Token;
import ast.BinaryOpNode;
import ast.BooleanNode;
import ast.FunctionNode;
import ast.FunctionNode.FunctionType;
import ast.IdNode;
import ast.IntNode;
import ast.ListNode;
import ast.Node;
import ast.QuoteNode;

public class CuteInterpreter {

	private final static BooleanNode TRUE_NODE = new BooleanNode();
	private final static BooleanNode FALSE_NODE = new BooleanNode();

	DefineMap defineMap = new DefineMap();
	
	static {
		TRUE_NODE.value = true;
		FALSE_NODE.value = false;
	} // 트루노드와 팔스노드

	private void errorLog(String err) {
		System.out.println(err);
	}

	enum CopyMode {
		NO_NEXT, NEXT
	} // 카피모드는 두종류, NO_NEXT =

	private Node runFunction(FunctionNode func) {
		Node rhs1 = func.getNext();
		Node rhs2 = (rhs1 != null) ? rhs1.getNext() : null;

		switch (func.value) {
		case CAR: {
			if (!checkQuote(rhs1))
				errorLog("Syntax Error!");
			Node item = runQuote((ListNode) rhs1);

			//copyNode를 이용하여 값을 return 하시오.
			return copyNode(((ListNode)item).value, CopyMode.NO_NEXT);

		}
		case CDR: {
			if (!checkQuote(rhs1))
				errorLog("Syntax Error!");
			Node item = runQuote((ListNode) rhs1);

			((ListNode)item).value = ((ListNode)item).value.getNext();

			return copyNode((ListNode)item, CopyMode.NEXT);
		}

		case CONS: {
			//CONS에 맞게 작성할것
			Node head = runExpr(rhs1);
			Node tail = runExpr(rhs2);

			head.setNext(((ListNode)tail).value);
			ListNode result = new ListNode();
			result.value = head;

			return result;
		}
		case ATOM_Q:
			if (!(rhs1 instanceof ListNode)
					|| (((ListNode) rhs1).value == null))
				return TRUE_NODE;
			else
				return FALSE_NODE;

		case EQ_Q:
			if (rhs1 != null && rhs1.equals(rhs2)) {
				return TRUE_NODE;
			} else {
				return FALSE_NODE;
			}

		case NULL_Q:
			if (rhs1 instanceof ListNode && ((ListNode) rhs1).value == null)
				return TRUE_NODE;
			else
				return FALSE_NODE;

		case NOT:
			if (((BooleanNode)rhs1).value == true)
				return FALSE_NODE;
			else
				return TRUE_NODE;

		case COND:
			Node tmp2 = rhs1;
			Node tmp1 = ((ListNode)rhs1).value;
			while(tmp2 != null){
				tmp1 = ((ListNode)tmp2).value;
				if(tmp1 instanceof ListNode)
				{
					Node runResult = this.runList((ListNode)tmp1);
					if(runResult instanceof BooleanNode && ((BooleanNode) runResult).value == true)
					{
						return tmp1.getNext();
					}
				}
				else
				{
					if(tmp1 instanceof BooleanNode && ((BooleanNode) tmp1).value == true)
					{
						return tmp1.getNext();
					}
				}
				tmp2 = tmp2.getNext();		
			}
			break;
			
		case DEFINE:
			Node first = rhs1;
			Node second = rhs2;
			
			if(second instanceof ListNode)
				if(  (((FunctionNode)((ListNode)second).value).value).equals("LAMBDA")   )
					second = runList((ListNode)second);

			defineMap.map.put(((IdNode)first).value, second);
	
			return TRUE_NODE;
		case LAMBDA:
			Node second2 = rhs2;
			Node third2 = ((ListNode)rhs2).value;

			return third2;
			

		default:
			break;

		}

		return null;
	}

	private Node runBinary(BinaryOpNode node) {
		Node result;
		Node left = node.getNext();
		Node right = left.getNext();
		left = runExpr(left);
		right = runExpr(right);
		if (left == null || right == null)
			errorLog("runBinary runExpr null");
		if (!(left instanceof IntNode) || !(right instanceof IntNode)) {
			errorLog("Type Error!");
			return null;
		}

		switch (node.value) {
		case MINUS:
			result = new IntNode();
			((IntNode) result).value = ((IntNode) left).value
					- ((IntNode) right).value;
			return result;
			// 기타 연산자, MINUS와 비슷하게 각자 구현할 것
			// 관계연산은 TRUE, FALSE반환
		case PLUS:
			result = new IntNode();
			((IntNode) result).value = ((IntNode) left).value
					+ ((IntNode) right).value;
			return result;

		case TIMES:
			result = new IntNode();
			((IntNode) result).value = ((IntNode) left).value
					* ((IntNode) right).value;
			return result;

		case DIV:
			result = new IntNode();
			((IntNode) result).value = ((IntNode) left).value
					/ ((IntNode) right).value;
			return result;

		case LT:
			result = new BooleanNode();
			if( ((IntNode) left).value < ((IntNode) right).value )
				((BooleanNode) result).value = true;
			else
				((BooleanNode) result).value = false;
			return result;

		case GT:
			result = new BooleanNode();
			if( ((IntNode) left).value > ((IntNode) right).value )
				((BooleanNode) result).value = true;
			else
				((BooleanNode) result).value = false;
			return result;

		case EQ:
			result = new BooleanNode();
			if( ((IntNode) left).value == ((IntNode) right).value )
				((BooleanNode) result).value = true;
			else
				((BooleanNode) result).value = false;
			return result;
		}
		return null;
	}


	private Node copyNode(Node node, CopyMode mode) {
		// node를 복사
		// mode에 따라서 next를 복사함
		if (node == null)
			return null;

		Node result = null;

		if (node instanceof BinaryOpNode)
			result = new BinaryOpNode();
		else if (node instanceof BooleanNode)
			result = new BooleanNode();
		else if (node instanceof FunctionNode)
			result = new FunctionNode();
		else if (node instanceof IdNode)
			result = new IdNode();
		else if (node instanceof IntNode)
			result = new IntNode();
		else if (node instanceof ListNode)
			result = new ListNode();

		result.copyValue(node);
		if (mode == CopyMode.NEXT && result != null)
			result.setNext(copyNode(node.getNext(), mode));

		return result;
	}

	private Node runList(ListNode list) {
		// list의 value가 QuoteNode일 경우
		
		if (list.value instanceof QuoteNode)
			return runQuote(list);
		Node opCode = list.value;
		Node result;
		int temp;
		if (opCode == null)
			return list;
		
		if (opCode instanceof FunctionNode)
		{
			//return runFunction((FunctionNode) opCode);
			result = runFunction((FunctionNode) opCode);
			if(result instanceof BinaryOpNode && list.getNext() != null)
			{
				defineMap.map.put( ((IdNode)(result.getNext())).value, list.getNext());
				return runBinary((BinaryOpNode)result);
			}
			else if(result == null) 
				return null;
			else if( !(((FunctionNode)opCode).value ==  FunctionType.LAMBDA))
				return result;
		}
		if (opCode instanceof ListNode)
			return runList( (ListNode)opCode); 
				
		if (opCode instanceof BinaryOpNode)
			return runBinary((BinaryOpNode)opCode);
		
		if (opCode instanceof IdNode && defineMap.map.containsKey(((IdNode) opCode).value) ){
			IntNode node1 = new IntNode();
			node1.copyValue(opCode.getNext());
			opCode = runExpr(opCode);
			opCode.setNext(node1);
			return runList((ListNode)opCode);
		}
			

		return list;
	}

	private Node runQuote(ListNode node) {
		// QuoteNode의 value를 반환함
		QuoteNode qItem = (QuoteNode) node.value;
		Node item = qItem.value;

		return item;
	}

	public Node runExpr(Node rootExpr) {
		if (rootExpr == null)
			return null;

		if (rootExpr instanceof IdNode) {
			if (defineMap.map.containsKey(((IdNode) rootExpr).value))
				rootExpr = defineMap.map.get(((IdNode) rootExpr).value);

			return rootExpr;
		}
		else if (rootExpr instanceof IntNode)
			return rootExpr;
		else if (rootExpr instanceof BooleanNode)
			return rootExpr;
		else if (rootExpr instanceof ListNode)
			return runList((ListNode) rootExpr);
		else
			errorLog("run Expr error");

		return null;

	}

	private boolean checkQuote(Node node) {
		// QuoteNode의 형태인지 확인하는 메소드
		if (!(node instanceof ListNode))
			return false;
		ListNode tmp = (ListNode) node;
		if (!(tmp.value instanceof QuoteNode))
			return false;
		return true;
	}

	public static void main(String[] args) throws Exception {
		String userInput;
		Scanner scan = new Scanner(System.in);
		
		System.out.println("Welcome to Dyn's Interpreter !");
		System.out.println("Input exit, if you escape interpreter---------------");

		CuteInterpreter i = new CuteInterpreter();
		
		while(true){
			System.out.print("> ");
			userInput = scan.nextLine();
			System.out.print("...");
			if(userInput.equals("exit"))
				break;
			
			CuteScanner s = new CuteScanner(userInput);
			List<Token> result = s.tokenize();
			Parser p = new Parser(result);
			Node node = p.parseExpr();

			Printer pt = new Printer(System.out);
			
			Node resultNode = i.runExpr(node);
			pt.printNode(resultNode);
			System.out.println();
		}
		System.out.println("Good bye ~!");
	}
}