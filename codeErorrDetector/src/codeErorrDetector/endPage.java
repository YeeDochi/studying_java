package codeErorrDetector;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class endPage extends JPanel { // 단순한 출력페이지
	private JTextArea Result = new JTextArea("");
	private JPanel ResultWindow = new JPanel();
	private JPanel ButtonWindow = new JPanel();
	private mainFrame F;
	private findError find = new findError();
	private JPanel errorWindow = new JPanel();
	private Errors errorLabel = new Errors();
	private JButton b = new JButton("돌아가기");
	private JButton change = new JButton("다크모드");
	
	private MainPage Main;
	private boolean Dark = false;
	// private double Timer;
	// 다크모드를 위해선 전부 필드멤버가 되어야함
	private Color color =new Color(0x55F3F3F0,false);
	private Color color2 = new Color(0x55D8E7EB, false);
	private Color color3 = new Color(0x55E5EBED, false);
	public endPage(mainFrame f) {

		setLayout(new BorderLayout());
		ResultWindow.setLayout(new GridLayout(2, 0));
		// ---------결과창-TEXT---------

		ResultWindow.add(Result, 2, 0);
		JScrollPane scroll = new JScrollPane(Result, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroll.setBounds(getVisibleRect());
		ResultWindow.add(scroll);
		scroll.setVisible(true);
		Result.setFont(new Font("Malgun Gothic",Font.BOLD,15));
		
		// ---------결과창-LABEL---------

		errorWindow.add(errorLabel);
		errorWindow.setSize(100, 70);
		errorWindow.setVisible(true);
		ResultWindow.add(errorWindow, 1, 0);
		add(ResultWindow, BorderLayout.CENTER);

		// ----------------------------

		setWhite();
		
		setSize(600, 400);
		this.setVisible(true);
		F = f;
		
		ButtonWindow.setLayout(new GridLayout(8,0));
		b.setFont(new Font("Malgun Gothic",Font.BOLD,13));
		b.addActionListener(new MyActionListener());
		change.setFont(new Font("Malgun Gothic",Font.BOLD,13));
		change.addActionListener(new MyActionListener());
		ButtonWindow.add(b,1,0);
		ButtonWindow.add(change,0,0);
		add(ButtonWindow, BorderLayout.EAST);

	}

	private class MyActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			String menuName =""+e.getActionCommand();
			switch(menuName) {
			case"돌아가기":
				F.changePanel(); // 창전환
				break;
			case"다크모드":
				if (Dark == true) {
					setWhite();
					F.setWhite();
					Main.setWhite();
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

	public void getMain(MainPage main) {
		Main = main;
	}
	
	public void getData(String error, double timer) {
		// Timer = timer;
		find.findError_M(error);
		errorLabel.setFont(new Font("Malgun Gothic", Font.BOLD, 11)); // 폰트변경
		errorLabel.getErrorCode(find.returnErrorCode(), "<h4>" + String.format("%.3f", timer) + "초 걸리셨네요</h4></html>");// 결과
																														// 라벨
																														// 출력
		if (find.returnErrorCode() == "1")
			Result.setText(find.returnMessage()); // 결과 텍스트 에리어 출력
		else
			Result.setText(find.returnErrorCode());// 결과 텍스트 에리어 출력
	}

	public void setDark() {

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
		

	}
	public void setWhite() {

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

	}
}
