package cuteParser2;

import java.io.PrintStream;

import cuteParser2.Node.Type;

public class Printer {
	PrintStream ps;    
	ListNode l_node;
	public Printer(PrintStream ps){ 
		this.ps = ps; 
	} 
	//Make your print class 
	Type temp;
	private void printList(Node head) {
		if (head == null) {
			ps.print("( ) ");
			return;
		}
		
		ps.print("( ");
		printNode(head);
		ps.print(")  ");
	}

	public void printNode(Node head) {
		if (head != null) {
			if(head.getType() == temp.QUOTED){
				ps.print("\'");
			}
			if (head instanceof ListNode) {
				ListNode ln = (ListNode) head;
				printList(ln.value);
			} else {
				ps.print("[" + head + "] ");
			}
			
			printNode(head.getNext());
		}
	}
}