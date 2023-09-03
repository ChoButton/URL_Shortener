package com.chobutton.back.repository;

import com.chobutton.back.entity.Url;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UrlRepository extends JpaRepository<Url, Integer> {

    // user별 등록한 URL을 조회하기 위한 기능
    List<Url> findAllByUser_Id(int userId);

    // originUrl로 접속시 DB에 해당하는 데이터의 PK를 불러와 인코딩을 하기 위한 기능
    Url findByOriginUrl(String originUrl);

}
