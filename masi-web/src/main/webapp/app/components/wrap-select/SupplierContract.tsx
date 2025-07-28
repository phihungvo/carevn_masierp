import { useQuery } from "@tanstack/react-query";
import { MapKeySelect } from "app/shared/model/arr-obj.model";
import { PaginationResponse } from "app/shared/model/pagination.model";
import axios from "axios";
import { ComponentProps } from "react";
import { Path } from "react-hook-form";
import WrapSelect from "./WrapSelect";

type Props<T> = Partial<Omit<ComponentProps<typeof WrapSelect>, 'name'>> & {
  name: Path<T>;
  label?: string;
};

const SupplierContract = <T = any>(props: Props<T>) => {
  const { name, label } = props;

  const query: MapKeySelect = useQuery({
    queryKey: ['supplier-contract'],
    queryFn: () => {
        return axios.get<PaginationResponse<any>>(
            '/services/masilogistics/api/supplier-contracts', 
            { params: { size: 2000000, page: 0 } }
        )
    },
    select: res => {
        let data = res?.data
        let arr = []
        let obj = {}
        data?.data?.forEach(item => {
            let tmp = {
                label: item?.contractCode + ' - ' + item?.supplier?.name,
                value: item?.id
            }
            arr.push(tmp)
            obj[item?.id] = { ...item, label: tmp.label }
        })
        return { arr, obj, totalRecord: data?.totalRecord }
    }
  })

  return <WrapSelect {...props} label={label} name={name} data={query?.data} />;
};

export default SupplierContract;