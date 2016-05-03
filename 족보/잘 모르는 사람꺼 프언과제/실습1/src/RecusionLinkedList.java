package linkedlist;

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
	 * 
	 * @param element
	 *            ������
	 * @param x
	 *            ���
	 */
	private void linkLast(int element, Node x) {
		if (x.next == null) { 
			linkNext(element,x); // ������ ��� �ڿ� ����.
		} else
			linkLast(element, x.next); // �������� �ƴҰ�� �������� �̵��ϸ� ��������带 ã��
	}

	/**
	 * ���� Node�� ���� Node�� ���Ӱ� ������ ��带 ����
	 * 
	 * @param element
	 *            ����
	 * @param pred
	 *            �������
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
		if (index == 0) { // index�� 0�̵ɰ�� ã����ġ�̹Ƿ� ���ڸ��� ��� ��ȯ.
			return x;
		}

		else {
			index--; // index��ŭ ������ node�� ã�°��̹Ƿ� index�� �ϳ��� ���ҽ�Ű�� ã�ư�.
			return node(index, x.next); // ����Ʈ�� �Ѿ�� �˻��ؾ��ϱ� ������ x.next�� �Ѱ���.
		}
		// ä���� ���, index�� �ٿ����鼭 ���� ��� �湮
	}

	/**
	 * ���� (3) ��� �������� ���� ��ȯ
	 */
	private int length(Node x) {

		// ���� �ƴҰ�� ���+1�� ���ָ� ������ΰ�� 0�� �����ϸ� recusion�ϰ� ���ư��� ������ 0���� node�� ������ŭ
		// +�� ��.
		if (x == null) {
			return 0;
		}

		else {
			return length(x.next) + 1;
		}
		// ä���� ���, recusion ���
	}

	/**
	 * ���� (4) ��� �������� ���� ��ȯ
	 */
	private String toString(Node x) {
		if (x.next == null) { // ���� ������ ���� ����� ��� �ϳ��� ã�ư��� item�� ����.
			return x.item + " ";
		} else {
			return x.item + " " + toString(x.next);

		}
		// ä���� ���, recusion ���
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
		// unlinknext �޼���� �޼��忡 �Է��� ����� ������带 ����Ʈ���� �������� ���ŵȳ���� �������� �Է��ѳ�带
		// �̾��ִ� �޼����̴�.
		// �켱 ó���� ����Ʈ�� ������ ã�ư���. ���� ����Ʈ�� ������ ��带 ������� ������ ����� ������带
		// unlinknext�޼��忡 �������
		// ��������尡 ������ �������ٰ� �ٽ� add�ϱ� ������ ����ȴ�.
		// ���� recursion�ϰ� ���� ������ ���������� ó������ �Űܰ��鼭 ��� �������� ����Ʈ�� �ٽ� ����ǰ� �ȴ�.
		// x�� ó����尡 �Ǹ� pred�� null�� �Ǳ⶧���� unlinknext�� null�� �� ó����尡 ���������� ���ġ��
		// �ȵǰ� �ȴ�.
		// �׷��Ƿ� unlinkfirst�޼��带 ����� ù��带 �������� �ٽ� add���ش�.
		if (x.next == null) {
			add(unlinkNext(pred));
		}

		else {
			reverse(x.next, x);

			if (pred == null) {
				add(unlinkFirst());
			}

			else {
				add(unlinkNext(pred));
			}

		}
		// ä���� ���, recuison ���
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
	 */
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
