package com.spiderman.util;

import java.io.Serializable;
import java.text.MessageFormat;


public class KeyValuePair<K,V> implements Serializable
{
    /**
     *序列化标识 
     */
    private static final long serialVersionUID = 1L;

    private K key;
    private V value;
    
    public KeyValuePair(){}
    
    public KeyValuePair(K key,V value)
    {
        this.key = key;
        this.value = value;
    }
    
    public K getKey()
    {
        return key;
    }

    public void setKey(K key)
    {
        this.key = key;
    }

    public V getValue()
    {
        return value;
    }

    public void setValue(V value)
    {
        this.value = value;
    }

    @Override
    public String toString()
    {
        return MessageFormat.format("K:{0}-V:{1}",key,value);
    }
    
}