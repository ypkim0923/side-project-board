package ypkim.sideproject.board.controller;

import lombok.RequiredArgsConstructor;
import ypkim.sideproject.board.domain.type.SearchType;
import ypkim.sideproject.board.dto.response.ArticleResponse;
import ypkim.sideproject.board.dto.response.ArticleWithCommentResponse;
import ypkim.sideproject.board.service.ArticleService;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@RequestMapping("/articles")
@Controller
public class ArticleController {

	private final ArticleService service;

	@GetMapping
	public String articles(
			@RequestParam(required = false) SearchType searchType,
			@RequestParam(required = false) String searchValue,
			@PageableDefault(size = 10, sort = "createdBy", direction = Direction.DESC) Pageable pageable,
			ModelMap map) {
		map.addAttribute("articles", service.searchArticles(searchType, searchValue, pageable).map(ArticleResponse::from));
		return "articles/index";
	}

	@GetMapping("/{articledId}")
	public String articles(@PathVariable Long articledId, ModelMap map) {
		ArticleWithCommentResponse response = ArticleWithCommentResponse.from(service.getArticle(articledId));
		map.addAttribute("article", response);
		map.addAttribute("articleComments", response.articleCommentResponses());
		return "articles/detail";
	}
}
