package cuteParser2;




public class IdNode extends Node{
	public final String value;
	public IdNode(Type type, String value){
		super(type);
		this.value = value;
	}
	@Override
	public String toString(){
		return "ID: " + value;
	}
}