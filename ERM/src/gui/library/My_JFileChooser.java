package gui.library;

import javax.swing.JFileChooser;
import javax.swing.UIManager;

import lang.Lang;

public class My_JFileChooser extends JFileChooser{

// настройка диалога файлового на русский язык
	public My_JFileChooser () {
		UIManager.put("FileChooser.openButtonText", Lang.getInstance().translate("Open"));
		UIManager.put("FileChooser.cancelButtonText", Lang.getInstance().translate( "Cancel"));
		UIManager.put("FileChooser.lookInLabelText",Lang.getInstance().translate( "Look in"));
		UIManager.put("FileChooser.fileNameLabelText", Lang.getInstance().translate("File Name"));
		UIManager.put("FileChooser.filesOfTypeLabelText", Lang.getInstance().translate("File Type"));

		UIManager.put("FileChooser.saveButtonText",Lang.getInstance().translate( "Save"));
		UIManager.put("FileChooser.saveButtonToolTipText",Lang.getInstance().translate( "Save"));
		UIManager.put("FileChooser.openButtonText", Lang.getInstance().translate("Open"));
		UIManager.put("FileChooser.openButtonToolTipText", Lang.getInstance().translate("Open"));
		UIManager.put("FileChooser.cancelButtonText", Lang.getInstance().translate("Cancel"));
		UIManager.put("FileChooser.cancelButtonToolTipText",Lang.getInstance().translate( "Cancel"));

		UIManager.put("FileChooser.lookInLabelText",Lang.getInstance().translate( "Folder"));
		UIManager.put("FileChooser.saveInLabelText", Lang.getInstance().translate("Folder"));
		UIManager.put("FileChooser.fileNameLabelText", Lang.getInstance().translate("File Name"));
		UIManager.put("FileChooser.filesOfTypeLabelText", Lang.getInstance().translate("File Type"));

		UIManager.put("FileChooser.upFolderToolTipText", Lang.getInstance().translate("UP Folder"));
		UIManager.put("FileChooser.newFolderToolTipText", Lang.getInstance().translate("New Folder"));
		UIManager.put("FileChooser.listViewButtonToolTipText", Lang.getInstance().translate("List View"));
		UIManager.put("FileChooser.detailsViewButtonToolTipText", Lang.getInstance().translate("Details View"));
		UIManager.put("FileChooser.fileNameHeaderText",  Lang.getInstance().translate("Name"));
		UIManager.put("FileChooser.fileSizeHeaderText",  Lang.getInstance().translate("Size"));
		UIManager.put("FileChooser.fileTypeHeaderText",  Lang.getInstance().translate("Type"));
		UIManager.put("FileChooser.fileDateHeaderText",  Lang.getInstance().translate("File Date"));
		UIManager.put("FileChooser.fileAttrHeaderText",  Lang.getInstance().translate("File Attr"));

		UIManager.put("FileChooser.acceptAllFileFilterText",  Lang.getInstance().translate("All Files"));
		this.updateUI();

		} 


}