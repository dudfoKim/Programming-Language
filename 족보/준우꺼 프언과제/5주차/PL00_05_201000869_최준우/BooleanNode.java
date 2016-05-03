/*
 * 과제 번호 : hw05
 * 분반 : 00분반
 * 소속 : 컴퓨터공학과
 * 학번 : 201000869
 * 이름 : 최준우
 */

package hw05_Reconizing_Token_u00;

/*
 * boolean type에 대한 node를 정의함.
 */
public class BooleanNode extends Node{ 
	public final boolean value; 
	public BooleanNode(Type type, boolean value) { 
		super(type); 
		this.value = value; 
	} 

	@Override 
	public String toString(){ 
		return "["+Boolean.toString(value)+"]"; 
	} 
} 
