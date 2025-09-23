package com.jslhrd.yorimichi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;






@Controller
@RequestMapping("/store")
public class StoreController {

    //첫검색 혹은 검색 버튼을 눌러서 검색.
    @GetMapping("/search?keyword={keyword}&tags={tags}")
    public String showStoresBySearch() {
        //List<StoreDTO> stores
        //List<StoreCategoryDTO> categories
        //List<StoreFacilityCategoryDTO> Facilities
        return "/list";
    }
    
    //비동기로 리스트를 가져오기 위한 메서드
    //카테고리나 시설 등 버튼을 눌러 설정이 바뀌면 리스트가 달라지는 형태.
    @ResponseBody
    @GetMapping("/search?keyword={keyword}&tags={tags}")
    public void showStoresByFilter() {
        
        //서비스로 가서 키워드로 가져온 가게 리스를 보여줄 수 있어야 함.
        //return List<StoreDTO> stores;
    }
    
    //가게 상세정보로 이동
    @GetMapping("/detail/{id}")
    public String showDetail(){
        //StoreDTO
        return "/detail";
    }


    //북마크 등록
    @ResponseBody
    @PostMapping("/bookmark")
    public void submitBookmark(@RequestBody Long id) {
        //등록 처리 후 북마크 갱신
        //return List<BookmarkDTO> Bookmarks;
    }

    //북마크 삭제
    @ResponseBody
    @DeleteMapping("/bookmark")
    public void deleteBookmark(@RequestBody Long Id){
        //삭제 처리 후 북마크 갱신
        //return List<BookmarkDTO> Bookmarks;
    }

    @GetMapping("/like")
    public String showLikes() {
        return "/list/";
    }

    @ResponseBody
    @PostMapping("/like")
    public void submitLike() {
        //return List<LikeDTO> likes;
    }

    @ResponseBody
    @DeleteMapping("/like")
    public void deleteLikes(){
        //return List<LikeDTO> likes;
    }
    


    


    


    
    
}
