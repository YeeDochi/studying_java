package codeErorrDetector;

public class findError { // 받아온 스트링에서 에러코드를 검출해야한다
// java.lang.ClassNotFoundException
// 위 오류코드는 컴파일 자체가 되지 않았음을 말한다.
// 런타임 오류를 받아오는것이아닌 위에서 실행된 컴파일때 발생한 오류를 넘길 필요가 있다.
// 또한 같은 이름의 코드를 여러번 컴파일시 이전에 남아있는 클레스 파일이 실행되어 오류가 없다고 출력되는 오류가 있다.
	//ㄴ> 매번 컴파일때마다 .class는 지울것
// 런타임 오류가 발생하지 않으면 컴파일에 성공한것임


	private String Error = "";
	private String Message="";
	boolean a;

	public void findError_M(String resultData) {
		a = true; // 만약 계속 true라면 에러 없음
		String[] datas = resultData.split("\n");

		for (String s : datas) { // java.을 가지는 라인을 찾는다.
			if (s.contains("java.")) {
				Error = s;
				break;
			}
		}
		// System.out.println(Error);

		String ErrorCodes[] = Error.split(" ");
		for (String s : ErrorCodes) { // java.을 가지는 문장을 찾는다
			if (s.contains("java.")) {
				Error = s; // 일반적으로는 에러코드 자체를 저장한다.
				a = false;
				break;
			}
		}

		if (Error.contains("java.lang.ClassNotFoundException:")) { // 클레스 생성이 되지 않았을시에 오타가 있다는것
			// 이경우에는 에러코드가 아닌 오류결과창 자체를 넘겨야한다.
			int i = 0;
			Error = "1"; // 자체적으로 지정한 에러코드는 1이다.
			Message="";

			while (true) {

				Message += datas[7 + i] + "\n"; // 위에 실행을 위한 명령어를 제외한 부분부터

				if (datas[7 + i].contains("error")) {
					if (!datas[7 + i].contains("error:")) // 에러의 개수를 알려주는 곳 까지 저장
						break;
				}
				i++;
			}
		}
		if (a) { // 컴파일 성공
			Error ="0";
		}
	}

	public String returnErrorCode() {
		return Error;
		//return "java.lang.UnsupportedClassVersionError:";
	}
	public String returnMessage() {
		return Message;
	}
}
