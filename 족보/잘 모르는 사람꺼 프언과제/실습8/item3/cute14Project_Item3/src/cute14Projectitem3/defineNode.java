/*
 * 과제번호 : PL08-2
 * 분반 : 02분반
 * 조 번호 : 15
 * 조원 소속 : 충남대학교 공과대학 컴퓨터공학과
 * 학번 및 성명 : 201002384(김범철) 201002436(성동욱)
 */

//define node에 상세한 정의가 있는 클래스
//
package cute14Projectitem3;

public class defineNode{
	
	private String tempString;
	private Node tempNode;
	
	public defineNode(String tempString, Node tempNode)
	{
		this.tempString=tempString;
		this.tempNode=tempNode;
		
	}

	public void setTempString(String tempString) {
		this.tempString = tempString;
	}

	public void setTempNode(Node tempNode) {
		this.tempNode = tempNode;
	}

	public String getTempString() {
		return tempString;
	}

	public Node getTempNode() {
		return tempNode;
	}
	
}
