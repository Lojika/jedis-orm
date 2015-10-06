/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.lojika.jedis.exception;

/**
 *
 * @author bamasyali
 */
public class JedisException extends Exception {

    public JedisException() {
    }

    public JedisException(String message) {
        super(message);
    }

    public JedisException(String message, Throwable cause) {
        super(message, cause);
    }

    public JedisException(Throwable cause) {
        super(cause);
    }

    public JedisException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
