package lass9436.comment.model;

public class Comment {

	private long commentSeq;
	private long userSeq;
	private long questionSeq;
	private String writer;
	private String contents;

	public Comment() {}

	public Comment(long userSeq, long questionSeq, String writer, String contents) {
		this.userSeq = userSeq;
		this.questionSeq = questionSeq;
		this.writer = writer;
		this.contents = contents;
	}

	public long getCommentSeq() {
		return commentSeq;
	}

	public void setCommentSeq(long commentSeq) {
		this.commentSeq = commentSeq;
	}

	public long getUserSeq() {
		return userSeq;
	}

	public void setUserSeq(long userSeq) {
		this.userSeq = userSeq;
	}

	public long getQuestionSeq() {
		return questionSeq;
	}

	public void setQuestionSeq(long questionSeq) {
		this.questionSeq = questionSeq;
	}

	public String getWriter() {
		return writer;
	}

	public void setWriter(String writer) {
		this.writer = writer;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}
}
