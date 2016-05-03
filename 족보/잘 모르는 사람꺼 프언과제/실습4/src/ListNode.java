package cute14Parser;

public class ListNode extends Node {
	public final Node value;

	public ListNode(Type type, Node value) {
		super(type);
		this.value = value;
	}
}