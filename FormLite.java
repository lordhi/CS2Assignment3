import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.AbstractAction;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.KeyStroke;
import javax.swing.border.Border;
import javax.swing.BorderFactory;
import javax.swing.ProgressMonitor;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingWorker;
import javax.swing.JSlider;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.Color;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Dimension;

import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;

import java.lang.Math;
import java.lang.Runtime;

public class FormLite
	extends JFrame{

	private JOptionPane pane;
	private JPanel mainPanel, dataEntry, oldNames;
	private JLabel lblname1, lblname2, lblname3, lblname4, lblname5, lblname6, lblid1, lblid2, lblid3, lblid4, lblid5, lblid6, lblstuff;
	private JTextField txfEntry;
	private JButton btnCheckID;
	private AbstractAction enterOrButton, pasteAction, reload, close;

	private JMenuBar menuBar;
	private JMenu menuFile;
	private JMenuItem reloadData, closeApp;

	private SwingWorker saveTime;

	private HashTable ht;

	public static void main(String[] args)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				new FormLite();
			}
		});
	}

	public FormLite()
	{
		ht = HashTableLoader.loadIDs(ht, mainPanel, 50000);


		enterOrButton = new AbstractAction(){
			@Override
			public void actionPerformed(ActionEvent e)
			{
				long t1 = System.nanoTime();
				String ID = txfEntry.getText();

		                if (ID.length() == 13)
        	        	{
					String name = ht.get(ID);
					if (name != null)
                	    addNewID(ID + "  ", name);
					else
						addNewID(ID + " ");
					txfEntry.setText("Enter ID number here.");
				}
				updateBorderColour(-1);
				long t2 = System.nanoTime();
				//saveSearchTime(t2-t1);
			}
		};

		reload = new AbstractAction()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{	
				ht = HashTableLoader.loadIDs(ht, mainPanel, 50000);
			}
		};

		pasteAction = new AbstractAction(){
			@Override
			public void actionPerformed(ActionEvent e)
			{
				try
				{
					String s = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
					String r = s.replaceAll("[^\\d]", "");
					if (s.equals(r) || (JOptionPane.showConfirmDialog(null,"Your clipboard contains illegal characters. Would you like to paste just the numeric characters?", "Warning", JOptionPane.YES_NO_OPTION)) == JOptionPane.YES_OPTION);
					{
						if (!(txfEntry.getSelectedText() == null))
						{
							txfEntry.replaceSelection(r);
						}else{
							String oldText = txfEntry.getText();
							int i = txfEntry.getCaretPosition();
							String tmp = oldText.substring(0,i) + r + oldText.substring(i);;
							txfEntry.setText(tmp);
							txfEntry.setCaretPosition(i + r.length());
						}

						String oldText = txfEntry.getText();
						oldText = oldText.replaceAll("[^\\d]", "");
						txfEntry.setText(oldText);
					}
				}catch(Exception ex){
					System.err.println(ex.getMessage());
				}
			}
		};

		close = new AbstractAction()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				System.exit(0);
			}
		};

		mainPanel = new JPanel(new GridLayout(3,1));

		mainPanel.add(dataEntry());
		mainPanel.add(lastName());
		mainPanel.add(oldNames());

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("ID Checking App");
		setSize(900, 600);

		this.setJMenuBar(menuStuff());

		setContentPane(mainPanel);
		setVisible(true);
	}

	public void addNewID(String ID, String name)
	{
		lblid6.setText(lblid5.getText());
		lblid5.setText(lblid4.getText());
		lblid4.setText(lblid3.getText());
		lblid3.setText(lblid2.getText());
		lblid2.setText(lblid1.getText());
		lblid1.setText(ID);

		lblname6.setText(lblname5.getText());
		lblname5.setText(lblname4.getText());
		lblname4.setText(lblname3.getText());
		lblname3.setText(lblname2.getText());
		lblname2.setText(lblname1.getText());
		lblname1.setText(name);
	}

	public void addNewID(String ID)
	{
		addNewID(ID, "ID not found.");
	}

	private JPanel lastName()
	{
		Font vd = new Font("Verdana", 2, 30);
		JPanel pn = new JPanel(new GridLayout(2,2));

		JLabel ID = new JLabel("ID:");
		JLabel Name = new JLabel("Name:");

		lblid1 = new JLabel("");
		lblname1 = new JLabel("");

		ID.setHorizontalAlignment(JLabel.CENTER);
		Name.setHorizontalAlignment(JLabel.CENTER);
		lblid1.setHorizontalAlignment(JLabel.RIGHT);
		lblname1.setHorizontalAlignment(JLabel.LEFT);

		ID.setFont(vd);
		Name.setFont(vd);
		lblid1.setFont(vd);
		lblname1.setFont(vd);

		pn.add(ID);
		pn.add(Name);
		pn.add(lblid1);
		pn.add(lblname1);

		return pn;
	}

	private JPanel oldNames()
	{
		JPanel pn = new JPanel(new GridLayout(5,2));
		Font vd = new Font("Verdana", 2, 30);

		lblid2 = new JLabel("");
		lblid3 = new JLabel("");
		lblid4 = new JLabel("");
		lblid5 = new JLabel("");
		lblid6 = new JLabel("");

		lblname2 = new JLabel("");
		lblname3 = new JLabel("");
		lblname4 = new JLabel("");
		lblname5 = new JLabel("");
		lblname6= new JLabel("");

		lblname2.setHorizontalAlignment(JLabel.LEFT);
		lblname3.setHorizontalAlignment(JLabel.LEFT);
		lblname4.setHorizontalAlignment(JLabel.LEFT);
		lblname5.setHorizontalAlignment(JLabel.LEFT);
		lblname6.setHorizontalAlignment(JLabel.LEFT);

		lblid2.setHorizontalAlignment(JLabel.RIGHT);
		lblid3.setHorizontalAlignment(JLabel.RIGHT);
		lblid4.setHorizontalAlignment(JLabel.RIGHT);
		lblid5.setHorizontalAlignment(JLabel.RIGHT);
		lblid6.setHorizontalAlignment(JLabel.RIGHT);

		lblname2.setFont(vd);
		lblname3.setFont(vd);
		lblname4.setFont(vd);
		lblname5.setFont(vd);
		lblname6.setFont(vd);

		lblid2.setFont(vd);
		lblid3.setFont(vd);
		lblid4.setFont(vd);
		lblid5.setFont(vd);
		lblid6.setFont(vd);

		pn.add(lblid2);
		pn.add(lblname2);

		pn.add(lblid3);
		pn.add(lblname3);

		pn.add(lblid4);
		pn.add(lblname4);

		pn.add(lblid5);
		pn.add(lblname5);

		pn.add(lblid6);
		pn.add(lblname6);

		return pn;
	}

	private JMenuBar menuStuff()
	{
		Font mF = new Font("Verdana", 2, 20);
		Font iF = new Font("Verdana", 2, 16);

		menuBar = new JMenuBar();

		menuFile = new JMenu("File");
		menuFile.setFont(mF);

		reloadData = new JMenuItem("Reload the data file");
		reloadData.setFont(iF);
		reloadData.addActionListener(reload);
		closeApp = new JMenuItem("Close");
		closeApp.setFont(iF);
		closeApp.addActionListener(close);
		menuFile.add(reloadData);
		menuFile.add(closeApp);
		

		menuBar.add(menuFile);

		return menuBar;
	}

	private JPanel dataEntry()
	{
		JPanel pnl = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 30));
		Font vd = new Font("Verdana", 2, 30);

		txfEntry = new JTextField("Enter ID number here.", 20);
		txfEntry.setPreferredSize(new Dimension(400, 60));
		txfEntry.setFont(vd);
		txfEntry.addKeyListener(new KeyAdapter(){
			public void keyTyped(KeyEvent e){
				char typed = e.getKeyChar();
				String s = txfEntry.getText();
				if ((typed < '0' || typed > '9'))
				{
					e.consume();
				}else{
					if (s.equals("Enter ID number here."))
					{
						s = typed + "";
						txfEntry.setText(s);
						e.consume();
					}

					updateBorderColour(s.length());
				}
			}
			public void keyPressed(KeyEvent e)
			{
				if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE)
				{
					String s = txfEntry.getText();

					if (s.length() == 1)
					{
						txfEntry.setText("Enter ID number here.");
						updateBorderColour(-1);
						e.consume();
					}else{
						s = s.replaceAll("[^\\d]", "");
						txfEntry.setText(s);
						updateBorderColour(s.length());
					}
				}
			}
		});
		txfEntry.addActionListener(enterOrButton);
		txfEntry.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_MASK), "pressed");
		txfEntry.getActionMap().put("pressed", pasteAction);

		btnCheckID = new JButton("Check ID");
		btnCheckID.setPreferredSize(new Dimension(200, 60));
		btnCheckID.addActionListener(enterOrButton);

		lblstuff = new JLabel();

		txfEntry.setFont(vd);
		btnCheckID.setFont(vd);
		lblstuff.setFont(vd);

		pnl.add(txfEntry);
		pnl.add(btnCheckID);
		pnl.add(lblstuff);

		return pnl;
	}

	private void updateBorderColour(int length)
	{
		Border border;
		if (length == -1)
			border = BorderFactory.createLineBorder(Color.GRAY, 0);
		else if(length == 12)
			border = BorderFactory.createLineBorder(Color.GREEN, 2);
		else
			border = BorderFactory.createLineBorder(Color.RED, 2);
		txfEntry.setBorder(border);
	}

	private void saveSearchTime(long t)
	{
		if (saveTime != null)
			saveTime.cancel(true);
		else
		{
			try
			{
				BufferedWriter wr = new BufferedWriter(new FileWriter("Report/Data/SearchTimes.csv"));
				wr.close();
			}catch(Exception e)
			{
				System.err.println(e.getMessage());
				System.exit(0);
			}
		}

		saveTime = new SwingWorker<String, Integer>()
		{
			@Override
			public String doInBackground()
			{
				try
				{
					BufferedWriter wr = new BufferedWriter(new FileWriter("Report/Data/SearchTimes.csv", true));
					wr.write(System.currentTimeMillis() + ", " + t + "\r\n");
					wr.close();
				}catch(Exception e){
					System.err.println(e.getMessage());
					System.exit(0);
				}
				return null;
			}
		};

		saveTime.execute();
	}
}