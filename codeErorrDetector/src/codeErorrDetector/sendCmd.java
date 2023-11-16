package codeErorrDetector;

import java.io.*;
import java.util.Scanner;

public class sendCmd {

	private String outputMessage = "";
	boolean ErrorShutdown;
	ProcessBuilder b;
	Process p;
	
	public sendCmd(){
		ErrorShutdown = false;
	}
	
	public boolean getCmd(String name) { // 저장된 .java파일을 컴파일 한다.

		// System.out.print("in CMD:" + name + "\n");
		try {
			b = new ProcessBuilder("cmd");
			b.redirectErrorStream(true);
			p = b.start();

			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));

			writer.write("cd C:\\testCode\n");
			writer.flush();
			writer.write("javac -g " + name + ".java -encoding UTF-8\n");
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
			//System.out.println(outputMessage);// 오류 출력 스윙으로 넘기는 기능 추가로 비활성화함
			p.waitFor();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return ErrorShutdown;
	}

	public String returnErrorMassage() {
		return outputMessage;
	}

	public void shutDownCmd() {
		try {
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));
			writer.write("^C \n");
			writer.flush();
			writer.write("exit" + "\n");
			writer.flush();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void initDir() {
		try {
			ProcessBuilder b = new ProcessBuilder("cmd");
			b.redirectErrorStream(true);
			Process p = b.start();

			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));
			writer.write("mkdir C:\\testCode \n");
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