import ButtonAdd from 'app/components/ButtonV2/ButtonAdd';
import ButtonDelete from 'app/components/ButtonV2/ButtonDelete';
import Flex from 'app/components/flex/flex';
import FormError from 'app/components/form/form-error';
import TableV2, { TableColumns } from 'app/components/table-v2/Table';
import TotalMoney from 'app/components/TotalMoney/TotalMoney';
import { Typography } from 'app/components/typography/typography';
import { DATE_FORMAT } from 'app/constants/common';
import { PAYMENT_REQUEST_STATUS } from 'app/shared/model/enumerations/payment-request';
import { IIncomingInvoice } from 'app/shared/model/incoming-invoice.model';
import { convertCurrency } from 'app/shared/util/format';
import { RefundRequestSchema } from 'app/validation/request-payment.validation';
import dayjs from 'dayjs';
import { useState } from 'react';
import { useFormContext } from 'react-hook-form';
import ModalSelectAdvanceRequest from './ModalSelectAdvanceRequest';

const RefundRequestDepositBill = () => {
  const [isOpenSelectHTU, setIsOpenSelectHTU] = useState(false);

  const { setValue, watch, formState } = useFormContext<RefundRequestSchema>();
  const reimbursementsWatch = watch('reimbursements');

  const toggleOpenSelectHTU = () => setIsOpenSelectHTU(!isOpenSelectHTU);

  const disabled =
    watch('status') === (PAYMENT_REQUEST_STATUS.WAITING_APPROVE as string) ||
    watch('status') === (PAYMENT_REQUEST_STATUS.CANCELLED as string) ||
    watch('status') === (PAYMENT_REQUEST_STATUS.APPROVED as string);

  const columns: TableColumns<any> = [
    {
      header: { render: 'Chứng từ số' },
      body: { render: ({ data }) => data?.advancement?.code },
    },
    {
      header: { render: 'Ngày lập' },
      body: {
        render: ({ data }) =>
          dayjs(data?.advancement?.createdDate).format(DATE_FORMAT.DATE),
      },
    },
    {
      header: { render: 'Số tiền' },
      body: {
        render: ({ data }) =>
          convertCurrency(data?.advancement?.totalAmount, false),
      },
    },
    {
      header: { render: 'Số tiền phiếu chi' },
      body: {
        render: ({ data }) =>
          convertCurrency(data?.advancement?.paymentVoucherAmount, false),
      },
    },
    {
      header: { render: 'Ghi chú' },
      body: { render: ({ data }) => data?.advancement?.content },
    },
    {
      header: { render: <></> },
      body: {
        render: ({ index }) => (
          <ButtonDelete
            onClick={() => {
              const newArr = JSON.parse(
                JSON.stringify(reimbursementsWatch),
              ).filter((_x, idx) => idx !== index);
              setValue('reimbursements', newArr);
            }}
            disabled={disabled}
          />
        ),
      },
    },
  ];

  const onIncomingChange = (data?: IIncomingInvoice[]) => {
    setValue(
      'reimbursements',
      data.map((x: IIncomingInvoice) => ({
        advanceId: x?.id,
        advancement: { ...x, totalAmount: Number(x.totalAmount) },
      })),
    );
  };

  return (
    <Flex direction="column" gap={12} className="spent">
      <Flex
        style={{ marginRight: '80px' }}
        justify="space-between"
        align="center"
      >
        <Flex align="center" gap={32}>
          <Typography
            level={5}
            style={{
              fontSize: '18px',
              fontWeight: '500',
              letterSpacing: '-0.5px',
            }}
          >
            I. Phiếu tạm ứng
          </Typography>
          <ButtonAdd
            text="Thêm chi tiêu"
            type="button"
            onClick={toggleOpenSelectHTU}
            disabled={disabled}
          />
        </Flex>

        <TotalMoney
          total={[...(reimbursementsWatch ?? [])]?.reduce(
            (acc, obj) =>
              acc + Number(obj?.advancement?.paymentVoucherAmount ?? 0),
            0,
          )}
        />
      </Flex>

      <TableV2<any>
        table_id="spent"
        columns={columns}
        data={[...(reimbursementsWatch ?? [])]}
      />

      {formState.errors?.reimbursements?.message ||
        (formState.errors?.reimbursements?.root?.message &&
          [...(reimbursementsWatch ?? [])]?.length === 0 && (
            <FormError
              message={
                formState.errors?.reimbursements?.message ||
                formState.errors?.reimbursements?.root?.message
              }
            />
          ))}

      <ModalSelectAdvanceRequest
        onIncomingChange={onIncomingChange}
        isOpen={isOpenSelectHTU}
        toggle={toggleOpenSelectHTU}
      />
    </Flex>
  );
};

export default RefundRequestDepositBill;
