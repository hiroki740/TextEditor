import java.awt.FileDialog;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.MenuShortcut;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

class TextEditor{
	public static void main(String[] args){
		MainWindow mw= new MainWindow();
	}
}

class MainWindow extends Frame implements ActionListener{
	TextArea textArea= new TextArea();

	MenuItem newFile = new MenuItem("新規作成");
	MenuItem openFile=new MenuItem("開く");
	MenuItem overSaveFile=new MenuItem("上書き保存");
	MenuItem saveFile=new MenuItem("名前を付けて保存");
	MenuItem closeFile=new MenuItem("ファイルを閉じる");
	MenuItem appClose=new MenuItem("テキストエディタを終了");

	String fineName =null;

	MainWindow(){
		setTitle("テキストエディタ");
		setSize(800,600);
		MenuBar meun=new MenuBar();
		Menu menuFile= new Menu("ファイル");
		Menu menuEdit= new Menu("編集");

		newFile = new MenuItem("新規作成");
		openFile=new MenuItem("ファイルを開く");
		overSaveFile=new MenuItem("上書き保存");
		saveFile=new MenuItem("名前を付けて保存");
		appClose=new MenuItem("テキストエディタを終了");

		newFile.addActionListener(this);
		openFile.addActionListener(this);
		overSaveFile.addActionListener(this);
		saveFile.addActionListener(this);
		appClose.addActionListener(this);

		menuFile.add(newFile);
		menuFile.add(openFile);
		menuFile.add(overSaveFile);
		menuFile.add(saveFile);
		menuFile.add(appClose);

		newFile.setShortcut(new MenuShortcut(KeyEvent.VK_N, false));
		openFile.setShortcut(new MenuShortcut(KeyEvent.VK_O, false));
		overSaveFile.setShortcut(new MenuShortcut(KeyEvent.VK_S, false));
		saveFile.setShortcut(new MenuShortcut(KeyEvent.VK_S, true));

		meun.setFont(new Font("MS Gothic", Font.PLAIN, 14));
		meun.add(menuFile);
		meun.add(menuEdit);
		setMenuBar(meun);

		textArea= new TextArea();
		textArea.setFont(new Font("Arial", Font.PLAIN, 21));
		add(textArea);

		setVisible(true);
		addWindowListener(new WinListener());
	}

	public void actionPerformed(ActionEvent e){
		if(e.getSource()==newFile) NewCreateFiile();
		if(e.getSource()==openFile) OpenLoadFile();
		if(e.getSource()==overSaveFile) OverSaveFile();
		if(e.getSource()==saveFile) NewSaveFile();
		if(e.getSource()==appClose) System.exit(0);
	}

	void NewCreateFiile(){
		textArea.setText("");
		NowFilePath=null;
		setTitle("無題-テキストエディタ");
	}

	void OpenLoadFile(){
		FileDialog fileDialog= new FileDialog(this,"ファイルを開く",FileDialog.LOAD);
		fileDialog.setVisible(true);
		String targetPath = fileDialog.getDirectory();
		String targetFile = fileDialog.getFile();
		NowFilePath=targetPath+targetFile;
		if (targetFile != null){
			String newLine =System.getProperty("line.separator");
			FileReader fileRead = null;
			BufferedReader bufferedRead=null;
			try{
				fileRead=new FileReader(NowFilePath);
				bufferedRead= new BufferedReader(fileRead);
				textArea.setText("");
				while(true){
					String targetText=bufferedRead.readLine();
					if(targetText==null) break;
					textArea.setText(textArea.getText() + targetText + newLine);
				 }
				 bufferedRead.close();
				 fileRead.close();
				 setTitle(targetFile+"-テキストエディタ");
			 }catch(IOException e){
				 JLabel label = new JLabel("ファイルを読み込めません。");
				 JOptionPane.showMessageDialog(this, label,"読み込みエラー",JOptionPane.ERROR_MESSAGE);
			 }
		 }
	 }

	String NowFilePath =null;
	void OverSaveFile(){
		try{
			if(NowFilePath==null){
				NewSaveFile();
			}else{
				FileWriter fileWriter=new FileWriter(NowFilePath);
				fileWriter.write(textArea.getText());
				fileWriter.close();
			}
		}catch(IOException e){
			JLabel label = new JLabel("ファイルが書き込めません。");
			JOptionPane.showMessageDialog(this, label,"書き込みエラー",JOptionPane.ERROR_MESSAGE);
		}
	}

	 void NewSaveFile(){
		 FileDialog fileDialog= new FileDialog(this,"名前を付けて保存",FileDialog.SAVE);
		 fileDialog.setVisible(true);
		 String targetPath =fileDialog.getDirectory();
		 String targetFile=fileDialog.getFile();
		 NowFilePath=targetPath+targetFile;
		 if(targetFile != null){
			 try{
				 FileWriter fileWriter=new FileWriter(NowFilePath);
				 fileWriter.write(textArea.getText());
				 fileWriter.close();
				 setTitle(targetFile+"-テキストエディタ");
			 }catch(IOException e){
				 JLabel label = new JLabel("ファイルが書き込めません。");
				 JOptionPane.showMessageDialog(this, label,"書き込みエラー",JOptionPane.ERROR_MESSAGE);
			 }
		 }
	 }
}

class WinListener extends WindowAdapter{
	public void windowClosing(WindowEvent e){System.exit(0);}
}