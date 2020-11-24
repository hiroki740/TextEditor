import java.awt.FileDialog;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.MenuShortcut;
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
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

class TextEditor{
	public static void main(String[] args){
		MainWindow mw= new MainWindow();
	}
}

class MainWindow extends Frame implements ActionListener{
	JTextArea textArea= new JTextArea();

	//「ファイル」のメニュー項目
	MenuItem newFileMenuItem = new MenuItem("新規作成");
	MenuItem openFileMenuItem=new MenuItem("開く");
	MenuItem overSaveFileMenuItem=new MenuItem("上書き保存");
	MenuItem saveFileMenuItem=new MenuItem("名前を付けて保存");
	MenuItem closeFileMenuItem=new MenuItem("ファイルを閉じる");
	MenuItem appCloseMenuItem=new MenuItem("テキストエディタを終了");

	//「編集」のメニュー項目
	MenuItem unDoMenuItem=new MenuItem("元に戻す");
	MenuItem cutOutMenuItem =new MenuItem("切り取り");
	MenuItem copyMenuItem=new MenuItem("コピー");
	MenuItem pastingMenuItem=new MenuItem("貼り付け");
	MenuItem deleteMenuItem=new MenuItem("削除");


	String fineName =null;

	MainWindow(){
		setTitle("テキストエディタ");
		setSize(800,600);
		MenuBar meun=new MenuBar();
		Menu menuFile= new Menu("ファイル");
		Menu menuEdit= new Menu("編集");

		//「ファイル」のメニュー項目
		newFileMenuItem = new MenuItem("新規作成");
		openFileMenuItem=new MenuItem("ファイルを開く");
		overSaveFileMenuItem=new MenuItem("上書き保存");
		saveFileMenuItem=new MenuItem("名前を付けて保存");
		appCloseMenuItem=new MenuItem("テキストエディタを終了");

		newFileMenuItem.addActionListener(this);
		openFileMenuItem.addActionListener(this);
		overSaveFileMenuItem.addActionListener(this);
		saveFileMenuItem.addActionListener(this);
		appCloseMenuItem.addActionListener(this);

		newFileMenuItem.setShortcut(new MenuShortcut(KeyEvent.VK_N, false));
		openFileMenuItem.setShortcut(new MenuShortcut(KeyEvent.VK_O, false));
		overSaveFileMenuItem.setShortcut(new MenuShortcut(KeyEvent.VK_S, false));
		saveFileMenuItem.setShortcut(new MenuShortcut(KeyEvent.VK_S, true));

		menuFile.add(newFileMenuItem);
		menuFile.add(openFileMenuItem);
		menuFile.add(overSaveFileMenuItem);
		menuFile.add(saveFileMenuItem);
		menuFile.add(appCloseMenuItem);

		//「編集」のメニュー項目
		unDoMenuItem=new MenuItem("元に戻す");
		cutOutMenuItem =new MenuItem("切り取り");
		copyMenuItem=new MenuItem("コピー");
		pastingMenuItem=new MenuItem("貼り付け");
		deleteMenuItem=new MenuItem("削除");

		unDoMenuItem.addActionListener(this);
		cutOutMenuItem.addActionListener(this);
		copyMenuItem.addActionListener(this);
		pastingMenuItem.addActionListener(this);
		deleteMenuItem.addActionListener(this);

		unDoMenuItem.setShortcut(new MenuShortcut(KeyEvent.VK_Z, false));
		cutOutMenuItem.setShortcut(new MenuShortcut(KeyEvent.VK_X, false));
		copyMenuItem.setShortcut(new MenuShortcut(KeyEvent.VK_C, false));
		pastingMenuItem.setShortcut(new MenuShortcut(KeyEvent.VK_V, false));
		deleteMenuItem.setShortcut(new MenuShortcut(KeyEvent.VK_DELETE));

		menuEdit.add(unDoMenuItem);
		menuEdit.add(cutOutMenuItem);
		menuEdit.add(copyMenuItem);
		menuEdit.add(pastingMenuItem);
		menuEdit.add(deleteMenuItem);

		//メニューバー
		meun.setFont(new Font("MS Gothic", Font.PLAIN, 14));
		meun.add(menuFile);
		meun.add(menuEdit);
		setMenuBar(meun);

		textArea= new JTextArea();
		JScrollPane scrollpane=new JScrollPane(textArea,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		textArea.setFont(new Font("Arial", Font.PLAIN, 21));
		add(textArea);

		setVisible(true);
		addWindowListener(new WinListener());
	}

	public void actionPerformed(ActionEvent e){
		//「ファイル」のメニュー項目
		if(e.getSource()==newFileMenuItem) NewCreateFiile();
		if(e.getSource()==openFileMenuItem) OpenLoadFile();
		if(e.getSource()==overSaveFileMenuItem) OverSaveFile();
		if(e.getSource()==saveFileMenuItem) NewSaveFile();
		if(e.getSource()==appCloseMenuItem) System.exit(0);

		//「編集」のメニュー項目
		if(e.getSource()==unDoMenuItem) textArea.copy();
		if(e.getSource()==cutOutMenuItem) textArea.cut();
		if(e.getSource()==copyMenuItem) textArea.copy();
		if(e.getSource()==pastingMenuItem) textArea.paste();
		if(e.getSource()==deleteMenuItem) NewCreateFiile();
	}

	//新規
	void NewCreateFiile(){
		textArea=new JTextArea();
		NowFilePath=null;
		setTitle("無題-テキストエディタ");
	}

	//開く
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

	//上書き保存
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

	//名前を付けて保存
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

	 //元に戻す
	 void UnDoEdit(){

	 }

	 //切り取り
	 void CutOutEdit(){

	 }

	 //

}

//メニュークラス
class MenuAction{

}

class WinListener extends WindowAdapter{
	public void windowClosing(WindowEvent e){System.exit(0);}
}