import { DatePicker } from 'antd';
import locale from 'antd/es/date-picker/locale/vi_VN';
import { DATE_FORMAT } from 'app/constants/common';
import 'dayjs/locale/vi';
import { ComponentProps, forwardRef } from 'react';

type Props = ComponentProps<typeof DatePicker>

const AntdDatePicker = (props: Props) => {
  return (
    <DatePicker
      {...props}
      locale={locale}
      className="antd-date-picker-custom"
      placeholder={props?.placeholder || 'Chọn'}
      format={props?.format || DATE_FORMAT.DATE}
    />
  );
}

export default forwardRef(AntdDatePicker)
