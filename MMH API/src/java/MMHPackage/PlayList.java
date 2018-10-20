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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Jonathon
 */
@Entity
@Table(name = "PlayList")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PlayList.findAll", query = "SELECT p FROM PlayList p"),
    @NamedQuery(name = "PlayList.findByPlayListID", query = "SELECT p FROM PlayList p WHERE p.playListID = :playListID"),
    @NamedQuery(name = "PlayList.findByUserID", query = "SELECT p FROM PlayList p WHERE p.userID = :userID"),
    @NamedQuery(name = "PlayList.findByPlayListName", query = "SELECT p FROM PlayList p WHERE p.playListName = :playListName"),
    @NamedQuery(name = "PlayList.findByRecommendedBy", query = "SELECT p FROM PlayList p WHERE p.recommendedBy = :recommendedBy")})
public class PlayList implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "PlayListID")
    private Integer playListID;
    @Column(name = "UserID")
    private Integer userID;
    @Size(max = 100)
    @Column(name = "PlayListName")
    private String playListName;
    @Size(max = 100)
    @Column(name = "RecommendedBy")
    private String recommendedBy;

    public PlayList() {
    }

    public PlayList(Integer playListID) {
        this.playListID = playListID;
    }

    public Integer getPlayListID() {
        return playListID;
    }

    public void setPlayListID(Integer playListID) {
        this.playListID = playListID;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public String getPlayListName() {
        return playListName;
    }

    public void setPlayListName(String playListName) {
        this.playListName = playListName;
    }

    public String getRecommendedBy() {
        return recommendedBy;
    }

    public void setRecommendedBy(String recommendedBy) {
        this.recommendedBy = recommendedBy;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (playListID != null ? playListID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PlayList)) {
            return false;
        }
        PlayList other = (PlayList) object;
        if ((this.playListID == null && other.playListID != null) || (this.playListID != null && !this.playListID.equals(other.playListID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "MMHPackage.PlayList[ playListID=" + playListID + " ]";
    }
    
}
