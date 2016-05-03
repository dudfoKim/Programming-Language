/*
 * 과제 번호 : hw05 
 * 분반 : 00분반
 * 소속 : 컴퓨터공학과
 * 학번 : 201000869
 * 이름 : 최준우
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
	 * node객체의 정의된 toString에 따라 객체의 정보를 출력한다.
	 */
	void printNode(Node node)
	{
		ps.print(node+" ");
	}
	
	/*
	 * parsing된 list를 출력한다.
	 * 각 list는 linked list로 구성되어 있으므로,
	 * list를 iteration하여 출력한다.
	 * 이 때, iteration을 하다 list node를 만나면
	 * 중첩 list를 만난 case이므로 printList를 재귀 호출한다.
	 */
	void printList(Node node)
	{
		Node nodeTemp = node;
		
		for(;nodeTemp!=null;nodeTemp=nodeTemp.next)
		{
			//type을 확인하여 quoted이면 '를 찍는다.
			if(nodeTemp.type.equals(Type.QUOTED))
				ps.print('\'');
			//nodeTemp의 class name이 listNode의 class name과 같으면
			//nodeTemp는 ListNode형이므로 ListNode로 casting이 가능하다.
			if(nodeTemp.getClass().equals(ListNode.class))
			{
				//따라서 nodeTemp를 printList할 수 있다.
				ps.print("( ");
				printList(((ListNode) nodeTemp).value);
				ps.print(") ");
			}
			else
				printNode(nodeTemp);
		}
	}
	//… etc 
}