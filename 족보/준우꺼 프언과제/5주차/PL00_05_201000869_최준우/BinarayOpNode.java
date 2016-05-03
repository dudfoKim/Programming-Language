/*
 * ���� ��ȣ : hw05
 * �й� : 00�й�
 * �Ҽ� : ��ǻ�Ͱ��а�
 * �й� : 201000869
 * �̸� : ���ؿ�
 */

package hw05_Reconizing_Token_u00;

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
} 