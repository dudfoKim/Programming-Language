/*
 * 과제 번호 : hw06
 * 분반 : 00분반
 * 소속 : 컴퓨터공학과
 * 학번 : 201000869
 * 이름 : 최준우
 */

package hw06_Reconizing_Token_u00;

import hw06_Reconizing_Token_u00.Node.Type;

public class CuteInterpreter { 
	private void errorLog(String err) { 
		System.out.println(err); 
	} 

	//booleanNode 정의
	private final static BooleanNode TRUE_NODE = new BooleanNode( 
			Type.NOT_QUOTED, true); 
	private final static BooleanNode FALSE_NODE = new BooleanNode( 
			Type.NOT_QUOTED, false); 

	//copymode정의
	enum CopyMode { 
		NO_NEXT, NEXT 
	} 

	/*
	 * 해당 node를 복사한다.
	 * copymode를 parameter로 받아 no_next일 경우 해당 node의 처음 node만 복사하고
	 * next일 경우 list를 전체를 복사한다.
	 */
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
			result = new FunctionNode(node.getType(), 
					((FunctionNode) node).value); 
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

	/*
	 * Function node를 읽어 해당 function을 작동한다.
	 */
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
		//list의 처음 node를 복사한다.
		//copymode를 no_next로 복사하여 처음 node만 result로 반환한다.
		case CAR: 
			if (rhs1 instanceof ListNode) { 
				Node item = ((ListNode) rhs1).value; 
				if (null != item) { 
					result = copyNode(item, CopyMode.NO_NEXT); 
					// item의 copy 본을 넘긴다. 
					// next는 복사하지 않음 
				} else { 
					errorLog("runCAR first NULL"); 
				} 
			} else { 
				errorLog("runCAR tag Error");// error를 리턴 
			} 
			break; 

			/*
			 * rhs1이 listNode일 경우 해당 node의 2번째 node를 head로 하는 result를 반환한다.
			 */
		case CDR: 
			if (rhs1 instanceof ListNode) { 
				Node firstItem = ((ListNode) rhs1).value; 

				if (firstItem != null) { 
					result = new ListNode(rhs1.type,firstItem.next); 
					//전체 리스트(rhs1)의 타입을 받고, 다음원소가 원소인 새로운 리스트 만듬 
				} else { 
					errorLog("runCDR first Error"); 
					// error 를 리턴: () 에서 cdr를 찾는 것이므로 
				} 
			} else { 
				errorLog("runCDR error"); // error를 리턴 
			} 
			break; 

			/*
			 * rhs1의 next에 tail의 value를 붙인다.
			 * 그리고 rhs1을 head로 갖는 list node를 만들어 반환한다.
			 */
		case CONS: 
			if (rhs1 == null) { 
				errorLog("runCons param error");// return error 
				break; 
			} 

			Node head = rhs1; 
			Node tail = rhs2; 

			if (tail instanceof ListNode) { 

				head.next=((ListNode) tail).value;

				result = new ListNode(tail.type, head);

				// tail의 타입을 가지고 head가 원소인 리스트를 만들고 
				// head와 tail을 연결 
			} else { 
				errorLog("runCons tail not list");// return error; 
			} 

			break; 

			/*
			 * rhs1이 listNode일 때, list가 비어있지 않으면 false
			 * 이 외의 경우는 모두 true
			 */
		case ATOM_Q: 
			/* 각자 구현: 
		if data가 atom이면 TRUE_NODE
		else if List노드가 null인 list 면 TRUE_NODE
		else 아니면 FALSE_NODE 
			 */
			if(rhs1 instanceof ListNode)
			{
				if(((ListNode)rhs1).value==null)
					result=TRUE_NODE;
				else
					result=FALSE_NODE;
			}
			else
				result=TRUE_NODE;
			break; 

			//각각의 node에 정의된 따라 equal을 호출하여 T/F를 판별하여 반환한다.
		case EQ_Q: 
			if (rhs1 != null && rhs1.equals( rhs2 )) { 
				// 각 노드 타입마다 equals가 구현되어야 함 
				result = TRUE_NODE; 
			} else { 
				result = FALSE_NODE; 
			} 
			break; 

			/*
			 * rhs1이 비어 있다면 True
			 * 이 외의 경우는 False를 반환한다.
			 */
		case NULL_Q: 
			/* 각자 구현: 
		if rhs1의 sublist 가 NULL이면 TRUE_NODE 
		else FALSE_NODE 
			 */ 
			if(rhs1 instanceof ListNode)
			{
				if(((ListNode)rhs1).value == null)
				{
					result=TRUE_NODE;
					break;
				}
			}
			result=FALSE_NODE;
			break; 

			/*
			 * 이번 과제에서는 구현하지 않는다.
			 */
		case NOT: 
			break; 
		case COND: 
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
	//list가 들어 왔을 때 첫 node의 function을 실행한다.
	private Node runList(ListNode list) { 
		// quote 되었으면 그대로 사용 
		if (list.getType() == Type.QUOTED) 
			return list; 

		Node result = null; 
		Node opCode = list.value; 

		if (opCode == null) 
			return list; // () 

		
		if (opCode instanceof FunctionNode) { 
			result = runFunction((FunctionNode) opCode); 
		}
		return result;
	} 

	
	/*
	 * ID,INT,BOOLEAN에 대해서는 해당 node를 그대로 반환한다.
	 * LIST에 대해서는 run list를 실행하여 해당 line의 명령을 실행한다. 
	 */
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

}
