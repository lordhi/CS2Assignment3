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
	private static JLabel lblname1, lblname2, lblname3, lblname4, lblname5;
	private static JTextField txfEntry;
	private static JButton btnCheckID;

	public Form()
	{
		mainPanel = new JPanel(new GridLayout(2,1));
		dataEntry = dataEntry();
		oldNames = new JPanel(new GridLayout(0, 1));

		oldNames.add(new JLabel("Name"));
		oldNames.add(new JLabel("Name1"));
		oldNames.add(new JLabel("Name2"));
		oldNames.add(new JLabel("Name3"));

		mainPanel.add(dataEntry);
		mainPanel.add(oldNames);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("ID Checking App");
		setSize(800, 400);

		setContentPane(mainPanel);
		setVisible(true);
	}

	private static JPanel dataEntry()
	{
		JPanel pnl = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
		Font vd = new Font("Verdana", 2, 30);

		txfEntry = new JTextField("Enter ID number here.", 20);
		txfEntry.setPreferredSize(new Dimension(100, 60));
		txfEntry.setFont(vd);

		btnCheckID = new JButton("Check ID");
		btnCheckID.setPreferredSize(new Dimension(100, 60));
		txfEntry.setFont(vd);

		pnl.add(txfEntry);
		pnl.add(btnCheckID);

		return pnl;
	}
}
