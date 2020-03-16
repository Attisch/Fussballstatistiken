package Programme;

import Allgemein.Verwendbare;

/**
 * @author Attila Kiss
 */
public class DownloadsEinordnen {

	private DownloadsEinordnen() {}

	public static void main(String[] args) {
		Speicherzugriff.Datei.downloadsEinordnen(Verwendbare.DOWNLOADS_VON, Verwendbare.DOWNLOADS_NACH);
	}

}
