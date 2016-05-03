/*
 * ���� ��ȣ : hw06
 * �й� : 00�й�
 * �Ҽ� : ��ǻ�Ͱ��а�
 * �й� : 201000869
 * �̸� : ���ؿ�
 */

package hw06_Reconizing_Token_u00;

/*
 * �����ڿ� ���� Node�� ������.
 */
public class BinarayOpNode extends Node{ 
	public enum BinType { MINUS, PLUS, TIMES, DIV, LT, GT, EQ, APOSTROPHE } 
	public final BinType value; 

	public BinarayOpNode(Type type, BinType value) { 
		super(type);
		this.value = value; 
	} 

	@Override 
	public String toString(){ 
		return "["+value.name()+"]"; 
	}

	@Override
	public boolean equals(Object obj) {
		//���� OID�� ���� ��� true�� ��ȯ
		if (obj == this) 
			return true; 

		//�ٸ� type�� node�� false�� ��ȯ
		if (!(obj instanceof BinarayOpNode)) 
			return false; 

		BinarayOpNode i = (BinarayOpNode)obj; 

		//value�� type�� ������ Ȯ���ϰ� ������� return�Ѵ�.
		return (this.value==i.value && this.type == i.type); 

	} 
} 