package ast;

public class IdNode extends Node{
	public final String value;
	public IdNode(Type type, String value){
		super(type);
		this.value = value.replace("\"","").replace("\\n", System.lineSeparator());
	}
	
	@Override
	public String toString(){
		return "ID: " + value;
	}
}
