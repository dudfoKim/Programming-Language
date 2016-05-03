/*
 * ���� ��ȣ : hw04 
 * �й� : 00�й�
 * �Ҽ� : ��ǻ�Ͱ��а�
 * �й� : 201000869
 * �̸� : ���ؿ�
 */

package hw04_Reconizing_Token_u00;

import hw04_Reconizing_Token_u00.Scanner.Token;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;


public class Test {

	/**
	 * as04.txt ������ filereader�� bufferedReader�� ���Ͽ�
	 * as04.txt�� text�� source�� �����Ͽ� Scanner ��ü�� ����
	 * source�� tokenize�Ѵ�. �� �� list�� �̿��Ͽ�
	 * parseList�� �����Ͽ� parsing tree�� �����.
	 * ������� tree�� �̿��Ͽ� printer class�� ������ printList �޼��带 ���Ͽ�
	 * List ���� ��� node�� ���� ��ȸ�Ͽ� ����Ѵ�.
	 * @param args
	 */
	public static void main(String[] args){ 
		//read file�� 

		try
		{
			File file = new File("./as04.txt");
			BufferedReader buf = new BufferedReader(new FileReader(file));
			String source="";
			String temp="";
			List<Token> tokens = null;
			while((temp = buf.readLine())!=null)
			{
				source+=temp;
			}
			
			Scanner s = new Scanner(source); 
			tokens = s.tokenize(); 
			
			//Token List�� BasicParser�� �Ѱ� Token����
			//parseList�� ���Ͽ� parsing tree�� �����.
			BasicParser p = new BasicParser(tokens); 
			Node node = p.parseList(); 

			//printer Ŭ������ system.out ��Ʈ���� �Ѱ��ش�.
			Printer pt = new Printer(System.out); 
			
			//printer Ŭ������ printList�� ���Ͽ� ó�� list node�� value��
			//�����Ͽ� ��� ����Ʈ ������ pre order�� ��ȸ�Ͽ� ����Ѵ�.
			pt.printList(((ListNode)node).value);
			
			buf.close();
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

		//�� 

	}

} 