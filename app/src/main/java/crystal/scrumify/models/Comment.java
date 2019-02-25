package crystal.scrumify.models;

import com.google.gson.annotations.SerializedName;

public class Comment {

    @SerializedName("name")
    private String commentator;

    @SerializedName("comment")
    private String content;

    public Comment(String commentator, String content) {
        this.commentator = commentator;
        this.content = content;
    }

    public String getCommentator() {
        return commentator;
    }

    public void setCommentator(String commentator) {
        this.commentator = commentator;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
