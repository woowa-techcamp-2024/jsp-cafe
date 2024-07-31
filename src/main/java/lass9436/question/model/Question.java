package lass9436.question.model;

public class Question {
	private long questionSeq;
	private long userSeq;
	private String writer;
	private String title;
	private String contents;

	// 기본 생성자
	public Question() {
	}

	// 매개변수를 받는 생성자
	public Question(long userSeq, String writer, String title, String contents) {
		this.userSeq = userSeq;
		this.writer = writer;
		this.title = title;
		this.contents = contents;
	}

	public Question(long questionSeq, long userSeq, String writer, String title, String contents) {
		this.questionSeq = questionSeq;
		this.userSeq = userSeq;
		this.writer = writer;
		this.title = title;
		this.contents = contents;
	}

	// Getters and Setters
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	public long getUserSeq() {
		return userSeq;
	}

	public void setUserSeq(long userSeq) {
		this.userSeq = userSeq;
	}

	@Override
	public String toString() {
		return "Question{" +
			"questionSeq=" + questionSeq +
			", writer='" + writer + '\'' +
			", title='" + title + '\'' +
			", contents='" + contents + '\'' +
			'}';
	}
}

