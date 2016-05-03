/*
 * ������ȣ : PL08-2
 * �й� : 02�й�
 * �� ��ȣ : 15
 * ���� �Ҽ� : �泲���б� �������� ��ǻ�Ͱ��а�
 * �й� �� ���� : 201002384(���ö) 201002436(������)
 */


package cute14Projectitem3;

public class IntNode extends Node {
	public final int value;

	public IntNode(Type type, int value) {
		super(type);
		this.value = value;
	}
	
	public boolean equals(Object obj){
		if (obj == this)
			return true;
		if (!(obj instanceof IntNode))
			return false;
		
		IntNode i = (IntNode)obj;
			return (this.value == i.value && this.type == i.type);
	}

	@Override
	public String toString() {
		return Integer.toString(value);
	}
}