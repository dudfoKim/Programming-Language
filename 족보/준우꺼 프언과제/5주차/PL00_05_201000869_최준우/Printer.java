/*
 * ���� ��ȣ : hw05 
 * �й� : 00�й�
 * �Ҽ� : ��ǻ�Ͱ��а�
 * �й� : 201000869
 * �̸� : ���ؿ�
 */

package hw05_Reconizing_Token_u00;

import hw05_Reconizing_Token_u00.Node.Type;

import java.io.PrintStream;

public class Printer { 

	PrintStream ps; 

	public Printer(PrintStream ps) { 
		this.ps = ps; 
	} 

	//Make your print class 
	//ps.print(...) 
	//node.getNext() 
	//node.toString() 

	//recursive call or iterate
	
	/*
	 * node��ü�� ���ǵ� toString�� ���� ��ü�� ������ ����Ѵ�.
	 */
	void printNode(Node node)
	{
		ps.print(node+" ");
	}
	
	/*
	 * parsing�� list�� ����Ѵ�.
	 * �� list�� linked list�� �����Ǿ� �����Ƿ�,
	 * list�� iteration�Ͽ� ����Ѵ�.
	 * �� ��, iteration�� �ϴ� list node�� ������
	 * ��ø list�� ���� case�̹Ƿ� printList�� ��� ȣ���Ѵ�.
	 */
	void printList(Node node)
	{
		Node nodeTemp = node;
		
		for(;nodeTemp!=null;nodeTemp=nodeTemp.next)
		{
			//type�� Ȯ���Ͽ� quoted�̸� '�� ��´�.
			if(nodeTemp.type.equals(Type.QUOTED))
				ps.print('\'');
			//nodeTemp�� class name�� listNode�� class name�� ������
			//nodeTemp�� ListNode���̹Ƿ� ListNode�� casting�� �����ϴ�.
			if(nodeTemp.getClass().equals(ListNode.class))
			{
				//���� nodeTemp�� printList�� �� �ִ�.
				ps.print("( ");
				printList(((ListNode) nodeTemp).value);
				ps.print(") ");
			}
			else
				printNode(nodeTemp);
		}
	}
	//�� etc 
}