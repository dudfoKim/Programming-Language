package cute14BuiltInFunction2;

public class IdNode extends Node {
	public final String value;

	public IdNode(Type type, String value) {
		super(type);
		this.value = value;
	}

	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (!(obj instanceof IdNode))
			return false;
		
		IdNode i = (IdNode)obj;
		
		return (this.value.equals(i.value) && this.type == i.type);
	}

	@Override
	public String toString() {
		return value;
	}
}