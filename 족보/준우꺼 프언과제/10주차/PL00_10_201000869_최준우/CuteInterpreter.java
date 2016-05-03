/*
 * 과제 번호 : hw09
 * 분반 : 00분반
 * 소속 : 컴퓨터공학과
 * 학번 : 201000869
 * 이름 : 최준우
 */

package hw10_Reconizing_Token_u00;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import hw10_Reconizing_Token_u00.FunctionNode.FunctionType;
import hw10_Reconizing_Token_u00.Node.Type;

public class CuteInterpreter { 

	/*
	 * Define을 정의하기 위한 symbol table을 구현한다.
	 * Map구조를 이용하여 String과 Node를 mapping하는 table을 구현한다.
	 * 이번 과제에서는 parameter의  처리를 위하여 arrayList를 선언하여 지역 함수의 Stack 환경을 구현한다.
	 */
	private Map<String ,Node> _table;
	
	public CuteInterpreter()
	{
		//Map Interface를 HashMap으로 구현한다.
		this._table = new HashMap<String, Node>();
		
	}

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
	 * 이번 과제에서는 Define된 String node가 있을 수 있으므로
	 * not quoted된 node는 runExpr로 연산을 수행하여 결과를 얻는다.
	 */
	private Node runFunction(FunctionNode func, ListNode list) { 

		Node result = null; 
		Node rhs1 = func.getNext(); 
		Node rhs2 = (rhs1 != null) ? rhs1.getNext() : null;

		if(rhs1 == null)
			return list;
		/*
		 * rhs1과 rhs2가 not quoted일 경우 runExpr로 연산을 수행한다.
		 * 그렇지 않은 경우는 그 값 그대로 left와 right에 대입하여 사용된다.
		 */
		Node left = rhs1;
		Node right = rhs2;

		if(!func.value.equals(FunctionType.LAMBDA))
		{
			if(rhs1 != null && rhs1.type.equals(Type.NOT_QUOTED))
				left = runExpr(rhs1);
			if(rhs2 != null && rhs2.type.equals(Type.NOT_QUOTED))
				right = runExpr(rhs2);
		}



		switch (func.value) { 
		//list의 처음 node를 복사한다.
		//copymode를 no_next로 복사하여 처음 node만 result로 반환한다.
		case CAR: 
			if (left instanceof ListNode) { 
				Node item = ((ListNode) left).value; 
				if (null != item) { 
					result = copyNode(item, CopyMode.NO_NEXT); 
					result.setType(Type.QUOTED);
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
			 * left이 listNode일 경우 해당 node의 2번째 node를 head로 하는 result를 반환한다.
			 */
		case CDR: 
			if (left instanceof ListNode) { 
				Node firstItem = ((ListNode) left).value; 

				if (firstItem != null) { 
					result = new ListNode(left.type,firstItem.next); 
					result.setType(Type.QUOTED);
					//전체 리스트(left)의 타입을 받고, 다음원소가 원소인 새로운 리스트 만듬 
				} else { 
					errorLog("runCDR first Error"); 
					// error 를 리턴: () 에서 cdr를 찾는 것이므로 
				} 
			} else { 
				errorLog("runCDR error"); // error를 리턴 
			} 
			break; 

			/*
			 * left의 next에 tail의 value를 붙인다.
			 * 그리고 left을 head로 갖는 list node를 만들어 반환한다.
			 */
		case CONS: 
			if (left == null) { 
				errorLog("runCons param error");// return error 
				break; 
			} 

			Node head = left; 
			Node tail = right; 

			if (tail instanceof ListNode) { 
				if(tail.type.equals(Type.QUOTED))
				{
					head.next=((ListNode) tail).value;

					result = new ListNode(tail.type, head);

					result.setType(Type.QUOTED);
				}
				else {
					errorLog("runCons tail not Quote");
				}
				// tail의 타입을 가지고 head가 원소인 리스트를 만들고 
				// head와 tail을 연결 
			} else { 
				errorLog("runCons tail not list");// return error; 
			} 

			break; 

			/*
			 * left이 listNode일 때, list가 비어있지 않으면 false
			 * 이 외의 경우는 모두 true
			 */
		case ATOM_Q: 
			/* 각자 구현: 
		if data가 atom이면 TRUE_NODE
		else if List노드가 null인 list 면 TRUE_NODE
		else 아니면 FALSE_NODE 
			 */
			if(left instanceof ListNode)
			{
				if(((ListNode)left).value==null)
					result=TRUE_NODE;
				else
					result=FALSE_NODE;
			}
			else
				result=TRUE_NODE;
			break; 

			//각각의 node에 정의된 따라 equal을 호출하여 T/F를 판별하여 반환한다.
		case EQ_Q: 
			if (left != null && left.equals( right )) { 
				// 각 노드 타입마다 equals가 구현되어야 함 
				result = TRUE_NODE; 
			} else { 
				result = FALSE_NODE; 
			} 
			break; 

			/*
			 * left이 비어 있다면 True
			 * 이 외의 경우는 False를 반환한다.
			 */
		case NULL_Q: 
			/* 각자 구현: 
		if left의 sublist 가 NULL이면 TRUE_NODE 
		else FALSE_NODE 
			 */ 
			if(left instanceof ListNode)
			{
				if(((ListNode)left).value == null)
				{
					result=TRUE_NODE;
					break;
				}
			}
			result=FALSE_NODE;
			break; 

			/*
			 * left list의 실행결과가 BooleanNode일 경우 결과 값이 True일 경우 False를 반환하고,
			 * 반대의 경우 True를 반환한다.
			 */
		case NOT:
			Node temp;
			if (left != null && (temp=runExpr(left)) instanceof BooleanNode) { 	
				if(isTrueNode(runExpr(temp)))
					result = FALSE_NODE;
				else
					result = TRUE_NODE;
			}
			break; 
			// COND Function의 경우 runCompare를 통하여 조건-수행 pair를 run한다.
		case COND: 
			result=runCompare(left);
			break; 

			/*
			 * define function을 정의한다.
			 * HashMap으로 구현된 Table에 <key, value> 쌍으로
			 * <Id, Node>를 Mapping하여 Map에 put한다.
			 */
		case DEFINE:
			if(left instanceof IdNode && left.type.equals(Type.QUOTED))
			{
				if(right instanceof Node )//&& (right instanceof BooleanNode || right instanceof IntNode ) || right.type.equals(Type.QUOTED)
				{
					this.insertTable(((IdNode)left).value, right);
					result = TRUE_NODE;
					break;
				}
				else if(right instanceof ListNode)
				{
					if(right.type.equals(Node.Type.QUOTED))
					{
						this.insertTable(((IdNode)left).value, right);
						result = TRUE_NODE;
						break;
					}
				}
			}

			result = FALSE_NODE;
			break; 
			
		/*
		 * user defined function을 symbol table에 저장한다. 선언된 parameter에 실인자 값을 binding한다.
		 */
		case LAMBDA:
			//함수의 지역변수를 저장하기 위해 arrayList를 구현한다.
			ArrayList<String> arrayList1=new ArrayList<String>();
			ArrayList<Node> arrayList2=new ArrayList<Node>();
			//i는 실인자 값의 수이고 j는 parameter의 수이다.
			int i=0, j=0;
			Node tempString = null;
			Node tempNext=list.getNext();
			//lambda의 rhs1은 parameter를 나타내는 list이다.
			//j로 parameter의 수를 카운트한다.
			if(left instanceof ListNode)
			{
				tempString=((ListNode)left).value;
				while(tempString != null)
				{
					j++;
					tempString = tempString.next;
				}

				if(((ListNode)left).value instanceof IdNode)
					tempString=((ListNode)left).value;
			}
			//parameter를 key값으로 실인자 값을 해쉬 테이블에 삽입하고,
			//arrayList에 add하여 stack을 유지한다.
			
			for(i=0;tempNext!=null;i++)
			{
				this.insertTable(((IdNode)tempString).value, copyNode(runExpr(tempNext),CopyMode.NO_NEXT));
				arrayList1.add(((IdNode)tempString).value);
				arrayList2.add(copyNode(runExpr(tempNext),CopyMode.NO_NEXT));
				tempString = tempString.next;
				tempNext = tempNext.next;
			}
			//만약 실인자 수가 0이면 lambda의 정의이므로 list 자체를 반환한다.
			if(i==0)
			{
				result = list;
				break;
			}
			//실인자를 계산하기 위해 사용되었던 parameter의 값을 원상 복구 시킨다.
			while(arrayList1.size()!=0)
				this.insertTable(arrayList1.remove(arrayList1.size()-1), arrayList2.remove(arrayList2.size()-1));
			//함수 body 부분을 연산하여 result 값을 낸다.
			result = runExpr(rhs2);
			
			//현 함수 stack의 지역변수를 삭제한다.
			tempString=((ListNode)left).value;
			for(int t=0;t<((i<j)?i:j);t++)
			{
				this._table.remove(((IdNode)tempString).value);
				tempString = tempString.next;
			}

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
	private Node runBinaray(BinarayOpNode node, ListNode list) {
		Node result = null;
		Node left = node.getNext();
		if(left==null)
			return list;
		
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
			result = runFunction((FunctionNode) opCode, list);
		}else if (opCode instanceof BinarayOpNode){
			result = runBinaray((BinarayOpNode) opCode, list);
		//lambda로 정의된 idNode에 대해 새List를 만들어 runExpr을 수행하여 lambda 함수에 대한 result를 얻는다.
		}else if (opCode instanceof IdNode){
			if(this._table.containsKey(((IdNode) opCode).value) && opCode.type.equals(Type.NOT_QUOTED)){
				if(runExpr(opCode) instanceof ListNode && ((ListNode)runExpr(opCode)).value instanceof FunctionNode){
					ListNode temp = new ListNode( Type.NOT_QUOTED,runExpr(opCode));
					
					temp.value.next = opCode.next;
					
					result = runExpr(temp);
				}
			//이외의 IdNode는 list 자체를 반환한다.
				else
					result = list;
			}
			else
				result = list;
		}
		else
		{
			result = runExpr(opCode);
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

		/*
		 * 이번 과제에서는 Id값이 Mapping되어 있는 Node를 사용하기 위해
		 * result에 Id를 Key로 table에 contain되 있는지 확인하고 table에서 Mapping 되어있는
		 * Node를 get한다. 
		 */
		if (rootExpr instanceof IdNode)
		{
			if(rootExpr.type == Type.NOT_QUOTED && this._table.containsKey(((IdNode)rootExpr).value))
			{
				result = this._table.get(((IdNode)rootExpr).value);
			}
			else
				result = rootExpr;
		}
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

	/*
	 * HashMap으로 구현한 table에 <Id, Node>쌍으로 put하여 준다.
	 */
	public void insertTable(String id, Node value)
	{
		this._table.put(id, value);
	}

}
