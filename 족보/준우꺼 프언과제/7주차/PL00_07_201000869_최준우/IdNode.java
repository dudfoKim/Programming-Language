/*
 * 과제 번호 : hw07
 * 분반 : 00분반
 * 소속 : 컴퓨터공학과
 * 학번 : 201000869
 * 이름 : 최준우
 */

package hw07_Reconizing_Token_u00;

/*
 * id에 대한 node를 정의함.
 */
public class IdNode extends Node{ 
	public final String value; 
	public IdNode(Type type, String value){ 
		super(type); 
		this.value = value; 
	} 

	@Override 
	public String toString(){ 
		return "[ "+ value+"]"; 
	}

	public boolean equals(Object obj) {
		//같은 OID를 가질 경우 true를 반환
		if (obj == this) 
			return true; 
		
		//다른 type의 node는 false를 반환
		if (!(obj instanceof IdNode)) 
			return false; 

		IdNode i = (IdNode)obj; 
		
		//value와 type이 같은지 확인하고 결과값을 return한다.
		return (this.value.equals(i.value) && this.type == i.type); 

	} 
} 