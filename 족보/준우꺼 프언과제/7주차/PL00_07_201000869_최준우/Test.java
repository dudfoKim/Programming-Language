/*
 * 과제 번호 : hw07
 * 분반 : 00분반
 * 소속 : 컴퓨터공학과
 * 학번 : 201000869
 * 이름 : 최준우
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
	 * as06.txt 파일을 filereader와 bufferedReader를 통하여
	 * as06.txt의 text를 source에 저장하여 Scanner 객체를 통해
	 * source를 tokenize한다. 그 뒤 list를 이용하여
	 * parseProgram를 실행하여 parsing tree를 만든다.
	 * 이번 과제는 node의 type을 설정한다.
	 * 만들어진 tree를 이용하여 printer class에 정의한 printList 메서드를 통하여
	 * List 안의 모든 node를 전위 순회하여 출력한다.
	 * @param args
	 */
	public static void main(String[] args){ 
		//read file… 

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

			//Token List를 BasicParser로 넘겨 Token들을
			//parseList를 통하여 parsing tree를 만든다.
			BasicParser p = new BasicParser(tokens); 
			Node node = p.parseProgram(); 

			//printer 클래스에 system.out 스트림을 넘겨준다.
			Printer pt = new Printer(System.out); 

			//printer 클래스의 printList를 통하여 처음 list node의 value로
			//시작하여 모든 리스트 노드들을 pre order로 순회하여 출력한다.
			//pt.printList(node);

			CuteInterpreter i = new CuteInterpreter(); 
			for(Node n = node; n != null; n = n.getNext()){ 
				//scanner를 통해 읽은 list을 순회하여 runExpr을 하여 function을 수행한 list를 만들어 출력한다.
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

		//… 

	}

} 