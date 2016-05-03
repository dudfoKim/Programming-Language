/*
 * 과제 번호 : hw07
 * 분반 : 00분반
 * 소속 : 컴퓨터공학과
 * 학번 : 201000869
 * 이름 : 최준우
 */

package hw07_Reconizing_Token_u00;

/*
 * list에 대한 node를 정의함.
 */
public class ListNode extends Node{ 
	public final Node value; 

	public ListNode(Type type, Node value) { 
		super(type); 
		this.value = value; 
	} 

	//listNode는 같은 OID를 갖는 case에 대해서만 true를 반환한다.
	public boolean equals(Object obj) {
		if (obj == this) 
			return true; 
		
		return false;
	} 
}