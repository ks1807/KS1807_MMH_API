/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MMHPackage;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Jonathon
 */
@Entity
@Table(name = "UserMood")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UserMood.findAll", query = "SELECT u FROM UserMood u"),
    @NamedQuery(name = "UserMood.findByMoodID", query = "SELECT u FROM UserMood u WHERE u.moodID = :moodID"),
    @NamedQuery(name = "UserMood.findByUserID", query = "SELECT u FROM UserMood u WHERE u.userID = :userID"),
    @NamedQuery(name = "UserMood.findByTrackID", query = "SELECT u FROM UserMood u WHERE u.trackID = :trackID"),
    @NamedQuery(name = "UserMood.findByMoodBefore", query = "SELECT u FROM UserMood u WHERE u.moodBefore = :moodBefore"),
    @NamedQuery(name = "UserMood.findByMoodBeforeTime", query = "SELECT u FROM UserMood u WHERE u.moodBeforeTime = :moodBeforeTime"),
    @NamedQuery(name = "UserMood.findByMoodAfter", query = "SELECT u FROM UserMood u WHERE u.moodAfter = :moodAfter"),
    @NamedQuery(name = "UserMood.findByMoodAfterTime", query = "SELECT u FROM UserMood u WHERE u.moodAfterTime = :moodAfterTime"),
    @NamedQuery(name = "UserMood.findByUserLiked", query = "SELECT u FROM UserMood u WHERE u.userLiked = :userLiked"),
    @NamedQuery(name = "UserMood.findByHasBeenRecommended", query = "SELECT u FROM UserMood u WHERE u.hasBeenRecommended = :hasBeenRecommended")})
public class UserMood implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "MoodID")
    private Integer moodID;
    @Column(name = "UserID")
    private Integer userID;
    @Column(name = "TrackID")
    private Integer trackID;
    @Size(max = 100)
    @Column(name = "MoodBefore")
    private String moodBefore;
    @Column(name = "MoodBeforeTime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date moodBeforeTime;
    @Size(max = 100)
    @Column(name = "MoodAfter")
    private String moodAfter;
    @Column(name = "MoodAfterTime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date moodAfterTime;
    @Size(max = 100)
    @Column(name = "UserLiked")
    private String userLiked;
    @Size(max = 100)
    @Column(name = "HasBeenRecommended")
    private String hasBeenRecommended;

    public UserMood() {
    }

    public UserMood(Integer moodID) {
        this.moodID = moodID;
    }

    public Integer getMoodID() {
        return moodID;
    }

    public void setMoodID(Integer moodID) {
        this.moodID = moodID;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public Integer getTrackID() {
        return trackID;
    }

    public void setTrackID(Integer trackID) {
        this.trackID = trackID;
    }

    public String getMoodBefore() {
        return moodBefore;
    }

    public void setMoodBefore(String moodBefore) {
        this.moodBefore = moodBefore;
    }

    public Date getMoodBeforeTime() {
        return moodBeforeTime;
    }

    public void setMoodBeforeTime(Date moodBeforeTime) {
        this.moodBeforeTime = moodBeforeTime;
    }

    public String getMoodAfter() {
        return moodAfter;
    }

    public void setMoodAfter(String moodAfter) {
        this.moodAfter = moodAfter;
    }

    public Date getMoodAfterTime() {
        return moodAfterTime;
    }

    public void setMoodAfterTime(Date moodAfterTime) {
        this.moodAfterTime = moodAfterTime;
    }

    public String getUserLiked() {
        return userLiked;
    }

    public void setUserLiked(String userLiked) {
        this.userLiked = userLiked;
    }

    public String getHasBeenRecommended() {
        return hasBeenRecommended;
    }

    public void setHasBeenRecommended(String hasBeenRecommended) {
        this.hasBeenRecommended = hasBeenRecommended;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (moodID != null ? moodID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UserMood)) {
            return false;
        }
        UserMood other = (UserMood) object;
        if ((this.moodID == null && other.moodID != null) || (this.moodID != null && !this.moodID.equals(other.moodID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "MMHPackage.UserMood[ moodID=" + moodID + " ]";
    }
    
}
