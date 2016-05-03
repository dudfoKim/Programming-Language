/*
 * 과제 번호 : hw07
 * 분반 : 00분반
 * 소속 : 컴퓨터공학과
 * 학번 : 201000869
 * 이름 : 최준우
 */

package hw07_Reconizing_Token_u00;

/*
 * Super class Node를 정의한다.
 * 각 node들의 공통된 attribute를 정의한다.
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
	//이번 과제에서는 type.QUOTED를 사용하기 때문에
	//type 설정자를 만든다.
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
	//EQ_Q 키워드를 구현하기 위해 추상 메서드를 선언한다.
	public abstract boolean equals(Object obj);
	
	public Type getType() {
		// TODO Auto-generated method stub
		return this.type;
	}
} 


