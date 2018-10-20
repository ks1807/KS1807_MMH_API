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
@Table(name = "MoodScore")
@XmlRootElement
@NamedQueries(
        {
    @NamedQuery(name = "MoodScore.findAll",
            query = "SELECT m FROM MoodScore m"),
    @NamedQuery(name = "MoodScore.findByMoodScoreID",
            query = "SELECT m FROM MoodScore m WHERE m.moodScoreID = :moodScoreID"),
    @NamedQuery(name = "MoodScore.findByMood",
            query = "SELECT m FROM MoodScore m WHERE m.mood = :mood"),
    @NamedQuery(name = "MoodScore.findByScore",
            query = "SELECT m FROM MoodScore m WHERE m.score = :score"),
    @NamedQuery(name = "MoodScore.findByEmoticon",
            query = "SELECT m FROM MoodScore m WHERE m.emoticon = :emoticon")
        })
public class MoodScore implements Serializable
{
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "MoodScoreID")
    private Integer moodScoreID;
    @Size(max = 100)
    @Column(name = "Mood")
    private String mood;
    @Column(name = "Score")
    private Integer score;
    @Size(max = 100)
    @Column(name = "Emoticon")
    private String emoticon;

    public MoodScore()
    {
    }

    public MoodScore(Integer moodScoreID)
    {
        this.moodScoreID = moodScoreID;
    }

    public Integer getMoodScoreID()
    {
        return moodScoreID;
    }

    public void setMoodScoreID(Integer moodScoreID)
    {
        this.moodScoreID = moodScoreID;
    }

    public String getMood()
    {
        return mood;
    }

    public void setMood(String mood)
    {
        this.mood = mood;
    }

    public Integer getScore()
    {
        return score;
    }

    public void setScore(Integer score)
    {
        this.score = score;
    }

    public String getEmoticon()
    {
        return emoticon;
    }

    public void setEmoticon(String emoticon)
    {
        this.emoticon = emoticon;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += (moodScoreID != null ? moodScoreID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object)
    {
        if (!(object instanceof MoodScore))
        {
            return false;
        }
        MoodScore other = (MoodScore) object;
        if ((this.moodScoreID == null && other.moodScoreID != null) ||
                (this.moodScoreID != null &&
                !this.moodScoreID.equals(other.moodScoreID)))
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "MMHPackage.MoodScore[ moodScoreID=" + moodScoreID + " ]";
    }
    
}
