/*
 * ���� ��ȣ : hw09
 * �й� : 00�й�
 * �Ҽ� : ��ǻ�Ͱ��а�
 * �й� : 201000869
 * �̸� : ���ؿ�
 */

package hw09_Reconizing_Token_u00;

/*
 * Keyword�� ���� node�� ������.
 */
public class FunctionNode extends Node{ 
	public enum FunctionType { DEFINE, LAMBDA, COND, NOT, CDR, CAR, CONS, EQ_Q, 
		NULL_Q, ATOM_Q } 
	public final FunctionType value; 
	public FunctionNode(Type type, FunctionType value) { 
		super(type); 
		this.value = value; 
	} 

	@Override 
	public String toString(){ 
		return value.name(); 
	}

	public boolean equals(Object obj) {
		//���� OID�� ���� ��� true�� ��ȯ
		if (obj == this) 
			return true;

		//�ٸ� type�� node�� false�� ��ȯ
		if (!(obj instanceof FunctionNode)) 
			return false; 

		FunctionNode i = (FunctionNode)obj; 
		
		//value�� type�� ������ Ȯ���ϰ� ������� return�Ѵ�.
		return (this.value == i.value && this.type == i.type); 

	} 
} 