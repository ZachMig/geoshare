package com.geoshare.backend.service;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.geoshare.backend.dto.ListCommentDTO;
import com.geoshare.backend.entity.GeoshareUser;
import com.geoshare.backend.entity.ListComment;
import com.geoshare.backend.entity.LocationList;
import com.geoshare.backend.repository.GeoshareUserRepository;
import com.geoshare.backend.repository.ListCommentRepository;
import com.geoshare.backend.repository.LocationListRepository;

@Service
public class ListCommentService {

	private ListCommentRepository listCommentRepository;
	private LocationListRepository locationListRepository;
	private GeoshareUserRepository userRepository;
	
	public ListCommentService(
			ListCommentRepository listCommentRepository,
			LocationListRepository locationListRepository, 
			GeoshareUserRepository userRepository) {
		
		this.listCommentRepository = listCommentRepository;
		this.locationListRepository = locationListRepository;
		this.userRepository = userRepository;
	}


	public List<ListComment> findAll(Long userID, String username, Long listID) {
		
		if (userID != null) {
			return listCommentRepository.findAllByUser(userID);
		}
		
		if (username != null) {
			return listCommentRepository.findAllByUser(username);
		}
		
		if (listID != null) {
			return listCommentRepository.findAllByList(listID);
		}
		
		throw new IllegalArgumentException("This GET request requires a search parameter.");
		
	}
	
	public ListComment findByID(Long id) {
		return listCommentRepository.findByIDOrThrow(id);
	}
	
	
	public void createListComment(ListCommentDTO commentDTO, Authentication auth) {

		//Lookup the List that this comment is commenting on
		LocationList owningList = locationListRepository.findByIDOrThrow(commentDTO.listID());
		
		//Lookup the user based on the authentication object name i.e. username of current logged in user
		GeoshareUser geoUser = userRepository.findByUsernameOrThrow(auth.getName());
		
		//If there is a parent comment, look it up in the DB
		ListComment parentComment = null;
		if (commentDTO.parentCommentID() != null) {
			parentComment = listCommentRepository.findByIDOrThrow(commentDTO.parentCommentID());
		}
		
		//Create the new comment to be written from what we have looked up, and what has been passed in
		ListComment newComment = new ListComment(
			commentDTO.content(),
			owningList,
			geoUser,
			parentComment
		);
		
		//Write the new comment to the database
		listCommentRepository.save(newComment);
		
	}
	
}
