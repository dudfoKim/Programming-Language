/*
 * ���� ��ȣ : hw07
 * �й� : 00�й�
 * �Ҽ� : ��ǻ�Ͱ��а�
 * �й� : 201000869
 * �̸� : ���ؿ�
 */

package hw07_Reconizing_Token_u00;

import hw07_Reconizing_Token_u00.Scanner.Token;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;


public class Test {

	/**
	 * as06.txt ������ filereader�� bufferedReader�� ���Ͽ�
	 * as06.txt�� text�� source�� �����Ͽ� Scanner ��ü�� ����
	 * source�� tokenize�Ѵ�. �� �� list�� �̿��Ͽ�
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
			File file = new File("./as07.txt");
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
				pt.printList(ir);
				System.out.println();
			}

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