package com.geoshare.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.geoshare.backend.entity.ListComment;
import com.geoshare.backend.service.ListCommentService;

@RestController
@RequestMapping("/api/listcomments")
public class ListCommentController {

	@Autowired
	private ListCommentService listCommentService;
	
	@GetMapping("/findall")
	public List<ListComment> getListComments(
			@RequestParam(value = "uid", required = false) Long userID,
			@RequestParam(value = "uname", required = false) String username,
			@RequestParam(value = "listid", required = false) Long listID)
	{
		if (userID != null) {
			return listCommentService.findAllByUser(userID);
		}
		
		if (username != null) {
			return listCommentService.findAllByUser(username);
		}
		
		if (listID != null) {
			return listCommentService.findAllByList(listID);
		}
		
		throw new IllegalArgumentException("This request requires one search parameter.");

	}
	
	@GetMapping("/find")
	public ListComment getListComment(
			@RequestParam(value = "cid", required = true) Long listCommentID) {
		return listCommentService.findByID(listCommentID);
	}
	
}
