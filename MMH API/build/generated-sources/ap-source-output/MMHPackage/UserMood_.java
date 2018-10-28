package MMHPackage;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-10-28T18:53:11")
@StaticMetamodel(UserMood.class)
public class UserMood_ { 

    public static volatile SingularAttribute<UserMood, Integer> moodID;
    public static volatile SingularAttribute<UserMood, String> moodBefore;
    public static volatile SingularAttribute<UserMood, Integer> trackID;
    public static volatile SingularAttribute<UserMood, Date> moodBeforeTime;
    public static volatile SingularAttribute<UserMood, String> moodAfter;
    public static volatile SingularAttribute<UserMood, String> hasBeenRecommended;
    public static volatile SingularAttribute<UserMood, Integer> userID;
    public static volatile SingularAttribute<UserMood, Date> moodAfterTime;
    public static volatile SingularAttribute<UserMood, String> userLiked;

}