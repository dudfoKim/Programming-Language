/*
 * ���� ��ȣ : hw09
 * �й� : 00�й�
 * �Ҽ� : ��ǻ�Ͱ��а�
 * �й� : 201000869
 * �̸� : ���ؿ�
 */

package hw10_Reconizing_Token_u00;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import hw10_Reconizing_Token_u00.FunctionNode.FunctionType;
import hw10_Reconizing_Token_u00.Node.Type;

public class CuteInterpreter { 

	/*
	 * Define�� �����ϱ� ���� symbol table�� �����Ѵ�.
	 * Map������ �̿��Ͽ� String�� Node�� mapping�ϴ� table�� �����Ѵ�.
	 * �̹� ���������� parameter��  ó���� ���Ͽ� arrayList�� �����Ͽ� ���� �Լ��� Stack ȯ���� �����Ѵ�.
	 */
	private Map<String ,Node> _table;
	
	public CuteInterpreter()
	{
		//Map Interface�� HashMap���� �����Ѵ�.
		this._table = new HashMap<String, Node>();
		
	}

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
	 * �̹� ���������� Define�� String node�� ���� �� �����Ƿ�
	 * not quoted�� node�� runExpr�� ������ �����Ͽ� ����� ��´�.
	 */
	private Node runFunction(FunctionNode func, ListNode list) { 

		Node result = null; 
		Node rhs1 = func.getNext(); 
		Node rhs2 = (rhs1 != null) ? rhs1.getNext() : null;

		if(rhs1 == null)
			return list;
		/*
		 * rhs1�� rhs2�� not quoted�� ��� runExpr�� ������ �����Ѵ�.
		 * �׷��� ���� ���� �� �� �״�� left�� right�� �����Ͽ� ���ȴ�.
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
		//list�� ó�� node�� �����Ѵ�.
		//copymode�� no_next�� �����Ͽ� ó�� node�� result�� ��ȯ�Ѵ�.
		case CAR: 
			if (left instanceof ListNode) { 
				Node item = ((ListNode) left).value; 
				if (null != item) { 
					result = copyNode(item, CopyMode.NO_NEXT); 
					result.setType(Type.QUOTED);
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
			 * left�� listNode�� ��� �ش� node�� 2��° node�� head�� �ϴ� result�� ��ȯ�Ѵ�.
			 */
		case CDR: 
			if (left instanceof ListNode) { 
				Node firstItem = ((ListNode) left).value; 

				if (firstItem != null) { 
					result = new ListNode(left.type,firstItem.next); 
					result.setType(Type.QUOTED);
					//��ü ����Ʈ(left)�� Ÿ���� �ް�, �������Ұ� ������ ���ο� ����Ʈ ���� 
				} else { 
					errorLog("runCDR first Error"); 
					// error �� ����: () ���� cdr�� ã�� ���̹Ƿ� 
				} 
			} else { 
				errorLog("runCDR error"); // error�� ���� 
			} 
			break; 

			/*
			 * left�� next�� tail�� value�� ���δ�.
			 * �׸��� left�� head�� ���� list node�� ����� ��ȯ�Ѵ�.
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
				// tail�� Ÿ���� ������ head�� ������ ����Ʈ�� ����� 
				// head�� tail�� ���� 
			} else { 
				errorLog("runCons tail not list");// return error; 
			} 

			break; 

			/*
			 * left�� listNode�� ��, list�� ������� ������ false
			 * �� ���� ���� ��� true
			 */
		case ATOM_Q: 
			/* ���� ����: 
		if data�� atom�̸� TRUE_NODE
		else if List��尡 null�� list �� TRUE_NODE
		else �ƴϸ� FALSE_NODE 
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

			//������ node�� ���ǵ� ���� equal�� ȣ���Ͽ� T/F�� �Ǻ��Ͽ� ��ȯ�Ѵ�.
		case EQ_Q: 
			if (left != null && left.equals( right )) { 
				// �� ��� Ÿ�Ը��� equals�� �����Ǿ�� �� 
				result = TRUE_NODE; 
			} else { 
				result = FALSE_NODE; 
			} 
			break; 

			/*
			 * left�� ��� �ִٸ� True
			 * �� ���� ���� False�� ��ȯ�Ѵ�.
			 */
		case NULL_Q: 
			/* ���� ����: 
		if left�� sublist �� NULL�̸� TRUE_NODE 
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
			 * left list�� �������� BooleanNode�� ��� ��� ���� True�� ��� False�� ��ȯ�ϰ�,
			 * �ݴ��� ��� True�� ��ȯ�Ѵ�.
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
			// COND Function�� ��� runCompare�� ���Ͽ� ����-���� pair�� run�Ѵ�.
		case COND: 
			result=runCompare(left);
			break; 

			/*
			 * define function�� �����Ѵ�.
			 * HashMap���� ������ Table�� <key, value> ������
			 * <Id, Node>�� Mapping�Ͽ� Map�� put�Ѵ�.
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
		 * user defined function�� symbol table�� �����Ѵ�. ����� parameter�� ������ ���� binding�Ѵ�.
		 */
		case LAMBDA:
			//�Լ��� ���������� �����ϱ� ���� arrayList�� �����Ѵ�.
			ArrayList<String> arrayList1=new ArrayList<String>();
			ArrayList<Node> arrayList2=new ArrayList<Node>();
			//i�� ������ ���� ���̰� j�� parameter�� ���̴�.
			int i=0, j=0;
			Node tempString = null;
			Node tempNext=list.getNext();
			//lambda�� rhs1�� parameter�� ��Ÿ���� list�̴�.
			//j�� parameter�� ���� ī��Ʈ�Ѵ�.
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
			//parameter�� key������ ������ ���� �ؽ� ���̺� �����ϰ�,
			//arrayList�� add�Ͽ� stack�� �����Ѵ�.
			
			for(i=0;tempNext!=null;i++)
			{
				this.insertTable(((IdNode)tempString).value, copyNode(runExpr(tempNext),CopyMode.NO_NEXT));
				arrayList1.add(((IdNode)tempString).value);
				arrayList2.add(copyNode(runExpr(tempNext),CopyMode.NO_NEXT));
				tempString = tempString.next;
				tempNext = tempNext.next;
			}
			//���� ������ ���� 0�̸� lambda�� �����̹Ƿ� list ��ü�� ��ȯ�Ѵ�.
			if(i==0)
			{
				result = list;
				break;
			}
			//�����ڸ� ����ϱ� ���� ���Ǿ��� parameter�� ���� ���� ���� ��Ų��.
			while(arrayList1.size()!=0)
				this.insertTable(arrayList1.remove(arrayList1.size()-1), arrayList2.remove(arrayList2.size()-1));
			//�Լ� body �κ��� �����Ͽ� result ���� ����.
			result = runExpr(rhs2);
			
			//�� �Լ� stack�� ���������� �����Ѵ�.
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
	 * ù node�� ���� �������� ��� 2���� parameter�� runExpr�� ���� ����� ������ ��,
	 * ������ �ش��ϴ� ���� ������ �Ѵ�.
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
			result = runFunction((FunctionNode) opCode, list);
		}else if (opCode instanceof BinarayOpNode){
			result = runBinaray((BinarayOpNode) opCode, list);
		//lambda�� ���ǵ� idNode�� ���� ��List�� ����� runExpr�� �����Ͽ� lambda �Լ��� ���� result�� ��´�.
		}else if (opCode instanceof IdNode){
			if(this._table.containsKey(((IdNode) opCode).value) && opCode.type.equals(Type.NOT_QUOTED)){
				if(runExpr(opCode) instanceof ListNode && ((ListNode)runExpr(opCode)).value instanceof FunctionNode){
					ListNode temp = new ListNode( Type.NOT_QUOTED,runExpr(opCode));
					
					temp.value.next = opCode.next;
					
					result = runExpr(temp);
				}
			//�̿��� IdNode�� list ��ü�� ��ȯ�Ѵ�.
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
	 * ID,INT,BOOLEAN�� ���ؼ��� �ش� node�� �״�� ��ȯ�Ѵ�.
	 * LIST�� ���ؼ��� run list�� �����Ͽ� �ش� line�� ����� �����Ѵ�. 
	 */
	public Node runExpr(Node rootExpr) { 
		if(rootExpr == null) 
			return null; 

		Node result = null; 

		/*
		 * �̹� ���������� Id���� Mapping�Ǿ� �ִ� Node�� ����ϱ� ����
		 * result�� Id�� Key�� table�� contain�� �ִ��� Ȯ���ϰ� table���� Mapping �Ǿ��ִ�
		 * Node�� get�Ѵ�. 
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
	 * HashMap���� ������ table�� <Id, Node>������ put�Ͽ� �ش�.
	 */
	public void insertTable(String id, Node value)
	{
		this._table.put(id, value);
	}

}
