/*
 * ���� ��ȣ : hw04 
 * �й� : 00�й�
 * �Ҽ� : ��ǻ�Ͱ��а�
 * �й� : 201000869
 * �̸� : ���ؿ�
 */

package hw04_Reconizing_Token_u00;

/*
 * list�� ���� node�� ������.
 */
public class ListNode extends Node{ 
	public final Node value; 

	public ListNode(Type type, Node value) { 
		super(type); 
		this.value = value; 
	} 
}