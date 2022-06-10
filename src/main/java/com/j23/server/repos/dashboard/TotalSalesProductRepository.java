package com.j23.server.repos.dashboard;

import com.j23.server.models.dashboard.TotalSalesProduct;
import com.j23.server.models.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TotalSalesProductRepository extends JpaRepository<TotalSalesProduct, String> {

  Optional<TotalSalesProduct> findByProductDetailAndCreatedOnBetween(Product productDetail, LocalDateTime dateFrom,
                                                                     LocalDateTime dateTill);

  List<TotalSalesProduct> findTop5ByTotalProfitIsAfterOrderByTotalProfitDesc(BigDecimal totalProfit);
}
