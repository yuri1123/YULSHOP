package com.yuri.shoppingsite.Repository;

import com.yuri.shoppingsite.domain.shop.ItemImg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemImgRepository extends JpaRepository<ItemImg, Long> {

    List<ItemImg> findByItemIdOrderByIdAsc(Long itemid);

    ItemImg findByItemIdAndRepimgYn(Long itemId, String repimgYn);

}
