package in.kgec.ideabook.ui.home;

import com.google.firebase.firestore.DocumentId;

import java.util.ArrayList;
import java.util.Date;

public class Post {
    private String userName, status, img_UserDp_url;
    ArrayList<String> likersList;
    @DocumentId
    private String documentId;
    private int noOfComments;
    Date timeOfPost;

    public Post() {

    }

    public Post(String userName, String status, String img_UserDp_url, ArrayList<String> likersList, String documentId, int noOfComments, Date timeOfPost) {
        this.userName = userName;
        this.status = status;
        this.img_UserDp_url = img_UserDp_url;
        this.likersList = likersList;
        this.documentId = documentId;
        this.noOfComments = noOfComments;
        this.timeOfPost = timeOfPost;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImg_UserDp_url() {
        return img_UserDp_url;
    }

    public void setImg_UserDp_url(String img_UserDp_url) {
        this.img_UserDp_url = img_UserDp_url;
    }

    public ArrayList<String> getLikersList() {
        return likersList;
    }

    public void setLikersList(ArrayList<String> likersList) {
        this.likersList = likersList;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public int getNoOfComments() {
        return noOfComments;
    }

    public void setNoOfComments(int noOfComments) {
        this.noOfComments = noOfComments;
    }

    public Date getTimeOfPost() {
        return timeOfPost;
    }

    public void setTimeOfPost(Date timeOfPost) {
        this.timeOfPost = timeOfPost;
    }
}
