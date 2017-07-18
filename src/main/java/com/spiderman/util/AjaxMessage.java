package com.spiderman.util;

public class AjaxMessage<T> {
	
	 public AjaxMessage(){}
	 
	 public AjaxMessage(boolean is,String msg,T data){
		 this.msg = msg;
		 this.data = data;
		 this.is = is;
	 }
	
	/**
	 * 提示信息
	 */
	private String msg;
	
	/**
	 * 返回的数据
	 */
	private T data;
	
	/**
	 * 是否成功
	 */
	private boolean is = false;

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public boolean isIs() {
		return is;
	}

	public void setIs(boolean is) {
		this.is = is;
	}
	
}
