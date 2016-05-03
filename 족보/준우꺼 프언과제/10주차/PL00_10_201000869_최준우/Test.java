/*
 * 과제 번호 : hw09
 * 분반 : 00분반
 * 소속 : 컴퓨터공학과
 * 학번 : 201000869
 * 이름 : 최준우
 */

package hw10_Reconizing_Token_u00;

import hw10_Reconizing_Token_u00.Scanner.Token;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;


public class Test {

	/**
	 * 이번 과제에서는 bufferedReader와 InputStream(System.in)을 통해 Scanner 객체를 통해
	 * console로 입력을 받아 source를 tokenize한다. 그 뒤 list를 이용하여
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
			//System.in을 통해 console에서 입력을 받아 readLine을 통해
			//tokenize한다.
			BufferedReader buf = new BufferedReader(new InputStreamReader(System.in));

			//printer 클래스에 system.out 스트림을 넘겨준다.
			Printer pt = new Printer(System.out); 
			List<Token> tokens = null;
			CuteInterpreter i = new CuteInterpreter();
			String source="";

			System.out.println("exit keyword : bye");
			System.out.print("> ");

			while(!(source = buf.readLine()).equals("bye"))
			{

				Scanner s = new Scanner(source); 
				tokens = s.tokenize(); 

				//Token List를 BasicParser로 넘겨 Token들을
				//parseList를 통하여 parsing tree를 만든다.
				BasicParser p = new BasicParser(tokens); 
				Node node = p.parseProgram(); 

		

				//printer 클래스의 printList를 통하여 처음 list node의 value로
				//시작하여 모든 리스트 노드들을 preorder로 순회하여 출력한다.
				
				for(Node n = node; n != null; n = n.getNext()){ 
					//scanner를 통해 읽은 list을 순회하여 runExpr을 하여 function을 수행한 list를 만들어 출력한다.
					Node ir = i.runExpr(n); 
					System.out.print("… ");
					pt.printList(ir);
					System.out.println();
				}
				System.out.print("> ");
			}
			System.out.print("… bye");
			buf.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

} 