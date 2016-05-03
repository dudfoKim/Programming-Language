package ast;

public class QuoteNode extends Node {
	public Node value;

	@Override
	public void copyValue(Node node) {
		// TODO Auto-generated method stub
		this.value = ((QuoteNode) node).value;
	}

	@Override
	public boolean equals(Object arg0) {
		// TODO Auto-generated method stub
		if(this==arg0) return true;
		if(!(arg0 instanceof QuoteNode)) return false;
		//if(!super.equals(arg0)) return false;
		QuoteNode tmp = (QuoteNode) arg0;
		if(value.equals(tmp.value)) return true;
		return false;
	}
	
	
}
