package codeErorrDetector;

import java.util.HashMap;

import javax.swing.JLabel;

public class Errors extends JLabel { // 에러 클레스는 에러 메세지를 받아 멘트를 라벨에 띄워준다.

	sendCmd send;
	endPage End;

	public Errors(sendCmd temp,endPage end) {
		send = temp;
		End =end;
	}

	HashMap<String, String> errortext = new HashMap<String, String>() { // 에러코드에 따른 메세지를 저장하는 헤시멥
		{
			put("java.lang.instrument��", "<html><h2>클레스 명을 적지 않으셨네요</h2><h3>파일을 다시 만들어 보자구요</h3>");
			put("0", "<html><h2>축하합니다. 오류가 없네요.</h2>");
			put("1", "<html><h2>코드 작성에 어딘가<br>오타가 있나봐요...</h2><h3>오타의 대강의 위치는 다음과 같아요.</h3>");
			put("java.lang.ArithmeticException:", "<html><h2>혹시 정수를 0으로 나누었나요...?</h2><h4>그거 진심인가요?</h4>");
			put("java.lang.NullPointerException:", "<html><h2>자꾸 없는걸 찾으시면<br>곤란한데요...</h2><h4>어디선가 null을 찾는사람이 있네요</h4>");
			put("java.io.FileNotFoundException:", "<html><h2>찾으시는 파일이<br>없는것 같아요</h2><h4>진짜 있긴 한건가요?</h4>");
			put("java.lang.ArrayIndexOutOfBoundsException:", "<html><h2>배열!! 배열이 넘친다구요!!</h2><h4>인덱스에 문제가 있네요</h4>");
			put("java.lang.ClassCastException:", "<html><h2>그런식의 케스팅은<br> 안되는것 같아요</h2><h4>업,다운케스팅중 뭔가 잘못되었겠죠</h4>");// 이것도
																													// 오타로써
																													// 받아들이는듯
			put("java.lang.OutOfMemoryError:", "<html><h2>메모리 넘쳐요...</h2><h4></h4>");
			put("java.lang.NegativeArraySizeException:", "<html><h2>배열의 크기가 음수요...?</h2><h4>무슨 -를 배열에 써넣는건가요..</h4>");
			put("java.lang.NoClassDefFoundError:", "<html><h2>클레스도 제대로<br>못쓰는 허접~</h2><h4>자바공부 다시하고와~~</h4>");
			put("java.lang.UnsupportedClassVersionError:",
					"<html><h2>Javac와 Java의 버전이 맞질 않네요</h2><h4>자바 버전을 맞춰야겠네요,</h4><h4>다시 컴파일 해 보시겠어요?</h4>");
			// put("","<html><h2></h2><h4></h4>"); /ㅊ .
		}
	};

	public void getErrorCode(String Code, String time) {
		String output = errortext.get(Code);
		// System.out.print(output);
		setText(output + time);
		
		
		if(Code == "0")End.setResultImage("축하.gif");
		else End.setResultImage("실패.png");
		if (Code == "java.lang.UnsupportedClassVersionError:") {
			send.setErrorFlagOn();
		}
	}

}
