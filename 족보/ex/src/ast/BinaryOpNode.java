package ast;

public class BinaryOpNode extends Node{
	public enum BinType { MINUS, PLUS, TIMES, DIV, LT, GT, EQ }
	public BinType value;
	
	
	@Override
	public String toString(){
		return value.name();
	}


	@Override
	public void copyValue(Node node) {
		// TODO Auto-generated method stub
		this.value = ((BinaryOpNode) node).value;
	}


	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		if(this==obj) return true;
		if(!(obj instanceof BinaryOpNode)) return false;
		if(!super.equals(obj)) return false;
		BinaryOpNode tmp = (BinaryOpNode) obj;
		if(this.value==tmp.value) return true;
		return false;
	}
	
	
}
