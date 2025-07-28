import ButtonAdd from 'app/components/ButtonV2/ButtonAdd';
import ButtonDelete from 'app/components/ButtonV2/ButtonDelete';
import Flex from 'app/components/flex/flex';
import TableV2, { TableColumns } from 'app/components/table-v2/Table';
import useEmployee from 'app/hooks/use-employee';
import { useFormContext } from 'react-hook-form';
import './AdvanceInfo.scss';
import FormInputV2 from 'app/components/formV2/form-input/form-input';

const { useGetEmployeesQuery } = useEmployee;

const AdvanceInfo = (props: any) => {
  const { name } = props;
  const methods = useFormContext();
  const { watch, setValue, formState, control } = methods;

  const data = watch(name)

  const columns: TableColumns<any> = [
    {
      header: { render: 'Thông số kỷ thuật' },
      body: {
        render: ({ data, index }) => (
          <FormInputV2
          control={control}
          key={`${name}.${index}.code`}
          id={`${name}.${index}.code`}
          name={`${name}.${index}.code`}
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
          key={`${name}.${index}.value`}
          id={`${name}.${index}.value`}
          name={`${name}.${index}.value`}
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
    const dataTmp = [...(data ?? [])];
    dataTmp.splice(index, 1);
    setValue(name, dataTmp);
  }

  const addRow = () => {
    const dataTmp = [...(data ?? [])];
    dataTmp.push({ code: '', value: '' });
    setValue(name, dataTmp);
  }

  return (
    <Flex direction="column" gap={8} className="ps">
      <div className="ps_header">
        <span>Thông số</span>
        <ButtonAdd
          data-id="add-person-sign"
          text="Thêm"
          onClick={addRow}
        />
      </div>

      <TableV2<any>
        table_id="person_sign"
        columns={columns}
        data={data}
      />
    </Flex>
  );
};

export default AdvanceInfo;
