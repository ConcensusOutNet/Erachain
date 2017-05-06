package gui2;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Enumeration;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTree;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import gui.items.accounts.My_Accounts_SplitPanel;
import gui.items.mails.Incoming_Mails_SplitPanel;
import gui.items.mails.Mail_Send_Panel;
import gui.items.mails.Outcoming_Mails_SplitPanel;
import gui.items.persons.IssuePersonPanel;
import gui.items.persons.Persons_My_SplitPanel;
import gui.items.persons.Persons_Search_SplitPanel;
import gui.items.statement.Issue_Document_Panel;
import gui.items.statement.Statements_My_SplitPanel;
import gui.items.statement.Statements_Search_SplitPanel;
import gui.library.MSplitPane;
import lang.Lang;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ����
 */
public class Main_Panel extends javax.swing.JPanel {

	private MainLeftPanel mlp;

	/**
	 * Creates new form split_1
	 */
	public Main_Panel() {
		initComponents();
		jSplitPane1.M_setDividerSize(20);
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
	// <editor-fold defaultstate="collapsed" desc="Generated Code">
	private void initComponents() {
		java.awt.GridBagConstraints gridBagConstraints;

		jSplitPane1 = new MSplitPane();
		jTabbedPane1 = new M_TabbedPanel(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
		mlp = new MainLeftPanel();
		jTabbedPane1.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub
			
				
			}
			

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub
				Component cc = arg0.getComponent();
				if( cc.getClass().getSimpleName().equals("M_TabbedPanel")){
				M_TabbedPanel mt = (M_TabbedPanel)cc;	
				
		// find path from name node		
				DefaultMutableTreeNode ss = getNodeByName(jTabbedPane1.getTitleAt(mt.getSelectedIndex()), (DefaultMutableTreeNode) mlp.tree.tree.getModel().getRoot());
		// set select from tree
				if (ss != null)	mlp.tree.tree.setSelectionPath( new TreePath(ss.getPath()) );
				}	
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		mlp.tree.tree.addKeyListener(new KeyListener(){

			@Override
			public void keyPressed(KeyEvent arg0) {
				// TODO Auto-generated method stub
				if(arg0.getKeyCode() == KeyEvent.VK_ENTER){
					// find path from name node		
					
					dylay(mlp.tree.tree.getLastSelectedPathComponent().toString());					
				}
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyTyped(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			
			
			
		});
		
		setLayout(new java.awt.GridBagLayout());


		jSplitPane1.setRightComponent(jTabbedPane1);

		 
		mlp.tree.tree.addTreeSelectionListener(new TreeSelectionListener() {

			@Override
			public void valueChanged(TreeSelectionEvent e) {
				// TODO Auto-generated method stub
			
				
			}

		});
		
		mlp.tree.tree.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub
				if (arg0.getClickCount()==1){
					
				Component aa = arg0.getComponent();
				if( aa.getClass().getSimpleName().equals("JTree")){
				JTree tr = ((JTree) aa);	
				if (tr.getLastSelectedPathComponent()== null)return;
				dylay(tr.getLastSelectedPathComponent().toString());
					
				};
								}
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});

		/*
		 * mlp.jButton1.addActionListener(new ActionListener(){
		 * 
		 * @Override public void actionPerformed(ActionEvent arg0) { // TODO
		 * Auto-generated method stub
		 * 
		 * 
		 * int s = jTabbedPane1.indexOfTab("tab1");
		 * 
		 * 
		 * 
		 * 
		 * if (s==-1) { jTabbedPane1.addTabWithCloseButton("tab1", new
		 * JPanel()); s= jTabbedPane1.indexOfTab("tab1");
		 * 
		 * 
		 * } jTabbedPane1.setSelectedIndex(s); }
		 * 
		 * 
		 * });
		 */

		jSplitPane1.setLeftComponent(mlp);
		

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 0.1;
		gridBagConstraints.weighty = 0.1;
		add(jSplitPane1, gridBagConstraints);
		jSplitPane1.setDividerLocation(250);
	}// </editor-fold>

	// Variables declaration - do not modify
	
	private MSplitPane jSplitPane1;
	private M_TabbedPanel jTabbedPane1;
	// End of variables declaration
	
	private void dylay(String str){
		
		
		if(str.equals( Lang.getInstance().translate("My Persons"))){
			ccase1(Lang.getInstance().translate("My Persons"), new Persons_My_SplitPanel());
			return;
		}
		if ( str.equals( Lang.getInstance().translate("Search Persons"))){
			ccase1( Lang.getInstance().translate("Search Persons"), new Persons_Search_SplitPanel());
			return;
		}
		if ( str.equals( Lang.getInstance().translate("Issue Person"))){
			ccase1( Lang.getInstance().translate("Issue Person"), new IssuePersonPanel());
			return;

		}
		
		if ( str.equals( Lang.getInstance().translate("My Accounts"))){
			ccase1( Lang.getInstance().translate("My Accounts"), new My_Accounts_SplitPanel());
			return;

		}
		if ( str.equals( Lang.getInstance().translate("My Statements"))){
			ccase1( Lang.getInstance().translate("My Statements"), new Statements_My_SplitPanel());
			return;

		}
		if ( str.equals( Lang.getInstance().translate("Search Statements"))){
			ccase1( Lang.getInstance().translate("Search Statements"), new Statements_Search_SplitPanel());
			return;

		}
		if ( str.equals( Lang.getInstance().translate("Issue Statement"))){
			ccase1( Lang.getInstance().translate("Issue Statement"), new Issue_Document_Panel());
			return;

		}
		if ( str.equals( Lang.getInstance().translate("Incoming Mails"))){
			ccase1( Lang.getInstance().translate("Incoming Mails"), new Incoming_Mails_SplitPanel());
			return;
		}
		if ( str.equals( Lang.getInstance().translate("Outcoming Mails"))){
			ccase1( Lang.getInstance().translate("Outcoming Mails"), new Outcoming_Mails_SplitPanel());
			return;
		}
		if ( str.equals( Lang.getInstance().translate("Send Mail"))){
			ccase1( Lang.getInstance().translate("Send Mail"), new Mail_Send_Panel(null,null,null,null));
			return;
		}
			
		
	}
	private void ccase1(String str, JPanel pp){
		int s=-1;
		s = jTabbedPane1.indexOfTab(str);
		if (s == -1) {
			jTabbedPane1.addTabWithCloseButton(str, pp);
			s = jTabbedPane1.indexOfTab(str);
		}
		jTabbedPane1.setSelectedIndex(s);
		
		
		
	}
    private DefaultMutableTreeNode getNodeByName(String sNodeName, DefaultMutableTreeNode parent){
        if (parent != null)
            for (Enumeration e = parent.breadthFirstEnumeration(); e.hasMoreElements();){
                DefaultMutableTreeNode current = (DefaultMutableTreeNode) e.nextElement();
                if (sNodeName.equals(current.getUserObject())){
                    return current;
                }
            }
        return null;
    }
	
}