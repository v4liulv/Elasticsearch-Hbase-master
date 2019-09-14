package com.sinobest.eshbasetest.domain;


import org.omg.CORBA.portable.UnknownException;

/**
 * 文章实体类
 * @author Administrator
 *
 */
public class Doc {
	
	private Integer id;
	private String title;
	
	private String describe;
	
	private String content;
	
	private String author;

	private String cjsj;

	public void set(String field , Object value) {
		switch (field){
			case "id":
				this.id = (Integer) value;
			case "title":
				assert value instanceof String;
				this.title = (String) value;
				break;
			case "describe":
				assert value instanceof String;
				this.describe = (String) value;
				break;
			case "content":
				assert value instanceof String;
				this.content = (String) value;
				break;
			case "author":
				assert value instanceof String;
				this.author = (String) value;
				break;
			case "cjsj":
				assert value instanceof String;
				this.cjsj = (String) value;
				break;
			default:
				throw new UnknownException(new RuntimeException("Doc属性不存在: " + field));
		}
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getDescribe() {
		return describe;
	}
	public void setDescribe(String describe) {
		this.describe = describe;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}

	public String getCjsj() {
		return cjsj;
	}

	public void setCjsj(String cjsj) {
		this.cjsj = cjsj;
	}
}
