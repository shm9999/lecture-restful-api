package com.restfulapi.myrestfulservice.repository;

import com.restfulapi.myrestfulservice.bean.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
}
