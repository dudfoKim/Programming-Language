/*
 * ������ȣ : PL08-2
 * �й� : 02�й�
 * �� ��ȣ : 15
 * ���� �Ҽ� : �泲���б� �������� ��ǻ�Ͱ��а�
 * �й� �� ���� : 201002384(���ö) 201002436(������)
 */

//define node�� ���� ���ǰ� �ִ� Ŭ����
//
package cute14Projectitem3;

public class defineNode{
	
	private String tempString;
	private Node tempNode;
	
	public defineNode(String tempString, Node tempNode)
	{
		this.tempString=tempString;
		this.tempNode=tempNode;
		
	}

	public void setTempString(String tempString) {
		this.tempString = tempString;
	}

	public void setTempNode(Node tempNode) {
		this.tempNode = tempNode;
	}

	public String getTempString() {
		return tempString;
	}

	public Node getTempNode() {
		return tempNode;
	}
	
}
