package service;

import MMHPackage.MusicTrack;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Stateless
@Path("mmhpackage.musictrack")
public class MusicTrackFacadeREST extends AbstractFacade<MusicTrack>
{
    RunServerInterface ServerInterface = new RunServerInterface();
    @PersistenceContext(unitName = "MMH_APIPU")
    private EntityManager em;

    public MusicTrackFacadeREST()
    {
        super(MusicTrack.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(MusicTrack entity)
    {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Integer id, MusicTrack entity)
    {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Integer id)
    {
        super.remove(super.find(id));
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public MusicTrack find(@PathParam("id") Integer id)
    {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<MusicTrack> findAll()
    {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<MusicTrack> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to)
    {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(super.count());
    }

    @Override
    protected EntityManager getEntityManager()
    {
        return em;
    }
    
    //Paths our API uses.
    @GET
    @Path("GetRecommendedTracksUser/{UserID}/{UserPassword}")
    @Produces({ MediaType.TEXT_PLAIN })
    public String GetRecommendedTracksUser(@PathParam("UserID") String UserID,
            @PathParam("UserPassword") String UserPassword)
    {
        String[] Parameters = new String[2];
        Parameters[0] = UserID;
        Parameters[1] = UserPassword;
        
        Parameters[0] = ServerInterface.SanitiseURL(Parameters[0]);
        Parameters[1] = ServerInterface.SanitiseURL(Parameters[1]);
        return ServerInterface.ConnectToServer("GetRecommendedTracksUser",
                Parameters);
    }
    
    @GET
    @Path("GetRecommendedTracksSystem/{UserID}/{UserPassword}")
    @Produces({ MediaType.TEXT_PLAIN })
    public String GetRecommendedTracksSystem(@PathParam("UserID") String UserID,
            @PathParam("UserPassword") String UserPassword)
    {
        String[] Parameters = new String[2];
        Parameters[0] = UserID;
        Parameters[1] = UserPassword;
        
        Parameters[0] = ServerInterface.SanitiseURL(Parameters[0]);
        Parameters[1] = ServerInterface.SanitiseURL(Parameters[1]);
        return ServerInterface.ConnectToServer("GetRecommendedTracksSystem",
                Parameters);
    }
    
    @GET
    @Path("TrackStarted/{TrackName}/{Genre}/{Artist}/{Length}/"
            + "{BeforeMood}/{UserID}/{UserPassword}")
    @Produces({ MediaType.TEXT_PLAIN })
    public String TrackStarted(
            @PathParam("TrackName") String TrackName,
            @PathParam("Genre") String Genre,
            @PathParam("Artist") String Artist,
            @PathParam("Length") String Length,
            @PathParam("BeforeMood") String BeforeMood,
            @PathParam("UserID") String UserID,
            @PathParam("UserPassword") String UserPassword)
    {
        String[] Parameters = new String[7];
        Parameters[0] = TrackName;
        Parameters[1] = Genre;
        Parameters[2] = Artist;
        Parameters[3] = Length;
        Parameters[4] = BeforeMood;
        Parameters[5] = UserID;
        Parameters[6] = UserPassword;
        
        Parameters[0] = ServerInterface.SanitiseURL(Parameters[0]);
        Parameters[1] = ServerInterface.SanitiseURL(Parameters[1]);
        Parameters[2] = ServerInterface.SanitiseURL(Parameters[2]);
        Parameters[3] = ServerInterface.SanitiseURL(Parameters[3]);
        Parameters[4] = ServerInterface.SanitiseURL(Parameters[4]);
        Parameters[5] = ServerInterface.SanitiseURL(Parameters[5]);
        Parameters[6] = ServerInterface.SanitiseURL(Parameters[6]);
        
        return ServerInterface.ConnectToServer("TrackStarted", Parameters);
    }
    
    @GET
    @Path("TrackEnded/{MoodID}/{AfterMood}/{UserLiked}/{DiaryEntryOne}/"
            + "{DiaryEntryTwo}/{DiaryEntryThree}/{UserID}/{UserPassword}")
    @Produces({ MediaType.TEXT_PLAIN })
    public String TrackEnded(@PathParam("MoodID") String MoodID,
            @PathParam("AfterMood") String AfterMood,
            @PathParam("UserLiked") String UserLiked,
            @PathParam("DiaryEntryOne") String DiaryEntryOne,
            @PathParam("DiaryEntryTwo") String DiaryEntryTwo,
            @PathParam("DiaryEntryThree") String DiaryEntryThree,
            @PathParam("UserID") String UserID,
            @PathParam("UserPassword") String UserPassword)
    {
        String[] Parameters = new String[8];
        Parameters[0] = MoodID;
        Parameters[1] = AfterMood;
        Parameters[2] = UserLiked;
        Parameters[3] = DiaryEntryOne;
        Parameters[4] = DiaryEntryTwo;
        Parameters[5] = DiaryEntryThree;
        Parameters[6] = UserID;
        Parameters[7] = UserPassword;
        
        Parameters[0] = ServerInterface.SanitiseURL(Parameters[0]);
        Parameters[1] = ServerInterface.SanitiseURL(Parameters[1]);
        Parameters[2] = ServerInterface.SanitiseURL(Parameters[2]);
        Parameters[3] = ServerInterface.SanitiseURL(Parameters[3]);
        Parameters[4] = ServerInterface.SanitiseURL(Parameters[4]);
        Parameters[5] = ServerInterface.SanitiseURL(Parameters[5]);
        Parameters[6] = ServerInterface.SanitiseURL(Parameters[6]);
        Parameters[7] = ServerInterface.SanitiseURL(Parameters[7]);
        
        return ServerInterface.ConnectToServer("TrackEnded", Parameters);
    }
}
