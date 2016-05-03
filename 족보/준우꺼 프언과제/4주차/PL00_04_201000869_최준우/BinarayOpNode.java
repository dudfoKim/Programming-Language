/*
 * 과제 번호 : hw04 
 * 분반 : 00분반
 * 소속 : 컴퓨터공학과
 * 학번 : 201000869
 * 이름 : 최준우
 */

package hw04_Reconizing_Token_u00;

/*
 * 연산자에 대한 Node를 정의함.
 */
public class BinarayOpNode extends Node{ 
	public enum BinType { MINUS, PLUS, TIMES, DIV, LT, GT, EQ } 
	public final BinType value; 

	public BinarayOpNode(Type type, BinType value) { 
		super(type);
		this.value = value; 
	} 

	@Override 
	public String toString(){ 
		return "["+value.name()+"]"; 
	} 
} 