import { zodResolver } from '@hookform/resolvers/zod';
import ButtonPrint from 'app/components/ButtonV2/ButtonPrint';
import ButtonV2 from 'app/components/ButtonV2/ButtonV2';
import CardV2 from 'app/components/CardV2/CardV2';
import Flex from 'app/components/flex/flex';
import { Typography } from 'app/components/typography/typography';
import { useAppSelector } from 'app/config/store';
import { PATH } from 'app/constants/path';
import useEmployee from 'app/hooks/use-employee';
import { useGetInventoriesById } from 'app/hooks/use-inventories';
import { FORM } from 'app/shared/model/enumerations/form.model';
import { InventoriesWarehouse } from 'app/shared/model/enumerations/warehouse.model';
import { INVENTORIES_STATUS } from 'app/shared/model/inventories-storage.model';
import {
  inventoriesSchema,
  InventoriesSchema,
} from 'app/validation/inventories.validation';
import { useContext, useEffect } from 'react';
import { FormProvider, useForm } from 'react-hook-form';
import { DateObject } from 'react-multi-date-picker';
import { useNavigate, useParams } from 'react-router';
import { useSearchParams } from 'react-router-dom';
import InventoriesForm from './components/inventories-form';
import { InventoriesStorageContext } from './inventories-storage-provider';
import InventoriesApproveModal from './modals/inventories-approve-modal';
import InventoriesApproveSuccessModal from './modals/inventories-approve-success-modal';
import InventoriesAttachmentModal from './modals/inventories-attachment-modal';
import InventoriesConfirmImport from './modals/inventories-confirm-import';
import InventoriesConfirmReject from './modals/inventories-confirm-reject';
import InventoriesConfirmReview from './modals/inventories-confirm-review';
import InventoriesConfirmImportSuccessModal from './modals/inventories-confirm-success-modal';
import InventoriesRejectSuccessModal from './modals/inventories-reject-success-modal';
import InventoriesReviewSuccessModal from './modals/inventories-review-success-modal';
import InventoriesUpdateSuccessModal from './modals/inventories-update-success-modal';
import InventoriesPrint from './components/InventoriesPrint/inventories-print';
import AuthGuard from 'app/components/guards/auth-guard';

const { useGetEmployeesQuery } = useEmployee;

const InventoriesUpdateForm = () => {
  const { id } = useParams();
  const navigate = useNavigate();

  const [searchParams, setSearchParams] = useSearchParams();
  const warehouseImportType = searchParams.get('warehouse');

  const titlePrefix =
    warehouseImportType ===
    (InventoriesWarehouse?.WAREHOUSE_DEPRECIATION_IMPORT as string)
      ? 'TS - CCDC'
      : '';

  const {
    toggleApprove,
    toggleReject,
    toggleReview,
    setSelectedRecord,
    toggleConfirm,
  } = useContext(InventoriesStorageContext);

  const { data } = useGetInventoriesById(id);

  const account = useAppSelector(state => state.authentication.account);
  const { data: employees } = useGetEmployeesQuery();

  const methods = useForm<InventoriesSchema>({
    resolver: zodResolver(inventoriesSchema),
    defaultValues: {
      createdAt: new DateObject(),
      requestApprovals: [],
    },
  });

  const { setValue } = methods;

  useEffect(() => {
    if (data) {
      setValue('code', data.code);
      setValue('incomingWarehouseId', data.incomingWarehouseId);
      setValue('inventoriesTypeId', data.inventoriesTypeId);
      setValue('dateCreate', new DateObject(data.dateCreate).add(7, 'hours'));
      setValue('isInvoice', data.isInvoice);
      setValue('isNoReview', !data.isReview);
      setValue('note', data.note);
      setValue('status', data.status);
      setValue('customerId', data.customerId);
      setValue('taxCode', data.customer?.taxCode);
      setValue('address', data.customer?.address);
      setValue('shipper', data.attribute?.shipper);
      setValue('shipperPhone', data.attribute?.shipperPhone);
      setValue(
        'inventoriesDetails',
        data.inventoriesDetails?.map(item => ({
          id: item.id,
          code: item.code,
          itemId: item.itemId,
          quantity: item.quantity?.toString(),
          price: item.price?.toString(),
          note: item.note,
          uomId: item.item?.uomId,
          totalPrice: item.totalPrice?.toString(),
        })),
      );

      setValue('invoiceId', data.invoiceId);
      setValue('purchaseContractId', data.purchaseContractId);
      setValue('productionId', data.productionId);
      setValue('employeeId', data?.employeeId);

      setValue('invoices', data?.invoices ?? []);
      setValue('invoice', data?.invoice ?? null);

      const empSelected = employees?.data?.find(x => x.id === data?.employeeId);
      const empWorkspace = empSelected
        ? `${empSelected.workspace.normalizedName} - ${empSelected.workspace?.name}`
        : '';

      setValue('workspaceName', empWorkspace);
      setValue('requestApprovals', data?.requestApprovals);

      const existAttachments = data?.file?.map(x => ({
        ...x,
        createdAt: new Date(x.createdAt),
      }));
      setValue('file', existAttachments);

      setValue(
        'createdByName',
        [
          data?.createdByEmployee?.employeeCode,
          data?.createdByEmployee?.fullName,
        ].join(' - '),
      );
    }
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

  const handleConfirm = () => {
    toggleConfirm();
    setSelectedRecord(data?.id);
  };

  const isDisabledReview =
    account &&
    (data?.status === INVENTORIES_STATUS.NEW ||
      data?.status === INVENTORIES_STATUS.REJECTED) &&
    data?.createdBy === account.id;

  const isDisabledReject =
    account &&
    data?.status === INVENTORIES_STATUS.WAITING_APPROVED &&
    data?.requestApprovals
      ?.filter(x => !x.approvedSign)
      ?.map(x => x.employeeId)
      .includes(account.id);

  const isDisabledApprove =
    account &&
    data?.status === INVENTORIES_STATUS.WAITING_APPROVED &&
    data?.requestApprovals
      ?.filter(x => !x.approvedSign)
      ?.map(x => x.employeeId)
      .includes(account.id);

  return (
    <>
      <FormProvider {...methods}>
        <CardV2
          header={
            <Flex justify="space-between" align="center">
              <Typography level={4}>
                Cập nhật phiếu nhập kho {titlePrefix}
              </Typography>
              <Flex align="center" gap={10}>
                  <ButtonV2
                    onClick={() =>
                      navigate(PATH.INVENTORIES_STORAGE + location.search)
                    }
                  >
                    Đóng
                  </ButtonV2>

                <AuthGuard permissionKey='LOGISTICS_INVENTORIES_STORAGE.EDIT'>
                  {(data?.status === INVENTORIES_STATUS.NEW ||
                    data?.status === INVENTORIES_STATUS.REJECTED) && (
                    <ButtonV2 onClick={handleReview} disabled={!isDisabledReview}>
                      Trình duyệt
                    </ButtonV2>
                  )}
  
                  {data?.status === INVENTORIES_STATUS.APPROVED && (
                    <ButtonV2 onClick={handleConfirm} disabled={isDisabledReview}>
                      Xác nhận nhập kho
                    </ButtonV2>
                  )}
  
                  {data?.status === INVENTORIES_STATUS.WAITING_APPROVED && (
                    <>
                      <ButtonV2
                        onClick={handleReject}
                        disabled={!isDisabledReject}
                        color="red"
                      >
                        Từ chối
                      </ButtonV2>
                      <ButtonV2
                        onClick={handleApprove}
                        disabled={!isDisabledApprove}
                      >
                        Duyệt
                      </ButtonV2>
                    </>
                  )}
  
                  <ButtonV2
                    color="blue"
                    variant="solid"
                    form={FORM.INVENTORIES}
                    type="submit"
                    disabled={
                      data?.status === INVENTORIES_STATUS.APPROVED ||
                      data?.status === INVENTORIES_STATUS.WAITING_APPROVED ||
                      data?.status === INVENTORIES_STATUS.CANCELLED ||
                      data?.status === INVENTORIES_STATUS.COMPLETED
                    }
                  >
                    Lưu
                  </ButtonV2>
                </AuthGuard>
                <AuthGuard permissionKey='LOGISTICS_INVENTORIES_STORAGE.EXPORT'>
                  <ButtonPrint
                    onClick={() => {
                      if (data?.id) {
                        setSearchParams({
                          printId: data?.id,
                          printType: 'INVENTORIES',
                        });
                        setTimeout(() => window.print(), 500);
                      }
                    }}
                    disabled={!data?.id}
                  />
                </AuthGuard>
              </Flex>
            </Flex>
          }
        >
          <InventoriesForm type="update" />
        </CardV2>

        <InventoriesAttachmentModal />
      </FormProvider>
      <InventoriesApproveModal />
      <InventoriesApproveSuccessModal
        directUrl={PATH.INVENTORIES_STORAGE + location.search}
      />
      <InventoriesConfirmReject />
      <InventoriesRejectSuccessModal
        directUrl={PATH.INVENTORIES_STORAGE + location.search}
      />
      <InventoriesConfirmReview />
      <InventoriesReviewSuccessModal
        directUrl={PATH.INVENTORIES_STORAGE + location.search}
      />
      <InventoriesUpdateSuccessModal />

      <InventoriesConfirmImport />
      <InventoriesConfirmImportSuccessModal
        directUrl={PATH.INVENTORIES_STORAGE + location.search}
      />
    </>
  );
};

export default InventoriesUpdateForm;
