package ast;

public abstract class Node {
	public enum Type {QUOTED, NOT_QUOTED}
	protected Type type;
	Node next;
	
	public Node(Type type) {
		this.type = type;
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
	
	public void setType(Type type){
		this.type = type;
	}
	
	public Type getType(){
		return type;
	}
}
