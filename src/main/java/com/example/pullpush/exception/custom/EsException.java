package com.example.pullpush.exception.custom;

import lombok.NoArgsConstructor;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 1/8/2019 1:42 PM
 * description:
 */
@NoArgsConstructor
public class EsException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public EsException(String message) {
        super(message);
    }
}
