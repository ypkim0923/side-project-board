package ypkim.sideproject.board.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/articles")
@Controller
public class ArticleController {

	@GetMapping
	public String articles(ModelMap map) {
		map.addAttribute("articles", List.of());
		return "articles/index";
	}

	@GetMapping("/{articledId}")
	public String articles(@PathVariable Long articledId, ModelMap map) {
		map.addAttribute("article", "null");
		map.addAttribute("articleComments", List.of());
		return "articles/detail";
	}
}
