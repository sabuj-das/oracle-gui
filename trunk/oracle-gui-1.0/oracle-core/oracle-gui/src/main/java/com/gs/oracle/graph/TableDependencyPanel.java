/**
 * 
 */
package com.gs.oracle.graph;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

/**
 * @author sabuj.das
 * 
 */
public class TableDependencyPanel extends JPanel {

	public TableDependencyPanel() {
		initComponents();
	}

	private void initComponents() {
		GridBagConstraints gridBagConstraints;

		generateGraphToolBar = new JToolBar();
		generateGraphButton = new JButton();
		jSeparator1 = new JToolBar.Separator();
		saveGraphButton = new JButton();
		jSeparator2 = new JToolBar.Separator();
		clearButton = new JButton();
		graphHolderPanel = new JPanel();
		imageViewToolBar = new JToolBar();
		fitToWindowButton = new JButton();
		jSeparator3 = new JToolBar.Separator();
		zoomInButton = new JButton();
		jSeparator4 = new JToolBar.Separator();
		zoomPercentTextField = new JTextField();
		jLabel2 = new JLabel();
		jSeparator5 = new JToolBar.Separator();
		zoomOutButton = new JButton();
		jSeparator6 = new JToolBar.Separator();
		actualSizeButton = new JButton();
		loadingLabel = new JLabel();
		graphHolderScrollPane = new JScrollPane();

		FormListener formListener = new FormListener();

		setLayout(new GridBagLayout());

		generateGraphToolBar.setFloatable(false);
		generateGraphToolBar.setRollover(true);

		generateGraphButton.setText("Generate Graph");
		generateGraphButton.setFocusable(false);
		generateGraphButton.setHorizontalTextPosition(SwingConstants.CENTER);
		generateGraphButton.setVerticalTextPosition(SwingConstants.BOTTOM);
		generateGraphButton.addActionListener(formListener);
		generateGraphToolBar.add(generateGraphButton);
		generateGraphToolBar.add(jSeparator1);

		saveGraphButton.setText("Save");
		saveGraphButton.setFocusable(false);
		saveGraphButton.setHorizontalTextPosition(SwingConstants.CENTER);
		saveGraphButton.setVerticalTextPosition(SwingConstants.BOTTOM);
		saveGraphButton.addActionListener(formListener);
		generateGraphToolBar.add(saveGraphButton);
		generateGraphToolBar.add(jSeparator2);

		clearButton.setText("Clear");
		clearButton.setFocusable(false);
		clearButton.setHorizontalTextPosition(SwingConstants.CENTER);
		clearButton.setVerticalTextPosition(SwingConstants.BOTTOM);
		clearButton.addActionListener(formListener);
		generateGraphToolBar.add(clearButton);

		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.weightx = 1.0;
		add(generateGraphToolBar, gridBagConstraints);

		graphHolderPanel.setBackground(new Color(255, 255, 255));
		graphHolderPanel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createCompoundBorder(BorderFactory
						.createLineBorder(new Color(0, 0, 0)), BorderFactory
						.createMatteBorder(1, 25, 1, 25, new Color(204, 204,
								255))), new LineBorder(
						new Color(153, 153, 255), 1, true)));
		graphHolderPanel.setLayout(new BorderLayout());
		graphHolderScrollPane.setViewportView(graphHolderPanel);
		
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		add(graphHolderScrollPane, gridBagConstraints);
		
		

		imageViewToolBar.setFloatable(false);
		imageViewToolBar.setRollover(true);

		fitToWindowButton.setText("Fit");
		fitToWindowButton.setFocusable(false);
		fitToWindowButton.setHorizontalTextPosition(SwingConstants.CENTER);
		fitToWindowButton.setVerticalTextPosition(SwingConstants.BOTTOM);
		fitToWindowButton.addActionListener(formListener);
		imageViewToolBar.add(fitToWindowButton);
		imageViewToolBar.add(jSeparator3);

		zoomInButton.setText("+");
		zoomInButton.setFocusable(false);
		zoomInButton.setHorizontalTextPosition(SwingConstants.CENTER);
		zoomInButton.setVerticalTextPosition(SwingConstants.BOTTOM);
		zoomInButton.addActionListener(formListener);
		imageViewToolBar.add(zoomInButton);
		imageViewToolBar.add(jSeparator4);

		zoomPercentTextField.setHorizontalAlignment(JTextField.CENTER);
		zoomPercentTextField.setText("100");
		zoomPercentTextField.setMinimumSize(new Dimension(50, 20));
		zoomPercentTextField.setPreferredSize(new Dimension(50, 20));
		zoomPercentTextField.addFocusListener(formListener);
		zoomPercentTextField.addKeyListener(formListener);
		imageViewToolBar.add(zoomPercentTextField);

		jLabel2.setText(" % ");
		imageViewToolBar.add(jLabel2);
		imageViewToolBar.add(jSeparator5);

		zoomOutButton.setText("-");
		zoomOutButton.setFocusable(false);
		zoomOutButton.setHorizontalTextPosition(SwingConstants.CENTER);
		zoomOutButton.setVerticalTextPosition(SwingConstants.BOTTOM);
		zoomOutButton.addActionListener(formListener);
		imageViewToolBar.add(zoomOutButton);
		imageViewToolBar.add(jSeparator6);

		actualSizeButton.setText("Actual");
		actualSizeButton.setFocusable(false);
		actualSizeButton.setHorizontalTextPosition(SwingConstants.CENTER);
		actualSizeButton.setVerticalTextPosition(SwingConstants.BOTTOM);
		actualSizeButton.addActionListener(formListener);
		imageViewToolBar.add(actualSizeButton);

		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.weightx = 1.0;
		add(imageViewToolBar, gridBagConstraints);

		loadingLabel.setText("Loading");
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.fill = GridBagConstraints.VERTICAL;
		gridBagConstraints.anchor = GridBagConstraints.EAST;
		gridBagConstraints.insets = new Insets(2, 2, 2, 2);
		add(loadingLabel, gridBagConstraints);
	}

	// Code for dispatching events from components to event handlers.

	private class FormListener implements ActionListener, FocusListener,
			KeyListener {
		FormListener() {
		}

		public void actionPerformed(ActionEvent evt) {
			if (evt.getSource() == generateGraphButton) {
				TableDependencyPanel.this
						.generateGraphButtonActionPerformed(evt);
			} else if (evt.getSource() == saveGraphButton) {
				TableDependencyPanel.this.saveGraphButtonActionPerformed(evt);
			} else if (evt.getSource() == clearButton) {
				TableDependencyPanel.this.clearButtonActionPerformed(evt);
			} else if (evt.getSource() == fitToWindowButton) {
				TableDependencyPanel.this.fitToWindowButtonActionPerformed(evt);
			} else if (evt.getSource() == zoomInButton) {
				TableDependencyPanel.this.zoomInButtonActionPerformed(evt);
			} else if (evt.getSource() == zoomOutButton) {
				TableDependencyPanel.this.zoomOutButtonActionPerformed(evt);
			} else if (evt.getSource() == actualSizeButton) {
				TableDependencyPanel.this.actualSizeButtonActionPerformed(evt);
			}
		}

		public void focusGained(FocusEvent evt) {
		}

		public void focusLost(FocusEvent evt) {
			if (evt.getSource() == zoomPercentTextField) {
				TableDependencyPanel.this.zoomPercentTextFieldFocusLost(evt);
			}
		}

		public void keyPressed(KeyEvent evt) {
			if (evt.getSource() == zoomPercentTextField) {
				TableDependencyPanel.this.zoomPercentTextFieldKeyPressed(evt);
			}
			else if (evt.getSource() == graphHolderPanel) {
                TableDependencyPanel.this.graphHolderPanelKeyPressed(evt);
            }
		}

		public void keyReleased(KeyEvent evt) {
			if (evt.getSource() == zoomPercentTextField) {
				TableDependencyPanel.this.zoomPercentTextFieldKeyReleased(evt);
			}
			else if (evt.getSource() == graphHolderPanel) {
                TableDependencyPanel.this.graphHolderPanelKeyReleased(evt);
            }
		}

		public void keyTyped(KeyEvent evt) {
			if (evt.getSource() == zoomPercentTextField) {
				TableDependencyPanel.this.zoomPercentTextFieldKeyTyped(evt);
			}
			else if (evt.getSource() == graphHolderPanel) {
                TableDependencyPanel.this.graphHolderPanelKeyTyped(evt);
            }
		}
		
		public void mouseClicked(MouseEvent evt) {
            if (evt.getSource() == graphHolderPanel) {
                TableDependencyPanel.this.graphHolderPanelMouseClicked(evt);
            }
        }

        public void mouseEntered(MouseEvent evt) {
            if (evt.getSource() == graphHolderPanel) {
                TableDependencyPanel.this.graphHolderPanelMouseEntered(evt);
            }
        }

        public void mouseExited(MouseEvent evt) {
            if (evt.getSource() == graphHolderPanel) {
                TableDependencyPanel.this.graphHolderPanelMouseExited(evt);
            }
        }

        public void mousePressed(MouseEvent evt) {
            if (evt.getSource() == graphHolderPanel) {
                TableDependencyPanel.this.graphHolderPanelMousePressed(evt);
            }
        }

        public void mouseReleased(MouseEvent evt) {
            if (evt.getSource() == graphHolderPanel) {
                TableDependencyPanel.this.graphHolderPanelMouseReleased(evt);
            }
        }

        public void mouseDragged(MouseEvent evt) {
            if (evt.getSource() == graphHolderPanel) {
                TableDependencyPanel.this.graphHolderPanelMouseDragged(evt);
            }
        }

        public void mouseMoved(MouseEvent evt) {
            if (evt.getSource() == graphHolderPanel) {
                TableDependencyPanel.this.graphHolderPanelMouseMoved(evt);
            }
        }
	}

	private void generateGraphButtonActionPerformed(ActionEvent evt) {
		// TODO add your handling code here:
	}

	private void saveGraphButtonActionPerformed(ActionEvent evt) {
		// TODO add your handling code here:
	}

	private void clearButtonActionPerformed(ActionEvent evt) {
		// TODO add your handling code here:
	}

	private void fitToWindowButtonActionPerformed(ActionEvent evt) {
		// TODO add your handling code here:
	}

	private void zoomInButtonActionPerformed(ActionEvent evt) {
		// TODO add your handling code here:
	}

	private void zoomPercentTextFieldKeyPressed(KeyEvent evt) {
		// TODO add your handling code here:
	}

	private void zoomPercentTextFieldKeyReleased(KeyEvent evt) {
		// TODO add your handling code here:
	}

	private void zoomPercentTextFieldKeyTyped(KeyEvent evt) {
		// TODO add your handling code here:
	}

	private void zoomPercentTextFieldFocusLost(FocusEvent evt) {
		// TODO add your handling code here:
	}

	private void zoomOutButtonActionPerformed(ActionEvent evt) {
		// TODO add your handling code here:
	}

	private void actualSizeButtonActionPerformed(ActionEvent evt) {
		// TODO add your handling code here:
	}

	private void graphHolderPanelMouseEntered(MouseEvent evt) {
        // TODO add your handling code here:
    }

    private void graphHolderPanelMouseExited(MouseEvent evt) {
        // TODO add your handling code here:
    }

    private void graphHolderPanelMouseDragged(MouseEvent evt) {
        // TODO add your handling code here:
    }

    private void graphHolderPanelMouseMoved(MouseEvent evt) {
        // TODO add your handling code here:
    }

    private void graphHolderPanelMouseClicked(MouseEvent evt) {
        // TODO add your handling code here:
    }

    private void graphHolderPanelMousePressed(MouseEvent evt) {
        // TODO add your handling code here:
    }

    private void graphHolderPanelMouseReleased(MouseEvent evt) {
        // TODO add your handling code here:
    }
    
    private void graphHolderPanelKeyTyped(KeyEvent evt) {
        // TODO add your handling code here:
    }
    
    private void graphHolderPanelKeyPressed(KeyEvent evt) {
        // TODO add your handling code here:
    }

    private void graphHolderPanelKeyReleased(KeyEvent evt) {
        // TODO add your handling code here:
    }
    
	// Variables declaration 
	private JButton actualSizeButton;
	private JButton clearButton;
	private JButton fitToWindowButton;
	private JButton generateGraphButton;
	private JToolBar generateGraphToolBar;
	private JPanel graphHolderPanel;
	private JToolBar imageViewToolBar;
	private JLabel jLabel2;
	private JToolBar.Separator jSeparator1;
	private JToolBar.Separator jSeparator2;
	private JToolBar.Separator jSeparator3;
	private JToolBar.Separator jSeparator4;
	private JToolBar.Separator jSeparator5;
	private JToolBar.Separator jSeparator6;
	private JLabel loadingLabel;
	private JButton saveGraphButton;
	private JButton zoomInButton;
	private JButton zoomOutButton;
	private JTextField zoomPercentTextField;
	private JScrollPane graphHolderScrollPane;
	// End of variables declaration

}