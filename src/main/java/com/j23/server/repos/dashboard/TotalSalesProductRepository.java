package com.j23.server.repos.dashboard;

import com.j23.server.models.dashboard.TotalSalesProduct;
import com.j23.server.models.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    List<TotalSalesProduct> findTop5ByTotalProfitIsAfterAndCreatedOnBetweenOrderByTotalProfitDesc(BigDecimal totalProfit,
                                                                                                  LocalDateTime dateFrom, LocalDateTime dateTill);

    @Query(value = "SELECT SUM(t.total_profit) FROM total_sales_product t WHERE t.created_on >= :startDate AND t.created_on" +
            "<= :endDate", nativeQuery = true)
    BigDecimal sumTotalProfitForCurrentMonth(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}
