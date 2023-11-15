package codeErorrDetector;

public class runtimeTimer implements Runnable {
	private double second = 0;

	private boolean flag = false;

	public void stopTimer() {
		flag = true;
	}
	public double returnSecond() {
		return second;
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
			//System.out.print(String.format("%.4f",second)+"\n");
			if(flag)return;
		}
	}
}
