package cute14Parser;

public class IntNode extends Node {
	public final int value;

	public IntNode(Type type, int value) {
		super(type);
		this.value = value;
	}

	@Override
	public String toString() {
		return "INT: " + Integer.toString(value);
	}
}