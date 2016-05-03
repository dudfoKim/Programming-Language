/*
 * ������ȣ : PL08-2
 * �й� : 02�й�
 * �� ��ȣ : 15
 * ���� �Ҽ� : �泲���б� �������� ��ǻ�Ͱ��а�
 * �й� �� ���� : 201002384(���ö) 201002436(������)
 */

package cute14ProjectItem2;

public class ListNode extends Node {
	public final Node value;
	
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (!(obj instanceof ListNode))
			return false;
		
		ListNode i = (ListNode)obj;
			return (this.value == i.value && this.type == i.type);
	}

	public ListNode(Type type, Node value) {
		super(type);
		this.value = value;
	}
}