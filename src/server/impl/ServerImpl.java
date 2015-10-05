package server.impl;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.osgi.framework.BundleContext;

import server.Server;
import datalogger.DataLogger;
import datamanager.DataManager;

@Path("/DAQ_Server")
public class ServerImpl implements Server {

	private DataManager dm;
	private BundleContext context;

	public BundleContext getContext() {
		return context;
	}

	public void setContext(BundleContext context) {
		this.context = context;
	}

	@GET
	@Path("/stop")
	public Response stop(@QueryParam("stop") String stop,
			@Context UriInfo uriInfo, String content) {
		MultivaluedMap<String, String> queryParams = uriInfo
				.getQueryParameters();
		String stopParam = queryParams.getFirst("stop");
		System.out.println(stopParam);
		if (stopParam.equals("STOP")) {
			stopFromBrowser();
			System.out.println("ok!");
			return Response.ok("stop=" + stopParam + backButton()).build();
		} else {
			System.out.println("not ok!");
			return Response.serverError().build();
		}
	}

	@GET
	@Path("/period")
	public Response period(@QueryParam("period") int period) {
		System.out.println(period);
		if (period > 20 && dm != null) {
			System.out.println("<Server Bundle> " + "Previous period: "
					+ dm.getPeriod());
			dm.setPeriod(period);
			System.out.println("<Server Bundle> " + "Current period: "
					+ dm.getPeriod());
			return Response.ok("period=" + period + backButton()).build();
		} else {
			System.out.println("<Server Bundle> " + "Error setting period");
			return Response.serverError().build();
		}
	}

	@GET
	@Path("/home")
	public Response home() {
		System.out.println("<Server Bundle> " + "Home page");
		String s = "<h1> Hey! </h1>";
		if (dm == null) {
			s += "<h2> The DataManager is offline, try activating it first! </h2>";
			return Response.ok(s).build();
		}
		else {
			s += dataManagerInfo();
			s += dataManagerForms();
			return Response.ok(s).build();
		}
	}

	public void stopFromBrowser() {
		dm.STOP_RUNNING();
	}

	public DataManager getDataManager() {
		return dm;
	}

	public void setDataManager(DataManager dm) {
		this.dm = dm;
	}
	
	public String backButton() {
		String s = "";
		s += "<form action=\"home\">";
		s += "<input type=\"submit\" value=\"Go back\">";
		s += "</form>";
		return s;
	}
	
	public String stopDataManagerButton() {
		String s = "";
		s += "<form action=\"stop\">";
		s += "<input type=\"hidden\" name=\"stop\" value=\"STOP\">";
		s += "<input type=\"submit\" value=\"Stop DataManager\">";
		s += "</form>";
		return s;
	}
	
	public String dataManagerForms() {
		String s = "";
		s += "<form action=\"period\">";
		s += "<input type=\"text\" name=\"period\" value=\"" + dm.getPeriod() + "\">";
		s += "<input type=\"submit\" value=\"Change period\">";
		s += "</form>";
		return s;
	}
	
	public String dataManagerInfo() {
		String s = "";
		if (dm.getLoggers().size() > 0) {
			s += "<h2> The DataManager is online! Hooray! </h2>";
			s += "<table>";
				s += "<tr>";
					s += "<th>Logger</th>";
					s += "<th>Engineering Unit</th>";
					s += "<th>Data name</th>";
				s += "</tr>";
				for (DataLogger dl : dm.getLoggers()) {
					s += "<tr>";
					s += "<td>";
					s += dl.getDriverName();
					s += "</td>";
					s += "<td>";
					s += dl.getDataEngineeringUnit();
					s += "</td>";
					s += "<td>";
					s += dl.getDataName();
					s += "</td>";
					s += "</tr>";
				}
			s += "</table>";
		}
		else s += "<h3> No loggers online :( </h3>";
		s += stopDataManagerButton();
		s += "Period: " + dm.getPeriod() + "<br>";	
		return s;
	}
}
