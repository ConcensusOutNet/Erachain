package gui.items.imprints;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableColumn;

import com.github.rjeschke.txtmark.Processor;

import core.item.imprints.ImprintCls;
import gui.library.MTable;
import gui.models.BalancesTableModel;
import gui.models.PersonStatusesModel;
import gui.models.Renderer_Left;
import gui.models.Renderer_Right;
import lang.Lang;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Саша
 */
public class Imprints_Info_Panel extends javax.swing.JPanel {

    private BalancesTableModel table_model;
	/**
     * Creates new form ImprintsInfoPanel
     */
    public Imprints_Info_Panel(ImprintCls imprint) {
        initComponents(imprint);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents(ImprintCls imprint) {
        java.awt.GridBagConstraints gridBagConstraints;

        key_jLabel = new javax.swing.JLabel();
        key_jTextField = new javax.swing.JTextField();
        Name_jLabel = new javax.swing.JLabel();
        Name_jTextField = new javax.swing.JTextField();
        description_jLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        description_jTextArea = new javax.swing.JTextArea();
        owner_jLabel = new javax.swing.JLabel();
        owner_jTextField = new javax.swing.JTextField();
        holders_jLabel = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
       

        java.awt.GridBagLayout layout = new java.awt.GridBagLayout();
        layout.columnWidths = new int[] {0, 7, 0};
        layout.rowHeights = new int[] {0, 2, 0, 2, 0, 2, 0, 2, 0, 2, 0};
        setLayout(layout);

        key_jLabel.setText(Lang.getInstance().translate("Key")+":");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(7, 8, 0, 9);
        add(key_jLabel, gridBagConstraints);

        key_jTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                key_jTextFieldActionPerformed(evt);
            }
        });
        key_jTextField.setText(Long.toString(imprint.getKey()));
        key_jTextField.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(6, 0, 0, 9);
        add(key_jTextField, gridBagConstraints);

        Name_jLabel.setText(Lang.getInstance().translate("Name")+":");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 8, 0, 9);
        add(Name_jLabel, gridBagConstraints);
        Name_jTextField.setText(imprint.getName());
        Name_jTextField.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START; 
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 9);
        add(Name_jTextField, gridBagConstraints);

        description_jLabel.setText(Lang.getInstance().translate("Description")+":");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 8, 0, 9);
        add(description_jLabel, gridBagConstraints);
        
        description_jTextArea.setEditable(false);
        description_jTextArea.setText(Processor.process(imprint.getDescription()));
        description_jTextArea.setColumns(20);
        description_jTextArea.setRows(5);
        jScrollPane1.setViewportView(description_jTextArea);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weighty = 0.3;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 9);
        add(jScrollPane1, gridBagConstraints);

        owner_jLabel.setText(Lang.getInstance().translate("Owner")+":");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 8, 0, 9);
        add(owner_jLabel, gridBagConstraints);
        owner_jTextField.setText(imprint.getOwner().getPersonAsString());
        owner_jTextField.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 9);
        add(owner_jTextField, gridBagConstraints);

        holders_jLabel.setText(Lang.getInstance().translate("Holders"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 8, 0, 9);
        add(holders_jLabel, gridBagConstraints);

                
      //BALANCES
       table_model = new BalancesTableModel(imprint.getKey());
        holders_jTable1 = new MTable(table_model);
             //CHECKBOX FOR FAVORITE
        		TableColumn to_Date_Column1 = holders_jTable1.getColumnModel().getColumn( BalancesTableModel.COLUMN_BALANCE);	
        		//favoriteColumn.setCellRenderer(new Renderer_Boolean()); //personsTable.getDefaultRenderer(Boolean.class));
        		to_Date_Column1.setMinWidth(80);
        		to_Date_Column1.setMaxWidth(200);
        		to_Date_Column1.setPreferredWidth(120);//.setWidth(30); 		
        
        
        
        jScrollPane2.setViewportView(holders_jTable1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 8, 9, 9);
        add(jScrollPane2, gridBagConstraints);
    }// </editor-fold>                        

    private void key_jTextFieldActionPerformed(java.awt.event.ActionEvent evt) {                                               
        // TODO add your handling code here:
    }                                              

    public void  delay_on_Close(){
    	table_model.removeObservers();
		
		
	}
    
    
    // Variables declaration - do not modify                     
    private javax.swing.JLabel Name_jLabel;
    private javax.swing.JTextField Name_jTextField;
    private javax.swing.JLabel description_jLabel;
    private javax.swing.JTextArea description_jTextArea;
    private javax.swing.JLabel holders_jLabel;
    private MTable holders_jTable1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel key_jLabel;
    private javax.swing.JTextField key_jTextField;
    private javax.swing.JLabel owner_jLabel;
    private javax.swing.JTextField owner_jTextField;
    // End of variables declaration                   
}
