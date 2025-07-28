import Flex from 'app/components/flex/flex';
import { DATE_FORMAT } from 'app/constants/common';
import {
  MANUFACTURE_ORDER_STATUS,
  MANUFACTURE_ORDER_TYPE,
} from 'app/shared/model/enumerations/production-command.model';
import { ManufactureOrderSchema } from 'app/validation/manufacture-order.validation';
import dayjs from 'dayjs';
import { useFormContext } from 'react-hook-form';
import { useNavigate } from 'react-router';
import './style.scss';
import { convertCurrency } from 'app/shared/util/format';

interface TabDashboardItem5Props {
  directUrl?: string;
}
export const TabDashboardItem5 = (props: TabDashboardItem5Props) => {
  const { directUrl = '#' } = props;
  const navigate = useNavigate();

  const methods = useFormContext<ManufactureOrderSchema>();
  const { getValues, watch } = methods;

  const statusWatch = watch('status');
  const typeWatch = watch('typePage');

  return (
    <>
      <Flex
        direction="column"
        className="tab-dashboard_item5"
        justify="space-between"
        gap={12}
      >
        <div className="tab-dashboard_item5_top">
          <div className="tab-dashboard_item5_top_item">
            <span className="text_title">Mã</span>
            <span className="text_content">{getValues('name')}</span>
          </div>
          <div className="tab-dashboard_item5_top_item">
            <span className="text_title">
              {getValues('orderDeliveryDate')
                ? 'Thời hạn giao hàng'
                : 'Tháng/Năm'}
            </span>
            <span className="text_content">
              {getValues('orderDeliveryDate')
                ? dayjs(getValues('orderDeliveryDate')).format(DATE_FORMAT.DATE)
                : getValues('productionStandardDueDate')}
            </span>
          </div>

          {typeWatch ===
            (MANUFACTURE_ORDER_TYPE.MANUFACTURE_ORDER_BY_ORDER as string) && (
            <div className="tab-dashboard_item5_top_item">
              <span className="text_title">Loại hàng</span>
              <span className="text_content">{getValues('itemName')}</span>
            </div>
          )}

          <div className="tab-dashboard_item5_top_item">
            <span className="text_title">Khối lượng SX</span>
            <span className="text_content">
              {convertCurrency(getValues('productionQuantity') ?? 0, false)}
            </span>
          </div>
          <div className="tab-dashboard_item5_top_item">
            <span className="text_title">Thời gian SX</span>
            <span className="text_content">
              {dayjs(getValues('fromDate')?.toDate()).format(DATE_FORMAT.DATE)}{' '}
              - {dayjs(getValues('toDate')?.toDate()).format(DATE_FORMAT.DATE)}
            </span>
          </div>

          {typeWatch ===
            (MANUFACTURE_ORDER_TYPE.MANUFACTURE_ORDER_BY_STANDARD as string) && (
            <div className="tab-dashboard_item5_top_item"></div>
          )}

          <div className="tab-dashboard_item5_top_item_icon">
            <img
              src={`content/images/vuesax/linear/production-dashboard-icon-10.svg`}
              alt={`icon10`}
              style={{
                cursor:
                  statusWatch === (MANUFACTURE_ORDER_STATUS.NEW as string)
                    ? 'pointer'
                    : 'not-allowed',
              }}
              onClick={() => {
                if (statusWatch === (MANUFACTURE_ORDER_STATUS.NEW as string))
                  navigate(directUrl);
              }}
            />
          </div>
        </div>
        <div className="tab-dashboard_item5_bottom">
          <span className="text_note">Ghi chú: </span>
          <span className="text_note_content">{getValues('note')}</span>
        </div>
      </Flex>
    </>
  );
};
