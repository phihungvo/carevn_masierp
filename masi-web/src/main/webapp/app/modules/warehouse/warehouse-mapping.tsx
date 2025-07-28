
const warehouseTypeSelect = [
  {
    label: "Kho tài sản",
    value: "ASSET_STORAGE",
  },
  {
    label: "Kho công cụ dụng cụ",
    value: "TOOLS_AND_EQUIPMENT_STORAGE",
  },
  {
    label: "Kho bán thành phẩm",
    value: "SEMI_FINISHED_PRODUCTS_STORAGE",
  },
  {
    label: "Kho thành phẩm",
    value: "FINISHED_PRODUCTS_STORAGE",
  },
  {
    label: "Kho thương mại",
    value: "GOODS_STORAGE",
  },
  {
    label: "Kho đồng phục",
    value: "UNIFORM_WAREHOUSE",
  },
];

const warehouseTypeMapName = {
  [warehouseTypeSelect[0].value]: warehouseTypeSelect[0].label,
  [warehouseTypeSelect[1].value]: warehouseTypeSelect[1].label,
  [warehouseTypeSelect[2].value]: warehouseTypeSelect[2].label,
  [warehouseTypeSelect[3].value]: warehouseTypeSelect[3].label,
  [warehouseTypeSelect[4].value]: warehouseTypeSelect[4].label,
  [warehouseTypeSelect[5].value]: warehouseTypeSelect[5].label,
};

export default {
  warehouseTypeSelect,
  warehouseTypeMapName,
};
