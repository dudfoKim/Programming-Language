/*
 * 과제 번호 : hw07
 * 분반 : 00분반
 * 소속 : 컴퓨터공학과
 * 학번 : 201000869
 * 이름 : 최준우
 */

package hw07_Reconizing_Token_u00;

import hw07_Reconizing_Token_u00.Node.Type;

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
		 * rhs1 list의 실행결과가 BooleanNode일 경우 결과 값이 True일 경우 False를 반환하고,
		 * 반대의 경우 True를 반환한다.
		 */
		case NOT:
			Node temp;
			if (rhs1 != null && (temp=runExpr(rhs1)) instanceof BooleanNode) { 	
				if(isTrueNode(runExpr(temp)))
					result = FALSE_NODE;
				else
					result = TRUE_NODE;
			}
			break; 
		// COND Function의 경우 runCompare를 통하여 조건-수행 pair를 run한다.
		case COND: 
			result=runCompare(rhs1);
			break; 

			/*
			 * 이번 과제에서는 구현하지 않는다.
			 */
		case DEFINE: 
			break; 
		case LAMBDA: 
			break; 

		default: 
			break; 

		} 

		return result; 
	}

	/*
	 * 첫 node가 이진 연산자일 경우 2개의 parameter를 runExpr을 통해 결과를 산출한 뒤,
	 * 각각에 해당하는 연산 수행을 한다.
	 */
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
				result = new IntNode(Type.NOT_QUOTED, ((IntNode)left).value - ((IntNode)right).value);
				break;
			case PLUS:
				result = new IntNode(Type.NOT_QUOTED, ((IntNode)left).value + ((IntNode)right).value);
				break;
			case TIMES:
				result = new IntNode(Type.NOT_QUOTED, ((IntNode)left).value * ((IntNode)right).value);
				break;
			case DIV:
				result = new IntNode(Type.NOT_QUOTED, ((IntNode)left).value / ((IntNode)right).value);
				break;
			//대소 비교 연산의 결과는 booleanNode가 된다.
			case LT:
				result = new BooleanNode(Type.NOT_QUOTED, ((IntNode)left).value < ((IntNode)right).value);
				break;
			case GT:
				result = new BooleanNode(Type.NOT_QUOTED, ((IntNode)left).value > ((IntNode)right).value);
				break;
				// 기타 연산자, MINUS와 비슷하게 각자 구현할 것
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
		//parameter가 TRUE_NODE인지 확인하여 참/거짓을 반환한다.
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
			if (!(pair instanceof ListNode))	{
				errorLog("runSingleCompare not List");// error ;; 조건-수행 쌍은 원소가 2개인 리스트 형태여야 한다.
				break;
			}

			condition = ((ListNode)pair).value;			
			// 리스트의 첫번째 원소는 조건 노드

			//조건을 수행하여 해당 결과가 True이면 조건 다음에 달린 수행문을 run한다.
			if (isTrueNode(runExpr(condition))){	//condition을 실행한 조건이 참일 때(isTrueNode…)
				Node action = runExpr(condition.getNext());
				// condition의 다음 원소는 수행 노드
				result = action;
				// result는 action의 수행결과

				break;	// 하나의 조건-수행 쌍만 수행
			}
			//거짓일 경우 다음 pair를 수행한다.
			pair = pair.getNext();	// 조건이 거짓 일때, 다음 조건-수행 쌍을 처리
		}

		return result;//수행 결과
	}


	//list가 들어 왔을 때 첫 node의 function을 실행한다.
	//이번 과제에서는 runBinaray를 추가하여 이진 연산을 수행한다.
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
		}else if (opCode instanceof BinarayOpNode){
			result = runBinaray((BinarayOpNode) opCode);
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
