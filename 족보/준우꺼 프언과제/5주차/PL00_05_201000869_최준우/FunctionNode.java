/*
 * 과제 번호 : hw05
 * 분반 : 00분반
 * 소속 : 컴퓨터공학과
 * 학번 : 201000869
 * 이름 : 최준우
 */

package hw05_Reconizing_Token_u00;

/*
 * Keyword에 대한 node를 정의함.
 */
public class FunctionNode extends Node{ 
	public enum FunctionType { DEFINE, LAMBDA, COND, NOT, CDR, CAR, CONS, EQ_Q, 
		NULL_Q, ATOM_Q } 
	public final FunctionType value; 
	public FunctionNode(Type type, FunctionType value) { 
		super(type); 
		this.value = value; 
	} 

	@Override 
	public String toString(){ 
		return "["+value.name()+"]"; 
	} 
} 