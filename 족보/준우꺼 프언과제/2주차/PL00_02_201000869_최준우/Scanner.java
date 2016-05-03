package hw02_Reconizing_Token_u00;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Scanner {

	private int transM[][]; 
	private boolean accept[];
	private String source; 
	private int pos; 

	public Scanner(String source) { 
		this.accept = new boolean[]{ false, false, true, 
				true };// Final states: 2,3 
		this.transM = new int[4][128]; 
		this.source = source == null ? "" : source; 
		init_TM(); 
	} 

	/**
	 * Transition Matrix�� �ʱ�ȭ �Ѵ�.
	 * 
	 * state 0 : �ʱ� state
	 * state 1 : ���� �Է� ��� ����
	 * state 2 : integer ��� ����
	 * state 3 : id ��� ����
	 * 
	 * �ʱ� state 0 ���� �����Ͽ�
	 * ���ڰ� ��� �� �� 2�� state�� 
	 * ���ĺ��� ��� �� �� 3�� state��
	 * -�� ��� ���� 1�� state�� state ��ȯ�Ѵ�.
	 * 
	 * state 1���� ���ڰ� ������ 2�� state��
	 * �� �� ���ڰ� ������ -1 state�� ��ȯ�Ѵ�.
	 * 
	 * state 2���� ���ڰ� ������ �ٽ� 2�� state��
	 * �� �� ���ڰ� ������ -1 state�� ��ȯ�Ѵ�.
	 * 
	 * state 3���� ���ڳ� ���ĺ��� ������ �ٽ� 3�� state��
	 * �� �� ���ڰ� ������ -1 state�� ��ȯ�Ѵ�.
	 */
	private void init_TM() { 

		for(int i=0; i<4; i++)
		{
			if(i==0)
			{
				for(int j=0; j<128; j++)
				{
					if('0'<=j && j<='9')
						transM[i][j]=2;
					else if(('A'<=j && j<='Z')||('a'<=j && j<='z'))
						transM[i][j]=3;
					else if(j=='-')
						transM[i][j]=1;
					else
						transM[i][j]=-1;
				}
			}

			else if(i==1)
			{
				for(int j=0; j<128; j++)
				{
					if('0'<=j && j<='9')
						transM[i][j]=2;
					else
						transM[i][j]=-1;
				}
			}
			else if(i==2)
			{
				for(int j=0; j<128; j++)
				{
					if('0'<=j && j<='9')
						transM[i][j]=2;
					else
						transM[i][j]=-1;
				}
			}
			else if(i==3)
			{
				for(int j=0; j<128; j++)
				{
					if('0'<=j && j<='9')
						transM[i][j]=3;
					else if(('A'<=j && j<='Z')||('a'<=j && j<='z'))
						transM[i][j]=3;
					else
						transM[i][j]=-1;
				}
			}
		}

	} 
	/**
	 * as02.txt�� string ������ tokenize�Ѵ�.
	 * string���� ���ʿ��� whitespace�� ����� space�� �����ڷ� �Ͽ�
	 * ���� ������ state�� transition�Ѵ�.
	 * space�� ������ ���±��� transition�� ������ token�� ��ȯ�ϸ�
	 * as02.txt�� ��� string�� �а� ���� null�� �����Ѵ�.
	 * ���� ���� ���� ���� ���ڰ� ������ -1 state�� �� 
	 * acceptState error�� �߻��ϰ� tokenize�� �����Ѵ�.
	 * @return result - tokenize�Ͽ� ���� token�� ��ȯ�Ѵ�.
	 */
	private Token nextToken(){ 
		char c = Character.SPACE_SEPARATOR; 
		StringBuffer sb = new StringBuffer(); 
		Token result = null; 

		int StateOld = 0, StateNew; 
		boolean acceptState = false;


		while( pos < source.length() ){ 
			if(!Character.isWhitespace( c = source.charAt(pos))) 
				break; 
			pos++; 
		}//�ǹ� ���� ���鹮�� ���� 

		if( pos >= source.length() ) 
			return null; //input data�� �� �̻� ���� ��� 

		while (!acceptState) { 
			StateNew = transM[StateOld][c]; //�Է¹��ڷ� ���ο� ���� �Ǻ� 

			if (StateNew == -1) { // �Էµ� ������ ����(StateNew)�� reject�϶� ��������(StateOld)�� ���·� reject, accept�Ǻ� 
				if (accept[StateOld]) { 
					acceptState = true; 
					break; 
				} // accept 
				else { acceptState = false; break; } // reject 
			} 

			sb.append(c); 
			StateOld = StateNew; 

			pos++; 
			if(pos < source.length()) 
				c = source.charAt(pos); 
			else 
				c = 0; // Null Character 
		} 

		if(acceptState){ 
			for (TokenType t : TokenType.values()){ 
				if(t.finalState == StateOld){ 
					result = new Token(t, sb.toString()); 
					break; 
				} 
			} 
		}else
		{
			sb.append(c);
			System.out.println(String.format("acceptState error by \"%s\", position : %d\n", sb.toString(), this.pos));//  4
		}
		return result; 
	} 

	/**
	 * Token�� ��ü�� �����ϴ� ArrayList �����Ͽ�
	 * nextToken()�� null�� �� ���� list�� Token�� �����Ѵ�.
	 * @return list - as02.txt�� Token List
	 */
	public List<Token> tokenize() { 
		//Token ����Ʈ��ȯ, nextToken()�̿�.. 

		List<Token> list = new ArrayList<Token>();

		Token temp = this.nextToken();

		while(temp != null)
		{
			list.add(temp);
			temp = this.nextToken();
		}
		return list;
	} 

	/**
	 * as02.txt ������ filereader�� bufferedReader�� ���Ͽ�
	 * as02.txt�� text�� source�� �����Ͽ� Scanner ��ü�� ����
	 * source�� tokenize�Ѵ�. �� �� list�� ���� token���� ����Ѵ�.
	 * @param args
	 */
	@SuppressWarnings("resource")
	public static void main(String[] args){ 
		//txt file to String
		try
		{
			File file = new File("./as02.txt");
			BufferedReader buf = new BufferedReader(new FileReader(file));

			String source = buf.readLine();
			Scanner s = new Scanner(source); 
			List<Token> tokens = s.tokenize(); 
			//print 
			System.out.println(tokens.toString());
		}
		catch(FileNotFoundException e)
		{
			System.out.println("No File, AbNormal Terminate");
			e.printStackTrace();
		}
		catch(IOException e)
		{
			System.out.println("IOException, AbNormal Terminate");
			e.printStackTrace();
		}



	} 

	/**
	 * Token�� �ش� token�� type�� string ���� lexme�� ���� ������
	 * source���� �ǹ̸� ���� ���� ���� ������ �ɰ�����.
	 * @author choijunwoo
	 */

	public static class Token { 
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

	/**
	 * token�� ���� finalstate�� ID�� INT�� �����Ѵ�.
	 * @author choijunwoo
	 *
	 */
	public enum TokenType{ 
		ID(3), INT(2); 

		private final int finalState; 

		TokenType(int finalState) 
		{ 
			this.finalState = finalState; 
		} 
	}

}
