
package porschechatbot;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;

public class PorscheChatbotGUI extends JFrame {

    private JTextPane chatPane;
    private JTextField userInputField;
    private JButton sendButton;
    private StyledDocument doc;
    private Style userStyle, botStyle;

    // Conversation states
    private enum State {
        DEFAULT,
        AWAITING_MODEL_SELECTION,
        AWAITING_ACTION_SELECTION
    }

    private State currentState = State.DEFAULT;
    private CarInfo selectedCar = null;

    private static class CarInfo {
        String name;
        boolean convertible;
        int priceBeforeVAT;
        int priceAfterVAT;
        String description;

        CarInfo(String name, boolean convertible, int priceBeforeVAT, String description) {
            this.name = name;
            this.convertible = convertible;
            this.priceBeforeVAT = priceBeforeVAT;
            this.priceAfterVAT = (int) Math.round(priceBeforeVAT * 1.15);
            this.description = description;
        }
    }

    private final CarInfo[] cars = new CarInfo[] {
            new CarInfo("Porsche 911", true, 575000,
                    "The Porsche 911 is a timeless sports car with up to 640 hp. Carrera, Turbo, and GT3 available."),
            new CarInfo("Porsche Taycan", false, 460000,
                    "The Taycan is a high-performance electric sedan with up to 750 hp and 0–100 km/h in 2.8s."),
            new CarInfo("Porsche Cayenne", false, 355000,
                    "The Cayenne is a luxury SUV with powerful engines and sporty handling. Hybrid available."),
            new CarInfo("Porsche Macan", false, 270000,
                    "The Macan is a compact SUV with Porsche DNA—fast, stylish, and practical."),
            new CarInfo("Porsche Panamera", false, 430000,
                    "The Panamera is a luxury performance sedan with a thrilling drive and four doors."),
    };

    public PorscheChatbotGUI() {
        setTitle("Porsche Jeddah Chatbot");
        setSize(600, 700);
        setMinimumSize(new Dimension(500, 600));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(Color.decode("#F5F5F5"));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        setContentPane(mainPanel);

        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        header.setOpaque(false);
        JLabel logo = new JLabel("\uD83D\uDE97"); // Car emoji placeholder
        logo.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        header.add(logo);
        JLabel title = new JLabel("Porsche Jeddah Chatbot");
        title.setForeground(Color.decode("#222222"));
        title.setFont(new Font("Segoe UI Semibold", Font.BOLD, 28));
        header.add(title);
        mainPanel.add(header, BorderLayout.NORTH);

        chatPane = new JTextPane();
        chatPane.setEditable(false);
        chatPane.setBackground(Color.decode("#FFFFFF"));
        chatPane.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        chatPane.setMargin(new Insets(10, 10, 10, 10));
        doc = chatPane.getStyledDocument();

        userStyle = chatPane.addStyle("UserStyle", null);
        StyleConstants.setForeground(userStyle, Color.decode("#222222"));
        StyleConstants.setBackground(userStyle, Color.decode("#E0E0E0"));
        StyleConstants.setAlignment(userStyle, StyleConstants.ALIGN_RIGHT);
        StyleConstants.setFontSize(userStyle, 16);
        StyleConstants.setFontFamily(userStyle, "Segoe UI");
        StyleConstants.setSpaceAbove(userStyle, 10);
        StyleConstants.setSpaceBelow(userStyle, 10);
        StyleConstants.setLeftIndent(userStyle, 40);
        StyleConstants.setRightIndent(userStyle, 10);
        StyleConstants.setLineSpacing(userStyle, 0.2f);

        botStyle = chatPane.addStyle("BotStyle", null);
        StyleConstants.setForeground(botStyle, Color.decode("#B4121F")); // Red text color
        StyleConstants.setBackground(botStyle, Color.WHITE);              // White background, no highlight
        StyleConstants.setAlignment(botStyle, StyleConstants.ALIGN_LEFT);
        StyleConstants.setFontSize(botStyle, 16);
        StyleConstants.setFontFamily(botStyle, "Segoe UI");
        StyleConstants.setSpaceAbove(botStyle, 10);
        StyleConstants.setSpaceBelow(botStyle, 10);
        StyleConstants.setLeftIndent(botStyle, 10);
        StyleConstants.setRightIndent(botStyle, 40);
        StyleConstants.setLineSpacing(botStyle, 0.2f);

        JScrollPane scrollPane = new JScrollPane(chatPane);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setBackground(Color.decode("#F5F5F5"));
        scrollPane.getVerticalScrollBar().setUnitIncrement(20);

        JScrollBar verticalBar = scrollPane.getVerticalScrollBar();
        verticalBar.setUI(new BasicScrollBarUI() {
            private final Dimension d = new Dimension();

            @Override
            protected JButton createDecreaseButton(int orientation) {
                return createZeroButton();
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                return createZeroButton();
            }

            private JButton createZeroButton() {
                JButton jbutton = new JButton();
                jbutton.setPreferredSize(new Dimension(0, 0));
                jbutton.setMinimumSize(new Dimension(0, 0));
                jbutton.setMaximumSize(new Dimension(0, 0));
                return jbutton;
            }

            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = Color.decode("#B4121F");
                this.trackColor = Color.decode("#F5F5F5");
            }
        });

        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new BorderLayout(10, 10));
        inputPanel.setOpaque(false);

        userInputField = new JTextField();
        userInputField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        userInputField.setBackground(Color.WHITE);
        userInputField.setForeground(Color.decode("#222222"));
        userInputField.setCaretColor(Color.decode("#B4121F"));
        userInputField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.decode("#B4121F"), 2, true),
                BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        userInputField.addActionListener(e -> sendMessage());

        sendButton = new JButton("Send");
        sendButton.setFont(new Font("Segoe UI Semibold", Font.BOLD, 16));
        sendButton.setBackground(Color.decode("#B4121F"));
        sendButton.setForeground(Color.WHITE);
        sendButton.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        sendButton.setFocusPainted(false);
        sendButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        sendButton.addActionListener(e -> sendMessage());

        sendButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                sendButton.setBackground(Color.decode("#D9313F"));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                sendButton.setBackground(Color.decode("#B4121F"));
            }
        });

        inputPanel.add(userInputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        mainPanel.add(inputPanel, BorderLayout.SOUTH);

        appendBotMessage("Welcome to Porsche Jeddah! Ask me about our cars, test drives, or services.");

        setVisible(true);
    }

    private void sendMessage() {
        String userText = userInputField.getText().trim();
        if (userText.isEmpty()) return;

        appendUserMessage(userText);
        userInputField.setText("");

        String botResponse = processInput(userText);
        Timer timer = new Timer(300, e -> appendBotMessage(botResponse));
        timer.setRepeats(false);
        timer.start();
    }

    private void appendUserMessage(String message) {
        try {
            doc.insertString(doc.getLength(), message + "\n", userStyle);
            doc.setParagraphAttributes(doc.getLength(), 1, userStyle, false);
            chatPane.setCaretPosition(doc.getLength());
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    private void appendBotMessage(String message) {
        try {
            doc.insertString(doc.getLength(), message + "\n\n", botStyle);
            doc.setParagraphAttributes(doc.getLength(), 1, botStyle, false);
            chatPane.setCaretPosition(doc.getLength());
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    private String processInput(String input) {
        input = input.toLowerCase();

        switch (currentState) {
            case DEFAULT:
                if (input.contains("what car models") || input.contains("models") || input.contains("cars")) {
                    currentState = State.AWAITING_MODEL_SELECTION;
                    return listAllModels();
                }
                // If user names a model in default state, reply with info immediately
                for (CarInfo car : cars) {
                    if (input.contains(car.name.toLowerCase())) {
                        selectedCar = car;
                        currentState = State.AWAITING_ACTION_SELECTION;
                        return carInfoMessage(car) + "\nWould you like to 'buy' or 'test drive' this model?";
                    }
                }
                if (input.contains("buy")) {
                    return "Please first select a car model by typing its name.";
                }
                if (input.contains("test drive") || input.contains("drive")) {
                    return "Please specify which car model you want to test drive.";
                }
                if (input.contains("hello") || input.contains("hi")) {
                    return "Hello! How can I assist you today? 🚘";
                }
                if (input.contains("service") || input.contains("maintenance")) {
                    return "Porsche Jeddah offers certified service and maintenance. Schedule your appointment online or by phone.";
                }
                if (input.contains("price") || input.contains("cost")) {
                    return "Please specify which model you want the price for.";
                }
                if (input.contains("bye") || input.contains("exit")) {
                    return "Thank you for chatting with Porsche Jeddah! We look forward to welcoming you soon. 🚗";
                }
                return "I’m here to assist you with Porsche models, test drives, and services. Could you please rephrase your question?";

            case AWAITING_MODEL_SELECTION:
                // User should select a model now
                for (CarInfo car : cars) {
                    if (input.contains(car.name.toLowerCase())) {
                        selectedCar = car;
                        currentState = State.AWAITING_ACTION_SELECTION;
                        return carInfoMessage(car) + "\nWould you like to 'buy' or 'test drive' this model?";
                    }
                }
                return "Sorry, I didn't recognize that model. Please choose one of these models:\n" + listAllModels();

            case AWAITING_ACTION_SELECTION:
                if (input.contains("buy")) {
                    currentState = State.DEFAULT;
                    String carName = selectedCar != null ? selectedCar.name : "";
                    selectedCar = null;
                    return "Fantastic choice! Our sales team will contact you shortly regarding your purchase of the " + carName + ".";
                } else if (input.contains("test drive") || input.contains("drive")) {
                    currentState = State.DEFAULT;
                    String carName = selectedCar != null ? selectedCar.name : "";
                    selectedCar = null;
                    return "Great! We will help you schedule a test drive for the " + carName + ". Please visit our website or call Porsche Jeddah at 9200-XXX.";
                } else {
                    return "Please reply with 'buy' or 'test drive' to proceed, or type 'models' to see the car list again.";
                }

            default:
                currentState = State.DEFAULT;
                return "Let's start fresh. How can I assist you with Porsche models, test drives, or services?";
        }
    }

    private String listAllModels() {
        StringBuilder sb = new StringBuilder("Here are the Porsche models we offer:\n");
        for (CarInfo car : cars) {
            sb.append("- ").append(car.name);
            sb.append(car.convertible ? " (Convertible available)" : "");
            sb.append("\n");
        }
        sb.append("\nPlease type the model name to get more information.");
        return sb.toString();
    }

    private String carInfoMessage(CarInfo car) {
        return String.format("%s\n%s\nPrice before VAT: SAR %,d\nPrice after 15%% VAT: SAR %,d",
                car.name, car.description, car.priceBeforeVAT, car.priceAfterVAT);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(PorscheChatbotGUI::new);
    }
}
