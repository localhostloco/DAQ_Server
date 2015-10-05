package server.tracker;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

import server.Server;
import datamanager.DataManager;


public class DataManagerTrackerCustomizer implements ServiceTrackerCustomizer {
	
	private BundleContext context;
	private static Server srv;
	
	public DataManagerTrackerCustomizer(BundleContext context) {
		this.context = context;
	}
	
	@Override
	public Object addingService(ServiceReference reference) {
		// TODO Auto-generated method stub
		DataManager dm = (DataManager) context.getService(reference);
		if (srv.getDataManager() == null) {
			srv.setDataManager(dm);
			System.out.println("<Server Bundle> DataManager detected and added!");
		}
		return dm;
	}

	@Override
	public void modifiedService(ServiceReference reference, Object service) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removedService(ServiceReference reference, Object service) {
		// TODO Auto-generated method stub
		srv.setDataManager(null);
		System.out.println("<Server Bundle> DataManager disconnected from the server!");
	}

	public static void setSrv(Server srv) {
		DataManagerTrackerCustomizer.srv = srv;
	}
}
