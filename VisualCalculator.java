package visualcalculator;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

public class VisualCalculator extends JFrame implements ActionListener {
    private JTextField textField;
    private double currentValue;
    private String operator;

    public VisualCalculator() {
        initialize();
    }

    private void initialize() {
        setupMainFrame();
        setupTextField();
        setupButtons();
        setVisible(true);
    }

    private void setupMainFrame() {
        setTitle("Visual Calculator");
        setSize(400, 500); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
    }

    private void setupTextField() {
        textField = new JTextField(20); 
        textField.setEditable(false);
        textField.setHorizontalAlignment(JTextField.RIGHT);
        textField.setBackground(Color.WHITE);  
        textField.setForeground(Color.BLACK); 
        textField.setPreferredSize(new Dimension(200, 50));
        textField.setFont(new Font("SansSerif", Font.BOLD, 24));
        Border border = BorderFactory.createLineBorder(Color.BLACK, 10); // Black border with a thickness of 10
        textField.setBorder(border);

        add(textField, BorderLayout.NORTH);
    }

    private void setupButtons() {
    	   JPanel buttonPanel = new JPanel(new GridBagLayout()) {
    		   float hue = 0f;

    	        @Override
    	        protected void paintComponent(Graphics g) {
    	            super.paintComponent(g);
    	            Graphics2D g2d = (Graphics2D) g;
    	            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

    	            // Update the hue for color transition
    	            hue += 0.001f; // Adjust this value for speed of color change
    	            if (hue > 1f) {
    	                hue = 0f; // Reset hue after completing the spectrum
    	            }

    	            Color color = Color.getHSBColor(hue, 1f, 1f); // Full saturation and brightness
    	            g2d.setPaint(color);
    	            g2d.fillRect(0, 0, getWidth(), getHeight());
    	        }
    	    };
    	 // Timer for repainting the panel
    	    Timer repaintTimer = new Timer();
			repaintTimer.scheduleAtFixedRate(new TimerTask() {
				@Override
				public void run() {
					buttonPanel.repaint();
				}
			}, 0, 1000 / 60); // 60 FPS
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = .7;  // Give columns equal weight
        constraints.weighty = .5;
        constraints.insets = new Insets(5, 5, 5, 5);

        String[] buttonLabels = {
        		"x","/", "\u2190", "=",
        	    "7", "8", "9", "C",
        	    "4", "5", "6", "-",
        	    "1", "2", "3", "+",
        	    "0", ".", "±", 
        };

        int gridX = 0;
        int gridY = 0;
        for (String label : buttonLabels) {
            JButton button = new JButton(label);
            button.addActionListener(this);
            button.setFont(new Font("Arial", Font.BOLD, 20));
            button.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2)); // White border, adjust color and thickness// Adjust font and size as needed
            button.setBackground(Color.BLACK); // Or use any other contrasting color
            button.setForeground(Color.WHITE); 
            if (label.equals("=")) {
                button.setBackground(new Color(51, 153, 255)); // Equals button is white
                button.setForeground(Color.BLACK); // Text color for visibility
            } else if (label.equals("C")) {
                button.setBackground(new Color(204, 0, 0));
        	} else {
                button.setBackground(Color.BLACK); // Default button color
            }// Set the text color for contrast
            constraints.gridx = gridX;
            constraints.gridy = gridY;
            buttonPanel.add(button, constraints);
            button.setMargin(new Insets(10, 10, 10, 10)); // Increase internal padding
            button.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                	 if (!label.equals("=") && !label.equals("C")) {
                         button.setBackground(Color.DARK_GRAY);
                     }
					else if (label.equals("=")) {
						button.setBackground(new Color(51, 204, 255));
					} else if (label.equals("C")) {
						button.setBackground(new Color(255, 51, 51));
					} else {
						button.setBackground(Color.BLACK);
					}
                }

                @Override
                public void mouseExited(MouseEvent e) {
                	if (label.equals("=")) {
                        button.setBackground(new Color(51, 153, 255));
                    } else if (label.equals("C")) {
                        button.setBackground(new Color(204, 0, 0));
                    } else {
                        button.setBackground(Color.BLACK);
                    }
                }
            });


            gridX++;
            if (gridX > 3) {
                gridX = 0;
                gridY++;
            }
        }

        add(buttonPanel, BorderLayout.CENTER);
    }

    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        if (command.matches("[0-9]")) {
            textField.setText(textField.getText() + command);
        } else if (command.equals(".")) {
            if (!textField.getText().contains(".")) {
                textField.setText(textField.getText() + ".");
            }
        } else if (command.equals("C")) {
            textField.setText("");
            currentValue = 0;
            operator = null;
        } else if (command.equals("x")) { // Handle 'x' for multiplication
                if (operator == null) {
                    currentValue = Double.parseDouble(textField.getText());
                    operator = "*"; // Use '*' internally
                    textField.setText("");
                }
        } else if (command.matches("[+\\-*/]")) {
            if (operator == null) {
                currentValue = Double.parseDouble(textField.getText());
                operator = command;
                textField.setText("");
            }
        } else if (command.equals("±")) {
			if (!textField.getText().isEmpty()) {
				double value = Double.parseDouble(textField.getText());
				value = -value;
				textField.setText(Double.toString(value));
			}
        } else if (command.equals("=")) {
            if (operator != null) {
                double nextValue = Double.parseDouble(textField.getText());
                switch (operator) {
                    case "+":
                        currentValue += nextValue;
                        break;
                    case "-":
                        currentValue -= nextValue;
                        break;
                    case "*": 
                        currentValue *= nextValue;
                        break;
                    case "/":
                        if (nextValue != 0) {
                            currentValue /= nextValue;
                        } else {
                            textField.setText("Error: Division by zero");
                            return;
                        }
                        break;
                }
                textField.setText(Double.toString(currentValue));
                operator = null;
            }
        } else if (command.equals("\u2190")) { // Backspace functionality
            String currentText = textField.getText();
            if (currentText.length() > 0) {
                textField.setText(currentText.substring(0, currentText.length() - 1));
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VisualCalculator());
    }
}

    


		
		
		//Create frame (400 x 300) with title "Visual Calculator"
		//Set frame to close on exit
		
		
		
		
		//Create layout for calculator (GridBag, Border. Flow, Grid, Box, etc.)
		
		//Create Buttons (Manual)
		//Numbers = 0 - 9 (10 buttons)
		//Operators = +, -, *, /, = (5 buttons)
		//Clear, Backspace (2 buttons)
		//Decimal (1 button)
		//Positive/Negative (1 button)
		
		//GUI Display
		//TextField (Display)
		
		//Methods for operators
		//Add
		//Subtract
		//Multiply
		//Divide
		//Equals
		//Clear
		//Backspace
		//Decimal
		//Positive/Negative
		//Number (0 - 9)
		
		//ActionListeners for buttons
		
		//Error handling for division by zero
		//Error handling for invalid input
		//Error handling for overflow
		//Error handling for underflow
		//Error handling for invalid operator
		
		
		
		
		
		

    

