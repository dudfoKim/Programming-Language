package cute14_Parser;

import java.util.HashMap;
import java.util.Map;


public enum TokenType{//TokenType별 최종 State 정의
	INT(2), 
	ID(3), 
	MINUS(4), PLUS(5),
	APOSTROPHE(6), EQ(7), L_PAREN(8), R_PAREN(9),
	TIMES(10), DIV(11), LT(12), GT(13),//
	TRUE(14), FALSE(15),
	DEFINE(16), LAMBDA(17), COND(18), QUOTE(19),
	NOT(20), CAR(21), CDR(22), CONS(23),
	ATOM_Q(24), NULL_Q(25), EQ_Q(26);
	
	final int finalState;
	TokenType(int finalState) {
		this.finalState = finalState;
	}
	
	public static TokenType keyWordCheck(String str){
		return KeyWord.m.get(str); //if not keyword return null
	}
	private enum KeyWord{//TokenType에 대한 KeyWokrd 정의
		DEFINE("define", TokenType.DEFINE),
		LAMBDA("lambda", TokenType.LAMBDA),
		COND("cond",TokenType.COND),
		QUOTE("quote",TokenType.QUOTE),
		NOT("not",TokenType.NOT),
		CDR("cdr",TokenType.CDR),
		CAR("car",TokenType.CAR),
		CONS("cons",TokenType.CONS),
		EQ_Q("eq?",TokenType.EQ_Q),
		NULL_Q("null?",TokenType.NULL_Q),
		ATOM_Q("atom?",TokenType.ATOM_Q);
		
		
		final String word;
		final TokenType type;
		KeyWord(String word, TokenType type){
			this.word = word;
			this.type = type;
		}
		private static final Map<String, TokenType> m = new HashMap<String, TokenType>();
		static{
			for(KeyWord k : KeyWord.values()){
				m.put(k.word, k.type);
			}
		}
	}
}
