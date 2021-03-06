package gui.items.voting;

import gui.AccountRenderer;
import gui.PasswordPane;
import gui.items.ComboBoxModelItemsAll;
import gui.models.AccountsComboBoxModel;
import gui.models.OptionsComboBoxModel;
import lang.Lang;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import utils.DateTimeFormat;
import utils.Pair;
import controller.Controller;
import core.account.Account;
import core.account.PrivateKeyAccount;
import core.item.ItemCls;
import core.item.assets.AssetCls;
import core.transaction.Transaction;
import core.voting.Poll;
import core.voting.PollOption;
import database.DBSet;

@SuppressWarnings("serial")
public class Voting_Dialog extends JDialog
{
	private Poll poll;
	private JComboBox<Account> cbxAccount;
	private JComboBox<PollOption> cbxOptions;
	private JButton voteButton;
	private JTextField txtFeePow;
	private JComboBox<ItemCls> cbxAssets;
	
	public Voting_Dialog(Poll poll, int option, AssetCls asset)
	{
//		super(Lang.getInstance().translate("ARONICLE.com") + " - " + Lang.getInstance().translate("Vote"));
		
		this.poll = poll;
		
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
		
		
		//LABEL GBC
		GridBagConstraints labelGBC = new GridBagConstraints();
		labelGBC.insets = new Insets(0, 5, 5, 0);
		labelGBC.fill = GridBagConstraints.HORIZONTAL;   
		labelGBC.anchor = GridBagConstraints.NORTHWEST;
		labelGBC.weightx = 0;	
		labelGBC.gridx = 0;
		
		//DETAIL GBC
		GridBagConstraints detailGBC = new GridBagConstraints();
		detailGBC.insets = new Insets(0, 5, 5, 0);
		detailGBC.fill = GridBagConstraints.HORIZONTAL;  
		detailGBC.anchor = GridBagConstraints.NORTHWEST;
		detailGBC.weightx = 1;	
		detailGBC.gridwidth = 2;
		detailGBC.gridx = 1;		
		
		//LABEL NAME
		labelGBC.gridy = 1;
		JLabel nameLabel = new JLabel(Lang.getInstance().translate("Poll") + ":");
		this.add(nameLabel, labelGBC);
		
		//NAME
		detailGBC.gridy = 1;
		JTextField name = new JTextField(poll.getName());
		name.setEditable(false);
		this.add(name, detailGBC);		
		
		//LABEL DESCRIPTION
		labelGBC.gridy = 2;
		JLabel descriptionLabel = new JLabel(Lang.getInstance().translate("Description") + ":");
		this.add(descriptionLabel, labelGBC);
				
		//DESCRIPTION
		detailGBC.gridy = 2;
		JTextArea txtAreaDescription = new JTextArea(poll.getDescription());
		txtAreaDescription.setRows(4);
		txtAreaDescription.setBorder(name.getBorder());
		txtAreaDescription.setEditable(false);
		this.add(txtAreaDescription, detailGBC);		

		//ASSET LABEL GBC
		GridBagConstraints assetLabelGBC = new GridBagConstraints();
		assetLabelGBC.insets = new Insets(0, 5, 5, 0);
		assetLabelGBC.fill = GridBagConstraints.HORIZONTAL;   
		assetLabelGBC.anchor = GridBagConstraints.CENTER;
		assetLabelGBC.weightx = 0;	
		assetLabelGBC.gridwidth = 1;
		assetLabelGBC.gridx = 0;
		assetLabelGBC.gridy = 3;
		
		//ASSETS GBC
		GridBagConstraints assetsGBC = new GridBagConstraints();
		assetsGBC.insets = new Insets(0, 5, 5, 0);
		assetsGBC.fill = GridBagConstraints.HORIZONTAL;   
		assetsGBC.anchor = GridBagConstraints.NORTHWEST;
		assetsGBC.weightx = 0;	
		assetsGBC.gridwidth = 2;
		assetsGBC.gridx = 1;
		assetsGBC.gridy = 3;
		
		this.add(new JLabel(Lang.getInstance().translate("Check") + ":"), assetLabelGBC);
		
		cbxAssets = new JComboBox<ItemCls>(new ComboBoxModelItemsAll(ItemCls.ASSET_TYPE));
		cbxAssets.setSelectedItem(asset);
		this.add(cbxAssets, assetsGBC);
		
		cbxAssets.addActionListener (new ActionListener () {
		    public void actionPerformed(ActionEvent e) {

		    	AssetCls asset = ((AssetCls) cbxAssets.getSelectedItem());

		    	if(asset != null)
		    	{
		    		((AccountRenderer)cbxAccount.getRenderer()).setAsset(asset.getKey(DBSet.getInstance()));
		    		cbxAccount.repaint();
		    		cbxOptions.repaint();
		    		
		    	}
		    }
		});
		
        //LABEL ACCOUNT
      	labelGBC.gridy = 4;
      	JLabel ownerLabel = new JLabel(Lang.getInstance().translate("Account") + ":");
      	this.add(ownerLabel, labelGBC);
      		
      	//CBX ACCOUNT
      	detailGBC.gridy = 4;
      	this.cbxAccount = new JComboBox<Account>(new AccountsComboBoxModel());
      	cbxAccount.setRenderer(new AccountRenderer(asset.getKey(DBSet.getInstance())));
      	
      	this.add(this.cbxAccount, detailGBC);
		
      	//LABEL OPTIONS
      	labelGBC.gridy = 5;
      	JLabel optionsLabel = new JLabel(Lang.getInstance().translate("Option") + ":");
      	this.add(optionsLabel, labelGBC);
      		
      	//CBX ACCOUNT
      	detailGBC.gridy = 5;
      	this.cbxOptions = new JComboBox<PollOption>(new OptionsComboBoxModel(poll.getOptions()));
      	this.cbxOptions.setSelectedIndex(option);
      	this.cbxOptions.setRenderer(new DefaultListCellRenderer() {
      	    @SuppressWarnings("rawtypes")
			@Override
      	    public Component getListCellRendererComponent(JList list,
      	                                               Object value,
      	                                               int index,
      	                                               boolean isSelected,
      	                                               boolean cellHasFocus) {
      	    	PollOption employee = (PollOption)value;
      	        
      	    	AssetCls asset = ((AssetCls) cbxAssets.getSelectedItem());
      	    	
      	    	value = employee.toString(asset.getKey(DBSet.getInstance()));
      	        return super.getListCellRendererComponent(list, value,
      	                index, isSelected, cellHasFocus);
      	    }
      	});
      	
      	this.add(this.cbxOptions, detailGBC);
      	
      	 //LABEL FEE
      	labelGBC.gridy = 6;
      	JLabel feeLabel = new JLabel(Lang.getInstance().translate("Fee Power") + ":");
      	this.add(feeLabel, labelGBC);
      		
      	//TXT FEE
      	detailGBC.gridy = 6;
      	this.txtFeePow = new JTextField();
      	this.txtFeePow.setText("1");
        this.add(this.txtFeePow, detailGBC);
		
		//ADD EXCHANGE BUTTON
		detailGBC.gridy = 7;
		voteButton = new JButton(Lang.getInstance().translate("Vote"));
		voteButton.setPreferredSize(new Dimension(100, 25));
		voteButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				onVoteClick();
			}
		});	
		this.add(voteButton, detailGBC);
		
		 
		 setPreferredSize(new java.awt.Dimension(800, 650));
		 setMinimumSize(new java.awt.Dimension(650, 23));
		  setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setModal(true);
		//PACK
		this.pack();
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        
      
      
      
       
        
        
	}
	
	public void onVoteClick()
	{
		//DISABLE
		this.voteButton.setEnabled(false);
	
		//CHECK IF NETWORK OK
		if(false && Controller.getInstance().getStatus() != Controller.STATUS_OK)
		{
			//NETWORK NOT OK
			JOptionPane.showMessageDialog(null, Lang.getInstance().translate("You are unable to send a transaction while synchronizing or while having no connections!"), Lang.getInstance().translate("Error"), JOptionPane.ERROR_MESSAGE);
			
			//ENABLE
			this.voteButton.setEnabled(true);
			
			return;
		}
		
		//CHECK IF WALLET UNLOCKED
		if(!Controller.getInstance().isWalletUnlocked())
		{
			//ASK FOR PASSWORD
			String password = PasswordPane.showUnlockWalletDialog(this); 
			if(!Controller.getInstance().unlockWallet(password))
			{
				//WRONG PASSWORD
				JOptionPane.showMessageDialog(null, Lang.getInstance().translate("Invalid password"), Lang.getInstance().translate("Unlock Wallet"), JOptionPane.ERROR_MESSAGE);
				
				//ENABLE
				this.voteButton.setEnabled(true);
				
				return;
			}
		}
		
		//READ CREATOR
		Account sender = (Account) cbxAccount.getSelectedItem();
		
		try
		{
			//READ FEE
			int feePow = Integer.parseInt(txtFeePow.getText());
					
			//CREATE POLL
			PrivateKeyAccount creator = Controller.getInstance().getPrivateKeyAccountByAddress(sender.getAddress());
			PollOption option = (PollOption) this.cbxOptions.getSelectedItem();
			
			Pair<Transaction, Integer> result = Controller.getInstance().createPollVote(creator, poll, option, feePow);
			
			//CHECK VALIDATE MESSAGE
			switch(result.getB())
			{
			case Transaction.VALIDATE_OK:
				
				JOptionPane.showMessageDialog(new JFrame(), Lang.getInstance().translate("Poll vote has been sent!"), Lang.getInstance().translate("Success"), JOptionPane.INFORMATION_MESSAGE);
				this.dispose();
				break;	
				
				/*
			case Transaction.NOT_YET_RELEASED:
				
				JOptionPane.showMessageDialog(new JFrame(), Lang.getInstance().translate("Voting will be enabled at ") + DateTimeFormat.timestamptoString(Transaction.getVOTING_RELEASE()) + "!",  "Error", JOptionPane.ERROR_MESSAGE);
				break;
				*/
			case Transaction.ALREADY_VOTED_FOR_THAT_OPTION:
				
				JOptionPane.showMessageDialog(new JFrame(), Lang.getInstance().translate("You have already voted for that option!"), Lang.getInstance().translate("Error"), JOptionPane.ERROR_MESSAGE);
				break;	
				
			case Transaction.NOT_ENOUGH_FEE:
				
				JOptionPane.showMessageDialog(new JFrame(), Lang.getInstance().translate("Not enough %fee% balance!").replace("%fee%", AssetCls.FEE_NAME), Lang.getInstance().translate("Error"), JOptionPane.ERROR_MESSAGE);
				break;	
								
			case Transaction.NO_BALANCE:
			
				JOptionPane.showMessageDialog(new JFrame(), Lang.getInstance().translate("Not enough balance!"), Lang.getInstance().translate("Error"), JOptionPane.ERROR_MESSAGE);
				break;	
						
			default:
				
				JOptionPane.showMessageDialog(new JFrame(), Lang.getInstance().translate("Unknown error!"), Lang.getInstance().translate("Error"), JOptionPane.ERROR_MESSAGE);
				break;		
				
			}
		}
		catch(Exception e)
		{
			JOptionPane.showMessageDialog(new JFrame(), Lang.getInstance().translate("Invalid fee!"), Lang.getInstance().translate("Error"), JOptionPane.ERROR_MESSAGE);
		}
		
		//ENABLE
		this.voteButton.setEnabled(true);
	}
}
