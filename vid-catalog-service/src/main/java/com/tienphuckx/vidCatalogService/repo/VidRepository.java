package com.tienphuckx.vidCatalogService.repo;

import com.tienphuckx.vidCatalogService.entity.Vid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VidRepository extends JpaRepository<Vid, Long> {
}
