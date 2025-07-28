import { useQuery } from "@tanstack/react-query";
import { PaginationResponse } from "app/shared/model/pagination.model";
import axios from "axios";
import { ComponentProps } from "react";
import { Path } from "react-hook-form";
import WrapSelect from "./WrapSelect";
import { MapKeySelect } from "app/shared/model/arr-obj.model";
import useWorkspace from "app/hooks/use-workspace";

const {
    useGetWorkspacesQuery
} = useWorkspace

type Props<T> = Partial<Omit<ComponentProps<typeof WrapSelect>, 'name'>> & {
  name: Path<T>;
  label?: string;
};

const WorkSpace = <T = any>(props: Props<T>) => {
  const { name, label } = props;

  const query: MapKeySelect = useGetWorkspacesQuery(
    {
        page: 0,
        size: 2000000
    },
    (res) => {
        let data = res?.data
        let obj = {}
        let arr = []
        data?.data?.forEach((item) => {
          let tmp = {
            label: item?.normalizedName,
            value: item?.id
          }
          arr.push(tmp)
          obj[item?.id] = { ...item, label: tmp.label }
        })
        return { arr, obj, totalRecord: data?.totalRecord }
    }
)

  return <WrapSelect {...props} label={label} name={name} data={query?.data} />;
};

export default WorkSpace;