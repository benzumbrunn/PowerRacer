/**
 * 
 */
package server.lobby;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * Small GUI for server host to monitor activity and kick players.
 * 
 * @author Florian
 *
 */
public class ServerGUI {

	private static ServerGUI serverGUI;

	private JFrame frame;
	private JTextArea serverChatBox;

	/**
	 * Constructor which sets up ServerGUI. Sets JTextField listener to send
	 * server broadcasts and, with the command \k, kicks players.
	 */
	public ServerGUI() {
		frame = new JFrame("Server Console");
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setResizable(false);
		serverChatBox = new JTextArea();
		serverChatBox.setEditable(false);
		serverChatBox.setLineWrap(true);

		JScrollPane scrollPane = new JScrollPane(serverChatBox,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setPreferredSize(new Dimension(800, 400));

		JTextField serverConsole = new JTextField();
		serverConsole.setMinimumSize(new Dimension(300, 30));
		serverConsole.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (serverConsole.getText().length() != 0) {
					String text = serverConsole.getText();
					if (text.split(":")[0].equals("\\k")) {
						String[] parts = text.split(":");
						if (text.split(":").length == 3) {
							PlayerManager.getPlayerWithName(parts[1])
									.increaseKickCounter(10, parts[2]);
						} else {
							ServerGUI
									.addToConsole("Not the right number of \":\"\n\\k:name:reason");
						}
					} else {
						ChatLogic.serverBroadcast(text);
					}
					serverConsole.setText("");
				}
			}
		});

		JPanel panel = new JPanel();
		// panel.add(scrollPane);
		// panel.add(serverConsole);
		GroupLayout layout = new GroupLayout(panel);
		layout.setHorizontalGroup(layout.createParallelGroup()
				.addComponent(scrollPane).addComponent(serverConsole));
		layout.setVerticalGroup(layout.createSequentialGroup()
				.addComponent(scrollPane).addComponent(serverConsole));

		panel.setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		frame.getContentPane().add(panel);

		frame.pack();

		frame.setLocation(
				Toolkit.getDefaultToolkit().getScreenSize().width
						- frame.getWidth(),
				Toolkit.getDefaultToolkit().getScreenSize().height
						- frame.getHeight());
		frame.setVisible(true);
		serverGUI = this;
	}

	/**
	 * Adds the argument message to the JTextArea chatBox and scrolls its Focus
	 * down to the new message.
	 * 
	 * @param message
	 *            text to be added to chat
	 */
	public static void addToConsole(String message) {
		if (serverGUI != null) {
			serverGUI.serverChatBox.append(message + "\n");
			serverGUI.serverChatBox.setCaretPosition(serverGUI.serverChatBox
					.getDocument().getLength());
		}
	}
}
