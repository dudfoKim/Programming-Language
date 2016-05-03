package cute14_Parser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;



public class cute14 {

	private int transM[][];
	private boolean accept[];
	private String source;
	private int pos;

	public cute14(String source) {
		this.accept = new boolean[]{false,true,true,true,true,true,true,true,true,true,true,true,true,true,true,false};// Final states
		//0�� '#'�϶��� �������°� �� �� ���� �������϶��� �������°� ����
		this.transM = new int[27][128];
		this.source = source == null ? "" : source;
		init_TM();
	}


	private void init_TM() {
		for(int i=0;i<16;i++){ 
			for(int j=0;j<128;j++)
				transM[i][j] = -1;
		}

		/* �ʱ� ���¿��� Special Symbols�� ���� ���� ���°� ���� */
		transM[0]['-'] = 4;
		transM[0]['+'] = 5;
		transM[0]['\''] = 6;
		transM[0]['='] = 7;
		transM[0]['('] = 8;
		transM[0][')'] = 9;
		transM[0]['*'] = 10;
		transM[0]['/'] = 11;
		transM[0]['<'] = 12;
		transM[0]['>'] = 13;

		for(int i=0;i<16;i++){
			for(int j=0;j<128;j++){
				if(i == 0){//�ʱ� State
					if (j == '#') //�Է� ���� # �� ���
						transM[i][j] = 1;

					else if( (j > 47) && (j < 58) )	//�Է� ���� ����(0~9)�� ���
						transM[i][j] = 2; //digit ����

					//�Է� ���� ������ ���.(a~z && A~Z)
					else if ( ((j > 64)&&(j < 91)) || ((96 < j)&&(j < 123)) ) 
						transM[i][j] = 3; //a~z,A~Z ����.
				}

				if(i == 1){ //#�� �Էµ� ���� State
					if(j == 'T')//�Է� ���� T �ΰ��
						transM[i][j] = 14;
					else if(j == 'F') //�Է� ���� F �� ���
						transM[i][j] = 15;
				}

				if(i == 2){//���� ���°� INT �� ���
					if( (j > 47) && (j < 58) ) //digit
						transM[i][j] = i;
				}
				if(i == 3){//���� ���°� ID�� ���
					if ( ((j > 64)&&(j < 91)) || ((96 < j)&&(j < 123)) || j == '?') 
						transM[i][j] = i; 
					else if( (j > 47) && (j < 58) ) //digit
						transM[i][j] = i;
				}
				if(i == 4){// ���� ���°� '-' �� ���
					if(  (j > 47) && (j < 58) ){  //���ڰ� �Է����� ���� ���
						transM[i][j] = 2; //���� State�� INT�� �ȴ�.
					}
				}
				if(i == 5){// ���� ���°� '+' �� ���
					if((j > 47) && (j < 58)){ //���ڰ� �Է����� ���� ���
						transM[i][j] = 2; //���� State�� INT�� �ȴ�.
					}
				}
			}
		}

	}

	private Token nextToken(){
		int StateOld = 0, StateNew;
		boolean acceptState = false;
		char c = Character.SPACE_SEPARATOR;
		StringBuffer sb = new StringBuffer();
		Token result = null;

		while( pos < source.length() ){
			if(!Character.isWhitespace( c = source.charAt(pos)))
				break;
			pos++;
		}

		if( pos >= source.length() )
			return null;

		while (!acceptState) {
			StateNew = transM[StateOld][c];	//�Է¹��ڷ� ���ο� ���� �Ǻ�

			if (StateNew == -1) {	// �Էµ� ������ ����(StateNew)�� reject�϶� ��������(StateOld)�� ���·� reject, accept�Ǻ�
				if (accept[StateOld]) { 
					acceptState = true;	break; 
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
				String lexeme = sb.toString();
				if(t.finalState == StateOld){
					TokenType keyWord = TokenType.keyWordCheck(lexeme);
					if(keyWord != null){// Token Type�� KeyWord�� ��� 
						result = new Token(keyWord, lexeme);//KeyWord ��ȯ
					}
					else{
						result = new Token(t, lexeme);
					}
					break;
				}
			}
		}
		else
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
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		//read file�� 
		byte[] bytes = Files.readAllBytes(Paths.get("./as04.txt"));
		String source = new String(bytes);

		cute14 s = new cute14(source);
		List<Token> tokens = s.tokenize();
		BasicParser p = new BasicParser(tokens); 
		Node node = p.parseList(); 

		/*for(int i = 0 ; i < tokens.size() ; i++){
			System.out.println(tokens.get(i));
		}*/
		Printer pt = new Printer(System.out); 
		pt.printList(((ListNode)node).value);

	}
}