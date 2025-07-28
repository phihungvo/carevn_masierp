package com.masi.logistics.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.masi.logistics.service.dto.InventoriesCheckDetailDTO;
import com.masi.logistics.service.dto.ItemCategoryDTO;
import com.masi.logistics.service.dto.ItemDTO;
import com.masi.logistics.service.dto.UomDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;
import java.util.UUID;


@EqualsAndHashCode(callSuper = true)
@Table("inventories_check_detail")
@JsonIgnoreProperties(value = {"new"})
@SuppressWarnings("common-java:DuplicatedBlocks")
@Data
public class InventoriesCheckDetailMapItem extends InventoriesCheckDetail  implements Serializable, Persistable<UUID> {
    private static final long serialVersionUID = 1L;

    // Map data item
    @Column("item_name")
    private String itemName;

    @Column("item_code")
    private String itemCode;

    // Map data uom
    @Column("item_unit_name")
    private String itemUnitName;

    @Column("item_category_id")
    private UUID itemCategoryId;

    @Column("item_category_code")
    private String itemCategoryCode;

    @Column("item_category_name")
    private String itemCategoryName;

    public InventoriesCheckDetailDTO toDto() {
        InventoriesCheckDetailDTO dto = super.toDto();

        ItemDTO item = new ItemDTO();
        item.setName(this.itemName);
        item.setCode(this.itemCode);

        ItemCategoryDTO itemCategory = new ItemCategoryDTO();
        itemCategory.setId(this.itemCategoryId);
        itemCategory.setCode(this.itemCategoryCode);
        itemCategory.setName(this.itemCategoryName);
        item.setItemCategory(itemCategory);

        UomDTO itemUnit = new UomDTO();
        itemUnit.setName(this.itemUnitName);
        item.setUom(itemUnit);

        dto.setItem(item);
        return dto;
    }
}
