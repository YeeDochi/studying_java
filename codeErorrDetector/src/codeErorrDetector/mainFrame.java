package codeErorrDetector;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Event;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileNameExtensionFilter;

public class mainFrame extends JFrame {

	private CardLayout cards = new CardLayout();
	private MainPage main;
	private endPage end;

	private String Lname;
	private String data; // 작성된 코드
	private DotJava dot = new DotJava();
	private sendCmd send = new sendCmd();

	private JMenu runMenu;
	private JMenuBar mb;
	private JMenu fileMenu;
	
	private Color color2 = new Color(0x55D8E7EB, false);
	
	public mainFrame() {
		send.makeDir(); // 폴터준비
		setTitle("ErrorDetector");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		getContentPane().setLayout(cards);

		end = new endPage(this);
		main = new MainPage(end, this);
		end.getMain(main);
		createMenu();

		setWhite();
		add("start", main); // 창 전환을 위한 추가
		add("end", end);
		setSize(600, 400);
		this.setVisible(true);

	}

	private void createMenu() {

		MenuActionListener listenr = new MenuActionListener();
		
		 mb = new JMenuBar();
		 fileMenu = new JMenu("File");

		
		JMenuItem newFile = new JMenuItem("NewFile");
		newFile.addActionListener(listenr);
		newFile.setAccelerator(KeyStroke.getKeyStroke('N', Event.CTRL_MASK)); // crt+n
		fileMenu.add(newFile);
		fileMenu.addSeparator();

		JMenuItem save = new JMenuItem("Save");
		save.addActionListener(listenr);
		save.setAccelerator(KeyStroke.getKeyStroke('S', Event.CTRL_MASK)); // crt+s
		fileMenu.add(save);
		fileMenu.addSeparator();

		JMenuItem load = new JMenuItem("Load");
		load.setAccelerator(KeyStroke.getKeyStroke('L', Event.CTRL_MASK)); // crt+l
		load.addActionListener(listenr);
		fileMenu.add(load);
		fileMenu.addSeparator();

		JMenuItem exit = new JMenuItem("Exit");
		exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0));// esc
		exit.addActionListener(listenr);
		fileMenu.add(exit);

		mb.add(fileMenu);

		runMenu = new JMenu("Run");
		JMenuItem run = new JMenuItem("Run");
		run.setAccelerator(KeyStroke.getKeyStroke(116, Event.CTRL_MASK));// crt+f5
		run.addActionListener(listenr);
		runMenu.add(run);
		mb.add(runMenu);
		setJMenuBar(mb);
		
		
	}

	public void changePanel() { // 창전환 메소드
		cards.next(this.getContentPane());
	}

	class MenuActionListener implements ActionListener {
		JFileChooser root = new JFileChooser();

		public void actionPerformed(ActionEvent e) {
			String Cmd = e.getActionCommand();
			switch (Cmd) { // 메뉴 아이템의 종류 구분
			case "Load":
				String loadPath = saveLoad(0);
				if (loadPath != null) {
					String loadedData = "";
					String loadedName = Lname;
					loadedName = loadedName.replace(".java", "");
					try {
						File loadedFile = new File(loadPath);
						Scanner sc = new Scanner(loadedFile);
						while (sc.hasNextLine()) {
							loadedData += sc.nextLine() + "\r\n";
						}
					} catch (FileNotFoundException a) {}
					main.Reset(loadedName, loadedData);
				}
				break;

			case "Save":
				String savePath = saveLoad(1);// 경로 호출
				if (savePath != null) {
					try {
						data = main.comments.getText();
						dot.saveAsDotJava(savePath, data, main.returnName());
					} catch (Exception ex) {
						ex.printStackTrace();
					}
					JOptionPane.showMessageDialog(null, savePath + "에 저장되었습니다.");
				}
				break;

			case "NewFile": // 새로운 클레스 이름을 받아오며 초기화
				int result = JOptionPane.showConfirmDialog(null, "초기화 하시겠습니까?");
				if (result == JOptionPane.YES_OPTION)
					main.Reset(null, null); // 초기화 함수
				System.out.print("new");
				break;

			case "Exit":
				System.exit(0); // 나가기
				break;

			case "Run": // 메소드로 구성할까...?
				main.CompileRun();
				changePanel();
				break;

			}
		}
	}

	public String saveLoad(int Option) {
		JFileChooser fileChooser = new JFileChooser();
		if (Option == 1) {// 저장시 세팅
			fileChooser.setFileSelectionMode(fileChooser.DIRECTORIES_ONLY);
		} else if (Option == 0) { // 불러오기시 세팅
			FileNameExtensionFilter JAVA = new FileNameExtensionFilter("java파일", "java");
			fileChooser.addChoosableFileFilter(JAVA);
			fileChooser.setFileFilter(JAVA);
		}

		fileChooser.setCurrentDirectory(new File("C:\\testCode\\")); // 시작위치 testCode
		File selectedFile;
		int result = fileChooser.showOpenDialog(this);
		if (result == JFileChooser.APPROVE_OPTION) {// 확인 눌리면,파일이 선택되면
			selectedFile = fileChooser.getSelectedFile();// 변수에 저장
			if (Option == 0)
				Lname = selectedFile.getName();
		} else
			return null;// 만약 취소시 기본 저장위치로(? 수정해야될듯)
		return selectedFile.getAbsolutePath() + "\\";
	}

	public static void main(String[] args) {
		new mainFrame();
	}
	
	public void setDark() {
		mb.setBackground(Color.gray);
		fileMenu.setBackground(Color.gray);
	}
	public void setWhite() {
		mb.setBackground(color2);
		fileMenu.setBackground(color2);
	}
}
