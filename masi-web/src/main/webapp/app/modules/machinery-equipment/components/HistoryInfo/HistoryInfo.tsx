import ButtonAdd from 'app/components/ButtonV2/ButtonAdd';
import ButtonDelete from 'app/components/ButtonV2/ButtonDelete';
import Flex from 'app/components/flex/flex';
import TableV2, { TableColumns } from 'app/components/table-v2/Table';
import useEmployee from 'app/hooks/use-employee';
import { useFormContext } from 'react-hook-form';
import './HistoryInfo.scss';
import FormInputV2 from 'app/components/formV2/form-input/form-input';

const { useGetEmployeesQuery } = useEmployee;

const HistoryInfo = (props: any) => {
  const { name } = props;
  const methods = useFormContext();
  const { watch, setValue, formState, control } = methods;

  const dataValue = watch(name, )
  const columns: TableColumns<any> = [
    {
      header: { render: 'Ngày yêu cầu' },
      body: {
        render: ({ data, index }) => (
          <FormInputV2
          control={control}
          key={`${name}.${index}.dateRequest`}
          id={`${name}.${index}.dateRequest`}
          name={`${name}.${index}.dateRequest`}
          placeholder="Điền"
        />
        ),
      },
    },
    {
      header: { render: 'Công trình' },
      body: {
        render: ({ data, index }) => (
          <FormInputV2
          control={control}
          key={`${name}.${index}.project`}
          id={`${name}.${index}.project`}
          name={`${name}.${index}.project`}
          placeholder="Điền"
        />
        ),
      },
    },
    {
      header: { render: 'Tình trạng hư' },
      body: {
        render: ({ data, index }) => (
          <FormInputV2
          control={control}
          key={`${name}.${index}.conditionDamaged`}
          id={`${name}.${index}.conditionDamaged`}
          name={`${name}.${index}.conditionDamaged`}
          placeholder="Điền"
        />
        ),
      },
    },
    {
      header: { render: 'Di chuyển từ' },
      body: {
        render: ({ data, index }) => (
          <FormInputV2
          control={control}
          key={`${name}.${index}.from`}
          id={`${name}.${index}.from`}
          name={`${name}.${index}.from`}
          placeholder="Điền"
        />
        ),
      },
    },
    {
      header: { render: 'Di chuyển đến' },
      body: {
        render: ({ data, index }) => (
          <FormInputV2
          control={control}
          key={`${name}.${index}.to`}
          id={`${name}.${index}.to`}
          name={`${name}.${index}.to`}
          placeholder="Điền"
        />
        ),
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
    dataTmp.push({ dateRequest: '' });
    setValue(name, dataTmp);
  }

  return (
    <Flex direction="column" gap={8} className="ps">
      <div className="ps_header">
        <span>Lịch sử thiết bị</span>
        <ButtonAdd
          data-id="add-person-sign"
          text="Thêm"
          onClick={addRow}
        />
      </div>

      <TableV2<any>
        table_id="person_sign"
        columns={columns}
        data={[...(dataValue ?? [])]}
      />
    </Flex>
  );
};

export default HistoryInfo;
