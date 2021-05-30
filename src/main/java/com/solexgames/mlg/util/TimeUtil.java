package com.solexgames.mlg.util;

import org.apache.commons.lang.time.DurationFormatUtils;

/**
 * @author puugz
 * @since 30/05/2021 13:29
 */
public class TimeUtil {

	public static String millisToRoundedTime(long millis) {
		return DurationFormatUtils.formatDurationWords(millis, true, true);
	}

	public static String secondsToRoundedTime(int seconds) {
		return millisToRoundedTime(seconds * 1000L);
	}
}
