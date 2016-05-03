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

	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (!(obj instanceof IdNode)) 
			return false;
		IdNode i = (IdNode)obj;
		return ((this.value.compareTo(i.value) == 0) && this.type == i.type);
	}
}