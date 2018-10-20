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
    
    /*A single - in a string means that this parameter is empty. This is
    required as the GET URL cannot be empty. This function renders those
    strings as empty*/
    public String RemoveDashFromString(String TheString)
    {
        if (TheString.equals(""))
        {
            return "";
        }
        else
        {
            return TheString;
        }
    }
    
    private static String GetConfigurationDetails()
    {
        //The name of the file to open. Note this is the following directory:
        //...\Glassfish\glassfish4\glassfish\domains\MMH\config\
        String ConfigurationFileName = "MMH_SQLConnectionInfo.ini";
        String Line;
        String[] ConfigurationItems = new String[5];
        
        try
        {
            FileReader fileReader = new FileReader(ConfigurationFileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            int i = 0;
            //Read first five lines of the configuration file only
            while((Line = bufferedReader.readLine()) != null && i < 5)
            {
                ConfigurationItems[i] = Line;
                i++;
            }
            bufferedReader.close();
        }
        catch(FileNotFoundException ex)
        {
            System.out.println("Unable to open server configuratioon file '" +
                    ConfigurationFileName + "'");                
        }
        catch(IOException ex)
        {
            System.out.println(
                "Error reading server configuratioon file '" +
                        ConfigurationFileName + "'");                  
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
             relavant function and resturn the result (if any).*/
            switch (QueryName)
            {           
                case "TrackStarted":
                    Result = Playlist.TrackStarted(QueryContents[0],
                            QueryContents[1], QueryContents[2],
                            QueryContents[3], QueryContents[4],
                            QueryContents[5], SQLStatement);                           
                    break;
                case "TrackEnded":
                    Result = Playlist.TrackEnded(QueryContents[0],
                            QueryContents[1], QueryContents[2],
                            QueryContents[3], SQLStatement);
                    break;
                case "GetMoodList":
                    Result = UserQuery.GetMoodList(SQLStatement);
                    break; 
                case "GetMusicHistory":
                    Result = UserQuery.GetMusicHistory(QueryContents[0],
                            SQLStatement);
                    break; 
                case "GetUserDetailsRegistration":
                    Result = UserQuery.GetUserDetailsRegistration(
                            QueryContents[0], SQLStatement);
                    break;
                case "GetUserDetails":
                    Result = UserQuery.GetUserDetails(
                            Integer.parseInt(QueryContents[0]), SQLStatement);
                    break;
                case "GetUserPassword":
                    Result = UserQuery.GetUserPassword(QueryContents[0], SQLStatement);
                    break;
                case "GetUserID":
                    Result = UserQuery.GetUserID(QueryContents[0], SQLStatement);
                    break;
                case "GetUserSettings":
                    Result = UserQuery.GetUserSettings(
                            QueryContents[0], SQLStatement);
                    break;
                case "IsEmailAddressUnique":
                    Result = UserQuery.IsEmailAddressUnique(QueryContents[0],
                            SQLStatement);
                    break;
                case "InsertNewUser":
                    Result = UserQuery.InsertNewUser(QueryContents[0],
                            QueryContents[1], QueryContents[2],
                            QueryContents[3], QueryContents[4],
                            QueryContents[5], SQLStatement);
                    break;   
                case "UpdatePassword":
                        Result = UserQuery.UpdatePassword(QueryContents[0],
                                QueryContents[1], SQLStatement);
                    break;
                case "UpdateNewUser":
                    Result = UserQuery.UpdateNewUser(QueryContents[0],
                            QueryContents[1], QueryContents[2], QueryContents[3],
                            QueryContents[4], QueryContents[5], QueryContents[6],
                            SQLStatement);
                    break;
                case "UpdateUserSecondPage":
                    Result = UserQuery.UpdateUserSecondPage(QueryContents[0],
                            QueryContents[1], QueryContents[2],
                            QueryContents[3], QueryContents[4], SQLStatement);
                    break;
                case "UpdateUser":
                        Result = UserQuery.UpdateUser(QueryContents[0], QueryContents[1],
                                QueryContents[2], QueryContents[3], QueryContents[4],
                                QueryContents[5], SQLStatement);
                    break;
                case "UpdateSettings":
                        Result = UserQuery.UpdateSettings(QueryContents[0],
                                QueryContents[1], QueryContents[2], SQLStatement);
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
