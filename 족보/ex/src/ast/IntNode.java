package ast;


public class IntNode extends Node {
	public int value;
	
	@Override
	public String toString(){
		return "INT: " + Integer.toString(value);
	}

	@Override
	public void copyValue(Node node) {
		// TODO Auto-generated method stub
		this.value = ((IntNode) node).value;
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		if(this==obj) return true;
		if(!(obj instanceof IntNode)) return false;
		if(!super.equals(obj)) return false;
		IntNode tmp = (IntNode) obj;
		if(this.value==tmp.value) return true;
		return false;
	}
	
	
}