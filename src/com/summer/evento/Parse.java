package com.summer.evento;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parse {
	public static String parseLocation(String recognizedText) {
		Pattern p_loc = Pattern.compile("[0-9]{4}\\s*(BBB|EECS|DOW|FXB)|Dude Connector");
		Matcher m_loc = p_loc.matcher(recognizedText);
		if (m_loc.find())
			return m_loc.group();
		else
			return "";
	}

	public static String parseDate(String recognizedText) {
		Pattern[] p_dates = {Pattern.compile("(january|jan)\\s*[0-9]{1,2}"),
							Pattern.compile("(february|feb)\\s*[0-9]{1,2}"),
							Pattern.compile("(march|mar)\\s*[0-9]{1,2}"),
							Pattern.compile("(april|apr)\\s*[0-9]{1,2}"),
							Pattern.compile("(may)\\s*[0-9]{1,2}"),
							Pattern.compile("(jun|june)\\s*[0-9]{1,2}"),
							Pattern.compile("(jul|july)\\s*[0-9]{1,2}"),
							Pattern.compile("(august|aug)\\s*[0-9]{1,2}"),
							Pattern.compile("(september|sep)\\s*[0-9]{1,2}"),
							Pattern.compile("(october|oct)\\s*[0-9]{1,2}"),
							Pattern.compile("(november|nov)\\s*[0-9]{1,2}"),
							Pattern.compile("(december|dec)\\s*[0-9]{1,2}")};

		Calendar time = Calendar.getInstance();
		time.setTimeZone(TimeZone.getTimeZone("EST"));
		time.setTimeInMillis(System.currentTimeMillis());	// set to current time
		DateFormat format_date = DateFormat.getDateInstance();

		// find year
		Pattern p_year = Pattern.compile("201[0-9]");
		Matcher m_year = p_year.matcher(recognizedText);
		if (m_year.find())
			time.set(Calendar.YEAR, Integer.parseInt(m_year.group()));

		// find month and day
		for (int month = 0; month < 12; ++month) {
			// month
			Matcher m_date = p_dates[month].matcher(recognizedText.toLowerCase());
			if (m_date.find()) {
				time.set(Calendar.MONTH, month);
				// day
				Pattern p_day = Pattern.compile("[0-3]{0,1}[0-9]{1}");
				Matcher m_day = p_day.matcher(m_date.group());
				if (m_day.find())
					time.set(Calendar.DAY_OF_MONTH, Integer.parseInt(m_day.group()));
			}
		}
		
		return format_date.format(time.getTime());
	}
	
	public static String[] parseTime(String recognizedText) {
		String[] times = new String[2];
		Pattern p_time = Pattern.compile("[0-9]{1,2}\\s*[:.\\s]{1,2}\\s*[0-9]{0,2}[AaPp\\s]{1}");
		Matcher m_time = p_time.matcher(recognizedText);
		for (int i = 0; m_time.find() && i < 2; ++i) {
			times[i] = m_time.group().replaceAll("[:.]{1}", ":").replaceAll("\\s+", "");
		}
		Pattern p_ampm = Pattern.compile("[0-9]{1}+\\s*[aApP]+[mM]+");
		Matcher m_ampm = p_ampm.matcher(recognizedText);
		if (m_ampm.find()) {
			if (m_ampm.group().contains("a") || m_ampm.group().contains("A")) {	// TODO start & end time may have different am/pm
				times[0] += " AM";
				times[1] += " AM";
			}
			else if (m_ampm.group().contains("p") || m_ampm.group().contains("P")) {
				times[0] += " PM";
				times[1] += " PM";
			}
		}
		return times;
	}
}
