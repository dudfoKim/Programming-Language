/*
 * ���� ��ȣ : hw07
 * �й� : 00�й�
 * �Ҽ� : ��ǻ�Ͱ��а�
 * �й� : 201000869
 * �̸� : ���ؿ�
 */

package hw07_Reconizing_Token_u00;

/*
 * boolean type�� ���� node�� ������.
 */
public class BooleanNode extends Node{ 
	public final boolean value; 
	public BooleanNode(Type type, boolean value) { 
		super(type); 
		this.value = value; 
	} 

	@Override 
	public String toString(){ 
		return "["+((value)?"#T":"#F")+"]"; 
	} 

	public boolean equals(Object obj) {
		
		//���� OID�� ���� ��� true�� ��ȯ
		if (obj == this) 
			return true; 

		//�ٸ� type�� node�� false�� ��ȯ
		if (!(obj instanceof BooleanNode)) 
			return false; 

		BooleanNode i = (BooleanNode)obj; 
		
		//value�� type�� ������ Ȯ���ϰ� ������� return�Ѵ�.
		return (this.value == i.value && this.type == i.type); 

	} 
} 
