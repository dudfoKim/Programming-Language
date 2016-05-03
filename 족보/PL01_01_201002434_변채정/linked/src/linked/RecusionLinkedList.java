package linked;

public class RecusionLinkedList {
	private Node head;

	/**
	 * 새롭게 생성된 노드를 리스트의 처음으로 연결
	 */
	private void linkFirst(int element) {
		Node h = head;
		head = new Node(element, h);
	}

	/**
	 * 과제 (1) 주어진 Node x의 마지막으로 연결된 Node의 다음으로 새롭게 생성된 노드를 연결
	 * @param element         데이터
	 * @param x        노드
	 */
	private void linkLast(int element, Node x) {
		Node n = new Node(element, null);//새로운 노드 n을 생성
		if (x.next == null)// 받은 노드의 next가 null이라면, 즉 노드의 맨 끝이라면
		{
			n.item = element;//새로운 만든 노드에 입력받은 element값을 넣고
			x.next = n;//그 노드를 x의 next에 넣음으로써 x가 n노드를 가르키고 있는 것처럼 하게 해준다.
		}
		// 다음 원소로 연결
		else
			linkLast(element, x.next);//노드의 끝이 아니면 다음 노드를 방문

	}

	/**
	 * 이전 Node의 다음 Node로 새롭게 생성된 노드를 연결
	 * @param element   원소
	 * @param pred   이전노드
	 */
	private void linkNext(int element, Node pred) {
		Node next = pred.next;
		pred.next = new Node(element, next);
	}

	/**
	 * 리스트의 첫번째 원소 해제(삭제)
	 * 
	 * @return 첫번째 원소의 데이터
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
	 * 이전Node의 다음 Node연결 해제(삭제)
	 * 
	 * @param pred
	 *            이전노드
	 * @return 다음노드의 데이터
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
	 * 과제 (2) x노드에서 index만큼 떨어진 Node 반환
	 */
	private Node node(int index, Node x) {
		if (index == 0)//인덱스가 0이면  노드 x의 값을 반환한다.
			return x;
		else
			return node(index - 1, x.next);//인덱스가 0이 아니면 index의 값을 -1하고 노드는 다음 노드를 가르키게 하여 
										//최종적으로 index의 수만큼 노드가 다음 노드로 넘어가게 한다.
	}

	/**
	 * 과제 (3) 노드 끝까지의 갯수 반환
	 */
	private int length(Node x) {
		if (x.next == null)//마지막 노드이면 1을 반환
			return 1;
		else {
			return length(x.next) + 1;//마지막노드가 아니면 +1을 하고 다음 노드를 넣어서 length를 부름
		}

	}

	/**
	 * 과제 (4) 노드 끝까지의 내용 반환
	 */
	private String toString(Node x) {
		if (x.next == null)//마지막 노드이면 item값을 반환
			return " " + x.item;
		else
			return " " + x.item + toString(x.next);//x.item을 앞에 반환하고 다시 다음노드의 내용을 반환하는 함수를 호출함으로써
												//마지막노드가 아니면 값을 계속 반환한다.

	}

	/**
	 * 리스트를 거꾸로 만듬
	 * 
	 * @param x
	 *            현재 노드
	 * @param pred
	 *            현재노드의 이전 노드
	 */
	private void reverse(Node x, Node pred) {
		if (x.next == null) {//x가 마지막 노드라면 
			head = x;//head에 x를 넣어주고
			x.next = pred;//x가 이전노드를 가르키게 한다.
		} else {
			reverse(x.next, x);//마지막노드가 아니라면 
			x.next = pred;//x가 이전노드를 가르키게 한다.
		}

	}

	/**
	 * 원소를 리스트의 마지막에 추가
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
	 * 원소를 주어진 index 위치에 추가
	 * 
	 * @param index
	 *            리스트에서 추가될 위치
	 * @param element
	 *            추가될 데이터
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
	 * 리스트에서 index 위치의 원소 반환
	 */
	public int get(int index) {
		if (!(index >= 0 && index < size()))
			throw new IndexOutOfBoundsException("" + index);
		return node(index, head).item;
	}

	/**
	 * 리스트에서 index 위치의 원소 삭제
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
	 * 리스트를 거꾸로 만듬
	 **/
	public void reverse() {
		reverse(head, null);
	}

	/**
	 * 리스트의 원소 갯수 반환
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
	 * 리스트에 사용될 자료구조
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