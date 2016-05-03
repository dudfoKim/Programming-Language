/* 
 * ���� ��ȣ : 02
 * �й� ǥ�� : 01��
 * �Ҽ�, �й�, ���� : ��ǻ�Ͱ��а�, 201002434, ��ä��
*/

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;



public class REC {

	public enum TokenType{
		ID(3), INT(2);//3�̸� id, 2�̸� int
		private final int finalState;
		TokenType(int finalState) {
			this.finalState = finalState;
		}
	}

	public static class Token {
		public final TokenType type;
		public final String lexme;//���Ͽ��� ���� ��
		Token(TokenType type, String lexme) {
			this.type = type;
			this.lexme = lexme;
		}
		@Override
		public String toString() {
			return String.format("[%s: %s]", type.toString(), lexme);
		}
	}

	private int transM[][];
	private boolean accept[];
	private String source;
	private int pos;

	public REC (String source) {
		this.accept = new boolean[]{ false, false, true, true };// Final states: 2,3
		this.transM = new int[4][128];
		this.source = source == null ? "" : source;
		init_TM();
	}

	private void init_TM() {
		int i;
		for(i='0' ; i<='9' ; i++) {
			transM[0][i] = 2;
			transM[1][i] = 2;
			transM[2][i] = 2;
			transM[3][i] = 3;
		} //���ڰ� ������ ���º����Ѵ�

		transM[0]['-'] = 1; // '-'�̸� 1��

		for(i='A';i<='Z';i++){
			transM[0][i]=3;
			transM[3][i]=3;
		}//�빮�ڰ� ������ 3����
		for(i='a';i<='z';i++){
			transM[0][i]=3;
			transM[3][i]=3;
		}//�ҹ��ڰ� ������ 3���� 

		//����� ������
		transM[2][' '] = -1; 
		transM[3][' '] = -1;
	}

	private Token nextToken(){
		char c = Character.SPACE_SEPARATOR;
		StringBuffer sb = new StringBuffer();
		Token result = null;
		int StateOld = 0, StateNew;
		boolean acceptState = false;
		
		while( pos < source.length() ){
			if(!Character.isWhitespace( c = source.charAt(pos)))
				break;
			pos++;
		}//�ǹ� ���� ���鹮�� ����
		
		if( pos >= source.length() )
			return null; //input data�� �� �̻� ���� ���
		while (!acceptState) {
			StateNew = transM[StateOld][c]; //�Է¹��ڷ� ���ο� ���� �Ǻ�
			if (StateNew == -1) { // �Էµ� ������ ����(StateNew)�� reject�϶� ��������(StateOld)�� ���·� reject, accept�Ǻ�
				if (accept[StateOld]) {
					acceptState = true; break;
				} // accept
				else { acceptState = false; break; } // reject
			}
			sb.append(c);
			StateOld = StateNew;
			pos++;
			if(pos < source.length())
				c = source.charAt(pos);
			else
				c = 0; // Null Character
		}
		
		if(acceptState){
			for (TokenType t : TokenType.values()){
				if(t.finalState == StateOld){
					result = new Token(t, sb.toString());
					break;
				}
			}
		}else
			System.out.println(String.format("acceptState error %s\n", sb.toString()));
		return result;
	}

	public List<Token> tokenize() {
		List<Token> a = new ArrayList<Token>();
	
		while(true){
			Token list = nextToken();
			if(list!=null){//list�� ���� �ƴϸ�
				a.add(list); //list�� token�� �߰��Ѵ�
		}
			else
				break;
		}
		return a;//��� token�� �߰� �� list���� ��ȯ 
	}

	public static void main(String[] args){
		try{//���� ��������
		File inFile=new File("as02.txt");
		BufferedReader reader = new BufferedReader(new FileReader(inFile));
		String source=reader.readLine();//������ ���پ� �д´�
		REC s = new REC(source);
		List<Token> tokens = s.tokenize();//tokenize�� ���� �ҷ���
		System.out.println(tokens.toString());//����Ʈ����
	}
	catch(Exception ex){
	}
		
	}

}
