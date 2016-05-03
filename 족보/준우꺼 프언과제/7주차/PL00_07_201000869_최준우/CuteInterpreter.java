/*
 * ���� ��ȣ : hw07
 * �й� : 00�й�
 * �Ҽ� : ��ǻ�Ͱ��а�
 * �й� : 201000869
 * �̸� : ���ؿ�
 */

package hw07_Reconizing_Token_u00;

import hw07_Reconizing_Token_u00.Node.Type;

public class CuteInterpreter { 
	private void errorLog(String err) { 
		System.out.println(err); 
	} 

	//booleanNode ����
	private final static BooleanNode TRUE_NODE = new BooleanNode( 
			Type.NOT_QUOTED, true); 
	private final static BooleanNode FALSE_NODE = new BooleanNode( 
			Type.NOT_QUOTED, false); 

	//copymode����
	enum CopyMode { 
		NO_NEXT, NEXT 
	} 

	/*
	 * �ش� node�� �����Ѵ�.
	 * copymode�� parameter�� �޾� no_next�� ��� �ش� node�� ó�� node�� �����ϰ�
	 * next�� ��� list�� ��ü�� �����Ѵ�.
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
	 * Function node�� �о� �ش� function�� �۵��Ѵ�.
	 */
	private Node runFunction(FunctionNode func) { 
		/* 
		 func�� car, cdr, nullQ, atomQ �̸� 
		���� ���Ҹ� ���ڷ� �Ͽ� �ش� �Լ� ���� 
		 func�� cons, eqQ �̸� 
		 �������ҿ� �� ���� ���Ҹ� ���ڷ� �Ͽ� �ش� �Լ� ���� 
		 */ 

		Node result = null; 
		Node rhs1 = func.getNext(); 
		Node rhs2 = (rhs1 != null) ? rhs1.getNext() : null;

		switch (func.value) { 
		//list�� ó�� node�� �����Ѵ�.
		//copymode�� no_next�� �����Ͽ� ó�� node�� result�� ��ȯ�Ѵ�.
		case CAR: 
			if (rhs1 instanceof ListNode) { 
				Node item = ((ListNode) rhs1).value; 
				if (null != item) { 
					result = copyNode(item, CopyMode.NO_NEXT); 
					// item�� copy ���� �ѱ��. 
					// next�� �������� ���� 
				} else { 
					errorLog("runCAR first NULL"); 
				} 
			} else { 
				errorLog("runCAR tag Error");// error�� ���� 
			} 
			break; 

			/*
			 * rhs1�� listNode�� ��� �ش� node�� 2��° node�� head�� �ϴ� result�� ��ȯ�Ѵ�.
			 */
		case CDR: 
			if (rhs1 instanceof ListNode) { 
				Node firstItem = ((ListNode) rhs1).value; 

				if (firstItem != null) { 
					result = new ListNode(rhs1.type,firstItem.next); 
					//��ü ����Ʈ(rhs1)�� Ÿ���� �ް�, �������Ұ� ������ ���ο� ����Ʈ ���� 
				} else { 
					errorLog("runCDR first Error"); 
					// error �� ����: () ���� cdr�� ã�� ���̹Ƿ� 
				} 
			} else { 
				errorLog("runCDR error"); // error�� ���� 
			} 
			break; 

			/*
			 * rhs1�� next�� tail�� value�� ���δ�.
			 * �׸��� rhs1�� head�� ���� list node�� ����� ��ȯ�Ѵ�.
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

				// tail�� Ÿ���� ������ head�� ������ ����Ʈ�� ����� 
				// head�� tail�� ���� 
			} else { 
				errorLog("runCons tail not list");// return error; 
			} 

			break; 

			/*
			 * rhs1�� listNode�� ��, list�� ������� ������ false
			 * �� ���� ���� ��� true
			 */
		case ATOM_Q: 
			/* ���� ����: 
		if data�� atom�̸� TRUE_NODE
		else if List��尡 null�� list �� TRUE_NODE
		else �ƴϸ� FALSE_NODE 
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

			//������ node�� ���ǵ� ���� equal�� ȣ���Ͽ� T/F�� �Ǻ��Ͽ� ��ȯ�Ѵ�.
		case EQ_Q: 
			if (rhs1 != null && rhs1.equals( rhs2 )) { 
				// �� ��� Ÿ�Ը��� equals�� �����Ǿ�� �� 
				result = TRUE_NODE; 
			} else { 
				result = FALSE_NODE; 
			} 
			break; 

			/*
			 * rhs1�� ��� �ִٸ� True
			 * �� ���� ���� False�� ��ȯ�Ѵ�.
			 */
		case NULL_Q: 
			/* ���� ����: 
		if rhs1�� sublist �� NULL�̸� TRUE_NODE 
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
		 * rhs1 list�� �������� BooleanNode�� ��� ��� ���� True�� ��� False�� ��ȯ�ϰ�,
		 * �ݴ��� ��� True�� ��ȯ�Ѵ�.
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
		// COND Function�� ��� runCompare�� ���Ͽ� ����-���� pair�� run�Ѵ�.
		case COND: 
			result=runCompare(rhs1);
			break; 

			/*
			 * �̹� ���������� �������� �ʴ´�.
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
	 * ù node�� ���� �������� ��� 2���� parameter�� runExpr�� ���� ����� ������ ��,
	 * ������ �ش��ϴ� ���� ������ �Ѵ�.
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
			//��� �� ������ ����� booleanNode�� �ȴ�.
			case LT:
				result = new BooleanNode(Type.NOT_QUOTED, ((IntNode)left).value < ((IntNode)right).value);
				break;
			case GT:
				result = new BooleanNode(Type.NOT_QUOTED, ((IntNode)left).value > ((IntNode)right).value);
				break;
				// ��Ÿ ������, MINUS�� ����ϰ� ���� ������ ��
				// ���迬���� TRUE, FALSE��ȯ
			default:
				break;
			}
		}else
			errorLog("runBinaray not int");// error

		return result;
	}

	// �������� �Ǻ��ϴ� utility �Լ�
	private boolean isTrueNode(Node parameter){
		//parameter�� TRUE_NODE���� Ȯ���Ͽ� ��/������ ��ȯ�Ѵ�.
		return TRUE_NODE.equals(parameter);
	}

	// ������ (cond (condition1 result1) (condition2 result2) �� (#T resultN))����
	// ���� ����-���� �� ��, ������ true�� �ϳ��� ���� �����ϴ� �Լ�
	private Node runCompare(Node parameter) {
		Node condition = null;
		Node pair = parameter;// ����-���� �� �� ���� ó�� ��;
		Node result = null;

		// �� ����-���� �ָ��� ó���ϴ� loop
		while (pair != null){// �� �̻� ����-���� ���� ���� ������ ��� ����
			if (!(pair instanceof ListNode))	{
				errorLog("runSingleCompare not List");// error ;; ����-���� ���� ���Ұ� 2���� ����Ʈ ���¿��� �Ѵ�.
				break;
			}

			condition = ((ListNode)pair).value;			
			// ����Ʈ�� ù��° ���Ҵ� ���� ���

			//������ �����Ͽ� �ش� ����� True�̸� ���� ������ �޸� ���๮�� run�Ѵ�.
			if (isTrueNode(runExpr(condition))){	//condition�� ������ ������ ���� ��(isTrueNode��)
				Node action = runExpr(condition.getNext());
				// condition�� ���� ���Ҵ� ���� ���
				result = action;
				// result�� action�� ������

				break;	// �ϳ��� ����-���� �ָ� ����
			}
			//������ ��� ���� pair�� �����Ѵ�.
			pair = pair.getNext();	// ������ ���� �϶�, ���� ����-���� ���� ó��
		}

		return result;//���� ���
	}


	//list�� ��� ���� �� ù node�� function�� �����Ѵ�.
	//�̹� ���������� runBinaray�� �߰��Ͽ� ���� ������ �����Ѵ�.
	private Node runList(ListNode list) {
		// quote �Ǿ����� �״�� ���
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
	 * ID,INT,BOOLEAN�� ���ؼ��� �ش� node�� �״�� ��ȯ�Ѵ�.
	 * LIST�� ���ؼ��� run list�� �����Ͽ� �ش� line�� ����� �����Ѵ�. 
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
