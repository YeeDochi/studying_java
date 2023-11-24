package codeErorrDetector;

public class compileThread implements Runnable { // 컴파일을 하는 스레드 클레스

	private sendCmd send;
	private DotJava dot;
	private endPage End;
	private MainPage Main;
	private runtimeTimer time;
	private mainFrame F;

	public compileThread(sendCmd send, DotJava dot, endPage End, MainPage Main, runtimeTimer time, mainFrame F) {

		this.send = send;
		this.dot = dot;
		this.End = End;
		this.Main = Main;
		this.time =time;
		this.F =F;
	}

	public void setEndM_T() {
		double timer = time.returnSecond();
		End.getData(send.returnErrorMassage(), timer);
	}

	@Override
	public void run() {
		End.compileBtn(false); // 돌아가기 비활성화
		F.compile(false);
		String data = Main.returnCodeData(); // 텍스트 읽어옴
		try {
			dot.saveAsDotJava("C:\\testCode\\", data, Main.returnName());// 자바파일로 변경
			send.getCmd(Main.returnName());// 변경된 파일의 이름을 넘겨서 컴파일
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		time.stopTimer(); // 타이머 정지
		setEndM_T(); // 결과부 출력
		End.compileBtn(true); // 돌아가기 활성화
		F.compile(true);
		
	}
}
