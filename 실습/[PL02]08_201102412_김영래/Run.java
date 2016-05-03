package built;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class Run
{
	private int transM[][];
	private boolean accept[];
	private String source;
	private StringTokenizer st;

	public Run(String source) 
	{
		// 3) state �� �� ��ŭ �迭 �ʱ�ȭ
		this.transM = new int[27][128];
		this.accept = new boolean[]
		{
				false, true, true, true, true, true, true, true, true, true, true, true, true, true, true, false
		};
		
		this.source = source == null ? "" : source;
		st = new StringTokenizer(source, " ");
		init_TM();
	}

	private void init_TM() 
	{
		for(int i=0; i < transM.length; i++)
		{
			for (int j=0; j < transM[i].length; j++)
			{
				transM[i][j] = -1;
			}
		}
		
		int i;
		// int = 1 , id = 2
		
		for (i = '0'; i < '9'; i++) // digit
		{
			transM[0][i] = 1;
			transM[1][i] = 1; // digit
			transM[2][i] = 2; // id
			transM[3][i] = 1; // '-'
			transM[4][i] = 1; // '+'
		}
		for (i = 'a'; i < 'z'; i++) // alpha
		{
			transM[0][i] = 2;
			transM[2][i] = 2;
		}
		for (i = 'A'; i < 'Z'; i++) 
		{
			transM[0][i] = 2;
			transM[2][i] = 2;
		}
		// 4) '(', ')', '+', '-', '*', '/', '<', '=', '>', '\'', '#T', '#F', #��
		// ���ؼ� �۵��ϵ��� �ۼ�
		transM[2]['?'] = 2;
		transM[0]['('] = 5;
		transM[0][')'] = 6;
		transM[0]['+'] = 4;
		transM[0]['-'] = 3;
		transM[0]['*'] = 9;
		transM[0]['/'] = 10;
		transM[0]['<'] = 11;
		transM[0]['='] = 13;
		transM[0]['>'] = 12;
		transM[0]['\''] = 14;
		transM[0]['#'] = 15;
		transM[15]['T'] = 7;
		transM[15]['F'] = 8;
	}

	private Token nextToken() 
	{
		int StateOld = 0, StateNew;
		
		if(!st.hasMoreTokens())
		{
			return null;
		}
		// �� ���� ��ū�� ����
		String temp = st.nextToken();
		Token result = null;

		for (int i = 0; i < temp.length(); i++) 
		{
			StateNew = transM[StateOld][temp.charAt(i)];
			// �Է¹��ڷ� ���ο� ���� �Ǻ�
			
			// 5) ������ �ش��ϴ� ������ ����� �׿� �ش��ϴ� �ڵ� �ۼ�
			if (StateNew == -1) 
			{	
				if (!accept[StateOld])
				{
					System.out.println("acceptState error: " + temp);
					
					return result;
				}
				
				break;
			}
			
			StateOld = StateNew;
		}
		for (TokenType t : TokenType.values()) 
		{
			if (t.getFinalState()==StateOld)
			{
				TokenType keyWord = TokenType.keyWordCheck(temp);
				
				if (keyWord!=null)
				{
					result = new Token(keyWord, temp);
				}
				else
				{
					result = new Token(t, temp);
				}
				
				break;
			}
		}
		
		return result;
	}

	public List<Token> tokenize()
	{
		List<Token> tokens = new ArrayList<Token>();
		Token t = null;
		
		t = nextToken();
		// ��ū List�� ��ȯ�ϵ��� �ۼ�
		while (t != null) 
		{
			tokens.add(t);
			t = nextToken();
		}
		
		return tokens;
	}

	public static void main(String[] args) throws IOException 
	{
		//read file...
		FileReader fr = new FileReader("as08.txt");
		BufferedReader br = new BufferedReader(fr);
		
		String source = br.readLine();
		Run s = new Run(source);
		List<Token> tokens = s.tokenize();

		BasicParser p = new BasicParser(tokens);
		Node node = p.parseExpr();
		CuteInterpreter i = new CuteInterpreter();
		Node interpreterNode = i.runExpr(node);

		System.out.println("========== �Է��� �� ==========");
		Printer pt = new Printer(System.out);
		pt.printNode(node);
		
		System.out.println("\n\n\n========== ������� �� ==========");		

		for(Node n=node; n!=null; n=n.getNext())
		{
			Node ir = i.runExpr(n);
			pt.printNode(interpreterNode);
		}
	}
}