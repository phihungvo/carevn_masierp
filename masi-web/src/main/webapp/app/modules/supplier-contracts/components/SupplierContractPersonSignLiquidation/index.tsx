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
import { SUPPLIER_CONTRACT_STATUS } from 'app/shared/model/supplier-contract.model';
import { SupplierContractsSchema } from 'app/validation/supplier-contracts.validation';
import dayjs from 'dayjs';
import { useFormContext } from 'react-hook-form';

const { useGetEmployeesQuery } = useEmployee;

const SupplierContractPersonSignLiquidation = () => {
  const methods = useFormContext<SupplierContractsSchema>();
  const { watch, setValue, formState } = methods;

  const watchRequestApprovals = watch('liquidationRequestApprovals');
  const statusWatch = watch('status');

  const { data: employeeProfiles } = useGetEmployeesQuery();

  const disabled = false;
    // statusWatch === (SUPPLIER_CONTRACT_STATUS.APPROVED as string) ||
    // statusWatch === (SUPPLIER_CONTRACT_STATUS.REJECTED_LIQUIDATION as string) ||
    // statusWatch === (SUPPLIER_CONTRACT_STATUS.EXPIRED as string);

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
            name={`liquidationRequestApprovals.${index}.employeeId`}
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
              setValue('liquidationRequestApprovals', tmp);
            }}
            disabled={!disabled}
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
              setValue('liquidationRequestApprovals', newArr);
            }}
            disabled={!disabled}
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
      setValue('liquidationRequestApprovals', [
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
        <span>Người ký thanh lý</span>
        <ButtonAdd
          text="Thêm người ký"
          onClick={() => addPersonSign()}
          disabled={
            watchRequestApprovals?.length === DEFAULT_MAX_REQUEST_APPROVE ||
            !disabled
          }
        />
      </div>

      <TableV2<any>
        table_id="person_sign"
        columns={columns}
        data={[...(watchRequestApprovals ?? [])]}
      />

    </Flex>
  );
};

export default SupplierContractPersonSignLiquidation;
