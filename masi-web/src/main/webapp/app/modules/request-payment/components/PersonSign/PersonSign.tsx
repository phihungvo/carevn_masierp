import ButtonAdd from 'app/components/ButtonV2/ButtonAdd';
import ButtonDelete from 'app/components/ButtonV2/ButtonDelete';
import EllipsisParagraph from 'app/components/ellipsis-paragraph/ellipsis-paragraph';
import Flex from 'app/components/flex/flex';
import FormError from 'app/components/form/form-error';
import FormSelect from 'app/components/form/form-select';
import Input from 'app/components/input/input';
import TableV2, { TableColumns } from 'app/components/table-v2/Table';
import Tooltip from 'app/components/tooltip/tooltip';
import { DATE_FORMAT, DEFAULT_MAX_REQUEST_APPROVE } from 'app/constants/common';
import useEmployee from 'app/hooks/use-employee';
import { PAYMENT_REQUEST_STATUS } from 'app/shared/model/enumerations/payment-request';
import dayjs from 'dayjs';
import { useEffect } from 'react';
import { useFormContext } from 'react-hook-form';
import './PersonSign.scss';

const { useGetEmployeesQuery } = useEmployee;

type Props = {
  doNotSignKey?: string;
  disabledCheckBox?: boolean;
};

const PersonSign = ({
  doNotSignKey = 'isDoNotSign',
  disabledCheckBox,
}: Props) => {
  const methods = useFormContext();
  const { watch, setValue, formState } = methods;
  const watchRequestApprovals = watch('requestApprovals');
  const isDoNotSign = watch(doNotSignKey);
  const { data: employeeProfiles } = useGetEmployeesQuery();

  const disabled =
    watch('status') === (PAYMENT_REQUEST_STATUS.WAITING_APPROVE as string) ||
    watch('status') === (PAYMENT_REQUEST_STATUS.CANCELLED as string) ||
    watch('status') === (PAYMENT_REQUEST_STATUS.APPROVED as string);

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
              value: x.id,
              label: `${x?.code} - ${x?.lastName} ${x?.firstName} `,
            }))}
            onChanges={e => {
              const selected = employeeProfiles?.data?.find(x => x.id === e);
              const tmp = JSON.parse(JSON.stringify(watchRequestApprovals));
              tmp[index] = {
                ...tmp[index],
                employeeId: e,
                employee: {
                  code: selected?.code,
                  fullName: `${selected.lastName} ${selected?.firstName}`,
                },
                department: selected?.workspace?.name,
              };
              setValue('requestApprovals', tmp);
            }}
            disabled={disabled}
            isClearable={false}
            menuPlacement="top"
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
    if (currentArray.length < DEFAULT_MAX_REQUEST_APPROVE) {
      setValue('requestApprovals', [
        ...(currentArray ?? []),
        {
          index: currentArray?.length + 1,
          employeeId: '',
        },
      ]);
    }
  };

  const onCheckedChange = (e: React.ChangeEvent<HTMLInputElement>) =>
    setValue(doNotSignKey, e.target.checked);

  useEffect(() => {
    isDoNotSign && setValue('requestApprovals', []);
  }, [isDoNotSign]);

  return (
    <Flex direction="column" gap={8} className="ps">
      <div className="ps_header">
        <span>Người ký</span>
        <ButtonAdd
          data-id="add-person-sign"
          text="Thêm người ký"
          onClick={addPersonSign}
          disabled={
            watchRequestApprovals.length === DEFAULT_MAX_REQUEST_APPROVE ||
            disabled
          }
        />
        {!disabledCheckBox && (
          <Flex columnGap={12}>
            <Input
              type="checkbox"
              id="do-not-sign"
              value={isDoNotSign}
              onChange={!disabled && onCheckedChange}
              // disabled={disabled}
            />
            <label htmlFor="do-not-sign">Không trình ký duyệt</label>
          </Flex>
        )}
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
            (formState.errors?.requestApprovals?.message as string) ||
            (formState.errors?.requestApprovals?.root?.message as string)
          }
        />
      )}
    </Flex>
  );
};

export default PersonSign;
