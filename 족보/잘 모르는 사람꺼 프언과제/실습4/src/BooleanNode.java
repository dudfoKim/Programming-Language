package cute14Parser;

public class BooleanNode extends Node {
	public final boolean value;

	public BooleanNode(Type type, boolean value) {
		super(type);
		this.value = value;
	}

	@Override
	public String toString() {
		return Boolean.toString(value);
	}
}
