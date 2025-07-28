package com.masi.logistics.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.masi.logistics.service.dto.InventoriesDetailDTO;
import com.masi.logistics.service.dto.ItemDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@Data
@Table("inventories_detail")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InventoriesDetailMapItem extends  InventoriesDetail implements Serializable, Persistable<UUID> {
    @Serial
    private static final long serialVersionUID = 1L;

    @Column("item_name")
    private String itemName;

    @Column("item_code")
    private String itemCode;

    @Column("item_percent_protein")
    private Float itemPercentProtein;

    @Column("item_is_separation")
    private Boolean isSeparation;

    public InventoriesDetailDTO toDto() {
        InventoriesDetailDTO inventoriesDetailDTO = super.toDto();

        ItemDTO itemDTO = new ItemDTO();
        if(itemCode != null) {
            itemDTO.setCode(itemCode);
            itemDTO.setName(itemName);
            itemDTO.setPercentProtein(itemPercentProtein);
            itemDTO.setIsSeparation(isSeparation);
        }
        inventoriesDetailDTO.setItem(itemDTO);

        return inventoriesDetailDTO;
    }

}
