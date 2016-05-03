/*
 * ���� ��ȣ : hw01 
 * �й� : 00�й�
 * �Ҽ� : ��ǻ�Ͱ��а�
 * �й� : 201000869
 * �̸� : ���ؿ�
 *
 */

package hw01_MakeLL_u00;

import java.io.*;
import java.util.Scanner;

public class Test {

	/*
	 * scanner���� ������ �ҷ��� ���� �ȿ� data�� 
	 * linked list�� �����.
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
		 * hw01.txt �ȿ� �ִ� data�� linked list�� �����.
		 */
		System.out.println("hw01_List : ");
		System.out.println(list);
		System.out.println("Size : " + list.size());
		
		/*
		 * list�� ���� element�� 25�� node�� �߰��Ѵ�.
		 */
		System.out.println("\nhw01_List : ");
		list.add(25);
		System.out.println(list);
		System.out.println("Size : " + list.size());
		
		/*
		 * index�� 3�� �ڸ��� element�� 18�� node�� �߰��Ѵ�.
		 */
		System.out.println("\nhw01_List : ");
		list.add(3, 18);
		System.out.println(list);
		System.out.println("Size : " + list.size());
		
		/*
		 * index�� 3�� �ڸ��� element�� ����Ѵ�.
		 */
		System.out.println("\nindex 3 : " + list.get(3));
		
		/*
		 * index�� 3�� �ڸ��� node�� �����Ѵ�.
		 */
		System.out.println("\nhw01_List : ");
		list.remove(3);
		System.out.println(list);
		System.out.println("Size : " + list.size());
		
		/*
		 * list�� ������ �� ����Ͽ� �ش�.
		 */
		System.out.println("\nReverse_List : ");
		list.reverse();
		System.out.println(list);
		System.out.println("Size : " + list.size());
	}
}