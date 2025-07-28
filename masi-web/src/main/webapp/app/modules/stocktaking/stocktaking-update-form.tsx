import { zodResolver } from '@hookform/resolvers/zod';
import ButtonV2 from 'app/components/ButtonV2/ButtonV2';
import CardV2 from 'app/components/CardV2/CardV2';
import Flex from 'app/components/flex/flex';
import { Typography } from 'app/components/typography/typography';
import { useAppSelector } from 'app/config/store';
import { DEFAULT_PAGE_SIZE_NAX } from 'app/constants/common';
import { PATH } from 'app/constants/path';
import useEmployee from 'app/hooks/use-employee';
import { useGetStocktakingById } from 'app/hooks/use-stocktaking';
import { FORM } from 'app/shared/model/enumerations/form.model';
import { STOCKTAKING_STATUS } from 'app/shared/model/stocktaking.model';
import {
  stocktakingSchema,
  StocktakingSchema,
} from 'app/validation/stocktaking.validation';
import { useContext, useEffect } from 'react';
import { FormProvider, useForm } from 'react-hook-form';
import { DateObject } from 'react-multi-date-picker';
import { useNavigate, useParams } from 'react-router';
import StocktakingForm from './components/stocktaking-form';
import StocktakingApproveModal from './modals/stocktaking-approve-modal';
import StocktakingApproveSuccessModal from './modals/stocktaking-approve-success-modal';
import StocktakingConfirmReject from './modals/stocktaking-confirm-reject';
import StocktakingConfirmReview from './modals/stocktaking-confirm-review';
import StocktakingRejectSuccessModal from './modals/stocktaking-reject-success-modal';
import StocktakingReviewSuccessModal from './modals/stocktaking-review-success-modal';
import StocktakingUpdateSuccessModal from './modals/stocktaking-update-success-modal';
import { StocktakingContext } from './stocktaking-provider';
import { useSearchParams } from 'react-router-dom';
import ButtonPrint from 'app/components/ButtonV2/ButtonPrint';
import AuthGuard from 'app/components/guards/auth-guard';

const { useGetEmployeesQuery } = useEmployee;

const StocktakingUpdateForm = () => {
  const [params, setSearchParams] = useSearchParams();
  const { id } = useParams();
  const navigate = useNavigate();

  const { toggleApprove, toggleReject, toggleReview, setSelectedRecord } =
    useContext(StocktakingContext);

  const { data } = useGetStocktakingById(id);
  const { data: employees } = useGetEmployeesQuery({
    size: DEFAULT_PAGE_SIZE_NAX,
  });

  const account = useAppSelector(state => state.authentication.account);

  const methods = useForm<StocktakingSchema>({
    resolver: zodResolver(stocktakingSchema),
    defaultValues: {
      createdAt: new DateObject(),
      requestApprovals: [],
    },
  });

  const { setValue } = methods;

  useEffect(() => {
    setValue('code', data?.code);
    setValue('note', data?.note);
    setValue('checkDate', new DateObject(data?.checkDate));
    setValue('warehouseId', data?.warehouse?.id);
    setValue('status', data?.status);

    setValue(
      'itemInventories',
      data?.listInventoriesCheckDetail?.map(x => ({
        itemId: x?.itemId,
        itemCode: x?.item?.code,
        itemName: x?.item?.name,
        uomName: x?.item?.uom?.name ?? '',
        totalQty: `${x?.systemQuantity}`,
        totalQtyActual: `${x?.actualQuantity ?? 0}`,
        totalQtyDifference: `${x?.systemQuantity - x?.actualQuantity}`,
        note: x?.note,
      })),
    );

    const personApprover = [];
    if (data?.approver1) {
      const selected = employees?.data?.find(x => x.id === data?.approver1);
      personApprover.push({
        id: data?.approver1,
        code: data?.approver1Employee?.code,
        fullName: data?.approver1Employee?.fullName,
        position: selected
          ? selected?.workspace?.name
          : data?.approver1Employee?.workspaceName,
      });
    }

    if (data?.approver2) {
      const selected = employees?.data?.find(x => x.id === data?.approver2);
      personApprover.push({
        id: data?.approver2,
        code: data?.approver2Employee?.code,
        fullName: data?.approver2Employee?.fullName,
        position: selected
          ? selected?.workspace?.name
          : data?.approver2Employee?.workspaceName,
      });
    }

    if (data?.approver3) {
      const selected = employees?.data?.find(x => x.id === data?.approver3);
      personApprover.push({
        id: data?.approver3,
        code: data?.approver3Employee?.code,
        fullName: data?.approver3Employee?.fullName,
        position: selected
          ? selected?.workspace?.name
          : data?.approver3Employee?.workspaceName,
      });
    }

    setValue('inventoryPerson', personApprover ?? []);

    setValue('requestApprovals', data?.requestApprovals);
    const existAttachments = data?.attachment?.map(x => ({
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

  const isDisabledReview =
    account &&
    (data?.status === STOCKTAKING_STATUS.NEW ||
      data?.status === STOCKTAKING_STATUS.REJECTED) &&
    data?.createdBy === account.id;

  const isDisabledReject =
    account &&
    data?.status === STOCKTAKING_STATUS.WAITING_APPROVED &&
    data?.requestApprovals
      ?.filter(x => !x.approvedSign)
      ?.map(x => x.employeeId)
      .includes(account.id);

  const isDisabledApprove =
    account &&
    data?.status === STOCKTAKING_STATUS.WAITING_APPROVED &&
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
              <Typography level={4}>Cập nhật kiểm kê</Typography>
              <Flex align="center" gap={10}>
                <ButtonV2 onClick={() => navigate(PATH.STOCKTAKING)}>
                  Đóng
                </ButtonV2>

                <AuthGuard permissionKey='LOGISTICS_STOCKTAKING.EDIT'>
                  {(data?.status === STOCKTAKING_STATUS.NEW ||
                    data?.status === STOCKTAKING_STATUS.REJECTED) && (
                    <ButtonV2 onClick={handleReview} disabled={!isDisabledReview}>
                      Trình duyệt
                    </ButtonV2>
                  )}
  
                  {data?.status === STOCKTAKING_STATUS.WAITING_APPROVED && (
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
                    form={FORM.STOCKTAKING}
                    type="submit"
                    disabled={
                      data?.status === STOCKTAKING_STATUS.APPROVED ||
                      data?.status === STOCKTAKING_STATUS.WAITING_APPROVED ||
                      data?.status === STOCKTAKING_STATUS.CANCELLED
                    }
                  >
                    Lưu
                  </ButtonV2>
                </AuthGuard>
                <AuthGuard permissionKey='LOGISTICS_STOCKTAKING.EXPORT'>
                  <ButtonPrint
                    onClick={() => {
                      if (data?.id) {
                        setSearchParams({
                          printId: data?.id,
                          printType: 'STOCKTAKING',
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
          <StocktakingForm type="update" />
        </CardV2>
      </FormProvider>

      <StocktakingApproveModal />
      <StocktakingApproveSuccessModal directUrl={PATH.STOCKTAKING} />
      <StocktakingConfirmReject />
      <StocktakingRejectSuccessModal directUrl={PATH.STOCKTAKING} />
      <StocktakingConfirmReview />
      <StocktakingReviewSuccessModal directUrl={PATH.STOCKTAKING} />
      <StocktakingUpdateSuccessModal />
    </>
  );
};

export default StocktakingUpdateForm;
