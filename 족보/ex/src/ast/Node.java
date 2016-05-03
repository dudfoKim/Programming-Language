package ast;

public abstract class Node {
	Node next;
	public Node() {
		this.next = null;
	}
	public void setNext(Node next){
		this.next = next;
	}
	public void setLastNext(Node next){
		if(this.next != null)
			this.next.setLastNext(next);
		else
			this.next = next;
	}
	public Node getNext(){
		return next;
	}
	
	
	@Override
	public boolean equals(Object arg0) {
		// TODO Auto-generated method stub
		if(this==arg0) return true;
		if(!(arg0 instanceof Node)) return false;

		Node temp = (Node) arg0;
		if(next==null&&temp.next==null) return true;
		if(next==null^temp.next==null) return false;
		if(this.next.equals(temp.next)) return true;
		else return false;
		
	}
	public abstract void copyValue(Node node);
}
