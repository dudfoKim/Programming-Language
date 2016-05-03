package cute14ProjectItem1;

public class ListNode extends Node {
	public final Node value;
	
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (!(obj instanceof ListNode))
			return false;
		
		ListNode i = (ListNode)obj;
			return (this.value == i.value && this.type == i.type);
	}

	public ListNode(Type type, Node value) {
		super(type);
		this.value = value;
	}
}