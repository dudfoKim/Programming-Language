import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

	public class Scanner
	{
		public enum TokenType
		{
			ID(3), INT(2);
			private final int finalState;

			TokenType(int finalState)
			{
				this.finalState = finalState;
			}
		
		}

	public static class Token
	{
		public final TokenType type;
		public final String lexme;

		Token(TokenType type, String lexme)
		{
			this.type = type;
			this.lexme = lexme;
		}

		@Override
		public String toString() 
		{
			return String.format("[%s: %s]", type.toString(), lexme);
		}
	}

	private int transM[][];
	private boolean accept[];
	private String source;
	private int pos;

	public Scanner(String source)
	{
		this.accept = new boolean[] { false, false, true, true };// Final
																	// states:
																	// 2,3
		this.transM = new int[4][128];
		this.source = source == null ? "" : source;
		init_TM();
	}

	private void init_TM() {
		for (int i = 0; i < 4; i++) // 전부 -1로 초기화.
			for (int j = 0; j < 128; j++)
				transM[i][j] = -1;

		for (int i = 0; i < 4; i++) 
		{
			if (i == 3) { // 알파벳이 들어올경우 무조건 id이기 때문에 전부 3으로 초기화.
				for (int j = '0'; j <= '9'; j++)
					transM[i][j] = 3;

				for (int k = 'a'; k <= 'z'; k++)
					transM[i][k] = 3;

				for (int k = 'A'; k <= 'Z'; k++)
					transM[i][k] = 3;
			}

			else
			{   // 숫자들어올경우, 2 문자들어올경우 3으로 초기화.
				for (int j = '0'; j <= '9'; j++)
					transM[i][j] = 2;

				for (int k = 'a'; k <= 'z'; k++)
					transM[i][k] = 3;

				for (int k = 'A'; k <= 'Z'; k++)
					transM[i][k] = 3;
			}
			transM[i]['-'] = 1; // 음수일경우 1로 초기화.
		}
	}

	private Token nextToken()
	{
		char c = Character.SPACE_SEPARATOR;
		StringBuffer sb = new StringBuffer();
		Token result = null;
		int StateOld = 0, StateNew;
		boolean acceptState = false;
		
		while (pos < source.length())
		{
			if (!Character.isWhitespace(c = source.charAt(pos)))
				break;
			pos++;
		}//의미 없는 공백문자 무시
		
		if (pos >= source.length())
			return null; // input data가 더 이상 없을 경우
		while (!acceptState)
		{
			StateNew = transM[StateOld][c]; //입력문자로 새로운 상태 판별
			if (StateNew == -1)  // 입력된 문자의 상태(StateNew)가 reject일때 이전문자(StateOld)의 상태로 reject, accept판별
			{
									
				if (accept[StateOld])
				{
					acceptState = true;
					break;
				} // accept
				else
				{
					acceptState = false;
					break;
				} // reject
			}
			
			sb.append(c);
			StateOld = StateNew;
			pos++;
			if (pos < source.length())
				c = source.charAt(pos);
			else
				c = 0; // Null Character
		}
		
		if (acceptState)
		{
			for(TokenType t : TokenType.values())
			{
				if (t.finalState == StateOld)
				{
					result = new Token(t, sb.toString());
					break;
				}
			}
		} 
		
		else
			System.out.println(String.format("acceptState error %s\n",sb.toString()));

		return result;
	}

	public List<Token> tokenize() 
	{
		List<Token> list = new ArrayList<Token>(); // token형 array객체 생성.
		Token temp=null;
		
		while((temp = nextToken())!=null) // temp에 문자열중 문자하나의 문자형과 문자내용 저장.
			list.add(temp); // temp가 null이 아니면 문자가 있는것이므로 list에 추가.
		
		return list; 
	}
	
	public static void main(String[] args) throws IOException{
		
		
		BufferedReader br = new BufferedReader( new FileReader("as02.txt")); // 파일을 string으로 받아오기위해 BufferedReader사용
		String source = br.readLine(); //string으로 받아옴.
		Scanner s = new Scanner(source);
		
		List<Token> tokens = s.tokenize(); //tokenize()를 호출해 string 값들의 id와 int형 구분. 	
		
		for(int i=0 ; i<tokens.size() ; i++) // 출력
			System.out.println(tokens.get(i));
		
		br.close();
	}
}

	