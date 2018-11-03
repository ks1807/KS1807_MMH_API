package service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.security.NoSuchAlgorithmException;
import java.security.MessageDigest;

public class ApplicationUserQueries
{
    /*Note that the password encryption code below is not used on the server
    side (it has been moved to our client application). It will kept here
    though as it may be useful for a future developer to debug problems with
    the password.*/
    
    /*Code for this algorithm derived from:
    https://howtodoinjava.com/security/how-to-generate-secure-password-hash-md5-sha-pbkdf2-bcrypt-examples/
    */
    private String EncryptPassword(String Password)
    {
        String EncryptedPassword = "";
        try
        {
            //Using MD5 Message-Digest Algorithm to encrypt the password.
            MessageDigest Digest = MessageDigest.getInstance("MD5");
            
            //Add password bytes to digest.
            Digest.update(Password.getBytes());
            
            //Get the hash's bytes.
            byte[] Bytes = Digest.digest();
            
            //Convert these decimal bytes to hexadecimal format.
            StringBuilder StringToBuild = new StringBuilder();
            
            for(int i=0; i< Bytes.length ;i++)
            {
                StringToBuild.append(Integer.toString((Bytes[i] & 0xff) +
                        0x100, 16).substring(1));
            }

            EncryptedPassword = StringToBuild.toString();
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }    
        return EncryptedPassword;
    }
    
    public String GetMoodList(Statement SQLStatement)
    {
        try
        {
            String SQLQuery = "SELECT Score, Emoticon, Mood "
                    + "FROM MoodScore";
            ResultSet rs = SQLStatement.executeQuery(SQLQuery);


            String MoodScoreResult = "";
            while (rs.next())
            {
                MoodScoreResult = MoodScoreResult + rs.getString("Emoticon") + ",";
                MoodScoreResult = MoodScoreResult + rs.getString("Score") + ",";
                MoodScoreResult = MoodScoreResult + rs.getString("Mood") + "\n";
            }
            return MoodScoreResult;
        }
        catch (SQLException err)
        {
            return "Error: GetMoodList";
        }
    }
    
    //Gets the last ten music tracks. Gets the Name, Genre, Artist and Length.
    public String GetMusicHistory(String UserID, String UserPassword,
            Statement SQLStatement)
    {
        if (!AuthenticateUser(UserID, UserPassword, SQLStatement))
        {
            return "Incorrect UserID or Password. Query not executed.";
        }
        
        try
        {
            /*Gets the last ten music tracks that the user has listened to,
            using the mood after time as the time when the user finished the
            song*/
            String SQLQuery = "SELECT DISTINCT TOP (10) SpotifyTrackID, "
                    + "TrackName, Genre, Artist, Length, MoodAfterTime, "
                    + "MoodBefore, MoodAfter "
                    + "FROM MusicTrack INNER JOIN UserMood ON "
                    + "MusicTrack.TrackID = UserMood.TrackID " +
                    "WHERE UserMood.UserID = '" + UserID + "' " +
                    "AND MoodAfterTime IS NOT NULL AND MoodBeforeTime "
                    + "IS NOT NULL" +
                    " ORDER BY UserMood.MoodAfterTime DESC";
            ResultSet rs = SQLStatement.executeQuery(SQLQuery);

            String MusicResults = "";    
            while (rs.next())
            {
                MusicResults = MusicResults + rs.getString("SpotifyTrackID") + ",";
                MusicResults = MusicResults + rs.getString("TrackName") + ",";
                MusicResults = MusicResults + rs.getString("Genre") + ",";
                MusicResults = MusicResults + rs.getString("Artist") + ",";
                MusicResults = MusicResults + rs.getString("Length") + ",";
                MusicResults = MusicResults + rs.getString("MoodBefore") + ",";
                MusicResults = MusicResults + rs.getString("MoodAfter") + "\n";
            }
            
            //Return dashes to sigify that no records were returned.
            if (MusicResults.equals(""))
            {
                MusicResults = "-,-,-,-,-,-,-";
            }           
            return MusicResults;           
        }
        catch (SQLException err)
        {
            return "Error: GetMusicHistory";
        }
    }
    
    public String GetUserDetails(String UserID, String UserPassword,
            Statement SQLStatement)
    {
        if (!AuthenticateUser(UserID, UserPassword, SQLStatement))
        {
            return "Incorrect UserID or Password. Query not executed.";
        }
        
        try
        {
            String SQLQuery = "SELECT FirstName, LastName, EmailAddress, "
                    + "DateOfBirth, Gender "
                    + "FROM UserAccount WHERE UserID = '" + UserID + "'";
            ResultSet rs = SQLStatement.executeQuery(SQLQuery);

            String UserDetails = "";
            
            if (rs.next())
            {
                UserDetails = UserDetails + "FirstName: " +
                        rs.getString("FirstName") + "\n";
                UserDetails = UserDetails + "LastName: " +
                        rs.getString("LastName") + "\n";
                UserDetails = UserDetails + "EmailAddress: " +
                        rs.getString("EmailAddress") + "\n";
                UserDetails = UserDetails + "DateOfBirth: " +
                        rs.getString("DateOfBirth") + "\n";
                UserDetails = UserDetails + "Gender: " +
                        rs.getString("Gender") + "\n";
            }
            return UserDetails;           
        }
        catch (SQLException err)
        {
            return "Error: GetUserDetails";
        }
    }
    
    public String GetUserDetailsRegistration(String UserID, String UserPassword,
                Statement SQLStatement)
    {
        if (!AuthenticateUser(UserID, UserPassword, SQLStatement))
        {
            return "Incorrect UserID or Password. Query not executed.";
        }
        
        try
        {
            String SQLQuery = "SELECT FirstName, LastName, EmailAddress, "
                    + "DateOfBirth, Gender, AcceptedEthicsStatement "
                    + "FROM UserAccount WHERE UserID = '" + UserID + "'";
            ResultSet rs = SQLStatement.executeQuery(SQLQuery);

            String UserDetails = "";
            
            if (rs.next())
            {
                UserDetails = UserDetails + "FirstName: " +
                        rs.getString("FirstName") + "\n";
                UserDetails = UserDetails + "LastName: " +
                        rs.getString("LastName") + "\n";
                UserDetails = UserDetails + "EmailAddress: " +
                        rs.getString("EmailAddress") + "\n";
                UserDetails = UserDetails + "DateOfBirth: " +
                        rs.getString("DateOfBirth") + "\n";
                UserDetails = UserDetails + "Gender: " +
                        rs.getString("Gender") + "\n";
                UserDetails = UserDetails + "AcceptedEthicsStatement: " +
                        rs.getString("AcceptedEthicsStatement") + "\n";
            }
            return UserDetails;           
        }
        catch (SQLException err)
        {
            return "Error: GetUserDetailsRegistration";
        }
    }
    
    public String GetUserID(String EmailAddress, Statement SQLStatement)
    {
        try
        {
            String SQLQuery = "SELECT UserID "
                    + "FROM UserAccount WHERE EmailAddress = '" +
                    EmailAddress + "'";
            ResultSet rs = SQLStatement.executeQuery(SQLQuery);

            if (rs.next())
            {
                String UserIDString = "";
                UserIDString = rs.getString("UserID");
                if (!UserIDString.equals(""))
                {
                    return UserIDString;
                }
                else
                {
                    return "-1";
                }
            }
            return "-1";
        }
        catch (SQLException err)
        {
            return "-1";
        }
    }
    
    private String GetUserPassword(String UserID, Statement SQLStatement)
    {
        try
        {
            String SQLQuery = "SELECT UserPassword "
                    + "FROM UserAccount WHERE UserID = '" + UserID + "'";
            ResultSet rs = SQLStatement.executeQuery(SQLQuery);

            String UserPassword = "";
            
            if (rs.next())
            {
                UserPassword = rs.getString("UserPassword");
            }
            return UserPassword;           
        }
        catch (SQLException err)
        {
            return "Error: GetUserPassword";
        }
    }
    
    public String GetUserSettings(String UserID, String UserPassword,
            Statement SQLStatement)
    {
        if (!AuthenticateUser(UserID, UserPassword, SQLStatement))
        {
            return "Incorrect UserID or Password. Query not executed.";
        }
        
        try
        {
            String SQLQuery = "SELECT MakeRecommendations, MoodFrequency,"
                    + "RememberLogin FROM UserSettings WHERE UserID = '"
                    + UserID + "'";
            ResultSet rs = SQLStatement.executeQuery(SQLQuery);

            String UserSettings = "";
            
            if (rs.next())
            {
                UserSettings = UserSettings + "MakeRecommendations: " +
                        rs.getString("MakeRecommendations") + "\n";
                UserSettings = UserSettings + "MoodFrequency: " +
                        rs.getString("MoodFrequency") + "\n";
                UserSettings = UserSettings + "RememberLogin: " +
                        rs.getString("RememberLogin");
            }
            return UserSettings;           
        }
        catch (SQLException err)
        {
            return "Error: GetUserSettings";
        }
    }
    
    public String IsEmailAddressUnique(String EmailAddress, Statement
            SQLStatement)
    {
        //Make the Email Address check case insensitive.
        EmailAddress = EmailAddress.toLowerCase();
        
        try
        {
            String SQLQuery = "SELECT Count(EmailAddress) AS EmailCount "
                    + "FROM UserAccount WHERE EmailAddress = '" +
                    EmailAddress + "'";
            ResultSet rs = SQLStatement.executeQuery(SQLQuery);

            int EmailCount = 0;
            if (rs.next())
            {
                EmailCount = Integer.parseInt(rs.getString("EmailCount"));
            }
            if (EmailCount > 0)
            {
                return "NO";
            }
            else
            {
                return "YES";
            }            
        }
        catch (SQLException err)
        {
            return "Error: IsEmailAddressUnique";
        }
    }
    
    private boolean InsertNewSettings (String UserID, Statement SQLStatement)
    {
        try
        {
            String SQLQuery = "INSERT INTO UserSettings (UserID,"
            + "MoodFrequency, MakeRecommendations, RememberLogin) " +
            "VALUES('" + UserID + "','Once Per Track'," +
            "'Yes'" + ",'No');";
            SQLStatement.execute(SQLQuery);
            return true;
        }
        catch (SQLException err)
        {
            return false;
        }
    }
    
    public String InsertNewUser(String FirstName, String LastName,
            String EmailAddress, String DateOfBirth, String Gender,
            String AcceptedEthicsStatement, String UserPassword,
            Statement SQLStatement)
    {
        try
        {
            String UserID = "-1";
            
            //Make the Email Address all lowercase to ensure case insensitive search.
            EmailAddress = EmailAddress.toLowerCase();

            String SQLQuery = "SET NOCOUNT ON; INSERT INTO UserAccount (FirstName,"
                                + "LastName, DateOfBirth, Gender, EmailAddress, "
                    + "AcceptedEthicsStatement, UserPassword)\n" +
                        "VALUES('" + FirstName + "','" + LastName + "','" +
                    DateOfBirth + "','" + Gender + "','" + EmailAddress + "','"
                    + AcceptedEthicsStatement + "', '" + UserPassword + "'" + ");"
                    + "SELECT SCOPE_IDENTITY() AS UserID";
                ResultSet rs = SQLStatement.executeQuery(SQLQuery);
                
                if (rs.next())
                {
                    UserID = rs.getString("UserID");
                }    

                //Insert new settings record as well.
                if(!InsertNewSettings(UserID, SQLStatement))
                {
                    //Return -1 if it failed.
                    UserID = "-1";
                }
                
                if (DateOfBirth.equals(""))
                {
                    String SQLResult = SetDateOfBirthAsNULL(UserID, SQLStatement);
                    if (SQLResult.equals("Error: SetDateOfBirthAsNULL"))
                    {
                        return SQLResult;
                    }
                }        
                return UserID;
        }
        catch (SQLException err)
        {
            return "Error: InsertNewUser";
        }
    }
    
    public String UpdateNewUser(String FirstName, String LastName,
            String EmailAddress, String DateOfBirth, String Gender, String
            AcceptedEthicsStatement, String UserID, String UserPassword,
            Statement SQLStatement)
    {
        try
        {
            //Make the Email Address all lowercase to ensure case insensitive search.
            EmailAddress = EmailAddress.toLowerCase();

            String SQLQuery = "UPDATE UserAccount SET FirstName ='" + FirstName +
                    "', LastName = '" + LastName + "',DateOfBirth = '" +
                    DateOfBirth + "'," + "Gender = '" + Gender + "', " +
                    "EmailAddress = '" + EmailAddress + "', " +
                    "AcceptedEthicsStatement = '" + AcceptedEthicsStatement + "', " +
                    "UserPassword = '" + UserPassword + "' "
                    + "WHERE UserID = '" + UserID + "'";
                SQLStatement.execute(SQLQuery);
                
                if (DateOfBirth.equals(""))
                {
                    String SQLResult = SetDateOfBirthAsNULL(UserID, SQLStatement);
                    if (SQLResult.equals("Error: SetDateOfBirthAsNULL"))
                    {
                        return SQLResult;
                    }
                }
                return "Successful";
        }
        catch (SQLException err)
        {
            return "Error: UpdateNewUser";
        }
    }
    
    public String UpdatePassword(String NewPassword, String UserID,
            String UserPassword, Statement SQLStatement)
    {
        if (!AuthenticateUser(UserID, UserPassword, SQLStatement))
        {
            return "Incorrect UserID or Password. Query not executed.";
        }
        
        try
        {
            String SQLQuery = "UPDATE UserAccount SET UserPassword ='" +
                    NewPassword + "' WHERE UserID = '" + UserID + "'";
                SQLStatement.execute(SQLQuery);
            return "Successful";
        }
        catch (SQLException err)
        {
            return "Error: UpdatePassword";
        }
    }
    
    public String UpdateUser(String FirstName, String LastName,
            String EmailAddress, String DateOfBirth, String Gender,
            String UserID, String UserPassword,
            Statement SQLStatement)
    {     
        if (!AuthenticateUser(UserID, UserPassword, SQLStatement))
        {
            return "Incorrect UserID or Password. Query not executed.";
        }
        
        try
        {
            //Make the Email Address all lowercase to ensure case insensitive search.
            EmailAddress = EmailAddress.toLowerCase();

            String SQLQuery = "UPDATE UserAccount SET FirstName ='" + FirstName +
                    "', LastName = '" + LastName + "',DateOfBirth = '" +
                    DateOfBirth + "'," + "Gender = '" + Gender + "'," +
                    "EmailAddress = '" + EmailAddress + "' " +
                    "WHERE UserID = '" + UserID + "'";
                SQLStatement.execute(SQLQuery);
                
                if (DateOfBirth.equals(""))
                {
                    String SQLResult = SetDateOfBirthAsNULL(UserID, SQLStatement);
                    if (SQLResult.equals("Error: SetDateOfBirthAsNULL"))
                    {
                        return SQLResult;
                    }
                }
                return "Successful";
        }
        catch (SQLException err)
        {
            return "Error: UpdateUser";
        }
    }
    
    public String UpdateUserSecondPage(String MusicQuestionOne,
            String MusicQuestionTwo, String MusicQuestionThree, String
                    MusicQuestionFour, String UserID, String UserPassword,
                    Statement SQLStatement)
    {
        if (!AuthenticateUser(UserID, UserPassword, SQLStatement))
        {
            return "Incorrect UserID or Password. Query not executed.";
        }
        
        try
        {
            String SQLQuery = "UPDATE UserAccount SET MusicQuestionOne ='"
                    + MusicQuestionOne +
                    "', MusicQuestionTwo = '" + MusicQuestionTwo +
                    "',MusicQuestionThree = '" + MusicQuestionThree + "',"
                    + "MusicQuestionFour = '" + MusicQuestionFour + "' "
                    + "WHERE UserID = '" + UserID + "'";
                SQLStatement.execute(SQLQuery);
                return "Successful";
        }
        catch (SQLException err)
        {
            return "Error: UpdateUserSecondPage";
        }
    }
    
    public String UpdateSettings (String MakeRecommendations,
            String MoodFrequency, String RememberLogin, String UserID,
            String UserPassword, Statement SQLStatement)
    {
        if (!AuthenticateUser(UserID, UserPassword, SQLStatement))
        {
            return "Incorrect UserID or Password. Query not executed.";
        }
        
        try
        {
            String SQLQuery = "UPDATE UserSettings SET MakeRecommendations ='"
                    + MakeRecommendations + "', MoodFrequency = '" +
                    MoodFrequency + "' , RememberLogin = '" + RememberLogin + "' "
                    + "WHERE UserID = '" + UserID + "'";
                SQLStatement.execute(SQLQuery); 
                return "Successful";
        }
        catch (SQLException err)
        {
            return "Error: UpdateSettings";
        }
    }
    
    /*SQL Server has a rule that if an empty string is inserted/updated for a
    date field then the date is 1900-01-01. So this function forces a DB NULL
    to be set after the insert/update.*/
    private String SetDateOfBirthAsNULL(String UserID, Statement SQLStatement)
    {
        try
        {
            String SQLQuery = "UPDATE UserAccount SET DateOfBirth = NULL "
                    + "WHERE UserID = '" + UserID + "'";
                SQLStatement.execute(SQLQuery);
                return "";
        }
        catch (SQLException err)
        {
            return "Error: SetDateOfBirthAsNULL";
        }
    }
    
    public String VerifyLogin(String EmailAddress, String UserPassword,
            Statement SQLStatement)
    {
        String UserID = GetUserID(EmailAddress, SQLStatement);
        
        //Don't bother checking password if the ID does not match.
        if(UserID == "-1")
        {
            return UserID;
        }

        String StoredPassword = GetUserPassword(UserID, SQLStatement);

        //If password is wrong, return -1 to the application.
        if (UserPassword.equals(StoredPassword))
        {
            return UserID;
        }
        else
        {
            return "-1";
        }
    }

    public String VerifyPassword(String UserID, String UserPassword,
            Statement SQLStatement)
    {     
        String StoredPassword = GetUserPassword(UserID, SQLStatement);

        if (UserPassword.equals(StoredPassword))
        {
            return "Correct Password";
        }
        else
        {
            return "Incorrect Password";
        }
    }
    
    //Similiar to VerifyPassword but returns a bool instead.
    public boolean AuthenticateUser(String UserID, String UserPassword,
            Statement SQLStatement)
    {
        String StoredPassword = GetUserPassword(UserID, SQLStatement);
        
        if (StoredPassword.equals(UserPassword))
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}