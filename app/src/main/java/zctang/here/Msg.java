package zctang.here;

/**
 * Created by wangxiaoyang01 on 2017/5/19.
 */

class Msg {

    private boolean upvoted;
    private String id;
    private String msg;
    private String time;
    private String upvote;

    public Msg(String id, String content, String time, String upvote) {
        this.upvoted = false;
        this.id = id;
        this.msg = content;
        this.time = time;
        this.upvote = upvote;
    }

    public String getMsg() {
        return msg;
    }

    public String getTime() {
        return time;
    }

    public String getUpvote() {
        return upvote;
    }

    public String getId() {
        return id;
    }

    public boolean isUpvoted() {
        return upvoted;
    }

    public void setUpvoted(boolean upvoted) {
        this.upvoted = upvoted;
    }
}