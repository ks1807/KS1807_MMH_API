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

@Entity
@Table(name = "UserAccount")
@XmlRootElement
@NamedQueries(
        {
    @NamedQuery(name = "UserAccount.findAll",
            query = "SELECT u FROM UserAccount u"),
    @NamedQuery(name = "UserAccount.findByUserID",
            query = "SELECT u FROM UserAccount u WHERE u.userID = :userID"),
    @NamedQuery(name = "UserAccount.findByFirstName",
            query = "SELECT u FROM UserAccount u WHERE u.firstName = :firstName"),
    @NamedQuery(name = "UserAccount.findByLastName",
            query = "SELECT u FROM UserAccount u WHERE u.lastName = :lastName"),
    @NamedQuery(name = "UserAccount.findByDateOfBirth",
            query = "SELECT u FROM UserAccount u WHERE u.dateOfBirth = :dateOfBirth"),
    @NamedQuery(name = "UserAccount.findByGender",
            query = "SELECT u FROM UserAccount u WHERE u.gender = :gender"),
    @NamedQuery(name = "UserAccount.findByEmailAddress",
            query = "SELECT u FROM UserAccount u WHERE u.emailAddress = :emailAddress"),
    @NamedQuery(name = "UserAccount.findByPreferredPlatform",
            query = "SELECT u FROM UserAccount u WHERE u.preferredPlatform = :preferredPlatform"),
    @NamedQuery(name = "UserAccount.findBySpotifyID",
            query = "SELECT u FROM UserAccount u WHERE u.spotifyID = :spotifyID"),
    @NamedQuery(name = "UserAccount.findByMusicQuestionOne",
            query = "SELECT u FROM UserAccount u WHERE u.musicQuestionOne = :musicQuestionOne"),
    @NamedQuery(name = "UserAccount.findByMusicQuestionTwo",
            query = "SELECT u FROM UserAccount u WHERE u.musicQuestionTwo = :musicQuestionTwo"),
    @NamedQuery(name = "UserAccount.findByMusicQuestionThree",
            query = "SELECT u FROM UserAccount u WHERE u.musicQuestionThree = :musicQuestionThree"),
    @NamedQuery(name = "UserAccount.findByMusicQuestionFour",
            query = "SELECT u FROM UserAccount u WHERE u.musicQuestionFour = :musicQuestionFour"),
    @NamedQuery(name = "UserAccount.findByUserPassword",
            query = "SELECT u FROM UserAccount u WHERE u.userPassword = :userPassword")
})
public class UserAccount implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "UserID")
    private Integer userID;
    @Size(max = 100)
    @Column(name = "FirstName")
    private String firstName;
    @Size(max = 100)
    @Column(name = "LastName")
    private String lastName;
    @Column(name = "DateOfBirth")
    @Temporal(TemporalType.DATE)
    private Date dateOfBirth;
    @Size(max = 100)
    @Column(name = "Gender")
    private String gender;
    @Size(max = 100)
    @Column(name = "EmailAddress")
    private String emailAddress;
    @Size(max = 100)
    @Column(name = "PreferredPlatform")
    private String preferredPlatform;
    @Size(max = 100)
    @Column(name = "SpotifyID")
    private String spotifyID;
    @Size(max = 100)
    @Column(name = "MusicQuestionOne")
    private String musicQuestionOne;
    @Size(max = 100)
    @Column(name = "MusicQuestionTwo")
    private String musicQuestionTwo;
    @Size(max = 100)
    @Column(name = "MusicQuestionThree")
    private String musicQuestionThree;
    @Size(max = 100)
    @Column(name = "MusicQuestionFour")
    private String musicQuestionFour;
    @Size(max = 100)
    @Column(name = "UserPassword")
    private String userPassword;

    public UserAccount()
    {
        
    }

    public UserAccount(Integer userID)
    {
        this.userID = userID;
    }

    public Integer getUserID()
    {
        return userID;
    }

    public void setUserID(Integer userID)
    {
        this.userID = userID;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPreferredPlatform() {
        return preferredPlatform;
    }

    public void setPreferredPlatform(String preferredPlatform) {
        this.preferredPlatform = preferredPlatform;
    }

    public String getSpotifyID() {
        return spotifyID;
    }

    public void setSpotifyID(String spotifyID) {
        this.spotifyID = spotifyID;
    }

    public String getMusicQuestionOne() {
        return musicQuestionOne;
    }

    public void setMusicQuestionOne(String musicQuestionOne) {
        this.musicQuestionOne = musicQuestionOne;
    }

    public String getMusicQuestionTwo() {
        return musicQuestionTwo;
    }

    public void setMusicQuestionTwo(String musicQuestionTwo) {
        this.musicQuestionTwo = musicQuestionTwo;
    }

    public String getMusicQuestionThree() {
        return musicQuestionThree;
    }

    public void setMusicQuestionThree(String musicQuestionThree) {
        this.musicQuestionThree = musicQuestionThree;
    }

    public String getMusicQuestionFour() {
        return musicQuestionFour;
    }

    public void setMusicQuestionFour(String musicQuestionFour) {
        this.musicQuestionFour = musicQuestionFour;
    }

    public String getUserPassword()
    {
        return userPassword;
    }

    public void setUserPassword(String userPassword)
    {
        this.userPassword = userPassword;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += (userID != null ? userID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object)
    {
        if (!(object instanceof UserAccount))
        {
            return false;
        }
        UserAccount other = (UserAccount) object;
        if ((this.userID == null && other.userID != null) ||
                (this.userID != null && !this.userID.equals(other.userID)))
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "MMHPackage.UserAccount[ userID=" + userID + " ]";
    } 
}
