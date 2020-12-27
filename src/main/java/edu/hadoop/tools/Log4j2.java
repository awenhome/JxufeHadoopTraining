package edu.hadoop.tools;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.slf4j.Log4jLogger;

/**
 * @author 彭文忠
 * @date 2014年8月14日
 * @See 如果我们在工程(项目)中不提供log4j的配置文件，则log4j会使用默认的配置，级别为error 4中获取日志记录器的方式
 * @See2 本项目Log4j2配置文件为resources/log4j.properties
 */

public class Log4j2 {
	public static Logger getLogger(Class<?> clazz) {
		return LogManager.getLogger(clazz);
	}

	public static Logger getLogger(String className) {
		return LogManager.getLogger(className);
	}

	public static Logger getLogger() {
		return LogManager.getRootLogger();
	}

	/**
	 * @See 默认打印级别为INFo
	 * @param message
	 */
	public static void log(String message) {
		LogManager.getRootLogger().log(Level.INFO, message);
	}

	/**
	 * Logs a message object with the given level.
	 *
	 * @param level
	 *            the logging level
	 * @param message
	 *            the message object to log.
	 */
	public static void log(Level level, String message) {
		LogManager.getRootLogger().log(level, message);
	}

	/**
	 * Logs a message object with the given level.
	 *
	 * @param level
	 *            the logging level
	 * @param message
	 *            the message object to log.
	 */
	public static void log(Class<?> clazz, Level level, String message) {
		LogManager.getLogger(clazz).log(level, message);
	}

	/**
	 * Logs a message object with the given level.
	 *
	 * @param level
	 *            the logging level
	 * @param message
	 *            the message object to log.
	 */
	public static void log(String className, Level level, String message) {
		LogManager.getLogger(className).log(level, message);
	}

	public static void main(String[] args) {
		Log4j2.log("what wrong");
		Log4j2.log(Level.INFO, "发布任务成功，任务ID：" + "——id");
		String abc = null;
		Logger logger = Log4j2.getLogger(LogManager.ROOT_LOGGER_NAME);
		Logger log = Log4j2.getLogger(Log4j2.class);
		Logger l = Log4j2.getLogger(abc);
		Logger rl = Log4j2.getLogger();
		System.out.println("level：" + logger.getLevel());
		logger.log(Level.INFO, "333trace");
		logger.trace("1trace");
		logger.debug("2debug");
		logger.info("3info");
		logger.warn("4warn");
		logger.error("5error");
		logger.fatal("6fatal");

		log.trace("7trace");
		log.debug("8debug");
		log.info("9info");
		log.warn("10warn");
		log.error("11error");
		log.fatal("12fatal");

		l.trace("13trace");
		l.debug("14debug");
		l.info("15info");
		l.warn("16warn");
		l.error("17error");
		l.fatal("18fatal");

		rl.trace("19trace");
		rl.debug("20debug");
		rl.info("21info");
		rl.warn("22warn");
		rl.error("23error");
		rl.fatal("24fatal");
	}
}
