/*
 * ���� ��ȣ : hw07
 * �й� : 00�й�
 * �Ҽ� : ��ǻ�Ͱ��а�
 * �й� : 201000869
 * �̸� : ���ؿ�
 */

package hw07_Reconizing_Token_u00;

/*
 * id�� ���� node�� ������.
 */
public class IdNode extends Node{ 
	public final String value; 
	public IdNode(Type type, String value){ 
		super(type); 
		this.value = value; 
	} 

	@Override 
	public String toString(){ 
		return "[ "+ value+"]"; 
	}

	public boolean equals(Object obj) {
		//���� OID�� ���� ��� true�� ��ȯ
		if (obj == this) 
			return true; 
		
		//�ٸ� type�� node�� false�� ��ȯ
		if (!(obj instanceof IdNode)) 
			return false; 

		IdNode i = (IdNode)obj; 
		
		//value�� type�� ������ Ȯ���ϰ� ������� return�Ѵ�.
		return (this.value.equals(i.value) && this.type == i.type); 

	} 
} 