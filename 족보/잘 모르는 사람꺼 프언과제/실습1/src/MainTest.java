package linkedlist;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

//	���α׷��־�� 02�� 
//	������ȣ : 01
//  �Ҽ� : ��ǻ�Ͱ��а�
//	�й� : 201002384
//	�̸� : ���ö    
public class MainTest {
	public static void main(String[] args) {

		RecusionLinkedList list = new RecusionLinkedList();
		try {
			Scanner s = new Scanner(new File("./hw01.txt")); // ������ ������ �޾ƿ���
			addAll(list, s); // add�Ͽ� ó�� ���Ͽ� �ִ� ����� ����Ʈ�� �����.
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		System.out.println(list); // ó���� file�� �ִ� list�� ����Ȱ��� ���.
		list.add(11); // 11~15���� list�� add��.
		list.add(12);
		list.add(13);
		list.add(14);
		list.add(15);
		System.out.println(list); // add�Ȱ��� Ȯ�� �Ǿ����� ���.
		list.add(0, 16); // list�� 0��°�� 16�� �߰�.
		System.out.println(list); // ���� list ���.
		System.out.println(list.get(0)); // list�� 0��° �ڸ��� ���ڸ� ���� ���.
		System.out.println(list.remove(0)); // list�� 0��° �ڸ��� ���ڸ� list���� ����.
		System.out.println(list); // list ���.
		list.reverse(); // list�� �������� ������.
		System.out.println(list); // �������� �� list���.
	}

	public static void addAll(RecusionLinkedList list, Scanner s) {
		if (!s.hasNextInt())
			return;
		int data = s.nextInt();
		list.add(data);
		addAll(list, s);
	}

}
