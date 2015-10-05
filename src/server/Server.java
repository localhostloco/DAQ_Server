package server;

import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.osgi.framework.BundleContext;

import datamanager.DataManager;

public interface Server {

	void stopFromBrowser();
	
	void setDataManager(DataManager dm);
	
	DataManager getDataManager();
	
	Response stop(@QueryParam("stop") String stop, @Context UriInfo uriInfo, String content);
	
	void setContext(BundleContext context);
	
	BundleContext getContext();
}
