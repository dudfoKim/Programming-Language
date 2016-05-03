package compile;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.ListIterator;

import ast.BinaryOpNode;
import ast.BinaryOpNode.BinType;
import ast.BooleanNode;
import ast.FunctionNode;
import ast.FunctionNode.FunctionType;
import ast.IdNode;
import ast.IntNode;
import ast.ListNode;
import ast.Node;
import ast.QuoteNode;
import compile.CuteScanner.Token;
import compile.CuteScanner.TokenType;

public class Parser {
	private ListIterator<Token> iter;

	public Parser(List<Token> tokenList) {
		
		iter = tokenList.listIterator();
	}

	private void errorLog(String err) {
		System.out.println(err);
	}

	private Token getNextToken() {
		if (!iter.hasNext())
			return null;
		return iter.next();
	}

	public Node parseExpr() {
		Token t = getNextToken();
		if (t == null) {
			System.out.println("No more token");
			return null;
		}
		switch (t.type) {
		case ID:
			IdNode idNode = new IdNode();
			idNode.value = t.lexeme;
			return idNode;
		case INT:
			IntNode intNode = new IntNode();
			if (t.lexeme == null)
				System.out.println("???");
			intNode.value = new Integer(t.lexeme);
			return intNode;
			// BinaryOpNode�� ���Ͽ� �ۼ�
			// +, -, /, *�� �ش�
		case DIV:
			BinaryOpNode div = new BinaryOpNode();
			div.value = BinType.DIV;
			return div;
		case EQ:
			BinaryOpNode eq = new BinaryOpNode();
			eq.value = BinType.EQ;
			return eq;
		case MINUS:
			BinaryOpNode minus = new BinaryOpNode();
			minus.value = BinType.MINUS;
			return minus;
		case GT:
			BinaryOpNode gt = new BinaryOpNode();
			gt.value = BinType.GT;
			return gt;
		case PLUS:
			BinaryOpNode plus = new BinaryOpNode();
			plus.value = BinType.PLUS;
			return plus;
		case TIMES:
			BinaryOpNode times = new BinaryOpNode();
			times.value = BinType.TIMES;
			return times;
		case LT:
			BinaryOpNode lt = new BinaryOpNode();
			lt.value = BinType.LT;
			return lt;
		case ATOM_Q:
			FunctionNode atom = new FunctionNode();
			atom.value = FunctionType.ATOM_Q;
			return atom;
			// FunctionNode�� ���Ͽ� �ۼ�
			// Ű���尡 FunctionNode�� �ش�
		case CAR:
			FunctionNode car = new FunctionNode();
			car.value = FunctionType.CAR;
			return car;
		case CDR:
			FunctionNode cdr = new FunctionNode();
			cdr.value = FunctionType.CDR;
			return cdr;
		case COND:
			FunctionNode cond = new FunctionNode();
			cond.value = FunctionType.COND;
			return cond;
		case CONS:
			FunctionNode cons = new FunctionNode();
			cons.value = FunctionType.CONS;
			return cons;
		case DEFINE:
			FunctionNode define = new FunctionNode();
			define.value = FunctionType.DEFINE;
			return define;
		case EQ_Q:
			FunctionNode eqq = new FunctionNode();
			eqq.value = FunctionType.EQ_Q;
			return eqq;
		case LAMBDA:
			FunctionNode lambda = new FunctionNode();
			lambda.value = FunctionType.LAMBDA;
			return lambda;
		case NOT:
			FunctionNode not = new FunctionNode();
			not.value = FunctionType.NOT;
			return not;
		case NULL_Q:
			FunctionNode nullq = new FunctionNode();
			nullq.value = FunctionType.NULL_Q;
			return nullq;
			// BooleanNode�� ���Ͽ� �ۼ�
		case FALSE:
			BooleanNode falseNode = new BooleanNode();
			falseNode.value = false;
			return falseNode;
		case TRUE:
			BooleanNode trueNode = new BooleanNode();
			trueNode.value = true;
			return trueNode;
			// case L_PAREN�� ���� case R_PAREN�� ��쿡 ���ؼ� �ۼ�
			// L_PAREN�� ��� parseExprList()�� ȣ���Ͽ� ó��
		case L_PAREN:
			ListNode listNode = new ListNode();
			listNode.value = parseExprList();
			return listNode;
		case R_PAREN:
			return null;
		case APOSTROPHE:
			ListNode apListNode = new ListNode();
			QuoteNode apQuoteNode = new QuoteNode();
			//apQuoteNode�� value�� set�Ͻÿ�.
			apQuoteNode.value = parseExpr();
			apListNode.value = apQuoteNode;
			return apListNode;
		case QUOTE:
			//QuoteNode�� ��ȯ�ϵ��� �ۼ��Ͻÿ�.
			QuoteNode quoteNode = new QuoteNode();
			quoteNode.value = parseExpr();
			return quoteNode;
		}
		// head�� next�� ����� head�� ��ȯ�ϵ��� �ۼ�
		System.out.println("Parsing Error!");
		return null;
	}

	// List�� value�� �����ϴ� �޼ҵ�
	private Node parseExprList() {
		/*
		 * Node head = parseExpr(); if(head == null) return null; Node lastNext
		 * = parseExpr(); while(lastNext!=null){ head.setLastNext(lastNext);
		 * lastNext = parseExpr(); } return head;
		 */
		Node head = parseExpr();
		// head�� next ��带 set�Ͻÿ�.
		if (head == null) // if next token is RPAREN
			return null;
		head.setNext(parseExprList()); // Ȥ�� head.next = parseExprList();
		return head;
	}
	

}
