package com.example.utils;

/**
 * @author Lex
 */
public class IdGenerator {
    private static int nextId = 1;

    public static synchronized int getNextId() {
        return nextId++;
    }
}
