package codeErorrDetector;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.Scanner;

import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class mainFrame extends JFrame {

	private CardLayout cards = new CardLayout(); // 화면전환을 위한 레이아웃
	private MainPage main; // 시작 페널
	private endPage end; // 결과 페널

	private String Lname; // 파일 탐색기로 가져오는 파일의 이름
	private String data; // 작성된 코드
	private DotJava dot = new DotJava(); // 저장 객체
	private sendCmd send = new sendCmd(); // 컴파일 객체

	// -----------메뉴----------
	private JMenu runMenu;
	private JMenuBar mb;
	private JMenu fileMenu;

	private Color color2 = new Color(0x55D8E7EB, false); // 기본 색상 메뉴 색

	public mainFrame() {
		URL image = mainFrame.class.getClassLoader().getResource("ErrorCodeDetectorIcon.png");
		// exe환경에서 작동하도록 경로를 따와서 적용하도록 변경
		ImageIcon img = new ImageIcon(image);
		setIconImage(img.getImage());
		send.initDir(); // 폴터준비
		this.setTitle("ErrorDetector");
		this.addWindowListener(new WindowCl()); // 창 종료시에 사용될 이벤트
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE); // 위에 이벤트때문에 x를 눌러도 닫히지 않도록 설정
		// setDefaultCloseOperation(EXIT_ON_CLOSE);
		getContentPane().setLayout(cards); // 카드 레이아웃
		this.end = new endPage(this, send); // end가 main을 받아와야 하지만 main보다 먼저 생성됨
		main = new MainPage(end, this, send); // main은 정상적으로 인수를 받아온다.
		end.getMain(main); // main이 생성되고나서 다시 인수를 넘겨준다.
		this.createMenu(); // 메뉴바
		this.setWhite(); // 기본색으로 설정

		this.add("start", main); // 창 전환을 위한 추가
		this.add("end", end);

		setSize(600, 400);

		Dimension frameSize = this.getSize();// 프레임창의 크기
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();// 모니터 크기를 잰다
		// (모니터화면 가로 - 프레임화면 가로) / 2, (모니터화면 세로 - 프레임화면 세로) / 2 -> 중앙
		this.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
		// 창을 중앙에 위치시킨다.

		this.setVisible(true);

	}

	private void createMenu() {

		MenuActionListener listenr = new MenuActionListener();// 이벤트 리스너

		mb = new JMenuBar();
		fileMenu = new JMenu("File");

		// 모든 메뉴에는 단축키 설정이 되어있다.

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

	private class WindowCl implements WindowListener { // x로 종료시 메시지 출력

		@Override
		public void windowClosing(WindowEvent e) { // x를 누르면 창이 닫히는 대신 이 이벤트가 실행
			audio("경고음");
			int temp = JOptionPane.showConfirmDialog(null, "종료전 저장하시겠습니까?");
			if (0 == temp) {
				String savePath = saveLoad(1);// 경로 호출
				if (savePath != null) {
					try {
						data = main.returnCodeData(); // 입력되어있는 코드를 데이터로
						dot.saveAsDotJava(savePath, data, main.returnName());// 따온 페스와 데이터,이름으로 .java로 저장
					} catch (Exception ex) {
						ex.printStackTrace();
					}
					JOptionPane.showMessageDialog(null, savePath + "에 저장되었습니다."); // 실행 알림
				}
				shutdown(); // 저장 되면 종료
			} else if (1 == temp) { // 취소시 그냥 종료
				setDefaultCloseOperation(EXIT_ON_CLOSE);
			}
		}

		public void windowClosed(WindowEvent e) {
		}

		public void windowOpened(WindowEvent e) {
		}

		public void windowIconified(WindowEvent e) {
		}

		public void windowDeiconified(WindowEvent e) {
		}

		public void windowActivated(WindowEvent e) {
		}

		public void windowDeactivated(WindowEvent e) {
		}

	}

	private class MenuActionListener implements ActionListener {
		JFileChooser root = new JFileChooser();

		public void actionPerformed(ActionEvent e) {
			String Cmd = e.getActionCommand();
			switch (Cmd) { // 메뉴 아이템의 종류 구분
			case "Load":
				String loadPath = saveLoad(0); // 가져올 파일의 경로
				if (loadPath != null) { // 선택이 되었다면
					String loadedData = "";// 데이터 받을 변수 초기화
					String loadedName = Lname;// 세이브 로드메소드에서 가져온 이름을 세팅
					// Lname을 그대로 쓰지 않는것은 비정상적 종료시 전에 사용한 이름이 남아있는 오류를 발견해서이다.
					loadedName = loadedName.replace(".java", "");
					try {
						File loadedFile = new File(loadPath);
						Scanner sc = new Scanner(loadedFile); // 파일을 스케너 객체에 저장
						while (sc.hasNextLine()) {
							loadedData += sc.nextLine() + "\r\n";// 한줄씩 따오면서 데이터에 저장한다.
						}
					} catch (FileNotFoundException a) {
					}
					main.Reset(loadedName, loadedData); // 초기화 함수를 가져와 위의 정보들로 초기화
					audio("알림음");
					JOptionPane.showMessageDialog(null, Lname + " 를 불러왔습니다.");
				}
				break;

			case "Save":
				String savePath = saveLoad(1);// 경로 호출
				if (savePath != null) {
					try {
						data = main.returnCodeData(); // 입력되어있는 코드를 데이터로
						dot.saveAsDotJava(savePath, data, main.returnName());// 따온 페스와 데이터,이름으로 .java로 저장
					} catch (Exception ex) {
						ex.printStackTrace();
					}
					audio("알림음");
					JOptionPane.showMessageDialog(null, savePath + "에 저장되었습니다."); // 실행 알림
				}
				break;

			case "NewFile": // 새로운 클레스 이름을 받아오며 초기화
				audio("경고음");
				int result = JOptionPane.showConfirmDialog(null, "초기화 하시겠습니까?");
				if (result == JOptionPane.YES_OPTION)
					main.Reset(); // 초기화 함수
				System.out.print("new");
				break;

			case "Exit":
				audio("경고음");
				int temp = JOptionPane.showConfirmDialog(null, "종료전 저장하시겠습니까?");
				if (0 == temp) {
					savePath = saveLoad(1);// 경로 호출
					if (savePath != null) {
						try {
							data = main.returnCodeData(); // 입력되어있는 코드를 데이터로
							dot.saveAsDotJava(savePath, data, main.returnName());// 따온 페스와 데이터,이름으로 .java로 저장
						} catch (Exception ex) {
							ex.printStackTrace();
						}
						audio("알림음");
						JOptionPane.showMessageDialog(null, savePath + "에 저장되었습니다."); // 실행 알림
					}
					shutdown();
				} else if (1 == temp)
					shutdown(); // 나가기
				break;

			case "Run": // 메소드로 구성할까...?
				main.CompileRun();
				changePanel();
				break;

			}
		}
	}

	public void shutdown() { // 종료함수, 스레드에서 강제종료를 위해 분리시킴
		audio("경고음");
		System.exit(0);
	}

	public String saveLoad(int Option) { // 대망의 저장 불러오기 함수
		JFileChooser fileChooser = new JFileChooser(); // filechooser을 이용함
		if (Option == 1) {// 저장시 세팅
			fileChooser.setFileSelectionMode(fileChooser.DIRECTORIES_ONLY); // 폴터만 고르도록 세팅
		} else if (Option == 0) { // 불러오기시 세팅
			FileNameExtensionFilter JAVA = new FileNameExtensionFilter("java파일", "java"); // 자바 파일만 고르도록 세팅
			fileChooser.addChoosableFileFilter(JAVA);
			fileChooser.setFileFilter(JAVA);
		}

		fileChooser.setCurrentDirectory(new File("C:\\testCode\\")); // 시작위치 testCode
		File selectedFile;// 골라진 파일의 정보가 저장되는 파일 객체
		int result = fileChooser.showOpenDialog(this);
		if (result == JFileChooser.APPROVE_OPTION) {// 창이 열림을 확인
			selectedFile = fileChooser.getSelectedFile();// 파일객체에 저장
			if (Option == 0)
				Lname = selectedFile.getName(); // 이름을 따온다
		} else
			return null; // 취소 선택시 null리턴
		return selectedFile.getAbsolutePath() + "\\"; // 성공적으로 따오면 해당 경로를 리턴한다.
	}

	public static void audio(String soundName) {
		try {
			URL sound = mainFrame.class.getClassLoader().getResource(soundName+".wav");
			Clip clip = AudioSystem.getClip();
			clip.open(AudioSystem.getAudioInputStream(sound));
			// clip.loop(Clip.LOOP_CONTINUOUSLY);
			clip.start();
		} catch (Exception e) {
		}
	}

	public static void main(String[] args) {
		new mainFrame();
	}

	public void setDark() {
		mb.setBackground(Color.DARK_GRAY);
		fileMenu.setBackground(Color.DARK_GRAY);
		fileMenu.setForeground(Color.white);
		runMenu.setForeground(Color.white);
	}

	public void setWhite() {
		mb.setBackground(color2);
		fileMenu.setBackground(color2);
		fileMenu.setForeground(Color.black);
		runMenu.setForeground(Color.black);
	}
}
