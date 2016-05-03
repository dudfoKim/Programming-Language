package linked;

public class RecusionLinkedList {
	private Node head;

	/**
	 * ���Ӱ� ������ ��带 ����Ʈ�� ó������ ����
	 */
	private void linkFirst(int element) {
		Node h = head;
		head = new Node(element, h);
	}

	/**
	 * ���� (1) �־��� Node x�� ���������� ����� Node�� �������� ���Ӱ� ������ ��带 ����
	 * @param element         ������
	 * @param x        ���
	 */
	private void linkLast(int element, Node x) {
		Node n = new Node(element, null);//���ο� ��� n�� ����
		if (x.next == null)// ���� ����� next�� null�̶��, �� ����� �� ���̶��
		{
			n.item = element;//���ο� ���� ��忡 �Է¹��� element���� �ְ�
			x.next = n;//�� ��带 x�� next�� �������ν� x�� n��带 ����Ű�� �ִ� ��ó�� �ϰ� ���ش�.
		}
		// ���� ���ҷ� ����
		else
			linkLast(element, x.next);//����� ���� �ƴϸ� ���� ��带 �湮

	}

	/**
	 * ���� Node�� ���� Node�� ���Ӱ� ������ ��带 ����
	 * @param element   ����
	 * @param pred   �������
	 */
	private void linkNext(int element, Node pred) {
		Node next = pred.next;
		pred.next = new Node(element, next);
	}

	/**
	 * ����Ʈ�� ù��° ���� ����(����)
	 * 
	 * @return ù��° ������ ������
	 */
	private int unlinkFirst() {
		Node x = head;
		int element = x.item;
		head = head.next;
		x.item = Integer.MIN_VALUE;
		x.next = null;
		return element;
	}

	/**
	 * ����Node�� ���� Node���� ����(����)
	 * 
	 * @param pred
	 *            �������
	 * @return ��������� ������
	 */
	private int unlinkNext(Node pred) {
		Node x = pred.next;
		Node next = x.next;
		int element = x.item;
		x.item = Integer.MIN_VALUE;
		x.next = null;
		pred.next = next;
		return element;
	}

	/**
	 * ���� (2) x��忡�� index��ŭ ������ Node ��ȯ
	 */
	private Node node(int index, Node x) {
		if (index == 0)//�ε����� 0�̸�  ��� x�� ���� ��ȯ�Ѵ�.
			return x;
		else
			return node(index - 1, x.next);//�ε����� 0�� �ƴϸ� index�� ���� -1�ϰ� ���� ���� ��带 ����Ű�� �Ͽ� 
										//���������� index�� ����ŭ ��尡 ���� ���� �Ѿ�� �Ѵ�.
	}

	/**
	 * ���� (3) ��� �������� ���� ��ȯ
	 */
	private int length(Node x) {
		if (x.next == null)//������ ����̸� 1�� ��ȯ
			return 1;
		else {
			return length(x.next) + 1;//��������尡 �ƴϸ� +1�� �ϰ� ���� ��带 �־ length�� �θ�
		}

	}

	/**
	 * ���� (4) ��� �������� ���� ��ȯ
	 */
	private String toString(Node x) {
		if (x.next == null)//������ ����̸� item���� ��ȯ
			return " " + x.item;
		else
			return " " + x.item + toString(x.next);//x.item�� �տ� ��ȯ�ϰ� �ٽ� ��������� ������ ��ȯ�ϴ� �Լ��� ȣ�������ν�
												//��������尡 �ƴϸ� ���� ��� ��ȯ�Ѵ�.

	}

	/**
	 * ����Ʈ�� �Ųٷ� ����
	 * 
	 * @param x
	 *            ���� ���
	 * @param pred
	 *            �������� ���� ���
	 */
	private void reverse(Node x, Node pred) {
		if (x.next == null) {//x�� ������ ����� 
			head = x;//head�� x�� �־��ְ�
			x.next = pred;//x�� ������带 ����Ű�� �Ѵ�.
		} else {
			reverse(x.next, x);//��������尡 �ƴ϶�� 
			x.next = pred;//x�� ������带 ����Ű�� �Ѵ�.
		}

	}

	/**
	 * ���Ҹ� ����Ʈ�� �������� �߰�
	 */
	public boolean add(int element) {
		if (head == null) {
			linkFirst(element);
		} else {
			linkLast(element, head);
		}
		return true;
	}

	/**
	 * ���Ҹ� �־��� index ��ġ�� �߰�
	 * 
	 * @param index
	 *            ����Ʈ���� �߰��� ��ġ
	 * @param element
	 *            �߰��� ������
	 */
	public void add(int index, int element) {
		if (!(index >= 0 && index <= size()))
			throw new IndexOutOfBoundsException("" + index);
		if (index == 0)
			linkFirst(element);
		else
			linkNext(element, node(index - 1, head));
	}

	/**
	 * ����Ʈ���� index ��ġ�� ���� ��ȯ
	 */
	public int get(int index) {
		if (!(index >= 0 && index < size()))
			throw new IndexOutOfBoundsException("" + index);
		return node(index, head).item;
	}

	/**
	 * ����Ʈ���� index ��ġ�� ���� ����
	 */
	public int remove(int index) {
		if (!(index >= 0 && index < size()))
			throw new IndexOutOfBoundsException("" + index);
		if (index == 0) {
			return unlinkFirst();
		}
		return unlinkNext(node(index - 1, head));
	}

	/**
	 * ����Ʈ�� �Ųٷ� ����
	 **/
	public void reverse() {
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
		return "[ " + toString(head) + "]";
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