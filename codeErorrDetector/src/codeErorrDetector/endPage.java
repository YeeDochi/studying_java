package codeErorrDetector;

import java.awt.*;
import java.awt.event.*;
import java.net.URL;

import javax.swing.*;

public class endPage extends JPanel { // 단순한 출력페이지
	private mainFrame F; // 메인프레임을 받아오는 녀석
	private JTextArea Result = new JTextArea(""); // 에러코드 출력 텍스트창
	private JPanel ResultWindow = new JPanel(); // 텍스트창을 받는 페널
	private JPanel ButtonWindow = new JPanel(); // 버튼을 받는 페널
	private findError find = new findError(); // 에러를 구분해주는 클레스
	private JPanel errorWindow = new JPanel(); // 에러창 라벨을 받는페널
	private Errors errorLabel; // 재밌는 에러 메세지 출력을 위한 라벨
	private JLabel errorImage;
	private JLabel counter = new JLabel();
	private JButton b = new JButton("돌아가기"); // 버튼들 색 변환을위해 밖으로 빼 두었다.
	private JButton change = new JButton("다크모드");

	private MainPage Main; // 메인페이지를 받아오는 녀석
	private boolean Dark = false; // 다크모드를 위한 boolean
	// private double Timer;
	// 다크모드를 위해선 전부 필드멤버가 되어야함
	private Color color = new Color(0x55F3F3F0, false);
	private Color color2 = new Color(0x55D8E7EB, false);
	private Color color3 = new Color(0x55E5EBED, false); // 기본상태일때 쓰인 색들

	public endPage(mainFrame f, sendCmd send) {// 생성자가 메인프레임을 받아온다.
		errorLabel = new Errors(send, this);
		errorImage = new JLabel();
		setLayout(new BorderLayout()); // 창 자체의 레이아웃 보더레이아웃
		ResultWindow.setLayout(new GridLayout(2, 0)); // 결과를 띄우는 페널은
		// ---------결과창-TEXT---------

		ResultWindow.add(Result, 2, 0); // 그리드레이아웃으로 위치를 정해줌
		JScrollPane scroll = new JScrollPane(Result, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED); // 스크롤 설정
		scroll.setBounds(getVisibleRect());
		ResultWindow.add(scroll);
		scroll.setVisible(true);
		Result.setFont(new Font("Malgun Gothic", Font.BOLD, 15));

		// ---------결과창-LABEL---------
		errorWindow.setLayout(new GridLayout(0, 2));
		errorWindow.add(errorLabel);
		errorWindow.setSize(100, 70);
		errorWindow.setVisible(true);
		errorWindow.add(errorImage);
		ResultWindow.add(errorWindow, 1, 0);
		add(ResultWindow, BorderLayout.CENTER);

		// ----------------------------

		setWhite(); // 기본 화면색 설정

		setSize(600, 400);
		this.setVisible(true);
		F = f; // 메인프레임을 받아옴
		// -----------버튼들--------------
		ButtonWindow.setLayout(new GridLayout(8, 0));
		b.setFont(new Font("Malgun Gothic", Font.BOLD, 13));
		b.addActionListener(new MyActionListener());
		change.setFont(new Font("Malgun Gothic", Font.BOLD, 13));
		change.addActionListener(new MyActionListener());
		ButtonWindow.add(b, 1, 0);
		ButtonWindow.add(change, 0, 0);
		add(ButtonWindow, BorderLayout.EAST);
		// --------카운터-------------- // 창에 시간초를 표기하고 싶었으나 무리였다.
		for (int i = 2; i < 7; i++) {
			ButtonWindow.add(new JLabel());
		}
		ButtonWindow.add(counter);
		counter.setText("0.0000");
		counter.setFont(new Font("Malgun Gothic", Font.BOLD, 20));
	}

	private class MyActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			String menuName = "" + e.getActionCommand();
			switch (menuName) {
			case "돌아가기":
				F.changePanel(); // 창전환
				break;
			case "다크모드":
				if (Dark == true) {
					setWhite(); // 이 클레스의 색변환
					F.setWhite(); // 메인프레임(매뉴바)의 색변환
					Main.setWhite(); // 시작화면의 색변환
					Dark = false;
				} else {
					setDark();
					F.setDark();
					Main.setDark();
					Dark = true;
				}
				break;
			}
		}
	}

	public void getMain(MainPage main) { // 시작화면을 받아옴
		Main = main;
	} // 생성자에 넣으려했지만 메인프레임에서 end를 먼저 생성하기 때문에 서순으로 인해 생성자로는 받아오지 못했다.

	public void getData(String error, double timer) {
		// Timer = timer;
		String Error = find.findError_M(error);
		if (Error == "0")
			F.audio("성공");
		else
			F.audio("실패");
		errorLabel.setFont(new Font("Malgun Gothic", Font.BOLD, 11)); // 폰트변경
		errorLabel.getErrorCode(Error, "<h4>" + String.format("%.3f", timer) + "초 걸리셨네요</h4></html>");
		// 결과를 라벨창에 출력 런타임 시간정보도 출력한다.
		if (find.returnErrorCode() == "1") // 에러코드 1은 오타가 있을시 발생한다.
			// 이경우에는 오타가 발생한 부분을 따로 가져와 출력해준다.
			Result.setText(find.returnMessage()); // 결과 텍스트 에리어 출력
		else
			Result.setText(find.returnErrorCode());// 결과 텍스트 에리어 출력
	}
	public void compile() { // 컴파일 중일때의 화면
		setResultImage("진행중.gif");
		errorLabel.setText("<html><h2>컴파일 진행중 입니다....</h2></html>");
		Result.setText("");
	}
	public void compileBtn(boolean temp) { // 돌아가기 버튼 활성화, 비활성화
		b.setEnabled(temp);
	}

	public void setResultImage(String Link) { // 이미지 세팅
		URL image = mainFrame.class.getClassLoader().getResource(Link);
		ImageIcon icon = new ImageIcon(image);
		Image img = icon.getImage();
		Image changedImage = img.getScaledInstance(200, 140, Image.SCALE_FAST);
		icon = new ImageIcon(changedImage);
		errorImage.setIcon(icon);
	}

	public JLabel returnTimer() { // 타이머 리턴
		return counter;
	}

	public void setDark() { // 다크모드

		this.setBackground(Color.DARK_GRAY);
		Result.setBackground(Color.gray);
		ResultWindow.setBackground(Color.DARK_GRAY);
		ButtonWindow.setBackground(Color.DARK_GRAY);
		errorWindow.setBackground(Color.DARK_GRAY);
		errorLabel.setBackground(Color.DARK_GRAY);
		b.setBackground(Color.gray);
		change.setBackground(Color.gray);

		Result.setForeground(Color.white);
		ResultWindow.setForeground(Color.white);
		ButtonWindow.setForeground(Color.white);
		errorWindow.setForeground(Color.white);
		errorLabel.setForeground(Color.white);
		b.setForeground(Color.white);
		change.setForeground(Color.white);
		counter.setForeground(Color.white);

	}

	public void setWhite() { // 평상시 모드

		this.setBackground(color);
		Result.setBackground(color);
		ResultWindow.setBackground(color3);
		ButtonWindow.setBackground(color3);
		errorWindow.setBackground(color);
		errorLabel.setBackground(color);
		b.setBackground(color2);
		change.setBackground(color2);

		Result.setForeground(Color.black);
		ResultWindow.setForeground(Color.black);
		ButtonWindow.setForeground(Color.black);
		errorWindow.setForeground(Color.black);
		errorLabel.setForeground(Color.black);
		b.setForeground(Color.black);
		change.setForeground(Color.black);
		counter.setForeground(Color.black);

	}
}
