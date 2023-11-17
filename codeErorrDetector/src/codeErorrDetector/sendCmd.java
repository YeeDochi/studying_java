package codeErorrDetector;

import java.io.*;

public class sendCmd { // 대망의 컴파일을 위한 Cmd클레스

	private String outputMessage = "";
	private ProcessBuilder b;
	Process p;
	private String varError =" "; // --release 8의 삽입을 위한 변수
	
	public void getCmd(String name) { // 저장된 .java파일을 컴파일 한다.

		// System.out.print("in CMD:" + name + "\n");
		try {
			b = new ProcessBuilder("cmd"); // cmd를 만든다
			b.redirectErrorStream(true);
			p = b.start(); // cmd시작

			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));
			//cmd명령어를 저장하는 버퍼 명령어를 쌓아둘 수 있지만 그냥 그때그떄 출력한다.

			writer.write("cd C:\\testCode\n");
			writer.flush();
			writer.write("javac -g " + name + ".java -encoding UTF-8"+varError+"\n"); // 인코딩은 그다지 차이가 없는것 같다.
			//사용되는 컴퓨터의 javac와 이코드의 자바 버전이 다른 오류가 발생했다. 
			//이를 알려야 하기때문에 --release 8명령어를 해당 에러코드가 발생하면 다음 컴파일 부터 작동하도록 설계하였다.
			writer.flush();
			writer.write("java " + name + "\n");
			writer.flush();
			writer.write("del " + name + ".class \n");
			writer.flush();
			writer.write("exit" + "\n");
			writer.flush();

			BufferedReader std = new BufferedReader(new InputStreamReader(p.getInputStream())); // 이부분이 메시지를 읽어온다
			String outputLine = "";
			outputMessage = "";
			while ((outputLine = std.readLine()) != null) {
				outputMessage += outputLine + "\r\n";
			}
			//System.out.println(outputMessage);// 디버깅 용으로 사용되는 print 
			p.waitFor();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setErrorFlagOn() {
		varError = " --release 8 ";
		System.out.print(varError);
	}
	
	public String returnErrorMassage() { // cmd에서 출력되었던 모든 데이터를 넘긴다. 처리는 여기서 안함
		return outputMessage;
	}

	public void shutDownCmd() { // 긴급종료를 위한 메소드
		try {
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));
			writer.write("^C \n");// Ctrl+c 
			writer.flush();
			writer.write("exit" + "\n"); // 종료
			writer.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void initDir() { //자바파일을 저장, 불러올 위치가 없으면 컴파일이 실행되지 않는다.
		// 메인프레임 시작과 동시에 생성하기 위해 sendCmd메소드에서 분리
		try { // 작동 방식은 같다.
			ProcessBuilder b = new ProcessBuilder("cmd");
			b.redirectErrorStream(true);
			Process p = b.start();

			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));
			writer.write("mkdir C:\\testCode \n"); // 폴터 생성을 C드라이브로 잡아서 인지 관리자 권환이 없다면 만들어지지 않는 경우가 있다.
													// 적어도 첫 실행은 관리자 실행이 필요할듯 하다.
			writer.flush();
			writer.write("del C:\\testCode\\*.class \n");
			writer.flush();
			writer.write("exit" + "\n");
			writer.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

class DotJava { // 스트링을 .java파일로 저장한다.

	public void saveAsDotJava(String root, String data, String name) throws IOException {
		FileWriter fw = new FileWriter(root + name + ".java"); // 이름은 클레스명과 같아야한다.
		fw.write(data);
		fw.close();
	}
}