package cute14Parser;

public abstract class Node {
	public enum Type {
		QUOTED, NOT_QUOTED
	};

	public final Type type;
	Node next;

	public Node(Type type) {
		this.type = type;
		this.next = null;
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
