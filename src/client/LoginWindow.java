package client;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class LoginWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textName;
	private JTextField textAddress;
	private JTextField textPort;
	
	public LoginWindow() {
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(300, 330);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		textName = new JTextField();
		textName.setBounds(70, 73, 154, 20);
		contentPane.add(textName);
		textName.setColumns(10);
		
		JLabel lblNickname = new JLabel("Nickname :");
		lblNickname.setBounds(121, 48, 70, 14);
		contentPane.add(lblNickname);
		
		textAddress = new JTextField();
		textAddress.setBounds(70, 129, 154, 20);
		contentPane.add(textAddress);
		textAddress.setColumns(10);
		
		JLabel lblIpAddress = new JLabel("IP address :");
		lblIpAddress.setBounds(117, 104, 74, 14);
		contentPane.add(lblIpAddress);
		
		textPort = new JTextField();
		textPort.setBounds(70, 185, 154, 20);
		contentPane.add(textPort);
		textPort.setColumns(10);
		
		JLabel lblPort = new JLabel("Port:");
		lblPort.setBounds(132, 160, 30, 14);
		contentPane.add(lblPort);
		
		JButton btnJoin = new JButton("Join");
		btnJoin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Client user = new Client(textName.getText(), textAddress.getText(), Integer.parseInt(textPort.getText()));
				new ChatWindow();
				dispose();
				System.out.println(user.toString());
			}
		});
		btnJoin.setBounds(102, 247, 89, 23);
		contentPane.add(btnJoin);
	}
	
	
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				
				try {
					LoginWindow frame = new LoginWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
