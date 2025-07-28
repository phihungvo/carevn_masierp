import ButtonAdd from 'app/components/ButtonV2/ButtonAdd';
import ButtonDelete from 'app/components/ButtonV2/ButtonDelete';
import Flex from 'app/components/flex/flex';
import TableV2, { TableColumns } from 'app/components/table-v2/Table';
import useEmployee from 'app/hooks/use-employee';
import { useFormContext } from 'react-hook-form';
import './AdvanceInfo.scss';
import FormInputV2 from 'app/components/formV2/form-input/form-input';

const { useGetEmployeesQuery } = useEmployee;

const Materials = (props: any) => {
  const methods = useFormContext();
  const { watch, setValue, formState, control } = methods;
  const columns: TableColumns<any> = [
    {
      header: { render: 'Thông số kỷ thuật' },
      body: {
        render: ({ data, index }) => (
          <FormInputV2
          control={control}
          id="code"
          name="code"
          placeholder="Điền"
        />
        ),
      },
    },
    {
      header: { render: 'Giá trị' },
      body: {
        render: ({ data, index }) => (
          <FormInputV2
          control={control}
          id="code"
          name="code"
          placeholder="Điền"
        />
        ),
      },
    },
    {
      header: { render: '' },
      body: {
        render: ({ data, index }) => (
          <ButtonDelete />
        ),
      },
    },
  ];

  return (
    <Flex direction="column" gap={8} className="ps">
      <div className="ps_header">
        <span>Thông số</span>
        <ButtonAdd
          data-id="add-person-sign"
          text="Thêm"
        />
      </div>

      <TableV2<any>
        table_id="person_sign"
        columns={columns}
      />
    </Flex>
  );
};

export default Materials;
