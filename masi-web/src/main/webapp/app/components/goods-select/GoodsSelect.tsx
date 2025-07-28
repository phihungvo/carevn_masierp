import useItems from 'app/hooks/use-items';
import { ConvertedGoodsType } from 'app/validation/supplies-request.validation';
import { ComponentProps } from 'react';
import Select from '../select/select';

const { useGetItemsQuery } = useItems;

export type SelectItem = {
  label: string;
  value: string;
}

type Props = ComponentProps<typeof Select> & {
  onChooseGood: (good: ConvertedGoodsType['itemDTO'], selected: SelectItem) => void;
};

const GoodsSelect = (props: Props) => {
  const { onChooseGood, name, options, defaultValue, ...rest } = props;

  const items_query = useGetItemsQuery(
    {},
    {
      select: (data: any) => {
        let obj = {};
        let tmp = data?.data?.data.map(item => {
          const { id, code, name } = item;
          obj[id] = {
            id,
            code,
            label: code + ' - ' + name,
            name,
            vatId: item?.vatId,
            vatRate: item?.vatRate,
            uomId: item?.uomId
          } as ConvertedGoodsType['itemDTO'];
          return {
            label: code + ' - ' + name,
            value: id,
          };
        });

        return {
          data: tmp,
          obj,
          totalRecord: data?.data?.totalRecord,
        };
      },
    },
  );

  const onChange = (item: SelectItem) => {
    onChooseGood(items_query?.data?.obj?.[item?.value], item);
  }

  return (
    <Select
      {...rest}
      options={items_query?.data?.data || []}
      onChange={onChange}
      isClearable
      isSearchable
    />
  );
};

export default GoodsSelect;
