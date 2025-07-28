import { UseQueryResult } from '@tanstack/react-query'
import { DATE_FORMAT, isHasPermission } from 'app/constants/common'
import useModalRedux from 'app/hooks/use-modal-redux'
import classNames from 'classnames'
import dayjs from 'dayjs'
import { Calendar as CalendarLib, dayjsLocalizer, ToolbarProps } from 'react-big-calendar'
import { useFormContext } from 'react-hook-form'
import { Col, Row } from 'reactstrap'
import { DeliveryScheduleCalendar } from '../Types/list'
import useDeliverySearchParams from '../useDeliverySearchParams'
import { DeliveryScheduleSchema } from '../validations/delivery-schduler.validate'
import './Calendar.scss'
import Toolbar from './Toolbar'
import { useAppSelector } from 'app/config/store'

const localizer = dayjsLocalizer(dayjs)
dayjs.locale('vi')

type Props = {
  data?: UseQueryResult<{
    data: any[];
    events: any[];
    totalRecord: number;
  }, Error>,
  setQuery: React.Dispatch<any>
}

const Calendar = (props: Props) => {
  const { data, setQuery } = props

  const authorities = useAppSelector(
    state => state.authentication.account.authorities,
  );

  const {
    onToggleModal, searchParams
  } = useDeliverySearchParams()

  const { handleToggleModal, closeModal } = useModalRedux()

  const { setValue, reset } = useFormContext<DeliveryScheduleSchema>()

  const onSelectedEvent = (selectedEvent: DeliveryScheduleCalendar) => {
    if (!isHasPermission(authorities, 'DELIVERY_SCHEDULE.EDIT')) return
    onToggleModal({
      formType: 'many',
    })()
    reset({
      body: {
        deliveryDate: selectedEvent?.start?.toISOString(),
        mode: searchParams?.mode,
        itemId: selectedEvent?.itemId,
      }
    })
  }

  return (
    <div>
      <CalendarLib
        className={classNames('delivery-schedule-calendar', {
          '': true
        })}
        localizer={localizer}
        startAccessor="start"
        endAccessor="end"
        style={{ height: 700 }}
        views={['month']}
        defaultView='month'
        components={{
          toolbar: (props: ToolbarProps) => {
            return <Toolbar {...props} setQuery={setQuery} />
          },
        }}
        popup
        showAllEvents
        selectable
        events={data?.data?.events}
        onSelectSlot={(selectSlot) => {
          if (!isHasPermission(authorities, 'DELIVERY_SCHEDULE.CREATE')) return
          onToggleModal({
            formType: 'create',
            date: dayjs(selectSlot.start).format(DATE_FORMAT.DATE),
          })()
          reset({
            type: 'CREATE',
            body: {
              deliveryDate: selectSlot.start.toISOString(),
              mode: searchParams?.mode
            }
          })
        }}
        onSelectEvent={onSelectedEvent}
        onShowMore={(events: DeliveryScheduleCalendar[], date) => {
          handleToggleModal({
            isOpen: true,
            okText: 'Trở về',
            title: `Danh sách sự kiện - ${dayjs(date).format(DATE_FORMAT.DATE)}`,
            onOK: closeModal,
            content: (
              <Row>
                {events.map((event: DeliveryScheduleCalendar) => {
                  return (
                    <Col md={6}>
                      <button 
                        className='schedule-list__btn'
                        onClick={() => onSelectedEvent(event)}
                      >
                        {event?.title}
                      </button>
                    </Col>
                  );
                })}
              </Row>
            )
          })
        }}
      />
    </div>
  );
}

export default Calendar
