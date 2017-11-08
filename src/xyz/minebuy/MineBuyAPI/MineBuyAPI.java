package xyz.minebuy.MineBuyAPI;

import java.net.URL;

public final class MineBuyAPI {
	
	private URL url;

	public MineBuyAPI(URL url) throws MineBuyExpection{
		if(checkURL()) {
			// *TODO*
		} else throw new MineBuyExpection("The API KEY or the URL is not valid");
	}
	
	public boolean checkURL() {
		// *TODO*
		return url != null;
	}

}