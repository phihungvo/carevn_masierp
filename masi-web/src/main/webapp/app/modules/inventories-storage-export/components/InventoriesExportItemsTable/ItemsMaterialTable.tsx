import ButtonDelete from 'app/components/ButtonV2/ButtonDelete';
import EllipsisParagraph from 'app/components/ellipsis-paragraph/ellipsis-paragraph';
import FormSelect from 'app/components/form/form-select';
import FormDatePickerV2 from 'app/components/formV2/form-date/form-date-picker';
import FormInputV2 from 'app/components/formV2/form-input/form-input';
import TableV2 from 'app/components/table-v2/Table';
import Tooltip from 'app/components/tooltip/tooltip';
import useEmployee from 'app/hooks/use-employee';
import { INVENTORIES_STATUS } from 'app/shared/model/inventories-storage.model';
import { IItem } from 'app/shared/model/item.model';
import { IWorkspace } from 'app/shared/model/workspace.model';
import { convertCurrency } from 'app/shared/util/format';
import { InventoriesExportSchema } from 'app/validation/inventories-export.validation';
import { useFormContext } from 'react-hook-form';

const { useGetEmployeesQuery } = useEmployee;

interface ItemsMaterialTableProps {
  items: IItem[];
  workspaces: IWorkspace[];
  remove: (index: number) => void;
}
export const ItemsMaterialTable = (props: ItemsMaterialTableProps) => {
  const { items, workspaces, remove } = props;

  const { control, watch, setValue, formState } =
    useFormContext<InventoriesExportSchema>();

  const inventoriesMaterialDetails = watch('inventoriesMaterialDetails');

  const { data: employees } = useGetEmployeesQuery();

  const columns = [
    {
      header: { render: `Mã TS - CCDC` },
      body: {
        render: ({ index }) => (
          <FormSelect
            key={`inventoriesMaterialDetails.${index}.itemId`}
            control={control}
            name={`inventoriesMaterialDetails.${index}.itemId`}
            placeholder="Chọn"
            options={[...(items ?? [])]?.map(i => ({
              label: `${i?.code} - ${i?.name}`,
              value: i?.id,
            }))}
            disabled={disabled}
            onChanges={e => {
              const itemSelected = [...(items ?? [])]?.find(x => x.id === e);
              if (itemSelected) {
                setValue(
                  `inventoriesMaterialDetails.${index}.unitPrice`,
                  `${itemSelected?.unitPrice ?? 0}`,
                );
                const grpName = `${itemSelected?.itemCategory?.code ?? ''} - ${
                  itemSelected?.itemCategory?.name ?? ''
                }`;
                setValue(
                  `inventoriesMaterialDetails.${index}.groupName`,
                  `${grpName}`,
                );
              }
            }}
            isClearable={false}
          />
        ),
      },
    },
    {
      header: { render: 'Ngày ĐK' },
      body: {
        render: ({ index }) => (
          <FormDatePickerV2
            key={`inventoriesMaterialDetails.${index}.registerDate`}
            control={control}
            formState={formState}
            name={`inventoriesMaterialDetails.${index}.registerDate`}
            placeholder="Chọn"
            setValue={setValue}
            disabled={disabled}
          />
        ),
      },
    },
    {
      header: { render: 'Ngày khấu hao' },
      body: {
        render: ({ index }) => (
          <FormDatePickerV2
            key={`inventoriesMaterialDetails.${index}.depreciationDate`}
            control={control}
            formState={formState}
            name={`inventoriesMaterialDetails.${index}.depreciationDate`}
            placeholder="Chọn"
            setValue={setValue}
            disabled={disabled}
          />
        ),
      },
    },
    {
      header: { render: 'Bộ phận SD' },
      body: {
        render: ({ index }) => (
          <FormSelect
            key={`inventoriesMaterialDetails.${index}.departmentId`}
            control={control}
            name={`inventoriesMaterialDetails.${index}.departmentId`}
            placeholder="Chọn"
            options={workspaces?.map(u => ({
              label: u?.name,
              value: u?.id,
            }))}
            disabled={disabled}
          />
        ),
      },
    },
    {
      header: { render: 'Nhóm' },
      body: {
        render: ({ data }) => (
          <Tooltip label={data?.groupName} target={`groupName-${data.id}`}>
            <EllipsisParagraph
              text={data?.groupName}
              id={`groupName-${data.id}`}
              width={200}
            />
          </Tooltip>
        ),
      },
    },
    {
      header: { render: 'Tháng SD' },
      body: {
        render: ({ index }) => (
          <FormInputV2
            key={`inventoriesMaterialDetails.${index}.usageMonth`}
            control={control}
            name={`inventoriesMaterialDetails.${index}.usageMonth`}
            placeholder="Điền"
            disabled={disabled}
            style={{ width: '80px' }}
          />
        ),
      },
    },
    {
      header: { render: 'TK TS' },
      body: {
        render: ({ index }) => (
          <FormSelect
            key={`inventoriesMaterialDetails.${index}.holder`}
            control={control}
            name={`inventoriesMaterialDetails.${index}.holder`}
            placeholder="Chọn"
            options={employees?.data?.map(e => ({
              label: `${e?.code} - ${e.employeeProfile?.fullName}`,
              value: e?.id,
            }))}
            disabled={disabled}
          />
        ),
      },
    },
    {
      header: { render: 'TK KH/PB' },
      body: {
        render: ({ index }) => (
          <FormSelect
            key={`inventoriesMaterialDetails.${index}.depreciationAllocation`}
            control={control}
            name={`inventoriesMaterialDetails.${index}.depreciationAllocation`}
            placeholder="Chọn"
            options={employees?.data?.map(e => ({
              label: `${e?.code} - ${e.employeeProfile?.fullName}`,
              value: e?.id,
            }))}
            disabled={disabled}
          />
        ),
      },
    },
    {
      header: { render: 'TK chi phí' },
      body: {
        render: ({ index }) => (
          <FormSelect
            key={`inventoriesMaterialDetails.${index}.expenseAccount`}
            control={control}
            name={`inventoriesMaterialDetails.${index}.expenseAccount`}
            placeholder="Chọn"
            options={employees?.data?.map(e => ({
              label: `${e?.code} - ${e.employeeProfile?.fullName}`,
              value: e?.id,
            }))}
            disabled={disabled}
          />
        ),
      },
    },
    {
      header: { render: 'Yếu tố CP' },
      body: {
        render: ({ index }) => (
          <FormInputV2
            key={`inventoriesMaterialDetails.${index}.costElements`}
            control={control}
            name={`inventoriesMaterialDetails.${index}.costElements`}
            placeholder="Điền"
            style={{ width: '150px' }}
            disabled={disabled}
          />
        ),
      },
    },
    {
      header: { render: 'SL' },
      body: {
        render: ({ index }) => (
          <FormInputV2
            key={`inventoriesMaterialDetails.${index}.quantity`}
            control={control}
            name={`inventoriesMaterialDetails.${index}.quantity`}
            placeholder="Điền"
            style={{ width: '80px' }}
            disabled={disabled}
          />
        ),
      },
    },
    {
      header: { render: 'Nguyên giá' },
      body: { render: ({ data }) => convertCurrency(data?.unitPrice, false) },
    },
    {
      header: { render: 'Ghi chú' },
      body: {
        render: ({ index }) => (
          <FormInputV2
            key={`inventoriesMaterialDetails.${index}.note`}
            control={control}
            name={`inventoriesMaterialDetails.${index}.note`}
            placeholder="Điền"
            disabled={disabled}
            style={{ width: '250px' }}
          />
        ),
      },
    },
    {
      header: { render: '' },
      body: {
        render: ({ index }) => (
          <ButtonDelete onClick={() => remove(index)} disabled={disabled} />
        ),
      },
    },
  ];

  const disabled =
    watch('status') === (INVENTORIES_STATUS.WAITING_APPROVED as string) ||
    watch('status') === (INVENTORIES_STATUS.CANCELLED as string) ||
    watch('status') === (INVENTORIES_STATUS.APPROVED as string) ||
    watch('status') === (INVENTORIES_STATUS.COMPLETED as string);

  return (
    <TableV2
      table_id="inventories-detail-items-material"
      columns={columns}
      data={inventoriesMaterialDetails || []}
    />
  );
};
