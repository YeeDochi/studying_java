package codeErorrDetector;

public class runtimeTimer implements Runnable { // 런타임 측정을 위한 스레드
	// 페널에 타이머를 만들려했으나 컴파일중에 먹통이 되어 스레드로 분류
	private double second;
	private boolean flag; // 타이머 정지를 위한 boolean
	private mainFrame F; // 강제종료를 위한 메인프레임
	private sendCmd Send; // 커멘드창의 강제종료를 위한 cmd객체

	public runtimeTimer(mainFrame f,sendCmd send) {
		F = f;
		second = 0;
		flag = false;
		Send = send;

	}

	public void stopTimer() { // 정지 메소드
		flag = true;
	}

	public double returnSecond() { // 초를 리턴해주는 메소드
		return second;
	}

	public void sutdown() { // 런타임이 20초를 넘으면 비정상적인 무한루프
		// cmd와 메인프레임을 강제 종료한다.
		if (second > 20) {
			Send.shutDownCmd();
			F.shutdown();
		}
	}

	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(1); // 0.001초
			} catch (Exception e) {
				e.printStackTrace();
			}

			second += 0.001;
			sutdown(); // 20초 보다 오래 걸릴시 작동 종료
			System.out.print(String.format("%.4f", second) + "\n");
			if (flag)
				return;
		}
	}
}
