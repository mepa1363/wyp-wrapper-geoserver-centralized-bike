package org.geoserver.wps.gs;

import org.geotools.process.factory.DescribeParameter;
import org.geotools.process.factory.DescribeProcess;
import org.geotools.process.factory.DescribeResult;
import org.geotools.process.gs.GSProcess;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.HttpURLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@DescribeProcess(title = "OTP-WPS", description = "OTP WPS Demo")
public class Bikeshed_Centralized implements GSProcess {

	@DescribeResult(name = "BikeshedResult", description = "Generated bikeshed by WPS")
	public String execute(
			@DescribeParameter(name = "StartPoint", description = "Biking start point") String fromPlace,
			@DescribeParameter(name = "BikingPeriod", description = "Biking time period") String bikeTime,
			@DescribeParameter(name = "BikeshedOutput", description = "Biking type") String output) {
		return CallOTP(fromPlace, bikeTime, output);
	}

	public static String CallOTP(String fromPalce, String walkTime,
			String output) {
		URL url;
		String line;
		HttpURLConnection connection;
		StringBuilder sb = new StringBuilder();

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		Date date = new Date();
		String time = dateFormat.format(date);

		try {
			url = new URL(
					"http://gisciencegroup.ucalgary.ca:8080/opentripplanner-api-webapp/ws/iso?layers=traveltime&styles=mask&batch=true&fromPlace="
							+ fromPalce
							+ "&toPlace=51.09098935,-113.95179705&time="
							+ time
							+ "&mode=BICYCLE&maxWalkDistance=10000&walkTime="
							+ walkTime + "&output=" + output);
			connection = (HttpURLConnection) url.openConnection();

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
