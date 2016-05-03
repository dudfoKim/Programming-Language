package project2;

import java.io.PrintStream;

public class Printer
{
	PrintStream ps;

	public Printer(PrintStream ps)
	{
		this.ps = ps;
	}

	//Make your print class
	//ps.print(...)
	//node.getNext()
	//node.toString() 

	//recursive call or iterate 
	//void printNode
	//void printList
	//¡¦ etc	
	
	private void printList(Node head)
	{
		if(head==null)
		{
			ps.print("( ) ");
			
			return;
		}

		ps.print("( ");
		printNode(head);
		ps.print(")  ");
	}

	public void printNode(Node head)
	{
		if(head!=null)
		{
			if(head instanceof ListNode)
			{
				ListNode ln = (ListNode) head;
				printList(ln.value);
			}
			else if(head instanceof QuoteNode)
			{
				QuoteNode qn = (QuoteNode) head;
				printQuote(qn.value);
			}
			else
			{
				ps.print("[" + head + "] ");
			}
			
			printNode(head.getNext());
		}
	}
	
	private void printQuote(Node head)
	{
		ps.print("'");
		printNode(head);
	}
}