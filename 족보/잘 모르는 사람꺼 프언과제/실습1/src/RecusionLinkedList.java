package linkedlist;

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
	 * 
	 * @param element
	 *            데이터
	 * @param x
	 *            노드
	 */
	private void linkLast(int element, Node x) {
		if (x.next == null) { 
			linkNext(element,x); // 마지막 노드 뒤에 연결.
		} else
			linkLast(element, x.next); // 마지막이 아닐경우 다음노드로 이동하며 마지막노드를 찾음
	}

	/**
	 * 이전 Node의 다음 Node로 새롭게 생성된 노드를 연결
	 * 
	 * @param element
	 *            원소
	 * @param pred
	 *            이전노드
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
		if (index == 0) { // index가 0이될경우 찾는위치이므로 그자리의 노드 반환.
			return x;
		}

		else {
			index--; // index만큼 떨어진 node를 찾는것이므로 index를 하나씩 감소시키며 찾아감.
			return node(index, x.next); // 리스트를 넘어가며 검사해야하기 때문에 x.next를 넘겨줌.
		}
		// 채워서 사용, index를 줄여가면서 다음 노드 방문
	}

	/**
	 * 과제 (3) 노드 끝까지의 갯수 반환
	 */
	private int length(Node x) {

		// 끝이 아닐경우 계속+1을 해주며 끝노드인경우 0을 리턴하면 recusion하게 돌아가기 때문에 0부터 node의 갯수만큼
		// +가 됨.
		if (x == null) {
			return 0;
		}

		else {
			return length(x.next) + 1;
		}
		// 채워서 사용, recusion 사용
	}

	/**
	 * 과제 (4) 노드 끝까지의 내용 반환
	 */
	private String toString(Node x) {
		if (x.next == null) { // 갯수 셀때와 같이 연결된 노드 하나씩 찾아가며 item을 리턴.
			return x.item + " ";
		} else {
			return x.item + " " + toString(x.next);

		}
		// 채워서 사용, recusion 사용
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
		// unlinknext 메서드는 메서드에 입력한 노드의 다음노드를 리스트에서 제거한후 제거된노드의 다음노드와 입력한노드를
		// 이어주는 메서드이다.
		// 우선 처음에 리스트의 끝까지 찾아간다. 그후 리스트의 마지막 노드를 만날경우 마지막 노드의 이전노드를
		// unlinknext메서드에 넣을경우
		// 마지막노드가 연결이 끊어졌다가 다시 add하기 때문에 연결된다.
		// 그후 recursion하게 돌기 때문에 끝에서부터 처음으로 옮겨가면서 계속 뒤쪽으로 리스트가 다시 연결되게 된다.
		// x가 처음노드가 되면 pred가 null이 되기때문에 unlinknext에 null이 들어가 처음노드가 마지막으로 재배치가
		// 안되게 된다.
		// 그러므로 unlinkfirst메서드를 사용해 첫노드를 제거한후 다시 add해준다.
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
		// 채워서 사용, recuison 사용
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
	 */
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
