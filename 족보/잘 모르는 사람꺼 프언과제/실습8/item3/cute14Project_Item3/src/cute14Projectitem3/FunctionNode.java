/*
 * ������ȣ : PL08-2
 * �й� : 02�й�
 * �� ��ȣ : 15
 * ���� �Ҽ� : �泲���б� �������� ��ǻ�Ͱ��а�
 * �й� �� ���� : 201002384(���ö) 201002436(������)
 */

package cute14Projectitem3;

public class FunctionNode extends Node {
	public enum FunctionType
	{
		DEFINE, LAMBDA, COND, NOT, CDR, CAR, CONS, EQ_Q, NULL_Q, ATOM_Q
	};

	public final FunctionType value;

	public FunctionNode(Type type, FunctionType value)
	{
		super(type);
		this.value = value;
	}
	
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (!(obj instanceof FunctionNode))
			return false;
		
		FunctionNode i = (FunctionNode)obj;
			return (this.value == i.value && this.type == i.type);
	}

	@Override
	public String toString()
	{
		return value.name();
	}
}	