package gui.items.records;

	import java.awt.event.ActionEvent;
	import java.awt.event.ActionListener;
	import java.awt.event.FocusEvent;
	import java.awt.event.FocusListener;
	import java.awt.event.MouseAdapter;
	import java.awt.event.MouseEvent;
	import java.awt.event.MouseListener;
	import java.awt.event.MouseMotionListener;
	import java.awt.event.WindowEvent;
	import java.awt.event.WindowFocusListener;
	import java.awt.image.ColorModel;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.swing.Timer;
	import java.awt.*;

	import javax.swing.DefaultRowSorter;
	import javax.swing.JButton;
	import javax.swing.JDialog;
	import javax.swing.JFrame;
	import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
	import javax.swing.JPanel;
	import javax.swing.JPopupMenu;
	import javax.swing.JScrollPane;
	import javax.swing.JTable;
	import javax.swing.JTextField;
	import javax.swing.RowFilter;
	import javax.swing.RowSorter;
	import javax.swing.event.DocumentEvent;
	import javax.swing.event.DocumentListener;
	import javax.swing.event.ListSelectionEvent;
	import javax.swing.event.ListSelectionListener;
	import javax.swing.event.PopupMenuEvent;
	import javax.swing.event.PopupMenuListener;
	import javax.swing.table.TableColumn;
	import javax.swing.table.TableColumnModel;
	import javax.swing.table.TableRowSorter;

import org.mapdb.Fun.Tuple2;

import controller.Controller;
import core.account.PublicKeyAccount;
import core.item.ItemCls;
import core.item.assets.AssetCls;
	import core.item.persons.PersonCls;
import core.item.unions.UnionCls;
import core.transaction.Transaction;
import core.voting.Poll;
import database.DBSet;
import gui.MainFrame;
	import gui.Main_Internal_Frame;
	import gui.RunMenu;
	import gui.Split_Panel;
	import gui.items.assets.IssueAssetPanel;
	import gui.items.assets.TableModelItemAssets;
import gui.items.statement.Statements_Vouch_Table_Model;
import gui.library.MTable;
import gui.library.Voush_Library_Panel;
import gui.models.Renderer_Boolean;
	import gui.models.Renderer_Left;
	import gui.models.Renderer_Right;
	import gui.models.WalletItemAssetsTableModel;
	import gui.models.WalletItemPersonsTableModel;
import gui.transaction.TransactionDetailsFactory;
import gui.voting.VoteFrame;
import lang.Lang;


	public class Records_My_SplitPanel extends Split_Panel{
	
		private static final long serialVersionUID = 2717571093561259483L;

		JScrollPane jScrollPane4;
	
	
		private RunMenu Search_run_menu;
		All_Records_Panel allVotingsPanel;
// для прозрачности
	     int alpha =255;
	     int alpha_int;
//	     VotingDetailPanel votingDetailsPanel ;
		
		
		public Records_My_SplitPanel(){
		
			
			this.leftPanel.setVisible(false);
			allVotingsPanel = new All_Records_Panel();
			this.jSplitPanel.setLeftComponent(allVotingsPanel);
			
			
			setName(Lang.getInstance().translate("My Records"));
		
		 // searthLabel_SearchToolBar_LeftPanel.setText(Lang.getInstance().translate("Search") +":  ");
			
			jScrollPane4 = new  JScrollPane();
			
		// not show buttons
			jToolBar_RightPanel.setVisible(false);
		//	toolBar_LeftPanel.setVisible(false);
			jButton1_jToolBar_RightPanel.setText("<HTML><B> "+ Lang.getInstance().translate("Record")+ "</></> ");
			jButton1_jToolBar_RightPanel.setBorderPainted(true);
			jButton1_jToolBar_RightPanel.setFocusable(true);
			jButton1_jToolBar_RightPanel.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED)));
			jButton1_jToolBar_RightPanel.setSize(120, 30);
			jButton1_jToolBar_RightPanel.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					onClick();
				}
			});	
			
			
			
			
			jButton2_jToolBar_RightPanel.setVisible(false);
			/*	
	// not show My filter
			searth_My_JCheckBox_LeftPanel.setVisible(false);
			
	//CREATE TABLE
			search_Table_Model = new TableModelPersons();
			search_Table = new JTable(this.search_Table_Model);
			TableColumnModel columnModel = search_Table.getColumnModel(); // read column model
			columnModel.getColumn(0).setMaxWidth((100));
		
	//Custom renderer for the String column;
			search_Table.setDefaultRenderer(Long.class, new Renderer_Right()); // set renderer
			search_Table.setDefaultRenderer(String.class, new Renderer_Left(search_Table.getFontMetrics(search_Table.getFont()),search_Table_Model.get_Column_AutoHeight())); // set renderer
		
	//CHECKBOX FOR FAVORITE
			TableColumn favoriteColumn = search_Table.getColumnModel().getColumn(TableModelPersons.COLUMN_FAVORITE);	
			favoriteColumn.setCellRenderer(new Renderer_Boolean()); 
			favoriteColumn.setMinWidth(50);
			favoriteColumn.setMaxWidth(50);
			favoriteColumn.setPreferredWidth(50);
	//Sorter
			 search_Sorter = new TableRowSorter<TableModelPersons>(this.search_Table_Model);
			search_Table.setRowSorter(search_Sorter);	
		
	// UPDATE FILTER ON TEXT CHANGE
//			searchTextField_SearchToolBar_LeftPanel.getDocument().addDocumentListener( new search_tab_filter());
	// SET VIDEO			
			jTable_jScrollPanel_LeftPanel.setModel(this.search_Table_Model);
			jTable_jScrollPanel_LeftPanel = search_Table;
			jScrollPanel_LeftPanel.setViewportView(jTable_jScrollPanel_LeftPanel);
	//		setRowHeightFormat(true);
	 */
	// Event LISTENER	
			
			allVotingsPanel.records_Table.getSelectionModel().addListSelectionListener(new search_listener());
		
//			search_Table.addMouseListener( new search_Mouse());
				
		/*	
			Timer timer = new Timer( 200, new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent arg0) {
					
					
					
					if (alpha <50) {
						
						Search_run_menu.setVisible(false);
						alpha = 50;
					}
			//	Search_run_menu.setBackground(new Color(0,204,102,alpha));	
			//		Search_run_menu.jButton1.setForeground(new Color(0,0,0,alpha));
			//		Search_run_menu.jButton2.setForeground(new Color(0,0,0,alpha));
			//		Search_run_menu.jButton3.setForeground(new Color(0,0,0,alpha));
			//		Search_run_menu.jButton1.setBackground( new Color(212,208,200,alpha));
					alpha = alpha - alpha_int;
					
					
					
					
				}
				
			});
			
			*/
				   

			//	timer.start();
			
			
			
				 

			Search_run_menu  = new RunMenu();
			
			Search_run_menu.setUndecorated(true);
		//	Search_run_menu.setBackground(new Color(0,204,102,255));
		//	Dimension dim = new Dimension(180,70);
	    //	Search_run_menu.setSize(dim);
	    	Search_run_menu.setPreferredSize(new Dimension(180,95));
	    	Search_run_menu.setVisible(false);
	    	Search_run_menu.jButton1.setText(Lang.getInstance().translate("Set Status"));
	   // 	aaa.jButton1.setBorderPainted(false);
	  //  	Search_run_menu.jButton1.setFocusPainted(true);
	 //  	Search_run_menu.jButton1.setFocusCycleRoot(true);
		Search_run_menu.jButton1.setContentAreaFilled(false);
		Search_run_menu.jButton1.setOpaque(false);
//		Search_run_menu.jButton1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
	    
	    	   	
	    	
	    	Search_run_menu.jButton2.setText(Lang.getInstance().translate("Confirm"));
	    	Search_run_menu.jButton2.setContentAreaFilled(false);
	    	Search_run_menu.jButton2.setOpaque(false);
	    	Search_run_menu.getContentPane().add(Search_run_menu.jButton2);
	    	
	    	Search_run_menu.jButton3.setContentAreaFilled(false);
	  //  	Search_run_menu.jButton3.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
	    	Search_run_menu.jButton3.setOpaque(false);
	    
	   
	    	Search_run_menu.pack();
	    
	  //  	Search_run_menu.addWindowFocusListener( new run_Menu_Search_Focus_Listener());
	 
		 
			
	
		   
	
		}
		
		
		/*
	// set favorite Search	
		void favorite_all(JTable personsTable){
			int row = personsTable.getSelectedRow();
			row = personsTable.convertRowIndexToModel(row);

			PersonCls person = search_Table_Model.getPerson(row);
			//new AssetPairSelect(asset.getKey());

			
				//CHECK IF FAVORITES
				if(Controller.getInstance().isItemFavorite(person))
				{
					
					Controller.getInstance().removeItemFavorite(person);
				}
				else
				{
					
					Controller.getInstance().addItemFavorite(person);
				}
					

				personsTable.repaint();

		}
	
	// filter search
		 class search_tab_filter implements DocumentListener {
				
				public void changedUpdate(DocumentEvent e) {
					onChange();
				}

				public void removeUpdate(DocumentEvent e) {
					onChange();
				}

				public void insertUpdate(DocumentEvent e) {
					onChange();
				}

				public void onChange() {

					// GET VALUE
					String search = searchTextField_SearchToolBar_LeftPanel.getText();

				 	// SET FILTER
					//tableModelPersons.getSortableList().setFilter(search);
					search_Table_Model.fireTableDataChanged();
					
					RowFilter filter = RowFilter.regexFilter(".*" + search + ".*", 1);
					((DefaultRowSorter) search_Sorter).setRowFilter(filter);
					
					search_Table_Model.fireTableDataChanged();
					
				}
			}
	*/	
	// listener select row	 
		 class search_listener implements ListSelectionListener  {
				@Override
				public void valueChanged(ListSelectionEvent arg0) {
						String dateAlive;
										String date_birthday;
										String message; 
						// устанавливаем формат даты
										SimpleDateFormat formatDate = new SimpleDateFormat("dd.MM.yyyy"); // HH:mm");
						//создаем объект персоны
										UnionCls union;
					Transaction voting = null;
					if (allVotingsPanel.records_Table.getSelectedRow() >= 0 ){
						voting = (Transaction) allVotingsPanel.records_model.getItem(allVotingsPanel.records_Table.convertRowIndexToModel(allVotingsPanel.records_Table.getSelectedRow()));
					
				//	Person_info_panel_001 info_panel = new Person_info_panel_001(voting, false);
					
	//				votingDetailsPanel = new VotingDetailPanel(voting, (AssetCls)allVotingsPanel.cbxAssets.getSelectedItem());
				//	votingDetailsPanel.setPreferredSize(new Dimension(jScrollPane_jPanel_RightPanel.getSize().width-50,jScrollPane_jPanel_RightPanel.getSize().height-50));
					//jScrollPane_jPanel_RightPanel.setHorizontalScrollBar(null);
	//				jScrollPane_jPanel_RightPanel.setViewportView(votingDetailsPanel);
					//jSplitPanel.setRightComponent(votingDetailsPanel);
					
					
									
										     //   TransactionDetailsFactory.getInstance().createTransactionDetail(transaction);
											  
											 JPanel panel = new JPanel();
										        panel.setLayout(new GridBagLayout());
										      //  panel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
												
												//TABLE GBC
												GridBagConstraints tableGBC = new GridBagConstraints();
												tableGBC.fill = GridBagConstraints.BOTH; 
												tableGBC.anchor = GridBagConstraints.FIRST_LINE_START;
												tableGBC.weightx = 1;
												tableGBC.weighty = 1;
												tableGBC.gridx = 0;	
												tableGBC.gridy= 0;	
											//	JPanel a = TransactionDetailsFactory.getInstance().createTransactionDetail(voting);
												panel.add(TransactionDetailsFactory.getInstance().createTransactionDetail(voting),tableGBC);
												
												  
										        Tuple2<BigDecimal, List<Tuple2<Integer, Integer>>> signs = DBSet.getInstance().getVouchRecordMap().get(voting.getBlockHeight(DBSet.getInstance()),voting.getSeqNo(DBSet.getInstance()));
										        GridBagConstraints gridBagConstraints = null;
										        if (signs != null){
										  	    
										        
										       
										  	    	
										        	JLabel  jLabelTitlt_Table_Sign = new JLabel(Lang.getInstance().translate("Signatures")+":");
											        gridBagConstraints = new java.awt.GridBagConstraints();
											        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
											        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
											        gridBagConstraints.weightx = 0.1;
											        gridBagConstraints.insets = new java.awt.Insets(12, 11, 0, 11);
											        gridBagConstraints.gridx = 0;
											        gridBagConstraints.gridy = 1;
											        panel.add(jLabelTitlt_Table_Sign, gridBagConstraints);
											  	  
											  	  
											  											
													
												        gridBagConstraints = new java.awt.GridBagConstraints();
												        gridBagConstraints.gridx = 0;
												        gridBagConstraints.gridy = 2;
												        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
												        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
												        gridBagConstraints.weightx = 1.0;
												        gridBagConstraints.weighty = 1.0;
												        panel. add(new  Voush_Library_Panel(voting), gridBagConstraints);
										     
										        }
											     
											        
											        
											        
												
												
											
										        jScrollPane_jPanel_RightPanel.setViewportView( panel);
											
											 
										}
									}
										
					
					
					
					
					
					
					
								
		
					
					
				
			
			}
		 
		 
		 
		 
		 
	/*
	// mouse listener		
		class  search_Mouse extends MouseAdapter {
			@Override
			public void mousePressed(MouseEvent e) {
				Point p = e.getPoint();
				int row = search_Table.rowAtPoint(p);
				if(e.getClickCount() == 2)
				{
		//			row = personsTable.convertRowIndexToModel(row);
		//			PersonCls person = tableModelPersons.getPerson(row);
		//			new PersonFrame(person);
					
				}
			
			//	if(e.getClickCount() == 1 & e.getButton() == e.BUTTON1)
					if( e.getButton() == MouseEvent.BUTTON1)
				{
					
					
					row = search_Table.convertRowIndexToModel(row);
					PersonCls person = search_Table_Model.getPerson(row);	
	//выводим меню всплывающее
					if(Controller.getInstance().isItemFavorite(person))
					{
						Search_run_menu.jButton3.setText(Lang.getInstance().translate("Remove Favorite"));
					}
					else
					{
						Search_run_menu.jButton3.setText(Lang.getInstance().translate("Add Favorite"));
					}
		//			alpha = 255;
					alpha_int = 5;
					Search_run_menu.setBackground(new Color(1,204,102,255));		
				    Search_run_menu.setLocation(e.getXOnScreen(), e.getYOnScreen());
				    Search_run_menu.repaint();
			        Search_run_menu.setVisible(true);		
		    
			    
			
				}
			}
			}
	

	*/
/*
		
		
		class run_Menu_Search_Focus_Listener implements WindowFocusListener{
			@Override
			public void windowGainedFocus(WindowEvent arg0) {
				alpha = 255;
			}
			@Override
			public void windowLostFocus(WindowEvent arg0) {
				Search_run_menu.setVisible(false);
			}
		};
		*/
		
		 public void onClick()
			{
				//GET SELECTED OPTION
				int row = allVotingsPanel.records_Table.getSelectedRow();
				if(row == -1)
				{
					row = 0;
				}
				row = allVotingsPanel.records_Table.convertRowIndexToModel(row);
				
				
		
			
				Transaction voting = null;
				if (allVotingsPanel.records_Table.getSelectedRow() >= 0 ) voting = (Transaction) allVotingsPanel.records_model.getItem(allVotingsPanel.records_Table.convertRowIndexToModel(allVotingsPanel.records_Table.getSelectedRow()));
				//	Person_info_panel_001 info_panel = new Person_info_panel_001(voting, false);
					
	//			new Voting_Dialog(voting, 0, (AssetCls)allVotingsPanel.cbxAssets.getSelectedItem());
			
			
			
			
			
			}
	
	
	}



