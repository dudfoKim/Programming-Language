package cute14_Parser;

import java.io.PrintStream;

public class Printer {
	PrintStream ps;    
	ListNode l_node;
	public Printer(PrintStream ps){ 
		this.ps = ps; 
	} 
	//Make your print class 
	public void printNode(Node node){
		ps.print(" [" + node.toString() + "] ");
	}
	public void printList(Node node){
		ps.print("(");
		while(node != null){
			if(node.getClass() == ListNode.class){
				printList(((ListNode)node).value);
			}
			else{
				printNode(node);
			}
			node = node.next;
		}
		ps.print(")");

	}
}