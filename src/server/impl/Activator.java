package server.impl;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.util.tracker.ServiceTracker;

import datalogger.DataLogger;
import datamanager.DataManager;
import server.tracker.DataManagerTrackerCustomizer;

public class Activator implements BundleActivator {

	private ServiceRegistration registration;
	private ServiceTracker tracker;

	@Override
	public void start(BundleContext context) throws Exception {
		// TODO Auto-generated method stub
		try {
			ServerImpl servicio = new ServerImpl();
			servicio.setContext(context);
			registration = context.registerService(ServerImpl.class, servicio,
					null);
			DataManagerTrackerCustomizer customizer = new DataManagerTrackerCustomizer(context);
			customizer.setSrv(servicio);
			tracker = new ServiceTracker<>(context,
					DataManager.class.getName(), customizer);
			tracker.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		// TODO Auto-generated method stub
		registration.unregister();
		tracker.close();
	}

}
