package codeErorrDetector;

public class findError { // 받아온 스트링에서 에러코드를 검출해야한다
// java.lang.ClassNotFoundException
// 단순한 문법오류로 컴파일 자체가 되지 않았음 
// 상위로 올라가 어느 부분이 잘못되었는지를 찾을 필요가 있다.

// 런타임 오류가 발생하지 않으면 컴파일에 성공한것임

	private String Error = "";
	private String Message="";
	boolean a;

	public void findError_M(String resultData) {
		a = true;
		String[] datas = resultData.split("\n");

		for (String s : datas) {
			if (s.contains("java.")) {
				Error = s;
				break;
			}
		}
		// System.out.println(Error);

		String ErrorCodes[] = Error.split(" ");
		for (String s : ErrorCodes) {
			if (s.contains("java.")) {
				Error = s;
				a = false;
				break;
			}
		}

		if (Error.contains("java.lang.ClassNotFoundException:")) { // 클레스 생성이 되지 않았을시에 오타가 있다는것
			int i = 0;
			Error = "1";
			Message="";

			while (true) {

				Message += datas[7 + i] + "\n";

				if (datas[7 + i].contains("error")) {
					if (!datas[7 + i].contains("error:"))
						break;
				}
				i++;
			}
		}
		if (a) {
			Error ="0";
		}
	}

	public String returnErrorCode() {
		return Error;
	}
	public String returnMessage() {
		return Message;
	}
}
