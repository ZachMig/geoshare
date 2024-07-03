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
	
	public List<ListComment> findAllByList(Long listID) {
		return listCommentRepository.findAllByList(listID);
	}
	
	public List<ListComment> findAllByUser(Long userID) {
		return listCommentRepository.findAllByUser(userID);
	}
	
	public List<ListComment> findAllByUser(String username) {
		return listCommentRepository.findAllByUser(username);
	}
	
	public ListComment findByID(Long id) {
		return listCommentRepository.findByIDOrThrow(id);
	}
	
}
