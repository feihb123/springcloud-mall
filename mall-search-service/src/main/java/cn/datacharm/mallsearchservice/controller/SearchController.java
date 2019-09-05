package cn.datacharm.mallsearchservice.controller;

import cn.datacharm.mallsearchservice.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * description:
 *
 * @author Herb
 * @date 2019/09/2019-09-05
 */
public class SearchController {
    @Autowired
    private SearchService searchService;

    @RequestMapping("createIndex")
    public String createIndex() {
        searchService.createIndex();
        return "success";
    }
}
