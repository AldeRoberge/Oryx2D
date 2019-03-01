package oryx2D;

import alde.commons.properties.Property;
import oryx2D.properties.Properties;
import oryx2D.util.hint.HintTextField;
import oryx2D.util.hint.PasswordHintTextField;
import rotmg.account.core.WebAccount;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.awt.*;
import java.util.ArrayList;

public class LoaderUI {

    public Property EMAIL = Properties.EMAIL;
    public Property PASSWORD = Properties.PASSWORD;

    private JFrame frame;
    private JTextField emailField;
    private JTextField passwordField;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    LoaderUI window = new LoaderUI();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     */
    public LoaderUI() {
        this.initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        this.frame = new JFrame();

        ArrayList<Image> icons = new ArrayList<>();
        icons.add(Toolkit.getDefaultToolkit().getImage(LoaderUI.class.getResource("/oryx2D/icons/icon128.png")));
        icons.add(Toolkit.getDefaultToolkit().getImage(LoaderUI.class.getResource("/oryx2D/icons/icon16.png")));
        icons.add(Toolkit.getDefaultToolkit().getImage(LoaderUI.class.getResource("/oryx2D/icons/icon32.png")));
        this.frame.setIconImages(icons);
        this.frame.setResizable(false);
        this.frame.setBackground(Color.black);
        this.frame.setTitle("Oryx2D Launcher");
        this.frame.setLocationRelativeTo(null);

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                | UnsupportedLookAndFeelException e1) {
            e1.printStackTrace();
        }

        //frame.setResizable(false);
        this.frame.setBounds(100, 100, 800, 486);
        this.frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.frame.getContentPane().setLayout(new BorderLayout(0, 0));

        JPanel panel = new JPanel();
        this.frame.getContentPane().add(panel, BorderLayout.NORTH);
        panel.setLayout(new BorderLayout(0, 0));

        JLabel oryxLabel = new JLabel("icon");
        oryxLabel.setIcon(new ImageIcon(LoaderUI.class.getResource("/oryx2D/loader/oryx2D.png")));
        panel.add(oryxLabel, BorderLayout.CENTER);

        JPanel fillerPanel1 = new JPanel();
        fillerPanel1.setBackground(Color.BLACK);
        this.frame.getContentPane().add(fillerPanel1, BorderLayout.CENTER);
        fillerPanel1.setLayout(new BoxLayout(fillerPanel1, BoxLayout.Y_AXIS));

        JPanel emailPanel = new JPanel();
        FlowLayout flowLayout = (FlowLayout) emailPanel.getLayout();
        emailPanel.setBackground(Color.BLACK);
        fillerPanel1.add(emailPanel);

        this.emailField = new HintTextField(this.EMAIL.getValue(), "Email");
        
        
        
        emailField.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void changedUpdate(DocumentEvent arg0) {
				EMAIL.setValue(emailField.getText());
			}

			@Override
			public void insertUpdate(DocumentEvent arg0) {
			}

			@Override
			public void removeUpdate(DocumentEvent arg0) {
			}
        	
        	
        });
        
        emailPanel.add(this.emailField);
        this.emailField.setColumns(20);

        JPanel passwordPanel = new JPanel();
        passwordPanel.setBackground(Color.BLACK);
        fillerPanel1.add(passwordPanel);

        this.passwordField = new PasswordHintTextField(this.PASSWORD.getValue(), "Password");
        this.passwordField.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void changedUpdate(DocumentEvent arg0) {
				EMAIL.setValue(emailField.getText());
			}

			@Override
			public void insertUpdate(DocumentEvent arg0) {
			}

			@Override
			public void removeUpdate(DocumentEvent arg0) {
			}
        	
        	
        });
        
        passwordPanel.add(this.passwordField);
        this.passwordField.setColumns(20);

        JPanel fillerPanel2 = new JPanel();
        fillerPanel2.setBackground(Color.BLACK);
        fillerPanel1.add(fillerPanel2);

        JPanel fillerPanel3 = new JPanel();
        fillerPanel3.setBackground(Color.BLACK);
        fillerPanel1.add(fillerPanel3);

        JPanel fillerPanel4 = new JPanel();
        fillerPanel4.setBackground(Color.BLACK);
        fillerPanel1.add(fillerPanel4);

        JButton btnJoin = new JButton("        Connect        ");
        btnJoin.addActionListener(e -> {
            Game.launch(new WebAccount(this.EMAIL.getValue(), this.PASSWORD.getValue()));
            this.EMAIL.setValue(this.emailField.getText());
            this.PASSWORD.setValue(this.passwordField.getText());
            this.frame.dispose();
        });
        btnJoin.setForeground(Color.BLACK);
        btnJoin.setBackground(Color.WHITE);

        if (Properties.AUTOMATICALLY_CONNECT.getValueAsBoolean()) {
            btnJoin.doClick();
        }
        fillerPanel4.add(btnJoin);

        JPanel fillerPanel5 = new JPanel();
        fillerPanel5.setBackground(Color.BLACK);
        fillerPanel1.add(fillerPanel5);

        JPanel fillerPanel6 = new JPanel();
        fillerPanel6.setBackground(Color.BLACK);
        fillerPanel1.add(fillerPanel6);
    }

}
