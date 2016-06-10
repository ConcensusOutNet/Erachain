package gui.items.imprints;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.DefaultRowSorter;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.RowSorter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;

import core.item.imprints.ImprintCls;
import gui.CoreRowSorter;
import gui.Split_Panel;
import gui.items.unions.TableModelUnions;
import gui.models.Renderer_Boolean;
import gui.models.Renderer_Left;
import gui.models.Renderer_Right;
import gui.models.WalletItemImprintsTableModel;
import lang.Lang;

public class Search_Imprints_Tab extends Split_Panel{

private TableModelImprints tableModelImprints;

public Search_Imprints_Tab(){

	setName(Lang.getInstance().translate("Search Imprints"));
	searthLabel_SearchToolBar_LeftPanel.setText(Lang.getInstance().translate("Search") +":  ");
// not show buttons
	button1_ToolBar_LeftPanel.setVisible(false);
	button2_ToolBar_LeftPanel.setVisible(false);
	jButton1_jToolBar_RightPanel.setVisible(false);
	jButton2_jToolBar_RightPanel.setVisible(false);
	
//CREATE TABLE
	this.tableModelImprints = new TableModelImprints();
	final JTable imprintsTable = new JTable(this.tableModelImprints);

//Custom renderer for the String column;
	imprintsTable.setDefaultRenderer(Long.class, new Renderer_Right()); // set renderer
	imprintsTable.setDefaultRenderer(String.class, new Renderer_Left()); // set renderer
	imprintsTable.setDefaultRenderer(Boolean.class, new Renderer_Boolean()); // set renderer
//CHECKBOX FOR FAVORITE
//	TableColumn favoriteColumn = imprintsTable.getColumnModel().getColumn(TableModelUnions.COLUMN_FAVORITE);
//	favoriteColumn.setCellRenderer(new Renderer_Boolean()); //unionsTable.getDefaultRenderer(Boolean.class));
//	favoriteColumn.setMinWidth(50);
//	favoriteColumn.setMaxWidth(50);
//	favoriteColumn.setPreferredWidth(50);//.setWidth(30);
// column #1
	TableColumn column1 = imprintsTable.getColumnModel().getColumn(WalletItemImprintsTableModel.COLUMN_KEY);//.COLUMN_CONFIRMED);
	column1.setMinWidth(1);
	column1.setMaxWidth(1000);
	column1.setPreferredWidth(50);
//Sorter
	RowSorter sorter =   new TableRowSorter(this.tableModelImprints);
	imprintsTable.setRowSorter(sorter);	
// UPDATE FILTER ON TEXT CHANGE
	searchTextField_SearchToolBar_LeftPanel.getDocument().addDocumentListener(new DocumentListener() {
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
			tableModelImprints.fireTableDataChanged();
			RowFilter filter = RowFilter.regexFilter(".*" + search + ".*", 1);
			((DefaultRowSorter) sorter).setRowFilter(filter);
			tableModelImprints.fireTableDataChanged();
							
		}
	});
			
// set showvideo			
	jTable_jScrollPanel_LeftPanel.setModel(this.tableModelImprints);
	jTable_jScrollPanel_LeftPanel = imprintsTable;
	jScrollPanel_LeftPanel.setViewportView(jTable_jScrollPanel_LeftPanel);
	
// MENU
	JPopupMenu nameSalesMenu = new JPopupMenu();
	JMenuItem details = new JMenuItem(Lang.getInstance().translate("Details"));
	details.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			int row = imprintsTable.getSelectedRow();
			row = imprintsTable.convertRowIndexToModel(row);
			ImprintCls imprint = tableModelImprints.getImprint(row);
			new ImprintFrame(imprint);
		}
	});
	nameSalesMenu.add(details);
	imprintsTable.setComponentPopupMenu(nameSalesMenu);
	imprintsTable.addMouseListener(new MouseAdapter() {
	@Override
		public void mousePressed(MouseEvent e) {
			Point p = e.getPoint();
			int row = imprintsTable.rowAtPoint(p);
			imprintsTable.setRowSelectionInterval(row, row);
			if(e.getClickCount() == 2)
			{
				row = imprintsTable.convertRowIndexToModel(row);
				ImprintCls imprint = tableModelImprints.getImprint(row);
				new ImprintFrame(imprint);
			}
		}
	});
	
}
}