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
    @Path("TrackStarted/{UserID}/{TrackName}/{Genre}/{Artist}/{Length}/"
            + "{BeforeMood}")
    @Produces({ MediaType.TEXT_PLAIN })
    public String TrackStarted(@PathParam("UserID") String UserID,
            @PathParam("TrackName") String TrackName,
            @PathParam("Genre") String Genre,
            @PathParam("Artist") String Artist,
            @PathParam("Length") String Length,
            @PathParam("BeforeMood") String BeforeMood)
    {
        String[] Parameters = new String[6];
        Parameters[0] = UserID;
        Parameters[1] = TrackName;
        Parameters[2] = Genre;
        Parameters[3] = Artist;
        Parameters[4] = Length;
        Parameters[5] = BeforeMood;
        
        ServerInterface.RemoveDashFromString(Parameters[0]);
        ServerInterface.RemoveDashFromString(Parameters[1]);
        ServerInterface.RemoveDashFromString(Parameters[2]);
        ServerInterface.RemoveDashFromString(Parameters[3]);
        ServerInterface.RemoveDashFromString(Parameters[4]);
        ServerInterface.RemoveDashFromString(Parameters[5]);
        
        return ServerInterface.ConnectToServer("TrackStarted", Parameters);
    }
    
    @GET
    @Path("TrackEnded/{MoodID}/{AfterMood}/{UserLiked}/{DiaryEntryText}")
    @Produces({ MediaType.TEXT_PLAIN })
    public String TrackEnded(@PathParam("MoodID") String MoodID,
            @PathParam("AfterMood") String AfterMood,
            @PathParam("UserLiked") String UserLiked,
            @PathParam("DiaryEntryText") String DiaryEntryText)
    {
        String[] Parameters = new String[4];
        Parameters[0] = MoodID;
        Parameters[1] = AfterMood;
        Parameters[2] = UserLiked;
        Parameters[3] = DiaryEntryText;
        
        return ServerInterface.ConnectToServer("TrackEnded", Parameters);
    }
}
