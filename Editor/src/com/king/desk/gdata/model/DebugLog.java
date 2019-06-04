package com.king.desk.gdata.model;

/**
 * 日志打印工具
 * 
 * @author zhouby
 *
 */
public class DebugLog {
	 
	/**控制是否打印日志**/
	private static boolean isDebug=true;
	/**类名**/
	private static String className;
	/**方法名**/
	private static String methodName;
	
    private DebugLog(){
        /* Protect from instantiations */
    }
 
    private static String createLog(String log){
 
        StringBuffer buffer = new StringBuffer();
        buffer.append("[");
        buffer.append(methodName);
        buffer.append("]");
        buffer.append(log);
 
        return buffer.toString();
    }
 
    private static void getMethodNames(StackTraceElement[] sElements){
        className = sElements[1].getFileName();
        methodName = sElements[1].getMethodName();
    }
 
    public static void e(String message){
        if (!isDebug)
            return;
 
        // Throwable instance must be created before any methods  
        getMethodNames(new Throwable().getStackTrace());
        System.out.println(className + "--" + createLog(message));
    }
 
}
