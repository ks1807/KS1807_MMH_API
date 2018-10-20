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
@Table(name = "MusicTrack")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MusicTrack.findAll", query = "SELECT m FROM MusicTrack m"),
    @NamedQuery(name = "MusicTrack.findByTrackID", query = "SELECT m FROM MusicTrack m WHERE m.trackID = :trackID"),
    @NamedQuery(name = "MusicTrack.findByTrackName", query = "SELECT m FROM MusicTrack m WHERE m.trackName = :trackName"),
    @NamedQuery(name = "MusicTrack.findByGenre", query = "SELECT m FROM MusicTrack m WHERE m.genre = :genre"),
    @NamedQuery(name = "MusicTrack.findByArtist", query = "SELECT m FROM MusicTrack m WHERE m.artist = :artist"),
    @NamedQuery(name = "MusicTrack.findByLength", query = "SELECT m FROM MusicTrack m WHERE m.length = :length"),
    @NamedQuery(name = "MusicTrack.findByTotalMoodScore", query = "SELECT m FROM MusicTrack m WHERE m.totalMoodScore = :totalMoodScore"),
    @NamedQuery(name = "MusicTrack.findByNumberofTimesListened", query = "SELECT m FROM MusicTrack m WHERE m.numberofTimesListened = :numberofTimesListened")})
public class MusicTrack implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "TrackID")
    private Integer trackID;
    @Size(max = 100)
    @Column(name = "TrackName")
    private String trackName;
    @Size(max = 100)
    @Column(name = "Genre")
    private String genre;
    @Size(max = 100)
    @Column(name = "Artist")
    private String artist;
    @Size(max = 100)
    @Column(name = "Length")
    private String length;
    @Basic(optional = false)
    @NotNull
    @Column(name = "TotalMoodScore")
    private int totalMoodScore;
    @Basic(optional = false)
    @NotNull
    @Column(name = "NumberofTimesListened")
    private int numberofTimesListened;

    public MusicTrack() {
    }

    public MusicTrack(Integer trackID) {
        this.trackID = trackID;
    }

    public MusicTrack(Integer trackID, int totalMoodScore, int numberofTimesListened) {
        this.trackID = trackID;
        this.totalMoodScore = totalMoodScore;
        this.numberofTimesListened = numberofTimesListened;
    }

    public Integer getTrackID() {
        return trackID;
    }

    public void setTrackID(Integer trackID) {
        this.trackID = trackID;
    }

    public String getTrackName() {
        return trackName;
    }

    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public int getTotalMoodScore() {
        return totalMoodScore;
    }

    public void setTotalMoodScore(int totalMoodScore) {
        this.totalMoodScore = totalMoodScore;
    }

    public int getNumberofTimesListened() {
        return numberofTimesListened;
    }

    public void setNumberofTimesListened(int numberofTimesListened) {
        this.numberofTimesListened = numberofTimesListened;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (trackID != null ? trackID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MusicTrack)) {
            return false;
        }
        MusicTrack other = (MusicTrack) object;
        if ((this.trackID == null && other.trackID != null) || (this.trackID != null && !this.trackID.equals(other.trackID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "MMHPackage.MusicTrack[ trackID=" + trackID + " ]";
    }
    
}
