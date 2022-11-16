package com.akrithi.twitterApp.controller;

import com.akrithi.twitterApp.entity.Admin;
import com.akrithi.twitterApp.entity.DisplayFeed;
import com.akrithi.twitterApp.entity.Tweet;
import com.akrithi.twitterApp.entity.User;
import com.akrithi.twitterApp.service.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/admin")
public class AdminController
{
    @Autowired
    AdminRepository adminRepository;

    @PostMapping("/register")
    public ResponseEntity<String> Register(@RequestBody Admin admin)
    {
        try
        {
            String  str= adminRepository.registerAdmin(admin);
            return new ResponseEntity<String>(str, HttpStatus.CREATED);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @GetMapping("/login")
    public ResponseEntity<String> login(@RequestBody Admin admin)
    {
        try
        {
            String str1 = adminRepository.login(admin);
            if(str1!=null)
            {
                return new ResponseEntity<String>(str1, HttpStatus.OK);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> allUser()
    {
        try
        {
            List<User> userList=adminRepository.giveUsers();
            return new ResponseEntity<List<User>>(userList, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

//
//    @GetMapping("/users")
//    public ResponseEntity<Page<User>> allUser(Pageable page)
//    {
//        try
//        {
//            PageRequest pageable = PageRequest.of(1, 3);
//            Page<User> userList=adminRepository.findAll(page);
//            return new ResponseEntity<Page<User>>(userList, HttpStatus.OK);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }




    @GetMapping("/tweets")
    public ResponseEntity<List<Tweet>> allTweet()
    {
        try
        {
            List<Tweet> tweetList=adminRepository.allTweets();
            return new ResponseEntity<List<Tweet>>(tweetList, HttpStatus.OK);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/feeds")
    public ResponseEntity<List<DisplayFeed>> displayFeeds()
    {

        List<DisplayFeed> feeds= adminRepository.displayFeed();
        if(feeds==null)
        {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.of(Optional.of(feeds));

    }

    @DeleteMapping("/DeleteUser/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable("userId") String userId)
    {
        try
        {
            String str2=adminRepository.deleteUserById(userId);
            return new ResponseEntity<String>(str2, HttpStatus.OK);
        }
        catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @DeleteMapping("/DeleteTweet/{tweet_id}")
    public ResponseEntity<String> deleteTweet(@PathVariable("tweet_id") int tweet_id)
    {
        try
        {
            String tweet_delete=adminRepository.deleteTweetById(tweet_id);
            return new ResponseEntity<String>(tweet_delete, HttpStatus.OK);
        }
        catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @GetMapping("/viewTweetPhoto/{tweet_id}")
    public ResponseEntity<byte[]> viewTweetPhoto(@PathVariable int tweet_id) throws SQLException
    {
        byte[] tweet_photo=  adminRepository.viewTweetPhoto(tweet_id);
        if(tweet_photo!=null)
        {
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(tweet_photo);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

    }

}
