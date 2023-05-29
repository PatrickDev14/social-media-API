package com.cooksys.twitterAPI.services.impl;

import com.cooksys.twitterAPI.dtos.CredentialsDto;
import com.cooksys.twitterAPI.dtos.TweetResponseDto;
import com.cooksys.twitterAPI.dtos.UserRequestDto;
import com.cooksys.twitterAPI.dtos.UserResponseDto;
import com.cooksys.twitterAPI.entities.Tweet;
import com.cooksys.twitterAPI.entities.User;
import com.cooksys.twitterAPI.exceptions.BadRequestException;
import com.cooksys.twitterAPI.exceptions.NotAuthorizedException;
import com.cooksys.twitterAPI.exceptions.NotFoundException;
import com.cooksys.twitterAPI.mappers.CredentialsMapper;
import com.cooksys.twitterAPI.mappers.TweetMapper;
import com.cooksys.twitterAPI.mappers.UserMapper;
import com.cooksys.twitterAPI.repositories.TweetRepository;
import com.cooksys.twitterAPI.repositories.UserRepository;
import com.cooksys.twitterAPI.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	@Autowired
	private final UserMapper userMapper;
	@Autowired
	private final CredentialsMapper credentialsMapper;
	@Autowired
	private final TweetMapper tweetMapper;
	@Autowired
	private final UserRepository userRepository;
	@Autowired
	private final TweetRepository tweetRepository;
	@Autowired
	private final ValidateServiceImpl validateServiceImpl;

	// HELPER METHOD TO CHECK IF USER EXISTS
	public User getUserEntity(String username) throws NotFoundException {
		Optional<User> optionalUser = userRepository.findByCredentialsUsernameAndDeletedFalse(username);
		if (!validateServiceImpl.usernameExists(username)
				|| userRepository.findByCredentialsUsernameAndDeletedFalse(username).isEmpty()
				|| userRepository.findByCredentialsUsernameAndDeletedFalse(username).get().isDeleted()) {
			throw new NotFoundException("User not found with username: " + username);
		}
		return optionalUser.get();
	}

	// GET - ALL USERS
	@Override
	public List<UserResponseDto> getAllUsers() {

		List<User> userList = userRepository.findAllByDeletedFalse();
		List<UserResponseDto> userDtoList = new ArrayList<>();
		for (User user : userList) {
			UserResponseDto userDto = userMapper.entityToDto(user);
			userDto.setUsername(user.getCredentials().getUsername());
			userDtoList.add(userDto);
		}
		return userDtoList;
	}

	// POST - FOLLOW USER
	@Override
	public void followUser(CredentialsDto credentialsDto, String username)
			throws NotAuthorizedException, BadRequestException {
		boolean checker = false;
		for (User userCheck : userRepository.findAll()) {
			if (credentialsMapper.dtoToEntity(credentialsDto).equals(userCheck.getCredentials())) {
				User followerCheck = getUserEntity(credentialsDto.getUsername());
				if (getUserEntity(username).getFollowers().contains(followerCheck)) {

					throw new BadRequestException("user is already followed by this person");
				}
				User follower = getUserEntity(credentialsMapper.dtoToEntity(credentialsDto).getUsername());
				User followed = getUserEntity(username);
				List<User> followingList = follower.getFollowing();
				followingList.add(followed);
				follower.setFollowing(followingList);
				userRepository.saveAndFlush(follower);
				checker = true;
			}
		}
		if (!checker) {
			throw new NotAuthorizedException("Credentials do not match");
		}
	}

	// POST - UNFOLLOW USER
	@Override
	public void unfollowUser(CredentialsDto credentialsDto, String username)
			throws NotAuthorizedException, BadRequestException {
		boolean checker = false;
		for (User userCheck : userRepository.findAll()) {
			if (userCheck.getCredentials().equals(credentialsMapper.dtoToEntity(credentialsDto))) {
				User followerCheck = getUserEntity(credentialsDto.getUsername());
				if (!getUserEntity(username).getFollowers().contains(followerCheck)) {
					throw new BadRequestException("user is not followed by this person");
				} else {
					User unfollower = getUserEntity(credentialsDto.getUsername());
					User unfollowed = getUserEntity(username);
					List<User> following = unfollower.getFollowing();
					following.remove(unfollowed);
					unfollower.setFollowing(following);
					userRepository.saveAndFlush(unfollower);
					checker = true;
				}
			}
		}
		if (!checker) {
			throw new NotAuthorizedException("Credentials do not match");
		}

	}

	// GET - USER FEED
	@Override
	public List<TweetResponseDto> getFeed(String username) {

		User user = getUserEntity(username);

		List<TweetResponseDto> feed = new ArrayList<>();
		for (Tweet tweet : tweetRepository.findAll()) {
			if (user.getFollowing().contains(tweet.getAuthor())
					|| user.getTweets().contains(tweet) && !tweet.isDeleted()) {
				feed.add(tweetMapper.entityToDto(tweet));
			}
		}
		List<TweetResponseDto> feedReversed = new ArrayList<>();
		for (int i = feed.size() - 1; i >= 0; i--) {
			feedReversed.add(feed.get(i));
		}
		return feedReversed;
	}
  
	  public User getCredentialedUser(UserRequestDto userRequestDto) {
		  User validatingUser = userMapper.requestDtoToEntity(userRequestDto);
		  String validatingUsername = validatingUser.getCredentials().getUsername();
		  String validatingPassword = validatingUser.getCredentials().getPassword();
		  for (User u : userRepository.findAll()) {
			  if (Objects.equals(u.getCredentials().getUsername(), validatingUsername)
					  && Objects.equals(u.getCredentials().getPassword(), validatingPassword)) {

				  return u;
			  }
		  }

		  return null;
	  }

	  @Override
	  public UserResponseDto createOrReactivateUser(UserRequestDto userRequestDto) {

		  if (userRequestDto.getCredentials() == null && userRequestDto.getProfile() == null) {
			  throw new BadRequestException("the user request is empty");
		  }
		  if (userRequestDto.getCredentials() == null || userRequestDto.getProfile() == null) {
			  throw new BadRequestException("the user request is incomplete");
		  }
		  if (userRequestDto.getCredentials().getUsername() == null) {
			  throw new BadRequestException("a username is required");
		  }
		  if (userRequestDto.getCredentials().getPassword() == null) {
			  throw new BadRequestException("a password is required");
		  }
		  if (userRequestDto.getProfile().getEmail() == null) {
			  throw new BadRequestException("an email is required");
		  }

		  User userToCreateOrReactivate;
		  userToCreateOrReactivate = getCredentialedUser(userRequestDto);
		  // user already exists
		  if (userToCreateOrReactivate != null) {
			  // reactivate a user who isDeleted()
			  if (userToCreateOrReactivate.isDeleted()) {
				  userToCreateOrReactivate.setDeleted(false);
			  } else {
				  //user has an active account already
				  throw new BadRequestException("user has an active account");
			  }
		  } else {
			  //user doesn't exist yet
			  userToCreateOrReactivate = userMapper.requestDtoToEntity(userRequestDto);
		  }

		  return userMapper.entityToDto(userRepository.saveAndFlush(userToCreateOrReactivate));
	  }

//	// GET - USER MENTIONS
//	@Override
//	public List<TweetResponseDto> getUserMentions(String username) {
//		User user = getUserEntity(username);
//		List<TweetResponseDto> tweetDtoList = new ArrayList<>();
//		for (Tweet tweet : user.getMentionedTweets()) {
//			if (tweet.isDeleted() == false) {
//				String usernameDto = tweet.getAuthor().getCredentials().getUsername();
//				TweetResponseDto tweetDto = tweetMapper.entityToDto(tweet);
//				tweetDto.getAuthor().setUsername(usernameDto);
//				tweetDtoList.add(tweetDto);
//			}
//		}
//
//		return tweetDtoList;
//	}

	// DELETE USER
	@Override
	public UserResponseDto deleteUser(String username, CredentialsDto credentialsDto) throws NotAuthorizedException {
		User user = getUserEntity(username);
		if (user.getCredentials().equals(credentialsMapper.dtoToEntity(credentialsDto))) {
			user.setDeleted(true);
			userRepository.saveAndFlush(user);
			return userMapper.entityToDto(user);
		} else {
			throw new NotAuthorizedException("Credentials do not match");
		}
	}

	// UPDATE USER
	@Override
	public UserResponseDto updateUser(String username, UserRequestDto userRequestDto) {
		User user = getUserEntity(username);
		if (user.getCredentials().equals(userMapper.requestDtoToEntity(userRequestDto).getCredentials())) {

			user.setProfile(userMapper.requestDtoToEntity(userRequestDto).getProfile());
			userRepository.saveAndFlush(user);
			UserResponseDto userResponseDto = userMapper.entityToDto(user);

			return userResponseDto;
		} else {
			throw new NotAuthorizedException("Credentials do not match");
		}
	}

	// GET USER BY USERNAME
	@Override
	public UserResponseDto getUserByUsername(String username) {
		String name = getUserEntity(username).getCredentials().getUsername();
		UserResponseDto userDto = userMapper.entityToDto(getUserEntity(username));
		userDto.setUsername(name);
		return userDto;
	}	

	@Override
	public List<UserResponseDto> getActiveFollowers(String username) {
		// examine this vs getUserEntity
//        if (!validateServiceImpl.usernameExists(username)) {
//            throw new NotFoundException("User not found with username: " + username);
//        }
		User user = getUserEntity(username);
		List<User> followers = user.getFollowers();
		followers.removeIf(User::isDeleted);


		return userMapper.entitiesToDtos(followers);
	}

	@Override
	public List<UserResponseDto> getActiveFollowing(String username) {
		User user = getUserEntity(username);
		List<User> following = user.getFollowing();
		following.removeIf(User::isDeleted);
    
    return userMapper.entitiesToDtos(following);
	}


  @Override
  public List<TweetResponseDto> getTweetsMentioningUsername(String username) {
      User user = getUserEntity(username);
      List<Tweet> mentioningTweets = new ArrayList<>(user.getMentionedTweets());
      mentioningTweets.removeIf(Tweet::isDeleted);
      mentioningTweets.sort(Comparator.comparing(Tweet::getPosted, Comparator.reverseOrder()));

      return tweetMapper.entitiesToDtos(mentioningTweets);
  }

	//GET USER TWEETS
	@Override
	public List<TweetResponseDto> getTweets(String username) {

		List<TweetResponseDto> tweetResponseDtoList = new ArrayList<>();

		User user = getUserEntity(username);
		for (Tweet tweet : user.getTweets()) {
			if (tweet.isDeleted() == false) {
				tweetResponseDtoList.add(tweetMapper.entityToDto(tweet));
			}
		}

		for (TweetResponseDto tweetResponseDto : tweetResponseDtoList) {
			tweetResponseDto.getAuthor().setUsername(username);
		}
		return tweetResponseDtoList;
	}

}
