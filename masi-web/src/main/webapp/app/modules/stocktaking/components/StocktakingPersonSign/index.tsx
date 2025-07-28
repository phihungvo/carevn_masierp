import ButtonAdd from 'app/components/ButtonV2/ButtonAdd';
import ButtonDelete from 'app/components/ButtonV2/ButtonDelete';
import EllipsisParagraph from 'app/components/ellipsis-paragraph/ellipsis-paragraph';
import Flex from 'app/components/flex/flex';
import FormError from 'app/components/form/form-error';
import FormSelect from 'app/components/form/form-select';
import TableV2, { TableColumns } from 'app/components/table-v2/Table';
import Tooltip from 'app/components/tooltip/tooltip';
import { DATE_FORMAT, DEFAULT_MAX_REQUEST_APPROVE } from 'app/constants/common';
import useEmployee from 'app/hooks/use-employee';
import { STOCKTAKING_STATUS } from 'app/shared/model/stocktaking.model';
import { StocktakingSchema } from 'app/validation/stocktaking.validation';
import dayjs from 'dayjs';
import { useFormContext } from 'react-hook-form';
import './style.scss';

const { useGetEmployeesQuery } = useEmployee;

const StocktakingPersonSign = () => {
  const methods = useFormContext<StocktakingSchema>();
  const { watch, setValue, formState } = methods;

  const watchRequestApprovals = watch('requestApprovals');
  const statusWatch = watch('status');

  const { data: employeeProfiles } = useGetEmployeesQuery();

  const disabled =
    statusWatch === (STOCKTAKING_STATUS.WAITING_APPROVED as string) ||
    statusWatch === (STOCKTAKING_STATUS.CANCELLED as string) ||
    statusWatch === (STOCKTAKING_STATUS.APPROVED as string) ||
    statusWatch === (STOCKTAKING_STATUS.COMPLETED as string);

  const getWorkspaceByEmpId = (id: string) => {
    const emp = employeeProfiles?.data?.find(x => x.id === id);
    if (emp) return emp.workspace?.name;
  };

  const columns: TableColumns<any> = [
    {
      header: { render: 'Thứ tự' },
      body: { render: ({ index }) => index + 1 },
    },
    {
      header: { render: 'Phòng ban' },
      body: {
        render: ({ data, index }) => (
          <Tooltip
            label={getWorkspaceByEmpId(data?.employeeId)}
            target={`department-${index}`}
          >
            <EllipsisParagraph
              text={getWorkspaceByEmpId(data?.employeeId)}
              width={200}
              id={`department-${index}`}
            />
          </Tooltip>
        ),
      },
    },
    {
      header: { render: 'Nhân viên' },
      body: {
        render: ({ data, index }) => (
          <FormSelect
            name={`requestApprovals.${index}.employeeId`}
            placeholder="Chọn"
            options={employeeProfiles?.data?.map(x => ({
              value: `${x.id}`,
              label: `${x.code} - ${x.lastName} ${x.firstName}`,
            }))}
            onChanges={e => {
              const selected = employeeProfiles?.data?.find(x => x.id === e);
              const tmp = JSON.parse(JSON.stringify(watchRequestApprovals));
              tmp[index] = {
                ...tmp[index],
                employeeId: e,
                employee: {
                  code: selected.code,
                  fullName: `${selected.lastName} ${selected?.firstName}`,
                },
                department: selected?.workspace?.name,
              };
              setValue('requestApprovals', tmp);
            }}
            disabled={disabled}
            isClearable={false}
            isOptionDisabled={e =>
              watchRequestApprovals?.map(x => x.employeeId)?.includes(e.value)
            }
          />
        ),
      },
    },
    {
      header: { render: 'Ngày ký' },
      body: {
        render: ({ data }) =>
          data?.result && data?.updatedAt
            ? dayjs(data?.updatedAt).format(DATE_FORMAT.DATE)
            : '',
      },
    },
    {
      header: { render: '' },
      body: {
        render: ({ index }) => (
          <ButtonDelete
            onClick={() => {
              const newArr = JSON.parse(
                JSON.stringify(watchRequestApprovals),
              ).filter((_x, idx) => idx !== index);
              setValue('requestApprovals', newArr);
            }}
            disabled={disabled}
          />
        ),
      },
    },
  ];

  const addPersonSign = () => {
    const currentArray = JSON.parse(
      JSON.stringify(watchRequestApprovals ?? []),
    );
    if (currentArray?.length < DEFAULT_MAX_REQUEST_APPROVE) {
      setValue('requestApprovals', [
        ...(currentArray ?? []),
        {
          index: currentArray?.length + 1,
          employeeId: '',
        },
      ]);
    }
  };

  return (
    <Flex direction="column" gap={8} className="ps">
      <div className="ps_header">
        <span>Người ký</span>
        <ButtonAdd
          text="Thêm người ký"
          onClick={() => addPersonSign()}
          disabled={
            watchRequestApprovals?.length === DEFAULT_MAX_REQUEST_APPROVE ||
            disabled
          }
        />
      </div>

      <TableV2<any>
        table_id="person_sign"
        columns={columns}
        data={[...(watchRequestApprovals ?? [])]}
      />

      {(formState.errors?.requestApprovals?.message ||
        formState.errors?.requestApprovals?.root) && (
        <FormError
          message={
            formState.errors?.requestApprovals?.message ||
            formState.errors?.requestApprovals?.root?.message
          }
        />
      )}
    </Flex>
  );
};

export default StocktakingPersonSign;
