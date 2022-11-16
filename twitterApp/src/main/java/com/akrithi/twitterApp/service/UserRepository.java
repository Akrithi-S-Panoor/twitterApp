package com.akrithi.twitterApp.service;

import com.akrithi.twitterApp.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Repository
public class UserRepository
{
    private static final String REGISTER="INSERT INTO USER(userId,username,email,password) VALUES(?,?,?,?)";
    private static final String UPLOAD_PHOTO="UPDATE USER SET photo=? WHERE userId=?";
    private static final String CHECK_USER_EXISTANCE="SELECT userId,username,email,password FROM USER WHERE userId=?";
    private static final String LOGIN_QUERY = "SELECT * FROM USER WHERE password=?";
    private static final String INSERT_LOG ="INSERT INTO LOGIN(userId) VALUES(?)";
    private static final String LOGOUT="DELETE FROM LOGIN WHERE userId=?";
    private static final String TWEET="INSERT INTO TWEET(userId,hashtag,description,tweet_time) VALUES(?,?,?,?)";
    private static final String LOGIN_CHECK = "SELECT * FROM LOGIN WHERE login_id=?";
    private static final String LIKE ="UPDATE TWEET SET likes=? WHERE tweet_id=?";
    private static final String LIKE_REPLY ="UPDATE TWEET_DETAILS SET reply_likes=? WHERE reply_id=?";
    private static final String RETRIEVE="SELECT likes from TWEET WHERE tweet_id=?";
    private static final String RETRIEVE_REPLY="SELECT reply_likes from TWEET_DETAILS WHERE reply_id=?";
    private static final String CHECK_TWEET="SELECT tweet_id FROM TWEET WHERE tweet_id=?";
    private static final String CHECK_REPLY="SELECT reply_id FROM TWEET_DETAILS WHERE reply_id=?";
    private static final String UPLOAD_POST_PHOTO="INSERT INTO TWEET_PHOTO(tweet_id,t_photo) VALUES(?,?)";
    private static final String UPLOAD_REPLY_PHOTO="INSERT INTO REPLY_PHOTO(reply_id,r_photo) VALUES(?,?)";
    private static final String DISPLAY_FEED="SELECT User.userId,username,USER.photo,hashtag,description,likes,Tweet_photo.t_photo from User,Tweet,Tweet_photo where (Tweet.tweet_id=Tweet_photo.tweet_id) or Tweet.userId=User.userId";
    private static final String REPLY="INSERT INTO TWEET_DETAILS(reply_id,tweet_id,userId,reply,reply_time) VALUES(?,?,?,?,?)";
    private static final String ON_CLICK_HASHTAG="SELECT User.userId,username,USER.photo,hashtag,description,likes,Tweet_photo.t_photo from User,Tweet,Tweet_photo where Tweet.tweet_id=Tweet_photo.tweet_id and Tweet.userId=User.userId and Tweet.hashtag=?";
    private static final String ON_CLICK_AT_TAG="SELECT User.userId,username,USER.photo,hashtag,description,likes,Tweet_photo.t_photo from User,Tweet,Tweet_photo where (Tweet.userId=User.userId and User.userId=?) and Tweet.tweet_id=Tweet_photo.tweet_id";
    private static final String FOLLOW="INSERT INTO FOLLOWING(following_id,userId) VALUES(?,?)";
    private static final String FOLLOWING="INSERT INTO FOLLOWER(follower_id,userId) VALUES(?,?)";
    private static final String FOLLOWER_COUNT="SELECT COUNT(follower_id) FROM FOLLOWER WHERE userId=?";
    private static final String FOLLOWING_COUNT="SELECT COUNT(following_id) FROM FOLLOWING WHERE userId=?";
    private static final String GET_FOLLOWERS="SELECT User.userId,username,photo from User,follower where User.userId=follower.follower_id and follower.userId=?";
    private static final String GET_FOLLOWING="SELECT User.userId,username,photo from User,following where User.userId=following.following_id and following.userId=?";
    private static final String UNFOLLOW="DELETE FROM FOLLOWING WHERE following_id=? and userId=?";
    private static final String REDUCE_FOLLOWER="DELETE FROM FOLLOWER WHERE follower_id=? and userId=?";
    private static final String GET_TWEETS="SELECT User.userId,userName,photo,hashtag,description,likes,tweet_time from User ,Tweet  WHERE Tweet.userId=User.userId and tweet_id=?";
    private static final String GET_TWEET_PHOTO="SELECT t_photo from tweet_photo WHERE tweet_id=?";
    private static final String GET_REPLIES="SELECT User.userId,username,User.photo,reply,reply_time,reply_likes from tweet_details,User where Tweet_details.userId=User.userId and Tweet_details.tweet_id=?";
    private static final String GET_REPLY_PHOTO="SELECT r_photo from reply_photo r,Tweet t,tweet_details td WHERE td.tweet_id=t.tweet_id and td.reply_id=r.reply_id and t.tweet_id=?";
    private static final String MY_TWEET="SELECT userId,hashtag,description,tweet_time,likes FROM Tweet WHERE userId=?";
    private static final String MY_TWEET_PHOTO="SELECT t_photo FROM Tweet_photo tp,Tweet t WHERE t.userId=? and tp.tweet_id=t.tweet_id";
    private static final String RETRIEVE_PHOTO="SELECT photo from USER where userId=?";

    @Autowired
    private JdbcTemplate jdbcTemplate;


    public String registerUser(User user) throws IOException
    {
        user.getPassword();
        jdbcTemplate.update(REGISTER,user.getUserId(),user.getUsername(),user.getEmail(),user.getPassword());
        return "Registered successfully";
    }

    public String uploadPhoto(String userId, MultipartFile photo) throws IOException,DataAccessException
    {
        try
        {
            User user=  jdbcTemplate.queryForObject(CHECK_USER_EXISTANCE, (rs, rowNum) -> {
                return new User(rs.getString("userId"),rs.getString("username"),rs.getString("email"),rs.getString("password"));
            },userId);

            if(user!=null)
            {
                jdbcTemplate.update(UPLOAD_PHOTO ,photo.getBytes(),userId);
                return "Image uploaded successfully";
            }
        } catch (DataAccessException e)
        {
            return null;
        }
        return null;
    }

    public String login(User user)
    {
        try
        {
            User userr =jdbcTemplate.queryForObject(LOGIN_QUERY,(rs, rowNum) ->{
                return new User(rs.getString("userId"),rs.getString("username"),rs.getString("email"),rs.getString("password"));
            },user.getPassword());
            if(userr!=null)
            {
                jdbcTemplate.update(INSERT_LOG,user.getUserId());
                return "Log in success";
            }
        } catch (DataAccessException e)
        {
            return null;
        }
        return null;
    }


    public String logout(String userId)
    {
        jdbcTemplate.update(LOGOUT,userId);
        return "Logged out successfully";
    }


    public String tweet(Tweet tweet,int login_id)
    {
        try
        {
            Login login =jdbcTemplate.queryForObject(LOGIN_CHECK,(rs, rowNum) ->{
                return new Login(rs.getInt("login_id"),rs.getString("userId"));
            },login_id);

            if(login!=null)
            {
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                LocalDateTime tweetTime = LocalDateTime.now();

                jdbcTemplate.update(TWEET,tweet.getUserId(),tweet.getHashtag(),tweet.getDescription(),tweetTime);
                return "tweeted successfully";
            }

        } catch (Exception e) {
            return "not logged in";
        }

        return "cannot tweet";
    }


    public String like(Tweet tweet, int login_id)
    {
        try
        {
            Login login =jdbcTemplate.queryForObject(LOGIN_CHECK,(rs, rowNum) ->{
                return new Login(rs.getInt("login_id"),rs.getString("userId"));
            },login_id);


            if(login!=null)
            {
                int like= jdbcTemplate.queryForObject(RETRIEVE,new Object[]{tweet.getTweet_id()}, Integer.class);
                jdbcTemplate.update(LIKE,new Object[]{like+1,tweet.getTweet_id()});
                return "liked successfully";
            }

        } catch (Exception e) {
            return "user is not logged in";
        }
        return "Cannot like";
    }


    public String unlike(Tweet tweet,int login_id)
    {
        try
        {
            Login login =jdbcTemplate.queryForObject(LOGIN_CHECK,(rs, rowNum) ->{
                return new Login(rs.getInt("login_id"),rs.getString("userId"));
            },login_id);


            if(login!=null)
            {
                int like= jdbcTemplate.queryForObject(RETRIEVE,new Object[]{tweet.getTweet_id()}, Integer.class);
                jdbcTemplate.update(LIKE,new Object[]{like-1,tweet.getTweet_id()});
                return "Unliked successfully";
            }

        } catch (Exception e) {
            return "user is not logged in";
        }
        return "Cannot Unlike";
    }


    public String uploadPost(int tweet_id, MultipartFile t_photo) throws IOException
    {
        try
        {
            int tweetId= jdbcTemplate.queryForObject(CHECK_TWEET,new Object[]{tweet_id}, Integer.class);

            if(tweetId!=0)
            {
                jdbcTemplate.update(UPLOAD_POST_PHOTO ,tweet_id,t_photo.getBytes());
                return "Image posted successfully";
            }
        } catch (DataAccessException e)
        {
            return "no tweet found";
        }
        return "Invalid credentials";
    }


    public String uploadReplyPhoto(String  reply_id, MultipartFile r_photo) throws IOException
    {
        try
        {
            String replyId= jdbcTemplate.queryForObject(CHECK_REPLY,new Object[]{reply_id}, String.class);

            System.out.println();
            if(replyId!=null)
            {
                jdbcTemplate.update(UPLOAD_REPLY_PHOTO ,reply_id,r_photo.getBytes());
                return "Image posted successfully";
            }
        } catch (DataAccessException e)
        {
            return "no reply found";
        }
        return "Invalid credentials";
    }


    public List<DisplayFeed> displayFeed(int login_id)
    {
        try {
            Login login = jdbcTemplate.queryForObject(LOGIN_CHECK, (rs, rowNum) -> {
                return new Login(rs.getInt("login_id"), rs.getString("userId"));
            }, login_id);


            if (login != null)
            {
                List<DisplayFeed> feeds = jdbcTemplate.query(DISPLAY_FEED, (rs, rowNum) -> {
                    return new DisplayFeed("@"+rs.getString("userId"), rs.getString("username"),rs.getBytes("photo"), rs.getString("hashtag"), rs.getString("description"), rs.getInt("likes"),rs.getBytes("t_photo"));
                });

                return feeds;

            }
        }
        catch (DataAccessException e)
        {
            return null;
        }
        return null;
    }


    public List<DisplayFeed> onClickHashtag(String login_id,String hashtag)
    {
        try
        {
            Login login = jdbcTemplate.queryForObject(LOGIN_CHECK, (rs, rowNum) -> {
                return new Login(rs.getInt("login_id"), rs.getString("userId"));
            }, login_id);

            if(login!=null)
            {
                String hashtagWithHash="#"+hashtag;
                List<DisplayFeed> hash = jdbcTemplate.query(ON_CLICK_HASHTAG, (rs, rowNum) -> {
                    return new DisplayFeed("@"+rs.getString("userId"), rs.getString("username"),rs.getBytes("photo"), rs.getString("hashtag"), rs.getString("description"), rs.getInt("likes"),rs.getBytes("t_photo"));
                },hashtagWithHash);

                return hash;
            }

        } catch (DataAccessException e) {
            return null;
        }

        return null;
    }


    public String doReply(Tweet_details tweet_details, int login_id)
    {
        try
        {
            Login login =jdbcTemplate.queryForObject(LOGIN_CHECK,(rs, rowNum) ->{
                return new Login(rs.getInt("login_id"),rs.getString("userId"));
            },login_id);

            if(login!=null)
            {

                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                LocalDateTime replyTime = LocalDateTime.now();

                jdbcTemplate.update(REPLY,tweet_details.getReply_id(),tweet_details.getTweet_id(),tweet_details.getUserId(),tweet_details.getReply(),replyTime);
                return "replied successfully";
            }

        } catch (Exception e) {
            return "not logged in";
        }

        return "cannot reply";
    }


    public String likeReply(Tweet_details tweet_details, int login_id)
    {
        try
        {
            Login login =jdbcTemplate.queryForObject(LOGIN_CHECK,(rs, rowNum) ->{
                return new Login(rs.getInt("login_id"),rs.getString("userId"));
            },login_id);


            if(login!=null)
            {
                int reply_like= jdbcTemplate.queryForObject(RETRIEVE_REPLY,new Object[]{tweet_details.getReply_id()}, Integer.class);
                jdbcTemplate.update(LIKE_REPLY,new Object[]{reply_like+1,tweet_details.getReply_id()});
                return "liked reply successfully";
            }

        } catch (Exception e) {
            return "user is not logged in";
        }
        return "Cannot like reply";
    }


    public String unlikeReply(Tweet_details tweet_details,int login_id)
    {
        try
        {
            Login login =jdbcTemplate.queryForObject(LOGIN_CHECK,(rs, rowNum) ->{
                return new Login(rs.getInt("login_id"),rs.getString("userId"));
            },login_id);


            if(login!=null)
            {
                int replyLikes= jdbcTemplate.queryForObject(RETRIEVE_REPLY,new Object[]{tweet_details.getReply_id()}, Integer.class);
                jdbcTemplate.update(LIKE_REPLY,new Object[]{replyLikes-1,tweet_details.getReply_id()});
                return "Unliked reply successfully";
            }

        } catch (Exception e) {
            return "user is not logged in";
        }
        return "Cannot Unlike reply";
    }


    public String follow(Following following,int login_id)
    {
        try {
            Login login = jdbcTemplate.queryForObject(LOGIN_CHECK, (rs, rowNum) -> {
                return new Login(rs.getInt("login_id"), rs.getString("userId"));
            }, login_id);



            User user = jdbcTemplate.queryForObject(CHECK_USER_EXISTANCE, (rs, rowNum) -> {
                return new User(rs.getString("userId"),rs.getString("username"),rs.getString("email"),rs.getString("password"));
            }, following.getFollowing_id());



            if(login!=null && user!=null)
            {

                jdbcTemplate.update(FOLLOW,following.getFollowing_id(),following.getUserId());
                jdbcTemplate.update(FOLLOWING,following.getUserId(),following.getFollowing_id());

                return "followed successfully";
            }

        } catch (DataAccessException e) {
            return "no user exist";
        }
        return "something is wrong";
    }


    public String unFollow(Following following,int login_id)
    {
        try
        {
            Login login = jdbcTemplate.queryForObject(LOGIN_CHECK, (rs, rowNum) -> {
                return new Login(rs.getInt("login_id"), rs.getString("userId"));
            }, login_id);

            User user = jdbcTemplate.queryForObject(CHECK_USER_EXISTANCE, (rs, rowNum) -> {
                return new User(rs.getString("userId"),rs.getString("username"),rs.getString("email"),rs.getString("password"));
            }, following.getFollowing_id());


            if(login!=null && user!=null)
            {

                jdbcTemplate.update(UNFOLLOW,following.getFollowing_id(),following.getUserId());
                jdbcTemplate.update(REDUCE_FOLLOWER,following.getUserId(),following.getFollowing_id());

                return "unfollowed successfully";
            }

        } catch (Exception e) {
            return "user not logged in";
        }
        return "something is wrong";
    }


    public String onClickAtTag(int login_id,String userId)
    {
        try
        {
            Login login = jdbcTemplate.queryForObject(LOGIN_CHECK, (rs, rowNum) -> {
                return new Login(rs.getInt("login_id"), rs.getString("userId"));
            }, login_id);

            if(login!=null)
            {
                List<DisplayFeed> hash = jdbcTemplate.query(ON_CLICK_AT_TAG, (rs, rowNum) -> {
                    return new DisplayFeed("@"+rs.getString("userId"), rs.getString("username"),rs.getBytes("photo"), rs.getString("hashtag"), rs.getString("description"), rs.getInt("likes"),rs.getBytes("t_photo"));
                },userId);

                int follower_count= jdbcTemplate.queryForObject(FOLLOWER_COUNT,new Object[]{userId}, Integer.class);

                int following_count= jdbcTemplate.queryForObject(FOLLOWING_COUNT,new Object[]{userId}, Integer.class);

                return ""+hash+"\nfollower count"+follower_count+"\nfollowing count"+following_count;
            }

        } catch (DataAccessException e) {
            return "Not logged in";
        }
        return "something's wrong";
    }

    public List<User> viewFollowers(int login_id,String userId)
    {
        try
        {
            Login login = jdbcTemplate.queryForObject(LOGIN_CHECK, (rs, rowNum) -> {
                return new Login(rs.getInt("login_id"), rs.getString("userId"));
            }, login_id);

            if (login != null)
            {
                List<User> followers = jdbcTemplate.query(GET_FOLLOWERS, (rs, rowNum) -> {
                    return new User("@" + rs.getString("userId"), rs.getString("username"), rs.getBytes("photo"));
                }, userId);

                return followers;
            }
        }
        catch (DataAccessException e)
        {
            return null;
        }
        return null;
    }


    public List<User> viewFollowing(int login_id,String userId)
    {
        try
        {
            Login login = jdbcTemplate.queryForObject(LOGIN_CHECK, (rs, rowNum) -> {
                return new Login(rs.getInt("login_id"), rs.getString("userId"));
            }, login_id);

            if (login != null)
            {
                List<User> following = jdbcTemplate.query(GET_FOLLOWING, (rs, rowNum) -> {
                    return new User("@" + rs.getString("userId"), rs.getString("username"), rs.getBytes("photo"));
                }, userId);

                return following;
            }
        }
        catch (DataAccessException e)
        {
            return null;
        }
        return null;
    }


    public String viewReplyDetails(int login_id,int tweet_id)
    {
        try
        {
            Login login = jdbcTemplate.queryForObject(LOGIN_CHECK, (rs, rowNum) -> {
                return new Login(rs.getInt("login_id"), rs.getString("userId"));
            }, login_id);

            if (login != null)
            {
                List<DisplayFeed> tweet = jdbcTemplate.query(GET_TWEETS, (rs, rowNum) -> {
                    return new DisplayFeed("@" + rs.getString("userId"), rs.getString("username"), rs.getBytes("photo"),rs.getString("hashtag"),rs.getString("description"),rs.getInt("likes"),rs.getString("tweet_time"));
                },tweet_id);


                List<Tweet_photo> tweet_photo = jdbcTemplate.query(GET_TWEET_PHOTO, (rs, rowNum) -> {
                    return new Tweet_photo(rs.getBytes("t_photo"));
                },tweet_id);


                List<DisplayFeed> reply = jdbcTemplate.query(GET_REPLIES, (rs, rowNum) -> {
                    return new DisplayFeed("@" + rs.getString("userId"), rs.getString("username"), rs.getBytes("photo"),rs.getString("reply"),rs.getString("reply_time"),rs.getInt("reply_likes"));
                },tweet_id);

                List<Reply_photo> reply_photo = jdbcTemplate.query(GET_REPLY_PHOTO, (rs, rowNum) -> {
                    return new Reply_photo(rs.getBytes("r_photo"));
                },tweet_id);

                return "tweet\n\n"+tweet+"tweet photo\n\n"+tweet_photo+"reply\n\n "+reply+"photo\n\n "+reply_photo;
            }
        }
        catch (Exception e)
        {
            return "user not logged in";
        }

        return "something's wrong";
    }


    public String myTweets(int login_id,String userId)
    {
        try
        {
            Login login = jdbcTemplate.queryForObject(LOGIN_CHECK, (rs, rowNum) -> {
                return new Login(rs.getInt("login_id"), rs.getString("userId"));
            }, login_id);

            if(login!=null)
            {

                List<Tweet> tweets=jdbcTemplate.query(MY_TWEET,(rs, rowNum) ->{
                    return new Tweet(rs.getString("userId"),rs.getString("hashtag"),rs.getString("description"),rs.getString("tweet_time"),rs.getInt("likes"));
                },userId);


                List<Tweet_photo> tweet_photos = jdbcTemplate.query(MY_TWEET_PHOTO,(rs, rowNum) -> {
                    return new Tweet_photo(rs.getBytes("t_photo"));
                },userId);

                return " "+tweets+" "+tweet_photos;
            }
        }
        catch (Exception e)
        {
            return "user not logged in";
        }
        return "something's wrong";
    }


    public byte[] viewProfilePhoto(String userId) throws SQLException
    {
        try
        {
            User user = jdbcTemplate.queryForObject(RETRIEVE_PHOTO, (rs, rowNum) ->
            {
                return new User(rs.getBytes("photo"));
            },userId);

            if(user!=null)
            {
                return user.getPhoto();
            }
        }
        catch (Exception e)
        {
            return null;
        }
        return null;
    }
}


