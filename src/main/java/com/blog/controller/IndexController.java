package com.blog.controller;

import com.blog.beans.Comment;
import com.blog.queryvo.DetailedBlog;
import com.blog.queryvo.FirstPageBlog;
import com.blog.queryvo.RecommendBlog;
import com.blog.service.BlogService;
import com.blog.service.CommentService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class IndexController {
    @Autowired
    private BlogService blogService;
    @Autowired
    private CommentService commentService;
    //分页查询博客列表
    @GetMapping("/")
    public String index(Model model, @RequestParam(defaultValue = "1",value = "pageNum") Integer pageNum){
        PageHelper.startPage(pageNum,10);
        List<FirstPageBlog> allFirstPageBlog = blogService.getAllFirstPageBlog();
        List<RecommendBlog> recommendedBlog = blogService.getRecommendedBlog();
        PageInfo<FirstPageBlog> pageInfo = new PageInfo<>(allFirstPageBlog);
        model.addAttribute("pageInfo",pageInfo);
        model.addAttribute("recommendedBlogs",recommendedBlog);
        return "index";
    }
    //搜索博客
    @PostMapping("/search")
    public String search(Model model,@RequestParam(defaultValue = "1",value = "pageNum") Integer pageNum,String query){
        PageHelper.startPage(pageNum,1000);
        List<FirstPageBlog> searchBlog = blogService.getSearchBlog(query);
        PageInfo<FirstPageBlog> pageInfo = new PageInfo<>(searchBlog);
        model.addAttribute("pageInfo",pageInfo);
        return "search";
    }
    //跳转博客详情页面
    @GetMapping("/blog/{id}")
    public String detailBlog(@PathVariable("id") Long id,Model model) throws NotFoundException {
        DetailedBlog detailedBlog = blogService.getDetailedBlog(id);
        List<Comment> comments = commentService.listCommentByBlogId(id);
        model.addAttribute("blog",detailedBlog);
        model.addAttribute("comments",comments);
        return "blog";
    }
    //博客信息
    @GetMapping("/footer/blogmessage")
    public String blogMessage(HttpSession session){
        int blogTotal = blogService.getBlogTotal();
        int blogViewTotal = blogService.getBlogViewTotal();
        int blogCommentTotal = blogService.getBlogCommentTotal();
        int blogMessageTotal = blogService.getBlogMessageTotal();
        session.setAttribute("blogTotal",blogTotal);
        session.setAttribute("blogViewTotal",blogViewTotal);
        session.setAttribute("blogCommentTotal",blogCommentTotal);
        session.setAttribute("blogMessageTotal",blogMessageTotal);
        return "index";
    }
}
