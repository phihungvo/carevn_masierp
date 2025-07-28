import Badge from 'app/components/badge/badge';
import BadgeV2 from 'app/components/badge/badge-v2';
import { Color } from 'app/shared/model/enumerations/color.model';


const maintenanceCycleList = [
  {
    label: "Ngày",
    value: "day",
  },
  {
    label: "Tuần",
    value: "week",
  },
  {
    label: "Tháng",
    value: "month",
  },
  {
    label: "Năm",
    value: "year",
  },
]

const machineryEquipmentStatusMap = [
  {
    label: "Mới",
    value: "NEW",
  },
  {
    label: "Hoạt động",
    value: "ACTIVE",
  },
  {
    label: "Cần bảo trì",
    value: "MAINTENANCE_REQUIRED",
  },
  {
    label: "Hỏng",
    value: "BROKEN",
  }
]

const machineryEquipmentLocationMap = [
  {
    label: "Nhà máy",
    value: "FACTORY",
  },
  {
    label: "Văn phòng",
    value: "OFFICE",
  }
]

const machineryEquipmentStatusBadgeMapping = (status: string) => {
  switch (status) {
    case 'NEW':
      return <BadgeV2 className="bv2 pr-new">Mới</BadgeV2>;
    case 'ACTIVE':
      return <Badge color={Color.SUCCESS}>Hoạt động</Badge>;
    case 'MAINTENANCE_REQUIRED':
      return <Badge color={Color.WARNING}>Cần bảo trì</Badge>;
    case 'BROKEN':
      return <Badge color={Color.ERROR}>Hỏng</Badge>;
    default:
      return <></>;
  }
};

export default {
  machineryEquipmentStatusBadgeMapping,
  machineryEquipmentStatusMap,
  maintenanceCycleList,
  machineryEquipmentLocationMap,
};
