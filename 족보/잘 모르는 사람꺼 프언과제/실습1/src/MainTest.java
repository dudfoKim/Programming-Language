package linkedlist;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

//	프로그래밍언어 02반 
//	과제번호 : 01
//  소속 : 컴퓨터공학과
//	학번 : 201002384
//	이름 : 김범철    
public class MainTest {
	public static void main(String[] args) {

		RecusionLinkedList list = new RecusionLinkedList();
		try {
			Scanner s = new Scanner(new File("./hw01.txt")); // 파일의 값들을 받아온후
			addAll(list, s); // add하여 처음 파일에 있던 값들로 리스트를 만든다.
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		System.out.println(list); // 처음에 file에 있는 list이 연결된것을 출력.
		list.add(11); // 11~15까지 list에 add함.
		list.add(12);
		list.add(13);
		list.add(14);
		list.add(15);
		System.out.println(list); // add된것이 확인 되었는지 출력.
		list.add(0, 16); // list의 0번째에 16을 추가.
		System.out.println(list); // 그후 list 출력.
		System.out.println(list.get(0)); // list의 0번째 자리의 숫자를 얻어와 출력.
		System.out.println(list.remove(0)); // list의 0번째 자리의 숫자를 list에서 제거.
		System.out.println(list); // list 출력.
		list.reverse(); // list를 역순으로 뒤집음.
		System.out.println(list); // 역순으로 된 list출력.
	}

	public static void addAll(RecusionLinkedList list, Scanner s) {
		if (!s.hasNextInt())
			return;
		int data = s.nextInt();
		list.add(data);
		addAll(list, s);
	}

}
