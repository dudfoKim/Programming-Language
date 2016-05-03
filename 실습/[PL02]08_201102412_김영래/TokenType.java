package built;

import java.util.HashMap;
import java.util.Map;

public enum TokenType
{
	// 1) 괄호 안에 TokenType에 맞는 state number 입력
	INT(1), ID(2), MINUS(3), PLUS(4), 
	L_PAREN(5), R_PAREN(6), TRUE(7), FALSE(8), 
	TIMES(9), DIV(10), LT(11), GT(12), EQ(13), 
	APOSTROPHE(14), DEFINE(15), LAMBDA(16), 
	COND(17), QUOTE(18), NOT(19), CAR(20), CDR(21), 
	CONS(23), ATOM_Q(24), NULL_Q(25), EQ_Q(26);
	
	private final int finalState;

	TokenType(int finalState)
	{
		this.finalState = finalState;
	}

	public static TokenType keyWordCheck(String str)
	{
		// if not keyword return null
		
		return KeyWord.m.get(str); 
	}

	public int getFinalState()
	{
		return finalState;
	}

	private enum KeyWord 
	{
		DEFINE("define", TokenType.DEFINE), 
		LAMBDA("lambda", TokenType.LAMBDA),
		// 2) 위와 같은 방법으로 나머지 키워드 작성
		COND("cond", TokenType.COND), 
		QUOTE("quote", TokenType.QUOTE), 
		NOT("not", TokenType.NOT), 
		CAR("car", TokenType.CAR), 
		CDR("cdr",TokenType.CDR), 
		CONS("cons", TokenType.CONS), 
		ATOM_Q("atom?",TokenType.ATOM_Q), 
		NULL_Q("null?", TokenType.NULL_Q), 
		EQ_Q("eq?", TokenType.EQ_Q),
		APOSTROPHE("\'", TokenType.APOSTROPHE);

		final String word;
		final TokenType type;

		KeyWord(String word, TokenType type)
		{
			this.word = word;
			this.type = type;
		}

		private static final Map<String, TokenType> m = new HashMap<String, TokenType>();
		
		static 
		{
			for (KeyWord k : KeyWord.values()) 
			{
				m.put(k.word, k.type);
			}
		}
	}
}