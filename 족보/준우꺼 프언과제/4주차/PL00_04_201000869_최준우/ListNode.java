/*
 * 과제 번호 : hw04 
 * 분반 : 00분반
 * 소속 : 컴퓨터공학과
 * 학번 : 201000869
 * 이름 : 최준우
 */

package hw04_Reconizing_Token_u00;

/*
 * list에 대한 node를 정의함.
 */
public class ListNode extends Node{ 
	public final Node value; 

	public ListNode(Type type, Node value) { 
		super(type); 
		this.value = value; 
	} 
}