package cuteParser2;


public class FunctionNode extends Node{
	public enum FunctionType { DEFINE, LAMBDA, COND, NOT, CDR, CAR, CONS, EQ_Q, NULL_Q, ATOM_Q }
	public final FunctionType value;
	public FunctionNode(Type type, FunctionType value) {
		super(type);
		this.value = value;
	}
	@Override
	public String toString(){
		return value.name();
	}

	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (!(obj instanceof FunctionNode)) return false;
		FunctionNode i = (FunctionNode)obj;
		return (this.value == i.value && this.type == i.type);
	}

}