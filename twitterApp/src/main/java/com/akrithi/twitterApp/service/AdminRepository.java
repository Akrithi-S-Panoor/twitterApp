package com.akrithi.twitterApp.service;
import com.akrithi.twitterApp.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.sql.SQLException;
import java.util.List;

@Repository
public class AdminRepository
{
    private static final String REGISTER="INSERT INTO ADMIN(admin_id,admin_name) VALUES(?,?)";
    private static final String LOGIN_QUERY = "SELECT * FROM ADMIN WHERE admin_id=?";
    private static final String VIEW_ALL_USERS="SELECT * FROM USER";
    private static final String VIEW_ALL_TWEETS="SELECT hashtag,description,tweet_time,likes FROM TWEET";
    private static final String GET_TWEET_BY_ID="SELECT tweet_id,userId,hashtag,description,tweet_time,likes FROM TWEET WHERE tweet_id=?";
    private static final String DELETE_USER_BY_ID="DELETE FROM USER WHERE userId=?";
    private static final String DELETE_TWEET_BY_ID="DELETE FROM TWEET WHERE tweet_id=?";
    private static final String RETRIEVE_TWEET_PHOTO="SELECT t_photo from TWEET_PHOTO where tweet_id=?";
    private static final String DISPLAY_FEED="SELECT User.userId,username,USER.photo,hashtag,description,likes,Tweet_photo.t_photo from User,Tweet,Tweet_photo where (Tweet.tweet_id=Tweet_photo.tweet_id) or Tweet.userId=User.userId";

    private static final String VIEWALL="SELECT * FROM USER WHERE LIMIT=?,OFFSET=?";
    @Autowired
    private JdbcTemplate jdbcTemplate;

    int offset=0;
    int limit=3;
    public String registerAdmin(Admin admin)
    {
        jdbcTemplate.update(REGISTER,admin.getAdmin_id(),admin.getAdmin_name());
        return "Registered successfully";
    }

    public String login(Admin admin)
    {
        try
        {
            Admin adminn =jdbcTemplate.queryForObject(LOGIN_QUERY,(rs, rowNum) ->{
                return new Admin(rs.getString("admin_id"),rs.getString("admin_name"));
            },admin.getAdmin_id());

            if(adminn!=null)
            {
                return "logged in successfully";
            }
        }
        catch (DataAccessException e)
        {
            return null;
        }
        return null;
    }


//    public Page<User> allUsers()
//    {
//        List<User> users =jdbcTemplate.query(VIEW_ALL_USERS, (rs, rowNum) -> {
//            return new User(rs.getString("userId"),rs.getString("userName"),rs.getBytes("photo"));
//        });
//    }

    public List<User> giveUsers()
    {
        List <User> users= jdbcTemplate.query("select * from user limit ?,?", (rs, rowNum) ->
        {
            return new User(rs.getString("userId"),rs.getString("username"));
        },offset,limit);

        offset=offset+3;

        if(users.size()==0)
        {
            offset=0;
            List <User> users1= jdbcTemplate.query("select * from user limit ?,?", (rs, rowNum) ->
            {
                return new User(rs.getString("userId"),rs.getString("username"));
            },offset,limit);
            return users1;
        }
        return users;
    }

//    public int count() {
//        return jdbcTemplate.queryForObject("SELECT count(*) FROM USER", Integer.class);
//    }
//
//    // defaults sorts by Id if order not provided
//    public Page<User> findAll(Pageable page) {
//
//        //Order order = !page.getSort().isEmpty() ? page.getSort().toList().get(0) : Order.by("ID");
//
//        List<User> users = jdbcTemplate.query("SELECT * FROM USER" + " LIMIT " + page.getPageSize() + " OFFSET " + page.getOffset(),
//                (rs, rowNum) ->{
//                    return new User(rs.getString("userId"),rs.getString("userName"),rs.getBytes("photo"));
//                });
//
//        return new PageImpl<User>(users, page, count());
//    }

    public List<Tweet> allTweets()
    {
        return jdbcTemplate.query(VIEW_ALL_TWEETS, (rs, rowNum) -> {
            return new Tweet(rs.getString("hashtag"),rs.getString("description"),rs.getString("tweet_time"),rs.getInt("likes"));
        });
    }

    public List<DisplayFeed> displayFeed()
    {
        List<DisplayFeed> feeds = jdbcTemplate.query(DISPLAY_FEED, (rs, rowNum) -> {
            return new DisplayFeed("@"+rs.getString("userId"), rs.getString("username"),rs.getBytes("photo"), rs.getString("hashtag"), rs.getString("description"), rs.getInt("likes"),rs.getBytes("t_photo"));
        });

        return feeds;
    }

    public String deleteUserById(String userId)
    {
        User user=  jdbcTemplate.queryForObject(DELETE_TWEET_BY_ID, (rs, rowNum) -> {
            return new User(rs.getString("userId"),rs.getString("username"),rs.getString("email"),rs.getString("password"));
        },userId);

        if(user!=null)
        {
            jdbcTemplate.update(DELETE_USER_BY_ID,userId);
            return "User with user Id "+userId+ " is deleted";
        }
        else
        {
            return "User does not exist";
        }
    }

    public String deleteTweetById(int tweet_id)
    {
        Tweet tweet=  jdbcTemplate.queryForObject(GET_TWEET_BY_ID, (rs, rowNum) -> {
            return new Tweet(rs.getInt("tweet_id"),rs.getString("userId"),rs.getString("hashtag"),rs.getString("description"),rs.getString("tweet_time"),rs.getInt("likes"));
        },tweet_id);

        if(tweet!=null)
        {
            jdbcTemplate.update(DELETE_TWEET_BY_ID,tweet_id);
            return "Tweet with tweet Id "+tweet_id+ " is deleted";
        }
        else
        {
            return "User does not exist";
        }
    }

    public byte[] viewTweetPhoto(int tweet_id) throws SQLException
    {
        try
        {
            Tweet_photo tweet_photo = jdbcTemplate.queryForObject(RETRIEVE_TWEET_PHOTO, (rs, rowNum) ->
            {
                return new Tweet_photo(rs.getBytes("t_photo"));
            },tweet_id);

            if(tweet_photo!=null)
            {
                return tweet_photo.getT_photo();
            }
        }
        catch (Exception e)
        {
            return null;
        }
        return null;
    }
}
