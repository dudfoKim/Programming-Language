/*
 * ���� ��ȣ : hw04 
 * �й� : 00�й�
 * �Ҽ� : ��ǻ�Ͱ��а�
 * �й� : 201000869
 * �̸� : ���ؿ�
 */

package hw04_Reconizing_Token_u00;

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
		return "[ID: " + value+"]"; 
	} 
} 