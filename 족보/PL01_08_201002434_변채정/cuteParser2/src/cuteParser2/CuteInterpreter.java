package cuteParser2;


public class CuteInterpreter {
	private final static BooleanNode TRUE_NODE = new BooleanNode(cuteParser2.Node.Type.NOT_QUOTED, true);
	private final static BooleanNode FALSE_NODE = new BooleanNode(cuteParser2.Node.Type.NOT_QUOTED, false);
	enum CopyMode {
		NO_NEXT, NEXT
	}
	private void errorLog(String err) {
		System.out.println(err);
	}
	private Node copyNode(Node node, CopyMode mode) {
		if (node == null)
			return null;
		Node result = null;
		if (node instanceof BinarayOpNode) {
			result = new BinarayOpNode(node.getType(),
					((BinarayOpNode) node).value);
		} else if (node instanceof BooleanNode) {
			result = new BooleanNode(node.getType(), ((BooleanNode) node).value);
		} else if (node instanceof FunctionNode) {
			result = new FunctionNode(node.getType(),((FunctionNode) node).value);
		} else if (node instanceof IdNode) {
			result = new IdNode(node.getType(), ((IdNode) node).value);
		} else if (node instanceof IntNode) {
			result = new IntNode(node.getType(), ((IntNode) node).value);
		} else if (node instanceof ListNode) {
			result = new ListNode(node.getType(), copyNode(
					((ListNode) node).value, CopyMode.NEXT));
		}
		if (mode == CopyMode.NEXT && result != null)
			result.setNext(copyNode(node.getNext(), mode));
		return result;
	}
	private Node runFunction(FunctionNode func) {
		/*
			func가 car, cdr, nullQ, atomQ 이면
			다음 원소를 인자로 하여 해당 함수 구현
			func가 cons, eqQ 이면
			다음원소와 그 다음 원소를 인자로 하여 해당 함수 구현
		 */
		Node result = null;
		Node rhs1 = func.getNext();
		Node rhs2 = (rhs1 != null) ? rhs1.getNext() : null;
		switch (func.value) {
		case CAR:
			if (rhs1 instanceof ListNode) {
				Node item = ((ListNode) rhs1).value;
				if (null != item) {
					result = copyNode(item,null);
					// item의 copy 본을 넘긴다.
					// next는 복사하지 않음
				} else {
					errorLog("runCAR first NULL");
				}
			} else {
				errorLog("runCAR tag Error");// error를 리턴
			}
			break;
		case CDR:
			if (rhs1 instanceof ListNode) {
				Node firstItem = ((ListNode) rhs1).value;
				if (firstItem != null) {
					result = new ListNode(((ListNode)rhs1).type, firstItem.next);
					//전체 리스트(rhs1)의 타입을 받고, 다음원소가 원소인 새로운 리스트 만듬
				} else {
					errorLog("runCDR first Error");
					// error 를 리턴: () 에서 cdr를 찾는 것이므로
				}
			} else {
				errorLog("runCDR error"); // error를 리턴
			}
			break;
		case CONS:
			if (rhs1 == null) {
				errorLog("runCons param error");// return error
				break;
			}
			Node head = rhs1;
			Node tail = rhs2;
			if (tail instanceof ListNode) {
				result = new ListNode(head.type, head);
				// tail의 타입을 가지고 head가 원소인 리스트를 만들고
				// head와 tail을 연결
			} else {
				errorLog("runCons tail not list");// return error;
			}
			break;
		case ATOM_Q:
			if( rhs1.getClass() != ListNode.class ){
				result = TRUE_NODE;
			}
			// list is null
			else if( ((ListNode)rhs1).value == null){
				result = TRUE_NODE;
			}
			else
				result = FALSE_NODE;
			/* 각자 구현:
			if data가 atom이면 TRUE_NODE
else if List노드가 null인 list 면 TRUE_NODE
else 아니면 FALSE_NODE
			 */
			break;
		case EQ_Q:
			if (rhs1 != null && rhs1.equals( rhs2 )) {
				// 각 노드 타입마다 equals가 구현되어야 함
				result = TRUE_NODE;
			} else {
				result = FALSE_NODE;
			}
			break;
		case NULL_Q:
			if (rhs1 instanceof ListNode) {
				Node itemm = ((ListNode) rhs1).value;
				if (null == itemm) {
					result=TRUE_NODE;}
				else
					result = FALSE_NODE;
			}
			/* 각자 구현:
if rhs1의 sublist 가 NULL이면 TRUE_NODE
else FALSE_NODE
			 */
			break;
		case NOT:
	
			if(isTrueNode(rhs1)==true)
				result =FALSE_NODE;
			else if(isTrueNode(rhs1)==false)
				result = TRUE_NODE;
			break;
		case COND:
			result = runCompare(rhs1);
			break;
		case DEFINE:
			break;
		case LAMBDA:
			break;
		default:
			break;
		}
		return result;
	}
	private Node runList(ListNode list) {
		// quote 되었으면 그대로 사용
		if (list.getType() ==cuteParser2.Node.Type.QUOTED)
		return list;
		Node result = null;
		Node opCode = list.value;
		if (opCode == null)
		return list; // ()
		if (opCode instanceof FunctionNode) {
		result = runFunction((FunctionNode) opCode);
		}else if (opCode instanceof BinarayOpNode){
		result = runBinaray((BinarayOpNode) opCode);
		}
		return result;
		}
	public Node runExpr(Node rootExpr) {
		if(rootExpr == null)
			return null;
		Node result = null;
		if (rootExpr instanceof IdNode)
			result = rootExpr;
		else if (rootExpr instanceof IntNode)
			result = rootExpr;
		else if (rootExpr instanceof BooleanNode)
			result = rootExpr;
		else if (rootExpr instanceof ListNode)
			result = runList((ListNode) rootExpr);
		else
			errorLog("run Expr error");
		return result;
	}

	private Node runBinaray(BinarayOpNode node) {
		Node result = null;
		Node left = node.getNext();
		Node right = left.getNext();
		left = runExpr(left);
		right = runExpr(right);
		if (left== null || right == null )
			errorLog("runBinary runExpr null");// error
		if (left instanceof IntNode && right instanceof IntNode){
			switch(node.value){
			case MINUS:
				result = new IntNode(cuteParser2.Node.Type.NOT_QUOTED, ((IntNode)left).value - ((IntNode)right).value);
				break;
			case PLUS:
				result = new IntNode(cuteParser2.Node.Type.NOT_QUOTED, ((IntNode)left).value + ((IntNode)right).value);
				break;
			case TIMES:
				result = new IntNode(cuteParser2.Node.Type.NOT_QUOTED, ((IntNode)left).value * ((IntNode)right).value);
				break;
			case DIV:
				result = new IntNode(cuteParser2.Node.Type.NOT_QUOTED, ((IntNode)left).value / ((IntNode)right).value);
				break;
			case LT:
				if((( (IntNode)left ).value < ( (IntNode)right).value))
					result = TRUE_NODE;
				else
					result = FALSE_NODE;
				break;
			case EQ:
				if((( (IntNode)left ).value == ( (IntNode)right).value))
					result = TRUE_NODE;
				else
					result = FALSE_NODE;
				break;
			case GT:	
				if((( (IntNode)left ).value > ( (IntNode)right).value))
					result = TRUE_NODE;
				else
					result = FALSE_NODE;
				break;
				// 관계연산은 TRUE, FALSE반환
			default:
				break;
			}
		}else
			errorLog("runBinaray not int");// error
		return result;
	}

	// 참거짓을 판별하는 utility 함수
	private boolean isTrueNode(Node parameter){
		return TRUE_NODE.equals(parameter);
	}
	// 다음은 (cond (condition1 result1) (condition2 result2) … (#T resultN))에서
	// 여러 조건-수행 쌍 중, 조건이 true인 하나의 쌍을 수행하는 함수
	private Node runCompare(Node parameter) {
		Node condition = null;
		Node pair = parameter;// 조건-수행 쌍 중 제일 처음 것;
		Node result = null;
		// 각 조건-수행 쌍마다 처리하는 loop
		while (pair != null){// 더 이상 조건-수행 쌍이 없을 때까지 계속 수행
			if (!(pair instanceof ListNode)) {
				errorLog("runSingleCompare not List");// error ;; 조건-수행 쌍은 원소가 2개인 리스트 형태여야 한다.
				break;
			}
			condition = ((ListNode)pair).value;
			// 리스트의 첫번째 원소는 조건 노드
			if (isTrueNode(runExpr(condition))){ //condition을 실행한 조건이 참일 때(isTrueNode…)
				Node action = condition.next;
						// condition의 다음 원소는 수행 노드
						result = runExpr(action);
						// result는 action의 수행결과
						break; // 하나의 조건-수행 쌍만 수행
			}else{
				// 조건이 거짓 일때, 다음 조건-수행 쌍을 처리
				result = runExpr(condition);
				pair = pair.next; 
			}
		}
		return result;//수행 결과
	}
}
