package org.geoserver.wps.gs;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.geotools.process.factory.DescribeParameter;
import org.geotools.process.factory.DescribeProcess;
import org.geotools.process.factory.DescribeResult;
import org.geotools.process.gs.GSProcess;

@DescribeProcess(title = "Aggreagtaion-WPS", description = "Aggregation WPS - Network-based approach (Bikeshed)")
public class Aggregation_Bike_Centralized implements GSProcess {

	@DescribeResult(name = "AggregationResult", description = "Aggregated data")
	public String execute(
			@DescribeParameter(name = "POI", description = "POI data") String poi,
			@DescribeParameter(name = "Crime", description = "Crime data") String crime,
			@DescribeParameter(name = "StartPoint", description = "Biking start point") String start_point,
			@DescribeParameter(name = "Bikeshed", description = "Bikeshed") String bikeshed,
			@DescribeParameter(name = "BikingTimePeriod", description = "Biking time period") String biking_time_period,
			@DescribeParameter(name = "DistanceDecayFunction", description = "Distance Decay Function") String distance_decay_function) {
		return CallAggregationService(start_point, bikeshed, poi, crime,
				biking_time_period, distance_decay_function);
	}

	public static String CallAggregationService(String start_point,
			String bikeshed, String poi, String crime,
			String biking_time_period, String distance_decay_function) {
		URL url;
		String line;
		HttpURLConnection connection;
		StringBuilder sb = new StringBuilder();
		String url_string;
		String url_parameters;
		String USER_AGENT = "Mozilla/5.0";
		DataOutputStream wr;

		try {
			url_string = "http://127.0.0.1:7364/aggregation";
			url_parameters = "start_point=" + start_point + "&bikeshed="
					+ bikeshed + "&poi=" + poi + "&crime=" + crime
					+ "&biking_time_period=" + biking_time_period
					+ "&distance_decay_function=" + distance_decay_function;

			url = new URL(url_string);

			connection = (HttpURLConnection) url.openConnection();

			connection.setRequestMethod("POST");
			connection.setRequestProperty("User-Agent", USER_AGENT);
			connection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

			connection.setDoOutput(true);
			wr = new DataOutputStream(connection.getOutputStream());
			wr.writeBytes(url_parameters);
			wr.flush();
			wr.close();

			BufferedReader rd = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));

			while ((line = rd.readLine()) != null) {
				sb.append(line);
			}

			rd.close();

			connection.disconnect();

		} catch (Exception e) {
			System.out.println("Errors...");
		}

		return sb.toString();
	}
}
