import { DatePicker, Segmented } from 'antd'
import Flex from 'app/components/flex/flex'
import { iconPath } from 'app/shared/util/format'
import dayjs from 'dayjs'
import { useEffect, useRef, useState } from 'react'
import { ToolbarProps } from 'react-big-calendar'
import './Toolbar.scss'
import DayPicker from 'app/components/day-picker/DayPicker'
import { startAndEndOfCalendar } from 'app/shared/util/date-utils'

const views = {
  'Ngày': 'day',
  'Tuần': 'week',
  'Tháng': 'month'
}

const keys = {
  day: 'Ngày',
  week: 'Tuần',
  month: 'Tháng',
};

type Props = ToolbarProps & {
  setQuery: React.Dispatch<any>
}

const Toolbar = (props: Props) => {
  const {
    onNavigate,
    onView,
    date,
    view,
    setQuery
  } = props

  const [labelDate, setLabelDate] = useState<{
    'Ngày': string,
    'Tuần': string,
    'Tháng': string,
    key: string
  }>({
    'Ngày': '',
    'Tuần': '',
    'Tháng': '',
    key: 'Tháng'
  })
  const onSegmentedChange = (value: string) => {
    onView(views[value])
  }

  const onNavigateAction = (action: 'PREV' | 'NEXT') => () => {
    onNavigate(action)
    let { start, end } = startAndEndOfCalendar(dayjs(date).add(action === 'PREV' ? -1 : 1, 'month'))
    setQuery(pre => ({
      ...pre,
      calendar: {
        deliveryDate: start.toISOString(),
        expectedReceiveDate: end.toISOString()
      }
    }))
  }

  useEffect(() => {
    const month = dayjs(date).format('MM')
    const day = dayjs(date).date()
    const week = dayjs.duration({ months: +month }).weeks()
    setLabelDate({
      'Ngày': `Ngày ${day} Tháng ${month}`,
      'Tuần': `Tuần ${week}`,
      'Tháng': `Tháng ${month}`,
      key: keys[view]
    })
  }, [props])

  return (
    <Flex justify="end" align="center" style={{ marginBottom: '17px' }}>
      <div className="calendar__month">
        <button
          className="calendar__month--btn calendar__month--previous"
          onClick={onNavigateAction('PREV')}
        >
          <img src={iconPath('previous.svg')} alt="previous" />
        </button>
        <button className='calendar__month--content'>{labelDate?.[labelDate.key]}</button>
        <button
          className="calendar__month--btn calendar__month--next"
          onClick={onNavigateAction('NEXT')}
        >
          <img src={iconPath('next.svg')} alt="previous" />
        </button>
      </div>

      <Segmented<string>
        className="calendar__segmented"
        size="large"
        options={['Tháng']}
        onChange={onSegmentedChange}
        defaultValue="Tháng"
      />
    </Flex>
  );
}

export default Toolbar
