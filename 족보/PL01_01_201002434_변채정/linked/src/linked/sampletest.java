/* 
 * ���� ��ȣ : 01
 * �й� ǥ�� : 01��
 * �Ҽ�, �й�, ���� : ��ǻ�Ͱ��а�, 201002434, ��ä��
*/

package linked;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class sampletest {
	public static void addAll(RecusionLinkedList list, Scanner s) {
		if (!s.hasNextInt())
			return;
		int data = s.nextInt();
		list.add(data);
		addAll(list, s);
	}

	public static void main(String[] args) {
		RecusionLinkedList list = new RecusionLinkedList();
		try {
			Scanner s = new Scanner(new File("./hw01.txt"));
			addAll(list, s);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		System.out.println(list);
		list.reverse();
		System.out.println(list);
	}
}