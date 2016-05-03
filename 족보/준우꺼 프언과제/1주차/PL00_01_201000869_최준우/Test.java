/*
 * 과제 번호 : hw01 
 * 분반 : 00분반
 * 소속 : 컴퓨터공학과
 * 학번 : 201000869
 * 이름 : 최준우
 *
 */

package hw01_MakeLL_u00;

import java.io.*;
import java.util.Scanner;

public class Test {

	/*
	 * scanner에서 파일을 불러와 파일 안에 data를 
	 * linked list로 만든다.
	 */
	public static void addAll(RecusionLinkedList list, Scanner s){
		if( !s.hasNextInt() )
			return;
		int data = s.nextInt();
		list.add(data);
		addAll(list, s);
	}
	
	public static void main(String[] args){
		RecusionLinkedList list = new RecusionLinkedList();
		try {
			Scanner s = new Scanner(new File("./hw01.txt"));
			addAll(list, s);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		/*
		 * hw01.txt 안에 있는 data를 linked list로 만든다.
		 */
		System.out.println("hw01_List : ");
		System.out.println(list);
		System.out.println("Size : " + list.size());
		
		/*
		 * list의 끝에 element가 25인 node를 추가한다.
		 */
		System.out.println("\nhw01_List : ");
		list.add(25);
		System.out.println(list);
		System.out.println("Size : " + list.size());
		
		/*
		 * index가 3인 자리에 element가 18인 node를 추가한다.
		 */
		System.out.println("\nhw01_List : ");
		list.add(3, 18);
		System.out.println(list);
		System.out.println("Size : " + list.size());
		
		/*
		 * index가 3인 자리에 element를 출력한다.
		 */
		System.out.println("\nindex 3 : " + list.get(3));
		
		/*
		 * index가 3인 자리의 node를 삭제한다.
		 */
		System.out.println("\nhw01_List : ");
		list.remove(3);
		System.out.println(list);
		System.out.println("Size : " + list.size());
		
		/*
		 * list를 반전한 뒤 출력하여 준다.
		 */
		System.out.println("\nReverse_List : ");
		list.reverse();
		System.out.println(list);
		System.out.println("Size : " + list.size());
	}
}