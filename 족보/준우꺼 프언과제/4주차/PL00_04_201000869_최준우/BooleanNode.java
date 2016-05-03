/*
 * ���� ��ȣ : hw04 
 * �й� : 00�й�
 * �Ҽ� : ��ǻ�Ͱ��а�
 * �й� : 201000869
 * �̸� : ���ؿ�
 */

package hw04_Reconizing_Token_u00;

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
		return "["+Boolean.toString(value)+"]"; 
	} 
} 
