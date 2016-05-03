package ast;

public class FunctionNode extends Node{
	public enum FunctionType { DEFINE, LAMBDA, COND, NOT, CDR, CAR, CONS, EQ_Q, NULL_Q, ATOM_Q }
	public FunctionType value;
	
	@Override
	public String toString(){
		return value.name();
	}

	@Override
	public void copyValue(Node node) {
		// TODO Auto-generated method stub
		this.value = ((FunctionNode) node).value;
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		if(this==obj) return true;
		if(!(obj instanceof FunctionNode)) return false;
		if(!super.equals(obj)) return false;
		FunctionNode tmp = (FunctionNode) obj;
		if(this.value==tmp.value) return true;
		return false;
	}
	
	
}
