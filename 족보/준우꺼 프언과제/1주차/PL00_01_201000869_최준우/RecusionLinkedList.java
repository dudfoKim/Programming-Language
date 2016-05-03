/*
 * ���� ��ȣ : hw01 
 * �й� : 00�й�
 * �Ҽ� : ��ǻ�Ͱ��а�
 * �й� : 201000869
 * �̸� : ���ؿ�
 *
 */

package hw01_MakeLL_u00;

public class RecusionLinkedList{
	private Node head;
	/**
	 * ���Ӱ� ������ ��带 ����Ʈ�� ó������ ����
	 */
	private void linkFirst(int element){
		Node h = head;
		head = new Node(element, h);
	}
	/**
	 * ���� (1)
	 * �־��� Node x�� ���������� ����� Node�� �������� ���Ӱ� ������ ��带 ����
	 * @param element ������
	 * @param x ���
	 */
	/*
	 * Node x�� next�� �˻��Ͽ� null���� Ȯ���Ѵ�.
	 * List�� Ư���� ���� next�� null�̸� List�� End point�̹Ƿ�
	 * next�� null�϶� ����  linkLast�� ��� ȣ���Ͽ� ���� ��带 �湮�ϸ�
	 * list�� last�� ã�� ��, ���ο� ��带 ���� �߰��Ѵ�.
	 */
	private void linkLast(int element, Node x){
		//���� ��� �湮 recusion
		if(x.next == null)
			x.next = new Node(element, null);
		else
			linkLast(element, x.next);
		
	}
	/**
	 * ���� Node�� ���� Node�� ���Ӱ� ������ ��带 ����
	 * @param element ����
	 * @param pred �������
	 */
	private void linkNext(int element, Node pred){
		Node next = pred.next;
		pred.next = new Node(element, next);
	}
	/**
	 * ����Ʈ�� ù��° ���� ����(����)
	 * @return ù��° ������ ������
	 */
	private int unlinkFirst(){
		Node x = head;
		int element = x.item;
		head = head.next;
		x.item = Integer.MIN_VALUE;
		x.next = null;
		return element;
	}
	/**
	 * ����Node�� ���� Node���� ����(����)
	 * @param pred �������
	 * @return ��������� ������
	 */
	private int unlinkNext(Node pred){
		Node x = pred.next;
		Node next = x.next;
		int element = x.item;
		x.item = Integer.MIN_VALUE;
		x.next = null;
		pred.next = next;
		return element;
	}
	/**
	 * ���� (2)
	 * x��忡�� index��ŭ ������ Node ��ȯ
	 */
	/*
	 * �ش� index�� ã�ư� ��, node�� call�� ������ index�� 1 �ٿ��ָ�
	 * index�� 0�� �Ǹ� �ش� node�� ��ȯ�Ѵ�.
	 */
	private Node node(int index, Node x){
		//ä���� ���, index�� �ٿ����鼭 ���� ��� �湮
		if(index==0)
			return x;
		else
			return node(index-1, x.next);
	}
	/**
	 * ���� (3)
	 * ��� �������� ���� ��ȯ
	 */
	/*
	 * ��� ȣ���� �ϸ� call stack�� ���̴� ���� �̿��Ͽ�
	 * call�� return �Ǵ� list ������ 1�� ��ȯ�ϰ�
	 * ���� ���ø��� +1�� �Ͽ� list�� length�� �� �� �ִ�.
	 */
	private int length(Node x){
		//ä���� ���, recusion ���
		if(x.next == null)
			return 1;
		else
			return length(x.next) + 1;
	}
	/**
	 * ���� (4)
	 * ��� �������� ���� ��ȯ
	 */
	/*
	 * Node�� last�� �ƴ� ��, �ڽ��� item�� ���� toSiring��
	 * ��� ȣ���Ͽ� ������ �ֻ�ܿ��� last�� �ִ� item�� ��ȯ�ϸ�
	 * ��ȯ �� �� ���� String�� �ش� node�� item�� �׿�
	 * ��� ��忡 �ִ� item�� ��ȯ�Ѵ�.
	 */
	private String toString(Node x){
		//ä���� ���, recusion ���
		if(x.next==null)
			return x.item+"";
		return x.item +" "+ toString(x.next);
	}
	/**
	 * ����Ʈ�� �Ųٷ� ����
	 * @param x ���� ���
	 * @param pred �������� ���� ���
	 */
	/*
	 * list�� ������ ������ �װ�, list�� ���� ������ last node��
	 * head�� ������ ��, ������ �������鼭 node x�� next�� pred node��
	 * �����Ͽ� �ش�. 
	 */
	private void reverse(Node x, Node pred) {
		//ä���� ���, recuison ���
		if(x.next != null)
			reverse(x.next, x);
		else
			this.head = x;
		x.next = pred;
	}
	/**
	 * ���Ҹ� ����Ʈ�� �������� �߰�
	 */
	public boolean add(int element) {
		if(head == null){
			linkFirst(element);
		}else{
			linkLast(element, head);
		}
		return true;
	}
	/**
	 * ���Ҹ� �־��� index ��ġ�� �߰�
	 * @param index ����Ʈ���� �߰��� ��ġ
	 * @param element �߰��� ������
	 */
	public void add(int index, int element) {
		if (! (index >= 0 && index <= size()) )
			throw new IndexOutOfBoundsException("" + index);
		if(index == 0)
			linkFirst(element);
		else
			linkNext( element, node(index-1, head) );
	}
	/**
	 * ����Ʈ���� index ��ġ�� ���� ��ȯ
	 */
	public int get(int index) {
		if (! ( index >= 0 && index < size()) )
			throw new IndexOutOfBoundsException("" + index);
		return node(index, head).item;
	}
	/**
	 * ����Ʈ���� index ��ġ�� ���� ����
	 */
	public int remove(int index) {
		if (! ( index >= 0 && index < size()) )
			throw new IndexOutOfBoundsException("" + index);
		if(index == 0){
			return unlinkFirst();
		}
		return unlinkNext( node(index - 1, head) );
	}
	/**
	 * ����Ʈ�� �Ųٷ� ����
	 */
	public void reverse(){
		reverse(head, null);
	}
	/**
	 * ����Ʈ�� ���� ���� ��ȯ
	 */
	public int size() {
		return length(head);
	}
	@Override
	public String toString() {
		if (head == null)
			return "[]";
		return "[" + toString(head) + "]";
	}
	/**
	 * ����Ʈ�� ���� �ڷᱸ��
	 */
	private static class Node {
		int item;
		Node next;
		Node(int element, Node next) {
			this.item = element;
			this.next = next;
		}
	}
}