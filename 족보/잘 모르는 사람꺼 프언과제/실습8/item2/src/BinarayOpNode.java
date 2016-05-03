/*
 * ������ȣ : PL08-2
 * �й� : 02�й�
 * �� ��ȣ : 15
 * ���� �Ҽ� : �泲���б� �������� ��ǻ�Ͱ��а�
 * �й� �� ���� : 201002384(���ö) 201002436(������)
 */

package cute14ProjectItem2;
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