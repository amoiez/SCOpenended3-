// Import statements for Swing GUI components
import javax.swing.*;           // Provides JFrame, JPanel, JButton, JLabel, JTextArea, JScrollPane, BorderFactory
import javax.swing.border.*;    // Provides border styling classes like EmptyBorder, TitledBorder
import javax.swing.plaf.basic.BasicButtonUI;

// Import statements for AWT (Abstract Window Toolkit) components
import java.awt.*;              // Provides BorderLayout, GridLayout, FlowLayout, Color, Font, Dimension
import java.awt.event.*;        // Provides ActionListener interface and ActionEvent class for event handling
import java.awt.geom.*;         // Provides RoundRectangle2D for rounded corners

/**
 * TicketMachine - A Java Swing application simulating a City Metro Ticket Kiosk
 * 
 * This application demonstrates:
 * - GUI design using Swing/AWT components
 * - Layout management (BorderLayout, GridLayout, FlowLayout)
 * - Event handling using ActionListener
 * - Component hierarchy: JFrame -> JPanel -> Components
 * 
 * @author Software Construction Lab
 * @version 1.0
 */
public class TicketMachine extends JFrame implements ActionListener {
    
    // ==================== Custom Colors for Modern UI ====================
    private static final Color PRIMARY_BLUE = new Color(41, 128, 185);
    private static final Color DARK_BLUE = new Color(30, 60, 114);
    private static final Color ACCENT_GREEN = new Color(39, 174, 96);
    private static final Color ACCENT_RED = new Color(231, 76, 60);
    private static final Color ACCENT_ORANGE = new Color(243, 156, 18);
    private static final Color ACCENT_PURPLE = new Color(142, 68, 173);
    private static final Color BACKGROUND_DARK = new Color(44, 62, 80);
    private static final Color BACKGROUND_LIGHT = new Color(236, 240, 241);
    private static final Color CARD_BACKGROUND = new Color(255, 255, 255);
    private static final Color TEXT_DARK = new Color(44, 62, 80);
    private static final Color DISPLAY_BG = new Color(20, 30, 48);
    private static final Color DISPLAY_TEXT = new Color(0, 230, 118);
    
    // ==================== Instance Variables ====================
    
    // Display component - shows messages, balance, and ticket info
    private JTextArea displayArea;
    
    // Destination/Ticket selection buttons
    private JButton btnStationA;
    private JButton btnStationB;
    private JButton btnStationC;
    private JButton btnStationD;
    
    // Control buttons for money insertion
    private JButton btnInsert5;
    private JButton btnInsert10;
    private JButton btnInsert20;
    
    // Action buttons
    private JButton btnPrintTicket;
    private JButton btnClear;
    
    // Variables to track the current state
    private double currentBalance;      // Amount of money inserted by user
    private double selectedTicketPrice; // Price of the currently selected ticket
    private String selectedDestination; // Name of the selected destination
    private boolean ticketSelected;     // Flag to check if a ticket has been selected
    
    // Constants for ticket prices
    private static final double PRICE_STATION_A = 10.00;
    private static final double PRICE_STATION_B = 20.00;
    private static final double PRICE_STATION_C = 30.00;
    private static final double PRICE_STATION_D = 50.00;
    
    // ==================== Constructor ====================
    
    /**
     * Constructor - Initializes the Ticket Machine GUI
     * Sets up the main frame, panels, and all components
     */
    public TicketMachine() {
        // Set modern look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Initialize state variables
        currentBalance = 0.0;
        selectedTicketPrice = 0.0;
        selectedDestination = "";
        ticketSelected = false;
        
        // Configure the main JFrame
        setTitle("RideVibe - Metro Tickets");
        setSize(600, 820);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window on screen
        setResizable(true);
        setMinimumSize(new Dimension(550, 780));
        
        // Set BorderLayout as the main layout manager for the frame
        // BorderLayout divides the container into 5 regions: NORTH, SOUTH, EAST, WEST, CENTER
        setLayout(new BorderLayout(0, 0));
        
        // Create main container with gradient background
        JPanel mainContainer = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gradient = new GradientPaint(0, 0, DARK_BLUE, 0, getHeight(), new Color(20, 30, 48));
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainContainer.setLayout(new BorderLayout(10, 10));
        mainContainer.setBorder(new EmptyBorder(10, 15, 10, 15));
        
        // Create and add all panels to the main container
        mainContainer.add(createHeaderPanel(), BorderLayout.NORTH);
        mainContainer.add(createCenterPanel(), BorderLayout.CENTER);
        mainContainer.add(createControlPanel(), BorderLayout.SOUTH);
        
        add(mainContainer);
        
        // Update display with initial welcome message
        updateDisplay("Welcome to RideVibe! \u270C\uFE0F\n\nPick your destination and let's go!");
    }
    
    // ==================== Panel Creation Methods ====================
    
    /**
     * Creates the header panel containing the title
     * @return JPanel with the header/title label
     */
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Create rounded rectangle background with gradient
                GradientPaint gradient = new GradientPaint(0, 0, new Color(99, 102, 241), getWidth(), 0, new Color(168, 85, 247));
                g2d.setPaint(gradient);
                g2d.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 20, 20));
            }
        };
        headerPanel.setOpaque(false);
        headerPanel.setLayout(new GridBagLayout());
        headerPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        headerPanel.setPreferredSize(new Dimension(550, 80));
        
        // Create centered content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setOpaque(false);
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        
        // Title with emoji
        JLabel titleLabel = new JLabel("🚀 RideVibe");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Subtitle
        JLabel subtitleLabel = new JLabel("tap. ride. vibe. ✨");
        subtitleLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        subtitleLabel.setForeground(new Color(255, 255, 255, 220));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createVerticalStrut(5));
        contentPanel.add(subtitleLabel);
        
        headerPanel.add(contentPanel);
        return headerPanel;
    }
    
    /**
     * Creates the center panel containing the display and ticket selection buttons
     * Uses BoxLayout to stack components vertically
     * @return JPanel with display and ticket selection components
     */
    private JPanel createCenterPanel() {
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);
        
        // Add display panel at the top
        JPanel displayWrapper = createDisplayPanel();
        displayWrapper.setAlignmentX(Component.LEFT_ALIGNMENT);
        centerPanel.add(displayWrapper);
        
        centerPanel.add(Box.createVerticalStrut(8));
        
        // Add ticket selection panel in the middle
        JPanel ticketWrapper = createTicketSelectionPanel();
        ticketWrapper.setAlignmentX(Component.LEFT_ALIGNMENT);
        centerPanel.add(ticketWrapper);
        
        centerPanel.add(Box.createVerticalStrut(8));
        
        // Add money insertion panel at the bottom
        JPanel moneyWrapper = createMoneyPanel();
        moneyWrapper.setAlignmentX(Component.LEFT_ALIGNMENT);
        centerPanel.add(moneyWrapper);
        
        return centerPanel;
    }
    
    /**
     * Creates the display panel with a JTextArea acting as the kiosk screen
     * @return JPanel containing the display area
     */
    private JPanel createDisplayPanel() {
        JPanel displayPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(30, 40, 55));
                g2d.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 20, 20));
                
                // Add subtle inner glow effect
                g2d.setColor(new Color(0, 200, 100, 30));
                g2d.setStroke(new BasicStroke(3));
                g2d.draw(new RoundRectangle2D.Double(2, 2, getWidth()-4, getHeight()-4, 18, 18));
            }
        };
        displayPanel.setOpaque(false);
        displayPanel.setLayout(new BorderLayout());
        displayPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        displayPanel.setPreferredSize(new Dimension(560, 140));
        displayPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 140));
        
        // Create label for display title
        JLabel displayLabel = new JLabel("  📺 DISPLAY");
        displayLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        displayLabel.setForeground(new Color(150, 160, 170));
        displayLabel.setBorder(new EmptyBorder(0, 5, 8, 0));
        
        // Create the display text area (acts as the kiosk screen)
        displayArea = new JTextArea(5, 40);
        displayArea.setEditable(false);  // User cannot edit the display
        displayArea.setFont(new Font("Consolas", Font.BOLD, 13));
        displayArea.setBackground(DISPLAY_BG);
        displayArea.setForeground(DISPLAY_TEXT);
        displayArea.setCaretColor(DISPLAY_TEXT);
        displayArea.setMargin(new Insets(10, 10, 10, 10));
        displayArea.setLineWrap(true);
        displayArea.setWrapStyleWord(true);
        
        // Add scroll capability to the display
        JScrollPane scrollPane = new JScrollPane(displayArea);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(0, 180, 100, 100), 2));
        scrollPane.setPreferredSize(new Dimension(530, 100));
        scrollPane.getViewport().setBackground(DISPLAY_BG);
        
        JPanel innerPanel = new JPanel(new BorderLayout());
        innerPanel.setOpaque(false);
        innerPanel.add(displayLabel, BorderLayout.NORTH);
        innerPanel.add(scrollPane, BorderLayout.CENTER);
        
        displayPanel.add(innerPanel, BorderLayout.CENTER);
        return displayPanel;
    }
    
    /**
     * Creates the ticket selection panel with destination buttons
     * Uses GridLayout to arrange buttons in a 2x2 grid
     * @return JPanel containing destination/ticket buttons
     */
    private JPanel createTicketSelectionPanel() {
        JPanel outerPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(CARD_BACKGROUND);
                g2d.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 20, 20));
            }
        };
        outerPanel.setOpaque(false);
        outerPanel.setLayout(new BorderLayout());
        outerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        outerPanel.setPreferredSize(new Dimension(560, 200));
        outerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
        
        // Section title
        JLabel sectionLabel = new JLabel("🎯 SELECT DESTINATION");
        sectionLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        sectionLabel.setForeground(TEXT_DARK);
        sectionLabel.setBorder(new EmptyBorder(0, 5, 8, 0));
        
        JPanel ticketPanel = new JPanel();
        // GridLayout arranges components in a grid of rows and columns
        // Parameters: rows, columns, horizontal gap, vertical gap
        ticketPanel.setLayout(new GridLayout(2, 2, 10, 10));
        ticketPanel.setOpaque(false);
        ticketPanel.setPreferredSize(new Dimension(530, 160));
        
        // Create destination buttons with prices displayed
        btnStationA = createStyledButton("Station A", "$10.00", new Color(46, 204, 113), "🚉");
        btnStationB = createStyledButton("Station B", "$20.00", new Color(52, 152, 219), "🚉");
        btnStationC = createStyledButton("Station C", "$30.00", new Color(230, 126, 34), "🚉");
        btnStationD = createStyledButton("Station D", "$50.00", new Color(155, 89, 182), "🚉");
        
        // Add ActionListener to each button (this class implements ActionListener)
        btnStationA.addActionListener(this);
        btnStationB.addActionListener(this);
        btnStationC.addActionListener(this);
        btnStationD.addActionListener(this);
        
        // Add buttons to the panel
        ticketPanel.add(btnStationA);
        ticketPanel.add(btnStationB);
        ticketPanel.add(btnStationC);
        ticketPanel.add(btnStationD);
        
        outerPanel.add(sectionLabel, BorderLayout.NORTH);
        outerPanel.add(ticketPanel, BorderLayout.CENTER);
        
        return outerPanel;
    }
    
    /**
     * Creates the money insertion panel with buttons to add money
     * Uses FlowLayout for horizontal button arrangement
     * @return JPanel containing money insertion buttons
     */
    private JPanel createMoneyPanel() {
        JPanel outerPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(CARD_BACKGROUND);
                g2d.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 20, 20));
            }
        };
        outerPanel.setOpaque(false);
        outerPanel.setLayout(new BorderLayout());
        outerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        outerPanel.setPreferredSize(new Dimension(560, 90));
        outerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
        outerPanel.setMinimumSize(new Dimension(560, 90));
        
        // Section title
        JLabel sectionLabel = new JLabel("💵 INSERT MONEY");
        sectionLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        sectionLabel.setForeground(TEXT_DARK);
        sectionLabel.setBorder(new EmptyBorder(0, 5, 5, 0));
        
        JPanel moneyPanel = new JPanel();
        // FlowLayout arranges components in a row, wrapping to next line if needed
        // Parameters: alignment, horizontal gap, vertical gap
        moneyPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 5));
        moneyPanel.setOpaque(false);
        
        // Create money insertion buttons
        btnInsert5 = createMoneyButton("$5", new Color(39, 174, 96));
        btnInsert10 = createMoneyButton("$10", new Color(34, 153, 84));
        btnInsert20 = createMoneyButton("$20", new Color(30, 132, 73));
        
        // Add ActionListener to money buttons
        btnInsert5.addActionListener(this);
        btnInsert10.addActionListener(this);
        btnInsert20.addActionListener(this);
        
        // Add buttons to panel
        moneyPanel.add(btnInsert5);
        moneyPanel.add(btnInsert10);
        moneyPanel.add(btnInsert20);
        
        outerPanel.add(sectionLabel, BorderLayout.NORTH);
        outerPanel.add(moneyPanel, BorderLayout.CENTER);
        
        return outerPanel;
    }
    
    /**
     * Creates the bottom control panel with Print and Clear buttons
     * @return JPanel containing control buttons
     */
    private JPanel createControlPanel() {
        JPanel controlPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(40, 55, 71));
                g2d.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 20, 20));
            }
        };
        controlPanel.setOpaque(false);
        controlPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        controlPanel.setBorder(new EmptyBorder(5, 10, 5, 10));
        controlPanel.setPreferredSize(new Dimension(560, 60));
        
        // Create action buttons with modern styling
        btnPrintTicket = createActionButton("🎫 PRINT TICKET", ACCENT_GREEN);
        btnClear = createActionButton("✖ CANCEL", ACCENT_RED);
        
        btnPrintTicket.addActionListener(this);
        btnClear.addActionListener(this);
        
        controlPanel.add(btnPrintTicket);
        controlPanel.add(btnClear);
        
        return controlPanel;
    }
    
    /**
     * Creates a styled action button (Print/Clear)
     */
    private JButton createActionButton(String text, Color bgColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (getModel().isPressed()) {
                    g2d.setColor(bgColor.darker());
                } else if (getModel().isRollover()) {
                    g2d.setColor(bgColor.brighter());
                } else {
                    g2d.setColor(bgColor);
                }
                g2d.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 15, 15));
                
                // Draw text
                FontMetrics fm = g2d.getFontMetrics();
                g2d.setColor(Color.WHITE);
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2d.drawString(getText(), x, y);
            }
        };
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setPreferredSize(new Dimension(160, 40));
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }
    
    // ==================== Helper Methods for Button Styling ====================
    
    /**
     * Creates a styled destination button with modern design
     * @param title The station name
     * @param price The ticket price
     * @param bgColor The background color for the button
     * @param icon The emoji icon
     * @return Styled JButton
     */
    private JButton createStyledButton(String title, String price, Color bgColor, String icon) {
        JButton button = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Draw rounded rectangle background with gradient
                Color topColor = getModel().isPressed() ? bgColor.darker() : 
                                 getModel().isRollover() ? bgColor.brighter() : bgColor;
                Color bottomColor = topColor.darker();
                
                GradientPaint gradient = new GradientPaint(0, 0, topColor, 0, getHeight(), bottomColor);
                g2d.setPaint(gradient);
                g2d.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 14, 14));
                
                // Draw icon
                g2d.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
                g2d.setColor(new Color(255, 255, 255, 200));
                FontMetrics fm = g2d.getFontMetrics();
                g2d.drawString(icon, (getWidth() - fm.stringWidth(icon)) / 2, 22);
                
                // Draw title
                g2d.setFont(new Font("Segoe UI", Font.BOLD, 12));
                g2d.setColor(Color.WHITE);
                fm = g2d.getFontMetrics();
                g2d.drawString(title, (getWidth() - fm.stringWidth(title)) / 2, 40);
                
                // Draw price
                g2d.setFont(new Font("Segoe UI", Font.BOLD, 14));
                g2d.setColor(new Color(255, 255, 255, 230));
                fm = g2d.getFontMetrics();
                g2d.drawString(price, (getWidth() - fm.stringWidth(price)) / 2, 58);
            }
        };
        button.setPreferredSize(new Dimension(120, 70));
        button.setMinimumSize(new Dimension(120, 70));
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }
    
    /**
     * Creates a styled money insertion button
     * @param text The button text
     * @param bgColor The background color
     * @return Styled JButton
     */
    private JButton createMoneyButton(String text, Color bgColor) {
        JButton button = new JButton("💵 " + text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                Color color = getModel().isPressed() ? bgColor.darker() : 
                              getModel().isRollover() ? bgColor.brighter() : bgColor;
                
                g2d.setColor(color);
                g2d.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 25, 25));
                
                // Draw text
                g2d.setFont(getFont());
                g2d.setColor(Color.WHITE);
                FontMetrics fm = g2d.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2d.drawString(getText(), x, y);
            }
        };
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setPreferredSize(new Dimension(100, 40));
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }
    
    // ==================== Event Handling ====================
    
    /**
     * ActionListener implementation - handles all button click events
     * This method is called automatically when any button with this ActionListener is clicked
     * 
     * @param e The ActionEvent containing information about the event source
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        // Get the source of the event (which button was clicked)
        Object source = e.getSource();
        
        // ===== Handle Destination Selection Buttons =====
        // When a destination button is clicked, store the selection and update display
        
        if (source == btnStationA) {
            // User selected Station A
            selectTicket("Station A", PRICE_STATION_A);
        } 
        else if (source == btnStationB) {
            // User selected Station B
            selectTicket("Station B", PRICE_STATION_B);
        } 
        else if (source == btnStationC) {
            // User selected Station C
            selectTicket("Station C", PRICE_STATION_C);
        } 
        else if (source == btnStationD) {
            // User selected Station D
            selectTicket("Station D", PRICE_STATION_D);
        }
        
        // ===== Handle Money Insertion Buttons =====
        // When money button is clicked, add the amount to current balance
        
        else if (source == btnInsert5) {
            // Insert $5 into the machine
            insertMoney(5.00);
        } 
        else if (source == btnInsert10) {
            // Insert $10 into the machine
            insertMoney(10.00);
        } 
        else if (source == btnInsert20) {
            // Insert $20 into the machine
            insertMoney(20.00);
        }
        
        // ===== Handle Print Ticket Button =====
        // Validate payment and print ticket if sufficient funds
        
        else if (source == btnPrintTicket) {
            printTicket();
        }
        
        // ===== Handle Clear/Cancel Button =====
        // Reset all selections and balance
        
        else if (source == btnClear) {
            clearTransaction();
        }
    }
    
    // ==================== Business Logic Methods ====================
    
    /**
     * Handles ticket/destination selection
     * Stores the selected destination and price, then updates the display
     * 
     * @param destination The name of the selected station
     * @param price The ticket price for the selected destination
     */
    private void selectTicket(String destination, double price) {
        // Store selection in instance variables
        selectedDestination = destination;
        selectedTicketPrice = price;
        ticketSelected = true;
        
        // Build and display the selection message
        StringBuilder message = new StringBuilder();
        message.append("═══════════════════════════════\n");
        message.append("       TICKET SELECTED\n");
        message.append("═══════════════════════════════\n\n");
        message.append("Destination: ").append(destination).append("\n");
        message.append("Ticket Price: $").append(String.format("%.2f", price)).append("\n\n");
        message.append("───────────────────────────────\n");
        message.append("Current Balance: $").append(String.format("%.2f", currentBalance)).append("\n");
        
        // Check if more money is needed
        if (currentBalance < price) {
            double needed = price - currentBalance;
            message.append("Amount Needed: $").append(String.format("%.2f", needed)).append("\n");
            message.append("\n⚠ Please insert money to continue.");
        } else {
            message.append("\n✓ Sufficient funds! Press PRINT TICKET.");
        }
        
        updateDisplay(message.toString());
    }
    
    /**
     * Handles money insertion
     * Adds the specified amount to the current balance and updates display
     * 
     * @param amount The amount of money to add to the balance
     */
    private void insertMoney(double amount) {
        // Add money to current balance
        currentBalance += amount;
        
        // Build display message
        StringBuilder message = new StringBuilder();
        message.append("═══════════════════════════════\n");
        message.append("       MONEY INSERTED\n");
        message.append("═══════════════════════════════\n\n");
        message.append("Inserted: $").append(String.format("%.2f", amount)).append("\n");
        message.append("Current Balance: $").append(String.format("%.2f", currentBalance)).append("\n\n");
        
        // Show ticket info if already selected
        if (ticketSelected) {
            message.append("───────────────────────────────\n");
            message.append("Selected: ").append(selectedDestination).append("\n");
            message.append("Price: $").append(String.format("%.2f", selectedTicketPrice)).append("\n\n");
            
            // Check if sufficient funds now
            if (currentBalance >= selectedTicketPrice) {
                message.append("✓ Sufficient funds!\n");
                message.append("Press PRINT TICKET to continue.");
            } else {
                double needed = selectedTicketPrice - currentBalance;
                message.append("Still needed: $").append(String.format("%.2f", needed));
            }
        } else {
            message.append("Please select a destination.");
        }
        
        updateDisplay(message.toString());
    }
    
    /**
     * Handles the ticket printing process
     * Validates that a ticket is selected and sufficient funds are available
     * If valid, prints the ticket and calculates change
     */
    private void printTicket() {
        StringBuilder message = new StringBuilder();
        
        // Validation 1: Check if a ticket has been selected
        if (!ticketSelected) {
            message.append("═══════════════════════════════\n");
            message.append("          ⚠ ERROR\n");
            message.append("═══════════════════════════════\n\n");
            message.append("No destination selected!\n\n");
            message.append("Please select a destination first.");
            updateDisplay(message.toString());
            return;
        }
        
        // Validation 2: Check if sufficient funds are available
        if (currentBalance < selectedTicketPrice) {
            double shortage = selectedTicketPrice - currentBalance;
            message.append("═══════════════════════════════\n");
            message.append("    ⚠ INSUFFICIENT FUNDS\n");
            message.append("═══════════════════════════════\n\n");
            message.append("Ticket Price: $").append(String.format("%.2f", selectedTicketPrice)).append("\n");
            message.append("Your Balance: $").append(String.format("%.2f", currentBalance)).append("\n");
            message.append("───────────────────────────────\n");
            message.append("Short by: $").append(String.format("%.2f", shortage)).append("\n\n");
            message.append("Please insert more money.");
            updateDisplay(message.toString());
            return;
        }
        
        // All validations passed - Print the ticket!
        double change = currentBalance - selectedTicketPrice;
        
        message.append("═══════════════════════════════\n");
        message.append("     🎫 TICKET PRINTED! 🎫\n");
        message.append("═══════════════════════════════\n\n");
        message.append("╔═════════════════════════════╗\n");
        message.append("║     CITY METRO TICKET       ║\n");
        message.append("╠═════════════════════════════╣\n");
        message.append("║ TO: ").append(String.format("%-22s", selectedDestination)).append("║\n");
        message.append("║ FARE: $").append(String.format("%-19.2f", selectedTicketPrice)).append("║\n");
        message.append("║ PAID: $").append(String.format("%-19.2f", currentBalance)).append("║\n");
        message.append("╚═════════════════════════════╝\n\n");
        
        if (change > 0) {
            message.append("💰 CHANGE RETURNED: $").append(String.format("%.2f", change)).append("\n\n");
        }
        
        message.append("Thank you for traveling with us!\n");
        message.append("Have a safe journey! 🚇");
        
        updateDisplay(message.toString());
        
        // Reset the machine for next transaction after successful print
        // Reset state but keep the success message displayed
        currentBalance = 0.0;
        selectedTicketPrice = 0.0;
        selectedDestination = "";
        ticketSelected = false;
    }
    
    /**
     * Clears/Cancels the current transaction
     * Resets all state variables and returns any inserted money
     */
    private void clearTransaction() {
        StringBuilder message = new StringBuilder();
        message.append("═══════════════════════════════\n");
        message.append("     TRANSACTION CANCELLED\n");
        message.append("═══════════════════════════════\n\n");
        
        if (currentBalance > 0) {
            message.append("💰 Returning: $").append(String.format("%.2f", currentBalance)).append("\n\n");
        }
        
        message.append("Transaction has been reset.\n\n");
        message.append("───────────────────────────────\n");
        message.append("Welcome to City Metro!\n");
        message.append("Please select your destination.");
        
        // Reset all state variables
        currentBalance = 0.0;
        selectedTicketPrice = 0.0;
        selectedDestination = "";
        ticketSelected = false;
        
        updateDisplay(message.toString());
    }
    
    /**
     * Updates the display area with new text
     * @param text The text to display
     */
    private void updateDisplay(String text) {
        displayArea.setText(text);
        // Scroll to top of display
        displayArea.setCaretPosition(0);
    }
    
    // ==================== Main Method ====================
    
    /**
     * Main method - Entry point of the application
     * Creates and displays the Ticket Machine GUI
     * 
     * Uses SwingUtilities.invokeLater to ensure thread safety
     * by creating the GUI on the Event Dispatch Thread (EDT)
     * 
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        // SwingUtilities.invokeLater ensures GUI creation happens on the EDT
        // This is the recommended way to start Swing applications
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Create instance of TicketMachine
                TicketMachine machine = new TicketMachine();
                
                // Make the window visible
                machine.setVisible(true);
            }
        });
    }
}
