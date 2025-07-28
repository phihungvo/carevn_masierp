import { zodResolver } from '@hookform/resolvers/zod';
import ButtonPrint from 'app/components/ButtonV2/ButtonPrint';
import ButtonV2 from 'app/components/ButtonV2/ButtonV2';
import CardV2 from 'app/components/CardV2/CardV2';
import Flex from 'app/components/flex/flex';
import { Typography } from 'app/components/typography/typography';
import { useAppSelector } from 'app/config/store';
import { PATH } from 'app/constants/path';
import { useGetSupplierContractsById } from 'app/hooks/use-supplier-contract';
import { FORM } from 'app/shared/model/enumerations/form.model';
import { SUPPLIER_CONTRACT_STATUS } from 'app/shared/model/supplier-contract.model';
import { convertCurrency } from 'app/shared/util/format';
import {
  supplierContractsSchema,
  SupplierContractsSchema,
} from 'app/validation/supplier-contracts.validation';
import { useContext, useEffect } from 'react';
import { FormProvider, useForm } from 'react-hook-form';
import { DateObject } from 'react-multi-date-picker';
import { useNavigate, useParams } from 'react-router';
import SupplierContractsForm from './components/supplier-contracts-form';
import SupplierContractsApproveModal from './modals/supplier-contracts-approve-modal';
import SupplierContractsApproveSuccessModal from './modals/supplier-contracts-approve-success-modal';
import SupplierContractsAttachmentModal from './modals/supplier-contracts-attachment-modal';
import SupplierContractsConfirmReject from './modals/supplier-contracts-confirm-reject';
import SupplierContractsConfirmReview from './modals/supplier-contracts-confirm-review';
import SupplierContractsRejectSuccessModal from './modals/supplier-contracts-reject-success-modal';
import SupplierContractsReviewSuccessModal from './modals/supplier-contracts-review-success-modal';
import SupplierContractsUpdateSuccessModal from './modals/supplier-contracts-update-success-modal';
import { SupplierContractsContext } from './supplier-contracts-storage-provider';
import { useSearchParams } from 'react-router-dom';

const SupplierContractsUpdateForm = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [params, setSearchParams] = useSearchParams();

  const { toggleApprove, toggleReject, toggleReview, setSelectedRecord } =
    useContext(SupplierContractsContext);

  const { data } = useGetSupplierContractsById(id);

  const account = useAppSelector(state => state.authentication.account);

  const methods = useForm<SupplierContractsSchema>({
    resolver: zodResolver(supplierContractsSchema),
    defaultValues: {
      createdAt: new DateObject(),
      requestApprovals: [],
    },
  });

  const { setValue, watch, setError, handleSubmit, formState } = methods;
  const statusWatch = watch('status');
  const liquidationRequestApprovalsW = watch('liquidationRequestApprovals');

  const isLiquidation = false;
  // statusWatch === (SUPPLIER_CONTRACT_STATUS.APPROVED as string) ||
  // statusWatch === (SUPPLIER_CONTRACT_STATUS.WAITING_LIQUIDATION as string) ||
  // statusWatch === (SUPPLIER_CONTRACT_STATUS.REJECTED_LIQUIDATION as string) ||
  // statusWatch === (SUPPLIER_CONTRACT_STATUS.EXPIRED as string) ||
  // statusWatch === (SUPPLIER_CONTRACT_STATUS.LIQUIDATED as string);

  useEffect(() => {
    setValue('contractCode', data?.contractCode);
    setValue('contractName', data?.contractName);
    setValue('paymentTermNumber', `${data?.paymentTermNumber ?? 0}`);
    setValue('contractDate', new DateObject(data?.contractDate));
    setValue('deliveryEstDate', new DateObject(data?.deliveryEstDate));
    setValue('startDate', new DateObject(data?.startDate));
    setValue('endDate', new DateObject(data?.endDate));
    setValue('contractAmount', `${data?.contractAmount ?? 0}`);
    setValue('note', data?.note);
    setValue('requestApprovals', data?.requestApprovals);

    setValue('createdBy', data?.createdBy);

    setValue('note', data?.note);

    setValue('status', `${data?.status}`);

    setValue('totalAmount', `${data?.totalAmount}`);

    setValue('supplierId', data?.supplierId);
    if (data?.supplier) {
      setValue('supplierTaxCode', data?.supplier?.taxCode);
      setValue('supplierAddress', data?.supplier?.address);
    }
    setValue('supplierFullName', data?.supplierFullName);
    setValue('supplierPosition', data?.supplierPosition);
    setValue('supplierPhone', data?.supplierPhone);
    setValue('supplierEmail', data?.supplierEmail);

    setValue('suppliesRequestId', data?.suppliesRequestId);

    setValue('invoices', data?.incomingInvoices);

    setValue(
      'supplierContractDetails',
      data?.supplierContractDetails?.map(item => ({
        id: item.id,
        code: item.code,
        itemId: item.supplyItemId,
        quantity: item.quantity?.toString(),
        price: item.price?.toString(),
        note: item.note,
        uomId: item.unitId,
        totalPrice: convertCurrency(item.totalAmount, false),
        vatId: item.vatId,
        vatRate: `${item.vatRate ?? 0}`,
        vatAmount: `${item.vatAmount}`,
      })),
    );

    setValue('requestApprovals', data?.requestApprovals);
    setValue('liquidationRequestApprovals', data?.liquidationRequestApprovals);

    const existAttachments = data?.attachments?.map(x => ({
      ...x,
      createdAt: new Date(x.createdAt),
    }));
    setValue('attachments', existAttachments);
  }, [data]);

  const handleApprove = () => {
    toggleApprove();
    setSelectedRecord(data?.id);
  };

  const handleReject = () => {
    toggleReject();
    setSelectedRecord(data?.id);
  };

  const handleReview = () => {
    toggleReview();
    setSelectedRecord(data?.id);
  };

  const handleReviewLiq = () => {
    if (
      statusWatch === (SUPPLIER_CONTRACT_STATUS.APPROVED as string) ||
      statusWatch === (SUPPLIER_CONTRACT_STATUS.EXPIRED as string) ||
      statusWatch === (SUPPLIER_CONTRACT_STATUS.REJECTED_LIQUIDATION as string)
    ) {
      if (liquidationRequestApprovalsW.length === 0)
        setError('liquidationRequestApprovals', {
          message: 'Vui lòng chọn người ký',
          type: 'min',
        });
      else handleReview();
    }
  };

  const isDisabledReview =
    account &&
    (data?.status === SUPPLIER_CONTRACT_STATUS.NEW ||
      data?.status === SUPPLIER_CONTRACT_STATUS.REJECTED) &&
    data?.createdBy === account.id;

  const isDisabledReject =
    account &&
    data?.status === SUPPLIER_CONTRACT_STATUS.WAITING_APPROVE &&
    data?.requestApprovals
      ?.filter(x => !x.approvedSign)
      ?.map(x => x.employeeId)
      .includes(account.id);

  const isDisabledApprove =
    account &&
    data?.status === SUPPLIER_CONTRACT_STATUS.WAITING_APPROVE &&
    data?.requestApprovals
      ?.filter(x => !x.approvedSign)
      ?.map(x => x.employeeId)
      .includes(account.id);

  const isDisabledReviewLiquidation =
    account &&
    (data?.status === SUPPLIER_CONTRACT_STATUS.APPROVED ||
      data?.status === SUPPLIER_CONTRACT_STATUS.EXPIRED ||
      data?.status === SUPPLIER_CONTRACT_STATUS.REJECTED_LIQUIDATION) &&
    data?.createdBy === account.id;

  const isDisabledRejectLiquidation =
    account &&
    data?.status === SUPPLIER_CONTRACT_STATUS.WAITING_LIQUIDATION &&
    data?.liquidationRequestApprovals
      ?.filter(x => !x.approvedSign)
      ?.map(x => x.employeeId)
      .includes(account.id);

  const isDisabledApproveLiquidation =
    account &&
    data?.status === SUPPLIER_CONTRACT_STATUS.WAITING_LIQUIDATION &&
    data?.liquidationRequestApprovals
      ?.filter(x => !x.approvedSign)
      ?.map(x => x.employeeId)
      .includes(account.id);

  return (
    <>
      <FormProvider {...methods}>
        <CardV2
          header={
            <Flex justify="space-between" align="center">
              <Typography level={4}>Cập nhật hợp đồng mua</Typography>
              <Flex align="center" gap={10}>
                <ButtonV2 onClick={() => navigate(PATH.SUPPLIER_CONTRACTS)}>
                  Đóng
                </ButtonV2>
                <ButtonV2
                  color="blue"
                  variant="solid"
                  form={FORM.SUPPLIER_CONTRACTS}
                  type="submit"
                >
                  Lưu
                </ButtonV2>
                <ButtonPrint
                  onClick={() => {
                    if (data?.id) {
                      setSearchParams({
                        printId: data?.id,
                        printType: 'SUPPLIER_CONTRACTS',
                      });
                      setTimeout(() => window.print(), 500);
                    }
                  }}
                  disabled={!data?.id}
                />
              </Flex>
            </Flex>
          }
        >
          <SupplierContractsForm type="update" />
        </CardV2>

        <SupplierContractsAttachmentModal />
      </FormProvider>

      <SupplierContractsApproveModal isLiquidation={isLiquidation} />
      <SupplierContractsApproveSuccessModal
        directUrl={PATH.SUPPLIER_CONTRACTS + location.search}
        isLiquidation={isLiquidation}
      />
      <SupplierContractsConfirmReject isLiquidation={isLiquidation} />
      <SupplierContractsRejectSuccessModal
        directUrl={PATH.SUPPLIER_CONTRACTS + location.search}
        isLiquidation={isLiquidation}
      />
      <SupplierContractsConfirmReview isLiquidation={isLiquidation} />
      <SupplierContractsReviewSuccessModal
        directUrl={PATH.SUPPLIER_CONTRACTS + location.search}
        isLiquidation={isLiquidation}
      />
      <SupplierContractsUpdateSuccessModal />
    </>
  );
};

export default SupplierContractsUpdateForm;
