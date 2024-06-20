package com.geoshare.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.geoshare.backend.entity.ListComment;
import com.geoshare.backend.repository.ListCommentRepository;

@Service
public class ListCommentService {

	@Autowired
	private ListCommentRepository listCommentRepository;
	
	List<ListComment> findAllByList(Long listID) {
		return listCommentRepository.findAllByList(listID);
	}
	
	List<ListComment> findAllByUser(Long userID) {
		return listCommentRepository.findAllByUser(userID);
	}
	
	List<ListComment> findAllByUser(String username) {
		return listCommentRepository.findAllByUser(username);
	}
	
	ListComment findByID(Long id) {
		return listCommentRepository.findByID(id);
	}
	
}
