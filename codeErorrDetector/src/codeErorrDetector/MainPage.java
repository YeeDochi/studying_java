package codeErorrDetector;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MainPage extends JPanel {
	private String data; // 작성된 코드
	private String name; // 만든 클레스의 이름
	// 자바는 파일 이름과 클레스의 이름이 같아야한다.
	private DotJava dot = new DotJava(); // 저장해주는 객체
	private sendCmd send; // cmd에서 컴파일,실행을 해주는 객체
	private JPanel CommentWindow = new JPanel(); // 스크롤바 적용을 위한 페널
	private JPanel ButtonWindow = new JPanel(); // 버튼이 들어가는 페널
	private JTextArea comments = new JTextArea(""); // 코드 입력창
	private mainFrame F; // 메인프레임
	private endPage End; // 결과페널
	private JScrollPane scroll; // 스크롤
	private boolean Dark = true; // 다크모드를 위한 변수
	private JButton b = new JButton("코드확인"); // 버튼
	private JButton change = new JButton("다크모드");

	private Color color = new Color(0x55F3F3F0, false); // 사용된,사용될 수 있는 색상
	private Color color2 = new Color(0x55D8E7EB, false); // 기본모드에 사용됨
	private Color color3 = new Color(0x55E5EBED, false);

	public MainPage(endPage end, mainFrame f, sendCmd send) { // 결과창과 메인프레임을 매개변수로 받아온다.

		this.Reset(); // 초기화
		F = f; // 받아온 매게변수를 필드 멤버에 저장
		End = end;
		this.send =send;
		this.setLayout(new BorderLayout());
		// ---------입력창------------
		CommentWindow.add(comments); // 입력창을 띄우는 페널
		scroll = new JScrollPane(comments, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED); // 스크롤바 옵션
		scroll.setBounds(getVisibleRect());
		this.add(scroll);
		scroll.setVisible(true);
		this.add(CommentWindow, BorderLayout.WEST);
		comments.setFont(new Font("Malgun Gothic", Font.BOLD, 15));

		// --------버튼--------------
		b.setFont(new Font("Malgun Gothic", Font.BOLD, 13));
		b.addActionListener(new MyActionListener()); // 엑션리스너
		change.setFont(new Font("Malgun Gothic", Font.BOLD, 13));
		change.addActionListener(new MyActionListener()); // 엑션리스너
		ButtonWindow.setLayout(new GridLayout(8, 0));
		ButtonWindow.add(b, 0, 0);
		ButtonWindow.add(change, 1, 0);
		this.add(ButtonWindow, BorderLayout.EAST);
		// -------------------------
		this.setWhite(); // 기본모드 세팅
		this.setSize(600, 400);
		this.setVisible(true);

	}

	public void Reset(String Lname, String Ldata) { // 초기화 함수
		// 클레스 이름과 코드 내용을 매개변수대로 세팅한다.
		name = Lname;
		comments.setText(Ldata);
		comments.selectAll();
	} // 메인프레임에서 로드할때 사용함

	public void Reset() { // 초기화 함수
		name = JOptionPane.showInputDialog("클레스의 이름을 입력하세요.", "Project");
		if (name == null || name == "")// 다이얼로그에서 따로 입력된것이 없거나 창을 끄면
			name = "Project";// 기본이름인 Project로 세팅해준다. 없으면 컴파일 오류가 발생함
		comments.setText("public class " + name + "{\n\n 	public static void main(String[] args){\n\n 	}\n}");
		// 메인함수를 포함한 정말 기본적인 자바 코드 세팅
		comments.selectAll();
	} // 새파일을 열때 사용되는 초기화함수 오버로딩함

	private class MyActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			String menuName = "" + e.getActionCommand();
			switch (menuName) {
			case "코드확인":
				CompileRun(); // 컴파일한다.
				F.changePanel(); // 창전환 이를위해 메인프레임을 받아옴
				break;
			case "다크모드":
				if (Dark == true) {
					setWhite(); // 기본화면 색변환
					F.setWhite(); // 메인프레임 색변환
					End.setWhite(); // 결과창 색변환 이를위해 endPage를 받아옴
					Dark = false; // 토글을 위해..
				} else {
					setDark();
					F.setDark();
					End.setDark();
					Dark = true;
				}
				break;
			}
		}
	}

	public void CompileRun() {
		runtimeTimer timer = new runtimeTimer(F, send);
		Thread th = new Thread(timer); // 쓰레드 컴파일 타임 측정
		th.start();
		data = comments.getText(); // 텍스트 읽어옴
		try {
			dot.saveAsDotJava("C:\\testCode\\", data, name);// 자바파일로 변경
			send.getCmd(name);// 변경된 파일의 이름을 넘겨서 컴파일
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		timer.stopTimer(); // 타이머정지
		End.getData(send.returnErrorMassage(), timer.returnSecond()); // 결과창에 데이터 넘기기
	}

	public String returnName() { // 파일 이름 리턴
		return name;
	}

	public String returnCodeData() { // 코드 리턴
		return comments.getText();
	}

	public void setDark() {
		this.setBackground(Color.DARK_GRAY);
		CommentWindow.setBackground(Color.DARK_GRAY);
		ButtonWindow.setBackground(Color.DARK_GRAY);
		comments.setBackground(Color.GRAY);
		scroll.setBackground(Color.DARK_GRAY);
		b.setBackground(Color.gray);
		change.setBackground(Color.gray);

		this.setForeground(Color.white);
		CommentWindow.setForeground(Color.white);
		ButtonWindow.setForeground(Color.white);
		comments.setForeground(Color.white);
		b.setForeground(Color.white);
		change.setForeground(Color.white);
		Dark = true;
	}

	public void setWhite() {
		this.setBackground(color);
		CommentWindow.setBackground(color3);
		ButtonWindow.setBackground(color3);
		comments.setBackground(color);
		scroll.setBackground(color3);
		b.setBackground(color2);
		change.setBackground(color2);

		this.setForeground(Color.black);
		CommentWindow.setForeground(Color.black);
		ButtonWindow.setForeground(Color.black);
		comments.setForeground(Color.black);
		b.setForeground(Color.black);
		change.setForeground(Color.black);
		Dark = false;
	}
}
