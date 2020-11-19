package edu.hadoop.mr.demo.diyinput.news2;

import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * @author 彭文忠
 * @简介  基础数据javabean，重写write/readFields和compareTo方法
 */
public class NewsInfo implements WritableComparable<NewsInfo> {

	private int id;
	private String url;
	private String anthor;
	private String title;
	private int clicknum;
	private String crawldate;
	private String publishtime;
	private String sourcecard;
	private String content;

	public NewsInfo() {
	}

	public NewsInfo(int id, String url, String anthor, String title, int clicknum, String crawldate, String publishtime,
                    String sourcecard, String content) {
		super();
		this.id = id;
		this.url = url;
		this.anthor = anthor;
		this.title = title;
		this.clicknum = clicknum;
		this.crawldate = crawldate;
		this.publishtime = publishtime;
		this.sourcecard = sourcecard;
		this.content = content;
	}
	public void setNewsInfo(NewsInfo obj) {

		this.id = obj.getId();
		this.url = obj.getUrl();
		this.anthor = obj.getAnthor();
		this.title = obj.getTitle();
		this.clicknum = obj.getClicknum();
		this.crawldate = obj.getCrawldate();
		this.publishtime = obj.getPublishtime();
		this.sourcecard = obj.getSourcecard();
		this.content = obj.getContent();
	}

	// JavaBean 普通的get set方法....

	

	// JavaBean 普通的get set方法....
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getAnthor() {
		return anthor;
	}

	public void setAnthor(String anthor) {
		this.anthor = anthor;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getClicknum() {
		return clicknum;
	}

	// JavaBean 普通的get set方法....
	
	
	
	// JavaBean 普通的get set方法....
	
	public void setClicknum(int clicknum) {
		this.clicknum = clicknum;
	}

	public String getCrawldate() {
		return crawldate;
	}

	public void setCrawldate(String crawldate) {
		this.crawldate = crawldate;
	}

	public String getPublishtime() {
		return publishtime;
	}

	public void setPublishtime(String publishtime) {
		this.publishtime = publishtime;
	}

	public String getSourcecard() {
		return sourcecard;
	}

	public void setSourcecard(String sourcecard) {
		this.sourcecard = sourcecard;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	// JavaBean 普通的get set方法....
	
	
	
	// JavaBean 普通的get set方法....
	
	@Override
	public String toString() {
		return "NewsInfo [id=" + id + ", url=" + url + ", anthor=" + anthor + ", title=" + title + ", clicknum="
				+ clicknum + ", crawldate=" + crawldate + ", publishtime=" + publishtime + ", sourcecard=" + sourcecard
				+ ", content=" + content + "]";
	}

	public String toKeyValueString() {
		return "id:" + id + ", url:" + url + ", anthor:" + anthor + ", title:" + title + ", clicknum:"
				+ clicknum + ", crawldate:" + crawldate + ", publishtime:" + publishtime + ", sourcecard:" + sourcecard
				+ ", content:" + content + "";
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		this.id = in.readInt();
		this.url = in.readUTF();
		this.anthor = in.readUTF();
		this.title = in.readUTF();
		this.clicknum = in.readInt();
		this.crawldate = in.readUTF();
		this.publishtime = in.readUTF();
		this.sourcecard = in.readUTF();
		this.content = in.readUTF();
	
	}

	@Override
	public void write(DataOutput out) throws IOException {
		out.writeInt(id);
		out.writeUTF(url);
		out.writeUTF(anthor);
		out.writeUTF(title);
		out.writeInt(clicknum);
		out.writeUTF(crawldate);
		out.writeUTF(publishtime);
		out.writeUTF(sourcecard);
		out.writeUTF(content);
	}

	@Override
	public int compareTo(NewsInfo userInfo) {
//		if(this.clicknum>userInfo.getClicknum()) {
//			return -1;
//		}else {
//			return 1;
//		}
		return 0;
	}
}