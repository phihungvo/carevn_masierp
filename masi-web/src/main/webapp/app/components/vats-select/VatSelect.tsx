import useVatRate from 'app/hooks/use-vat-rate';
import { ComponentProps } from 'react';
import Select from '../select/select';

const { useGetVatRates } = useVatRate;

type Props = ComponentProps<typeof Select> & {
  selectedID?: string;
}

const VatSelect = (props: Props) => {
  const { selectedID, ...rest } = props

  const vats_query = useGetVatRates({}, res => {
    let data = res?.data;
    let arr = [];
    let obj = {};
    data?.data?.forEach(item => {
      let tmp = {
        label: item?.code + ' - ' + item?.name,
        value: item.id,
        percent: item.value,
      };
      obj[item.id] = tmp;
      arr.push(tmp);
    });
    return {
      arr,
      obj,
      totalRecord: data?.totalRecord,
    };
  });

  return (
    <Select
      placeholder="Chọn"
      value={selectedID && [{
        label: vats_query?.data?.obj[selectedID]?.label,
        value: selectedID,
      }]}
      {...rest}
      options={vats_query?.data?.arr}
    />
  );
};

export default VatSelect;
