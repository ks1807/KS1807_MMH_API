package service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GeneratePlayLists
{
    private static int AddTrack(String SpotifyTrackID, String TrackName,
            String Genre, String Artist, String Length, Statement SQLStatement)
    {
        try
        {
            /*Verify that we haven't already inserted this record before.
            If we have then just get the ID*/
            String SQLQuery = "SELECT TrackID FROM MusicTrack WHERE TrackName = '"
                    + TrackName + "'" ;
            ResultSet rs = SQLStatement.executeQuery(SQLQuery);
        
            String TrackIDString = "-1";
            if (rs.next())
            {
                TrackIDString = rs.getString("TrackID");
            }    
        
            int TrackID = Integer.parseInt(TrackIDString);
            if (TrackID == -1)
            {
                /*Create a new record in the MusicTrack table and get back the
                ID of the newly inserted record. Set the
                NumberOfTimesListened as 1*/
                SQLQuery = "SET NOCOUNT ON; INSERT INTO MusicTrack (TrackName, "
                        + "Genre, Artist, Length, NumberOfTimesListened, "
                        + "SpotifyTrackID)\n" +
                        "VALUES('" + TrackName + "', '" + Genre + "', '" +
                        Artist + "', '" + Length + "', " + 1 + ", '" +
                        SpotifyTrackID + "'); "
                        + "SELECT SCOPE_IDENTITY() AS NewTrackID";
                
                rs = SQLStatement.executeQuery(SQLQuery);
                if (rs.next())
                {
                    TrackIDString = rs.getString("NewTrackID");
                    TrackID = Integer.parseInt(TrackIDString);
                } 
            }
            rs.close();
            return TrackID;      
        }
        catch (SQLException err)
        {
            return -1;
        }
    }
    
    private static void AddTracksToPlaylist(int UserID, Statement SQLStatement)
    {
        try
        {
            /*Count all rows in the UserMood table that have a match UserID
            and where HasBeenRecommended = “No”.*/
            String SQLQuery = "SELECT Count(UserID) AS TracksNotRecommendedCount "
                    + "FROM UserMood WHERE UserID = " + "'" +
                    UserID + "' AND HasBeenRecommended = 'No'";
            
            ResultSet rs = SQLStatement.executeQuery(SQLQuery);
        
            int NonRecommendedCount = 0;
            if (rs.next())
            {
                NonRecommendedCount = Integer.parseInt(
                        rs.getString("TracksNotRecommendedCount"));
            }

            if (NonRecommendedCount > 10)
            {
                //Checks if the user wants the system to make recommendations.
                SQLQuery = "SELECT MakeRecommendations FROM UserSettings "
                        + "WHERE UserID = " + "'" +
                        UserID + "' AND MakeRecommendations = 'Yes'";

                rs = SQLStatement.executeQuery(SQLQuery);
                
                String MakeRecommendations = "";
                
                if (rs.next())
                {
                    MakeRecommendations = rs.getString("MakeRecommendations");
                }
                
                if (MakeRecommendations.equals("Yes"))
                {
                    int TrackIDs[] = MakeRecommendation(UserID, SQLStatement);
                    int UserTrackIDs[] = MakeRecommendationUsers(SQLStatement);

                    /*Insert a record into the PlayList table with the current
                    UserID, PlayListName as ‘Music To Make You Feel Better’ and
                    RecommendedBy as ‘System’. Get the PlayListID of this newly
                    inserted record.*/
                    SQLQuery = "SET NOCOUNT ON; INSERT INTO PlayList (UserID,"
                            + "PlayListName, RecommendedBy)\n" +
                    "VALUES('" + UserID + "', " + "'Music To Make You Feel Better'"
                            + ", '" + "System" + "'); SELECT SCOPE_IDENTITY() "
                            + "AS PlayListID";
                    
                    rs = SQLStatement.executeQuery(SQLQuery);
                
                    int PlayListID = -1;            
                    if (rs.next())
                    {
                        PlayListID = Integer.parseInt(rs.getString("PlayListID"));
                    }

                    for (int i = 0; i < TrackIDs.length; i++)
                    {
                        int TrackIDToInsert = TrackIDs[i];

                        /*Insert a record into the TracksInPlayList table with
                        the current UserID, the PlayListID and the
                        RecommendedTrackID from the array.*/
                        SQLQuery = "INSERT INTO TracksInPlayList (UserID, "
                                + "PlayListID, TrackID)\n" +
                                "VALUES('" + UserID + "', '" + PlayListID +
                                "', '" + TrackIDToInsert + "')";
                        SQLStatement.execute(SQLQuery);
                    }

                    /*Insert a record into the PlayList table with the current
                    UserID, PlayListName as ‘Music that others are listening to’
                    and RecommendedBy as ‘Users’. Get the PlayListID of this
                    newly inserted record.*/
                    SQLQuery = "SET NOCOUNT ON; INSERT INTO PlayList (UserID, "
                            + "PlayListName, RecommendedBy)\n" +
                            "VALUES('" + UserID + "', " +
                            "'Music that others are listening to'" +
                            ", '" + "Users" + "'); SELECT SCOPE_IDENTITY() "
                            + "AS PlayListID";
                    rs = SQLStatement.executeQuery(SQLQuery);
                
                    PlayListID = -1;            
                    if (rs.next())
                    {
                        PlayListID = Integer.parseInt(rs.getString("PlayListID"));
                    }

                    for (int i = 0; i < UserTrackIDs.length; i++)
                    {
                        int TrackIDToInsert = UserTrackIDs[i];

                        /*Insert a record into the TracksInPlayList table with
                        the current UserID, the PlayListID and the UserTrackID
                        from the array.*/
                        SQLQuery = "INSERT INTO TracksInPlayList (UserID, "
                                + "PlayListID, TrackID)\n" +
                                "VALUES('" + UserID + "', '" + PlayListID +
                                "', '" + TrackIDToInsert + "')";
                        SQLStatement.execute(SQLQuery);
                    }
                }
            }
            rs.close();
        }
        catch (SQLException err)
        {
        }
    }
    
    //Checks to see if the user should be prompted to enter their mood.
    public static String CheckMoodEntry(String UserID, String UserPassword,
            Statement SQLStatement)
    {   
        ApplicationUserQueries User = new ApplicationUserQueries();
        
        if (!User.AuthenticateUser(UserID, UserPassword, SQLStatement))
        {
            return "Incorrect UserID or Password. Query not executed.";
        }
        
        try
        {
            /*Get the MoodFrequency parameter from the UserSettings database
            table.*/
            String SQLQuery = "SELECT MoodFrequency FROM UserSettings "
                    + "WHERE UserID = '" + UserID + "'";

            ResultSet rs = SQLStatement.executeQuery(SQLQuery);
            
            String MoodFrequency = "";
            if (rs.next())
            {
                MoodFrequency = rs.getString("MoodFrequency");
                MoodFrequency = MoodFrequency.toUpperCase();
            } 

            /*Get the MoodAfterTime (Datetime) of the last entry into the
            UserMood table.*/
            SQLQuery = "SELECT TOP (1) MoodAfterTime FROM UserMood WHERE "
                    + "UserID = " + "'" + UserID + "' " +
            "ORDER BY MoodAfterTime DESC";
            rs = SQLStatement.executeQuery(SQLQuery);
            
            String MoodAfterTimeString = "";
            if (rs.next())
            {
                MoodAfterTimeString = rs.getString("MoodAfterTime");
            }
            rs.close();

            //No existing history, just always return yes if so.
            if (MoodAfterTimeString.equals(""))
            {
                return "Yes";
            }
            
            try
            {
                Date MoodAfterTime = DateTimeFromStringSQLFormat(
                        MoodAfterTimeString);
                Date CurrentDate = new Date();

                long DateDifference =
                        CurrentDate.getTime() - MoodAfterTime.getTime();
                long MinutesDifference = DateDifference / (60 * 1000);

                switch(MoodFrequency)
                {
                    case "ONCE PER TRACK" :
                        return "Yes";
                    case "ONCE EVERY 15 MINUTES":
                        if (MinutesDifference > 15)
                        {
                            return "Yes";
                        }
                        else
                        {
                            return "No";
                        }
                    case "ONCE PER HOUR":
                        if (MinutesDifference > 60)
                        {
                            return "Yes";
                        }
                        else
                        {
                            return "No";
                        }
                    case "ONCE PER 24 HOURS":
                        //1440 = Number of Minutes in a day
                        if (MinutesDifference > 1440)
                        {
                            return "Yes";
                        }
                        else
                        {
                            return "No";
                        }
                    case "NEVER" :
                        return "No";
                    default :
                        return "Invalid Setting";
                }
            }
            catch (ParseException e)
            {
                return "Error: CheckMoodEntry";
            }
        }
        catch (SQLException err)
        {
            return "Error: CheckMoodEntry";
        }
    }
    
    private static int ConvertMoodToNumber(String MoodName,
            Statement SQLStatement)
    {
        int Score = 0;
        try
        {
            String SQLQuery = "SELECT Score FROM MoodScore WHERE Mood = " + "'"
                    + MoodName + "'";

            ResultSet rs = SQLStatement.executeQuery(SQLQuery);
            
            String MoodScore = "0";
            if (rs.next())
            {
                MoodScore = rs.getString("Score");
            }
            rs.close();
            
            if (!MoodScore.equals(""))
            {
                Score = Integer.parseInt(MoodScore);
            }
            else
            {
                //Invalid Mood.
                return 0;
            }          
        }
        catch (SQLException err)
        {
            //Invalid Mood.
            return 0;
        }
        return Score;
    }
    
    //Gets a string and formats it into the format used by SQL Server.
    private static Date DateTimeFromStringSQLFormat(String DateString)
            throws ParseException
    {
        SimpleDateFormat SQLServerDateFormat = new
        SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try
        {
            Date FormattedDate = SQLServerDateFormat.parse(DateString);
            return FormattedDate;
        } catch (ParseException e)
        {
            e.printStackTrace();
            throw new ParseException("Invalid Datetime SQL Format", -1);
        }
    }
    
    public static int GetArrayIndexFromString(String[] Array,
            String SearchString)
    {
        int Index=0;
        for(int i=0; i < Array.length; i++)
        {
            if(Array[i].equals(SearchString))
            {
                Index=i;
                break;
            }
        }
        return Index;
    }
    
    //Gets the index place for the highest number in a floating point array.
    public static int GetIndexOfMaximumFloatValue(float[] FloatArray)
    {
        float MaximumValue = FloatArray[0];
        int i;
        for (i = 1; i < FloatArray.length - 1; i++)
        {
            if (FloatArray[i] > MaximumValue)
            {
                MaximumValue = FloatArray[i];
            }
        }
        return i;
    }

    private static int[] MakeRecommendation(int UserID, Statement SQLStatement)
    {
        int TrackIDs[] = new int[10];   
        try
        {
            String[][] GenresAndScores = SetRecommendationScoreForGenre(UserID,
                    SQLStatement);

            //Need to convert the strings back to numbers.
            float GenreScores[] = new float[GenresAndScores.length];
            for (int i = 0; i < GenresAndScores.length; i++)
            {
                GenreScores[i] = Float.valueOf(GenresAndScores[1][i]);
            }

            //Now we check which of these genres has the highest number and get its place in the index.
            int Index = GetIndexOfMaximumFloatValue(GenreScores);

            //We then get the string value of this genre from the index.
            String HighestGenre = GenresAndScores[0][Index];

            /*Select 10 unique music tracks that belong to this genre which
            have the highest average mood scores for ALL users.*/
            String SQLQuery = "SELECT TOP (10) TrackID, "
                    + "(TotalMoodScore/NumberOfTimesListened) AS AverageScore "
                    + "FROM MusicTrack WHERE Genre = '" + HighestGenre + "' "
                    + "ORDER BY AverageScore DESC";
            
            ResultSet rs = SQLStatement.executeQuery(SQLQuery);

            int i = 0;
            while (rs.next())
            {
                TrackIDs[i] = Integer.parseInt(rs.getString("TrackID"));
                i++;
            }
            rs.close();
            return TrackIDs;
        }
        catch (SQLException err)
        {
            return TrackIDs;
        }
    }
    
    /*Gets the latest recommended track for the user, that is the tracks that
    have been specifically recommended for that user.*/
    public static String GetRecommendedTracksUser(String UserID, String
            UserPassword, Statement SQLStatement)
    {
        ApplicationUserQueries User = new ApplicationUserQueries();
        
        if (!User.AuthenticateUser(UserID, UserPassword, SQLStatement))
        {
            return "Incorrect UserID or Password. Query not executed.";
        }
        
        try
        {
            String PlayListName = "Music To Make You Feel Better";
            /*Gets the latest recommended music track playlist. Latest is
            determined by the Playlist with the highest PlayList ID for that
            specfic UserID.
            Note the complex joining of MusicTrack, Playlist and TracksInPlaylist
            that are required to bring back the correct track names.*/
            String SQLQuery = "SELECT DISTINCT TOP (10) SpotifyTrackID, "
                    + "TrackName, Genre, Artist, Length, PlayListName, "
                    + "MusicTrack.TrackID, PlayList.PlayListID, "
                    + "TracksInPlayList.UserID " +
                    "FROM MusicTrack " +
                    "INNER JOIN TracksInPlayList ON " +
                    "MusicTrack.TrackID = TracksInPlayList.TrackID " +
                    "INNER JOIN Playlist ON " +
                    "MusicTrack.TrackID = TracksInPlayList.TrackID AND "
                    + "Playlist.PlayListID = TracksInPlayList.PlayListID AND " +
                    "Playlist.UserID = TracksInPlayList.UserID " +
                    "WHERE TracksInPlayList.UserID = '" + UserID + "'" +
                    "AND PlayListName = '" + PlayListName + "' " +
                    "ORDER BY PlayList.PlayListID DESC";
            ResultSet rs = SQLStatement.executeQuery(SQLQuery);

            String MusicResults = "";
            
            while (rs.next())
            {
                MusicResults = MusicResults + rs.getString("SpotifyTrackID") + ",";
                MusicResults = MusicResults + rs.getString("TrackName") + ",";
                MusicResults = MusicResults + rs.getString("Genre") + ",";
                MusicResults = MusicResults + rs.getString("Artist") + ",";
                MusicResults = MusicResults + rs.getString("Length") + "\n";              
            }
            //Return dashes to sigify that no records were returned.
            if (MusicResults.equals(""))
            {
                MusicResults = "-,-,-,-,-";
            }
            return MusicResults;           
        }
        catch (SQLException err)
        {
            return "Error: GetRecommendedTracksUser";
        }
    }
    
    /*Gets the latest recommended track, that is the tracks that
    have been specifically recommended based on all users.*/
    public static String GetRecommendedTracksSystem(String UserID, String
            UserPassword, Statement SQLStatement)
    {
        ApplicationUserQueries User = new ApplicationUserQueries();
        
        if (!User.AuthenticateUser(UserID, UserPassword, SQLStatement))
        {
            return "Incorrect UserID or Password. Query not executed.";
        }
        
        try
        {
            String PlayListName = "Music that others are listening to";
            /*Gets the latest recommended music track playlist. Latest is
            determined by the Playlist with the highest PlayList ID for that
            specfic UserID.
            Note the complex joining of MusicTrack, Playlist and TracksInPlaylist
            that are required to bring back the correct track names.*/
            String SQLQuery = "SELECT DISTINCT TOP (10) SpotifyTrackID, "
                    + "TrackName, Genre, Artist, Length, PlayListName, "
                    + "MusicTrack.TrackID, PlayList.PlayListID, "
                    + "TracksInPlayList.UserID " +
                    "FROM MusicTrack " +
                    "INNER JOIN TracksInPlayList ON " +
                    "MusicTrack.TrackID = TracksInPlayList.TrackID " +
                    "INNER JOIN Playlist ON " +
                    "MusicTrack.TrackID = TracksInPlayList.TrackID AND "
                    + "Playlist.PlayListID = TracksInPlayList.PlayListID AND " +
                    "Playlist.UserID = TracksInPlayList.UserID " +
                    "WHERE TracksInPlayList.UserID = '" + UserID + "'" +
                    "AND PlayListName = '" + PlayListName + "' " +
                    "ORDER BY PlayList.PlayListID DESC";
            ResultSet rs = SQLStatement.executeQuery(SQLQuery);

            String MusicResults = "";
            
            while (rs.next())
            {
                MusicResults = MusicResults + rs.getString("SpotifyTrackID") + ",";
                MusicResults = MusicResults + rs.getString("TrackName") + ",";
                MusicResults = MusicResults + rs.getString("Genre") + ",";
                MusicResults = MusicResults + rs.getString("Artist") + ",";
                MusicResults = MusicResults + rs.getString("Length") + "\n";              
            }
            //Return dashes to sigify that no records were returned.
            if (MusicResults.equals(""))
            {
                MusicResults = "-,-,-,-,-";
            }
            return MusicResults;           
        }
        catch (SQLException err)
        {
            return "Error: GetRecommendedTracksSystem";
        }
    }

    private static int[] MakeRecommendationUsers(Statement SQLStatement)
    {
        int TrackIDs[] = new int[10];
        
        try
        {
            String[][] GenresAndScores = SetRecommendationScoreForGenre(-1, 
                    SQLStatement);

            //Need to convert the strings back to numbers.
            float GenreScores[] = new float[GenresAndScores.length];
            for (int i = 0; i < GenresAndScores.length; i++)
            {
                GenreScores[i] = Float.valueOf(GenresAndScores[1][i]);
            }

            /*Now we check which of these genres has the highest number and get
            its place in the index.*/
            int Index = GetIndexOfMaximumFloatValue(GenreScores);

            //We then get the string value of this genre from the index.
            String HighestGenre = GenresAndScores[0][Index];

            /*Select 10 unique music tracks that belong to this genre which
            have the highest average mood scores for ALL users.*/
            String SQLQuery = "SELECT TOP (10) TrackID, "
                    + "(TotalMoodScore/NumberOfTimesListened) AS AverageScore "
                    + "FROM MusicTrack WHERE Genre = '" + HighestGenre + "' "
                    + "ORDER BY AverageScore DESC";

            ResultSet rs = SQLStatement.executeQuery(SQLQuery);

            int i = 0;
            while (rs.next())
            {
                TrackIDs[i] = Integer.parseInt(rs.getString("TrackID"));
                i++;
            }
            rs.close();
            return TrackIDs;
        }
        catch (SQLException err)
        {
            return TrackIDs;
        }
    }

    private static String[][] SetRecommendationScoreForGenre(int UserID,
            Statement SQLStatement)
    {
        try
        {
            String SQLQuery = "";
            ResultSet rs;
            int RowCount = 0;

            /*Get the list of all unique genres from the MusicTrack table (as
            long as the genre has been listened to at least once by the user)*/
            if (UserID > -1)
            {
                SQLQuery = "SELECT DISTINCT Genre FROM MusicTrack INNER JOIN "
                        + "UserMood ON MusicTrack.TrackID = UserMood.TrackID "
                        + "WHERE UserID = '" + UserID + "'";
                rs = SQLStatement.executeQuery(SQLQuery);
            }
            /*Or get all Genres that all users have ever listened to*/
            else
            {
                SQLQuery = "SELECT DISTINCT Genre FROM MusicTrack";
                rs = SQLStatement.executeQuery(SQLQuery);
            }

            //Count how many genres we have selected.
            rs.last();
            RowCount = rs.getRow();
            rs.beforeFirst();
            
            int GenreSize = RowCount;
            String Genres[] = new String[GenreSize];
            int i = 0;
            while (rs.next())
            {
                Genres[i] = rs.getString("Genre");
                i++;
            }

            /*Create a  float Array of the same size as Genres called GenreScores,
            initialize all values as 0.*/
            float GenreScores[] = new float[GenreSize];
            for (i = 0; i < GenreSize; i++)
            {
                GenreScores[i] = 0;
            }

            /*Create int Array of the same size as Genres called GenreTrackCount,
            initialize all values as 0.*/
            int GenreTrackCount[] = new int[GenreSize];
            for (i = 0; i < GenreSize; i++)
            {
                GenreTrackCount[i] = 0;
            }

            /*Get the list of all unique TrackIDs from the UserMood table with
            a matching UserID.*/
            if (UserID > -1)
            {
                SQLQuery = "SELECT DISTINCT TrackID FROM UserMood WHERE UserID = " +
                        "'" + UserID + "'";
                rs = SQLStatement.executeQuery(SQLQuery);
            }
            /*Get the list of all unique TrackIDs from the UserMood table.*/
            else
            {
                SQLQuery = "SELECT DISTINCT TrackID FROM UserMood";
                rs = SQLStatement.executeQuery(SQLQuery);
            }

            //Count how many TrackIDs we have selected.
            rs.last();
            RowCount = rs.getRow();
            rs.beforeFirst();
            
            int TrackArraySize = RowCount;
            int TrackArray[] = new int [TrackArraySize];
            
            //Load the TrackIDs into the array.
            i = 0;
            while (rs.next())
            {
                TrackArray[i] = Integer.parseInt(rs.getString("TrackID"));
                i++;
            }

            /*Create an empty second float array of the same size as
            TrackArray called TrackScores.*/
            float TrackScores[] = new float[TrackArraySize];
            for (i = 0; i < TrackArraySize; i++)
            {
                /*Get the average mood score for every music track we have.*/
                TrackScores[i] = SetRecommendationScoreForTrack(UserID,
                        String.valueOf(TrackArray[i]), SQLStatement);

                /*For the nth TrackID in the TrackArray, match the TrackID with
                the Genre through the MusicTrack table.*/
                SQLQuery = "SELECT DISTINCT Genre FROM MusicTrack WHERE TrackID = " +
                        "'" + TrackArray[i] + "'";
                rs = SQLStatement.executeQuery(SQLQuery);
                
                String TheGenre = "";
                if (rs.next())
                {
                    TheGenre = rs.getString("Genre");
                }    

                /*Make sure that the Genre Scores correspond to where the
                string was originally added in the Genre array (so that the
                Classical Music score should go where Classical Music was
                added).*/
                int GenreIndex = GetArrayIndexFromString(Genres, TheGenre);

                /*Add up the scores for each music track in the same genre
                and count how many music tracks are in that genre.*/
                GenreScores[GenreIndex] = GenreScores[GenreIndex] + TrackScores[i];
                GenreTrackCount[GenreIndex] = GenreTrackCount[GenreIndex] + 1;
            }

            String[][] AverageGenreScores = new String[GenreSize][GenreSize];
            for (i = 0; i < GenreSize; i++)
            {
                /*We now get the average genre score for each genre, which is
                all of the track scores per genre divided by the number of
                tracks in that genre.*/
                float TrackCount = GenreTrackCount[i];

                //Avoid Divide by Zero Error
                if (TrackCount == 0.0)
                {
                    TrackCount = 1;
                }

                float Average = GenreScores[i]/TrackCount;
                String TheGenre = Genres[i];
                AverageGenreScores[0][i] = TheGenre;
                AverageGenreScores[1][i] = String.valueOf(Average);
            }          
            rs.close();
            return AverageGenreScores;
        }
        catch (SQLException err)
        {
            return new String[0][0];
        }
    }

    private static float SetRecommendationScoreForTrack(int UserID,
            String TrackID, Statement SQLStatement)
    {
        try
        {
            float Score = 0;
            String SQLQuery = "";
            int MoodTotal = 0;

            if (UserID > -1)
            {
                /*Count the number of rows we need to go through. Exclude any
                mood entries if they don't have a Before AND After Mood*/
                SQLQuery = "SELECT Count(UserID) AS UserCount FROM UserMood "
                        + "WHERE UserID = '" + UserID + "' " + "AND TrackID = '"
                        + TrackID + "' AND MoodBefore IS NOT NULL AND MoodAfter "
                        + "IS NOT NULL";
                
                ResultSet rs = SQLStatement.executeQuery(SQLQuery);

                int RowCount = 0;
                if (rs.next())
                {
                    RowCount = Integer.parseInt(rs.getString("UserCount"));
                } 

                /*Get the MoodBefore and MoodAfter strings from the UserMood
                table by matching the record to the UserID and TrackID. Note
                this gets the nth record in the database as we want all the
                accumulated mood scores*/
                int i;
                for (i = 1; i < RowCount; i++)
                {
                    SQLQuery = "SELECT TOP(1) MoodBefore FROM (SELECT "
                            + "ROW_NUMBER() OVER" +
                            "(ORDER BY MoodBefore ASC) AS rownumber, MoodBefore "
                            + "FROM UserMood " +
                            "WHERE UserID = '" + UserID + "' AND TrackID = '" + 
                            TrackID + "') " +
                            "AS Mood WHERE rownumber = " + i;

                    rs = SQLStatement.executeQuery(SQLQuery);
                    
                    String BeforeMood = "";
                    if (rs.next())
                    {
                        BeforeMood = rs.getString("MoodBefore");
                    }

                    SQLQuery = "SELECT TOP(1) MoodAfter FROM (SELECT "
                            + "ROW_NUMBER() OVER" +
                            "(ORDER BY MoodAfter ASC) AS rownumber, MoodAfter "
                            + "FROM UserMood " +
                            "WHERE UserID = '" + UserID + "' AND TrackID = '" + 
                            TrackID + "') " +
                            "AS Mood WHERE rownumber = " + i;

                    rs = SQLStatement.executeQuery(SQLQuery);
                    
                    String AfterMood = "";
                    if (rs.next())
                    {
                        AfterMood = rs.getString("MoodAfter");
                    }

                    int MoodBeforeNum  = ConvertMoodToNumber(BeforeMood,
                            SQLStatement);
                    int MoodAfterNum  = ConvertMoodToNumber(AfterMood,
                            SQLStatement);
                    MoodTotal = MoodTotal + (MoodAfterNum - MoodBeforeNum);
                }            
                rs.close();
                return (float) MoodTotal/(float) i;
            }
            else
            {
                /*Count the number of rows we need to go through. Exclude any
                mood entries if they don't have a Before AND After Mood*/
                SQLQuery = "SELECT Count(UserID) AS UserCount FROM UserMood "
                        + "WHERE TrackID = '" + TrackID + "' " +
                        "AND MoodBefore IS NOT NULL AND MoodAfter IS NOT NULL";

                ResultSet rs = SQLStatement.executeQuery(SQLQuery);

                int RowCount = 0;
                if (rs.next())
                {
                    RowCount = Integer.parseInt(rs.getString("UserCount"));
                } 

                /*Get the MoodBefore and MoodAfter strings from the UserMood
                table by matching the record to the TrackID (getting all tracks
                regardless of user). Note this gets the nth record in the
                database as we want all the accumulated mood scores*/
                int i;
                for (i = 1; i < RowCount; i++)
                {
                    SQLQuery = "SELECT TOP(1) MoodBefore FROM (SELECT "
                            + "ROW_NUMBER() OVER" +
                            "(ORDER BY MoodBefore ASC) AS RowNumber, MoodBefore "
                            + "FROM UserMood " +
                            "WHERE TrackID = '" + TrackID + "')" +
                            " AS Mood WHERE RowNumber = " + i;
                    
                    rs = SQLStatement.executeQuery(SQLQuery);
                    
                    String BeforeMood = "";
                    if (rs.next())
                    {
                        BeforeMood = rs.getString("MoodBefore");
                    }

                    SQLQuery = "SELECT TOP(1) MoodAfter FROM (SELECT "
                            + "ROW_NUMBER() OVER" +
                            "(ORDER BY MoodAfter ASC) AS RowNumber, MoodAfter "
                            + "FROM UserMood " +
                            "WHERE TrackID = '" + TrackID + "')" +
                            " AS Mood WHERE RowNumber = " + i;
                    
                    rs = SQLStatement.executeQuery(SQLQuery);
                    
                    String AfterMood = "";
                    if (rs.next())
                    {
                        AfterMood = rs.getString("MoodAfter");
                    } 

                    int MoodBeforeNum  = ConvertMoodToNumber(BeforeMood,
                            SQLStatement);
                    int MoodAfterNum  = ConvertMoodToNumber(AfterMood,
                            SQLStatement);
                    MoodTotal = MoodTotal + (MoodAfterNum - MoodBeforeNum);
                }               
                rs.close();
                return (float) MoodTotal/(float) i;
            }
        }
        catch (SQLException err)
        {
            return -1;
        }
    }
    
    private static String UserEnterMoodBefore(String UserID, int TrackID,
            String BeforeMood, Statement SQLStatement)
    {
        try
        {
            //Get the current Date/Time and format it for SQL Server.
            Date CurrentDate = new Date();          
            SimpleDateFormat SQLDateFormat =
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String MoodBeforeTime = SQLDateFormat.format(CurrentDate);

            if (!BeforeMood.equals(""))
            {
                /*System gets current Date/Time, BeforeMood, UserID and
                Track ID and adds these to UserMood database table.
                Get the newly inserted MoodID as well.*/
                    String SQLQuery = "SET NOCOUNT ON;"
                            + "INSERT INTO UserMood (UserID, TrackID, MoodBefore," +
                            "MoodBeforeTime)\n" +
                            "VALUES('" + UserID + "', '" + TrackID + "', '" +
                            BeforeMood + "', '" + MoodBeforeTime + "')\n;" +
                            "SELECT SCOPE_IDENTITY() AS MoodID";

                ResultSet rs = SQLStatement.executeQuery(SQLQuery);

                String MoodID = "-1";
                if (rs.next())
                {
                    MoodID = rs.getString("MoodID");
                } 
                return MoodID;
            }
            else
            {
                return "-1";
            }
        }
        catch (SQLException err)
        {
            return "Error: UserEnterMoodBefore";
        }
    }
    
    private static boolean UserEnterMoodAfter(int MoodID, String AfterMood,
            String UserLiked, String DiaryEntryOne, String DiaryEntryTwo,
            String DiaryEntryThree, String DiaryEntryFour,
            String DiaryEntryFive, Statement SQLStatement)
    {
        try
        {
            /*System to get the UserID from the table by matching this with
            MoodID.*/
            String SQLQuery = "SELECT UserID FROM UserMood WHERE MoodID = "
                    + "'" + MoodID + "'";

            ResultSet rs = SQLStatement.executeQuery(SQLQuery);

            int UserID = -1;
            if (rs.next())
            {
               UserID = Integer.parseInt(rs.getString("UserID"));
            }
            
            //Get the current Date/Time and format it for SQL Server.
            Date CurrentDate = new Date();          
            SimpleDateFormat SQLDateFormat =
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String MoodAfterTime = SQLDateFormat.format(CurrentDate);

            if (!AfterMood.equals(""))
            {
                /*System to get the BeforeMood and TrackID from the table by
                matching this with MoodID.*/
                SQLQuery = "SELECT MoodBefore, TrackID FROM UserMood WHERE "
                        + "MoodID = " + "'" +
                        MoodID + "'";

                 rs = SQLStatement.executeQuery(SQLQuery);

                 String BeforeMood = "";
                 int TrackID = -1;

                 if (rs.next())
                 {
                    BeforeMood = rs.getString("MoodBefore");
                    TrackID = Integer.parseInt(rs.getString("TrackID"));
                 }
                 
                /*System gets current Date/Time, AfterMood, UserLiked, MoodID
                and updates the UserMood database table with these parameters
                where MoodID matches.*/
                SQLQuery = "UPDATE UserMood SET MoodAfter = '" + AfterMood +
                        "', " + "MoodAfterTime = '" + MoodAfterTime + "', " +
                        "UserLiked = '" + UserLiked + "', " +
                        "HasBeenRecommended = '" + "No" + "'\n" +
                        "WHERE MoodID = '" + MoodID + "'";

                SQLStatement.execute(SQLQuery);

                int BeforeScore = ConvertMoodToNumber(BeforeMood, SQLStatement);
                int AfterScore = ConvertMoodToNumber(AfterMood, SQLStatement);
                int ScoreDiff = AfterScore - BeforeScore;
                
                /*Update the MusicTrack table to add the score to it and
                increment the listen count, so we have a record as to wether
                this music track tends to make users feel better or not.
                Note this is a cummulative score for all users.*/
                SQLQuery = "UPDATE MusicTrack SET TotalMoodScore = "
                        + "TotalMoodScore + " + ScoreDiff + ", "
                        + "NumberOfTimesListened =  NumberOfTimesListened + 1 "
                        + "WHERE TrackID = "
                        + TrackID;
                
                SQLStatement.execute(SQLQuery);

                /*Only make a diary entry if the user has entered text in at
                    least one question.*/
                if (!DiaryEntryOne.equals("") || !DiaryEntryTwo.equals("") ||
                        !DiaryEntryThree.equals(""))
                {
                    //Get the time the user made the diary entry.
                    CurrentDate = new Date();          
                    String DiaryEntryTime = SQLDateFormat.format(CurrentDate);

                    SQLQuery = "INSERT INTO UserDiary (UserID, DiaryEntryDate, "
                            + "DiaryEntryOne, DiaryEntryTwo, DiaryEntryThree, "
                            + "DiaryEntryFour, DiaryEntryFive)\n" +
                            "VALUES('" + UserID + "', '" + DiaryEntryTime +
                            "', '" + DiaryEntryOne + "', '" + DiaryEntryTwo + "', '"
                            + DiaryEntryThree + "', '" + DiaryEntryFour + "', "
                            + "'" + DiaryEntryFive + "')";
                    
                    SQLStatement.execute(SQLQuery);
                }
                AddTracksToPlaylist(UserID, SQLStatement);                
                rs.close();
                return true;
            }
            return false;
        }
        catch (SQLException err)
        {
            return false;
        }
    }

    public static String TrackStarted(String SpotifyTrackID, String TrackName,
            String Genre, String Artist, String Length, String BeforeMood,
            String UserID, String UserPassword, Statement SQLStatement)
    {
        ApplicationUserQueries User = new ApplicationUserQueries();
        
        if (!User.AuthenticateUser(UserID, UserPassword, SQLStatement))
        {
            return "Incorrect UserID or Password. Query not executed.";
        }
        
        int TrackID = AddTrack(SpotifyTrackID, TrackName, Genre, Artist, Length,
                SQLStatement);
        String MoodID = UserEnterMoodBefore(UserID, TrackID, BeforeMood,
                SQLStatement);
        return MoodID;
    }
    
    public static String TrackEnded(String SpotifyTrackID, String MoodID,
            String AfterMood, String UserLiked, String DiaryEntryOne,
            String DiaryEntryTwo, String DiaryEntryThree, String DiaryEntryFour,
            String DiaryEntryFive, String UserID, String UserPassword,
            Statement SQLStatement)
    {
        ApplicationUserQueries User = new ApplicationUserQueries();
        
        if (!User.AuthenticateUser(UserID, UserPassword, SQLStatement))
        {
            return "Incorrect UserID or Password. Query not executed.";
        }
        
        int MoodIDNum = Integer.parseInt(MoodID);
        if (MoodIDNum > 0)
        {
            UserEnterMoodAfter(MoodIDNum, AfterMood, UserLiked, DiaryEntryOne,
                    DiaryEntryTwo, DiaryEntryThree, DiaryEntryFour,
                    DiaryEntryFive, SQLStatement);
            return Integer.toString(MoodIDNum);
        }
        else
        {
            return "-1";
        } 
    }
}