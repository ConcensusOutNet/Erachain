package gui.items;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import gui.CoreRowSorter;
import gui.items.ItemFrame;
import lang.Lang;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableColumn;

import core.item.ItemCls;

@SuppressWarnings("serial")
public class AllItemsFrame extends JFrame{
	
	private TableModelItems tableModelItems;

	public AllItemsFrame(String title) {
		
		super(Lang.getInstance().translate("ARONICLE.com") + " - " + Lang.getInstance().translate(title));
		
		//ICON
		List<Image> icons = new ArrayList<Image>();
		icons.add(Toolkit.getDefaultToolkit().getImage("images/icons/icon16.png"));
		icons.add(Toolkit.getDefaultToolkit().getImage("images/icons/icon32.png"));
		icons.add(Toolkit.getDefaultToolkit().getImage("images/icons/icon64.png"));
		icons.add(Toolkit.getDefaultToolkit().getImage("images/icons/icon128.png"));
		this.setIconImages(icons);
		
		//CLOSE
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		//LAYOUT
		this.setLayout(new GridBagLayout());
		
		//PADDING
		((JComponent) this.getContentPane()).setBorder(new EmptyBorder(5, 5, 5, 5));
		
		//SEACH LABEL GBC
		GridBagConstraints searchLabelGBC = new GridBagConstraints();
		searchLabelGBC.insets = new Insets(0, 5, 5, 0);
		searchLabelGBC.fill = GridBagConstraints.HORIZONTAL;   
		searchLabelGBC.anchor = GridBagConstraints.NORTHWEST;
		searchLabelGBC.weightx = 0;	
		searchLabelGBC.gridwidth = 1;
		searchLabelGBC.gridx = 0;
		searchLabelGBC.gridy = 0;
		
		//SEACH GBC
		GridBagConstraints searchGBC = new GridBagConstraints();
		searchGBC.insets = new Insets(0, 5, 5, 0);
		searchGBC.fill = GridBagConstraints.HORIZONTAL;   
		searchGBC.anchor = GridBagConstraints.NORTHWEST;
		searchGBC.weightx = 1;	
		searchGBC.gridwidth = 1;
		searchGBC.gridx = 1;
		searchGBC.gridy = 0;
		
		//TABLE GBC
		GridBagConstraints tableGBC = new GridBagConstraints();
		tableGBC.insets = new Insets(0, 5, 5, 0);
		tableGBC.fill = GridBagConstraints.BOTH;  
		tableGBC.anchor = GridBagConstraints.NORTHWEST;
		tableGBC.weightx = 1;	
		tableGBC.weighty = 1;	
		tableGBC.gridwidth = 2;
		tableGBC.gridx = 0;	
		tableGBC.gridy = 1;	
		
		//CREATE TABLE
		this.tableModelItems = new TableModelItems();
		final JTable itemsTable = new JTable(this.tableModelItems);

		/*
		//CHECKBOX FOR DIVISIBLE
		TableColumn divisibleColumn = itemsTable.getColumnModel().getColumn(ItemsTableModel.COLUMN_DIVISIBLE);
		divisibleColumn.setCellRenderer(itemsTable.getDefaultRenderer(Boolean.class));
		*/
		
		//ASSETS SORTER
		Map<Integer, Integer> indexes = new TreeMap<Integer, Integer>();
		CoreRowSorter sorter = new CoreRowSorter(this.tableModelItems, indexes);
		itemsTable.setRowSorter(sorter);
		
		//CREATE SEARCH FIELD
		final JTextField txtSearch = new JTextField();

		// UPDATE FILTER ON TEXT CHANGE
		txtSearch.getDocument().addDocumentListener(new DocumentListener() {
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
				String search = txtSearch.getText();

			 	// SET FILTER
				tableModelItems.getSortableList().setFilter(search);
				tableModelItems.fireTableDataChanged();
			}
		});

		// MENU
		JPopupMenu nameSalesMenu = new JPopupMenu();
		JMenuItem details = new JMenuItem(Lang.getInstance().translate("Details"));
		details.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int row = itemsTable.getSelectedRow();
				row = itemsTable.convertRowIndexToModel(row);

				ItemCls item = tableModelItems.getItem(row);
				new ItemFrame(item);
			}
		});
		nameSalesMenu.add(details);

		itemsTable.setComponentPopupMenu(nameSalesMenu);
		itemsTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				Point p = e.getPoint();
				int row = itemsTable.rowAtPoint(p);
				itemsTable.setRowSelectionInterval(row, row);
				
				if(e.getClickCount() == 2)
				{
					row = itemsTable.convertRowIndexToModel(row);
					ItemCls item = tableModelItems.getItem(row);
					new ItemFrame(item);
				}
			}
		});

		this.add(new JLabel(Lang.getInstance().translate("search") + ":"), searchLabelGBC);
		this.add(txtSearch, searchGBC);
		this.add(new JScrollPane(itemsTable), tableGBC);
		
		//PACK
		this.pack();
		//this.setSize(500, this.getHeight());
		this.setResizable(true);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	public void removeObservers() {
		this.tableModelItems.removeObservers();
	}
}
