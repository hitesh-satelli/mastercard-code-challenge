package com.assignment.mastercard;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApplicationConfiguration {
	//REST API which listens to the below url with 2 request parameters namely origin and destination
	@RequestMapping("/connected")
	public String hello(@RequestParam("origin") String origin, @RequestParam("destination") String destination) {

		System.out.println(origin);
		System.out.println(destination);
		File file = new File("city.txt");

		Scanner scan = null;

		Map<String, String> citiesMap = new HashMap<String, String>();
		try {
			scan = new Scanner(file);
			while (scan.hasNextLine()) {
				String line = scan.nextLine();
				String[] tokens = line.split(",");
				//If the origin-destination is a pair in the file, return true as there is a direct connection between these two cities
				if (tokens[0].equalsIgnoreCase(origin) && tokens[1].equalsIgnoreCase(destination))
					return "Yes";
				//If the destination-origin is a pair in the file, return true as there is a direct connection between these two cities
				if (tokens[0].equalsIgnoreCase(destination) && tokens[1].equalsIgnoreCase(origin))
					return "Yes";
				citiesMap.put(tokens[0], tokens[1]);
			}
		} catch (IOException e) {
			System.out.println(e);
		} finally {
			scan.close();
		}
		return getConnection(citiesMap, origin, destination);
	}

	//Checking for indirect connection between origin-destination from the file
	private String getConnection(Map<String, String> citiesMap, String origin, String destination) {

		String str = citiesMap.get(origin);
		String str1 = citiesMap.get(destination);
		while (str != null || str1 != null) {
			if (citiesMap.get(str) != null) {
				if (origin.equalsIgnoreCase(citiesMap.get(str)) || destination.equalsIgnoreCase(citiesMap.get(str)))
					return "Yes";
			}
			if (citiesMap.get(str1) != null) {
				if (origin.equalsIgnoreCase(citiesMap.get(str1)) || destination.equalsIgnoreCase(citiesMap.get(str1)))
					return "Yes";
			} else
				return "No";
		}

		return "No";
	}
}
