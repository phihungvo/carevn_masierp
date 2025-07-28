import { UseQueryResult } from '@tanstack/react-query';
import { Spin } from 'antd';
import EllipsisParagraph from 'app/components/ellipsis-paragraph/ellipsis-paragraph';
import Flex from 'app/components/flex/flex';
import { TableColumns } from 'app/components/table-v2/Table';
import Tooltip from 'app/components/tooltip/tooltip';
import { durationMonth } from 'app/shared/util/date-utils';
import { getDateRangeCalendar } from 'app/shared/util/helper';
import classNames from 'classnames';
import dayjs from 'dayjs';
import { useEffect, useMemo, useState } from 'react';
import { useFormContext } from 'react-hook-form';
import { Table } from 'reactstrap';
import '../../../components/table-v2/Table.scss';
import { TableDeliverySchedule } from '../Types/list';
import { ScheduleModalData } from '../Types/modal';
import useDeliverySearchParams from '../useDeliverySearchParams';
import { DeliveryScheduleSchema } from '../validations/delivery-schduler.validate';
import './DeliveryTable.scss';
import { convertCurrency } from 'app/shared/util/format';
import { isEmpty, sumBy } from 'lodash';
import { DATE_FORMAT, isHasPermission } from 'app/constants/common';
import { useAppSelector } from 'app/config/store';

type Props = {
  query: any;
  data: UseQueryResult<{
    data: any[];
    events: any[];
    totalRecord: number;
  }, Error>
}

const DeliveryTable = (props: Props) => {
  const { query, data } = props;

  const authorities = useAppSelector(
    state => state.authentication.account.authorities,
  );

  const {
    onToggleModal, isSell, isPurchase, searchParams, isCreate
  } = useDeliverySearchParams()

  const [width, setWidth] = useState<number>(2300)

  const dateRange = useMemo(() => {
    let start = query?.table?.['deliveryDate'];
    let end = query?.table?.['expectedReceiveDate'];
    if (start && end) {
      start = dayjs(start)
      end = dayjs(end)
      let months = durationMonth(start, end)
      let days = getDateRangeCalendar(start, end)
      setWidth(1276 + days.length * 120)
      return { months, days }
    }
    return { months: [], days: [] };
  }, [query?.table?.['deliveryDate'], query?.table?.['expectedReceiveDate']]);

  console.log('dateRange', dateRange);

  const dayInMonth = {
    total: dayjs().daysInMonth(),
    today: dayjs().get('dates'),
    month: dayjs().get('month'),
  }

  const { getValues, setValue, reset, watch } = useFormContext<DeliveryScheduleSchema>()
  console.log('fullValue', watch());

  const columns: TableColumns<TableDeliverySchedule> = [
    {
      header: {
        render: 'Mua hàng'
      },
      body: {
        td_class: 'text-center vertical-middle --sticky-one-column',
        render: ({ data }) => {
          return (
            <Tooltip
              label={data?.itemName}
              target={`mua-hang-${data?.id}`}
            >
              <EllipsisParagraph
                text={data?.itemName}
                width={100}
                id={`mua-hang-${data?.id}`}
              />
            </Tooltip>
          );
        }
      }
    },
    {
      header: {
        render: 'Đối tác'
      },
      body: {
        td_class: 'text-center vertical-middle --sticky-two-column',
        render: ({ data }) => {
          return (
            <Tooltip
              label={isSell ? data?.['customerName'] : data?.supplierName}
              target={`doi-tac-${data?.id}`}
            >
              <EllipsisParagraph
                text={isSell ? data?.['customerName'] : data?.supplierName}
                width={100}
                id={`doi-tac-${data?.id}`}
              />
            </Tooltip>
          );
        }
      }
    },
    {
      header: {
        render: 'Mã hợp đồng'
      },
      body: {
        td_class: 'text-center vertical-middle',
        render: ({ data }) => {
          return (
            <Tooltip
              label={isSell ? data?.['contractName'] : data?.contractCode}
              target={`ma-hop-dong-${data?.id}`}
            >
              <EllipsisParagraph
                text={isSell ? data?.['contractName'] : data?.contractCode}
                width={100}
                id={`ma-hop-dong-${data?.id}`}
              />
            </Tooltip>
          );
        }
      }
    },
    {
      header: {
        render: 'SL HD'
      },
      body: {
        td_class: 'text-center vertical-middle',
        render: ({ data }) => data?.contractQuantity ? convertCurrency(data?.contractQuantity) : ''
      }
    },
    {
      header: {
        render: 'Đã nhận'
      },
      body: {
        td_class: 'text-center vertical-middle',
        render: ({ data }) => data?.received ? convertCurrency(data?.received) : ''
      }
    },
    {
      header: {
        render: 'Còn lại'
      },
      body: {
        td_class: 'text-center vertical-middle',
        render: ({ data }) => data?.remain ? convertCurrency(data?.remain) : ''
      }
    },
    {
      header: {
        render: 'Kế hoạch nhập'
      },
      body: {
        td_class: 'text-center vertical-middle',
        render: ({ data }) => data?.planningImport ? convertCurrency(data?.planningImport) : ''
      }
    },
    {
      header: {
        render: 'Nợ hàng'
      },
      body: {
        td_class: 'text-center vertical-middle',
        render: ({ data }) => data?.outstandingQuantity ? convertCurrency(data?.outstandingQuantity) : ''
      }
    },
    ...dateRange?.days?.map((record) => {
      return {
        body: {
          td_class: classNames('table-v2__delivery-schedule--day --content text-center vertical-middle', {
            '--today': record?.isToday
          }),
          render: ({ data }: { data: TableDeliverySchedule, index: number }) => {
            let isMatchingDate = isPurchase 
              ? data?.deliveryDate === record?.format 
              : dayjs(data?.deliveryDate).format(DATE_FORMAT.YEAR_DATE) === record?.format;
            let isBgYellow = false;
            let isTextRed = false;
            let quantity = data?.expectedQuantity
            let actualQuantity = data?.actualQuantity
            let sl = quantity;
            if (quantity !== actualQuantity) {
              isBgYellow = true
            }
            if (quantity && !actualQuantity) {
              isTextRed = true
            }
            if (actualQuantity) {
              isTextRed = false
              sl = actualQuantity
            }
            return (
              <button
                type="button"
                className={classNames("delivery-schedule__table-btn table-v2__delivery-schedule--day", {
                  '--today': record?.isToday,
                  '--bg-yellow': isBgYellow && isMatchingDate,
                  '--text-red': isTextRed && isMatchingDate,
                })}
                onClick={() => {
                  if (!isHasPermission(
                    authorities, 
                    isMatchingDate ? 'DELIVERY_SCHEDULE.EDIT' : 'DELIVERY_SCHEDULE.CREATE'
                  )) return
                  onToggleModal({ formType: isMatchingDate ? 'update' : 'create' })()
                  reset({
                    type: isMatchingDate ? 'UPDATE' : 'CREATE',
                    body: {
                      mode: searchParams?.mode,
                      contractCode: data?.contractCode,
                      id: data?.id,
                      itemCode: data?.itemCode,
                      supplierCode: data?.supplierCode,
                      contractId: isMatchingDate ? data?.contractId : undefined,
                      orderId: isMatchingDate ? data?.orderId : undefined,
                      orderCode: data?.['orderCode'],
                      deliveryDate: isMatchingDate ? data?.deliveryDate : record?.iso,
                      quantity: isMatchingDate ? data?.expectedQuantity : undefined,
                      actualDeliveryDate: isMatchingDate ? data?.['actualDeliveryDate'] : undefined,
                      actualQuantity: isMatchingDate ? data?.['actualQuantity'] : undefined,
                      deliveryLocation: isMatchingDate ? data?.['address'] : undefined,
                      note: isMatchingDate ? data?.['note'] : undefined,
                      deliveryDetail: isMatchingDate
                      ? [{ 
                          contractQuantity: data?.contractQuantity,
                          deliveryQuantity: data?.expectedQuantity,
                          id: data?.itemId,
                          isChecked: true,
                          name: data?.itemName,
                        }]
                      : []
                    }
                  } as any)
                }}
              >
                {isMatchingDate && convertCurrency(sl)}
              </button>
            );
          }
        }
      }
    })
  ]

  return (
    <Table
      className="table-v2 table-v2--fixed table-v2__delivery-schedule"
      responsive
      data-border
      style={{ width: `${width}px`, borderCollapse: 'separate', borderSpacing: 0 }}
    >
      <thead>
        <tr>
          <th colSpan={2} style={{ width: '284px' }} className='--sticky-one-column'>Mua hàng</th>
          <th rowSpan={2} style={{ width: '142px' }} className="text-center vertical-middle">
            Mã hợp đồng
          </th>
          <th rowSpan={2} style={{ width: '170px' }} className="text-center vertical-middle">
            <div>SL theo</div>
            <div>HĐ</div>
          </th>
          <th rowSpan={2} style={{ width: '170px' }} className="text-center vertical-middle">
            Đã nhận
          </th>
          <th rowSpan={2} style={{ width: '170px' }} className="text-center vertical-middle">
            Còn lại
          </th>
          <th rowSpan={2} style={{ width: '170px' }} className="text-center vertical-middle">
            <div>Kế hoạch</div>
            <div>nhập</div>
          </th>
          <th rowSpan={2} style={{ width: '170px' }} className="text-center vertical-middle">
            Nợ hàng
          </th>
          {dateRange?.months?.map((item, index) => (
            <th
              key={`calendar-months-range-${index}`}
              className={classNames('text-center', {
                '--today': index + 1 === dayInMonth.today,
              })}
              colSpan={item.sizeDay + 1}
            >
              {item.labelMonth}
            </th>
          ))}
        </tr>
        <tr>
          <th style={{ width: '142px' }} className="vertical-middle --sticky-one-column">Hàng hóa</th>
          <th style={{ width: '142px' }} className="vertical-middle --sticky-two-column">Đối tác</th>
          {dateRange?.days?.map((item, index) => (
            <th
              key={`delivery-schedule-th-${index}`}
              style={{ width: '120px' }}
              className={classNames(
                'text-center table-v2__delivery-schedule--day',
                {
                  '--today': item?.isToday,
                  '--weekend': item.shortName === 'CN',
                },
              )}
            >
              <Flex direction="column" justify="center" rowGap={5}>
                <span>{item?.shortName}</span>
                <span>{item?.dayInMonth}</span>
              </Flex>
            </th>
          ))}
        </tr>
      </thead>
      <tbody>
        {isEmpty(data?.data?.data) && (
          <tr>
            <td colSpan={columns.length} className="text-center">
              {data?.isLoading ? <Spin /> : 'Không có dữ liệu'}
            </td>
          </tr>
        )}
        {!isEmpty(data?.data?.data) && data?.data?.data?.map((item, rootIndex) => (
          <tr key={`calendar-tbody-tr-${rootIndex}`}>
            {columns?.map((column, index) => {
              return (
                <td
                  className={column?.body?.td_class}
                  key={`calendar-tbody-tr-td-${index}`}
                >
                  {column.body.render({ data: item as any, index })}
                </td>
              );
            })}
          </tr>
        ))}
        {!!data?.data?.data?.length && (
          <tr key="calendar-tbody-tr-last-row">
            <td colSpan={2} className='--sticky-one-column'>Tổng cộng</td>
            <td />
            <td className="text-center vertical-middle --last-row">
              {convertCurrency(data?.data?.['lastRow']?.contractQuantity)}
            </td>
            <td className="text-center vertical-middle --last-row">
              {convertCurrency(data?.data?.['lastRow']?.received)}
            </td>
            <td className="text-center vertical-middle --last-row">
              {convertCurrency(data?.data?.['lastRow']?.remain)}
            </td>
            <td className="text-center vertical-middle --last-row">
              {convertCurrency(data?.data?.['lastRow']?.planningImport)}
            </td>
            <td className="text-center vertical-middle --last-row">
              {convertCurrency(data?.data?.['lastRow']?.outstandingQuantity)}
            </td>
            {dateRange?.days?.map((record, index) => {
              return (
                <td className="text-center vertical-middle --last-row">
                  {convertCurrency(
                    data?.data?.data?.reduce((acc, data) => {
                      let isMatchingDate = isPurchase 
                        ? data?.deliveryDate === record?.format 
                        : dayjs(data?.deliveryDate).format(DATE_FORMAT.YEAR_DATE) === record?.format;
                      let quantity = data?.expectedQuantity;
                      let actualQuantity = data?.actualQuantity;
                      let sl = quantity;
                      if (actualQuantity) {
                        sl = actualQuantity;
                      }
                      return isMatchingDate ? acc + sl : acc;
                    }, 0),
                  )}
                </td>
              );
            })}
          </tr>
        )}
      </tbody>
    </Table>
  );
}

export default DeliveryTable
