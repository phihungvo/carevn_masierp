import { Col, Row, Spin } from 'antd';
import Flex from 'app/components/flex/flex';
import { useEffect } from 'react';
import { useFormContext } from 'react-hook-form';
import { useScheduleList } from '../apis/api';
import useDeliverySearchParams from '../useDeliverySearchParams';
import { DeliveryScheduleSchema } from '../validations/delivery-schduler.validate';
import './ScheduleList.scss';

type Props = {};

const ScheduleList = (props: Props) => {
  const { 
    onSwitchScreen, onToggleModal, isPurchase, searchParams
  } = useDeliverySearchParams();

  const { watch, setValue, reset } = useFormContext<DeliveryScheduleSchema>();
  const deliveriedDate = watch('body.deliveryDate');
  const itemId = watch('body.itemId');

  const itemsQuery = useScheduleList(itemId, deliveriedDate);

  let isArray = isPurchase ? Array.isArray(itemsQuery?.data?.data) : Array.isArray(itemsQuery?.data);
  let isLength = isPurchase ? !!itemsQuery?.data?.data?.length : !!itemsQuery?.data?.length;
  let dataMap = isPurchase ? itemsQuery?.data?.data : itemsQuery?.data;

  const onClick = (id: string) => () => {
    onSwitchScreen();
    let selectedItem = dataMap?.find(item => item?.id === id);
    if (!selectedItem) return;
    reset(
      {
        type: 'UPDATE',
        body: {
          mode: searchParams?.mode,
          id: selectedItem?.id,
          contractCode: selectedItem?.contractCode,
          itemCode: selectedItem?.contractMaterial?.code,
          supplierCode: selectedItem?.contractMaterial?.supplier?.code,
          itemId,
          deliveryDate: selectedItem?.deliveryDate,
          quantity: selectedItem?.quantity,
          actualDeliveryDate: selectedItem?.['actualDeliveryDate'],
          actualQuantity: selectedItem?.['actualQuantity'],
          deliveryLocation: selectedItem?.['address'],
          note: selectedItem?.['note'],
          orderId: selectedItem?.orderId,
        },
      },
      { keepDirtyValues: true },
    );
  };

  useEffect(() => {
    !deliveriedDate && onToggleModal()();
  }, [deliveriedDate]);

  if (itemsQuery?.isLoading) {
    return (
      <Flex direction='column' gap={5} justify='center' align='center'>
        <Spin />
        <span>Đang tải dữ liệu</span>
      </Flex>
    )
  }

  return (
    <Row gutter={[10, 10]}>
      {isArray && isLength ? (
        dataMap?.map((item, index) => {
          console.log('item', item);
          return (
            <Col span={12}>
              <button
                className="schedule-list__btn"
                key={index}
                onClick={onClick(item?.id)}
              >
                {`${item?.contractMaterial?.name} - ${item?.quantity}`}
              </button>
            </Col>
          );
        })
      ) : (
        <div>Không có dữ liệu hoặc có lỗi xảy ra. Vui lòng thử lại sau</div>
      )}
    </Row>
  );
};

export default ScheduleList;
