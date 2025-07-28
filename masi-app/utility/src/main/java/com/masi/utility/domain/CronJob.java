package com.masi.utility.domain;

import com.cronutils.model.CronType;
import com.cronutils.model.definition.CronDefinitionBuilder;
import com.cronutils.model.time.ExecutionTime;
import com.cronutils.parser.CronParser;
import com.masi.utility.domain.enumeration.CronJobStatus;

import java.io.Serial;
import java.io.Serializable;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A CronJob.
 */
@Slf4j
@Data
@Table("cron_job")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CronJob implements Serializable, Persistable<Long> {

    @Serial
    private static final long serialVersionUID = 13212341L;

    @Id
    @Column("id")
    private Long id;

    @Column("action")
    private String action;

    @Column("name")
    private String name;

    @Column("every_minute")
    private Integer everyMinute;

    @Column("at_minute")
    private Integer atMinute;

    @Column("at_hour")
    private Integer atHour;

    @Column("at_day_of_month")
    private Integer atDayOfMonth;

    @Column("at_month")
    private Integer atMonth;

    @Column("at_day_of_week")
    private Integer atDayOfWeek;

    @Column("enabled")
    private Boolean enabled;

    @Column("last_run")
    private ZonedDateTime lastRun = ZonedDateTime.now();

    @Column("next_run")
    private ZonedDateTime nextRun;

    @Column("description")
    private String description;

    @Column("status")
    private CronJobStatus status;

    @Column("updated_at")
    private ZonedDateTime updatedAt;

    @Column("updated_by")
    private String updatedBy = "system";

    @Column("zone_id")
    private String zoneId = "Asia/Ho_Chi_Minh";

    @Column("url_db")
    private String urlDb;

    @Column("user_db")
    private String userDb;

    @Column("pw_db")
    private String pwDb;
    @Column("zone_offset")
    private float zoneOffset = 7; // eg +7 , -7 , 0 = UTC, 7.5 = UTC+7.5

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public void calculateNextRun() {
        if (this.getEveryMinute() != null) {

            this.nextRun = this.getLastRun().plusMinutes(this.getEveryMinute());
            return;
        }

        var cron = (this.getAtMinute() != null ? this.getAtMinute() : "*") +
            " " +
            (this.getAtHour() != null ? this.getAtHour() : "*") +
            " " +
            (this.getAtDayOfMonth() != null ? this.getAtDayOfMonth() : "*") +
            " " +
            (this.getAtMonth() != null ? this.getAtMonth() : "*") +
            " " +
            (this.getAtDayOfWeek() != null ? this.getAtDayOfWeek() : "*");
        var cronDefinition = CronDefinitionBuilder.instanceDefinitionFor(CronType.UNIX);
        CronParser parser = new CronParser(cronDefinition);
        ExecutionTime executionTime = ExecutionTime.forCron(parser.parse(cron));
        this.nextRun = executionTime.nextExecution(this.getLastRun()).orElse(ZonedDateTime.now()).withZoneSameLocal(ZoneId.of("UTC"));
    }

    public CronJob id(Long id) {
        this.setId(id);
        return this;
    }

    public CronJob action(String action) {
        this.setAction(action);
        return this;
    }

    public CronJob name(String name) {
        this.setName(name);
        return this;
    }

    public CronJob everyMinute(Integer everyMinute) {
        this.setEveryMinute(everyMinute);
        return this;
    }

    public CronJob atMinute(Integer atMinute) {
        this.setAtMinute(atMinute);
        return this;
    }

    public CronJob atHour(Integer atHour) {
        this.setAtHour(atHour);
        return this;
    }

    public CronJob atDayOfMonth(Integer atDayOfMonth) {
        this.setAtDayOfMonth(atDayOfMonth);
        return this;
    }

    public CronJob atMonth(Integer atMonth) {
        this.setAtMonth(atMonth);
        return this;
    }

    public CronJob atDayOfWeek(Integer atDayOfWeek) {
        this.setAtDayOfWeek(atDayOfWeek);
        return this;
    }

    public CronJob enabled(Boolean enabled) {
        this.setEnabled(enabled);
        return this;
    }

    public CronJob lastRun(ZonedDateTime lastRun) {
        this.setLastRun(lastRun);
        return this;
    }

    public CronJob nextRun(ZonedDateTime nextRun) {
        this.setNextRun(nextRun);
        return this;
    }

    public CronJob description(String description) {
        this.setDescription(description);
        return this;
    }

    public CronJob status(CronJobStatus status) {
        this.setStatus(status);
        return this;
    }

    public CronJob updatedAt(ZonedDateTime updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public CronJob updatedBy(String updatedBy) {
        this.setUpdatedBy(updatedBy);
        return this;
    }

    @Transient
    private boolean isPersisted;

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public CronJob setIsPersisted() {
        this.isPersisted = true;
        return this;
    }


    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

}
