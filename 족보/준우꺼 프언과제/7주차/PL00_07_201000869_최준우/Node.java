/*
 * ���� ��ȣ : hw07
 * �й� : 00�й�
 * �Ҽ� : ��ǻ�Ͱ��а�
 * �й� : 201000869
 * �̸� : ���ؿ�
 */

package hw07_Reconizing_Token_u00;

/*
 * Super class Node�� �����Ѵ�.
 * �� node���� ����� attribute�� �����Ѵ�.
 */
public abstract class Node { 
	public enum Type {QUOTED, NOT_QUOTED} 
	public Type type; 
	Node next; 

	public Node(Type type) {
		this.type = type;
		this.next = null;
	}
	public Node getNext(){
		return next;
		
	}
	//�̹� ���������� type.QUOTED�� ����ϱ� ������
	//type �����ڸ� �����.
	public void setType(Type type){
		this.type = type;
	}
	public void setNext(Node next){
		this.next = next;
	}
	public void setLastNext(Node next){
		if(this.next != null)
			this.next.setLastNext(next);
		else
			this.next = next;
	}
	//EQ_Q Ű���带 �����ϱ� ���� �߻� �޼��带 �����Ѵ�.
	public abstract boolean equals(Object obj);
	
	public Type getType() {
		// TODO Auto-generated method stub
		return this.type;
	}
} 


