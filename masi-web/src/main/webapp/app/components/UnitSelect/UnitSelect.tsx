import { DEFAULT_PAGE, DEFAULT_PAGE_SIZE_NAX } from 'app/constants/common';
import useUom from 'app/hooks/use-uom';
import { MapKeySelect } from 'app/shared/model/arr-obj.model';
import { ComponentProps } from 'react';
import Select from '../select/select';
import SelectV3 from '../select/SelectV3';

const { useGetUoms } = useUom;

type Props = ComponentProps<typeof SelectV3> & {
  selectedId?: string;
}

const UnitSelectV2 = (props: Props) => {
  const { selectedId, ...rest } = props;

  const uoms: MapKeySelect = useGetUoms(
    { size: DEFAULT_PAGE_SIZE_NAX, page: DEFAULT_PAGE },
    res => {
      let obj = {};
      let arr = [];
      res.data?.forEach(item => {
        let tmp = {
          label: item.name,
          value: item.id,
        };
        obj[item.id] = tmp;
        arr.push(tmp);
      });
      return {
        arr,
        obj,
        totalRecord: res.totalRecord,
      };
    },
  );

  return (
    <Select
      placeholder="Chọn"
      value={selectedId && [{
        label: uoms?.data?.obj?.[selectedId]?.label,
        value: selectedId,
      }]}
      {...rest}
      options={uoms?.data?.arr}
    />
  );
};

export default UnitSelectV2;
