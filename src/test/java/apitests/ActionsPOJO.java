package apitests;

public class ActionsPOJO {

    private String content;
    private String action;
    private String postStatus;


    public String getPostStatus() {
        return postStatus;
    }

    public void setPostStatus(String postStatus) {
        this.postStatus = postStatus;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String commentPost) {

        this.content = commentPost;
    }


    public String getAction() {
        return action;
    }

    public void setAction(String likePosts) {
        this.action = likePosts;
    }
}
