/*
 * 과제 번호 : hw01 
 * 분반 : 00분반
 * 소속 : 컴퓨터공학과
 * 학번 : 201000869
 * 이름 : 최준우
 *
 */

package hw01_MakeLL_u00;

public class RecusionLinkedList{
	private Node head;
	/**
	 * 새롭게 생성된 노드를 리스트의 처음으로 연결
	 */
	private void linkFirst(int element){
		Node h = head;
		head = new Node(element, h);
	}
	/**
	 * 과제 (1)
	 * 주어진 Node x의 마지막으로 연결된 Node의 다음으로 새롭게 생성된 노드를 연결
	 * @param element 데이터
	 * @param x 노드
	 */
	/*
	 * Node x의 next를 검사하여 null인지 확인한다.
	 * List의 특성에 의해 next가 null이면 List의 End point이므로
	 * next가 null일때 까지  linkLast를 재귀 호출하여 다음 노드를 방문하며
	 * list의 last를 찾은 뒤, 새로운 노드를 끝에 추가한다.
	 */
	private void linkLast(int element, Node x){
		//다음 노드 방문 recusion
		if(x.next == null)
			x.next = new Node(element, null);
		else
			linkLast(element, x.next);
		
	}
	/**
	 * 이전 Node의 다음 Node로 새롭게 생성된 노드를 연결
	 * @param element 원소
	 * @param pred 이전노드
	 */
	private void linkNext(int element, Node pred){
		Node next = pred.next;
		pred.next = new Node(element, next);
	}
	/**
	 * 리스트의 첫번째 원소 해제(삭제)
	 * @return 첫번째 원소의 데이터
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
	 * 이전Node의 다음 Node연결 해제(삭제)
	 * @param pred 이전노드
	 * @return 다음노드의 데이터
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
	 * 과제 (2)
	 * x노드에서 index만큼 떨어진 Node 반환
	 */
	/*
	 * 해당 index를 찾아갈 때, node를 call할 때마다 index를 1 줄여주며
	 * index가 0이 되면 해당 node를 반환한다.
	 */
	private Node node(int index, Node x){
		//채워서 사용, index를 줄여가면서 다음 노드 방문
		if(index==0)
			return x;
		else
			return node(index-1, x.next);
	}
	/**
	 * 과제 (3)
	 * 노드 끝까지의 갯수 반환
	 */
	/*
	 * 재귀 호출을 하면 call stack이 쌓이는 것을 이용하여
	 * call이 return 되는 list 끝에서 1을 반환하고
	 * 쌓인 스택마다 +1을 하여 list의 length를 알 수 있다.
	 */
	private int length(Node x){
		//채워서 사용, recusion 사용
		if(x.next == null)
			return 1;
		else
			return length(x.next) + 1;
	}
	/**
	 * 과제 (4)
	 * 노드 끝까지의 내용 반환
	 */
	/*
	 * Node가 last가 아닐 때, 자신의 item과 다음 toSiring을
	 * 재귀 호출하여 스택의 최상단에서 last에 있는 item을 반환하며
	 * 반환 될 때 마다 String에 해당 node에 item이 쌓여
	 * 모든 노드에 있는 item을 반환한다.
	 */
	private String toString(Node x){
		//채워서 사용, recusion 사용
		if(x.next==null)
			return x.item+"";
		return x.item +" "+ toString(x.next);
	}
	/**
	 * 리스트를 거꾸로 만듬
	 * @param x 현재 노드
	 * @param pred 현재노드의 이전 노드
	 */
	/*
	 * list의 끝까지 스택을 쌓고, list의 끝을 만나면 last node를
	 * head에 대입한 뒤, 스택을 내려오면서 node x의 next를 pred node로
	 * 연결하여 준다. 
	 */
	private void reverse(Node x, Node pred) {
		//채워서 사용, recuison 사용
		if(x.next != null)
			reverse(x.next, x);
		else
			this.head = x;
		x.next = pred;
	}
	/**
	 * 원소를 리스트의 마지막에 추가
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
	 * 원소를 주어진 index 위치에 추가
	 * @param index 리스트에서 추가될 위치
	 * @param element 추가될 데이터
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
	 * 리스트에서 index 위치의 원소 반환
	 */
	public int get(int index) {
		if (! ( index >= 0 && index < size()) )
			throw new IndexOutOfBoundsException("" + index);
		return node(index, head).item;
	}
	/**
	 * 리스트에서 index 위치의 원소 삭제
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
	 * 리스트를 거꾸로 만듬
	 */
	public void reverse(){
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
		return "[" + toString(head) + "]";
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