import { zodResolver } from "@hookform/resolvers/zod"
import ButtonAdd from "app/components/ButtonV2/ButtonAdd"
import ButtonColumn from "app/components/ButtonV2/ButtonColumn"
import ButtonV2 from "app/components/ButtonV2/ButtonV2"
import CardV2 from "app/components/CardV2/CardV2"
import Flex from "app/components/flex/flex"
import Modal from "app/components/modal/modal"
import { Typography } from "app/components/typography/typography"
import useCustomers from "app/hooks/use-customers"
import { iconPath } from "app/shared/util/format"
import classNames from "classnames"
import dayjs from "dayjs"
import { useEffect } from "react"
import { FormProvider, SubmitHandler, useForm } from "react-hook-form"
import { createSearchParams, NavLink } from "react-router-dom"
import { useDeleteDeliverySchedule, useDeliveryScheduleList, useDeliveryScheduleMutation } from "./apis/api"
import Calendar from "./Calendar/Calendar"
import './DeliverySchedule.scss'
import Filter from "./modal/Filter"
import ScheduleList from "./modal/ScheduleList"
import { ScheduleModal } from "./modal/ScheduleModal"
import DeliveryTable from "./Table/DeliveryTable"
import useDeliverySearchParams, { mode, purchase, sell, type } from "./useDeliverySearchParams"
import { convertValues } from "./utils/converteValues"
import { deliveryScheduleSchema, DeliveryScheduleSchema } from "./validations/delivery-schduler.validate"
import useModalRedux from "app/hooks/use-modal-redux"
import AuthGuard from "app/components/guards/auth-guard"
import { useAppSelector } from "app/config/store"
import { isHasPermission } from "app/constants/common"

const {
  useGetCustomerById
} = useCustomers

type Props = {}

const DeliverySchedule = (props: Props) => {
  const {
    isTable, isPurchase, isSell, searchParams, isCreate,
    isOpenModal, isCalendar, isEdit, onToggleModal, isMany,
    onSwitchScreen
  } = useDeliverySearchParams()

  const authorities = useAppSelector(
    state => state.authentication.account.authorities,
  );

  const {
    apiResponse,
    filter: { query, setQuery }
  } = useDeliveryScheduleList()

  const { handleToggleModal, closeModal } = useModalRedux()

  const methods = useForm<DeliveryScheduleSchema>({
    resolver: zodResolver(deliveryScheduleSchema),
    defaultValues: {
      type: 'CREATE',
      body: {
        deliveryDetail: []
      }
    }
  })
  const { reset, getValues, handleSubmit, watch, formState: { errors }, setValue } = methods
  const scheduleId = watch('body.id')
  const contractCode = watch('body.contractCode')
  const supplierCode = watch('body.supplierCode')
  const itemCode = watch('body.itemCode')
  const orderCode = watch('body.orderCode')
  const itemId = watch('body.itemId')

  const purchaseTitleModal = `${contractCode} - ${itemCode}`
  const sellTitleModal = `${orderCode} - ${itemCode}`
  const editTitleModal = isEdit && isPurchase ? purchaseTitleModal : sellTitleModal
  const createTitleModal = isCreate && isPurchase ? 'Tạo mới lịch mua hàng' : 'Tạo mới lịch bán hàng' 

  const deliveryScheduleMutation = useDeliveryScheduleMutation(scheduleId)
  const deleteDeliveryScheduleMutation = useDeleteDeliverySchedule(scheduleId)

  const onSubmit: SubmitHandler<DeliveryScheduleSchema> = (values) => {
    deliveryScheduleMutation.mutate(convertValues(values, isCreate, isSell))
  }

  const onDelete = () => {
    onToggleModal()()
    setTimeout(() => {
      handleToggleModal({
        isOpen: true,
        cancelText: 'Trở về',
        okText: 'Xác nhận',
        title: 'Xác nhận xóa lịch',
        content: `Bạn có chắc chắn muốn xóa lịch ${isPurchase ? 'mua hàng' : 'bán hàng'} này không?`,
        onOK: () => deleteDeliveryScheduleMutation.mutate(),
        onCancel: closeModal
      })
    }, 100)
  }

  useEffect(() => {
    if (isCreate) {
      setValue('type', String(searchParams.formType).toUpperCase() as any)
    }
  }, [isCreate])

  useEffect(() => {
    !isOpenModal && reset()
  }, [isOpenModal])

  return (
    <FormProvider {...methods}>
      <CardV2
        header={
          <Flex align="center" justify="space-between">
            <Typography level={4}>Lịch giao nhận hàng</Typography>
            <AuthGuard permissionKey="DELIVERY_SCHEDULE.CREATE">
              <ButtonAdd
                text="Thêm"
                onClick={() => {
                  onToggleModal({ formType: 'create' })()
                  reset({
                    type: 'CREATE',
                    body: {
                      mode: isPurchase ? purchase : sell,
                    }
                  })
                }}
              />
            </AuthGuard>
          </Flex>
        }
      >
        <Flex direction="column" rowGap={20}>
          <Flex justify="space-between" align="center">
            <Flex columnGap={4}>
              <NavLink
                to={{
                  search: createSearchParams({
                    ...(searchParams as any),
                    [mode]: purchase,
                  }).toString(),
                }}
                className={({ isActive }) =>
                  classNames('delivery-schedule__btn-redirect', {
                    '--active': isActive && isPurchase,
                  })
                }
              >
                Mua hàng
              </NavLink>
              <NavLink
                to={{
                  search: createSearchParams({
                    ...(searchParams as any),
                    [mode]: sell,
                  }).toString(),
                }}
                className={({ isActive }) =>
                  classNames('delivery-schedule__btn-redirect', {
                    '--active': isActive && isSell,
                  })
                }
              >
                Bán hàng
              </NavLink>
            </Flex>
            <Flex columnGap={16}>
              <Filter setQuery={setQuery} query={query} />
              <ButtonColumn />
              <Flex align="center" columnGap={11}>
                <span>Chế độ xem</span>
                <NavLink
                  to={{
                    search: createSearchParams({
                      ...(searchParams as any),
                      [type]: 'table',
                    }).toString(),
                  }}
                  className={({ isActive }) =>
                    classNames('delivery-schedule__btn-mode', {
                      '--active': isActive && isTable,
                    })
                  }
                  onClick={() => {
                    setQuery(pre => ({
                      ...pre,
                      deliveryDate: dayjs().startOf('month').toISOString(),
                      expectedReceiveDate: dayjs().endOf('month').toISOString(),
                    }));
                  }}
                >
                  <img src={iconPath('table_mode.svg')} alt="table mode" />
                </NavLink>
                <NavLink
                  to={{
                    search: createSearchParams({
                      ...(searchParams as any),
                      [type]: 'calendar',
                    }).toString(),
                  }}
                  className={({ isActive }) =>
                    classNames('delivery-schedule__btn-mode', {
                      '--active': isActive && isCalendar,
                    })
                  }
                >
                  <img src={iconPath('calendar.svg')} alt="table mode" />
                </NavLink>
              </Flex>
            </Flex>
          </Flex>
          {isTable ? (
            <DeliveryTable query={query} data={apiResponse as any} />
          ) : (
            <Calendar data={apiResponse as any} setQuery={setQuery} />
          )}
        </Flex>
      </CardV2>
      {(isHasPermission(authorities, 'DELIVERY_SCHEDULE.CREATE') || 
      isHasPermission(authorities, 'DELIVERY_SCHEDULE.EDIT')) && 
      <Modal
        isOpen={isOpenModal}
        titleHeader={
          !isMany
            ? isEdit
              ? editTitleModal
              : createTitleModal
            : `Danh sách lịch ${isPurchase ? 'mua' : 'bán'} hàng`
        }
        okText="Trở về"
        cancel={false}
        onOk={onToggleModal()}
        footer={
          !isMany && (
            <Flex
              align="center"
              justify={isEdit ? 'space-between' : 'end'}
              style={{ width: '100%' }}
            >
              {isEdit && (
                <ButtonV2
                  color="red"
                  style={{ padding: '6px 37px' }}
                  onClick={onDelete}
                  disabled={!!getValues('body.actualQuantity')}
                >
                  Xóa
                </ButtonV2>
              )}
              <Flex columnGap={8}>
                <ButtonV2
                  style={{ padding: '6px 37px' }}
                  onClick={() => {
                    if (itemId) {
                      onSwitchScreen()
                    } else {
                      onToggleModal()();
                    }
                  }}
                >
                  {itemId ? 'Quay về' : 'Đóng'}
                </ButtonV2>
                <ButtonV2
                  form="delivery-schedule-form"
                  variant="primary"
                  style={{ padding: '6px 37px' }}
                  onClick={handleSubmit(onSubmit, error => {
                    console.log('error', error);
                  })}
                  isLoading={deliveryScheduleMutation?.isPending}
                >
                  Lưu
                </ButtonV2>
              </Flex>
            </Flex>
          )
        }
        toggle={onToggleModal()}
      >
        {isMany ? (
          <ScheduleList />
        ) : (
          <ScheduleModal />
        )}
      </Modal>}
    </FormProvider>
  );
}

export default DeliverySchedule
