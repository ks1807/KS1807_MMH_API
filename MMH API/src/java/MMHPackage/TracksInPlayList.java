/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MMHPackage;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Jonathon
 */
@Entity
@Table(name = "TracksInPlayList")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TracksInPlayList.findAll", query = "SELECT t FROM TracksInPlayList t"),
    @NamedQuery(name = "TracksInPlayList.findByTracksInPlayListID", query = "SELECT t FROM TracksInPlayList t WHERE t.tracksInPlayListID = :tracksInPlayListID"),
    @NamedQuery(name = "TracksInPlayList.findByUserID", query = "SELECT t FROM TracksInPlayList t WHERE t.userID = :userID"),
    @NamedQuery(name = "TracksInPlayList.findByPlayListID", query = "SELECT t FROM TracksInPlayList t WHERE t.playListID = :playListID"),
    @NamedQuery(name = "TracksInPlayList.findByTrackID", query = "SELECT t FROM TracksInPlayList t WHERE t.trackID = :trackID")})
public class TracksInPlayList implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "TracksInPlayListID")
    private Integer tracksInPlayListID;
    @Column(name = "UserID")
    private Integer userID;
    @Column(name = "PlayListID")
    private Integer playListID;
    @Column(name = "TrackID")
    private Integer trackID;

    public TracksInPlayList() {
    }

    public TracksInPlayList(Integer tracksInPlayListID) {
        this.tracksInPlayListID = tracksInPlayListID;
    }

    public Integer getTracksInPlayListID() {
        return tracksInPlayListID;
    }

    public void setTracksInPlayListID(Integer tracksInPlayListID) {
        this.tracksInPlayListID = tracksInPlayListID;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public Integer getPlayListID() {
        return playListID;
    }

    public void setPlayListID(Integer playListID) {
        this.playListID = playListID;
    }

    public Integer getTrackID() {
        return trackID;
    }

    public void setTrackID(Integer trackID) {
        this.trackID = trackID;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tracksInPlayListID != null ? tracksInPlayListID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TracksInPlayList)) {
            return false;
        }
        TracksInPlayList other = (TracksInPlayList) object;
        if ((this.tracksInPlayListID == null && other.tracksInPlayListID != null) || (this.tracksInPlayListID != null && !this.tracksInPlayListID.equals(other.tracksInPlayListID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "MMHPackage.TracksInPlayList[ tracksInPlayListID=" + tracksInPlayListID + " ]";
    }
    
}
