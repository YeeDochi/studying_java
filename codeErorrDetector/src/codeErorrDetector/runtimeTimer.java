package codeErorrDetector;

import javax.swing.JOptionPane;

public class runtimeTimer implements Runnable {
	private double second;
	private boolean flag;
	private mainFrame F;
	private sendCmd Send;

	public runtimeTimer(mainFrame f,sendCmd send) {
		F = f;
		second = 0;
		flag = false;
		Send = send;

	}

	public void stopTimer() {
		flag = true;
	}

	public double returnSecond() {
		return second;
	}

	public void sutdown() {
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
