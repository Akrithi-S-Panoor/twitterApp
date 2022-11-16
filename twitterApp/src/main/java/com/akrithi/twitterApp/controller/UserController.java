package com.akrithi.twitterApp.controller;

import com.akrithi.twitterApp.entity.*;
import com.akrithi.twitterApp.service.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController
{
    @Autowired
    UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<String> Register(@RequestBody User user) throws IOException {
        try
        {
            String reg_msg =userRepository.registerUser(user);
            return new ResponseEntity<String>(reg_msg, HttpStatus.OK);
        } catch (Exception e)
        {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/photo")
    public ResponseEntity<String> uploadPhoto(@RequestParam String userId , @RequestParam MultipartFile photo) throws IOException
    {
        try
        {
            String upload_msg=userRepository.uploadPhoto(userId,photo);

            if(upload_msg!=null)
            {
                return new ResponseEntity<String>(upload_msg, HttpStatus.OK);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @GetMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user)
    {
        try
        {
            String log_msg=userRepository.login(user);

            if(log_msg!=null)
            {
                return new ResponseEntity<String>(log_msg, HttpStatus.OK);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @DeleteMapping("/logout/{userId}")
    public ResponseEntity<String> logout(@PathVariable("userId") String userId)
    {
        try
        {
            String logout_msg=userRepository.logout(userId);
            return new ResponseEntity<String>(logout_msg, HttpStatus.OK);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/tweet/{login_id}")
    public ResponseEntity<String> tweet(@RequestBody Tweet tweet,@PathVariable("login_id") int login_id)
    {
        try
        {
            String tweet_msg = userRepository.tweet(tweet,login_id);
            return new ResponseEntity<String>(tweet_msg, HttpStatus.OK);
        } catch (Exception e)
        {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/like/{login_id}")
    public ResponseEntity<String> like(@RequestBody Tweet tweet,@PathVariable("login_id") int login_id)
    {
        try
        {
            String like_msg=userRepository.like(tweet,login_id);
            return new ResponseEntity<String>(like_msg, HttpStatus.OK);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/unlike/{login_id}")
    public ResponseEntity<String> unlike(@RequestBody Tweet tweet,@PathVariable("login_id") int login_id)
    {
        try
        {
            String unlike_msg=userRepository.unlike(tweet,login_id);
            return new ResponseEntity<String>(unlike_msg, HttpStatus.OK);
        } catch (Exception e)
        {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/postPhoto")
    public ResponseEntity<String> postPhoto(@RequestParam int tweet_id , @RequestParam MultipartFile t_photo) throws IOException
    {
        try
        {
            String post_msg=userRepository.uploadPost(tweet_id,t_photo);
            return new ResponseEntity<String>(post_msg, HttpStatus.OK);

        } catch (Exception e)
        {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/replyPhoto")
    public ResponseEntity<String> replyPhoto(@RequestParam String reply_id , @RequestParam MultipartFile r_photo) throws IOException
    {
        try
        {
            String reply_photo_msg=userRepository.uploadReplyPhoto(reply_id,r_photo);
            return new ResponseEntity<String>(reply_photo_msg, HttpStatus.OK);


        } catch (Exception e)
        {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/feeds/{login_id}")
    public ResponseEntity<List<DisplayFeed>> displayFeeds(@PathVariable("login_id") int login_id)
    {

        List<DisplayFeed> feeds= userRepository.displayFeed(login_id);
        if(feeds==null)
        {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.of(Optional.of(feeds));

    }

    @GetMapping("/hashtags/{login_id}/{hashtag}")
    public ResponseEntity<List<DisplayFeed>> onClickHash(@PathVariable("login_id") String login_id, @PathVariable("hashtag") String hashtag)
    {

        List<DisplayFeed> hash_msg= userRepository.onClickHashtag(login_id,hashtag);
        if(hash_msg==null)
        {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.of(Optional.of(hash_msg));

    }

    @PostMapping("/reply/{login_id}")
    public ResponseEntity<String> reply(@RequestBody Tweet_details tweet_details, @PathVariable("login_id") int login_id)
    {
        try
        {
            String reply_msg=userRepository.doReply(tweet_details,login_id);
            return new ResponseEntity<String>(reply_msg, HttpStatus.CREATED);

        } catch (Exception e)
        {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @PutMapping("/likeReply/{login_id}")
    public ResponseEntity<String> likeReply(@RequestBody Tweet_details tweet_details,@PathVariable("login_id") int login_id)
    {
        try
        {
            String like_reply_msg=userRepository.likeReply(tweet_details,login_id);
            return new ResponseEntity<String>(like_reply_msg, HttpStatus.OK);

        } catch (Exception e)
        {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @PutMapping("/unlikeReply/{login_id}")
    public ResponseEntity<String> unlikeReply(@RequestBody Tweet_details tweet_details,@PathVariable("login_id") int login_id)
    {
        try
        {
            String unlike_reply_msg=userRepository.unlikeReply(tweet_details,login_id);
            return new ResponseEntity<String>(unlike_reply_msg, HttpStatus.OK);


        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();        }

    }

    @PostMapping("/follow/{login_id}")
    public ResponseEntity<String> reply(@RequestBody Following following, @PathVariable("login_id") int login_id)
    {
        try
        {
            String follow_msg=userRepository.follow(following,login_id);
            return new ResponseEntity<String>(follow_msg, HttpStatus.OK);

        } catch (Exception e)
        {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/atTag/{login_id}/{userId}")
    public ResponseEntity<String> onClickAtTag(@PathVariable("login_id") int login_id, @PathVariable("userId") String userId)
    {
        try
        {
            String at_tag_msg=userRepository.onClickAtTag(login_id,userId);
            return new ResponseEntity<String>(at_tag_msg, HttpStatus.FOUND);

        } catch (Exception e)
        {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/followers/{login_id}/{userId}")
    public ResponseEntity<List<User>> follower(@PathVariable("login_id") int login_id, @PathVariable("userId") String userId)
    {
        List<User> followerList= userRepository.viewFollowers(login_id,userId);
            if(followerList==null)
            {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
            return ResponseEntity.of(Optional.of(followerList));
    }

    @GetMapping("/followings/{login_id}/{userId}")
    public ResponseEntity<List<User>> following(@PathVariable("login_id") int login_id, @PathVariable("userId") String userId)
    {
        List<User> followingList= userRepository.viewFollowing(login_id,userId);
        if(followingList==null)
        {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.of(Optional.of(followingList));
    }

    @DeleteMapping("/unfollow/{login_id}")
    public ResponseEntity<String> unfollow(@RequestBody  Following following,@PathVariable("login_id") int login_id)
    {
        try
        {
            String unfollow_msg=userRepository.unFollow(following,login_id);
            return new ResponseEntity<String>(unfollow_msg, HttpStatus.FOUND);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/reply_details/{login_id}/{tweet_id}")
    public ResponseEntity<String> viewReplies(@PathVariable("login_id") int login_id, @PathVariable("tweet_id") int tweet_id)
    {
        try
        {
            String reply_msg=userRepository.viewReplyDetails(login_id,tweet_id);
            return new ResponseEntity<String>(reply_msg, HttpStatus.FOUND);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();        }
    }

    @GetMapping("/myTweets/{login_id}/{userId}")
    public ResponseEntity<String> myTweets(@PathVariable("login_id") int login_id, @PathVariable("userId") String userId)
    {
        try
        {
            String my_tweets_msg=userRepository.myTweets(login_id,userId);
            return new ResponseEntity<String>(my_tweets_msg, HttpStatus.FOUND);

        }
        catch (Exception e)
        {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/viewProfile/{userId}")
    public ResponseEntity<byte[]> viewProfilePhoto(@PathVariable String userId) throws SQLException
    {
        byte[] p_photo=  userRepository.viewProfilePhoto(userId);
        if(p_photo!=null)
        {
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(p_photo);

        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

}
