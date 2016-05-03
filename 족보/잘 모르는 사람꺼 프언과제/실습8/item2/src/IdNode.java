/*
 * 과제번호 : PL08-2
 * 분반 : 02분반
 * 조 번호 : 15
 * 조원 소속 : 충남대학교 공과대학 컴퓨터공학과
 * 학번 및 성명 : 201002384(김범철) 201002436(성동욱)
 */

package cute14ProjectItem2;

public class IdNode extends Node {
	public final String value;

	public IdNode(Type type, String value) {
		super(type);
		this.value = value;
	}

	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (!(obj instanceof IdNode))
			return false;
		
		IdNode i = (IdNode)obj;
		
		return (this.value.equals(i.value) && this.type == i.type);
	}

	@Override
	public String toString() {
		return value;
	}
}