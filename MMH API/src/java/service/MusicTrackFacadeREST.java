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
    @Path("TrackStarted%2F{TrackName}%2F{Genre}%2F{Artist}%2F{Length}%2F"
            + "{BeforeMood}%2F{UserID}%2F{UserPassword}")
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
        
        Parameters[0] = ServerInterface.RemoveDashFromString(Parameters[0]);
        Parameters[1] = ServerInterface.RemoveDashFromString(Parameters[1]);
        Parameters[2] = ServerInterface.RemoveDashFromString(Parameters[2]);
        Parameters[3] = ServerInterface.RemoveDashFromString(Parameters[3]);
        Parameters[4] = ServerInterface.RemoveDashFromString(Parameters[4]);
        Parameters[5] = ServerInterface.RemoveDashFromString(Parameters[5]);
        Parameters[6] = ServerInterface.RemoveDashFromString(Parameters[6]);
        
        return ServerInterface.ConnectToServer("TrackStarted", Parameters);
    }
    
    @GET
    @Path("TrackEnded%2F{MoodID}%2F{AfterMood}%2F{UserLiked}%2F{DiaryEntryOne}%2F"
            + "{DiaryEntryTwo}%2F{DiaryEntryThree}%2F{UserID}%2F{UserPassword}")
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
        
        Parameters[0] = ServerInterface.RemoveDashFromString(Parameters[0]);
        Parameters[1] = ServerInterface.RemoveDashFromString(Parameters[1]);
        Parameters[2] = ServerInterface.RemoveDashFromString(Parameters[2]);
        Parameters[3] = ServerInterface.RemoveDashFromString(Parameters[3]);
        Parameters[4] = ServerInterface.RemoveDashFromString(Parameters[4]);
        Parameters[5] = ServerInterface.RemoveDashFromString(Parameters[5]);
        Parameters[6] = ServerInterface.RemoveDashFromString(Parameters[6]);
        Parameters[7] = ServerInterface.RemoveDashFromString(Parameters[7]);
        
        return ServerInterface.ConnectToServer("TrackEnded", Parameters);
    }
}
