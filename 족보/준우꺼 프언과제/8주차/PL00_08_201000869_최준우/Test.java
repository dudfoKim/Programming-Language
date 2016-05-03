/*
 * ���� ��ȣ : hw08
 * �й� : 00�й�
 * �Ҽ� : ��ǻ�Ͱ��а�
 * �й� : 201000869
 * �̸� : ���ؿ�
 */

package hw08_Reconizing_Token_u00;

import hw08_Reconizing_Token_u00.Scanner.Token;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;


public class Test {

	/**
	 * �̹� ���������� bufferedReader�� InputStream(System.in)�� ���� Scanner ��ü�� ����
	 * console�� �Է��� �޾� source�� tokenize�Ѵ�. �� �� list�� �̿��Ͽ�
	 * parseProgram�� �����Ͽ� parsing tree�� �����.
	 * �̹� ������ node�� type�� �����Ѵ�.
	 * ������� tree�� �̿��Ͽ� printer class�� ������ printList �޼��带 ���Ͽ�
	 * List ���� ��� node�� ���� ��ȸ�Ͽ� ����Ѵ�.
	 * @param args
	 */
	public static void main(String[] args){ 
		//read file�� 

		try
		{
			//System.in�� ���� console���� �Է��� �޾� readLine�� ����
			//tokenize�Ѵ�.
			BufferedReader buf = new BufferedReader(new InputStreamReader(System.in));
			String source="";

			List<Token> tokens = null;
			System.out.println("exit keyword : bye");
			System.out.print("> ");

			while(!(source = buf.readLine()).equals("bye"))
			{

				Scanner s = new Scanner(source); 
				tokens = s.tokenize(); 

				//Token List�� BasicParser�� �Ѱ� Token����
				//parseList�� ���Ͽ� parsing tree�� �����.
				BasicParser p = new BasicParser(tokens); 
				Node node = p.parseProgram(); 

				//printer Ŭ������ system.out ��Ʈ���� �Ѱ��ش�.
				Printer pt = new Printer(System.out); 

				//printer Ŭ������ printList�� ���Ͽ� ó�� list node�� value��
				//�����Ͽ� ��� ����Ʈ ������ pre order�� ��ȸ�Ͽ� ����Ѵ�.
				//pt.printList(node);

				CuteInterpreter i = new CuteInterpreter(); 
				for(Node n = node; n != null; n = n.getNext()){ 
					//scanner�� ���� ���� list�� ��ȸ�Ͽ� runExpr�� �Ͽ� function�� ������ list�� ����� ����Ѵ�.
					Node ir = i.runExpr(n); 
					System.out.print("�� ");
					pt.printList(ir);
					System.out.println();
				}
				System.out.print("> ");
			}
			System.out.print("�� bye");
			buf.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

} 