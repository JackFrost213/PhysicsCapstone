package main;
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */


import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JTextField;

/**
 *
 * @author hfrie
 */
public class Panel3DGUI extends javax.swing.JPanel {

    /**
     * Creates new form GUIPanelTest
     */
    public Panel3DGUI() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        guiScaleLabel = new javax.swing.JLabel();
        guiTickLabel = new javax.swing.JLabel();
        guiRotationLabel = new javax.swing.JLabel();
        guiScaleX = new javax.swing.JTextField();
        guiScaleY = new javax.swing.JTextField();
        guiScaleZ = new javax.swing.JTextField();
        guiTickX = new javax.swing.JTextField();
        guiTickY = new javax.swing.JTextField();
        guiTickZ = new javax.swing.JTextField();
        guiRotX = new javax.swing.JTextField();
        guiRotY = new javax.swing.JTextField();
        guiRotZ = new javax.swing.JTextField();

        guiScaleLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        guiScaleLabel.setText("Scale");

        guiTickLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        guiTickLabel.setText("# of Ticks");

        guiRotationLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        guiRotationLabel.setText("Rotation P");

        guiScaleX.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                guiScaleXActionPerformed(evt);
            }
        });
        
        guiScaleX.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
            	guiScaleXPropertyChange(e);
            	//System.out.println("SCALE CHANGED");
            }
        });

        guiScaleY.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
            	guiScaleYPropertyChange(e);
            }
        });

        guiScaleZ.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
            	guiScaleZPropertyChange(e);
            }
        });

        guiTickX.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
            	guiTickXPropertyChange(e);
            }
        });

        guiTickY.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
            	guiTickYPropertyChange(e);
            }
        });
        
        guiTickZ.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
            	guiTickZPropertyChange(e);
            }
        });

        guiRotX.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
            	guiRotXPropertyChange(e);
            }
        });

        guiRotY.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
            	guiRotYPropertyChange(e);
            }
        });
        
        guiRotZ.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
            	guiRotZPropertyChange(e);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(guiRotationLabel))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(guiTickLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(guiScaleX, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(guiScaleY, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(guiScaleZ, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(guiTickX, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(guiTickY, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(guiTickZ, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(guiScaleLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(guiRotZ, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(guiRotX, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(guiRotY, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(guiScaleLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(guiScaleX, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(guiScaleY, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(guiScaleZ, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(guiTickLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(guiTickX, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(guiTickY, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(guiTickZ, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(guiRotationLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(guiRotZ, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(guiRotX, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(guiRotY, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 12, Short.MAX_VALUE))
        );
        
        this.setSize(110,160);
    }// </editor-fold>                        

    private void guiScaleXActionPerformed(java.awt.event.ActionEvent evt) {                                          
        // TODO add your handling code here:
    }                                         

    private void guiScaleXPropertyChange(KeyEvent evt) {       
    	try {
    	if(Float.valueOf(guiScaleX.getText()) != 0) {
        SimulationMain.deltaX=Float.valueOf(guiScaleX.getText());
    	}
    	}
    	catch(Exception e) {
    		//do nothing
    	}
    }                                        

    private void guiScaleYPropertyChange(KeyEvent evt) {       
    	try {
    		if(Float.valueOf(guiScaleY.getText()) != 0) {
        SimulationMain.deltaY=Float.valueOf(guiScaleY.getText());
    	}
    	}
    	catch(Exception e) {
    		//do nothing
    	}
    }   
    
    private void guiScaleZPropertyChange(KeyEvent evt) {       
    	try {
    		if(Float.valueOf(guiScaleZ.getText()) != 0) {
        SimulationMain.deltaZ=Float.valueOf(guiScaleZ.getText());
    	}
    	}
    	catch(Exception e) {
    		//do nothing
    	}
    } 

    private void guiTickXPropertyChange(KeyEvent evt) {       
    	try {
        SimulationMain.numOfSegmentsX=Float.valueOf(guiTickX.getText());
    	}
    	catch(Exception e) {
    		//do nothing
    	}
    }                                    

    private void guiTickYPropertyChange(KeyEvent evt) {       
    	try {
        SimulationMain.numOfSegmentsY=Float.valueOf(guiTickY.getText());
    	}
    	catch(Exception e) {
    		//do nothing
    	}
    }  
    
    private void guiTickZPropertyChange(KeyEvent evt) {       
    	try {
        SimulationMain.numOfSegmentsZ=Float.valueOf(guiTickZ.getText());
    	}
    	catch(Exception e) {
    		//do nothing
    	}
    }   

    private void guiRotXPropertyChange(KeyEvent evt) {       
    	try {
        SimulationMain.setRotY(Float.valueOf(guiRotX.getText()));
    	}
    	catch(Exception e) {
    		//do nothing
    	}
    }                                        

    private void guiRotYPropertyChange(KeyEvent evt) {       
    	try {
        SimulationMain.setRotZ(Float.valueOf(guiRotY.getText()));
    	}
    	catch(Exception e) {
    		//do nothing
    	}
    }   
    
    private void guiRotZPropertyChange(KeyEvent evt) {       
    	try {
        SimulationMain.setRotX(Float.valueOf(guiRotZ.getText()));
    	}
    	catch(Exception e) {
    		//do nothing
    	}
    }                                       


    // Variables declaration - do not modify                     
    public javax.swing.JTextField guiRotX;
    public javax.swing.JTextField guiRotY;
    public javax.swing.JTextField guiRotZ;
    private javax.swing.JLabel guiRotationLabel;
    private javax.swing.JLabel guiScaleLabel;
    public javax.swing.JTextField guiScaleX;
    public javax.swing.JTextField guiScaleY;
    public javax.swing.JTextField guiScaleZ;
    private javax.swing.JLabel guiTickLabel;
    public javax.swing.JTextField guiTickX;
    public javax.swing.JTextField guiTickY;
    public javax.swing.JTextField guiTickZ;
    // End of variables declaration                   
}
