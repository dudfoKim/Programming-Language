/*
 * ���� ��ȣ : hw05
 * �й� : 00�й�
 * �Ҽ� : ��ǻ�Ͱ��а�
 * �й� : 201000869
 * �̸� : ���ؿ�
 */

package hw05_Reconizing_Token_u00;

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
		return "[INT: " + Integer.toString(value)+"]"; 
	} 
} 