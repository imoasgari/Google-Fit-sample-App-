package logger;

/**
 * Created by user on 30/06/2017.
 */

public interface LogNode {
	void println(int priority, String tag, String msg, Throwable tr);
}
