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
@Table(name = "UserSettings")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UserSettings.findAll", query = "SELECT u FROM UserSettings u"),
    @NamedQuery(name = "UserSettings.findByUserSettingID", query = "SELECT u FROM UserSettings u WHERE u.userSettingID = :userSettingID"),
    @NamedQuery(name = "UserSettings.findByUserID", query = "SELECT u FROM UserSettings u WHERE u.userID = :userID"),
    @NamedQuery(name = "UserSettings.findByMoodFrequency", query = "SELECT u FROM UserSettings u WHERE u.moodFrequency = :moodFrequency"),
    @NamedQuery(name = "UserSettings.findByMakeRecommendations", query = "SELECT u FROM UserSettings u WHERE u.makeRecommendations = :makeRecommendations"),
    @NamedQuery(name = "UserSettings.findByRememberLogin", query = "SELECT u FROM UserSettings u WHERE u.rememberLogin = :rememberLogin")})
public class UserSettings implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "UserSettingID")
    private Integer userSettingID;
    @Column(name = "UserID")
    private Integer userID;
    @Size(max = 100)
    @Column(name = "MoodFrequency")
    private String moodFrequency;
    @Size(max = 100)
    @Column(name = "MakeRecommendations")
    private String makeRecommendations;
    @Size(max = 100)
    @Column(name = "RememberLogin")
    private String rememberLogin;

    public UserSettings() {
    }

    public UserSettings(Integer userSettingID) {
        this.userSettingID = userSettingID;
    }

    public Integer getUserSettingID() {
        return userSettingID;
    }

    public void setUserSettingID(Integer userSettingID) {
        this.userSettingID = userSettingID;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public String getMoodFrequency() {
        return moodFrequency;
    }

    public void setMoodFrequency(String moodFrequency) {
        this.moodFrequency = moodFrequency;
    }

    public String getMakeRecommendations() {
        return makeRecommendations;
    }

    public void setMakeRecommendations(String makeRecommendations) {
        this.makeRecommendations = makeRecommendations;
    }

    public String getRememberLogin() {
        return rememberLogin;
    }

    public void setRememberLogin(String rememberLogin) {
        this.rememberLogin = rememberLogin;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (userSettingID != null ? userSettingID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UserSettings)) {
            return false;
        }
        UserSettings other = (UserSettings) object;
        if ((this.userSettingID == null && other.userSettingID != null) || (this.userSettingID != null && !this.userSettingID.equals(other.userSettingID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "MMHPackage.UserSettings[ userSettingID=" + userSettingID + " ]";
    }
    
}
