/*
 * ������ȣ : PL08-2
 * �й� : 02�й�
 * �� ��ȣ : 15
 * ���� �Ҽ� : �泲���б� �������� ��ǻ�Ͱ��а�
 * �й� �� ���� : 201002384(���ö) 201002436(������)
 */

package cute14ProjectItem2;

import java.util.ArrayList;
//

public class CuteInterpreter {
	
	private static ArrayList<defineNode> li = new ArrayList<defineNode>();
	// import �� ArrayList�� �̿��Ͽ� defineNode Ŭ������ �������� li�� �����Ѵ�.
	
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
		 * func�� car, cdr, nullQ, atomQ �̸� ���� ���Ҹ� ���ڷ� �Ͽ� �ش� �Լ� ���� func�� cons,
		 * eqQ �̸� �������ҿ� �� ���� ���Ҹ� ���ڷ� �Ͽ� �ش� �Լ� ����
		 */
		int i, flag = 0;
		// flag�� �� ���ǿ� ���� �ɷ����� �������� ���ȴ�.
		Node result = null;
		Node rhs1 = func.getNext();
		Node rhs2 = (rhs1 != null) ? rhs1.getNext() : null;

		if (func.value != FunctionNode.FunctionType.DEFINE) {
			if (rhs1 != null) {
				//���ڷ� �޾ƿ� functionnode�� func�� value type�� define�� �ƴϸ鼭
				//���� ��尡 ������� �ʴٸ�,
				for (i = 0; i < li.size(); i++) {
					//define node�� �縮�ŭ for�� �ݺ�,
					if (li.get(i).getTempString().equals(rhs1.toString()))
						//����Ʈ�� ���� equals�޼��带 ���� tostring���� ��ȯ�� ����
						//������ ���� ������ ��쿣
						rhs1 = li.get(i).getTempNode();
						//define node�� ����Ʈ���� ���� �޾ƿ� func�� �����κ����� �ִ´�.
				}
			}

			if (rhs2 != null) {
				// ���� ���� ��������ʴٸ�
				for (i = 0; i < li.size(); i++) {
					// �� �ڵ�� ���. �ٸ� rhs2 string�� ���Ѵٴ°� ��
					if (li.get(i).getTempString().equals(rhs2.toString()))
						rhs2 = li.get(i).getTempNode();
				}
			}
		}

		// if(rhs1!=null)

		if (rhs2 != null) {
			if (rhs2.getType() == Node.Type.NOT_QUOTED
					&& (!(rhs2 instanceof IntNode) && !(rhs2 instanceof BooleanNode)))
				// ���ڰ��� not quoted�鼭 intNode�� �ƴϰ�  booleanNode�� �ƴҰ��
				rhs2 = runExpr(rhs2);
				//rhs2�� ���� runExpr���� ���ڷ� �־� �۵���Ų��.
		}
		
		// ���⼭���� ������ �ö��(+ elearning ����Ʈ ����, ��������) Ÿ�Կ����� �ɷ����� ���� �ۼ��� �ڵ��̴�.
		if (rhs1 != null)
		{
			if(func.value == FunctionNode.FunctionType.DEFINE)
			{	//rhs1�� ���� �ʾҰ�, func�� value�� define�� ���
				if (rhs1.getType() == Node.Type.NOT_QUOTED	&& (!(rhs1 instanceof IntNode)
							&& !(rhs1 instanceof BooleanNode) && !(rhs1 instanceof IdNode)))
				{
					//�� ��쿣 rhs1�� not_quoted�鼭
					//int��尡 �ƴϰ�, boolean��尡 �ƴϰ�, id��嵵 �ƴϸ� Ÿ�Կ����� ����̹Ƿ� 
					//�����α׷� Ÿ�Կ����� ����ϰ�, flag ���� ��ȭ���������μ� �Ʒ� �ڵ尡 ������� �ʰ� �Ѵ�.
				errorLog("type error");
				flag = 1;
				}
			}
			
			else //�� ��쿣 define�� �ƴѰ�쿡 �ش��Ѵ�.
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
			{ //rhs2�� ���ڷ� ������ ���� not_quoted�̸鼭 int�� boolean ������°� �ƴҰ��
				//Ÿ�Կ����� �ɷ�����.
				errorLog("type error");
				flag = 1;
			}
		}

		if (flag == 0) { //������ ������� ���� ��� ������ �ڵ尡 ������ �ȴ�.(flag�� �Ǻ�)
			switch (func.value) {
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
			case CDR:
				if (rhs1 instanceof ListNode) {
					Node firstItem = ((ListNode) rhs1).value;
					if (firstItem != null) {
						result = new ListNode(rhs1.type, firstItem.next);
						// ��ü ����Ʈ(rhs1)�� Ÿ���� �ް�, �������Ұ� ������ ���ο� ����Ʈ ����
					} else {
						errorLog("runCDR first Error");
						// error �� ����: () ���� cdr�� ã�� ���̹Ƿ�
					}
				} else {
					errorLog("runCDR error"); // error�� ����
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
				// ���� tail�� ��� not_quoted�� ��ȯ�ϴµ� cons�� ��������
				// �ܺ�(��ȣ)�� quoted�� �����鼭 ���� quoted�� �ʿ䰡 �����Ƿ� �ڵ带 �ۼ���.
				// (printerŬ�������� quoted�� �Ǻ��Ͽ� ' �� ���̹Ƿ�)
				
				if (tail instanceof ListNode) {
					head.setNext(((ListNode) tail).value);
					result = new ListNode(tail.type, head);
					// tail�� Ÿ���� ������ head�� ������ ����Ʈ�� �����
					// head�� tail�� ����
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
				 * ���� ����: if data�� atom�̸� TRUE_NODE else if List��尡 null�� list ��
				 * TRUE_NODE else �ƴϸ� FALSE_NODE
				 */
				break;
			case EQ_Q:
				if (rhs1 != null && rhs1.equals(rhs2)) {
					// �� ��� Ÿ�Ը��� equals�� �����Ǿ�� ��
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
				 * ���� ����: if rhs1�� sublist �� NULL�̸� TRUE_NODE else FALSE_NODE
				 */
				break;
			case NOT:
				Node temp = new BooleanNode(Node.Type.NOT_QUOTED, false);
				// ������ �񱳳�� temp�� ����� false�ϰ�� true��� ��ȯ.
				if (rhs1.equals(temp))
					result = TRUE_NODE;
				else
					result = FALSE_NODE;
				// ����
				break;
			case COND:
				result = runCompare(rhs1);
				break;
			case DEFINE:
				if (rhs1 instanceof IdNode) {
					//rhs1 �� id���� �����ϴ°��̹Ƿ� ����Ǻ�
					if (rhs2 instanceof IntNode || rhs2 instanceof ListNode) {
						// �ι�° ���ڷ� ���� ���� intnode�̰ų� list�� �̷���� () ���� �����̾�� �Ѵ�.
						insertTable(rhs1.toString(), rhs2);
						// insertTable �޼����� ���ڷ� �־��ش�.
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
		// quote �Ǿ����� �״�� ���
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
		
		//����Ʈ�� value���� IDNode�����ϰ��,
		else if (opCode instanceof IdNode) {
			for (i = 0; i < li.size(); i++) {
				//����Ʈ�� �����ŭ �ݺ�(�˻�)
				if (li.get(i).getTempString().equals(opCode.toString()))
					result = li.get(i).getTempNode();
				///list�� opcode�� ���� ����Ʈ�� ���� �������� �߰ߵ� ��� ������� ����Ʈ�� tempnode���� �޾ƿ´�.
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
		// �� �־��� ����� ���� �κ��̴�. li�� �ᱹ lookuptable �޼��� ������ ����ϴ� ��.
		// left��, ����Ʈ���� ������ ���� ��忡�� ���� �����ϰ�, ������ ���� ����.

		if (left instanceof IntNode && right instanceof IntNode) {
			// ������ ���꿡 �°� ��ȣ ����
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
				// ��Ÿ ������, MINUS�� ����ϰ� ���� ������ ��
				// ���迬���� TRUE, FALSE��ȯ
				break;
			default:
				break;
			}
		} else
			errorLog("runBinaray not int");// error
		return result;
	}

	// �������� �Ǻ��ϴ� utility �Լ�
	private boolean isTrueNode(Node parameter)
	{
		return TRUE_NODE.equals(parameter);
	}

	// ������ (cond (condition1 result1) (condition2 result2) �� (#T resultN))����
	// ���� ����-���� �� ��, ������ true�� �ϳ��� ���� �����ϴ� �Լ�
	private Node runCompare(Node parameter) {
		Node condition = null;
		Node pair = parameter;// ����-���� �� �� ���� ó�� ��;
		Node result = null;
		// �� ����-���� �ָ��� ó���ϴ� loop
		while (pair != null) {// �� �̻� ����-���� ���� ���� ������ ��� ����
			if (!(pair instanceof ListNode)) {
				errorLog("runSingleCompare not List");// error ;; ����-���� ���� ���Ұ�
														// 2���� ����Ʈ ���¿��� �Ѵ�.
				break;
			}

			condition = ((ListNode) pair).value;
			// condition�� pair�� ù��° node
			// ���� condition�� next�� ù��° node�� ���ǿ� ���� ���� pair�� �Ѿ�ų� result�� ��
			// return��.
			if (isTrueNode(runExpr(condition))) { // condition�� ������ ������ ����
													// ��(isTrueNode��)
													// ���� condition�� ���̸�
													// action�� if�� ��������� ����� �ְ�
													// ���Ͻ�Ű������ result�� action��
													// �ִ´�.
				Node action = condition.next;
				result = action;
				break;
			}
			pair = pair.next; // ������ ���� �϶�, ���� ����-���� ���� ó��
		}

		return result;// ���� ���

	}

	private void insertTable(String ID, Node value) {
		//case define���� ���ڷ� �Ѱ�� �κ��̴�. ����Ʈ �Ӹ� �ƴ϶� ���� �����ϴµ� ����.
		int i, count = 0;
		defineNode temp = new defineNode(ID, value);
		//node value�� �Ѿ�� ���ڰ� definenode�� tempnode�κ��� ����ϰ� �ȴ�.

		for (i = 0; i < li.size(); i++)
		{//�� ���� list�� lookuptable������ �ϴµ�, 
			if (li.get(i).getTempString().equals(ID.toString())) 
			{
				li.get(i).setTempNode(value);
				count++;
			}//tempstring��(���ڷ� �޾ƿ� value�κ�) String ID�� equal�̶��(������ ���ǵ� �����ִٸ�)
			//settempnode�� �̿��Ͽ� �� ��ġ�� �κ�(���ǵǾ����� �κ�)�� ���ο� ������ ������.
			//ī��Ʈ�� �߰����������μ� ���� �ڵ�� �ߺ��Ǵ� ���� �����Ѵ�.
		}

		if (count == 0)
			//��ġ�ϴ� �κ��� ���ٸ�(count�� 0�̶����� ������ ���ǵ� �κ��� �����ٴ� �Ҹ�)
			li.add(temp);
		//���������� ����� li�κп� id�� value�� ���ڷ� ���� temp�� ���� ����Ʈ�� �߰���Ų��.
	}

}
