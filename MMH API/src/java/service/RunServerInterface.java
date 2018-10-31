package service;
import java.io.*;
import java.util.regex.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class RunServerInterface
{
    private static final String JDBCDriver =
            "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    private static final String ServerConnectionType = "jdbc:sqlserver";
    
    //Replaces characters in the URL in accordance with the Percent Encoding Rule
    //https://en.wikipedia.org/wiki/Percent-encoding
    public static String URLReplace(String TheString)
    {
            TheString = TheString.replace("%20", " ");
            TheString = TheString.replace("%3A", ":");
            TheString = TheString.replace("%3B", "/");
            TheString = TheString.replace("%2F", ";");
            TheString = TheString.replace("%40", "@");
            TheString = TheString.replace("%2F", "/");
            TheString = TheString.replace("%2F", "/");
            TheString = TheString.replace("%2F", "/");
            TheString = TheString.replace("%3C", "<");
            TheString = TheString.replace("%3E", ">");
            TheString = TheString.replace("%3D", "=");
            TheString = TheString.replace("%26", "&");
            TheString = TheString.replace("%25", "%");
            TheString = TheString.replace("%24", "$");
            TheString = TheString.replace("%23", "#");
            TheString = TheString.replace("%2B", "+");
            TheString = TheString.replace("%2C", ",");
            TheString = TheString.replace("%3F", "?");
            return TheString;
    }
    
    /*A single - in a string means that this parameter is empty. This is
    required as the GET URL cannot be empty. This function renders those
    strings as empty.*/
    public String SanitiseURL(String TheString)
    {
        if (TheString.equals("-"))
        {
            return "";
        }
        else
        {
            TheString = URLReplace(TheString);
            return TheString;
        }
    }
    
    private static String GetConfigurationDetails()
    {
        //In case the file read fails.
        String DefaultConnectionString =
                "jdbc:sqlserver://PE-KS1807\\SQLEXPRESS:1433;databasename=" +
                "MFMHDatabase_UAT;user=TestUser;password=P@ssw0rd1;";
        
        //The name of the file to open. Note this is the following directory:
        //...\Glassfish\glassfish4\glassfish\domains\MMH\config\
        String ConfigurationFileName = "MMH_SQLConnectionInfo.ini";
        String Line;
        //Read first five lines of the configuration file only.
        int NumberofLines = 5;
        String[] ConfigurationItems = new String[NumberofLines];
        
        try
        {
            FileReader fileReader = new FileReader(ConfigurationFileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            int i = 0;
            while((Line = bufferedReader.readLine()) != null && i < NumberofLines)
            {
                //Ignore the new line which is treated as an empty string.
                if (!Line.equals(""))
                {
                    ConfigurationItems[i] = Line;
                    i++;
                }
            }
            bufferedReader.close();
        }
        //If file read failed, just use the default connection string.
        catch(FileNotFoundException ex)
        {
            return DefaultConnectionString;
        }
        catch(IOException ex)
        {
            return DefaultConnectionString;
        }
        
        //Get the part of the configuration line between the single quotes.
        String[] ConfigStrings = new String[5];
        Pattern p = Pattern.compile("'([^']*)'");
        
        for (int i = 0; i < 5; i++)
        {
            Matcher m = p.matcher(ConfigurationItems[i]);
            if (m.find())
            {
                ConfigStrings[i] = m.group();
                //Get rid of the single quotes from the string.
                ConfigStrings[i] = ConfigStrings[i].replace("'", "");
            }
        }
        
        String ServerName = ConfigStrings[0];
        String ServerPort = ConfigStrings[1];
        String DatabaseName = ConfigStrings[2];
        String DBUser = ConfigStrings[3];
        String DBUserPassword = ConfigStrings[4];

        //Build the connection string based on what is in the file.
        String JDBC_URL = ServerConnectionType + "://" +
            ServerName + ":" + ServerPort + ";databasename=" + DatabaseName +
            ";user=" + DBUser + ";password=" + DBUserPassword + ";";
        return JDBC_URL;
    }
    
    public static String ConnectToServer(String QueryName, String[] QueryContents)
    {
        String Result = "";
        
         /*First try loading the drivers that connect to the server*/
        try
        {
           Class.forName(JDBCDriver).getConstructor().newInstance();
           System.out.println("JDBC driver loaded");
        }
        catch (Exception err)
        {
           System.err.println("Error loading JDBC driver");
           err.printStackTrace(System.err);
           System.exit(0);
        }

        try
        {
            //Connect to the database with the connection string.
            Connection DatabaseConnection =
                    DriverManager.getConnection(GetConfigurationDetails());

            //Create the statement object and result set for SQL Server.
            Statement SQLStatement = DatabaseConnection.createStatement(
                    ResultSet.TYPE_SCROLL_SENSITIVE,
                            ResultSet.CONCUR_READ_ONLY);

             ApplicationUserQueries UserQuery = new ApplicationUserQueries();
             GeneratePlayLists Playlist = new GeneratePlayLists();
             
             /*Depending on which query has been called by the API, run the
             relavant function and return the result.*/
            switch (QueryName)
            {
                case "CheckMoodEntry":
                    Result = Playlist.CheckMoodEntry(QueryContents[0],
                            QueryContents[1], SQLStatement);
                    break; 
                case "TrackStarted":
                    Result = Playlist.TrackStarted(QueryContents[0],
                            QueryContents[1], QueryContents[2],
                            QueryContents[3], QueryContents[4],
                            QueryContents[5], QueryContents[6],
                            QueryContents[7], SQLStatement);                           
                    break;
                case "TrackEnded":
                    Result = Playlist.TrackEnded(QueryContents[0],
                            QueryContents[1], QueryContents[2],
                            QueryContents[3], QueryContents[4],
                            QueryContents[5], QueryContents[6],
                            QueryContents[7], QueryContents[8], SQLStatement);
                    break;
                case "GetMoodList":
                    Result = UserQuery.GetMoodList(SQLStatement);
                    break; 
                case "GetMusicHistory":
                    Result = UserQuery.GetMusicHistory(QueryContents[0],
                            QueryContents[1], SQLStatement);
                    break; 
                case "GetRecommendedTracksUser":
                    Result = Playlist.GetRecommendedTracksUser(QueryContents[0],
                            QueryContents[1], SQLStatement);
                    break;
                case "GetRecommendedTracksSystem":
                    Result = Playlist.GetRecommendedTracksSystem(QueryContents[0],
                            QueryContents[1], SQLStatement);
                    break; 
                case "GetUserDetailsRegistration":
                    Result = UserQuery.GetUserDetailsRegistration(
                            QueryContents[0], QueryContents[1], SQLStatement);
                    break;
                case "GetUserDetails":
                    Result = UserQuery.GetUserDetails(QueryContents[0],
                            QueryContents[1], SQLStatement);
                    break;
                case "GetUserID":
                    Result = UserQuery.GetUserID(QueryContents[0], SQLStatement);
                    break;
                case "GetUserSettings":
                    Result = UserQuery.GetUserSettings(
                            QueryContents[0], QueryContents[1], SQLStatement);
                    break;
                case "IsEmailAddressUnique":
                    Result = UserQuery.IsEmailAddressUnique(QueryContents[0],
                            SQLStatement);
                    break;
                case "InsertNewUser":
                    Result = UserQuery.InsertNewUser(QueryContents[0],
                            QueryContents[1], QueryContents[2],
                            QueryContents[3], QueryContents[4],
                            QueryContents[5], QueryContents[6], SQLStatement);
                    break;   
                case "UpdatePassword":
                        Result = UserQuery.UpdatePassword(QueryContents[0],
                                QueryContents[1], QueryContents[2],
                                SQLStatement);
                    break;
                case "UpdateNewUser":
                    Result = UserQuery.UpdateNewUser(QueryContents[0],
                            QueryContents[1], QueryContents[2], QueryContents[3],
                            QueryContents[4], QueryContents[5], QueryContents[6],
                            QueryContents[7], SQLStatement);
                    break;
                case "UpdateUserSecondPage":
                    Result = UserQuery.UpdateUserSecondPage(QueryContents[0],
                            QueryContents[1], QueryContents[2],
                            QueryContents[3], QueryContents[4], QueryContents[5],
                            SQLStatement);
                    break;
                case "UpdateUser":
                        Result = UserQuery.UpdateUser(QueryContents[0], QueryContents[1],
                                QueryContents[2], QueryContents[3], QueryContents[4],
                                QueryContents[5], QueryContents[6], SQLStatement);
                    break;
                case "UpdateSettings":
                        Result = UserQuery.UpdateSettings(QueryContents[0],
                                QueryContents[1], QueryContents[2],
                                QueryContents[3], QueryContents[4],
                                SQLStatement);
                    break;
                case "VerifyLogin":
                        Result = UserQuery.VerifyLogin(QueryContents[0],
                                QueryContents[1], SQLStatement);
                    break;
                case "VerifyPassword":
                        Result = UserQuery.VerifyPassword(
                                QueryContents[0], QueryContents[1], SQLStatement);
                    break;
                default:
                    Result = "Invalid query name!";
                    break;
            }
            
            DatabaseConnection.close();           
        }
        catch (SQLException err)
        {
           System.err.println("Error connecting to the database");
           err.printStackTrace(System.err);
           System.exit(0);
        }
        return Result;
    }  
}
