/*
 * ���� ��ȣ : hw09
 * �й� : 00�й�
 * �Ҽ� : ��ǻ�Ͱ��а�
 * �й� : 201000869
 * �̸� : ���ؿ�
 */

package hw10_Reconizing_Token_u00;

/*
 * int�� ���� node�� ������.
 */
public class IntNode extends Node { 
	public final int value; 
	public IntNode(Type type,int value) { 
		super(type);
		this.value = value;
	} 

	@Override 
	public String toString(){ 
		return Integer.toString(value); 
	} 

	public boolean equals(Object obj) {
		//���� OID�� ���� ��� true�� ��ȯ
		if (obj == this) 
			return true; 

		//�ٸ� type�� node�� false�� ��ȯ
		if (!(obj instanceof IntNode)) 
			return false; 

		IntNode i = (IntNode)obj; 

		//value�� type�� ������ Ȯ���ϰ� ������� return�Ѵ�.
		return (this.value==i.value && this.type == i.type); 

	} 
} 