package service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.security.NoSuchAlgorithmException;
import java.security.MessageDigest;

public class ApplicationUserQueries
{
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
            String SQLQuery = "SELECT Mood, Score, Emoticon "
                    + "FROM MoodScore";
            ResultSet rs = SQLStatement.executeQuery(SQLQuery);


            String MoodScoreResult = "GetMoodList: ";
            while (rs.next())
            {
                MoodScoreResult = MoodScoreResult + rs.getString("Mood") + ",";
                MoodScoreResult = MoodScoreResult + rs.getString("Score") + ",";
                MoodScoreResult = MoodScoreResult + rs.getString("Emoticon") + "\n";
            }
            return MoodScoreResult;
        }
        catch (SQLException err)
        {
            System.err.println("Error executing query");
            err.printStackTrace(System.err);
            System.exit(0);
            return "";
        }
    }
    
    //Gets the last ten music tracks. Gets the Name, Genre, Artist and Length.
    public String GetMusicHistory(String UserID, String UserPassword,
            Statement SQLStatement)
    {
        if (!AuthenticateUser(UserID, UserPassword, SQLStatement))
        {
            return "GetMusicHistory: Incorrect UserID or Password."
                    + " Query not executed.";
        }
        
        try
        {
            /*Gets the last ten music tracks that the user has listened to,
            using the mood after time as the time when the user finished the
            song*/
            String SQLQuery = "SELECT DISTINCT TOP (10) TrackName, Genre, "
                    + "Artist, Length, MoodAfterTime "
                    + "FROM MusicTrack INNER JOIN UserMood ON "
                    + "MusicTrack.TrackID = UserMood.TrackID " +
                    "WHERE UserMood.UserID = '" + UserID + "' " +
                    "AND MoodAfterTime IS NOT NULL AND MoodBeforeTime IS NOT NULL" +
                    " ORDER BY UserMood.MoodAfterTime DESC";
            ResultSet rs = SQLStatement.executeQuery(SQLQuery);

            String MusicResults = "GetMusicHistory:\n";    
            while (rs.next())
            {
                MusicResults = MusicResults + rs.getString("TrackName") + ",";
                MusicResults = MusicResults + rs.getString("Genre") + ",";
                MusicResults = MusicResults + rs.getString("Artist") + ",";
                MusicResults = MusicResults + rs.getString("Length") + "\n";
            }          
            return MusicResults;           
        }
        catch (SQLException err)
        {
            System.err.println("Error executing query");
            err.printStackTrace(System.err);
            System.exit(0);
            return "";
        }
    }
    
    public String GetUserDetails(String UserID, String UserPassword,
            Statement SQLStatement)
    {
        if (!AuthenticateUser(UserID, UserPassword, SQLStatement))
        {
            return "GetUserDetails: Incorrect UserID or Password."
                    + " Query not executed.";
        }
        
        try
        {
            String SQLQuery = "SELECT FirstName, LastName, EmailAddress, "
                    + "DateOfBirth, Gender "
                    + "FROM UserAccount WHERE UserID = '" + UserID + "'";
            ResultSet rs = SQLStatement.executeQuery(SQLQuery);

            String UserDetails = "GetUserDetails:\n";
            
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
            System.err.println("Error executing query");
            err.printStackTrace(System.err);
            System.exit(0);
            return "";
        }
    }
    
    public String GetUserDetailsRegistration(String UserID, String UserPassword,
                Statement SQLStatement)
    {
        if (!AuthenticateUser(UserID, UserPassword, SQLStatement))
        {
            return "GetUserDetailsRegistration: Incorrect UserID or Password."
                    + " Query not executed.";
        }
        
        try
        {
            String SQLQuery = "SELECT FirstName, LastName, EmailAddress, "
                    + "DateOfBirth, Gender "
                    + "FROM UserAccount WHERE UserID = '" + UserID + "'";
            ResultSet rs = SQLStatement.executeQuery(SQLQuery);

            String UserDetails = "GetUserDetailsRegistration:\n";
            
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
            System.err.println("Error executing query");
            err.printStackTrace(System.err);
            System.exit(0);
            return "";
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
            System.err.println("Error executing query");
            err.printStackTrace(System.err);
            System.exit(0);
            return "-1";
        }
    }
    
    public String GetUserPassword(String UserID, Statement SQLStatement)
    {
        try
        {
            String SQLQuery = "SELECT UserPassword "
                    + "FROM UserAccount WHERE UserID = '" + UserID + "'";
            ResultSet rs = SQLStatement.executeQuery(SQLQuery);

            String UserPassword = "";
            
            if (rs.next())
            {
                UserPassword = "UserPassword: " + rs.getString("UserPassword");
            }
            return UserPassword;           
        }
        catch (SQLException err)
        {
            System.err.println("Error executing query");
            err.printStackTrace(System.err);
            System.exit(0);
            return "";
        }
    }
    
    public String GetUserSettings(String UserID, String UserPassword,
            Statement SQLStatement)
    {
        if (!AuthenticateUser(UserID, UserPassword, SQLStatement))
        {
            return "GetUserSettings: Incorrect UserID or Password."
                    + " Query not executed.";
        }
        
        try
        {
            String SQLQuery = "SELECT MakeRecommendations, MoodFrequency "
                    + "FROM UserSettings WHERE UserID = '" + UserID + "'";
            ResultSet rs = SQLStatement.executeQuery(SQLQuery);

            String UserSettings = "GetUserSettings:\n";
            
            if (rs.next())
            {
                UserSettings = UserSettings + "MakeRecommendations: " +
                        rs.getString("MakeRecommendations") + "\n";
                UserSettings = UserSettings + "MoodFrequency: " +
                        rs.getString("MoodFrequency");
            }
            return UserSettings;           
        }
        catch (SQLException err)
        {
            System.err.println("Error executing query");
            err.printStackTrace(System.err);
            System.exit(0);
            return "";
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
                return "IsEmailAddressUnique: NO";
            }
            else
            {
                return "IsEmailAddressUnique: YES";
            }            
        }
        catch (SQLException err)
        {
            System.err.println("Error executing query");
            err.printStackTrace(System.err);
            System.exit(0);
            return "";
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
            System.err.println("Error executing query");
            err.printStackTrace(System.err);
            System.exit(0);
            return false;
        }
    }
    
    public String InsertNewUser(String FirstName, String LastName,
            String EmailAddress, String DateOfBirth, String Gender,
            String UserPassword, Statement SQLStatement)
    {
        try
        {
            String UserID = "";
            
            //Make the Email Address all lowercase to ensure case insensitive search.
            EmailAddress = EmailAddress.toLowerCase();

            String SQLQuery = "SET NOCOUNT ON; INSERT INTO UserAccount (FirstName,"
                                + "LastName, DateOfBirth, Gender, EmailAddress,"
                                + "UserPassword)\n" +
                        "VALUES('" + FirstName + "','" + LastName + "','" +
                    DateOfBirth + "','" + Gender + "','" + EmailAddress + "','"
                    + UserPassword + "'" + ");"
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
                return UserID;
        }
        catch (SQLException err)
        {
            System.err.println("Error executing query");
            err.printStackTrace(System.err);
            System.exit(0);
            return "";
        }
    }
    
    public String UpdateNewUser(String FirstName, String LastName,
            String EmailAddress, String DateOfBirth, String Gender, String
                    UserID, String UserPassword, Statement SQLStatement)
    {
        try
        {
            //Make the Email Address all lowercase to ensure case insensitive search.
            EmailAddress = EmailAddress.toLowerCase();

            String SQLQuery = "UPDATE UserAccount SET FirstName ='" + FirstName +
                    "', LastName = '" + LastName + "',DateOfBirth = '" +
                    DateOfBirth + "'," + "Gender = '" + Gender + "'," +
                    "EmailAddress = '" + EmailAddress + "'," +
                    "UserPassword = '" + UserPassword + "' "
                    + "WHERE UserID = '" + UserID + "'";
                SQLStatement.execute(SQLQuery);
                return "UpdateNewUser: Successful";
        }
        catch (SQLException err)
        {
            System.err.println("Error executing query");
            err.printStackTrace(System.err);
            System.exit(0);
        }
        return "";
    }
    
    public String UpdatePassword(String NewPassword, String UserID,
            String UserPassword, Statement SQLStatement)
    {
        if (!AuthenticateUser(UserID, UserPassword, SQLStatement))
        {
            return "UpdatePassword: Incorrect UserID or Password."
                    + " Query not executed.";
        }
        
        try
        {
            String SQLQuery = "UPDATE UserAccount SET UserPassword ='" +
                    NewPassword + "' WHERE UserID = '" + UserID + "'";
                SQLStatement.execute(SQLQuery);
            return "UpdatePassword: Successful";
        }
        catch (SQLException err)
        {
            System.err.println("Error executing query");
            err.printStackTrace(System.err);
            System.exit(0);
        }
        return "";
    }
    
    public String UpdateUser(String FirstName, String LastName,
            String EmailAddress, String DateOfBirth, String Gender,
            String UserID, String UserPassword,
            Statement SQLStatement)
    {     
        if (!AuthenticateUser(UserID, UserPassword, SQLStatement))
        {
            return "UpdateUser: Incorrect UserID or Password."
                    + " Query not executed.";
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
                return "UpdateUser: Successful";
        }
        catch (SQLException err)
        {
            System.err.println("Error executing query");
            err.printStackTrace(System.err);
            System.exit(0);
        }
        return "";
    }
    
    public String UpdateUserSecondPage(String MusicQuestionOne,
            String MusicQuestionTwo, String MusicQuestionThree, String
                    MusicQuestionFour, String UserID, String UserPassword,
                    Statement SQLStatement)
    {
        if (!AuthenticateUser(UserID, UserPassword, SQLStatement))
        {
            return "UpdateUserSecondPage: Incorrect UserID or Password."
                    + " Query not executed.";
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
                return "UpdateUserSecondPage: Successful";
        }
        catch (SQLException err)
        {
            System.err.println("Error executing query");
            err.printStackTrace(System.err);
            System.exit(0);
        }
        return "";
    }
    
    public String UpdateSettings (String MakeRecommendations,
            String MoodFrequency, String UserID, String UserPassword,
            Statement SQLStatement)
    {
        if (!AuthenticateUser(UserID, UserPassword, SQLStatement))
        {
            return "UpdateSettings: Incorrect UserID or Password."
                    + " Query not executed.";
        }
        
        try
        {
            String SQLQuery = "UPDATE UserSettings SET MakeRecommendations ='"
                    + MakeRecommendations + "', MoodFrequency = '" +
                    MoodFrequency + "' "
                    + "WHERE UserID = '" + UserID + "'";
                SQLStatement.execute(SQLQuery); 
                return "UpdateSettings: Successful";
        }
        catch (SQLException err)
        {
            System.err.println("Error executing query");
            err.printStackTrace(System.err);
            System.exit(0);
            return "";
        }
    }
    
    public String VerifyLogin(String EmailAddress, String UserPassword,
            Statement SQLStatement)
    {
        String UserID = GetUserID(EmailAddress, SQLStatement);

        //Make sure the UserID: string is removed when running the password query.
        String UserIDNumberOnly = UserID.replace("UserID: ","");
        
        //Don't bother checking password if the ID does not match.
        if(UserIDNumberOnly == "-1")
        {
            return UserID;
        }

        String StoredPassword = GetUserPassword(UserIDNumberOnly, SQLStatement);
        
        /*Get rid of the substring in front if we are just doing an
        internal password comparison (not sending the password back to the App)*/
        StoredPassword = StoredPassword.replace("UserPassword: ","");

        //If password is wrong, return -1 to the application.
        if (UserPassword.equals(StoredPassword))
        {
            return UserID;
        }
        else
        {
            return "UserID: -1";
        }
    }

    public String VerifyPassword(String UserID, String UserPassword,
            Statement SQLStatement)
    {     
        String StoredPassword = GetUserPassword(UserID, SQLStatement);
        
        /*Get rid of the substring in front if we are just doing an
        internal password comparison (not sending the password back to the App)*/
        StoredPassword = StoredPassword.replace("UserPassword: ","");

        if (UserPassword.equals(StoredPassword))
        {
            return "VerifyPassword: Correct Password";
        }
        else
        {
            return "VerifyPassword: Incorrect Password";
        }
    }
    
    //Similiar to VerifyPassword but returns a bool instead.
    public boolean AuthenticateUser(String UserID, String UserPassword,
            Statement SQLStatement)
    {
        String StoredPassword = GetUserPassword(UserID, SQLStatement);
        StoredPassword = StoredPassword.replace("UserPassword: ","");
        
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