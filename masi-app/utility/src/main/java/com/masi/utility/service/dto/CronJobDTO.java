package com.masi.utility.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.masi.utility.domain.CronJob;
import com.masi.utility.domain.enumeration.CronJobStatus;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * A DTO for the {@link com.masi.utility.domain.CronJob} entity.
 */
@Slf4j
@Data
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CronJobDTO implements Serializable {

    private Long id;

    private String action;

    private String name;

    private Integer everyMinute;

    private Integer atMinute;

    private Integer atHour;

    private Integer atDayOfMonth;

    private Integer atMonth;

    private Integer atDayOfWeek;

    private Boolean enabled;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private ZonedDateTime lastRun;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private ZonedDateTime nextRun;

    private String description;

    private CronJobStatus status;

    private ZonedDateTime updatedAt;

    private String updatedBy;

    private String urlDb;

    private String userDb;

    private String pwDb;

    private float zoneOffset = 7; // eg +7 , -7 , 0 = UTC, 7.5 = UTC+7.5

    public void applyChanges(CronJob cronJob) {
        if (cronJob == null) {
            return;
        }
        cronJob.setName(this.getName());
        cronJob.setAtMinute(this.getAtMinute());
        cronJob.setAtHour(this.getAtHour() - (int) zoneOffset);

        cronJob.setAtDayOfMonth(this.getAtDayOfMonth());
        cronJob.setAtMonth(this.getAtMonth());
        cronJob.setAtDayOfWeek(this.getAtDayOfWeek());
        cronJob.setEnabled(this.getEnabled());
        cronJob.setDescription(this.getDescription());
        cronJob.setStatus(this.getStatus());
        cronJob.setUpdatedAt(ZonedDateTime.now());
        cronJob.setUrlDb(this.getUrlDb());
        cronJob.setUserDb(this.getUserDb());
        cronJob.setPwDb(this.getPwDb());
        cronJob.setZoneOffset(this.getZoneOffset());
        if (this.getAtHour() != null) {
            if (cronJob.getAtHour() < 0) {
                cronJob.setAtHour(cronJob.getAtHour() + 24);
                if (cronJob.getAtDayOfMonth() != null) {
                    cronJob.setAtDayOfMonth(cronJob.getAtDayOfMonth() - 1);
                }
            }
            if (cronJob.getAtHour() >= 24) {
                cronJob.setAtHour(cronJob.getAtHour() - 24);
                if (cronJob.getAtDayOfMonth() != null) {
                    cronJob.setAtDayOfMonth(cronJob.getAtDayOfMonth() + 1);
                }
            }
        }
        log.info("updated cron job {}", cronJob);
        cronJob.setIsPersisted();
    }

    public CronJobDTO toClient() {
        if (this.getAtHour() != null) {
            this.setAtHour(this.getAtHour() + (int) zoneOffset);
            if (this.getAtHour() >= 24) {
                this.setAtHour(this.getAtHour() - 24);
                if (this.getAtDayOfMonth() != null) {
                    this.setAtDayOfMonth(this.getAtDayOfMonth() + 1);
                }
            }
            if (this.getAtHour() < 0) {
                this.setAtHour(this.getAtHour() + 24);
                if (this.getAtDayOfMonth() != null) {
                    this.setAtDayOfMonth(this.getAtDayOfMonth() - 1);
                }
            }
        }
        return this;
    }
}
