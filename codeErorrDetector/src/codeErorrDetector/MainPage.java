package codeErorrDetector;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MainPage extends JPanel {
	private String data; // 작성된 코드
	private String name; // 클레스의 이름
	private DotJava dot = new DotJava();
	private sendCmd send = new sendCmd();
	private JPanel CommentWindow = new JPanel(); // 스크롤바 적용을 위한 페널
	private JPanel ButtonWindow = new JPanel(); // 버튼이 들어가는 페널
	public JTextArea comments = new JTextArea("");
	private mainFrame F;
	private endPage End;
	private JScrollPane scroll;
	private boolean Dark = true;
	private JButton b = new JButton("코드확인"); // 버튼
	private JButton change = new JButton("다크모드");

	private Color color = new Color(0x55F3F3F0, false);
	private Color color2 = new Color(0x55D8E7EB, false);
	private Color color3 = new Color(0x55E5EBED, false);
	public MainPage(endPage end, mainFrame f) {

		Reset(null, null);
		F = f;
		End = end;
		setLayout(new BorderLayout());
		// ---------입력창------------
		CommentWindow.add(comments); // 입력창을 띄우는 페널
		scroll = new JScrollPane(comments, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED); // 스크롤바 옵션
		scroll.setBounds(getVisibleRect());
		add(scroll);
		scroll.setVisible(true);
		add(CommentWindow, BorderLayout.WEST);
		comments.setFont(new Font("Malgun Gothic", Font.BOLD, 15));

		// --------버튼--------------
		b.setFont(new Font("Malgun Gothic", Font.BOLD, 13));
		b.addActionListener(new MyActionListener()); // 엑션리스너
		change.setFont(new Font("Malgun Gothic", Font.BOLD, 13));
		change.addActionListener(new MyActionListener()); // 엑션리스너
		ButtonWindow.setLayout(new GridLayout(8,0));
		ButtonWindow.add(b,0,0);
		ButtonWindow.add(change,1,0);
		add(ButtonWindow, BorderLayout.EAST);

		setWhite();
		setSize(600, 400);
		this.setVisible(true);

	}

	public void Reset(String Lname, String Ldata) {

		if (Lname == null && Ldata == null) {
			name = JOptionPane.showInputDialog("클레스의 이름을 입력하세요.", "Project");
			if (name == null || name == "")
				name = "Project";
			comments.setText("public class " + name + "{\n\n public static void main(String[] args){\n\n}\n}");
		} else {
			name = Lname;
			comments.setText(Ldata);
		}
		comments.selectAll();
	}

	private class MyActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			String menuName = "" + e.getActionCommand();
			switch (menuName) {
			case "코드확인":
				CompileRun();
				F.changePanel();
				break;
			case "다크모드":
				if (Dark == true) {
					setWhite();
					F.setWhite();
					End.setWhite();
					Dark = false;
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
		runtimeTimer timer = new runtimeTimer();
		Thread th = new Thread(timer); // 쓰레드 컴파일 타임 측정
		th.start();
		data = comments.getText(); // 텍스트 읽어옴
		try {
			dot.saveAsDotJava("C:\\testCode\\", data, name);// 자바파일로 변경
			send.getCmd(name);// 변경된 파일의 이름을 넘겨서 컴파일
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		timer.stopTimer();
		End.getData(send.returnErrorMassage(), timer.returnSecond());
	}

	public String returnName() {
		return name;
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
