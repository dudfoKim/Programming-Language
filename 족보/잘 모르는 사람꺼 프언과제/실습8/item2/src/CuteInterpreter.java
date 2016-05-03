/*
 * 과제번호 : PL08-2
 * 분반 : 02분반
 * 조 번호 : 15
 * 조원 소속 : 충남대학교 공과대학 컴퓨터공학과
 * 학번 및 성명 : 201002384(김범철) 201002436(성동욱)
 */

package cute14ProjectItem2;

import java.util.ArrayList;
//

public class CuteInterpreter {
	
	private static ArrayList<defineNode> li = new ArrayList<defineNode>();
	// import 한 ArrayList를 이용하여 defineNode 클래스형 전역변수 li를 선언한다.
	
	private final static BooleanNode TRUE_NODE = new BooleanNode(
			Node.Type.NOT_QUOTED, true);
	private final static BooleanNode FALSE_NODE = new BooleanNode(
			Node.Type.NOT_QUOTED, false);

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

	private Node runFunction(FunctionNode func) {
		/*
		 * func가 car, cdr, nullQ, atomQ 이면 다음 원소를 인자로 하여 해당 함수 구현 func가 cons,
		 * eqQ 이면 다음원소와 그 다음 원소를 인자로 하여 해당 함수 구현
		 */
		int i, flag = 0;
		// flag는 각 조건에 따른 걸러내기 위함으로 사용된다.
		Node result = null;
		Node rhs1 = func.getNext();
		Node rhs2 = (rhs1 != null) ? rhs1.getNext() : null;

		if (func.value != FunctionNode.FunctionType.DEFINE) {
			if (rhs1 != null) {
				//인자로 받아온 functionnode의 func의 value type이 define이 아니면서
				//다음 노드가 비어있지 않다면,
				for (i = 0; i < li.size(); i++) {
					//define node의 사리즈만큼 for문 반복,
					if (li.get(i).getTempString().equals(rhs1.toString()))
						//리스트내 에서 equals메서드를 통해 tostring으로 변환된 값과
						//동일한 값을 가지는 경우엔
						rhs1 = li.get(i).getTempNode();
						//define node의 리스트에서 값을 받아와 func의 다음부분으로 넣는다.
				}
			}

			if (rhs2 != null) {
				// 다음 값이 비어있지않다면
				for (i = 0; i < li.size(); i++) {
					// 위 코드와 비슷. 다만 rhs2 string과 비교한다는것 뿐
					if (li.get(i).getTempString().equals(rhs2.toString()))
						rhs2 = li.get(i).getTempNode();
				}
			}
		}

		// if(rhs1!=null)

		if (rhs2 != null) {
			if (rhs2.getType() == Node.Type.NOT_QUOTED
					&& (!(rhs2 instanceof IntNode) && !(rhs2 instanceof BooleanNode)))
				// 인자값이 not quoted면서 intNode가 아니고  booleanNode가 아닐경우
				rhs2 = runExpr(rhs2);
				//rhs2에 대해 runExpr내부 인자로 넣어 작동시킨다.
		}
		
		// 여기서부턴 예제에 올라온(+ elearning 사이트 예제, 조건포함) 타입오류를 걸러내기 위해 작성된 코드이다.
		if (rhs1 != null)
		{
			if(func.value == FunctionNode.FunctionType.DEFINE)
			{	//rhs1이 비지 않았고, func의 value가 define인 경우
				if (rhs1.getType() == Node.Type.NOT_QUOTED	&& (!(rhs1 instanceof IntNode)
							&& !(rhs1 instanceof BooleanNode) && !(rhs1 instanceof IdNode)))
				{
					//이 경우엔 rhs1이 not_quoted면서
					//int노드가 아니고, boolean노드가 아니고, id노드도 아니면 타입에러인 경우이므로 
					//에러로그로 타입에러를 출력하고, flag 값을 변화시켜줌으로서 아래 코드가 실행되지 않게 한다.
				errorLog("type error");
				flag = 1;
				}
			}
			
			else //이 경우엔 define이 아닌경우에 해당한다.
				if (rhs1.getType() == Node.Type.NOT_QUOTED
				&& (!(rhs1 instanceof IntNode)	&& !(rhs1 instanceof BooleanNode)))
				{
			errorLog("type error");
			flag = 1;
			}
		}

		
		
		if (rhs2 != null) {
			if (rhs2.getType() == Node.Type.NOT_QUOTED
					&& (!(rhs2 instanceof IntNode) && !(rhs2 instanceof BooleanNode)))
			{ //rhs2의 인자로 들어오는 값이 not_quoted이면서 int나 boolean 노드형태가 아닐경우
				//타입에러로 걸러진다.
				errorLog("type error");
				flag = 1;
			}
		}

		if (flag == 0) { //에러로 검출되지 않은 경우 다음의 코드가 실행이 된다.(flag로 판별)
			switch (func.value) {
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
			case CDR:
				if (rhs1 instanceof ListNode) {
					Node firstItem = ((ListNode) rhs1).value;
					if (firstItem != null) {
						result = new ListNode(rhs1.type, firstItem.next);
						// 전체 리스트(rhs1)의 타입을 받고, 다음원소가 원소인 새로운 리스트 만듬
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
				head.setType(Node.Type.NOT_QUOTED);
				tail.setType(Node.Type.NOT_QUOTED);
				// 헤드와 tail의 경우 not_quoted로 변환하는데 cons로 묶어질때
				// 외부(괄호)에 quoted가 붙으면서 내부 quoted는 필요가 없으므로 코드를 작성함.
				// (printer클래스에서 quoted를 판별하여 ' 를 붙이므로)
				
				if (tail instanceof ListNode) {
					head.setNext(((ListNode) tail).value);
					result = new ListNode(tail.type, head);
					// tail의 타입을 가지고 head가 원소인 리스트를 만들고
					// head와 tail을 연결
				} else {
					errorLog("runCons tail not list");// return error;
				}
				break;
			case ATOM_Q:
				if (rhs1.getClass() != ListNode.class)
					result = TRUE_NODE;
				else if (rhs1 instanceof ListNode
						&& ((ListNode) rhs1).value == null)
					result = TRUE_NODE;
				else
					result = FALSE_NODE;
				/*
				 * 각자 구현: if data가 atom이면 TRUE_NODE else if List노드가 null인 list 면
				 * TRUE_NODE else 아니면 FALSE_NODE
				 */
				break;
			case EQ_Q:
				if (rhs1 != null && rhs1.equals(rhs2)) {
					// 각 노드 타입마다 equals가 구현되어야 함
					result = TRUE_NODE;
				} else {
					result = FALSE_NODE;
				}
				break;
			case NULL_Q:
				if (((ListNode) rhs1).value == null)
					result = TRUE_NODE;
				else
					result = FALSE_NODE;
				/*
				 * 각자 구현: if rhs1의 sublist 가 NULL이면 TRUE_NODE else FALSE_NODE
				 */
				break;
			case NOT:
				Node temp = new BooleanNode(Node.Type.NOT_QUOTED, false);
				// 임의의 비교노드 temp를 만들어 false일경우 true노드 반환.
				if (rhs1.equals(temp))
					result = TRUE_NODE;
				else
					result = FALSE_NODE;
				// 구현
				break;
			case COND:
				result = runCompare(rhs1);
				break;
			case DEFINE:
				if (rhs1 instanceof IdNode) {
					//rhs1 은 id노드로 정의하는것이므로 노드판별
					if (rhs2 instanceof IntNode || rhs2 instanceof ListNode) {
						// 두번째 인자로 들어가는 것은 intnode이거나 list로 이루어진 () 내부 형태이어야 한다.
						insertTable(rhs1.toString(), rhs2);
						// insertTable 메서드의 인자로 넣어준다.
						result = TRUE_NODE;
					} else
						result = FALSE_NODE;
				}

				else
					result = FALSE_NODE;

				break;

			case LAMBDA:
				break;

			default:
				break;
			}
		}
		return result;
	}

	private Node runList(ListNode list) {
		// quote 되었으면 그대로 사용
		int i;
		Node result = null;
		Node opCode = list.value;

		if (list.getType() == Node.Type.QUOTED)
			return list;

		if (opCode == null)
			return list;

		if (opCode instanceof FunctionNode) {
			result = runFunction((FunctionNode) opCode);
		} else if (opCode instanceof BinarayOpNode) {
			result = runBinaray((BinarayOpNode) opCode);
		}
		
		//리스트의 value형이 IDNode형태일경우,
		else if (opCode instanceof IdNode) {
			for (i = 0; i < li.size(); i++) {
				//리스트의 사이즈만큼 반복(검색)
				if (li.get(i).getTempString().equals(opCode.toString()))
					result = li.get(i).getTempNode();
				///list의 opcode의 값과 리스트의 값과 같은것이 발견될 경우 결과값을 리스트의 tempnode에서 받아온다.
			}
		}

		if (result != null)
			result.setType(Node.Type.QUOTED);

		return result;
	}

	public Node runExpr(Node rootExpr) {
		if (rootExpr == null)
			return null;
		Node result = null;

		if (rootExpr instanceof IdNode) {
			result = rootExpr;
			result.setType(Node.Type.QUOTED);
		} else if (rootExpr instanceof IntNode) {
			result = rootExpr;
			result.setType(Node.Type.QUOTED);
		} else if (rootExpr instanceof BooleanNode) {
			result = rootExpr;
			result.setType(Node.Type.QUOTED);
		} else if (rootExpr instanceof ListNode) {
			result = runList((ListNode) rootExpr);
		} else
			errorLog("run Expr error");

		return result;
	}

	private Node runBinaray(BinarayOpNode node) {
		int i;
		Node result = null;
		Node left = node.getNext();
		Node right = left.getNext();
		left = runExpr(left);
		right = runExpr(right);

		if (left == null || right == null)
			errorLog("runBinary runExpr null");// error

		for (i = 0; i < li.size(); i++) {
			if (li.get(i).getTempString().equals(left.toString()))
				left = li.get(i).getTempNode();
		}

		for (i = 0; i < li.size(); i++) {
			if (li.get(i).getTempString().equals(right.toString()))
				right = li.get(i).getTempNode();
		}
		// 위 있었던 연산과 같은 부분이다. li가 결국 lookuptable 메서드 역할을 담당하는 것.
		// left값, 리스트값과 동일한 값을 노드에서 빼와 저장하고, 오른쪽 역시 같다.

		if (left instanceof IntNode && right instanceof IntNode) {
			// 각각의 연산에 맞게 부호 지정
			switch (node.value) {
			case MINUS:
				result = new IntNode(Node.Type.NOT_QUOTED,
						((IntNode) left).value - ((IntNode) right).value);
				break;
			case PLUS:
				result = new IntNode(Node.Type.NOT_QUOTED,
						((IntNode) left).value + ((IntNode) right).value);
				break;
			case TIMES:
				result = new IntNode(Node.Type.NOT_QUOTED,
						((IntNode) left).value * ((IntNode) right).value);
				break;
			case DIV:
				result = new IntNode(Node.Type.NOT_QUOTED,
						((IntNode) left).value / ((IntNode) right).value);
				break;
			case LT:
				if (((IntNode) left).value < ((IntNode) right).value)
					result = TRUE_NODE;
				else
					result = FALSE_NODE;
				break;
			case GT:
				if (((IntNode) left).value > ((IntNode) right).value)
					result = TRUE_NODE;
				else
					result = FALSE_NODE;
				break;
			case EQ:
				if (((IntNode) left).value == ((IntNode) right).value)
					result = TRUE_NODE;
				else
					result = FALSE_NODE;
				// 기타 연산자, MINUS와 비슷하게 각자 구현할 것
				// 관계연산은 TRUE, FALSE반환
				break;
			default:
				break;
			}
		} else
			errorLog("runBinaray not int");// error
		return result;
	}

	// 참거짓을 판별하는 utility 함수
	private boolean isTrueNode(Node parameter)
	{
		return TRUE_NODE.equals(parameter);
	}

	// 다음은 (cond (condition1 result1) (condition2 result2) … (#T resultN))에서
	// 여러 조건-수행 쌍 중, 조건이 true인 하나의 쌍을 수행하는 함수
	private Node runCompare(Node parameter) {
		Node condition = null;
		Node pair = parameter;// 조건-수행 쌍 중 제일 처음 것;
		Node result = null;
		// 각 조건-수행 쌍마다 처리하는 loop
		while (pair != null) {// 더 이상 조건-수행 쌍이 없을 때까지 계속 수행
			if (!(pair instanceof ListNode)) {
				errorLog("runSingleCompare not List");// error ;; 조건-수행 쌍은 원소가
														// 2개인 리스트 형태여야 한다.
				break;
			}

			condition = ((ListNode) pair).value;
			// condition은 pair중 첫번째 node
			// 따라서 condition의 next는 첫번째 node의 조건에 따라서 다음 pair로 넘어가거나 result에 들어가
			// return됨.
			if (isTrueNode(runExpr(condition))) { // condition을 실행한 조건이 참일
													// 때(isTrueNode…)
													// 따라서 condition이 참이면
													// action에 if이 수행됐을시 결과를 넣고
													// 리턴시키기위해 result에 action을
													// 넣는다.
				Node action = condition.next;
				result = action;
				break;
			}
			pair = pair.next; // 조건이 거짓 일때, 다음 조건-수행 쌍을 처리
		}

		return result;// 수행 결과

	}

	private void insertTable(String ID, Node value) {
		//case define에서 인자로 넘겼던 부분이다. 리스트 뿐만 아니라 값도 정의하는데 쓰임.
		int i, count = 0;
		defineNode temp = new defineNode(ID, value);
		//node value로 넘어온 인자가 definenode의 tempnode부분을 담당하게 된다.

		for (i = 0; i < li.size(); i++)
		{//이 역시 list인 lookuptable역할을 하는데, 
			if (li.get(i).getTempString().equals(ID.toString())) 
			{
				li.get(i).setTempNode(value);
				count++;
			}//tempstring과(인자로 받아온 value부분) String ID가 equal이라면(예전에 정의된 적이있다면)
			//settempnode를 이용하여 그 겹치는 부분(정의되었었던 부분)에 새로운 값으로 덮어씌운다.
			//카운트를 추가시켜줌으로서 밑의 코드와 중복되는 일이 없게한다.
		}

		if (count == 0)
			//일치하는 부분이 없다면(count가 0이란말은 예전에 정의된 부분이 없었다는 소리)
			li.add(temp);
		//전역변수로 선언된 li부분에 id와 value를 인자로 받은 temp를 새로 리스트에 추가시킨다.
	}

}
