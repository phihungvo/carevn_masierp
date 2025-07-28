package com.masi.employee.repository;

import com.masi.employee.domain.UniformFormDetail;
import com.masi.employee.domain.enumeration.UniformReleaseType;
import com.masi.employee.service.reports.ExportImportReport;

import com.masi.employee.service.reports.UniformChangeDetail;
import com.masi.employee.service.reports.UniformStockAggregate;
import com.masi.employee.service.reports.UniformStockChange;
import jakarta.persistence.Column;
import lombok.Data;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.UUID;

import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the UniformFormDetail entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UniformFormDetailRepository
    extends ReactiveCrudRepository<UniformFormDetail, UUID>, UniformFormDetailRepositoryInternal {
    Flux<UniformFormDetail> findAllBy(Pageable pageable);

    @Query("SELECT * FROM uniform_form_detail entity WHERE entity.uniform_id = :id")
    Flux<UniformFormDetail> findByUniform(UUID id);

    @Query("SELECT * FROM uniform_form_detail entity WHERE entity.uniform_id IS NULL")
    Flux<UniformFormDetail> findAllWhereUniformIsNull();

    @Query("SELECT * FROM uniform_form_detail entity WHERE entity.uniform_release_id = :id")
    Flux<UniformFormDetail> findByUniformRelease(UUID id);

    @Query("SELECT * FROM uniform_form_detail entity WHERE entity.uniform_release_id IS NULL")
    Flux<UniformFormDetail> findAllWhereUniformReleaseIsNull();

    @Query("SELECT * FROM uniform_form_detail entity WHERE entity.uniform_order_id = :id AND delete_at IS NULL AND delete_by IS NULL")
    Flux<UniformFormDetail> findByUniformOrder(UUID id);

    @Query("SELECT * FROM uniform_form_detail entity WHERE entity.uniform_release_id = :id AND delete_at IS NULL AND delete_by IS NULL")
    Flux<UniformFormDetail> findByUniformReleaseNotDelete(UUID id);

    @Query("SELECT * FROM uniform_form_detail entity WHERE entity.uniform_release_id = :id AND  entity.company = :company AND delete_at IS NULL AND delete_by IS NULL")
    Flux<UniformFormDetail> findByUniformReleaseNotDelete(UUID id, String company);

    @Query("SELECT * FROM uniform_form_detail entity WHERE entity.uniform_order_stock_id = :id AND  entity.company = :company AND delete_at IS NULL AND delete_by IS NULL")
    Flux<UniformFormDetail> findByUniformStockOrderNotDelete(UUID id, String company);

    @Query("SELECT * FROM uniform_form_detail entity WHERE entity.uniform_order_id IS NULL")
    Flux<UniformFormDetail> findAllWhereUniformOrderIsNull();

    @Query("SELECT * FROM uniform_form_detail entity WHERE entity.uniform_return_id = :id")
    Flux<UniformFormDetail> findByUniformReturn(UUID id);

    @Query("SELECT * FROM uniform_form_detail entity WHERE entity.uniform_return_id IS NULL")
    Flux<UniformFormDetail> findAllWhereUniformReturnIsNull();

    @Override
    <S extends UniformFormDetail> Mono<S> save(S entity);

    @Override
    Flux<UniformFormDetail> findAll();

    @Override
    Mono<UniformFormDetail> findById(UUID id);

    @Override
    Mono<Void> deleteById(UUID id);

    @Query("""
            select u.id, u.code, u.name, ufd.quantity, ufd.uniform_order_stock_id, ufd.uniform_release_id, ufd.create_at as date FROM uniform u JOIN uniform_form_detail ufd on u.id = ufd.uniform_id
            WHERE ufd.uniform_order_stock_id is not NULL or ufd.uniform_release_id is not null
            and  ufd.create_at between :fromDate and :toDate and u.company = :company
            """)
    Flux<UniformStockChange> getUniformStockChange(ZonedDateTime fromDate, ZonedDateTime toDate, String company);

    @Query("""
        SELECT u.id,  u.code, u.name, us.stock,u.base_price, u.uom_id as unit_id from uniform u join uniform_stock us on us.uniform_id = u.id where u.company = :company
        """)
    Flux<UniformStockAggregate> getUniformStockAggregate(String company);


//    @Query("SELECT  u.id, u.name, SUM(ud.quantity) AS total, CASE WHEN ud.uniform_return_id IS NOT NULL THEN 'RETURN' ELSE 'RELEASE' END AS type FROM uniform u JOIN uniform_form_detail ud ON ud.uniform_id = u.id left join uniform_release ur on ur.id = ud.uniform_release_id left join uniform_return ure on ure.id = ud.uniform_return_id WHERE (ud.uniform_return_id IS NOT NULL OR( ud.uniform_release_id IS NOT NULL and ur.type='SUPPORT')) AND u.company = :company AND ((ur.date >= :fromDate AND ur.date <= :toDate) or (ure.date >= :fromDate AND ure.date <= :toDate)) GROUP BY u.id, u.name, type  ORDER BY u.name LIMIT :limit OFFSET :offset;")

    @Query("""

             SELECT
            sub.id,
            sub.name,
            SUM(sub.total) AS total,
            sub.type
        FROM (
            SELECT
                u.id,
                u.name,
                ud.quantity AS total,
                CASE
                    WHEN ud.uniform_return_id IS NOT NULL THEN 'RETURN'
                    ELSE 'RELEASE'
                END AS type
            FROM
                uniform u
            JOIN
                uniform_form_detail ud ON ud.uniform_id = u.id
            LEFT JOIN
                uniform_release ur ON ur.id = ud.uniform_release_id
            LEFT JOIN
                uniform_return ure ON ure.id = ud.uniform_return_id
            WHERE
                (
                    ud.uniform_return_id IS NOT NULL
                    OR (
                        ud.uniform_release_id IS NOT NULL
                        AND ur.type = 'SUPPORT'
                    )
                )
                AND u.company = :company
                AND (
                    (ur.date::date >= :fromDate AND ur.date::date <= :toDate)
                    OR (ure.date::date >= :fromDate AND ure.date::date <= :toDate)
                )
        ) sub
        GROUP BY
            sub.id,
            sub.name,
            sub.type
        ORDER BY
            sub.name
        LIMIT
            :limit
        OFFSET
            :offset;

        """)
    Flux<ExportImportReport> exportImportReport(LocalDate fromDate, LocalDate toDate, String company, int limit, long offset);

    //    @Query("SELECT count(*) FROM (SELECT  u.id, CASE WHEN ud.uniform_return_id IS NOT NULL THEN 'RETURN' ELSE 'RELEASE' END AS type FROM uniform u JOIN uniform_form_detail ud ON ud.uniform_id = u.id left join uniform_release ur on ur.id = ud.uniform_release_id left join uniform_return ure on ure.id = ud.uniform_return_id WHERE (ud.uniform_return_id IS NOT NULL OR( ud.uniform_release_id IS NOT NULL and ur.type='SUPPORT')) AND u.company = :company  AND ((ur.date >= :fromDate AND ur.date <= :toDate) or (ure.date >= :fromDate AND  ure.date <= :toDate)) GROUP BY u.id, type ) as count")
// what the f am i writing
    @Query("""

            Select count(*) as totalRow from (
               SELECT\s
                 sub.id,\s
                 sub.name,\s
                 SUM(sub.total) AS total
               FROM\s
                 (
                   SELECT\s
                     u.id,\s
                     u.name,\s
                     ud.quantity AS total,\s
                     CASE WHEN ud.uniform_return_id IS NOT NULL THEN 'RETURN' ELSE 'RELEASE' END AS type\s
                   FROM\s
                     uniform u\s
                     JOIN uniform_form_detail ud ON ud.uniform_id = u.id\s
                     LEFT JOIN uniform_release ur ON ur.id = ud.uniform_release_id\s
                     LEFT JOIN uniform_return ure ON ure.id = ud.uniform_return_id\s
                   WHERE\s
                     (
                       ud.uniform_return_id IS NOT NULL\s
                       OR (
                         ud.uniform_release_id IS NOT NULL\s
                         AND ur.type = 'SUPPORT'
                       )
                     )\s
                     AND u.company = :company\s
                     AND (
                       (
                         ur.date::date >= :fromDate\s
                         AND ur.date::date <= :toDate
                       )\s
                       OR (
                         ure.date::date >= :fromDate\s
                         AND ure.date::date <= :toDate
                       )
                     )
                 ) sub\s
               GROUP BY\s
                 sub.id,\s
                 sub.name,\s
                 sub.type\s
               ) as sub1;
        """)
    Mono<Long> countExportImportReport(LocalDate fromDate, LocalDate toDate, String company);


    @Query("""
        select t.id, t.name, sum(t.total) as total,t."type" from  ( (SELECT u.id ,u.name ,SUM(ud.quantity) as total,'STOCKED' as "type"
            FROM uniform u  JOIN uniform_form_detail ud on  ud.uniform_id = u."id"
            JOIN uniform_order uo on uo.id = ud.uniform_order_id
            WHERE uo.status ='STOCKED'
            and u.company = :company
            and uo.date::date >= :fromDate AND uo.date::date <= :toDate
            GROUP BY u.id, u.name, "type"
          )
        UNION ALL
            (SELECT u.id ,u.name ,SUM(ud.quantity) as total,'RELEASE' as "type"
            FROM uniform u  JOIN uniform_form_detail ud on  ud.uniform_id = u."id"
            JOIN uniform_release ur on ur.id = ud.uniform_release_id
            WHERE u.company = :company
            and ur.date::date >= :fromDate AND ur.date::date <= :toDate
            GROUP BY u.id, u.name, "type"

           )
        UNION ALL (
               SELECT u.id ,u.name ,SUM(us.stock) as total,'STOCK' as "type"
          FROM uniform u
          LEFT JOIN uniform_stock us ON u.id = us.uniform_id
          WHERE u.company = :company
          GROUP BY u.id, u.name, "type"

        )) as  t GROUP BY t.id, t.name, t."type" order by t."name"   LIMIT  :limit
            OFFSET :offset

        """)
    Flux<ExportImportReport> getSupercalifragilisticexpialidociousUniformReport(LocalDate fromDate, LocalDate toDate, String company, int limit, long offset);


    @Query("""
        select count(*) from (  select t.id,t.type from  ( (SELECT u.id ,u.name ,SUM(ud.quantity) as total,'STOCKED' as "type"
            FROM uniform u  JOIN uniform_form_detail ud on  ud.uniform_id = u."id"
            JOIN uniform_order uo on uo.id = ud.uniform_order_id
            WHERE uo.status ='STOCKED'
            and u.company = :company
            and uo.date::date >= :fromDate AND uo.date::date <= :toDate
            GROUP BY u.id, u.name, "type"
          )
        UNION ALL
            (SELECT u.id ,u.name ,SUM(ud.quantity) as total,'RELEASE' as "type"
            FROM uniform u  JOIN uniform_form_detail ud on  ud.uniform_id = u."id"
            JOIN uniform_release ur on ur.id = ud.uniform_release_id
            WHERE u.company = :company
            and ur.date::date >= :fromDate AND ur.date::date <= :toDate
            GROUP BY u.id, u.name, "type"

           )
        UNION ALL (
               SELECT u.id ,u.name ,SUM(us.stock) as total,'STOCK' as "type"
          FROM uniform u
          LEFT JOIN uniform_stock us ON u.id = us.uniform_id
          WHERE u.company = :company
          GROUP BY u.id, u.name, "type"

        )) as  t GROUP BY t.id,  t."type"
                                     ) as b

        """)
    Mono<Long> countSupercalifragilisticexpialidociousUniformReport(LocalDate fromDate, LocalDate toDate, String company);

    @Query("""
        SELECT u.id ,u.name ,SUM(ud.quantity) as total,'STOCKED' as "type"
        FROM uniform u  JOIN uniform_form_detail ud on  ud.uniform_id = u."id"
        JOIN uniform_order uo on uo.id = ud.uniform_order_id
        WHERE uo.status ='STOCKED'
        and u.company = :company
        and uo.date::date >= :fromDate AND uo.date::date <= :toDate
        GROUP BY u.id, u.name, "type"
        ORDER  BY u."name"
        LIMIT  :limit
        OFFSET :offset
        """)
    Flux<ExportImportReport> importStockReport(LocalDate fromDate, LocalDate toDate, String company, int limit, long offset);

    @Query("""
        SELECT count(*)
        FROM (SELECT u.id ,u.name ,SUM(ud.quantity) as total,'STOCKED' as "type"
        FROM uniform u  JOIN uniform_form_detail ud on  ud.uniform_id = u."id"
        JOIN uniform_order uo on uo.id = ud.uniform_order_id
        WHERE uo.status ='STOCKED'
        and u.company = :company
        and uo.date::date >= :fromDate AND uo.date::date <= :toDate
        GROUP BY u.id, u.name, "type") as count
        """)
    Mono<Long> countImportStockReport(LocalDate fromDate, LocalDate toDate, String company);

    @Query("""
        SELECT u.id ,u.name ,SUM(ud.quantity) as total,ur."type" as "type"
        FROM uniform u  JOIN uniform_form_detail ud on  ud.uniform_id = u."id"
        JOIN uniform_release ur on ur.id = ud.uniform_release_id
        WHERE u.company = :company
        and ur.date::date >= :fromDate AND ur.date::date <= :toDate
        and (ur."type" = :type or :type is null)
        GROUP BY u.id, u.name, "type"
        ORDER  BY u."name" ASC
        LIMIT  :limit
        OFFSET :offset
        """)
    Flux<ExportImportReport> exportStockReport(LocalDate fromDate, LocalDate toDate, String company, int limit, long offset, UniformReleaseType type);

    @Query("""
        SELECT count(*)
        FROM (SELECT u.id ,u.name ,SUM(ud.quantity) as total,ur."type" as "type"
        FROM uniform u  JOIN uniform_form_detail ud on  ud.uniform_id = u."id"
        JOIN uniform_release ur on ur.id = ud.uniform_release_id
        WHERE u.company = :company
        and ur.date::date >= :fromDate AND ur.date::date <= :toDate
        and (ur."type" = :type or :type is null)
        GROUP BY u.id, u.name, "type") as count
        """)
    Mono<Long> countExportStockReport(LocalDate fromDate, LocalDate toDate, String company, UniformReleaseType type);


    @Query("SELECT entity.* FROM uniform_form_detail entity WHERE entity.uniform_id IN (:listUniformId) AND entity.delete_at IS NULL AND entity.delete_by IS NULL AND entity.uniform_release_id IS NOT NULL")
    Flux<UniformFormDetail> findAllNotDeleteByUniformRelease();

    // function remove by uniform order id and set delete_at and delete_by
    @Modifying
    @Query("UPDATE uniform_form_detail SET delete_at = NOW(), delete_by = :deleteBy WHERE uniform_order_id = :id AND delete_at IS NULL AND delete_by IS NULL")
    Mono<Void> deleteByUniformOrder(UUID id, String deleteBy);


    @Query("""
                SELECT
                  u."name" AS uniform_name,
                  ufd.quantity,
                  ep.full_name AS employee_name,
                  ep.employee_code,
                  ur."date" as at_date,
                  'RELEASE' AS type
                FROM
                  uniform_form_detail ufd
                  JOIN uniform u ON u.id = ufd.uniform_id
                  RIGHT  JOIN uniform_release ur ON ufd.uniform_release_id = ur.id
                  JOIN employee_profile ep ON ur.employee_id = ep.id
                WHERE ur.date >= :fromDate
                  AND ur.date <= :toDate
                  AND u.company = :company
        and ufd.uniform_id = :uniform
                ORDER BY ur.date DESC
                LIMIT :limit
                OFFSET :offset

        """)
    Flux<UniformChangeDetail> getUniformReleaseReportByUniform(LocalDate fromDate, LocalDate toDate, UUID uniform, String company, int limit, long offset);

    @Query("""
                SELECT count(*)
                FROM
                  uniform_form_detail ufd
                  JOIN uniform u ON u.id = ufd.uniform_id
                  RIGHT  JOIN uniform_release ur ON ufd.uniform_release_id = ur.id
                  JOIN employee_profile ep ON ur.employee_id = ep.id
                WHERE ur.date >= :fromDate
                  AND ur.date <= :toDate
                  AND u.company = :company
        and ufd.uniform_id = :uniform

        """)
    Mono<Long> countUniformReleaseReportByUniform(LocalDate fromDate, LocalDate toDate, UUID uniform, String company);


    @Query("""
                SELECT
                  u."name" AS uniform_name,
                  ufd.quantity,
                  ep.full_name AS employee_name,
                  ep.employee_code,
                  ur.date as at_date,
                    'STOCKED' AS type
                FROM
                  uniform_form_detail ufd
                  JOIN uniform u ON u.id = ufd.uniform_id
                  RIGHT  JOIN uniform_order ur ON ufd.uniform_order_id = ur.id
                  JOIN employee_profile ep ON ur.create_by :: uuid = ep.id
                WHERE
                  ur.status = 'STOCKED'
                    AND ur.date >= :fromDate
                    AND ur.date <= :toDate
                    AND u.company = :company
        and ufd.uniform_id = :uniform
                ORDER BY
                    ur.date DESC
                LIMIT :limit
                OFFSET :offset
        """)
    Flux<UniformChangeDetail> getUniformOrderReportByUniform(LocalDate fromDate, LocalDate toDate, UUID uniform, String company, int limit, long offset);

    @Query("""
        SELECT count(*)
        FROM
          uniform_form_detail ufd
          JOIN uniform u ON u.id = ufd.uniform_id
          RIGHT  JOIN uniform_order ur ON ufd.uniform_order_id = ur.id
          JOIN employee_profile ep ON ur.create_by :: uuid = ep.id
        WHERE
          ur.status = 'STOCKED'
            AND ur.date >= :fromDate
            AND ur.date <= :toDate
            AND u.company = :company
        and ufd.uniform_id = :uniform
        """)
    Mono<Long> countUniformOrderReportByUniform(LocalDate fromDate, LocalDate toDate, UUID uniform, String company);
}

interface UniformFormDetailRepositoryInternal {

    <S extends UniformFormDetail> Mono<S> save(S entity);

    Flux<UniformFormDetail> findAllBy(Pageable pageable);

    Flux<UniformFormDetail> findAll();

    Mono<UniformFormDetail> findById(UUID id);

    Flux<UniformFormDetail> findAllByUniformOrderId(UUID id);
    // this is not supported at the moment because of
    // https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<UniformFormDetail> findAllBy(Pageable pageable, Criteria criteria);

    Flux<UniformFormDetail> findAllByFieldNotNullAndBetweenAndCompany(String field, LocalDate fromDate, LocalDate toDate, String company);

    Flux<UniformFormDetail> findAllByUniformReleaseIdAndCompany(UUID id, String company);

    Flux<UniformFormDetail> findAllByUniformOrderStock(UUID uniformOrderStockId, String company);
}
