package project1;

import java.util.List;
import java.util.ListIterator;

import project1.BinarayOpNode.BinType;

public class BasicParser
{
	private ListIterator<Token> iter;

	public BasicParser(List<Token> tokenList)
	{
		iter = tokenList.listIterator();
	}

	private void errorLog(String err)
	{
		System.out.println(err);
	}

	private Token getNextToken()
	{
		if(!iter.hasNext())
		{
			return null;
		}

		return iter.next();
	}

	public Node parseExpr()
	{
		Token t = getNextToken();

		if(t == null)
		{
			//System.out.println("No more token");

			return null;
		}

		switch(t.type)
		{
			case ID:
			{
				IdNode idNode = new IdNode();
				idNode.value = t.lexeme;
	
				return idNode;
			}
			case INT:
			{
				IntNode intNode = new IntNode();
	
				if(t.lexeme==null)
				{
					System.out.println("???");
				}
	
				intNode.value = new Integer(t.lexeme);
	
				return intNode; 
			}
	
			// BinaryOpNode에 대하여 작성 
			// +, -, /, *가 해당
			case PLUS:
			{
				BinarayOpNode plus = new BinarayOpNode();
				plus.value = BinType.PLUS;
	
				return plus;
			}
			case MINUS:
			{
				BinarayOpNode minus = new BinarayOpNode();
				minus.value = BinType.MINUS;
	
				return minus;
			}
			case DIV:
			{
				BinarayOpNode div = new BinarayOpNode();
				div.value = BinType.DIV;
	
				return div;
			}
			case TIMES:
			{
				BinarayOpNode times = new BinarayOpNode();
				times.value = BinType.TIMES;
	
				return times;
			}
			case LT:
			{
				BinarayOpNode LT = new BinarayOpNode();
				LT.value = BinType.LT;
	
				return LT;
			}
			case GT:
			{
				BinarayOpNode GT = new BinarayOpNode();
				GT.value = BinType.GT;
	
				return GT;
			}
			case EQ:
			{
				BinarayOpNode EQ = new BinarayOpNode();
				EQ.value = BinType.EQ;
	
				return EQ;
			}

			// FunctionNode에 대하여 작성
			// 키워드가 FunctionNode에 해당
			case ATOM_Q:
			{
				FunctionNode atom = new FunctionNode();
				atom.value = FunctionNode.FunctionType.ATOM_Q;
	
				return atom;
			}
			case DEFINE:
			{
				FunctionNode define = new FunctionNode();
				define.value = FunctionNode.FunctionType.DEFINE;
	
				return define;
			}
			case LAMBDA:
			{
				FunctionNode lambda = new FunctionNode();
				lambda.value = FunctionNode.FunctionType.LAMBDA;
	
				return lambda;
			}
			case COND:
			{
				FunctionNode cond = new FunctionNode();
				cond.value = FunctionNode.FunctionType.COND;
	
				return cond;
			}
			case NOT:
			{
				FunctionNode not = new FunctionNode();
				not.value = FunctionNode.FunctionType.NOT;
	
				return not;
			}
			case CDR:
			{
				FunctionNode cdr = new FunctionNode();
				cdr.value = FunctionNode.FunctionType.CDR;
	
				return cdr;
			}
			case CAR:
			{
				FunctionNode car = new FunctionNode();
				car.value = FunctionNode.FunctionType.CAR;
	
				return car;
			}
			case CONS:
			{
				FunctionNode cons = new FunctionNode();
				cons.value = FunctionNode.FunctionType.CONS;
	
				return cons;
			}
			case EQ_Q:
			{
				FunctionNode eq = new FunctionNode();
				eq.value = FunctionNode.FunctionType.EQ_Q;
	
				return eq;
			}
			case NULL_Q:
			{
				FunctionNode null_q = new FunctionNode();
				null_q.value = FunctionNode.FunctionType.NULL_Q;
	
				return null_q;
			}
			case FALSE:
			{
				BooleanNode falseNode = new BooleanNode();
				falseNode.value = false;
	
				return falseNode;
			}
			case TRUE:
			{
				BooleanNode trueNode = new BooleanNode();
				trueNode.value = true;
	
				return trueNode;
			}

			// BooleanNode에 대하여 작성
			// L_PAREN일 경우 parseExprList()를 호출하여 처리
			case L_PAREN:
			{
				ListNode listNode = new ListNode();
				listNode.value = parseExprList();
	
				return listNode;
			}
			case R_PAREN:
			{
				return null;
			}
			case APOSTROPHE:
			{
				ListNode apListNode = new ListNode();
				QuoteNode apQuoteNode = new QuoteNode();
				
				//apQuoteNode의 value를 set하시오.
				apListNode.value = apQuoteNode;
				apQuoteNode.value = parseExpr();
				
				return apListNode;
			}
			case QUOTE:
			{
				//QuoteNode를 반환하도록 작성하시오.
	
				QuoteNode quote = new QuoteNode();
				quote.value = parseExpr();
	
				return quote;
			}
		}

		System.out.println("Parsing Error!");

		return null;
	}

	// List의 value를 생성하는 메소드
	private Node parseExprList()
	{
		Node head = parseExpr();
		
		if (head == null) // if next token is RPAREN
		{
			return null; 
		}
		
		head.setNext(parseExprList()); 
		
		return head;
	}
}