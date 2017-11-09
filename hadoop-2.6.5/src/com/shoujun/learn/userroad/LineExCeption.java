/**
 * @FileName: LineException.java
 * @Package: com.shoujun.learn.userroad
 * @author caoshoujun
 * @created 2017/11/9 16:33
 * <p/>
 * Copyright 2016 ziroom
 */
package com.shoujun.learn.userroad;

/**
 * <p></p>
 *
 * <PRE>
 * <BR>	修改记录
 * <BR>-----------------------------------------------
 * <BR>	修改日期			修改人			修改内容
 * </PRE>
 *
 * @author caoshoujun
 * @version 1.0
 * @since 1.0
 */
public class LineException extends Exception {
    int flag;
    public LineException(String msg, int flag){
        super(msg);
        this.flag = flag;
    }
    public int getFlag(){
        return this.flag;
    }
}
