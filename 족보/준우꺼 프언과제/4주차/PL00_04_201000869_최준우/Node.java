/*
 * 과제 번호 : hw04 
 * 분반 : 00분반
 * 소속 : 컴퓨터공학과
 * 학번 : 201000869
 * 이름 : 최준우
 */

package hw04_Reconizing_Token_u00;

/*
 * Super class Node를 정의한다.
 * 각 node들의 공통된 attribute를 정의한다.
 */
public abstract class Node { 
	public enum Type {QUOTED, NOT_QUOTED} 
	public final Type type; 
	Node next; 

	public Node(Type type) { 
		this.type = type; 
		this.next = null; 
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

	public Node getNext(){ 
		return next; 
	} 
} 


