
public abstract class Node {
	public enum Type {
		QUOTED, NOT_QUOTED
	}

	public Type type;
	Node next;

	public Node(Type type) {
		this.type = type;
		this.next = null;
	}
	
	public Type getType() {
		return type;
	}
	
	public void setType(Type type){
		this.type=type;
	}

	public void setNext(Node next) {
		this.next = next;
	}

	public void setLastNext(Node next) {
		if (this.next != null)
			this.next.setLastNext(next);
		else
			this.next = next;
	}

	public Node getNext() {
		return next;
	}
}
