package cute14BuiltInFunction2;

public class BooleanNode extends Node {

	public final boolean value;

	public BooleanNode(Type type, boolean value) {
		super(type);
		this.value = value;
	}
	
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (!(obj instanceof BooleanNode))
			return false;
		
		BooleanNode i = (BooleanNode)obj;
			return (this.value == i.value && this.type == i.type);
	}


	@Override
	public String toString() {
		return Boolean.toString(value).toUpperCase();
	}
}
