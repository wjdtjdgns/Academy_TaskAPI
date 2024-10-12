package com.nhnacademy.miniDooray.repository;

import com.nhnacademy.miniDooray.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {
}
