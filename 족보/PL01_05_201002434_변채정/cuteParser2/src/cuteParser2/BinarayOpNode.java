package cuteParser2;

public class BinarayOpNode extends Node{
	public enum BinType { MINUS, PLUS, TIMES, DIV, LT, GT, EQ }
	public final BinType value;
	public BinarayOpNode(Type type, BinType value) {
		super(type);
		this.value = value;
	}
	@Override
	public String toString(){
		return value.name();
	}
}