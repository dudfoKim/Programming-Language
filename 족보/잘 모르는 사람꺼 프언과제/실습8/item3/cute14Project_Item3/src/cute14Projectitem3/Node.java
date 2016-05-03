/*
 * 과제번호 : PL08-2
 * 분반 : 02분반
 * 조 번호 : 15
 * 조원 소속 : 충남대학교 공과대학 컴퓨터공학과
 * 학번 및 성명 : 201002384(김범철) 201002436(성동욱)
 */

package cute14Projectitem3;

public abstract class Node {
	public enum Type {
		QUOTED, NOT_QUOTED
	}

	public Type type;
	Node next;

	public Node(Type type) {
		this.type = type;
		this.next = null;
	}
	
	public Type getType() {
		return type;
	}
	
	public void setType(Type type){
		this.type=type;
	}

	public void setNext(Node next) {
		this.next = next;
	}

	public void setLastNext(Node next) {
		if (this.next != null)
			this.next.setLastNext(next);
		else
			this.next = next;
	}

	public Node getNext() {
		return next;
	}
	

}
