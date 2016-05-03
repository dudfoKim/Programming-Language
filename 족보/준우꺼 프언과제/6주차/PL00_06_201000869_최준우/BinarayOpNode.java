/*
 * 과제 번호 : hw06
 * 분반 : 00분반
 * 소속 : 컴퓨터공학과
 * 학번 : 201000869
 * 이름 : 최준우
 */

package hw06_Reconizing_Token_u00;

/*
 * 연산자에 대한 Node를 정의함.
 */
public class BinarayOpNode extends Node{ 
	public enum BinType { MINUS, PLUS, TIMES, DIV, LT, GT, EQ, APOSTROPHE } 
	public final BinType value; 

	public BinarayOpNode(Type type, BinType value) { 
		super(type);
		this.value = value; 
	} 

	@Override 
	public String toString(){ 
		return "["+value.name()+"]"; 
	}

	@Override
	public boolean equals(Object obj) {
		//같은 OID를 가질 경우 true를 반환
		if (obj == this) 
			return true; 

		//다른 type의 node는 false를 반환
		if (!(obj instanceof BinarayOpNode)) 
			return false; 

		BinarayOpNode i = (BinarayOpNode)obj; 

		//value와 type이 같은지 확인하고 결과값을 return한다.
		return (this.value==i.value && this.type == i.type); 

	} 
} 