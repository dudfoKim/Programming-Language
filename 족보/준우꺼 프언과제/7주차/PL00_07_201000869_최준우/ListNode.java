/*
 * ���� ��ȣ : hw07
 * �й� : 00�й�
 * �Ҽ� : ��ǻ�Ͱ��а�
 * �й� : 201000869
 * �̸� : ���ؿ�
 */

package hw07_Reconizing_Token_u00;

/*
 * list�� ���� node�� ������.
 */
public class ListNode extends Node{ 
	public final Node value; 

	public ListNode(Type type, Node value) { 
		super(type); 
		this.value = value; 
	} 

	//listNode�� ���� OID�� ���� case�� ���ؼ��� true�� ��ȯ�Ѵ�.
	public boolean equals(Object obj) {
		if (obj == this) 
			return true; 
		
		return false;
	} 
}