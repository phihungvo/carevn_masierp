package com.carevn.masi.repository;

import com.carevn.masi.domain.Group;
import com.carevn.masi.repository.rowmapper.AuthorityRowMapper;
import com.carevn.masi.repository.rowmapper.GroupRowMapper;
import com.carevn.masi.repository.rowmapper.UserRowMapper;
import com.carevn.masi.security.SecurityUtils;
import com.carevn.masi.service.dto.BasicSearchQuery;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.convert.R2dbcConverter;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.repository.support.SimpleR2dbcRepository;
import org.springframework.data.relational.core.sql.*;
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoinCondition;
import org.springframework.data.relational.repository.query.RelationalEntityInformation;
import org.springframework.data.relational.repository.support.MappingRelationalEntityInformation;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;

@Slf4j
public class GroupRepositoryInternalImpl
    extends SimpleR2dbcRepository<Group, UUID>
    implements GroupRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final GroupRowMapper groupRowMapper;
    private final AuthorityRowMapper authorityRowMapper;
    private final UserRowMapper userRowMapper;

    private static final Table entityTable = Table.aliased("masi_group", EntityManager.ENTITY_ALIAS);
    private static final Table groupUserTable = Table.aliased("masi_group_user", "gu");
    private static final Table userTable = Table.aliased("masi_user", "u");
    private static final Table groupAuthorityTable = Table.aliased("masi_group_authority", "ga");
    private static final Table authorityTable = Table.aliased("masi_authority", "a");

    public GroupRepositoryInternalImpl(R2dbcEntityOperations entityOperations,
                                       R2dbcConverter converter,
                                       EntityManager entityManager,
                                       DatabaseClient db,
                                       R2dbcEntityTemplate r2dbcEntityTemplate,
                                       GroupRowMapper groupRowMapper,
                                       AuthorityRowMapper authorityRowMapper,
                                       UserRowMapper userRowMapper) {
        super(
            new MappingRelationalEntityInformation(
                converter.getMappingContext().getRequiredPersistentEntity(Group.class)),
            entityOperations,
            converter);
        this.db = db;
        this.r2dbcEntityTemplate = r2dbcEntityTemplate;
        this.entityManager = entityManager;
        this.groupRowMapper = groupRowMapper;
        this.authorityRowMapper = authorityRowMapper;
        this.userRowMapper = userRowMapper;
    }

    @Override
    public Flux<Group> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all()
            .concatMap(group -> {
                groups.put(group.getId(), group);
                return Mono.just(group);
            })
            .thenMany(Flux.fromIterable(groups.values()));
    }

    @Override
    public Flux<Group> findAll() {
        return findAllBy(null);
    }

    private RowsFetchSpec<Group> createBriefQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = GroupSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(GroupSqlHelper.getColumns(entityTable, "g"));
        var selectFrom = Select.builder().select(columns).from(entityTable);
        String select = entityManager.createSelect(selectFrom, Group.class, pageable, whereClause);
        return db.sql(select).map(this::briefProcess);
    }

    private RowsFetchSpec<Group> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = GroupSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(GroupSqlHelper.getColumns(entityTable, "g"));
        columns.addAll(UserSqlHelper.getColumns(userTable, "u"));
        columns.addAll(AuthoritySqlHelper.getColumns(authorityTable, "a"));
        SelectFromAndJoinCondition selectFrom = Select.builder().select(columns).from(entityTable)
            .leftOuterJoin(groupUserTable)
            .on(Column.create("id", entityTable))
            .equals(Column.create("group_id", groupUserTable))
            .leftOuterJoin(userTable)
            .on(Column.create("user_id", groupUserTable))
            .equals(Column.create("id", userTable))
            .leftOuterJoin(groupAuthorityTable)
            .on(Column.create("id", entityTable))
            .equals(Column.create("group_id", groupAuthorityTable))
            .leftOuterJoin(authorityTable)
            .on(Column.create("group_authority", groupAuthorityTable))
            .equals(Column.create("name", authorityTable));

        String select = entityManager.createSelect(selectFrom, Group.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    private Group briefProcess(Row row, RowMetadata metadata) {
        return groupRowMapper.apply(row, EntityManager.ENTITY_ALIAS);
    }

    private final Map<UUID, Group> groups = new ConcurrentHashMap<>();

    private Group process(Row row, RowMetadata metadata) {
        UUID groupId = row.get("e_id", UUID.class);
        Group entity = groups.getOrDefault(groupId, groupRowMapper.apply(row, EntityManager.ENTITY_ALIAS));

        if (row.get("u_id", UUID.class) != null) {
            entity.getUsers().add(userRowMapper.apply(row, "u"));
        }
        if (row.get("a_name", String.class) != null) {
            entity.getAuthorities().add(authorityRowMapper.apply(row, "a"));
        }

        groups.put(groupId, entity);
        return entity;
    }

    @Override
    public <S extends Group> Mono<S> save(S entity) {
        return super.save(entity);
    }

    @Override
    public Mono<Group> findByUserId(UUID userId) {
        var userIdColumnAlias = "gu.user_id";
        Condition whereClause = Conditions.just(String.format("%s = '%s'", userIdColumnAlias, userId));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Flux<Group> findAllByQuery(BasicSearchQuery query, Pageable pageable) {
        Condition whereClause = Conditions.just("1=1");
        if (query.getSearch() != null) {
            query.setSearch(query.getSearch().trim().replace("'", "''"));
            whereClause = whereClause.and(Conditions.just(String.format("unaccent(%s) ilike unaccent('%%%s%%')", "name", query.getSearch())));
        }
        return createBriefQuery(pageable, whereClause).all();
    }

    @Override
    public Mono<Long> countAllByQuery(BasicSearchQuery query) {
        Condition whereClause = Conditions.just("1=1");
        if (query.getSearch() != null) {
            query.setSearch(query.getSearch().trim().replace("'", "''"));
            whereClause = whereClause.and(Conditions.just(String.format("unaccent(%s) ilike unaccent('%%%s%%')", "name", query.getSearch())));
        }
        return createBriefQuery(null, whereClause).all().count();
    }

    @Override
    public Mono<Group> getById(UUID id) {
        Condition whereClause = Conditions.just(String.format("e.id = '%s'", id));
        return createQuery(null, whereClause).first().map(e -> groups.get(e.getId()));
    }

    @Override
    public Mono<Void> clearCache() {
        groups.clear();
        return Mono.empty().then();
    }

    @Override
    public Mono<Void> clearCache(UUID id) {
        groups.remove(id);
        return Mono.empty().then();
    }
}
