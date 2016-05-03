package project1;

import java.util.HashMap;

public class CuteInterpreter
{
	private final static BooleanNode TRUE_NODE = new BooleanNode();
	private final static BooleanNode FALSE_NODE = new BooleanNode();
	private HashMap<String, Node> table;

	CuteInterpreter()
	{
		table = new HashMap<String, Node>();
	}

	static
	{
		TRUE_NODE.value = true;
		FALSE_NODE.value = false;
	}

	private void errorLog(String err)
	{
		System.out.println(err);
	}

	enum CopyMode
	{
		NO_NEXT, NEXT
	}

	public void insertTable(String id, Node value)
	{
		table.put(id, value);
	}

	private Node runBinaray(BinarayOpNode node)
	{
		Node result;
		Node left = node.getNext();
		Node right = left.getNext();
	
		left = runExpr(left);
		right = runExpr(right);

		if(left ==null||right ==null)
		{
			errorLog("runBinary runExpr null");
		}
		if(!(left instanceof IntNode)||!(right instanceof IntNode))
		{
			errorLog("Type Error!");
			
			return null;
		}

		switch(node.value)
		{
			case MINUS:
			{
				result = new IntNode();
				
				((IntNode) result).value = ((IntNode) left).value - ((IntNode) right).value;
				
				return result;
			}
			case PLUS:
			{
				result = new IntNode();
				
				((IntNode) result).value = ((IntNode) left).value + ((IntNode) right).value;
				
				return result;
			}
			case TIMES:
			{
				result = new IntNode();

				((IntNode) result).value = ((IntNode) left).value * ((IntNode) right).value;
				
				return result;
			}
			case DIV:
			{
				result = new IntNode();
				
				((IntNode) result).value = ((IntNode) left).value / ((IntNode) right).value;
				
				return result;
			}
			case LT:
			{
				result = new BooleanNode();
				
				((BooleanNode) result).value = ((IntNode) left).value < ((IntNode) right).value;
				
				return result;
			}
			case GT:
			{
				result = new BooleanNode();
				
				((BooleanNode) result).value = ((IntNode) left).value > ((IntNode) right).value;
				
				return result;
			}
			case EQ:
			{
				result = new BooleanNode();
				
				if(((IntNode) left).value == ((IntNode) right).value)
				{
					result = TRUE_NODE;
				}
				else
				{
					result = FALSE_NODE;
				}
				
				return result;
			}
		}

		return null;
	}

	private Node runFunction(FunctionNode func)
	{
		Node rhs1 = func.getNext();
		Node rhs2 = (rhs1 != null) ? rhs1.getNext() : null;
		
		Node left = rhs1;
		Node right = rhs2;

		if(rhs1!=null && !this.checkQuote(rhs1))
		{
			left = runExpr(rhs1);
		}
		if(rhs2!=null && !this.checkQuote(rhs2))
		{
			right = runExpr(rhs2);
		}

		switch (func.value)
		{
			case CAR:
			{
				if(!checkQuote(left))
				{
					errorLog("Syntax Error!");
				}
				
				Node item = runQuote((ListNode) left);
			
				//	copyNode를 이용하여 값을 return 하시오.
				return copyNode(((ListNode) item).value, CopyMode.NO_NEXT);
			}
			case CDR:
			{
				if(!checkQuote(left))
				{
					errorLog("Syntax Error!");
				}
				
				Node item = runQuote((ListNode) left);
				
				return copyNode(((ListNode) item).value.getNext(), CopyMode.NEXT);
			}
			case CONS:
			{				
				//CONS에 맞게 작성할것
				Node head = runExpr(left);
				Node tail = runExpr(right);
				
				ListNode result = new ListNode();
				result.value = head;
				
				if(head==null)
				{
					errorLog("head null");
				}
				
				head.setNext(tail);
				
				return result;
			}
			case ATOM_Q:
			{				
				if(!(left instanceof ListNode) || (((ListNode) left).value==null))
				{
					return TRUE_NODE;
				}
				else
				{
					return FALSE_NODE;
				}
			}
			case EQ_Q:
			{
				if(left!=null && left.equals(right)) 
				{
					return TRUE_NODE;
				} 
				else 
				{
					return FALSE_NODE;
				}
			}
			case NULL_Q:
			{				
				if (left instanceof ListNode && ((ListNode) left).value==null)
				{
					return TRUE_NODE;
				}
				else
				{
					return FALSE_NODE;
				}
			}
			case NOT:
			{				
				Node temp;

				if(left!=null && (temp=runExpr(left)) instanceof BooleanNode)
				{ 	
					if(runNot(runExpr(temp)))
					{
						return FALSE_NODE;
					}
					else
					{
						return TRUE_NODE;
					}
				}
			}
			case COND:
			{
				return runCond(left);
			}
			case DEFINE:
			{
				if(rhs1 instanceof IdNode)
				{
					if(right instanceof Node && (right instanceof BooleanNode || right instanceof IntNode))
					{
						insertTable(((IdNode)rhs1).value, right);
						
						return TRUE_NODE;
					}
					else if(right instanceof ListNode && checkQuote(right))
					{
						insertTable(((IdNode)rhs1).value, right);
						
						return TRUE_NODE;
					}
				}
			}
			case LAMBDA:
			{
				System.out.println("아직까지 미구현한 부분");
				
				return FALSE_NODE;
			}
			default:
			{
				break;
			}
		}
		
		return null;
	}
	
	private boolean runNot(Node node)
	{
		return TRUE_NODE.equals(node);
	}

	private Node runCond(Node node)
	{
		Node value = node;
		Node temp = null;
		Node result = null;

		while(value!=null)
		{
			if((value instanceof ListNode))
			{
				temp = ((ListNode)value).value;

				if(runNot(runExpr(temp)))
				{
					Node tempNode = temp.next;

					result = runExpr(tempNode);

					break;
				}
				else
				{
					result = runExpr(temp);
					value = value.getNext();
				}
			}
			else
			{
				System.out.println("ListNode가 아닙니다!");

				break;
			}
		}

		return result;
	}
	
	private Node copyNode(Node node, CopyMode mode)
	{
		// node를 복사
		// mode에 따라서 next를 복사함
		if(node==null)
		{
			return null;
		}
		
		Node result = null;
		
		if(node instanceof BinarayOpNode)
		{
			result = new BinarayOpNode();
		}
		else if(node instanceof BooleanNode)
		{
			result = new BooleanNode();
		}
		else if(node instanceof FunctionNode)
		{
			result = new FunctionNode();
		}
		else if(node instanceof IdNode)
		{
			result = new IdNode();
		}
		else if(node instanceof IntNode)
		{
			result = new IntNode();
		}
		else if(node instanceof ListNode)
		{
			result = new ListNode();
		}
		
		result.copyValue(node);
		
		if(mode==CopyMode.NEXT && result!=null)
		{
			result.setNext(copyNode(node.getNext(), mode));
		}
		
		return result;
	}

	private Node runList(ListNode list)
	{
		// list의 value가 QuoteNode일 경우
		if(list.value instanceof QuoteNode)
		{
			return runQuote(list);
		}
		
		Node opCode = list.value;
		
		if(opCode==null)
		{
			return list;
		}
		if(opCode instanceof FunctionNode)
		{
			return runFunction((FunctionNode) opCode);
		}
		if(opCode instanceof BinarayOpNode)
		{
			return runBinaray((BinarayOpNode) opCode);
		}
		
		return list;
	}

	private Node runQuote(ListNode node)
	{
		// QuoteNode의 value를 반환함
		QuoteNode qItem = (QuoteNode) node.value;
		Node item = qItem.value;

		return item;
	}

	public Node runExpr(Node rootExpr)
	{
		if(rootExpr==null)
		{
			return null;
		}
		if(rootExpr instanceof IdNode)
		{
			if((checkQuote(rootExpr)==false) && (table.containsKey(((IdNode)rootExpr).value)))
			{
				return table.get(((IdNode)rootExpr).value);
			}
			else
			{
				return rootExpr;
			}
		}
		else if(rootExpr instanceof IntNode)
		{
			return rootExpr;
		}
		else if(rootExpr instanceof BooleanNode)
		{
			return rootExpr;
		}
		else if(rootExpr instanceof ListNode)
		{
			return runList((ListNode) rootExpr);
		}
		else
		{
			errorLog("run Expr error");
		}
		
		return null;
	}
	
	private boolean checkQuote(Node node)
	{
		// QuoteNode의 형태인지 확인하는 메소드
		if(!(node instanceof ListNode))
		{
			return false;
		}
		
		ListNode tmp = (ListNode) node;

		if(!(tmp.value instanceof QuoteNode))
		{
			return false;
		}
		
		return true;
	}
}