import { useQuery } from "@tanstack/react-query";
import { PaginationResponse } from "app/shared/model/pagination.model";
import axios from "axios";
import { ComponentProps } from "react";
import { Path } from "react-hook-form";
import WrapSelect from "./WrapSelect";
import { MapKeySelect } from "app/shared/model/arr-obj.model";

type Props<T> = Partial<Omit<ComponentProps<typeof WrapSelect>, 'name'>> & {
  name: Path<T>;
  label?: string;
};

const GroupCode = <T = any>(props: Props<T>) => {
  const { name, label } = props;

  const query: MapKeySelect = useQuery({
    queryKey: ['itemCategory-groupCode'],
    queryFn: () => axios.get<PaginationResponse<GroupCode>>('/services/masilogistics/api/item-categories'),
    select: res => {
      let data = res?.data
      let obj = {}
      let arr = []
      data?.data?.forEach((item) => {
        let tmp = {
          label: item?.code + ' - ' + item?.name,
          value: item?.id
        }
        arr.push(tmp)
        obj[item?.id] = { ...item, label: tmp.label }
      })
      return { arr, obj, totalRecord: data?.totalRecord }
  }});

  return <WrapSelect {...props} label={label} name={name} data={query?.data} />;
};

export default GroupCode;

type GroupCode = {
  id: string,
  code: string,
  name: string,
  company: string,
  isDeleted: boolean,
  createdBy: string,
  createdDate: string,
  warehouseType: {
    id: string,
    name: string
  },
  items: [],
  warehouseTypeId: string
}
