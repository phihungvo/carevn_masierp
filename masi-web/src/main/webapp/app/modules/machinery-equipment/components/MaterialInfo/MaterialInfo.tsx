import ButtonAdd from 'app/components/ButtonV2/ButtonAdd';
import ButtonDelete from 'app/components/ButtonV2/ButtonDelete';
import Flex from 'app/components/flex/flex';
import TableV2, { TableColumns } from 'app/components/table-v2/Table';
import useEmployee from 'app/hooks/use-employee';
import { useFormContext } from 'react-hook-form';
import './MaterialInfo.scss';
import FormInputV2 from 'app/components/formV2/form-input/form-input';
import FormSelect from 'app/components/form/form-select';
import useItems from 'app/hooks/use-items';
import { useSearchParams } from 'react-router-dom';
import { ItemType } from 'app/shared/model/enumerations/item-category.model';
import useUom from 'app/hooks/use-uom';
import useInventoriesStorage from 'app/hooks/use-inventories-storage';
import { DEFAULT_PAGE, DEFAULT_PAGE_SIZE_NAX } from 'app/constants/common';

const { useGetDepreciationInventoriesStorageQuery } = useInventoriesStorage;
const { useGetEmployeesQuery } = useEmployee;
const { useGetUoms } = useUom;

const MaterialInfo = (props: any) => {
  const { name, data } = props;
  const methods = useFormContext();
  const [searchParams] = useSearchParams();
  const { watch, setValue, formState, control } = methods;

  const dataValue = watch(name)
  const { data: items } = useGetDepreciationInventoriesStorageQuery({
    page: DEFAULT_PAGE,
    size: DEFAULT_PAGE_SIZE_NAX,
    'id.notEquals': data?.id,
    'status.doesNotContain': 'LIQUIDATION',
    checkDepreciation:true,
  })
  const { data: uoms } = useGetUoms();


  const columns: TableColumns<any> = [
    {
      header: { render: 'Mã vật tư' },
      body: {
        render: ({ data, index }) => (
          <FormSelect
          control={control}
          name={`${name}.${index}.itemId`}
          placeholder="Chọn"
          options={items?.data?.map(i => ({
            label: `${i?.code}`,
            value: i?.id,
          }))}
          isClearable={false}
        />
        ),
      },
    },
    {
      header: { render: 'Tên vật tư' },
      body: {
        render: ({ data, index }) => {
          const itemSelected = (items?.data?? [])?.find(x => x.id === data.itemId);
          return itemSelected?.item?.name ?? '';
        },
      },
    },
    {
      header: { render: 'Đơn vị tính' },
      body: {
        render: ({ data, index }) => {
          const itemSelected = (items?.data?? [])?.find(x => x.id === data.itemId);
          return itemSelected?.item?.uom?.name ?? '';
        },
      },
    },

    {
      header: { render: 'Ghi chú' },
      body: {
        render: ({ data, index }) => (
          <FormInputV2
          control={control}
          key={`${name}.${index}.note`}
          id={`${name}.${index}.note`}
          name={`${name}.${index}.note`}
          placeholder="Điền"
        />
        ),
      },
    },
    {
      header: { render: '' },
      body: {
        render: ({ data, index }) => (
          <ButtonDelete  onClick={()=>removeRow(index)}/>
        ),
      },
    },
  ];

  const removeRow = (index: number) => {
    const dataTmp = [...(dataValue ?? [])];
    dataTmp.splice(index, 1);
    setValue(name, dataTmp);
  }

  const addRow = () => {
    const dataTmp = [...(dataValue ?? [])];
    dataTmp.push({ itemId: "" });
    setValue(name, dataTmp);
  }

  return (
    <Flex direction="column" gap={8} className="ps">
      <div className="ps_header">
        <span>Thông số</span>
        <ButtonAdd
          data-id={`add_${name}`}
          text="Thêm"
          onClick={addRow}
        />
      </div>

      <TableV2<any>
        table_id={name}
        columns={columns}
        data={dataValue}
      />
    </Flex>
  );
};

export default MaterialInfo;
