package scraper.view.control;

import java.awt.*;
import java.awt.datatransfer.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public interface IClipboard<T> extends ClipboardOwner {
	
	Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
	
	void handleCopy(List<T> listToCopy);
	
	void handlePaste();
	
	default void copy(String toCopy) {
		StringSelection stringSelection = new StringSelection(toCopy);
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(stringSelection, this);
	}
	
	default List<String> paste() {
		String clipboardText = null;
		Transferable contents = clipboard.getContents(null);
		
		if (contents != null && contents.isDataFlavorSupported(DataFlavor.stringFlavor)) {
			try {
				clipboardText = (String) contents.getTransferData(DataFlavor.stringFlavor);
			}
			catch (Exception ignored){}
		}
		
		if (clipboardText != null) {
			String[] clipboardTextArray = clipboardText.split("\\n");
			return Arrays.stream(clipboardTextArray).collect(Collectors.toList());
		} else {
			return new ArrayList<>();
		}
	}
	
	@Override default void lostOwnership(Clipboard aClipboard, Transferable aContents){}
}
