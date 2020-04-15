package client;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import java.awt.GridBagLayout;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import java.awt.GridBagConstraints;
import javax.swing.JButton;
import java.awt.Insets;
import javax.swing.JTextField;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JLabel;
import java.awt.Component;

public class ChatWindow extends JFrame implements Runnable{

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textMessage;
	private JTextArea txtrHistory;
	private JTextArea textUserlist;
	private JLabel lblAdvice;
	private JLabel lblUsers;
	private boolean running;
	private Client client;
	
	public ChatWindow(String nickname, String address, int port) {
		
		client = new Client(nickname, address, port);
		
		if(!client.openConnection()) {
			System.err.println("Connection error");
			console("Connection error");
			return;
		}
		client.send("/n/"+client.nickname);
		
		
		
		CreateWin();
		Thread run = new Thread(this,"Client");
		run.start();
	}
	
	private void CreateWin() {
		setResizable(false);
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		setTitle("Chat Client");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(809, 520);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{109, 459 ,150};
		gbl_contentPane.rowHeights = new int[]{25, 430, 30};
		gbl_contentPane.columnWeights = new double[]{1.0, 1.0};
		gbl_contentPane.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		//Labels
		
		lblAdvice = new JLabel("Wash your hands frequently");
		lblAdvice.setFont(new Font("Tahoma", Font.PLAIN, 13));
		GridBagConstraints gbc_lblAdvice = new GridBagConstraints();
		gbc_lblAdvice.gridwidth = 2;
		gbc_lblAdvice.insets = new Insets(0, 0, 5, 5);
		gbc_lblAdvice.gridx = 0;
		gbc_lblAdvice.gridy = 0;
		contentPane.add(lblAdvice, gbc_lblAdvice);
		
		lblUsers = new JLabel("Users");
		lblUsers.setFont(new Font("Tahoma", Font.PLAIN, 14));
		GridBagConstraints gbc_lblUsers = new GridBagConstraints();
		gbc_lblUsers.insets = new Insets(0, 0, 5, 0);
		gbc_lblUsers.gridx = 2;
		gbc_lblUsers.gridy = 0;
		contentPane.add(lblUsers, gbc_lblUsers);
		
		//Text fields
		
		txtrHistory = new JTextArea();
		txtrHistory.setEditable(false);
		JScrollPane history_scroll = new JScrollPane(txtrHistory); 
		txtrHistory.setFont(new Font("Tahoma", Font.PLAIN, 14));
		GridBagConstraints h_scrollConstraints = new GridBagConstraints();
		h_scrollConstraints.gridwidth = 2;
		h_scrollConstraints.fill = GridBagConstraints.BOTH;
		h_scrollConstraints.gridx = 0;
		h_scrollConstraints.gridy = 1;
		h_scrollConstraints.insets = new Insets(0, 2, 5, 5);
		contentPane.add(history_scroll, h_scrollConstraints);
		
		textMessage = new JTextField();
		textMessage.setFont(new Font("Tahoma", Font.PLAIN, 13));
		textMessage.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					send(textMessage.getText());
				}
			}
		});
		
		textUserlist = new JTextArea();
		textUserlist.setEnabled(false);
		JScrollPane userls_scroll = new JScrollPane(textUserlist); 
		GridBagConstraints userls_scrollConstraints = new GridBagConstraints();
		userls_scrollConstraints.insets = new Insets(0, 0, 5, 0);
		userls_scrollConstraints.fill = GridBagConstraints.BOTH;
		userls_scrollConstraints.gridx = 2;
		userls_scrollConstraints.gridy = 1;
		contentPane.add(userls_scroll, userls_scrollConstraints);
		
		//Message field
	
		GridBagConstraints gbc_textMessage = new GridBagConstraints();
		gbc_textMessage.gridwidth = 2;
		gbc_textMessage.insets = new Insets(0, 0, 0, 5);
		gbc_textMessage.fill = GridBagConstraints.HORIZONTAL;
		gbc_textMessage.gridx = 0;
		gbc_textMessage.gridy = 2;
		contentPane.add(textMessage, gbc_textMessage);
		textMessage.setColumns(10);
		
		//Button
		
		JButton btnSend = new JButton("Send");
		btnSend.setAlignmentX(Component.RIGHT_ALIGNMENT);
		btnSend.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				send(textMessage.getText());
			}
		});
		GridBagConstraints gbc_btnSend = new GridBagConstraints();
		gbc_btnSend.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnSend.gridx = 2;
		gbc_btnSend.gridy = 2;
		contentPane.add(btnSend, gbc_btnSend);
		
		setVisible(true);
		textMessage.requestFocusInWindow();
	}
	
	
	public void run() {
		running = true;
		listen();
	}

	
	public void send(String msg) {
		if(msg.equals(""))
			return;
		
		client.send(msg);
		textMessage.setText("");
	}
	
	public void console(String msg) {
		txtrHistory.append(msg + "\n\r");
		txtrHistory.setCaretPosition(txtrHistory.getDocument().getLength());
	}
	
	
	private void listen() {
		Thread listen = new Thread("listen") {
			public void run() {
				while(running) {
					console(client.receive());
				}
			}
		};
		listen.start();
	}
	
}
