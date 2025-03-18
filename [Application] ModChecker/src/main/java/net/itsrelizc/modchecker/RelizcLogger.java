package net.itsrelizc.modchecker;

import java.awt.Component;
import java.io.OutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.*;

import javax.swing.JTextArea;

public class RelizcLogger {
    private static final Logger logger = Logger.getLogger(RelizcLogger.class.getName());
    
    public static JTextArea areaLogger;
    
    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    static {
        // Remove default handlers
        Logger rootLogger = Logger.getLogger("");
        Handler[] handlers = rootLogger.getHandlers();
        for (Handler handler : handlers) {
            rootLogger.removeHandler(handler);
        }
        
        

        // Create a custom console handler
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setFormatter(new Formatter() {
            

            @Override
            public String format(LogRecord record) {
                return format2(record);
            }
        });

        consoleHandler.setLevel(Level.ALL);
        logger.addHandler(consoleHandler);
        logger.setLevel(Level.ALL);

        // Redirect System.out to logger
        System.setOut(new PrintStream(new LoggerOutputStream(logger, Level.INFO), true));
    }
    
    private static String format2(LogRecord record) {
    	String timestamp = dateFormat.format(new Date(record.getMillis()));
        return String.format("[%s %s]: %s%n", timestamp, record.getLevel(), record.getMessage());
    }

    public static void log(Level level, String message) {
    	if (areaLogger != null) {
    		String timestamp = dateFormat.format(new Date(System.currentTimeMillis()));
            String message2 =  String.format("[%s %s]: %s%n", timestamp, level.toString().substring(0, 1), message);
        	
        	areaLogger.append(message2);
        	areaLogger.setCaretPosition(areaLogger.getDocument().getLength());
    	}
    	
    	
        logger.log(level, message);
    }
    
    public static void info(String message) {
    	log(Level.INFO, message);
    }
    
    public static void warn(String message) {
    	log(Level.WARNING, message);
    }
    
    public static void severe(String message) {
    	log(Level.SEVERE, message);
    }
}

// Helper class to redirect System.out
class LoggerOutputStream extends OutputStream {
    private final Logger logger;
    private final Level level;
    private final StringBuilder buffer = new StringBuilder();

    public LoggerOutputStream(Logger logger, Level level) {
        this.logger = logger;
        this.level = level;
    }

    @Override
    public void write(int b) {
        if (b == '\n') {
            logger.log(level, buffer.toString());
            if (RelizcLogger.areaLogger != null) {
        		String timestamp = RelizcLogger.dateFormat.format(new Date(System.currentTimeMillis()));
                String message2 =  String.format("[%s %s]: %s%n", timestamp, level.toString().substring(0, 1), buffer.toString());
            	
                RelizcLogger.areaLogger.append(message2);
                RelizcLogger.areaLogger.setCaretPosition(RelizcLogger.areaLogger.getDocument().getLength());
        	}
            buffer.setLength(0); // Clear buffer
        } else {
            buffer.append((char) b);
        }
    }
}
