package cute14BuiltInFunction2;
public class BinarayOpNode extends Node {
	public enum BinType {
		MINUS, PLUS, TIMES, DIV, LT, GT, EQ
	}

	public final BinType value;

	public BinarayOpNode(Type type, BinType value) {
		super(type);
		this.value = value;
	}
	
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (!(obj instanceof BinarayOpNode))
			return false;
		
		BinarayOpNode i = (BinarayOpNode)obj;
			return (this.value == i.value && this.type == i.type);
	}


	@Override
	public String toString() {
		return value.name();
	}
}