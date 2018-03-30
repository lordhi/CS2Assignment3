import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextField;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Dimension;

public class Form
	extends JFrame{

	private static JPanel mainPanel, dataEntry, oldNames;
	private static JLabel lblname1, lblname2, lblname3, lblname4, lblname5, lblname6, lblid1, lblid2, lblid3, lblid4, lblid5, lblid6;
	private static JTextField txfEntry;
	private static JButton btnCheckID;

	public static void main(String[] args)
	{
		new Form();
	}

	public Form()
	{
		mainPanel = new JPanel(new GridLayout(3,1));

		mainPanel.add(dataEntry());
		mainPanel.add(lastName());
		mainPanel.add(oldNames());

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("ID Checking App");
		setSize(900, 600);

		setContentPane(mainPanel);
		setVisible(true);
	}

	public static void addNewID(String ID, String name)
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

	public static void addNewID(String ID)
	{
		addNewID(ID, "ID not found.");
	}

	private static JPanel lastName()
	{
		Font vd = new Font("Verdana", 2, 40);
		JPanel pn = new JPanel(new GridLayout(2,2));

		JLabel ID = new JLabel("ID:");
		JLabel Name = new JLabel("Name:");

		lblid1 = new JLabel("id");
		lblname1 = new JLabel("name");

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

	private static JPanel oldNames()
	{
		JPanel pn = new JPanel(new GridLayout(5,2));
		Font vd = new Font("Verdana", 2, 30);

		lblid2 = new JLabel("id1");
		lblid3 = new JLabel("id2");
		lblid4 = new JLabel("id3");
		lblid5 = new JLabel("id4");
		lblid6 = new JLabel("id5");

		lblname2 = new JLabel("1");
		lblname3 = new JLabel("2");
		lblname4 = new JLabel("3");
		lblname5 = new JLabel("4");
		lblname6= new JLabel("5");

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

	private static JPanel dataEntry()
	{
		JPanel pnl = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 30));
		Font vd = new Font("Verdana", 2, 30);

		txfEntry = new JTextField("Enter ID number here.", 20);
		txfEntry.setPreferredSize(new Dimension(400, 60));
		txfEntry.setFont(vd);

		btnCheckID = new JButton("Check ID");
		btnCheckID.setPreferredSize(new Dimension(200, 60));
		txfEntry.setFont(vd);

		pnl.add(txfEntry);
		pnl.add(btnCheckID);

		return pnl;
	}
}
