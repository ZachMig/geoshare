package com.geoshare.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.geoshare.backend.dto.ListCommentDTO;
import com.geoshare.backend.entity.ListComment;
import com.geoshare.backend.service.ListCommentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/listcomments")
public class ListCommentController {

	private ListCommentService listCommentService;
	
	public ListCommentController(ListCommentService listCommentService) {
		this.listCommentService = listCommentService;
	}

	@GetMapping("/findall")
	public ResponseEntity<List<ListComment>> getListComments(
			@RequestParam(value = "uid", required = false) Long userID,
			@RequestParam(value = "uname", required = false) String username,
			@RequestParam(value = "listid", required = false) Long listID) {
		
			return new ResponseEntity<>(listCommentService.findAll(userID, username, listID), HttpStatus.OK);
	}
	
	@GetMapping("/find")
	public ResponseEntity<ListComment> getListComment(
			@RequestParam(value = "cid", required = true) Long listCommentID) {
		
		return new ResponseEntity<>(listCommentService.findByID(listCommentID), HttpStatus.OK);
	}
	
	@PostMapping("/create")
	public ResponseEntity<String> createListComment( 
			@Valid
			@RequestBody 
			ListCommentDTO commentDTO,
			Authentication auth) {
		
		listCommentService.createListComment(commentDTO, auth);
		return new ResponseEntity<>("List Comment create request handled successfully.", HttpStatus.OK);
	}
			
}
			
			
